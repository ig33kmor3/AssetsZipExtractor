package AssetsApp.Extraction;

import javafx.scene.control.TextArea;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Extractor {

    public static void unZip(File zipFile, File outputDirectory){
        try{
            byte[] buffer = new byte[4096];
            ZipInputStream zipInput = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
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
                } else {
                    new File(newFile.getParent()).mkdirs();
                    BufferedOutputStream bufferedOutput = new BufferedOutputStream(new FileOutputStream(newFile));
                    int len;
                    while ((len = zipInput.read(buffer)) > 0){
                        bufferedOutput.write(buffer, 0, len);
                    }
                    bufferedOutput.close();
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
