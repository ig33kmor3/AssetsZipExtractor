package AssetsApp.Search;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XMLFinder {

    private List<File> recursiveXMLSearchList = new ArrayList<File>();

    public List<File> getRecursiveXMLSearchList() {
        return this.recursiveXMLSearchList;
    }

    public void searchDirectoryListing(File directory){
        File[] xmlFileArray = directory.listFiles();
        try{
            for (File file : xmlFileArray){
                if(file.isDirectory()){
                    searchDirectoryListing(file);
                } else {
                    checkForXMLFileExtension(file);
                }
            }
        } catch(NullPointerException message){
            message.getStackTrace();
        }
    }

    private void checkForXMLFileExtension(File file){
        String filename = file.getName();
        if(filename.toLowerCase().endsWith(".xml")){
            this.recursiveXMLSearchList.add(file);
        }
    }
}
