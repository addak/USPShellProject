package Models;

/*
    Presents the output of the parser
    in an easy to use manner. This class should
    be pretty self-explanatory.
 */

import java.util.ArrayList;

public class ParserOutput {

    private String command;
    private ArrayList<String> parameters;
    private ArrayList<String> arguments;

    public ParserOutput(String command, ArrayList<String> parameters, ArrayList<String> arguments) {
        this.command = command;
        this.parameters = parameters;
        this.arguments = arguments;
    }

    public String getCommand() {
        return command;
    }

    public ArrayList<String> getParameters() {
        return parameters;
    }

    public ArrayList<String> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "ParserOutput{" +
                "command='" + command + '\'' +
                ", parameters=" + parameters +
                ", arguments=" + arguments +
                '}';
    }
}
