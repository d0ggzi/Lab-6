import commands.ClientMessage;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientSender {
    private DatagramChannel channel;
    private int serverPort = 55667;
    private static final Logger logger = LogManager.getLogger(ClientSender.class);


    public ClientSender(DatagramChannel channel){
        this.channel = channel;
    }

    public void sendMessage(ClientMessage clientMessage) {
        try (ByteArrayOutputStream byteStream = new
                ByteArrayOutputStream(65536);
             ObjectOutputStream os = new ObjectOutputStream(new
                     BufferedOutputStream(byteStream));) {
            os.flush();
            os.writeObject(clientMessage);
            os.close();
            byte[] sendBuf = byteStream.toByteArray();
            channel.send(ByteBuffer.wrap(sendBuf), new InetSocketAddress("localhost", serverPort));
        } catch (SocketTimeoutException e){
            logger.error("Сервер не отвечает, попробуйте позже!");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
