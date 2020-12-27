package HelperClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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

            if( !Files.exists(parent) )
                Files.createDirectories(parent);

//            if(Files.isDirectory(sourceFile) && Files.list(sourceFile).count() > 0){
//                //Have to recursively move files from subtree... gotta use walk
//            }

            Files.move(sourceFile, destinationFile);
        }

        return null;
    }
    
    public static Object moveFile(String[] values) throws IOException{
        Path sourceFile = Paths.get(values[0]);
        Path destinationFile = Paths.get(values[1]);

        if(Files.exists(sourceFile)){
            Path parent = destinationFile.getParent();

            if( !Files.exists(parent) )
                Files.createDirectories(parent);

//            if(Files.isDirectory(sourceFile) && Files.list(sourceFile).count() > 0){
//                //Have to recursively move files from subtree... gotta use walk
//            }
            else{

                if(Files.exists(destinationFile)){
                    System.out.println("Destination exists already. Do you wish to overwrite it? (Y/N)");
                    String decision = reader.nextLine().toUpperCase();

                    if(decision.equals("Y")){
                        Files.move(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                    }
//                    else{
//                        //revert directory sub-tree creation , if any
//                    }
                }
                else{
                    Files.move(sourceFile, destinationFile);
                }
            }


        }
        return null;
    }

    public static void deleteFile(String path) throws IOException{
        Path deleteFile = Paths.get(path);

        if( Files.exists(deleteFile) ){

            if( Files.isDirectory(deleteFile) && Files.list(deleteFile).count() > 0){
                // Have to recursively delete files... gotta use walk

                recursiveDelete(deleteFile);
                return;
            }

            Files.delete(deleteFile);
        }
    }

    public static void recursiveDelete(Path path) throws IOException{

        if(!Files.isDirectory(path)){
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
