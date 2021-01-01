import HelperClasses.Colour;
import HelperClasses.InternalState;
import HelperClasses.ShellVerbs;
import HelperClasses.VerbMapFunction;
import Parser.Parser;
import Parser.ParserOutput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    static HashMap<String,VerbMapFunction> map = new HashMap<>();

    static{

        map.put("ls", ShellVerbs::listFiles);
        map.put("pwd", ShellVerbs::presentWorkingDirectory);
        map.put("cd", ShellVerbs::changeDirectory);
        map.put("cls", ShellVerbs::clearScreen);
        map.put("mv", ShellVerbs::moveFile);
        map.put("cp", ShellVerbs::copyFile);
        map.put("rm",ShellVerbs::deleteFile);
    }


    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        InternalState.setScanner(scanner);
        while(true) {
            System.out.print(">> ");
            String input = scanner.nextLine().trim();
            ArrayList<ParserOutput> parserOutput = Parser.parseInput(input);
            for(ParserOutput output : parserOutput) {
                if(output.getCommand().equals("quit")) return;
                VerbMapFunction function = map.get(output.getCommand());
                if(function != null) function.call(output.getParameters(), output.getArguments());
                else System.out.println(Colour.RED + output.getCommand() + ": command not found" + Colour.RESET);
            }
//            String command = scanner.next();
//            if(command.equals("quit")) return;
//            String input = scanner.nextLine().trim();
//            VerbMapFunction fn = map.get(command);
//            String[] rest = {input};
//            if (fn != null) fn.call(rest);
//            else System.out.println("Invalid Verb!");
        }

    }
}
