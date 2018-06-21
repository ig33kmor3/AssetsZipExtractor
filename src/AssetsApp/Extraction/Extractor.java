package AssetsApp.Extraction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fileOutput = new FileOutputStream(newFile);
                int len;
                while ((len = zipInput.read(buffer)) > 0){
                    fileOutput.write(buffer, 0, len);
                }
                fileOutput.close();
                zipEntry = zipInput.getNextEntry();
            }
            zipInput.closeEntry();
            zipInput.close();
        } catch (IOException message){
            message.getStackTrace();
        }
    }
}
