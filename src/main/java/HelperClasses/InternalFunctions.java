package HelperClasses;

import Interfaces.VerbMapFunction;
import Models.Colour;
import Models.InternalState;
import Models.ParserOutput;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
    Contains functions that are common between multiple
    commands
 */

public class InternalFunctions {

    //Generates output for ls command according to parameters passed
    public static void lsParam(Path p, HashSet<Character> args) throws IOException {

        if(!args.contains('a') && Files.isHidden(p))
            return;

        StringBuilder outputBuilder = new StringBuilder();

        if(args.contains('l')){
            BasicFileAttributes filesAttributes = Files.readAttributes(p, BasicFileAttributes.class);

            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd hh:mm:ss");
            String lastModTime = formatter.format(new Date(filesAttributes.lastModifiedTime().toMillis()));

            outputBuilder.append(String.format("%-10d %-16s ", filesAttributes.size(), lastModTime));
        }

        if(Files.isRegularFile(p))
            outputBuilder.append(Colour.YELLOW).append(p.getFileName()).append(Colour.RESET);
        else
            outputBuilder.append(Colour.CYAN).append(p.getFileName()).append(Colour.RESET);

        System.out.println(outputBuilder);

    }

    //Recursively copies file/folder from source to destination
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

    //Recursively moves file/folder from source to destination
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

    //Recursively deletes file/folder from source to destination
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

    //Returns absolute path for any given path
    public static Path getPath(String pathString){

        String absPath = "";

        if(pathString.equals("."))
            absPath = InternalState.getInstance().getPresentWorkingDirectory().toString();
        else if (pathString.equals("..")) {

            Path pwd = InternalState.getInstance().getPresentWorkingDirectory();
//            if(pwd.getParent() != null)
//                arguments.get(0) = pwd.getParent().toString();
            if(pwd.getParent() != null)
                absPath = pwd.getParent().toString();
//            else
//                arguments.get(0) = pwd.toString();
            else
                absPath = pwd.toString();

        }
        else{
            Pattern absPathPattern = Pattern.compile("^[A-Z]:.*");
            if(pathString.startsWith("."+ File.separator)){
//                arguments.get(0) = InternalState.getInstance().getPresentWorkingDirectory().toString() + arguments.get(0).substring(1);
                absPath = InternalState.getInstance().getPresentWorkingDirectory().toString() + pathString.substring(1);
            }
            else if(pathString.startsWith(File.separator)){
                Path pwd = InternalState.getInstance().getPresentWorkingDirectory().getRoot();
//                arguments.get(0) = pwd.toString() + arguments.get(0).substring(1);
                absPath = pwd.toString() + pathString.substring(1);
            }
            else if(!absPathPattern.matcher(pathString).matches())
//                arguments.get(0) = InternalState.getInstance().getPresentWorkingDirectory().toString() + "/" + arguments.get(0);
                absPath =  InternalState.getInstance().getPresentWorkingDirectory().toString() + "/" + pathString;
        }

        return Paths.get(absPath);
    }

    /*
        Responsible for executing commands and piping
        output from one command to another. This is also
        used by history command hence it's a separate function
        instead of just being part of the main method.
     */
    public static void executeCommands(ArrayList<ParserOutput> parserOutput) throws Exception{
        ParserOutput output = parserOutput.get(0);
        String commandOutput = null;
        if(output.getCommand().equals("quit")) System.exit(0);
        else if(output.getCommand().equals("history")) {
            InternalState.getInstance().getCommandHistory().removeFirst();
        }
        VerbMapFunction function = InternalState.getMap().get(output.getCommand());
        if(function != null) {
            commandOutput = (String) function.call(output.getParameters(), output.getArguments());
            if(commandOutput == null) commandOutput = "";
        } else {
            System.out.println(Colour.RED + output.getCommand() +" : command not found" + Colour.RESET);
            return;
        }
        for(int i = 1; i < parserOutput.size(); ++i) {
            output = parserOutput.get(i);
            output.getArguments().add(commandOutput);
            if(output.getCommand().equals("quit")) return;
            function = InternalState.getMap().get(output.getCommand());
            if(function != null) {
                commandOutput = (String) function.call(output.getParameters(), output.getArguments());
                if(commandOutput == null) commandOutput = "";
            } else {
                System.out.println(Colour.RED + output.getCommand() +" : command not found" + Colour.RESET);
                continue;
            }
        }
    }
}
