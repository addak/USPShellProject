package HelperClasses;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface VerbMapFunction {
    Object call(String[] params) throws IOException;
}
