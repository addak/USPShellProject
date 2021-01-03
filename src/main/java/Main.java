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

    static {

        map.put("ls", ShellVerbs::listFiles);
        map.put("pwd", ShellVerbs::presentWorkingDirectory);
        map.put("cd", ShellVerbs::changeDirectory);
        map.put("cls", ShellVerbs::clearScreen);
        map.put("mv", ShellVerbs::moveFile);
        map.put("cp", ShellVerbs::copyFile);
        map.put("rm", ShellVerbs::deleteFile);
        map.put("chmod", ShellVerbs::chmod);
        map.put("exec", ShellVerbs::execute);
        map.put("chown",ShellVerbs::chown);
        map.put("bgjobs", ShellVerbs::showBackgroundJobs);
        map.put("killjob", ShellVerbs::killBackgroundJobs);
        map.put("create", ShellVerbs::createFile);

    }


    public static void main(String[] args) {

        Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        InternalState.setScanner(scanner);
        while(true) {
            System.out.print(">> ");
            try {
                String input = scanner.nextLine().trim();
                ArrayList<ParserOutput> parserOutput = Parser.parseInput(input);
                for(ParserOutput output : parserOutput) {
                    if(output.getCommand().equals("quit")) return;
                    VerbMapFunction function = map.get(output.getCommand());
                    if(function != null) function.call(output.getParameters(), output.getArguments());
                    else System.out.println(Colour.RED + output.getCommand() + ": command not found" + Colour.RESET);
                }
            } catch (Exception e) {
                System.out.println(Colour.RED + "Shell error: " + e.getMessage() + Colour.RESET);
            }
        }

    }
}
