package Interfaces;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public interface VerbMapFunction {
    Object call(ArrayList<String> parameters, ArrayList<String> arguments) throws Exception;
}
