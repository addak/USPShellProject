import HelperClasses.*;
import Models.Colour;
import Models.HistoryTableEntry;
import Models.InternalState;
import Parser.Parser;
import Models.ParserOutput;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

/*
    The main driver function of the shell. It displays the prompt
    and accepts input from the user, which is then passed to
    InternalFunctions.executeCommands().
 */


    public static void main(String[] args) {

        Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        InternalState.setScanner(scanner);
        while(true) {
            System.out.print(Colour.BLUE + InternalState.getInstance().getPresentWorkingDirectory().toString() + Colour.RESET + " >> ");
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
