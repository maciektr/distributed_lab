import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Supplier extends Common {
    private final String[] products;

    Supplier(String declaration) throws IOException, TimeoutException {
        super();

        products = declaration.split(" ");
        for(String product : products)
            getQueue(product);

        channel.queueBind(adminQueueName, adminExchangeName, "*.supplier");
        channel.basicConsume(adminQueueName, true, getAdminConsumer());
    }
    public void consume() throws IOException {
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                Message message = new Message(body);
                System.out.println("Received: " + message.decode());
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };

        for(String product : products)
            channel.basicConsume(getQueueName(product), false, consumer);
    }

    public static void main(String[] argv) throws IOException, TimeoutException {
        System.out.println("Supplier");
        System.out.println("Declare your products");
        Scanner scanner = new Scanner(System.in);
        Supplier supplier = new Supplier(scanner.nextLine());
        supplier.consume();
    }
}
