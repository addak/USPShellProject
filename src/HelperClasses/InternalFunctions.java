package HelperClasses;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class InternalFunctions {
    /*
        Shitty code to parse two paths from input
        for cp and mv commands. If possible, rewrite
        this in a much more elegant manner.
     */
    public static String[] parsePaths(String input) {
        String sourceFilePath, destinationFilePath; int i = 0;
        StringBuilder sb = new StringBuilder();
        if(input.charAt(0) == '\'' || input.charAt(0) == '\"') {
            for (i = 1; i < input.length() && input.charAt(i) != input.charAt(0); ++i)
                sb.append(input.charAt(i));
            ++i;
        }
        else while(i < input.length() && input.charAt(i) != ' ')
            sb.append(input.charAt(i++));
        sourceFilePath = sb.toString();
        if(sourceFilePath.charAt(0) != '/')
            sourceFilePath = InternalState.getInstance().getPresentWorkingDirectory().toString() + '/' + sb.toString();
        sb = new StringBuilder();
        ++i;

        if(input.charAt(i) == '\'' || input.charAt(i) == '\"') {
            char end = input.charAt(i++);
            for(; i < input.length() && input.charAt(i) != end; ++i)
                sb.append(input.charAt(i));
        }
        else
            while(i < input.length() && input.charAt(i) != ' ')
                sb.append(input.charAt(i++));
        destinationFilePath = sb.toString();
        if(destinationFilePath.charAt(0) != '/')
            destinationFilePath = InternalState.getInstance().getPresentWorkingDirectory().toString() + '/' + sb.toString();
        String[] paths = {sourceFilePath, destinationFilePath};
        return paths;
    }

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
                    recursivelyDeleteFolder(childFile);
                }
                else {
                    Files.delete(childFile);
                }
            }
            Files.delete(path);
        } else if (Files.isRegularFile(path)) Files.delete(path);
    }

    public static void recursivelyDeleteFolder(Path path) throws IOException {
        if(Files.isDirectory(path)) {
            List<Path> paths = Files.list(path).collect(Collectors.toList());
            for(Path childFile : paths) {
                if(Files.isDirectory(childFile)) {
                    recursivelyDeleteFolder(childFile);
                }
                else {
                    Files.delete(childFile);
                }
            }
            Files.delete(path);
        } else if (Files.isRegularFile(path)) Files.delete(path);
    }
}
