package HelperClasses;

import com.sun.jna.*;

/*
    Init object as below (in Internal state I guess)
    POSIX posix = (POSIX) Native.load("c",POSIX.class);

    All function prototypes of POSIX functions go here!

    If you want exceptions to be thrown, add "throws com.sun.jna.LastErrorException"
    By default, no response is returned (failure in this case)

    mappings for data types : https://github.com/java-native-access/jna/blob/master/www/Mappings.md
    Other stuff : https://github.com/java-native-access/jna
 */



public interface POSIX extends Library {

    //Is this needed??
//    @Structure.FieldOrder({"O_RDONLY","O_WRONLY","O_RDWR"})
//    class mode_t extends Structure {
//        public short O_RDONLY;
//        public short O_WRONLY;
//        public short O_RDWR;
//    }

    int chmod(String filepath,int mode) throws com.sun.jna.LastErrorException;
    int chown(String filepath,int owner, int group) throws com.sun.jna.LastErrorException;
    int creat(String filepath,int mode) throws com.sun.jna.LastErrorException;
}