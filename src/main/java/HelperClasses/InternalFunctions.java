package HelperClasses;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class InternalFunctions {

    public static void recursiveCopy(Path srcPath, Path desPath) throws IOException {

        if(Files.isDirectory(srcPath)){

            List<Path> contents = Files.list(srcPath).collect(Collectors.toList());

            if(contents.isEmpty()) {
                Files.copy(srcPath, desPath);
                return;
            }

            for(Path content : contents){
                recursiveMove(content, desPath.resolve(content.getFileName()));
            }

            Files.delete(srcPath);

        }
        else{
            if( !Files.exists(desPath.getParent()) ){
                Files.createDirectories(desPath.getParent());
            }

            Files.copy(srcPath, desPath);
        }
    }

    public static void recursiveMove(Path srcPath, Path desPath) throws IOException{

        if(Files.isDirectory(srcPath)){

            List<Path> contents = Files.list(srcPath).collect(Collectors.toList());

            if(contents.isEmpty()) {
                Files.move(srcPath, desPath);
                return;
            }

            for(Path content : contents){
                recursiveMove(content, desPath.resolve(content.getFileName()));
            }

            Files.delete(srcPath);

        }
        else{
            if( !Files.exists(desPath.getParent()) ){
                Files.createDirectories(desPath.getParent());
            }

            Files.move(srcPath, desPath);
        }
    }

    public static void recursiveDelete(Path path) throws IOException{
        if(Files.isDirectory(path)) {
            List<Path> paths = Files.list(path).collect(Collectors.toList());
            for(Path childFile : paths) {
                if(Files.isDirectory(childFile)) {
                    recursiveDelete(childFile);
                }
                else {
                    Files.delete(childFile);
                }
            }
            Files.delete(path);
        } else if (Files.isRegularFile(path)) Files.delete(path);
    }
}
