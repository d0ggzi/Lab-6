import commands.Command;
import exceptions.UnknownCommand;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Класс для команды execute_script с проверкой на зацикливание
 *
 * @author Max Laptev
 * @version 1.0
 */

public class ScriptCmd extends Command {
    public void startClient(String filepath, ClientCommandManager clientCommandManager){
        if (noCycle(filepath)){
            File file = new File(filepath);
            Scanner scanner;
            try{
                scanner = new Scanner(file);
                int lineIter = 0;
                while (scanner.hasNextLine()){
                    String line = scanner.nextLine().trim();
                    lineIter += 1;
                    if (line.equals("")){
                        continue;
                    }
                    try{
                        String[] userCmd = line.split(" ", 2);
                        clientCommandManager.StartCommandManager(userCmd, null, null);
                    }
                    catch (UnknownCommand e){
                        System.out.println("Ошибка при выполнении на строке " + lineIter +" в файле " + filepath);
                        System.out.println(e);
                        break;
                    }
                }
            } catch (FileNotFoundException e) {
                if (!file.exists()){
                    System.out.println("Файл не найден, попробуйте снова.");
                }
                else if (!file.canRead()){
                    System.out.println("У файла нет прав на чтение, попробуйте снова.");
                }
            }catch (Exception e){
                System.out.println("Неправильный путь, попробуйте снова");
            }
        }

    }

    boolean flag = false;

    /**
     * Метод проверки на цикличность файлов
     *
     * @param filepath - путь до первого файла
     */

    public boolean noCycle(String filepath) {
        try {
            HashSet<String> paths = new HashSet<>();
            findAllPathForScript(paths, filepath);
            if (flag) {
                System.out.println("В файле существует цикл, запуск невозможен, попробуйте снова.");
                return false;
            }
            else {
                return true;
            }
        }catch (ArrayIndexOutOfBoundsException e) {
            System.out.print("Неправильный аргумент, попробуйте снова.");
        }catch (IOException e){
            System.out.println("Файл не найден или недостаточно прав для запуска, попробуйте снова.");
        }catch (Exception e){
            System.out.println("Неправильный путь, попробуйте снова");
        }
        return false;
    }

    /**
     * Рекурсионный алгоритм на основе поиска в глубину
     *
     * @param set - посещенные файлы
     * @param path - путь до файла
     */

    public void findAllPathForScript(HashSet<String> set, String path) throws IOException {
        set.add(path.toLowerCase(Locale.ROOT));
        LinkedHashSet<String> set1 = new LinkedHashSet<>();
        if (flag){
            return;
        }
        try {
            List<String> allLines = Files.readAllLines(Paths.get(path));
            for (String s : allLines) {
                if (s.contains("execute_script") && s.contains(".txt")) {
                    String[] line = s.split(" ");
                    set1.add(line[1].toLowerCase(Locale.ROOT));
                }
            }
            for (String str : set1) {
                if (!set.contains(str)) {
                    findAllPathForScript(set, str);
                } else {
                    flag = true;
                    return;
                }
            }
        }catch (Exception e) {
            System.out.print("");
        }
        // C:\Users\maks-\GoogleDrive\programming\java\lab6_all\lab6\Client\src\main\resources\script.txt
    }
}
