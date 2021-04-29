package monitoring;

import akka.actor.typed.*;
import akka.actor.typed.javadsl.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import monitoring.commands.*;

public class ActorStation extends AbstractBehavior<Command> {
    private final ActorRef<Command> dispatcher;
    private final ActorRef<Command> database;
    private final String stationName;
    private final Random random = new Random();
    private final Map<Integer, Long> queryTime;
    private int lastQueryId;

    public ActorStation(ActorContext<Command> context, String stationName, ActorRef<Command> dispatcher, ActorRef<Command> database) {
        super(context);
        this.dispatcher = dispatcher;
        this.database = database;
        this.stationName = stationName;
        this.lastQueryId = 0;
        this.queryTime = new HashMap<>();
    }

    public static Behavior<Command> create(String stationName, ActorRef<Command> dispatcher, ActorRef<Command> database) {
        return Behaviors.setup((context) -> new ActorStation(context, stationName, dispatcher, database));
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(Begin.class, this::onBegin)
                .onMessage(QueryResult.class, this::onQueryResult)
                .onMessage(DbQueryResult.class, this::onDbQueryResult)
                .build();
    }

    private void send_query() {
        this.lastQueryId++;
        dispatcher.tell(new Query(getContext().getSelf(), stationName, this.lastQueryId,
                100 + random.nextInt(50), 50, 300));
        this.queryTime.put(this.lastQueryId, System.currentTimeMillis());
    }

    private Behavior<Command> onBegin(Begin msg) {
        send_query();
        send_query();
        return this;
    }

    private Behavior<Command> onQueryResult(QueryResult msg) {
        long wait = System.currentTimeMillis() - this.queryTime.get(msg.queryId);

        synchronized (System.out) {
            System.out.print("Station: " + stationName + " On time: ");
            System.out.printf("%.2f", msg.onTime);
            System.out.println("% Wait: " + wait + "ms Errors count: " + msg.errors.size());
            for (int key : msg.errors.keySet())
                System.out.println(key + " " + msg.errors.get(key));
            System.out.println();
        }

        return this;
    }

    private Behavior<Command> onDbQueryResult(DbQueryResult msg) {
        if (msg.errorsCount != 0)
            System.out.println("Errors: " + msg.satelliteId + " : " + msg.errorsCount);
        return this;
    }

    private Behavior<Command> onDbQuery(DbQuery msg) {
        database.tell(new DbQuery(getContext().getSelf(), msg.satelliteId));
        return this;
    }
}
