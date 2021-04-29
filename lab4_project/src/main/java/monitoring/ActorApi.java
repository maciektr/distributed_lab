package monitoring;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import monitoring.commands.*;

public class ActorApi extends AbstractBehavior<Command> {
    public ActorApi(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(ActorApi::new);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(ApiCall.class, this::onApiCall)
                .build();
    }

    private Behavior<Command> onApiCall(ApiCall msg) {
        msg.actor.tell(new ApiResult(msg.queryKey, msg.satelliteId, SatelliteApi.getStatus(msg.satelliteId)));
        return this;
    }
}
