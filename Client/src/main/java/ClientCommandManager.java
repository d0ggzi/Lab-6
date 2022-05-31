import commands.*;
import exceptions.UnknownCommand;
import organizations.Organization;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Класс определения команды по введенной строке и ее передачи на выполнение
 *
 * @author Max Laptev
 * @version 1.0
 */

public class ClientCommandManager {
    private LinkedList<String> lastCommands;
    private ClientSender clientSender;
    private ClientReceiver clientReceiver;
    protected static HashMap<String, Command> allCommands;

    {
        lastCommands = new LinkedList<>();
        allCommands = new HashMap<>();
        allCommands.put("help", new HelpCmd());
        allCommands.put("info", new InfoCmd());
        allCommands.put("show", new ShowCmd());
        allCommands.put("add", new AddCmd());
        allCommands.put("update", new UpdateCmd());
        allCommands.put("remove_by_id", new RemoveByIdCmd());
        allCommands.put("clear", new ClearCmd());
        // allCommands.put("execute_script", new ScriptCmd());
        allCommands.put("add_if_min", new AddIfMinCmd());
        allCommands.put("remove_lower", new RemoveLowerCmd());
        allCommands.put("filter_contains_name", new FilterContainsNameCmd());
        allCommands.put("filter_greater_than_type", new FilterGreaterThanType());
        allCommands.put("print_field_descending_type", new PrintFieldDescendingTypeCmd());
    }
    public void StartCommandManager(String[] userCommand, ClientSender clSender, ClientReceiver clReceiver) throws UnknownCommand {
        if (clReceiver != null) this.clientReceiver = clReceiver;
        if (clSender != null) this.clientSender = clSender;
        ClientMessage clientMessage;

        try {
            if (lastCommands.size() == 6) {
                lastCommands.removeFirst();
            }
            lastCommands.addLast(userCommand[0]);
            switch (userCommand[0]) {
                case "history":
                    lastCommands.removeLast();
                    System.out.println(lastCommands);
                    break;
                case "help":
                case "info":
                case "show":
                case "print_field_descending_type":
                case "clear":
                    clientMessage = new ClientMessage(allCommands.get(userCommand[0]));
                    clientSender.sendMessage(clientMessage);
                    clientReceiver.getMessage();
                    break;
                case "remove_by_id":
                case "filter_greater_than_type":
                    try{
                        long id = Long.parseLong(userCommand[1]);
                    }catch (Exception e){
                        System.out.println("Неправильный аргумент, id - целое число");
                        break;
                    }
                    clientMessage = new ClientMessage(allCommands.get(userCommand[0]), userCommand[1]);
                    clientSender.sendMessage(clientMessage);
                    clientReceiver.getMessage();
                    break;
                case "filter_contains_name":
                    clientMessage = new ClientMessage(allCommands.get(userCommand[0]), userCommand[1]);
                    clientSender.sendMessage(clientMessage);
                    clientReceiver.getMessage();
                    break;
                case "add_if_min":
                case "remove_lower":
                case "add":
                    GetNewOrg getNewOrg = new GetNewOrg();
                    Organization newOrg = getNewOrg.start();
                    clientMessage = new ClientMessage(allCommands.get(userCommand[0]), newOrg);
                    clientSender.sendMessage(clientMessage);
                    clientReceiver.getMessage();
                    break;
                case "update":
                    try{
                        long id = Long.parseLong(userCommand[1]);
                    }catch (Exception e){
                        System.out.println("Неправильный аргумент, id - целое число");
                        break;
                    }
                    GetNewOrg getNewOrg2 = new GetNewOrg();
                    Organization newOrg2 = getNewOrg2.start();
                    clientMessage = new ClientMessage(allCommands.get(userCommand[0]), userCommand[1], newOrg2);
                    clientSender.sendMessage(clientMessage);
                    clientReceiver.getMessage();
                    break;
                case "execute_script":
                    ScriptCmd scriptCmd = new ScriptCmd();
                    scriptCmd.startClient(userCommand[1], this);
                    break;
                case "exit":
                    System.out.println("Завершение программы...");
                    System.exit(1);
                default:
                    lastCommands.removeLast();
                    throw new UnknownCommand(userCommand[0]);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Отсутствует аргумент. Наберите 'help' для справки.");
        }
    }
}
