package Interfaces;

/*
    Defines the interface which each command
    implementation must follow
 */

import java.util.ArrayList;

public interface VerbMapFunction {
    Object call(ArrayList<String> parameters, ArrayList<String> arguments) throws Exception;
}
