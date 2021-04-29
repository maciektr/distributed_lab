package monitoring.commands;

import akka.actor.typed.ActorRef;

public class Timeout implements Command {
    public final ActorRef<Command> actor;
    public final String queryKey;
    public final int queryId;
    public final int range;

    public Timeout(ActorRef<Command> actor, String queryKey, int queryId, int range) {
        this.actor = actor;
        this.queryKey = queryKey;
        this.queryId = queryId;
        this.range = range;
    }
}
