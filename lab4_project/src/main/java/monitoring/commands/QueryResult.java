package monitoring.commands;

import monitoring.SatelliteApi;

import java.util.Map;

public class QueryResult implements Command {
    public final int queryId;
    public final Map<Integer, SatelliteApi.Status> errors;
    public final double onTime;

    public QueryResult(int queryId, Map<Integer, SatelliteApi.Status> errors, double onTime) {
        this.queryId = queryId;
        this.errors = errors;
        this.onTime = onTime;
    }
}
