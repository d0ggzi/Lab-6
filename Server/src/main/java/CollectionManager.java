import commands.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import organizations.*;

import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

/**
 * Класс выполнения команд, работы с коллекцией
 *
 * @author Max Laptev
 * @version 1.0
 */

public class CollectionManager {
    private LinkedHashSet<Organization> organizations;
    protected static HashMap<String, String> commands;
    private final Date initiazitionDate;
//    final ScriptCmd script = new ScriptCmd();
    File workFile = null;
    private static final Logger logger = LogManager.getLogger(CollectionManager.class);


    {
        commands = new HashMap<>();
        this.initiazitionDate = new Date();
        commands.put("help", "Вывести справку по доступным командам.");
        commands.put("info", "Вывести в стандартный поток вывода информацию о коллекции.");
        commands.put("show", "Вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        commands.put("add {element}", "Добавить новый элемент в коллекцию.");
        commands.put("update id {element}", "Обновить значение элемента коллекции, id которого равен заданному.");
        commands.put("remove_by_id id", "Удалить элемент из коллекции по его id.");
        commands.put("clear", "Очистить коллекцию.");
        commands.put("execute_script file_name", "Считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же.");
        commands.put("exit", "Завершить программу (без сохранения в файл).");
        commands.put("add_if_min {element}", "Добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции.");
        commands.put("remove_lower {element}", "Удалить из коллекции все элементы, превышающие заданный.");
        commands.put("history", "Вывести последние 5 команд.");
        commands.put("filter_contains_name name", "Вывести элементы, значение поля name которых содержит заданную подстроку.");
        commands.put("filter_greater_than_type type", "Вывести элементы, значение поля type которых больше заданного.");
        commands.put("print_field_descending_type", "Вывести значения поля type всех элементов в порядке убывания.");
    }

    public CollectionManager(String filepath){
        while (true){
            File file = new File(filepath);
            try {
                if (file.isFile()){
                    Read collection = new Read();
                    organizations = collection.reading(file);
                    workFile = file;
                    break;
                }
                throw new FileNotFoundException();
            }
            catch (FileNotFoundException e){
                if (!file.exists()){
                    System.out.println("Файл по указанному пути не существует");
                }else if (!file.canRead()){
                    System.out.println("У файла по указанному пути нет прав на чтение");
                }else if (file.isDirectory()){
                    System.out.println("Указанный путь не ведет к файлу");
                }
                System.out.println("Введите путь к файлу:");
                filepath = read();
            }
        }
    }

    public String read(){
        try{
            Scanner scan = new Scanner(System.in);
            return scan.nextLine();
        }catch (NoSuchElementException e){
            System.exit(1);
        }
        return null;
    }


    public void execute(HelpCmd c, DatagramChannel channel, InetSocketAddress socketAddress){
        {
            String helpText = "Команды: \n";
            for (String s: commands.keySet()){
                helpText += s + " - " + commands.get(s) + "\n";
            }
            ServerMessage serverMessage = new ServerMessage(helpText);
            ServerSender serverSender = new ServerSender(serverMessage);
            try{
                serverSender.sendMessage(channel, socketAddress);
                logger.info("Команда help успешно отправлена!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void execute(InfoCmd c, DatagramChannel channel, InetSocketAddress socketAddress){
        String infoText = "Тип коллекции - " + organizations.getClass() + "\n" + "Дата инициализации - " + initiazitionDate + "\n" + "Количество элементов - " + organizations.size();
        ServerMessage serverMessage = new ServerMessage(infoText);
        ServerSender serverSender = new ServerSender(serverMessage);
        try{
            serverSender.sendMessage(channel, socketAddress);
            logger.info("Команда info успешно отправлена!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execute(ShowCmd c, DatagramChannel channel, InetSocketAddress socketAddress){
        String showText = "";
        if (organizations.isEmpty()){
            showText = "Коллекция пуста.";
        }
        else{
            for (Organization org: organizations) {
                showText += org.toString() + "\n";
            }
        }
        ServerMessage serverMessage = new ServerMessage(showText);
        ServerSender serverSender = new ServerSender(serverMessage);
        try{
            serverSender.sendMessage(channel, socketAddress);
            logger.info("Команда show успешно отправлена!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execute(PrintFieldDescendingTypeCmd c, DatagramChannel channel, InetSocketAddress socketAddress){
        String descendingText = "";
        boolean flag = true;
        TreeSet<Organization> sortedSet = new TreeSet<>(organizations);
        for (Organization el : sortedSet){
            flag = false;
            descendingText += el + "\n";
        }
        if (flag){
            descendingText = "Извините, элементов нет";
        }
        ServerMessage serverMessage = new ServerMessage(descendingText);
        ServerSender serverSender = new ServerSender(serverMessage);
        try{
            serverSender.sendMessage(channel, socketAddress);
            logger.info("Команда print_field_descending_type успешно отправлена!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execute(AddCmd c, Organization newOrg, DatagramChannel channel, InetSocketAddress socketAddress){
        organizations.add(newOrg);
        ServerMessage serverMessage = new ServerMessage("Организация успешно добавлена.");
        ServerSender serverSender = new ServerSender(serverMessage);
        try{
            serverSender.sendMessage(channel, socketAddress);
            logger.info("Команда add успешно выполнена!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execute(UpdateCmd c, long id, Organization updatedOrg, DatagramChannel channel, InetSocketAddress socketAddress){
        String updateText = "";
//        boolean flag = false;
//        LinkedHashSet<Organization> oldOrg = new LinkedHashSet<>(organizations);
//        for (Organization el: oldOrg){
//            if (el.getId() == id){
//                flag = true;
//                el.setName(updatedOrg.getName());
//                el.setCoordinates(updatedOrg.getCoordinates());
//                el.setAnnualTurnover(updatedOrg.getAnnualTurnover());
//                el.setEmployeesCount(updatedOrg.getEmployeesCount());
//                el.setType(updatedOrg.getType());
//                el.setPostalAddress(updatedOrg.getPostalAddress());
//            }
//        }
//        if (flag){
//            updateText = "Организация успешно обновлена.";
//        }else{
//            updateText = "Не существует организации с id " + id;
//        }
        if (organizations.stream().filter(org -> org.getId() == id).collect(Collectors.toList()).size() == 0){
            updateText = "Не существует организации с id " + id;
        } else{
            organizations.stream().filter(el -> {
                if (el.getId() == id){
                    el.setName(updatedOrg.getName());
                    el.setCoordinates(updatedOrg.getCoordinates());
                    el.setAnnualTurnover(updatedOrg.getAnnualTurnover());
                    el.setEmployeesCount(updatedOrg.getEmployeesCount());
                    el.setType(updatedOrg.getType());
                    el.setPostalAddress(updatedOrg.getPostalAddress());
                }
                return true;
            }).collect(Collectors.toList());
            updateText = "Организация успешно обновлена.";
        }
        ServerMessage serverMessage = new ServerMessage(updateText);
        ServerSender serverSender = new ServerSender(serverMessage);
        try{
            serverSender.sendMessage(channel, socketAddress);
            logger.info("Команда update успешно выполнена!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execute(RemoveByIdCmd c, long id, DatagramChannel channel, InetSocketAddress socketAddress){
        boolean flag = false;
        String removeText = "";
        LinkedHashSet<Organization> oldOrg = new LinkedHashSet<>(organizations);
        for (Organization el: oldOrg){
            if (el.getId() == id){
                flag = true;
                organizations.remove(el);
            }
        }
        if (flag) {
            removeText = "Организация с id " + id + " успешно удалена.";
        }else{
            removeText = "Не существует организации с id " + id;
        }
        ServerMessage serverMessage = new ServerMessage(removeText);
        ServerSender serverSender = new ServerSender(serverMessage);
        try{
            serverSender.sendMessage(channel, socketAddress);
            logger.info("Команда remove_by_id успешно выполнена!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execute(ClearCmd c, DatagramChannel channel, InetSocketAddress socketAddress){
        String clearText = "";
        if (organizations.isEmpty()){
            clearText = "Коллекция уже пуста.";
        }
        else{
            organizations.clear();
            clearText = "Коллекция очищена.";
        }
        ServerMessage serverMessage = new ServerMessage(clearText);
        ServerSender serverSender = new ServerSender(serverMessage);
        try{
            serverSender.sendMessage(channel, socketAddress);
            logger.info("Команда clear успешно выполнена!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(){
        StringBuilder toXmlFile = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        for (Organization el : organizations){
            toXmlFile.append("<organization>\n");
            toXmlFile.append(el.toXmlFormat());
            toXmlFile.append("</organization>\n");
        }

        try {
            FileWriter fileWriter = new FileWriter(workFile);
            fileWriter.write(toXmlFile.toString());
            fileWriter.flush();
            System.out.println("Коллекция успешно сохранена.");
        } catch (IOException e) {
            if (!workFile.canWrite()) {
                System.out.println("У файла нет доступа на запись.");
            }
            System.out.println("Не удалось сохранить коллекцию в файл.");
        }
    }


    public void execute(AddIfMinCmd c, Organization newOrg, DatagramChannel channel, InetSocketAddress socketAddress){
        String addIfMinText = "";
        int minAnnualTurnover = 999999999;
        for (Organization el: organizations){
            if (el.getAnnualTurnover() < minAnnualTurnover){
                minAnnualTurnover = el.getAnnualTurnover();
            }
        }
        if (newOrg.getAnnualTurnover() < minAnnualTurnover){
            organizations.add(newOrg);
            addIfMinText = "Организация успешно добавлена.";
        }
        else {
            addIfMinText = "Организация не была добавлена.";
        }
        ServerMessage serverMessage = new ServerMessage(addIfMinText);
        ServerSender serverSender = new ServerSender(serverMessage);
        try{
            serverSender.sendMessage(channel, socketAddress);
            logger.info("Команда add_if_min успешно выполнена!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execute(RemoveLowerCmd c, Organization newOrg, DatagramChannel channel, InetSocketAddress socketAddress){
        String removeLowerText = "";
        int lowerAnnualTurnover = newOrg.getAnnualTurnover();
        LinkedList<String> removed = new LinkedList<>();
        LinkedHashSet<Organization> oldOrg = new LinkedHashSet<>(organizations);
        for (Organization el : oldOrg) {
            if (el.getAnnualTurnover() < lowerAnnualTurnover){
                removed.add(el.getName());
                organizations.remove(el);
            }
        }
        removeLowerText = "Удалены организации: " + removed;
        ServerMessage serverMessage = new ServerMessage(removeLowerText);
        ServerSender serverSender = new ServerSender(serverMessage);
        try{
            serverSender.sendMessage(channel, socketAddress);
            logger.info("Команда remove_lower успешно выполнена!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execute(FilterContainsNameCmd c, String name, DatagramChannel channel, InetSocketAddress socketAddress){
        String containsNameText = "";
        boolean flag = true;
        for (Organization el : organizations){
            if (el.getName().equals(name)){
                flag = false;
                containsNameText += el + "\n";
            }
        }
        if (flag){
            containsNameText = "Извините, таких элементов нет";
        }
        ServerMessage serverMessage = new ServerMessage(containsNameText);
        ServerSender serverSender = new ServerSender(serverMessage);
        try{
            serverSender.sendMessage(channel, socketAddress);
            logger.info("Команда filter_contains_name успешно выполнена!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execute(FilterGreaterThanType c, int inputAnnualTurnover, DatagramChannel channel, InetSocketAddress socketAddress){
        String greaterThanText = "";
        boolean flag = true;
        for (Organization el : organizations){
            if (el.getAnnualTurnover() > inputAnnualTurnover){
                flag = false;
                greaterThanText += el + "\n";
            }
        }
        if (flag){
            greaterThanText = "Извините, таких элементов нет";
        }
        ServerMessage serverMessage = new ServerMessage(greaterThanText);
        ServerSender serverSender = new ServerSender(serverMessage);
        try{
            serverSender.sendMessage(channel, socketAddress);
            logger.info("Команда filter_greater_than_type успешно выполнена!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
