package monitoring.commands;

import akka.actor.typed.ActorRef;

public class DbQuery implements Command {
    public final ActorRef<Command> actor;
    public final int satelliteId;

    public DbQuery(ActorRef<Command> actor, int satelliteId) {
        this.actor = actor;
        this.satelliteId = satelliteId;
    }
}
