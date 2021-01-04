import HelperClasses.*;
import Models.HistoryTableEntry;
import Models.InternalState;
import Parser.Parser;
import Parser.ParserOutput;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

//    static HashMap<String,VerbMapFunction> map = new HashMap<>();
//
//    static {
//
//        map.put("ls", ShellVerbs::listFiles);
//        map.put("pwd", ShellVerbs::presentWorkingDirectory);
//        map.put("cd", ShellVerbs::changeDirectory);
//        map.put("cls", ShellVerbs::clearScreen);
//        map.put("mv", ShellVerbs::moveFile);
//        map.put("cp", ShellVerbs::copyFile);
//        map.put("rm", ShellVerbs::deleteFile);
//        map.put("chmod", ShellVerbs::chmod);
//        map.put("exec", ShellVerbs::execute);
//        map.put("chown",ShellVerbs::chown);
//        map.put("bgjobs", ShellVerbs::showBackgroundJobs);
//        map.put("killjob", ShellVerbs::killBackgroundJobs);
//        map.put("create", ShellVerbs::createFile);
//        map.put("display", ShellVerbs::displayFile);
//
//    }


    public static void main(String[] args) {

        Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        InternalState.setScanner(scanner);
        while(true) {
            System.out.print(Colour.BLACK + InternalState.getInstance().getPresentWorkingDirectory().toString() + Colour.RESET + " >> ");
            try {
                String input = scanner.nextLine().trim();
                if(!input.equals("")) {
                    ArrayList<ParserOutput> parserOutput = Parser.parseInput(input);
                    HistoryTableEntry entry = new HistoryTableEntry(input, parserOutput);
                    InternalState.getInstance().updateCommandHistory(entry);
                    InternalFunctions.executeCommands(parserOutput);
                }
            } catch (Exception e) {
                System.out.println(Colour.RED + "Shell error: " + e + Colour.RESET);
            }
        }

    }
}
