package AssetsApp.Search;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ZipFinder {

    private List<File> listOfZips = new ArrayList<File>();

    public ZipFinder(){
        this.listOfZips = null;
    }

    public List<File> getListOfZips() {
        return listOfZips;
    }

    public void searchDirectoryListing(File directory){
        File[] zipFileArray = directory.listFiles();
        try{
            for (File file : zipFileArray){
                if(file.isDirectory()){
                    System.out.println(file.getAbsolutePath());
                    searchDirectoryListing(file);
                } else {
                    System.out.println(file.getAbsolutePath());
                    checkForZipFileExtension(file);
                }
            }
        } catch(NullPointerException message){
            message.getStackTrace();
        }
    }

    private void checkForZipFileExtension(File file){
        String filename = file.getName();
        if(filename.toLowerCase().endsWith(".zip")){
            listOfZips.add(file);
            System.out.println(filename + " " + "is a zip file!");

        }
    }
}
