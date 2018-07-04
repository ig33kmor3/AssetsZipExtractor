package AssetsApp.Search;

import java.io.File;
import java.util.*;

public class XMLFinder {

    private Map<String, File> recursiveXMLMap = new HashMap<>();
    private HashSet<File> recursiveXMLSearchSet = new HashSet<>();

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
            createXMLSearchSet();
        } catch(NullPointerException message){
            message.getStackTrace();
        }
    }

    private void checkForXMLFileExtension(File file){
        if(file.getName().toLowerCase().endsWith(".xml") && !this.recursiveXMLMap.containsKey(file.getName())){
            this.recursiveXMLMap.put(file.getName(), file);
        }
    }

    private void createXMLSearchSet(){
        for(String key : this.recursiveXMLMap.keySet()){
            File setFile = this.recursiveXMLMap.get(key);
            this.recursiveXMLSearchSet.add(setFile);
        }
    }
}
