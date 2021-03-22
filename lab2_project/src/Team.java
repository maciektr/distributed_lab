import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Team extends Common{
    private String teamName;
    private final HashSet<String> queues = new HashSet<>();

    public Team(String teamName) throws IOException, TimeoutException {
        super();
        this.teamName = teamName;
    }

    @Override
    void getQueue(String order) throws IOException {
        if(queues.contains(getQueueName(order)))
            return;
        super.getQueue(order);
        queues.add(getQueueName(order));
    }

    public void send(String order) throws IOException {
        Message message = new Message(order, teamName);
        getQueue(order);
        channel.basicPublish(exchangeName, order, null, message.encode());

    }

    public static void main(String[] argv) throws IOException, TimeoutException {
        System.out.println("Enter team name:");
        Scanner scanner = new Scanner(System.in);
        Team team = new Team(scanner.nextLine());
        while(true){
            System.out.println("Enter your order:");
            String order = scanner.nextLine();
            if(order.equals("!quit"))
                break;
            team.send(order);
        }
    }
}
