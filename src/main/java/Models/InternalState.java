package Models;

/*
    Things that need to be accessible globally go here
 */

import HelperClasses.ShellVerbs;
import Interfaces.VerbMapFunction;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

/*
        jobTable stores details of all background jobs
        commandHistory stores details of last 9 commands
        map stores the names of all available commands and their corresponding functions in ShellVerb
        presentWorkingDirectory stores the present working directory
        reader stores the current Scanner object
*/
public class InternalState {
    private static InternalState instance = null;

    private HashMap<Long, JobTableEntry> jobTable;
    private LinkedList<HistoryTableEntry> commandHistory;
    private static HashMap<String, VerbMapFunction> map = new HashMap<>();

    private Path presentWorkingDirectory;
    private static Scanner reader;

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
        map.put("display", ShellVerbs::displayFile);
        map.put("history", ShellVerbs::commandHistory);
        map.put("unravel",ShellVerbs::unravel);
        map.put("ravel",ShellVerbs::ravel);
        map.put("mkdir", ShellVerbs::mkdir);
    }

    private InternalState() {
        presentWorkingDirectory = Paths.get(System.getProperty("user.dir"));
        jobTable = new HashMap<>();
        commandHistory = new LinkedList<>();
    }

    public static InternalState getInstance() {
        if(instance == null) instance = new InternalState();
        return instance;
    }

    public static void setScanner(Scanner obj){
        reader = obj;
    }

    public static Scanner getScanner(){ return reader; }

    public Path getPresentWorkingDirectory() {
        return presentWorkingDirectory;
    }

    public void setPresentWorkingDirectory(Path presentWorkingDirectory) {
        this.presentWorkingDirectory = presentWorkingDirectory;
    }

    public void addJob(Long pid, JobTableEntry entry) { jobTable.put(pid, entry);}

    public void removeJob(Long pid) {jobTable.remove(pid);}

    public HashMap<Long, JobTableEntry> getJobTable() { return jobTable; }

    public static HashMap<String, VerbMapFunction> getMap() { return map; }

    public void updateCommandHistory(HistoryTableEntry entry) {
        if(commandHistory.size() == 10) commandHistory.removeLast();
        commandHistory.addFirst(entry);
    }

    public LinkedList<HistoryTableEntry> getCommandHistory() {
        return commandHistory;
    }
}
