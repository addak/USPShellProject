package HelperClasses;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 *  All the shell functions go here I guess
 *
 *  Note:-
 *  1.  The list function works fine
 *  2. The copy and move functions will delete the destination folder and file(duh) if it exists, Need to modify it such that
 *      it overwrites only contents in destination that match/exist
 *  3.  The delete function is modified a bit (Probably works)
 *  4. Rest of the Code not tested at the moment
 */

public class ShellVerbs {

    private static Scanner reader;

    public static void setScanner(Scanner obj){
        reader = obj;
    }

    public static Object listFiles(String[] values) throws IOException {
        Path path = Paths.get(values[0]);
        return Files.list(path)
                .collect(Collectors.toList());
    }

    public static Object copyFile(String[] values) throws IOException{
        Path sourceFile = Paths.get(values[0]);
        Path destinationFile = Paths.get(values[1]);

        if(Files.exists(sourceFile)){
            Path parent = destinationFile.getParent();

            if(Files.exists(destinationFile)){
                System.out.println("Destination exists already. Do you wish to overwrite it? (Y/N)");
                String decision = reader.nextLine().toUpperCase();

                if(decision.equals("Y")){
                    recursiveDelete(destinationFile);
                    recursiveCopy(sourceFile, destinationFile);
                }
            }
            else{

                if(!Files.exists(parent)){
                    Files.createDirectories(parent);
                }

                recursiveCopy(sourceFile, destinationFile);
            }


        }

        return null;
    }

    private static void recursiveCopy(Path srcPath, Path desPath) throws IOException{

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

    public static Object moveFile(String[] values) throws IOException{
        Path sourceFile = Paths.get(values[0]);
        Path destinationFile = Paths.get(values[1]);

        if(Files.exists(sourceFile)){
            Path parent = destinationFile.getParent();

            if(Files.exists(destinationFile)){
                System.out.println("Destination exists already. Do you wish to overwrite it? (Y/N)");
                String decision = reader.nextLine().toUpperCase();

                if(decision.equals("Y")){
                    recursiveDelete(destinationFile);
                    recursiveMove(sourceFile, destinationFile);
                }
            }
            else{

                if(!Files.exists(parent)){
                    Files.createDirectories(parent);
                }

                recursiveMove(sourceFile, destinationFile);
            }


        }

        return null;
    }

    private static void recursiveMove(Path srcPath, Path desPath) throws IOException{

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

    public static Void deleteFile(String[] values) throws IOException{
        Path deleteFile = Paths.get(values[0]);

        if( Files.exists(deleteFile) ){ recursiveDelete(deleteFile); }

        return null;
    }

    private static void recursiveDelete(Path path) throws IOException{

        if(!Files.isRegularFile(path)){
            Files.delete(path);
            return;
        }

        List<Path> contents = Files.list(path).collect(Collectors.toList());

        if(contents.isEmpty()){
            Files.delete(path);
            return;
        }

        for( Path content : contents){
            if(!Files.isSymbolicLink(content)){
                recursiveDelete(content);
            }
        }
    }
}
