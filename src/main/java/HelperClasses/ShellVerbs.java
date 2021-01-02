package HelperClasses;

import Parser.*;
import com.sun.jna.Native;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    static POSIX posixFns = Native.load("c",POSIX.class);

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
        HashSet<Character> params = new HashSet<>();
        String paramString = parameters.size() != 0 ? parameters.get(0) : "";
        for(int i=0; i < paramString.length(); i++)
            params.add(paramString.charAt(i));

        List<Path> paths = Files.list(path).collect(Collectors.toList());
        for(Path pathOnj : paths){
            InternalFunctions.lsParam(pathOnj,params);
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

        HashSet<Character> params = new HashSet<>();
        String paramString = parameters.size() != 0 ? parameters.get(0) : "";
        for(int i=0; i < paramString.length(); i++)
            params.add(paramString.charAt(i));

        if(Files.exists(sourceFile)){
            Path parent = destinationFile.getParent();

            if(Files.exists(destinationFile)){
                if(params.contains('i'))
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

        HashSet<Character> params = new HashSet<>();
        String paramString = parameters.size() != 0 ? parameters.get(0) : "";
        for(int i=0; i < paramString.length(); i++)
            params.add(paramString.charAt(i));

        if(Files.exists(sourceFile)){
            Path parent = destinationFile.getParent();

            if(Files.exists(destinationFile)){
                if(params.contains('i'))
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
        HashSet<Character> params = new HashSet<>();
        String paramString = parameters.size() != 0 ? parameters.get(0) : "";
        for(int i=0; i < paramString.length(); i++)
            params.add(paramString.charAt(i));

        for(String path : arguments) {
            if(path.charAt(0) != '/')
                path = InternalState.getInstance().getPresentWorkingDirectory().toString() + '/' + path;
            Path deleteFile = Paths.get(path);
            if(Files.exists(deleteFile)) {

                if(Files.isDirectory(deleteFile) && Files.list(deleteFile).count() > 0 && !params.contains('r')){
                    System.out.println(Colour.RED + "Directory isn't empty" + Colour.RESET);
                    return null;
                }

                InternalFunctions.recursiveDelete(deleteFile);
            }
        }
        return null;
    }

    public static void chmod(ArrayList<String> parameters, ArrayList<String> arguments) throws IOException{

        Path filePath = Paths.get(arguments.get(0));

        if(!Files.exists(filePath)) {
            System.out.println(Colour.RED + "File doesn't exist");
            return;
        }
        else if(parameters.size() == 0)
            return;

        //https://www.programcreek.com/java-api-examples/?api=java.nio.file.attribute.PosixFilePermission
        String currentPerm = PosixFilePermissions.toString(Files.getPosixFilePermissions(filePath));

        boolean[] setPerm = new boolean[3];

        setPerm[0] = currentPerm.charAt(0) == 'r';
        setPerm[1] = currentPerm.charAt(1) == 'w';
        setPerm[2] = currentPerm.charAt(2) == 'x';

        int gPerm = 0;
        gPerm += currentPerm.charAt(3) == 'r' ? 3 : 0;
        gPerm += currentPerm.charAt(4) == 'w' ? 2 : 0;
        gPerm += currentPerm.charAt(5) == 'x' ? 1 : 0;

        int oPerm = 0;
        oPerm += currentPerm.charAt(6) == 'r' ? 3 : 0;
        oPerm += currentPerm.charAt(7) == 'w' ? 2 : 0;
        oPerm += currentPerm.charAt(8) == 'x' ? 1 : 0;


        String paramString = parameters.get(0) ;

        boolean add = false;

        for(int i=0; i < paramString.length(); i++){

            if( paramString.charAt(i) == '+') {
                add = true;
                continue;
            }
            else if( paramString.charAt(i) == '-') {
                add = false;
                continue;
            }

            if(add){
                if(paramString.charAt(i) == 'r')
                    setPerm[0] = true;
                else if( paramString.charAt(i) == 'w')
                    setPerm[1] = true;
                else if(paramString.charAt(i) == 'x')
                    setPerm[2] = true;

            }
            else{
                if(paramString.charAt(i) == 'r')
                    setPerm[0] = false;
                else if( paramString.charAt(i) == 'w')
                    setPerm[1] = false;
                else if(paramString.charAt(i) == 'x')
                    setPerm[2] = false;

            }
        }

        int currentValueOwnerPerm = 0;

        currentValueOwnerPerm += setPerm[0] ? 3 : 0;
        currentValueOwnerPerm += setPerm[1] ? 2 : 0;
        currentValueOwnerPerm += setPerm[2] ? 1 : 0;

        String mod = "0" + currentValueOwnerPerm + gPerm + oPerm;

        posixFns.chmod(filePath.toAbsolutePath().toString(), Integer.parseInt(mod));
    }

    public static String execute(ArrayList<String> parameters, ArrayList<String> arguments) throws Exception {
        boolean shouldCapture = false;
        if(arguments.size() > 0 && arguments.get(0).charAt(1) == 'c') shouldCapture = true;
        String[] command = new String[arguments.size()]; int i = 0;
        for(String argument: arguments) {
            command[i++] = argument;
        }
        ProcessBuilder processBuilder = new ProcessBuilder()
                .command(command)
                .directory(InternalState.getInstance()
                        .getPresentWorkingDirectory()
                        .toFile()
                );
        if(shouldCapture) {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(
              new InputStreamReader(process.getInputStream())
            );
            String line;
            StringBuilder output = new StringBuilder();
            while((line = reader.readLine()) != null) output.append(
              line + System.lineSeparator()
            );
            return output.toString();
        }
        else {
            int exitCode = processBuilder.inheritIO().start().waitFor();
            return null;
        }
    }
}
