package HelperClasses;

/*
    Things that need to be accessible globally go here
 */

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class InternalState {
    private static InternalState instance = null;

    private Path presentWorkingDirectory;
    private static Scanner reader;

    private InternalState() {
        presentWorkingDirectory = Paths.get(System.getProperty("user.dir"));
    }

    public static void setScanner(Scanner obj){
        reader = obj;
    }

    public static Scanner getScanner(){ return reader; }

    public static InternalState getInstance() {
        if(instance == null) instance = new InternalState();
        return instance;
    }

    public Path getPresentWorkingDirectory() {
        return presentWorkingDirectory;
    }

    public void setPresentWorkingDirectory(Path presentWorkingDirectory) {
        this.presentWorkingDirectory = presentWorkingDirectory;
    }
}
