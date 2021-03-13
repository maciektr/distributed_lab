import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class JavaUdpServer {
    private static DatagramSocket socket = null;
    private static int portNumber = 9008;

    public static void main(String args[])
    {
        System.out.println("JAVA UDP SERVER");

        try{
            socket = new DatagramSocket(portNumber);

            while(true) {
                byte[] receiveBuffer = new byte[1024];
                Arrays.fill(receiveBuffer, (byte)0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);
                String msg = new String(receivePacket.getData());
                String resp = "Server response" + msg[0] == 'P' ? " Python":" Java";
                msg = msg.substring(1);
                log("Received msg: " + msg);
                log("Client address: " + receivePacket.getAddress().getHostAddress());
                sendMsg(resp, receivePacket.getAddress());
            }
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

    private static void sendMsg(String msg, InetAddress address) throws Exception{
        byte[] sendBuffer = "Ping Java Udp (from server)".getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, 9009);
        socket.send(sendPacket);
    }

    private static void log(String msg){
        System.out.println("Server | " + msg);
    }
}
