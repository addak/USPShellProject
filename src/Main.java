import HelperClasses.ShellVerbs;
import HelperClasses.VerbMapFunction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    static HashMap<String,VerbMapFunction> map = new HashMap<>();

    static{

        map.put("ls", ShellVerbs::listFiles);

        map.put("mv", ShellVerbs::moveFile);
        map.put("cp", ShellVerbs::copyFile);
        map.put("rm",ShellVerbs::deleteFile);
    }


    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(System.in)));

        while(true) {
            System.out.print(">> ");
            String[] inputLine = scanner.nextLine().split("\\s+");

            if (inputLine[0].equals("quit") ) {
                return;
            }

            VerbMapFunction fn = map.get(inputLine[0]);
            String[] rest = Arrays.copyOfRange(inputLine,1,inputLine.length);

            if (fn != null) {
                @SuppressWarnings("unchecked")
                List<Path> paths = (List<Path>)fn.call(rest);
                for(Path pathOnj : paths){
                    if(Files.isRegularFile(pathOnj))
                        System.out.println(pathOnj.getFileName());
                    else
                        System.out.println(ANSI_CYAN + pathOnj.getFileName() + ANSI_RESET);
                }
            } else {
                System.out.println("Invalid Verb!");
            }

        }

    }
}
