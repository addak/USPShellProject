package HelperClasses;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
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

    public static Object clearScreen(ArrayList<String> parameters, ArrayList<String> arguments) {
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

    public static Object presentWorkingDirectory(ArrayList<String> parameters, ArrayList<String> arguments) {
        System.out.println(Colour.GREEN
                + InternalState.getInstance().getPresentWorkingDirectory().toString()
                + Colour.RESET);
        return null;
    }

    public static Object changeDirectory(ArrayList<String> parameters, ArrayList<String> arguments){
        if(arguments == null || arguments.size() == 0 || arguments.get(0).equals("."))
            return null;
        else if (arguments.get(0).equals("..")) {

            Path pwd = InternalState.getInstance().getPresentWorkingDirectory();
            if(pwd.getParent() != null)
                InternalState.getInstance().setPresentWorkingDirectory(pwd.getParent());
        }
        else {
            Pattern absPathPattern = Pattern.compile("^[A-Z]:.*");
            if(arguments.get(0).startsWith("."+ File.separator)){
//                arguments.get(0) = InternalState.getInstance().getPresentWorkingDirectory().toString() + arguments.get(0).substring(1);
                arguments.set(0, InternalState.getInstance().getPresentWorkingDirectory().toString() + arguments.get(0).substring(1));
            }
            else if(arguments.get(0).startsWith(File.separator)){
                Path pwd = InternalState.getInstance().getPresentWorkingDirectory().getRoot();
//                arguments.get(0) = pwd.toString() + arguments.get(0).substring(1);
                arguments.set(0, pwd.toString() + arguments.get(0).substring(1));
            }
            else if(!absPathPattern.matcher(arguments.get(0)).matches())
//                arguments.get(0) = InternalState.getInstance().getPresentWorkingDirectory().toString() + "/" + arguments.get(0);
                arguments.set(0, InternalState.getInstance().getPresentWorkingDirectory().toString() + "/" + arguments.get(0));
            Path destination = Paths.get(arguments.get(0));
            if(Files.exists(destination))
                InternalState.getInstance().setPresentWorkingDirectory(destination);
            else System.out.println(Colour.RED + "Specified path can't be found." + Colour.RESET);
        }
        return null;
    }

    public static Object listFiles(ArrayList<String> parameters, ArrayList<String> arguments) throws IOException {
        Path path;
//        if(arguments.get(0).length() == 0 || arguments.get(0).equals(".")){
////            arguments.get(0) = InternalState.getInstance().getPresentWorkingDirectory().toString();
//            arguments.set(0, InternalState.getInstance().getPresentWorkingDirectory().toString());
//        }
        if(arguments.size() == 0)
            arguments.add(InternalState.getInstance().getPresentWorkingDirectory().toString());
        else if(arguments.get(0).equals("."))
            arguments.set(0, InternalState.getInstance().getPresentWorkingDirectory().toString());
        else if (arguments.get(0).equals("..")) {

            Path pwd = InternalState.getInstance().getPresentWorkingDirectory();
//            if(pwd.getParent() != null)
//                arguments.get(0) = pwd.getParent().toString();
            if(pwd.getParent() != null)
                arguments.set(0, pwd.getParent().toString());
//            else
//                arguments.get(0) = pwd.toString();
            else
                arguments.set(0, pwd.toString());

        }
        else{
            Pattern absPathPattern = Pattern.compile("^[A-Z]:.*");
            if(arguments.get(0).startsWith("."+ File.separator)){
//                arguments.get(0) = InternalState.getInstance().getPresentWorkingDirectory().toString() + arguments.get(0).substring(1);
                arguments.set(0, InternalState.getInstance().getPresentWorkingDirectory().toString() + arguments.get(0).substring(1));
            }
            else if(arguments.get(0).startsWith(File.separator)){
                Path pwd = InternalState.getInstance().getPresentWorkingDirectory().getRoot();
//                arguments.get(0) = pwd.toString() + arguments.get(0).substring(1);
                arguments.set(0, pwd.toString() + arguments.get(0).substring(1));
            }
            else if(!absPathPattern.matcher(arguments.get(0)).matches())
//                arguments.get(0) = InternalState.getInstance().getPresentWorkingDirectory().toString() + "/" + arguments.get(0);
                arguments.set(0, InternalState.getInstance().getPresentWorkingDirectory().toString() + "/" + arguments.get(0));
        }

        path = Paths.get(arguments.get(0));

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

    public static Object copyFile(ArrayList<String> parameters, ArrayList<String> arguments) throws IOException{
        if(arguments.get(0).equals("")) return null;
        String sourceFilePath = arguments.get(0), destinationFilePath = arguments.get(1);

        if(sourceFilePath.charAt(0) != '/')
            sourceFilePath = InternalState.getInstance().getPresentWorkingDirectory().toString() + '/' + sourceFilePath;

        if(destinationFilePath.charAt(0) != '/')
            destinationFilePath = InternalState.getInstance().getPresentWorkingDirectory().toString() + '/' + destinationFilePath;

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

    public static Object moveFile(ArrayList<String> parameters, ArrayList<String> arguments) throws IOException{
        if(arguments.get(0).equals("")) return null;
        String sourceFilePath = arguments.get(0), destinationFilePath = arguments.get(1);

        if(sourceFilePath.charAt(0) != '/')
            sourceFilePath = InternalState.getInstance().getPresentWorkingDirectory().toString() + '/' + sourceFilePath;

        if(destinationFilePath.charAt(0) != '/')
            destinationFilePath = InternalState.getInstance().getPresentWorkingDirectory().toString() + '/' + destinationFilePath;

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

    public static Void deleteFile(ArrayList<String> parameters, ArrayList<String> arguments) throws IOException{
        for(String path : arguments) {
            if(path.charAt(0) != '/')
                path = InternalState.getInstance().getPresentWorkingDirectory().toString() + '/' + path;
            Path deleteFile = Paths.get(path);
            if(Files.exists(deleteFile)) InternalFunctions.recursiveDelete(deleteFile);
        }
        return null;
    }
}
