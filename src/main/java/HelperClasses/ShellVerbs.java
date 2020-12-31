package HelperClasses;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *  All the shell functions go here I guess
 *
 *  Note:-
 *  1.  The list function works fine
 *  2. The copy and move functions will delete the destination folder and file(duh) if it exists, Need to modify it such that
 *      it overwrites only contents in destination that match/exist
 *  3. Rest of the Code not tested at the moment
 */

public class ShellVerbs {

    public static Object clearScreen(String[] params) {
        //Doesn't work in IntelliJ's console but
        //it should work in a regular cmd window
        /*
            Windows version of this code, no longer used
            try{
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                System.out.println();
            } catch (Exception e) {}
         */
        System.out.print("\033[H\033[2J");
        System.out.flush();
        return null;
    }

    public static Object presentWorkingDirectory(String[] params) {
        System.out.println(Colour.GREEN
                + InternalState.getInstance().getPresentWorkingDirectory().toString()
                + Colour.RESET);
        return null;
    }

    public static Object changeDirectory(String[] params){
        if(params == null || params.length == 0 || params[0].equals("."))
            return null;
        else if (params[0].equals("..")) {

            Path pwd = InternalState.getInstance().getPresentWorkingDirectory();
            if(pwd.getParent() != null)
                InternalState.getInstance().setPresentWorkingDirectory(pwd.getParent());
        }
        else {
            Pattern absPathPattern = Pattern.compile("^[A-Z]:.*");
            if(params[0].startsWith("."+ File.separator)){
                params[0] = InternalState.getInstance().getPresentWorkingDirectory().toString() + params[0].substring(1);
            }
            else if(params[0].startsWith(File.separator)){
                Path pwd = InternalState.getInstance().getPresentWorkingDirectory().getRoot();
                params[0] = pwd.toString() + params[0].substring(1);
            }
            else if(!absPathPattern.matcher(params[0]).matches())
                params[0] = InternalState.getInstance().getPresentWorkingDirectory().toString() + "/" + params[0];

            Path destination = Paths.get(params[0]);
            if(Files.exists(destination))
                InternalState.getInstance().setPresentWorkingDirectory(destination);
            else System.out.println(Colour.RED + "Specified path can't be found." + Colour.RESET);
        }
        return null;
    }

    public static Object listFiles(String[] params) throws IOException {
        Path path;
        if(params[0].length() == 0 || params[0].equals(".")){
            params[0] = InternalState.getInstance().getPresentWorkingDirectory().toString();
        }
        else if (params[0].equals("..")) {

            Path pwd = InternalState.getInstance().getPresentWorkingDirectory();
            if(pwd.getParent() != null)
                params[0] = pwd.getParent().toString();
            else
                params[0] = pwd.toString();

        }
        else{
            Pattern absPathPattern = Pattern.compile("^[A-Z]:.*");
            if(params[0].startsWith("."+ File.separator)){
                params[0] = InternalState.getInstance().getPresentWorkingDirectory().toString() + params[0].substring(1);
            }
            else if(params[0].startsWith(File.separator)){
                Path pwd = InternalState.getInstance().getPresentWorkingDirectory().getRoot();
                params[0] = pwd.toString() + params[0].substring(1);
            }
            else if(!absPathPattern.matcher(params[0]).matches())
                params[0] = InternalState.getInstance().getPresentWorkingDirectory().toString() + "/" + params[0];
        }

        path = Paths.get(params[0]);

//        else {
//            if(values[0].charAt(0) != '/') {
//                values[0] = InternalState.getInstance().getPresentWorkingDirectory().toString() + "/" + values[0];
//            }
//            path = Paths.get(values[0]);
//        }
        List<Path> paths = Files.list(path).collect(Collectors.toList());
        for(Path pathOnj : paths){
            if(Files.isRegularFile(pathOnj))
                System.out.println(Colour.YELLOW + pathOnj.getFileName() + Colour.RESET);
            else
                System.out.println(Colour.CYAN + pathOnj.getFileName() + Colour.RESET);
        }
        return null;
    }

    public static Object copyFile(String[] values) throws IOException{
        if(values[0].equals("")) return null;
        String sourceFilePath, destinationFilePath;
        String[] paths = InternalFunctions.parsePaths(values[0]);
        sourceFilePath = paths[0];
        destinationFilePath = paths[1];

        Path sourceFile = Paths.get(sourceFilePath);
        Path destinationFile = Paths.get(destinationFilePath);

        if(Files.exists(sourceFile)){
            Path parent = destinationFile.getParent();

            if(Files.exists(destinationFile)){
                System.out.println("Destination exists already. Do you wish to overwrite it? (Y/N)");
                String decision = InternalState.getScanner().nextLine().toUpperCase();

                if(decision.equals("Y")){
                    InternalFunctions.recursiveDelete(destinationFile);
                    InternalFunctions.recursiveCopy(sourceFile, destinationFile);
                }
            }
            else{

                if(!Files.exists(parent)){
                    Files.createDirectories(parent);
                }

                InternalFunctions.recursiveCopy(sourceFile, destinationFile);
            }


        } else {
            System.out.println(Colour.RED + "Specified path can't be found." + Colour.RESET);
        }

        return null;
    }

    public static Object moveFile(String[] values) throws IOException{
        if(values[0].equals("")) return null;
        String sourceFilePath, destinationFilePath;
        String[] paths = InternalFunctions.parsePaths(values[0]);
        sourceFilePath = paths[0];
        destinationFilePath = paths[1];

        Path sourceFile = Paths.get(sourceFilePath);
        Path destinationFile = Paths.get(destinationFilePath);

        if(Files.exists(sourceFile)){
            Path parent = destinationFile.getParent();

            if(Files.exists(destinationFile)){
                System.out.println("Destination already exists. Do you wish to overwrite it? (Y/N)");
                String decision = InternalState.getScanner().nextLine().toUpperCase();

                if(decision.equals("Y")){
                    InternalFunctions.recursiveDelete(destinationFile);
                    InternalFunctions.recursiveMove(sourceFile, destinationFile);
                }
            }
            else{

                if(!Files.exists(parent)){
                    Files.createDirectories(parent);
                }

                InternalFunctions.recursiveMove(sourceFile, destinationFile);
            }


        }

        return null;
    }

    public static Void deleteFile(String[] values) throws IOException{
        if(values[0].charAt(0) != '/')
            values[0] = InternalState.getInstance().getPresentWorkingDirectory().toString() + '\\' + values[0];
        Path deleteFile = Paths.get(values[0]);
        if(Files.exists(deleteFile)) InternalFunctions.recursiveDelete(deleteFile);
        return null;
    }
}
