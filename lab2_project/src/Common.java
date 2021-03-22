import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public abstract class Common {
    static final String exchangeName = "MARKET_EXCHANGE";
    private static final String queueNamePrefix = "QUEUE_";
    final String adminExchangeName;
    final String adminQueueName;

    Channel channel;

    public Common() throws IOException, TimeoutException {
        channel = getChannel();
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
        adminExchangeName = Admin.adminExchangeName;
        channel.exchangeDeclare(adminExchangeName, BuiltinExchangeType.TOPIC);
        adminQueueName = channel.queueDeclare().getQueue();
    }

    void getQueue(String productName) throws IOException {
        String queueName = getQueueName(productName);
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, exchangeName, productName);
        System.out.println("Queue created: " + queueName + " exchange: " + exchangeName + " key: " + productName);
    }

    static Channel getChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        return connection.createChannel();
    }

    static String getQueueName(String productName){
        return queueNamePrefix + productName;
    }

    Consumer getAdminConsumer(){
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("Admin message: " + message);
            }
        };
    }
}
