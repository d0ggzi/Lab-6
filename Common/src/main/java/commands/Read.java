package commands;

import organizations.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.ZonedDateTime;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Scanner;

/**
 * Класс чтения и парсинга данных из файла формата xml
 *
 * @author Max Laptev
 * @version 1.0
 */

public class Read {
    private String finalXmlString = "";
    private LinkedHashSet<Organization> organizations = new LinkedHashSet<>();

    public LinkedHashSet<Organization> reading(File file) throws FileNotFoundException {
        Scanner scan = new Scanner(file); // "C:\\Users\\maks-\\GoogleDrive\\programming\\java\\lab5_all\\test\\src\\test.xml"
        StringBuilder xmlStringAll = new StringBuilder();
        while (scan.hasNextLine()) {
            xmlStringAll.append(scan.nextLine()).append("\n");
        }

        String[] organizationsList = xmlStringAll.toString().split("</organization>");
        for (int temp = 0; temp < organizationsList.length - 1; temp++) {

            this.finalXmlString = organizationsList[temp];

            long id = Long.parseLong(splitter("id"));
            String name = splitter("name");
            int coordinates_x = Integer.parseInt(splitter("coordinates-x"));
            int coordinates_y = Integer.parseInt(splitter("coordinates-y"));
            ZonedDateTime creationDate = ZonedDateTime.parse(splitter("creationDate"));
            int annualTurnover = Integer.parseInt(splitter("annualTurnover"));
            Long employeesCount = Long.parseLong(splitter("employeesCount"));
            OrganizationType organizationType = OrganizationType.valueOf(splitter("organizationType").toUpperCase(Locale.ROOT));
            String zipCode = splitter("zipCode");
            long location_x = Long.parseLong(splitter("location-x"));
            long location_y = Long.parseLong(splitter("location-y"));
            int location_z = Integer.parseInt(splitter("location-z"));

            Organization org = new Organization(id, name, new Coordinates(coordinates_x, coordinates_y), creationDate, annualTurnover, employeesCount, organizationType, new Address(zipCode, new Location(location_x, location_y, location_z)));
            organizations.add(org);
            System.out.println("Компания " + name + " успешно добавлена.");

        }
        return organizations;
    }

    /**
     * Метод получения данных по тегу
     *
     * @param tag - xml тег, данные которого нужно получить
     */

    public String splitter(String tag){
        String str = finalXmlString.split(tag)[1];
        str = str.substring(1, str.length() - 2).trim();
        return str;
    }

}
