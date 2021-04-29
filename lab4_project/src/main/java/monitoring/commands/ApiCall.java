package monitoring.commands;

import akka.actor.typed.ActorRef;

public class ApiCall implements Command {
    public final ActorRef<Command> actor;
    public final String queryKey;
    public final int satelliteId;

    public ApiCall(ActorRef<Command> actor, String queryKey, int satelliteId) {
        this.actor = actor;
        this.queryKey = queryKey;
        this.satelliteId = satelliteId;
    }

}
