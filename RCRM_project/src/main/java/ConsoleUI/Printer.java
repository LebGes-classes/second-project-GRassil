package ConsoleUI;

import java.util.List;

public class Printer {

    public static <T> void printMsg(T t) {
        String text = String.format(
                        """
                        ⌈‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
                        | %s
                        ⌊________________
                        """, t.toString());
        System.out.println(text);
    }

    public static void printList(List list) {
        for (Object item : list) {
            System.out.println(item);
        }
    }

    public static void messageError() {
        printMsg("!!!\tSomething get wrong, try again\t!!!");
    }
}
