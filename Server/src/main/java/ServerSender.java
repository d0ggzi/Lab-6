import commands.ServerMessage;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;


public class ServerSender implements Serializable {
    private ServerMessage serverMessage;
    public ServerSender(ServerMessage serverMessage) {
        this.serverMessage = serverMessage;
    }


    public void sendMessage(DatagramChannel channel, InetSocketAddress socketAddress) throws IOException {
        try (ByteArrayOutputStream byteStream = new
                ByteArrayOutputStream(65536);
             ObjectOutputStream os = new ObjectOutputStream(new
                     BufferedOutputStream(byteStream));) {
            os.flush();
            os.writeObject(serverMessage);
            os.close();
            byte[] sendBuf = byteStream.toByteArray();
            channel.send(ByteBuffer.wrap(sendBuf), socketAddress);
        }

    }
}
