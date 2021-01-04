package Parser;

import Parser.ParserBackend.ShellLexer;
import Parser.ParserBackend.ShellParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

/*
    parseInput takes entire line input into the prompt and splits it
    based on '|'. parseCommand then parses each string from the prior
    step to determine the command, parameters and arguments.
    e.g.: cp -xt a.txt b.txt
    Command -> cp
    Parameters -> -xt
    Arguments -> a.txt, b.txt
 */

public class Parser {

    public static ArrayList<ParserOutput> parseInput(String fullCommand) throws Exception {
        ArrayList<ParserOutput> output = new ArrayList<>(1);
        String[] commands = fullCommand.trim().split("(\\|)|( \\| )");
        for(String command: commands) {
            ParserOutput commandParserOutput = parseCommand(command);
            output.add(commandParserOutput);
        }
        return output;
    }

    public static ParserOutput parseCommand(String commandLineInput) throws Exception{
        Scanner s = new Scanner(commandLineInput);
        ArrayList<String> parameters = new ArrayList<>(), arguments = new ArrayList<>();
        String command = s.next(), restOfInput;
        if(s.hasNext()) {
            restOfInput = s.nextLine().trim();
            ANTLRInputStream inputStream = new ANTLRInputStream(
                    new ByteArrayInputStream(
                            restOfInput.getBytes(StandardCharsets.UTF_8)
                    )
            );
            ShellLexer lexer = new ShellLexer(inputStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            ShellParser parser = new ShellParser(tokens);
            ParseTree tree = parser.input();
            for(int i = 0; i < tree.getChildCount(); ++i) {
                String token = tree.getChild(i).toString();
                if(token.charAt(0) == '-' || token.charAt(0) == '+') parameters.add(token);
                else if(token.charAt(0) == '"') arguments.add(token.substring(1, token.length()-1));
                else arguments.add(token);
            }
        }
        ParserOutput output = new ParserOutput(command, parameters, arguments);
        return output;
    }

}
