package Z2;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;

import java.util.LinkedList;
import java.util.List;

public class ActorTextService extends AbstractBehavior<ActorTextService.Command>  {
    // --- messages
    interface Command {}

    public static class ReceptionistResponse implements Command {
        final Receptionist.Listing listing;

        public ReceptionistResponse(Receptionist.Listing listing) {
            this.listing = listing;
        }
    }

    public static class Request implements Command {
        final String text;

        public Request(String text) {
            this.text = text;
        }
    }


    // --- constructor and create
    private ActorRef<Receptionist.Listing> receptionistResponseAdapter;
    private List<ActorRef<String>> workers = new LinkedList<>();

    public ActorTextService(ActorContext<ActorTextService.Command> context) {
        super(context);
        receptionistResponseAdapter = context.messageAdapter(Receptionist.Listing.class, ReceptionistResponse::new);
        context
            .getSystem()
            .receptionist()
            .tell(Receptionist.subscribe(ActorUpperCase.upperCaseServiceKey, receptionistResponseAdapter));
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(ActorTextService::new);
    }

    // --- define message handlers
    @Override
    public Receive<Command> createReceive() {

        System.out.println("creating receive for text service");

        return newReceiveBuilder()
                .onMessage(Request.class, this::onRequest)
                .onMessage(ReceptionistResponse.class, this::onReceptionistResponse)
                .build();
    }

    private Behavior<Command> onRequest(Request msg) {
        System.out.println("request: " + msg.text);
        for (ActorRef<String> worker : workers) {
            System.out.println("sending to worker: " + worker);
            worker.tell(msg.text);
        }
        return this;
    }

    private Behavior<Command> onReceptionistResponse(ReceptionistResponse msg){
        Receptionist.Listing listing = msg.listing;
        this.workers = new LinkedList<>();
        this.workers.addAll(listing.getAllServiceInstances(ActorUpperCase.upperCaseServiceKey));
        return this;
    }

}
