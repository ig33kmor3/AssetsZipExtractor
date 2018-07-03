package AssetsApp.Search;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class XMLFinder {

    private List<File> recursiveXMLSearchList = new ArrayList<File>();
    private HashSet<File> recursiveXMLSearchSet;

    public Set<File> getRecursiveXMLSearchList() {
        return this.recursiveXMLSearchSet;
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
        this.recursiveXMLSearchSet =  new HashSet<>(this.recursiveXMLSearchList);
    }

    private void checkForXMLFileExtension(File file){
        String filename = file.getName();
        if(filename.toLowerCase().endsWith(".xml")){
            this.recursiveXMLSearchList.add(file);
        }
    }
}
