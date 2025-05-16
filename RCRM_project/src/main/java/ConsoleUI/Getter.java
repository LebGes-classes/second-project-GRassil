package ConsoleUI;

import models.account.EAccType;
import models.account.EPosition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Scanner;

import static ConsoleUI.Printer.printList;
import static ConsoleUI.Printer.printMsg;
import static services.utils.ModelServiceMap.getServiceClass;

public class Getter {
    private static Scanner scanner = new Scanner(System.in);

    //Получение текста
    public static String getText(String text) {
        printMsg(text);
        return scanner.nextLine().trim();
    }
    // Регистрация текста
    public static String getApprovedText(String text) {
        String answer;
        do {
            answer=getText(String.format("Enter %s", text)); // Просьба ввести то, что передали в параметр
        } while (!approveChoice(answer));
        return answer; // При подтверждении поля вернуть значение
    }

    // Получение числа
    public static int getInt(String text) {
        printMsg(text); // Выводим текст
        while (!scanner.hasNextInt()) { // Пока не будет введено число
            printMsg("Неправильный ввод, попробуйте заново");
            scanner.next(); //чистим ввод от неверного ввода
        }
        int id = scanner.nextInt();
        scanner.nextLine();// чистим ввод от лишних символов
        return id;
    }

    //Получение объекта по id
    public static <T> T getModel(Class<T> modelClass, String text) {
        try {
            // Получаем класс сервис для модели
            Class<?> serviceClass = getServiceClass(modelClass);
            // Получаем метод getAll и применяем, чтобы получить список объектов
            Method getAllMethod = serviceClass.getMethod("getAll");
            List<T> list = (List<T>) getAllMethod.invoke(null);

            if (list == null) {
                return null;
            }

            // Печатаем лист для выбора пользователем
            printList(list);

            T obj; // создаем объект

            // Цикл пока пользователь не подтвердит свой выбор
            do {
                // Цикл пока пользователь не введет правильный id из списка объектов
                do {
                    int id = getInt(text); // Получаем id модели

                    // Проверяем объект на наличие в списке
                    // Если объект с введенным id есть, то метод вернет объект класса, иначе null
                    obj = (T) serviceClass.getMethod("get", int.class).invoke(null, id);
                } while (obj == null);

            } while (!approveChoice(obj));

            return obj; // Возвращаем объект
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            printMsg("Error int getModel - "+ e);
            return null;
        }
    }

    public static boolean approveChoice(Object obj) {
        while (true) {
            String answer = getText(String.format("Confirm your choice (%d) \n [Y|y]es\t[N|n]o", obj));

            switch (answer.toLowerCase()) {
                case "y":
                    return true;
                case "n":
                    return false;
            }
        }
    }

    // Получение типа аккаунта
    public static EAccType getType() {

        String position = null; //Изначально позиция никакая
        do {
            do { // Цикл идет пока пользователь не введет правильное значение
                String positionChar = getText("""
                Enter your account type:\
                
                - [C|c]ustomer\
                
                - [E|e]mployee\
                
                - [V|v]endor\
                
                Your answer:\t
                """);
                switch (positionChar.toLowerCase()) {
                    case "c":
                        position = "CUSTOMER";
                        break;
                    case "e":
                        position = "EMPLOYEE";
                        break;
                    case "v":
                        position = "VENDOR";
                        break;
                    default:
                        printMsg("Something went wrong, try again: ");
                }
            }while (position == null);
        } while (approveChoice(position)); // Выбор идет пока пользователь не подтвердит свой выбор
        return EAccType.valueOf(position);
    }

    // Получение должности
    public static EPosition getPosition() {
        String position = null; //Изначально позиция никакая

        do {
            while (position == null) { // Цикл идет пока пользователь не введет правильное значение
                String positionChar = getText("""
                        Enter your position: \
                        
                        - [M|m]anager\
                        
                        - [W|w]orker\
                        
                        Your answer:\t
                        """);

                switch (positionChar.toLowerCase()) {
                    case "m":
                        position = "MANAGER";
                        break;
                    case "w":
                        position = "WORKER";
                        break;
                    default:
                        System.out.println("Something went wrong, try again");
                }
            }
        } while (approveChoice(position)); // Выбор идет пока пользователь не подтвердит свой выбор
        return EPosition.valueOf(position);
    }
}
