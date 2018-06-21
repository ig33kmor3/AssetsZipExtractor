package AssetsApp.Search;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class ZipFinder {

    public static void searchForZip(File directory){

//        FileFilter zipSearchFilter = new FileFilter() {
//            @Override
//            public boolean accept(File pathname) {
//                if(pathname.getName().toLowerCase().endsWith("zip")){
//                   return true;
//                }
//                return false;
//            }
//        };

        File[] zipFileArray = directory.listFiles();
        try{
            for (File file : zipFileArray){
                if(file.isDirectory()){
                    System.out.println(file.getAbsolutePath());
                    searchForZip(file);
                } else {
                    System.out.println(file.getAbsolutePath());
                }
            }
        } catch(NullPointerException message){
            message.getStackTrace();
        }
    }
}
