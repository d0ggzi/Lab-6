import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

import commands.ClientMessage;
import commands.ServerMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientReceiver {
    private DatagramChannel channel;
    private static final Logger logger = LogManager.getLogger(ClientReceiver.class);

    public ClientReceiver(DatagramChannel channel){
        this.channel = channel;
    }

    public void getMessage() {
        ByteBuffer buffer = ByteBuffer.allocate(65536);
        try {
            logger.info("Клиент получает сообщение: ");
            Thread.sleep(500);
            channel.receive(buffer);
        } catch (SocketTimeoutException e) {
            logger.error("Сервер не отвечает, попробуйте позже!");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try (
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buffer.array()))) {
            ServerMessage serverMessage = (ServerMessage) ois.readObject();
            System.out.println(serverMessage.toString());
            buffer.clear();
        } catch (StreamCorruptedException e) {

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
