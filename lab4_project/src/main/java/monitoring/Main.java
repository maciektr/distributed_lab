package monitoring;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.Behaviors;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.util.ArrayList;

import monitoring.commands.*;

public class Main {

    public static Behavior<Void> create() {
        return Behaviors.setup(
                context -> {
                    ActorRef<Command> database = context.spawn(ActorDatabase.create("jdbc:sqlite:src/main/db.sqlite"), "database");
                    ActorRef<Command> dispatcher = context.spawn(ActorDispatcher.create(database), "dispatcher");

                    ArrayList<ActorRef<Command>> stations = new ArrayList<>();
                    stations.add(context.spawn(ActorStation.create("first_station", dispatcher, database), "first_station"));
                    stations.add(context.spawn(ActorStation.create("second_station", dispatcher, database), "second_station"));
                    stations.add(context.spawn(ActorStation.create("third_station", dispatcher, database), "third_station"));

                    for (ActorRef<Command> station : stations)
                        station.tell(new Begin());

                    Thread.sleep(1000);
                    for (int i = 100; i < 200; i++)
                        database.tell(new DbQuery(stations.get(0), i));

                    return Behaviors.receive(Void.class).onSignal(Terminated.class, sig -> Behaviors.stopped()).build();
                });
    }

    public static void main(String[] args) {
        File configFile = new File("src/main/dispatcher.conf");
        Config config = ConfigFactory.parseFile(configFile);
        ActorSystem.create(Main.create(), "main", config);
    }
}
