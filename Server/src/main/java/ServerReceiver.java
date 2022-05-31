import commands.*;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.Arrays;
import commands.Command;
import commands.HelpCmd;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//public void read(SelectionKey key) throws IOException{
//        DatagramChannel channel = (DatagramChannel) key.channel();
//        ByteBuffer buf = ByteBuffer.allocate(100);
//        InetSocketAddress socketAddress = (InetSocketAddress) channel.receive(buf);
//        System.out.println("client ip and port:"+socketAddress.getHostString()+","+socketAddress.getPort());
//        byte[] data = buf.array();
//        String msg = new String(data).trim();
//        System.out.println("message come from client:"+msg);
//        channel.send(ByteBuffer.wrap(new String("Hello client!").getBytes()),socketAddress);
//        channel.close();
//        }

public class ServerReceiver {
    private static DatagramPacket datagramPack;
    private static final Logger logger = LogManager.getLogger(ServerReceiver.class);


    public void getMessage(SelectionKey key, CollectionManager collectionManager) {
        try{
            DatagramChannel channel = (DatagramChannel) key.channel();
            ByteBuffer buf = ByteBuffer.allocate(65536);
            InetSocketAddress socketAddress = (InetSocketAddress) channel.receive(buf);
            byte[] data = buf.array();
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            ClientMessage clientMessage = (ClientMessage) ois.readObject();
            logger.info("Пришло сообщение от клиента: ");
            System.out.println(clientMessage.toString());
            Command cmd = clientMessage.command;
            if (cmd instanceof HelpCmd){
                collectionManager.execute((HelpCmd) cmd, channel, socketAddress);
            }if (cmd instanceof InfoCmd){
                collectionManager.execute((InfoCmd) cmd, channel, socketAddress);
            }if (cmd instanceof ShowCmd){
                collectionManager.execute((ShowCmd) cmd, channel, socketAddress);
            }if (cmd instanceof AddCmd){
                collectionManager.execute((AddCmd) cmd, clientMessage.org, channel, socketAddress);
            }if (cmd instanceof ClearCmd){
                collectionManager.execute((ClearCmd) cmd, channel, socketAddress);
            }if (cmd instanceof AddIfMinCmd){
                collectionManager.execute((AddIfMinCmd) cmd, clientMessage.org, channel, socketAddress);
            }if (cmd instanceof FilterContainsNameCmd){
                collectionManager.execute((FilterContainsNameCmd) cmd, (String) clientMessage.arg, channel, socketAddress);
            }if (cmd instanceof FilterGreaterThanType){
                collectionManager.execute((FilterGreaterThanType) cmd, Integer.parseInt(clientMessage.arg), channel, socketAddress);
            }if (cmd instanceof PrintFieldDescendingTypeCmd){
                collectionManager.execute((PrintFieldDescendingTypeCmd) cmd, channel, socketAddress);
            }if (cmd instanceof RemoveLowerCmd){
                collectionManager.execute((RemoveLowerCmd) cmd, clientMessage.org, channel, socketAddress);
            }if (cmd instanceof RemoveByIdCmd){
                System.out.println(clientMessage.arg);
                collectionManager.execute((RemoveByIdCmd) cmd, Long.parseLong(clientMessage.arg), channel, socketAddress);
            }if (cmd instanceof UpdateCmd){
                collectionManager.execute((UpdateCmd) cmd, Long.parseLong(clientMessage.arg), clientMessage.org, channel, socketAddress);
            }
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }


    }

}
