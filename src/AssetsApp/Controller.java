package AssetsApp;

import AssetsApp.Extraction.Extractor;
import AssetsApp.Search.XMLFinder;
import AssetsApp.Search.ZipFinder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Controller {

    @FXML
    private GridPane mainGridPane;
    @FXML
    private TextField textFieldZipFolderLocation;
    @FXML
    private Button btnZipFolderLocation;
    @FXML
    private TextField textFieldZipOutputLocation;
    @FXML
    private Button btnZipOutputLocation;
    @FXML
    private Button btnStartExtraction;
    @FXML
    private Button btnClear;
    @FXML
    private Label notificationArea;

    private File zipFile;
    private File zipOutputRootDirectory;
    private File zipOutputUncompressedDirectory;
    private File zipOutputXMLDirectory;
    private List<File> zipFileList;
    private List<File> xmlFileList;

    @FXML
    public void initialize() {
        this.textFieldZipFolderLocation.setEditable(false);
        this.textFieldZipOutputLocation.setEditable(false);
        this.notificationArea.setText("Select zip file and output location!");
    }

    @FXML
    public void onBrowseOrClearButtonClick(ActionEvent btnClicked) {
        if (btnClicked.getSource().equals(this.btnZipFolderLocation)) {
            this.zipFile = getZipFileDialog();
            checkForZipAndOutputLocation();
        } else if (btnClicked.getSource().equals(this.btnZipOutputLocation)) {
            this.zipOutputRootDirectory = getZipOutputDirectoryDialog();
            checkForZipAndOutputLocation();
        } else if (btnClicked.getSource().equals(this.btnClear)) {
            this.textFieldZipFolderLocation.clear();
            this.textFieldZipOutputLocation.clear();
            this.notificationArea.setText("Select zip file and output location!");
        }
    }

    private File getZipFileDialog(){
        FileChooser chooseZipFile = new FileChooser();
        chooseZipFile.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Zip Files", "*.zip"));
        File zipFile = chooseZipFile.showOpenDialog(mainGridPane.getScene().getWindow());
        if(zipFile != null){
            this.textFieldZipFolderLocation.setText(zipFile.getAbsolutePath());
            return zipFile;
        } else {
            this.notificationArea.setText("Zip file not selected!");
            return null;
        }
    }

    private File getZipOutputDirectoryDialog(){
        DirectoryChooser chooseZipOutputDirectory = new DirectoryChooser();
        File zipOutputDirectory = chooseZipOutputDirectory.showDialog(mainGridPane.getScene().getWindow());
        if(zipOutputDirectory != null){
            this.textFieldZipOutputLocation.setText(zipOutputDirectory.getAbsolutePath());
            return zipOutputDirectory;
        } else {
            this.notificationArea.setText("Zip output folder not selected!");
            return null;
        }
    }

    private void checkForZipAndOutputLocation(){
        if(this.zipFile != null && this.zipOutputRootDirectory != null){
            this.notificationArea.setText("Click Start Extraction to begin!");
        }
    }

    @FXML
    public void onStartButtonClick(){
        if(this.zipFile != null && this.zipOutputRootDirectory != null){
            createOutputFolderTree();
            startZipExtractionProcess();
            searchDirectoriesForXMLFiles();
            copyXMLToZipOutputXMLDirectory();
        } else {
            this.notificationArea.setText("Please select a zip file and output location!");
        }
    }

    private void createOutputFolderTree(){
        notificationArea.setText("Creating output directories!");
        this.zipOutputUncompressedDirectory = new File(this.zipOutputRootDirectory.getAbsolutePath() + File.separator + "00_uncompressedZipContents");
        this.zipOutputXMLDirectory = new File(this.zipOutputRootDirectory.getAbsolutePath() + File.separator + "01_xmlOutputContents");
        this.zipOutputUncompressedDirectory.mkdirs();
        this.zipOutputXMLDirectory.mkdirs();
    }

    private void startZipExtractionProcess(){
        this.notificationArea.setText("Take a break (don't exit). Extracting zip files ...");
        ZipFinder zipSearch = new ZipFinder();
        File zipFileParentDirectory;
        do{
            Extractor.unZip(this.zipFile, this.zipOutputUncompressedDirectory);
            zipSearch.searchDirectoryListing(this.zipOutputUncompressedDirectory);
            this.zipFileList = zipSearch.getRecursiveZipSearchList();
            while(!this.zipFileList.isEmpty()){
                for (File zipFile:this.zipFileList){
                    zipFileParentDirectory = new File(zipFile.getParent());
                    Extractor.unZip(zipFile, zipFileParentDirectory);
                    zipFile.delete();
                }
                zipSearch.resetZipList();
                zipSearch.searchDirectoryListing(this.zipOutputUncompressedDirectory);
            }
        } while(!this.zipFileList.isEmpty());
    }

    private void searchDirectoriesForXMLFiles(){
        this.notificationArea.setText("Searching for XML files ...");
        XMLFinder xmlSearch = new XMLFinder();
        xmlSearch.searchDirectoryListing(this.zipOutputUncompressedDirectory);
        this.xmlFileList = xmlSearch.getRecursiveXMLSearchList();
    }

    private void copyXMLToZipOutputXMLDirectory(){
        if(this.xmlFileList.isEmpty()){
            System.out.println("There are no .xml files in the directory tree!");
            this.notificationArea.setText("There were no XML files located. Please close program!");
        } else {
            this.notificationArea.setText("Moving XML files to " + this.zipOutputRootDirectory + File.separator + this.zipOutputXMLDirectory);
            for (File xmlFile:this.xmlFileList){
                Path sourcePath = Paths.get(xmlFile.getAbsolutePath());
                File destinationFile = new File(this.zipOutputXMLDirectory + File.separator + xmlFile.getName());
                Path destinationPath = Paths.get(destinationFile.getAbsolutePath());
                System.out.println("Copying " + xmlFile.getName() + " to xmlOutput directory.");
                try{
                    Files.copy(sourcePath, destinationPath);
                } catch(IOException message){
                    message.getStackTrace();
                }
            }
            int number = this.xmlFileList.size();
            String numberOfXMLs = Integer.toString(number);
            this.notificationArea.setText(numberOfXMLs + " extracted. Please close program!");
            this.xmlFileList.clear();
        }
    }
}