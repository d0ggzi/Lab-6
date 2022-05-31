import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * Client
 * @author donald
 * 11 апреля 2017 г.
 * 21:24:09
 */
public class DatagramChannelClient {
    //manager the channel
    private Selector selector;
    /**
     * stat Client
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException{
        DatagramChannelClient client = new DatagramChannelClient();
        client.initClient("localhost",4322);
        client.listen();
    }
    /**
     * get the Socket and finish some initial work
     * @param ip Server ip
     * @param port connect Server port
     * @throws IOException
     */
    public void initClient(String ip,int port) throws IOException{
        //get the Socket
        DatagramChannel channel = DatagramChannel.open();
        // set no blocking mode
        channel.configureBlocking(false);
        channel.socket().bind(new InetSocketAddress(ip,port));

        //get the channel manager
        this.selector = Selector.open();
        //Register the channel to manager and bind the event
        channel.register(selector,SelectionKey.OP_READ);
        // Отправляем данные на сервер
        channel.send(ByteBuffer.wrap(new String("Hello Server!").getBytes()),new InetSocketAddress("localhost", 4321));
    }
    /**
     * use asking mode to listen the event of selector
     * @throws IOException
     */
    @SuppressWarnings("rawtypes")
    public void listen() throws IOException{
        System.out.println("===========The Client is start!===========");
        while(true){
            selector.select();
            Iterator ite =  this.selector.selectedKeys().iterator();
            while(ite.hasNext()){
                SelectionKey key = (SelectionKey)ite.next();
                ite.remove();
                if (key.isReadable()) read(key);
            }

        }
    }
    /**
     * deal with the message come from the server
     * @param key
     * @throws IOException
     */
    public void read(SelectionKey key) throws IOException{
        DatagramChannel channel = (DatagramChannel) key.channel();
        ByteBuffer buf = ByteBuffer.allocate(100);
        // Используйте получение для чтения данных. Если вы используете чтение, вы должны использовать метод подключения для подключения к серверу и убедиться, что соединение установлено, так же, как и запись
        InetSocketAddress socketAddress = (InetSocketAddress) channel.receive(buf);
        System.out.println("server ip and port:"+socketAddress.getHostString()+","+socketAddress.getPort());
        byte[] data = buf.array();
        String msg = new String(data).trim();
        System.out.println("message come from server:"+msg);
        channel.close();
    }

}