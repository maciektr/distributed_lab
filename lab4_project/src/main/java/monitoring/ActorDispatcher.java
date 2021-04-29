package monitoring;

import akka.actor.typed.*;
import akka.actor.typed.javadsl.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import monitoring.commands.*;

public class ActorDispatcher extends AbstractBehavior<Command> {
    private final ActorRef<Command> database;
    private final Map<String, Map<Integer, SatelliteApi.Status>> queries = new HashMap<>();

    public ActorDispatcher(ActorContext<Command> context, ActorRef<Command> database) {
        super(context);
        this.database = database;
    }

    public static Behavior<Command> create(ActorRef<Command> database) {
        return Behaviors.setup(context -> new ActorDispatcher(context, database));
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(Query.class, this::onRequest)
                .onMessage(Timeout.class, this::onTimeout)
                .onMessage(ApiResult.class, this::onApiResult)
                .build();
    }

    private Behavior<Command> onRequest(Query msg) {
        String queryKey = msg.stationName + "#" + msg.queryId;
        queries.put(queryKey, new HashMap<>());
        for (int id = msg.firstSatelliteId; id < msg.firstSatelliteId + msg.range; id++) {
            getContext().spawn(
                    ActorApi.create(),
                    "api_" + msg.stationName + "_" + msg.queryId + "_" + id,
                    DispatcherSelector.fromConfig("my-dispatcher")
            ).tell(new ApiCall(getContext().getSelf(), queryKey, id));

        }
        getContext().getSystem().scheduler().scheduleOnce(
                Duration.ofMillis(msg.timeout),
                () -> getContext().getSelf().tell(new Timeout(msg.actor, queryKey, msg.queryId, msg.range)),
                getContext().getExecutionContext()
        );
        return this;
    }

    private Behavior<Command> onApiResult(ApiResult msg) {
        if (queries.containsKey(msg.queryKey)) {
            queries.get(msg.queryKey).put(msg.satelliteId, msg.status);
        }
        return this;
    }

    private Behavior<Command> onTimeout(Timeout msg) {
        double on_time = (double) queries.get(msg.queryKey).keySet().size() / (double) msg.range;
        Map<Integer, SatelliteApi.Status> resp = queries.get(msg.queryKey);
        Map<Integer, SatelliteApi.Status> result = new HashMap<>();
        for (Integer key : resp.keySet())
            if (resp.get(key) != SatelliteApi.Status.OK)
                result.put(key, resp.get(key));
        msg.actor.tell(new QueryResult(msg.queryId, result, on_time));
        queries.remove(msg.queryKey);
        database.tell(new DbSave(new ArrayList<>(result.keySet())));
        return this;
    }
}
