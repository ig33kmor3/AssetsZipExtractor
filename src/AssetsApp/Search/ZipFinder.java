package AssetsApp.Search;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ZipFinder {

    private List<File> recursiveZipSearchList = new ArrayList<File>();

    public List<File> getRecursiveZipSearchList() {
        return this.recursiveZipSearchList;
    }

    public void searchDirectoryListing(File directory){
        File[] zipFileArray = directory.listFiles();
        try{
            for (File file : zipFileArray){
                if(file.isDirectory()){
                    searchDirectoryListing(file);
                } else {
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
            this.recursiveZipSearchList.add(file);
        }
    }

    public void resetZipList(){
        this.recursiveZipSearchList.clear();
    }
}
