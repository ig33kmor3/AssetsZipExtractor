package AssetsApp.Extraction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Extractor {

    public static void unZip(File zipFile, File outputDirectory){
        try{
            byte[] buffer = new byte[4096];
            ZipInputStream zipInput = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry zipEntry = zipInput.getNextEntry();
            while(zipEntry != null){
                String zipName = zipEntry.getName();
                File newFile = new File(outputDirectory + File.separator + zipName);
                System.out.println("File Unzip: " + newFile.getAbsolutePath());
                if(zipEntry.isDirectory()){
                    File newDirectory = new File(newFile.getAbsolutePath());
                    if(!newDirectory.exists()){
                        newDirectory.mkdirs();
                    }
//                } else if(zipEntry.getName().toLowerCase().endsWith(".zip")){
////                    new File(newFile.getParent()).mkdirs();
////                    ZipOutputStream zipOutput = new ZipOutputStream(new FileOutputStream(newFile));
////                    zipOutput.putNextEntry(zipEntry);
////                    int len;
////                    while ((len = zipInput.read(buffer)) > 0){
////                        zipOutput.write(buffer, 0, len);
////                    }
////                    zipOutput.close();
//                    System.out.println("Discovered a Zip File: " + zipEntry.getName());
                } else {
                    new File(newFile.getParent()).mkdirs();
                    FileOutputStream fileOutput = new FileOutputStream(newFile);
                    int len;
                    while ((len = zipInput.read(buffer)) > 0){
                        fileOutput.write(buffer, 0, len);
                    }
                    fileOutput.close();
                }
                zipEntry = zipInput.getNextEntry();
            }
            zipInput.closeEntry();
            zipInput.close();
        } catch (IOException message){
            System.out.println(message.getStackTrace());
        }
    }
}
