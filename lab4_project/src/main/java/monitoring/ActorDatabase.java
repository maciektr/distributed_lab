package monitoring;

import akka.actor.typed.*;
import akka.actor.typed.javadsl.*;

import java.sql.*;

import monitoring.commands.*;

public class ActorDatabase extends AbstractBehavior<Command> {
    Connection conn = null;

    public ActorDatabase(ActorContext<Command> context, String url) {
        super(context);

        try {
            this.conn = DriverManager.getConnection(url);
            this.conn.prepareStatement("DROP TABLE IF EXISTS errors;\n;").executeUpdate();
            this.conn.prepareStatement("CREATE TABLE errors(satellite_id int not null, errors_count int not null);").executeUpdate();
            StringBuilder sql = new StringBuilder("INSERT INTO errors(satellite_id, errors_count) VALUES");
            for (int i = 100; i < 200; i++)
                sql.append("(").append(i).append(", 0),");
            sql.append("(200,0);");
            this.conn.prepareStatement(sql.toString()).executeUpdate();
        } catch (SQLException ignored) {
        }
    }

    public static Behavior<Command> create(String url) {
        return Behaviors.setup(context -> new ActorDatabase(context, url));
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(DbQuery.class, this::onDbQuery)
                .onMessage(DbSave.class, this::onDbSave)
                .build();
    }

    private Behavior<Command> onDbSave(DbSave msg) {
        String prefix = "";
        StringBuilder query = new StringBuilder("UPDATE errors SET errors_count = errors_count + 1 WHERE satellite_id IN (");
        for (int satelliteId : msg.satellites) {
            query.append(prefix).append(satelliteId);
            prefix = ",";
        }
        query.append(");");
        try {
            this.conn.prepareStatement(query.toString()).executeUpdate();
        } catch (SQLException ignored) {
        }
        return this;
    }

    private Behavior<Command> onDbQuery(DbQuery msg) {
        String query = "SELECT errors_count FROM errors WHERE satellite_id = " + msg.satelliteId + ";";
        try {
            ResultSet rs = this.conn.prepareStatement(query).executeQuery();
            rs.next();
            msg.actor.tell(new DbQueryResult(msg.satelliteId, rs.getInt(1)));
        } catch (SQLException ignored) {
        }
        return this;
    }
}
