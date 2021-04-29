package monitoring.commands;

import akka.actor.typed.ActorRef;

public class Query implements Command {
    public final ActorRef<Command> actor;
    public final String stationName;
    public int queryId;
    public int firstSatelliteId;
    public int range;
    public int timeout;

    public Query(ActorRef<Command> actor, String stationName, int queryId, int firstSatelliteId, int range, int timeout) {
        this.actor = actor;
        this.stationName = stationName;
        this.queryId = queryId;
        this.firstSatelliteId = firstSatelliteId;
        this.range = range;
        this.timeout = timeout;
    }
}
