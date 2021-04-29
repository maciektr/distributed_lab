package monitoring.commands;

public class DbQueryResult implements Command {
    public final int satelliteId;
    public final int errorsCount;

    public DbQueryResult(int satelliteId, int errorsCount) {
        this.satelliteId = satelliteId;
        this.errorsCount = errorsCount;
    }
}
