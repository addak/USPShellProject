package Models;

/*
    This class is used to store details of
    background jobs
 */

public class JobTableEntry {

    private String fullCommand;
    private ProcessHandle jobHandle;

    public JobTableEntry(String fullCommand, ProcessHandle jobHandle) {
        this.fullCommand = fullCommand;
        this.jobHandle = jobHandle;
    }

    public String getFullCommand() {
        return fullCommand;
    }

    public ProcessHandle getJobHandle() {
        return jobHandle;
    }
}


