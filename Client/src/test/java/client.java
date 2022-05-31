import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;


public class client {
    public static void main(String[] args) {
        byte arr[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int len = arr.length;
        DatagramChannel dc;
        ByteBuffer buf;
        int port = 6789;
        SocketAddress addr;

        InetAddress host;

        {
            try {
                host = InetAddress.getLocalHost();
                addr = new InetSocketAddress(host, port);
                dc = DatagramChannel.open();
                buf = ByteBuffer.wrap(arr);
                dc.send(buf, addr);

                buf.clear();
                addr = dc.receive(buf);

                for (byte j : arr) {
                    System.out.println(j);
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
