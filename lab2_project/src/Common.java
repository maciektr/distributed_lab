import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract class Common {
    static final String exchangeName = "MARKET_EXCHANGE";
    private static final String queueNamePrefix = "QUEUE_";

    Channel channel;

    public Common() throws IOException, TimeoutException {
        channel = getChannel();
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);

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
}
