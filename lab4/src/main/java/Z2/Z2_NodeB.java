package Z2;

import akka.actor.typed.*;
import akka.actor.typed.javadsl.Behaviors;
import akka.cluster.typed.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;


public class Z2_NodeB {

    public static Behavior<Void> create() {
        return Behaviors.setup(
                context -> {

                    ActorRef<ActorTextService.Command> actorTextService = context.spawn(ActorTextService.create(), "text_service");

                    System.out.println("Enter your message");
                    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                    String msg = in.readLine();

//                    actorTextService.tell(new ActorTextService.Request(msg));
                    actorTextService.tell(new ActorTextService.Request("Hello World"));

                    return Behaviors.receive(Void.class)
                            .onSignal(Terminated.class, sig -> Behaviors.stopped())
                            .build();
                });
    }

    public static void main(String[] args) {
        File configFile = new File("src/main/nodeB.conf");
        Config config = ConfigFactory.parseFile(configFile);
        System.out.println("Node B: config: " + config);

        ActorSystem.create(Z2_NodeB.create(), "ClusterSystem", config);
    }
}
