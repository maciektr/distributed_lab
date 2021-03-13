import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class JavaUdpClient {

    public static void main(String args[]) throws Exception
    {
        System.out.println("JAVA UDP CLIENT");
        DatagramSocket socket = null;
        int portNumber = 9009;

        try {
            socket = new DatagramSocket(portNumber);
            InetAddress address = InetAddress.getByName("localhost");
            // byte[] sendBuffer = "Ping Java Udp (from cl)".getBytes();

            sendMsg(socket, "Ping Java Udp (from cl)");
            log("Received msg: " + getMsg(socket));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    private static void sendMsg(DatagramSocket socket, String msg){
        byte[] sendBuffer = ("J" + msg).getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, 9008);
        socket.send(sendPacket);

    }

    private static String getMsg(DatagramSocket socket) throws Exception{
        byte[] receiveBuffer = new byte[1024];
        Arrays.fill(receiveBuffer, (byte)0);
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        socket.receive(receivePacket);
        return new String(receivePacket.getData());
    }

    private static void log(String msg){
        System.out.println("Client | " + msg);
    }
}
