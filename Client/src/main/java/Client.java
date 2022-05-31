import java.net.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

public class Client {
    public static void main(String[] args) {
        String host = "localhost";

        try{
            DatagramChannel channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.socket().bind(new InetSocketAddress(host, Integer.parseInt(args[0])));
            ClientSender clientSender = new ClientSender(channel);
            ClientReceiver clientReceiver = new ClientReceiver(channel);

            System.out.println("Привет! Напиши help, чтобы узнать функционал");
            ClientApp app = new ClientApp();
            app.start(clientSender, clientReceiver);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
