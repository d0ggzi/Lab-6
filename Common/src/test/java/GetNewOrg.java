import organizations.*;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class GetNewOrg {
    public Organization getNewOrg(){
        long id = (long) (Math.random() * 1000000);
        String addName;
        int addCoordX;
        int addCoordY;
        int addAnnualTurnover;
        long addEmployeesCount;
        String addZipCode;
        OrganizationType addType;
        long addLocationX;
        long addLocationY;
        int addLocationZ;

        while (true){
            System.out.println("Введите имя организации: ");
            addName = read();
            if (addName.equals("")){
                System.out.println("Вы ввели пустую строку, попробуйте снова.");
                continue;
            }
            break;
        }

        while (true){
            System.out.println("Введите координату x (>-900): ");
            try{
                addCoordX = Integer.parseInt(read());
                if (addCoordX <= -900){
                    System.out.println("Значение должно быть больше -900");
                    continue;
                }
                break;
            }
            catch (NumberFormatException e){
                System.out.println("Вы ввели не число, попробуйте снова.");
            }
        }

        while (true){
            System.out.println("Введите координату y: ");
            try{
                addCoordY = Integer.parseInt(read());
                break;
            }
            catch (NumberFormatException e){
                System.out.println("Вы ввели не число, попробуйте снова.");
            }
        }

        while (true){
            System.out.println("Введите ежегодный оборот ( в миллионах $ ): ");
            try{
                addAnnualTurnover = Integer.parseInt(read());
                if (addAnnualTurnover <= 0){
                    System.out.println("Ежегодный оборот должен быть больше 0");
                    continue;
                }
                break;
            }
            catch (NumberFormatException e){
                System.out.println("Вы ввели не число, попробуйте снова.");
            }
        }


        while (true){
            System.out.println("Введите количество работников: ");
            try{
                addEmployeesCount = Long.parseLong(read());
                if (addEmployeesCount <= 0){
                    System.out.println("Количество работником должно быть больше 0");
                    continue;
                }
                break;
            }
            catch (NumberFormatException e){
                System.out.println("Вы ввели не число, попробуйте снова.");
            }
        }

        while (true){
            System.out.println("Введите тип организации из списка - COMMERCIAL, TRUST, OPEN_JOINT_STOCK_COMPANY");
            try{
                addType = OrganizationType.valueOf(read().toUpperCase(Locale.ROOT));
                break;
            }
            catch (IllegalArgumentException e){
                System.out.println("Вы ввели неизвестный тип, попробуйте снова.");
            }
        }

        while (true){
            System.out.println("Введите zipcode: ");
            addZipCode = read();
            if (addZipCode.equals("")){
                System.out.println("Вы ввели пустую строку, попробуйте снова.");
                continue;
            }
            break;
        }

        while (true){
            System.out.println("Введите координату x месторасположения: ");
            try{
                addLocationX = Long.parseLong(read());
                break;
            }
            catch (NumberFormatException e){
                System.out.println("Вы ввели не число, попробуйте снова.");
            }
        }


        while (true){
            System.out.println("Введите координату y месторасположения: ");
            try{
                addLocationY = Long.parseLong(read());
                break;
            }
            catch (NumberFormatException e){
                System.out.println("Вы ввели не число, попробуйте снова.");
            }
        }

        while (true){
            System.out.println("Введите координату z месторасположения: ");
            try{
                addLocationZ = Integer.parseInt(read());
                break;
            }
            catch (NumberFormatException e){
                System.out.println("Вы ввели не число, попробуйте снова.");
            }
        }

        Organization addOrg = new Organization(id, addName, new Coordinates(addCoordX, addCoordY), addAnnualTurnover, addEmployeesCount, addType, new Address(addZipCode, new Location(addLocationX, addLocationY, addLocationZ)));
        return addOrg;
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
}
