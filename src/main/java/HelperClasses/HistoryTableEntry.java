package HelperClasses;

import Parser.ParserOutput;

import java.util.ArrayList;

public class HistoryTableEntry {

    private String fullCommand;
    private ArrayList<ParserOutput> parserOutput;

    public HistoryTableEntry(String fullCommand, ArrayList<ParserOutput> parserOutput) {
        this.fullCommand = fullCommand;
        this.parserOutput = parserOutput;
    }

    public String getFullCommand() {
        return fullCommand;
    }

    public ArrayList<ParserOutput> getParserOutput() {
        return parserOutput;
    }
}
