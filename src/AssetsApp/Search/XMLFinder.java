package AssetsApp.Search;

import java.io.File;
import java.util.*;

public class XMLFinder {

    private List<File> recursiveXMLSearchList = new ArrayList<>();
    private Map<String, File> xmlMap = new HashMap<>();
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
        } catch(NullPointerException message){
            message.getStackTrace();
        }
        eliminateXMLDuplication();
    }

    private void checkForXMLFileExtension(File file){
        String filename = file.getName();
        if(filename.toLowerCase().endsWith(".xml")){
            this.recursiveXMLSearchList.add(file);
        }
    }

    private void eliminateXMLDuplication(){
        for(File listFile : this.recursiveXMLSearchList){
            if(!this.xmlMap.containsKey(listFile.getName())){
                this.xmlMap.put(listFile.getName(), listFile);
            }
        }
        createSet();
    }

    private void createSet(){
        for(String key : this.xmlMap.keySet()){
            File setFile = this.xmlMap.get(key);
            this.recursiveXMLSearchSet.add(setFile);
        }
    }
}
