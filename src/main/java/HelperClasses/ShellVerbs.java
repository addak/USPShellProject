package HelperClasses;

import Interfaces.POSIX;
import Models.HistoryTableEntry;
import Models.InternalState;
import Models.JobTableEntry;
import com.sun.jna.Native;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.*;
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
            path = InternalState.getInstance().getPresentWorkingDirectory();
        else
            path = InternalFunctions.getPath(arguments.get(0));

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

    public static Void chmod(ArrayList<String> parameters, ArrayList<String> arguments) throws IOException{

        if(arguments.size() == 0){
            System.out.println(Colour.RED + "No arguments" + Colour.RESET);
            return null;
        }

        Path filePath = InternalFunctions.getPath(arguments.get(0));

        if(!Files.exists(filePath)) {
            System.out.println(Colour.RED + "File doesn't exist" +Colour.RESET);
            return null;
        }
        else if(parameters.size() == 0) {
            System.out.println(Colour.RED + "No paramters mentioned" + Colour.RESET);
            return null;
        }

        //https://www.programcreek.com/java-api-examples/?api=java.nio.file.attribute.PosixFilePermission
        String currentPerm = PosixFilePermissions.toString(Files.getPosixFilePermissions(filePath));

        boolean[] setPerm = new boolean[3];

        setPerm[0] = currentPerm.charAt(0) == 'r';
        setPerm[1] = currentPerm.charAt(1) == 'w';
        setPerm[2] = currentPerm.charAt(2) == 'x';

        int gPerm = 0;
        gPerm += currentPerm.charAt(3) == 'r' ? 4 : 0;
        gPerm += currentPerm.charAt(4) == 'w' ? 2 : 0;
        gPerm += currentPerm.charAt(5) == 'x' ? 1 : 0;

        int oPerm = 0;
        oPerm += currentPerm.charAt(6) == 'r' ? 4 : 0;
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

        currentValueOwnerPerm += setPerm[0] ? 4 : 0;
        currentValueOwnerPerm += setPerm[1] ? 2 : 0;
        currentValueOwnerPerm += setPerm[2] ? 1 : 0;

        String mod = "0" + currentValueOwnerPerm + gPerm + oPerm;
        int val = Integer.parseInt(mod,8);

        posixFns.chmod(filePath.toAbsolutePath().toString(), val);

        return null;
    }

    public static Void chown(ArrayList<String> parameters, ArrayList<String> arguments) throws IOException{

        if(arguments.size() != 2){
            System.out.println(Colour.RED + "Invalid no. of arguments" + Colour.RESET);
            return null;
        }
        else{
            String arg1 = arguments.get(0);
            String[] values = arg1.split(":");
            String userName = null, groupName = null;

            Path filePath = InternalFunctions.getPath(arguments.get(1));

            if(!arg1.contains(":")){
                userName = arg1;
            }
            else if( arg1.endsWith(":")){
                userName = groupName = values[0];
            }
            else if(values.length == 2){
                if(!values[0].isEmpty() && !values[1].isEmpty()){
                    userName = values[0];
                    groupName = values[1];
                }
                else if(values[0].isEmpty() && !values[1].isEmpty()){
                    groupName = values[1];
                }
                else{
                    System.out.println(Colour.RED + "Invalid format");
                    return null;
                }
            }

            int uId = -1,gId = -1;
            Process child;
            Scanner s;
            if(userName != null && !userName.isEmpty()){
                String command = "id -u " + userName;
                child = Runtime.getRuntime().exec(command);
                s = new Scanner(child.getInputStream());
                try{
                    uId = Integer.parseInt(s.nextLine());
                }
                catch (Exception e){
                    s = new Scanner(child.getErrorStream());
                    System.out.println(Colour.RED + s.nextLine() + Colour.RESET);
                    return null;
                }
            }

            if(groupName != null && !groupName.isEmpty()){
                String command = "id -g " + groupName;
                child = Runtime.getRuntime().exec(command);

                s = new Scanner(child.getInputStream());
                try{
                    gId = Integer.parseInt(s.nextLine());
                }
                catch (Exception e){
                    s = new Scanner(child.getErrorStream());
                    System.out.println(Colour.RED + s.nextLine() + Colour.RESET);
                    return null;
                }
            }

            posixFns.chown(filePath.toAbsolutePath().toString(),uId, gId);

        }

        return null;
    }

    public static String execute(ArrayList<String> parameters, ArrayList<String> arguments) throws Exception {
        boolean shouldCapture = false, shouldRunInBackground = false;
        if(parameters.size() > 0 && parameters.get(0).charAt(1) == 'c') shouldCapture = true;
        else if(parameters.size() > 0 && parameters.get(0).charAt(1) == 'b') shouldRunInBackground = true;

        StringBuilder fullCommand = new StringBuilder();
        String[] command = new String[arguments.size()]; int i = 0;
        for(String argument: arguments) {
            command[i++] = argument;
            fullCommand.append(argument).append(" ");
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
            while((line = reader.readLine()) != null) output.append(line)
                    .append(System.lineSeparator());
            return output.toString();
        } else if (shouldRunInBackground) {
            Process process = processBuilder.start();
            Long pid = process.pid();
            JobTableEntry entry = new JobTableEntry(fullCommand.toString(), process.toHandle());
            InternalState.getInstance().addJob(pid, entry);
            return null;
        } else {
            Process process = processBuilder.inheritIO().start();
            System.out.println(process.toHandle().pid());
            int exitCode = process.waitFor();
            return null;
        }
    }

    public static Void showBackgroundJobs(ArrayList<String> parameters, ArrayList<String> arguments) {
        HashMap<Long, JobTableEntry> jobTable = InternalState.getInstance().getJobTable();
        if(arguments.size() == 0) {
            System.out.println("Current background jobs: ");
            System.out.println("PID - Command - Alive");
            for(Long pid : jobTable.keySet()) {
                JobTableEntry entry = jobTable.get(pid);
                System.out.println(pid + " - " + entry.getFullCommand() + " - " + entry.getJobHandle().isAlive());
            }
        } else {
            System.out.println("PID - Command - Alive");
            for(String argument : arguments) {
                Long pid = null;
                try {
                    pid = Long.parseLong(argument);
                } catch (NumberFormatException e) {
                    System.out.println(Colour.RED + argument + " is not a valid PID" + Colour.RESET);
                }
                if(pid != null) {
                    JobTableEntry entry = InternalState.getInstance().getJobTable().getOrDefault(pid, null);
                    if(entry != null) {
                        System.out.println(pid + " - " + entry.getFullCommand() + " - " + entry.getJobHandle().isAlive());
                    }
                }
            }
        }
        return null;
    }

    public static Void killBackgroundJobs (ArrayList<String> parameters, ArrayList<String> arguments){
        if(arguments.size() > 0) {
            HashMap<Long, JobTableEntry> jobTable = InternalState.getInstance().getJobTable();
            if(arguments.get(0).charAt(0) == '*') {
                for(Long pid : jobTable.keySet()) {
                    ProcessHandle job = jobTable.get(pid).getJobHandle();
                    if(job.isAlive()) {
                        if(parameters.size() > 0 && parameters.get(0).charAt(1) == 'f') job.destroyForcibly();
                        else job.destroy();
                        jobTable.remove(pid);
                    }
                }
            } else {
                for(String argument : arguments) {
                    Long pid = null;
                    try {
                        pid = Long.parseLong(argument);
                    } catch (NumberFormatException e) {
                        System.out.println(Colour.RED + argument + " is not a valid PID" + Colour.RESET);
                    }
                    if(pid != null) {
                        ProcessHandle job = jobTable.get(pid).getJobHandle();
                        if(job.isAlive()) {
                            if(parameters.size() > 0 && parameters.get(0).charAt(1) == 'f') job.destroyForcibly();
                            else job.destroy();
                            jobTable.remove(pid);
                        }
                    }
                }
            }
        }
        return null;
    }

    public static Void createFile(ArrayList<String> parameters, ArrayList<String> arguments) throws Exception {
        boolean shouldOverwrite = false;
        Path path;

        if(parameters.size() > 0 && parameters.get(0).charAt(1) == 'f') shouldOverwrite = true;


        if(arguments.size() == 0) {
            System.out.println(Colour.RED + "create: no path or filename specified" + Colour.RESET);
            return null;
        } else {
            path = InternalFunctions.getPath(arguments.get(0));
            boolean res = new File(path.toString()).getParentFile().mkdirs();
        }

        if(shouldOverwrite) {
            Files.deleteIfExists(path);
        } else if(Files.exists(path)) {
            System.out.print(Colour.YELLOW + "File exists. Should overwrite?[Y/N]: " + Colour.RESET);
            if(InternalState.getScanner().nextLine().toUpperCase().charAt(0) == 'Y') Files.delete(path);
            else return null;
        }

        Files.createFile(path);
        new File(path.toString()).getParentFile().mkdirs();

        if(arguments.size() > 1) {
            String content = arguments.get(1);
            BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString()));
            writer.write(content + System.lineSeparator());
            writer.close();
        }

        return null;
    }

    public static Void displayFile (ArrayList<String> parameters, ArrayList<String> arguments) throws Exception {

        Path path;
        String line;

        if(arguments.size() == 0) {
            System.out.println(Colour.RED + "display: no path or filename specified" + Colour.RESET);
            return null;
        }

        path = InternalFunctions.getPath(arguments.get(0));

        if(!Files.exists(path)) {
            System.out.println(Colour.RED + "display: file does not exist" + Colour.RESET);
            return null;
        }

        if(!Files.isRegularFile(path)) {
            System.out.println(Colour.RED + "display: " + path.toString() + " is not a file" + Colour.RESET);
            return null;
        }

        BufferedReader reader = new BufferedReader(new FileReader(path.toString()));
        while((line = reader.readLine()) != null) System.out.println(line);
        return null;

    }

    public static Void commandHistory(ArrayList<String> parameters, ArrayList<String> arguments) throws Exception {
        LinkedList<HistoryTableEntry> commandHistory = InternalState.getInstance().getCommandHistory();
        if(arguments.size() == 0) {
            System.out.println("Last 10 commands (newest first):");
            Iterator<HistoryTableEntry> iterator = commandHistory.iterator();
            int i = 1;
            while(iterator.hasNext()) {
                System.out.println((i++) + ": " + iterator.next().getFullCommand());
            }
        } else {
            try {
                int index = Integer.parseInt(arguments.get(0));
                if(index > 10 || index < 1) {
                    System.out.println(Colour.RED + "history: index must be in the range 1-10" + Colour.RESET);
                    return null;
                } else {
                    HistoryTableEntry entry = commandHistory.get(index - 1);
                    InternalState.getInstance().updateCommandHistory(entry);
                    InternalFunctions.executeCommands(
                            entry.getParserOutput()
                    );
                }
            } catch (NumberFormatException e) {
                System.out.println(Colour.RED + "history: invalid index" + Colour.RESET);
            }
        }
        return null;
    }

    public static Void ravel(ArrayList<String> parameters, ArrayList<String> arguments) throws IOException{

        if(arguments.size() != 2){
            System.out.println(Colour.RED + "Invalid no. of args" +Colour.RESET);
            return null;
        }

        Path srcFile =  InternalFunctions.getPath(arguments.get(0));
        Path desFile = InternalFunctions.getPath(arguments.get(1));

        if(!Files.exists(srcFile)){
            System.out.println(Colour.RED + "Source File doesn't exist" + Colour.RESET);
            return null;
        }

        if(Files.exists(desFile)){
            System.out.print(Colour.YELLOW + "File exists. Should overwrite?[Y/N]: " + Colour.RESET);
            if(InternalState.getScanner().nextLine().toUpperCase().charAt(0) == 'Y') Files.delete(desFile);
        }
        else if(!Files.exists(desFile.getParent()))
            Files.createDirectories(desFile.getParent());

        Archiver compressor = ArchiverFactory.createArchiver(desFile.toFile());
        String extension = compressor.getFilenameExtension();

        String archiveName = desFile.getFileName().toString().replace(extension,"");

        compressor.create(archiveName, desFile.getParent().toFile(), srcFile.toFile());


        return null;
    }

    public static Void unravel(ArrayList<String> parameters, ArrayList<String> arguments) throws IOException{

        if(arguments.size() != 2){
            System.out.println(Colour.RED + "Invalid no. of args" +Colour.RESET);
            return null;
        }

        Path srcFile =  InternalFunctions.getPath(arguments.get(0));
        Path desFile = InternalFunctions.getPath(arguments.get(1));


        if(!Files.exists(srcFile)){
            System.out.println(Colour.RED + "Source File doesn't exist" + Colour.RESET);
            return null;
        }

        if(Files.exists(desFile)){
            System.out.print(Colour.YELLOW + "File exists. Should overwrite?[Y/N]: " + Colour.RESET);
            if(InternalState.getScanner().nextLine().toUpperCase().charAt(0) == 'Y') Files.delete(desFile);
        }
        else if(!Files.exists(desFile.getParent()))
            Files.createDirectories(desFile.getParent());

        Archiver extractor = ArchiverFactory.createArchiver(srcFile.toFile());

        extractor.extract(srcFile.toFile(), desFile.toFile());

        return null;
    }

    public static Void mkdir(ArrayList<String> parameters, ArrayList<String> arguments) throws IOException{

        if(arguments.size() == 0){
            System.out.println(Colour.RED + "Invalid no of args");
        }

        Path directoryPath = InternalFunctions.getPath(arguments.get(0));

        if(Files.exists(directoryPath))
            return null;

        if(parameters.size() != 0 && parameters.get(0).equals("-p")){
            Files.createDirectories(directoryPath);
        }
        else{
            Files.createDirectory(directoryPath);
        }
        return null;
    }

}
