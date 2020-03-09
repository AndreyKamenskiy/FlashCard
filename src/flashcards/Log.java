package flashcards;

import java.util.Scanner;

public class Log {
    private static StringBuilder log = new StringBuilder();

    public static void showLog(){
        System.out.print(log);
    }

    public static String getLog(){
        return log.toString();
    }

    public static void println(String text) {
        log.append(text);
        log.append("\n");
        System.out.println(text);
    }

    public static void print(String text) {
        log.append(text);
        System.out.print(text);
    }

    public static void printf(String format, Object... args) {
        String[] str = format.split("%[s,d]");

        for (int i = 0; i < str.length; i++) {
            log.append(str[i]);
            if (i < args.length) {
                log.append(args[i]);
            }
        }
        System.out.printf(format,args);
    }

    public static void resetLog() {
        //i don't know what method to choose
        //log.delete(0, log.length()); // delete all elements of log
        log = new StringBuilder(); // TrashCollector will delete olg log
    }

    public static String nextLine() {
        String line = new Scanner(System.in).nextLine();
        log.append(line);
        log.append("\n");
        return line;
    }

}
