package AssetsApp;

import AssetsApp.Extraction.Extractor;
import AssetsApp.Search.XMLFinder;
import AssetsApp.Search.ZipFinder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
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
    @FXML
    private ProgressIndicator progressIndicator;

    private File zipFile;
    private File zipOutputRootDirectory;
    private File zipOutputUncompressedDirectory;
    private File zipOutputXMLDirectory;
    private List<File> zipFileList = Collections.synchronizedList(new ArrayList<>());
    private List<File> xmlFileList = Collections.synchronizedList(new ArrayList<>());

    @FXML
    public void initialize() {
        this.notificationArea.setText("Select a zip file and extract location!");
        this.progressIndicator.setVisible(false);
        this.textFieldZipFolderLocation.setEditable(false);
        this.textFieldZipOutputLocation.setEditable(false);
        this.btnStartExtraction.setDisable(true);
        this.btnClear.setDisable(true);
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
            this.btnClear.setDisable(true);
            this.btnStartExtraction.setDisable(true);
            this.notificationArea.setText("Select a zip file and extract location!");
            this.progressIndicator.setVisible(false);
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
            this.btnClear.setDisable(false);
            this.btnStartExtraction.setDisable(false);
            this.notificationArea.setText("Click Start Extraction to begin!");
        }
    }

    @FXML
    public void onStartButtonClick(){
        if(this.zipFile != null && this.zipOutputRootDirectory != null){
            Runnable startExtraction = () -> {
                startProgressIndication();
                createOutputFolderTree();
                startZipExtractionProcess();
                searchDirectoriesForXMLFiles();
                copyXMLToZipOutputXMLDirectory();
            };
            new Thread(startExtraction).start();
        } else {
            this.notificationArea.setText("Please select a zip file and extract location!");
        }
    }

    private void startProgressIndication(){
        Platform.runLater(()->{
            this.progressIndicator.setVisible(true);
            this.progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        });
    }

    private void createOutputFolderTree(){
        preventButtonInteraction();
        Platform.runLater(()->{
            this.notificationArea.setText("Take a break. Performing operation!");
        });
        this.zipOutputUncompressedDirectory = new File(this.zipOutputRootDirectory.getAbsolutePath() + File.separator + "uncompressedZipContents");
        this.zipOutputXMLDirectory = new File(this.zipOutputRootDirectory.getAbsolutePath() + File.separator + "xmlOutputContents");
        this.zipOutputUncompressedDirectory.mkdirs();
        this.zipOutputXMLDirectory.mkdirs();
    }

    private void preventButtonInteraction(){
        Platform.runLater(()->{
            this.btnZipFolderLocation.setDisable(true);
            this.btnZipOutputLocation.setDisable(true);
            this.btnStartExtraction.setDisable(true);
            this.btnClear.setDisable(true);
        });
    }

    private void startZipExtractionProcess(){
        preventButtonInteraction();
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
        preventButtonInteraction();
        XMLFinder xmlSearch = new XMLFinder();
        xmlSearch.searchDirectoryListing(this.zipOutputUncompressedDirectory);
        this.xmlFileList = xmlSearch.getRecursiveXMLSearchList();
    }

    private void copyXMLToZipOutputXMLDirectory(){
        preventButtonInteraction();
        if(this.xmlFileList.isEmpty()){
            System.out.println("There are no .xml files in the directory tree!");
            Platform.runLater(()->{
                this.notificationArea.setText("There were no XML files located. Please close program!");
                stopProgressIndication();
            });
        } else {
            for (File xmlFile:this.xmlFileList){
                Path sourcePath = Paths.get(xmlFile.getAbsolutePath());
                File destinationFile = new File(this.zipOutputXMLDirectory + File.separator + xmlFile.getName());
                Path destinationPath = Paths.get(destinationFile.getAbsolutePath());
                System.out.println("Copying: " + xmlFile.getName() + " to xmlOutput directory");
                try{
                    Files.copy(sourcePath, destinationPath);
                } catch(IOException message){
                    message.getStackTrace();
                }
            }
            int number = this.xmlFileList.size();
            String numberOfXMLs = Integer.toString(number);
            Platform.runLater(()->{
                this.notificationArea.setText(numberOfXMLs + " XMLs extracted!");
                stopProgressIndication();
            });
            this.xmlFileList.clear();
        }
        enableClearButtonInteraction();
    }

    private void stopProgressIndication(){
        Platform.runLater(()->{
            this.progressIndicator.setProgress(1.0f);
        });
    }

    private void enableClearButtonInteraction(){
        Platform.runLater(()->{
            this.btnClear.setDisable(false);
        });
    }
}