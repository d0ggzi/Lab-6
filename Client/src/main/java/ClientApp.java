import exceptions.UnknownCommand;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientApp {
    final ClientCommandManager cmd = new ClientCommandManager();

    public void start(ClientSender clientSender, ClientReceiver clientReceiver) {
        Scanner scan = new Scanner(System.in);
        while (true){
            try{
                String[] userCommand = scan.nextLine().trim().split(" ", 2);
                cmd.StartCommandManager(userCommand, clientSender, clientReceiver);
            }catch (UnknownCommand e){
                System.out.println(e);
            }catch (NoSuchElementException e){
                System.exit(1);
            }
        }
    }
}
