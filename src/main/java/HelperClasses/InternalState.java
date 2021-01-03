package HelperClasses;

/*
    Things that need to be accessible globally go here
 */

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

public class InternalState {
    private static InternalState instance = null;

    private HashMap<Long, JobTableEntry> jobTable;
    private Path presentWorkingDirectory;
    private static Scanner reader;

    private InternalState() {
        presentWorkingDirectory = Paths.get(System.getProperty("user.dir"));
        jobTable = new HashMap<>();
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
}
