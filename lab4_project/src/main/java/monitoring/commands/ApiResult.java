package monitoring.commands;

import monitoring.SatelliteApi;

public class ApiResult implements Command {
    public final SatelliteApi.Status status;
    public final String queryKey;
    public final int satelliteId;

    public ApiResult(String queryKey, int satelliteId, SatelliteApi.Status status) {
        this.status = status;
        this.queryKey = queryKey;
        this.satelliteId = satelliteId;
    }
}
