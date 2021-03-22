import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Admin{
    public static final String adminExchangeName = "ADMIN_EXCHANGE";

    public static void main(String[] argv) throws IOException, TimeoutException {
        Channel channel = Common.getChannel();
        channel.exchangeDeclare(adminExchangeName, BuiltinExchangeType.TOPIC);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Receiver legend:");
        System.out.println("1 - only teams");
        System.out.println("2 - only suppliers");
        System.out.println("3 - teams and suppliers");

        while(true) {
            System.out.println("Enter receiver:");

            String key = br.readLine();
            switch (key) {
                case "1":
                    key = "team.#";
                    break;
                case "2":
                    key = "#.supplier";
                    break;
                case "3":
                    key = "team.supplier";
                    break;
            }

            System.out.println("Enter your message:");
            String message = br.readLine();
            if(message.equals("!quit"))
                break;

            channel.basicPublish(adminExchangeName, key, null, message.getBytes(StandardCharsets.UTF_8));
        }
    }
}