package AssetsApp;

import AssetsApp.Extraction.Extractor;
import AssetsApp.Search.XMLFinder;
import AssetsApp.Search.ZipFinder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
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
    private TextArea notificationArea;

    private File zipFile;
    private File zipOutputRootDirectory;
    private File zipOutputUncompressedDirectory;
    private File zipOutputXMLDirectory;
    private List<File> zipFileList;
    private List<File> xmlFileList;

    @FXML
    public void initialize() {
        textFieldZipFolderLocation.setEditable(false);
        textFieldZipOutputLocation.setEditable(false);
        notificationArea.setEditable(false);
    }

    @FXML
    public void onBrowseButtonClick(ActionEvent btnClicked) {
        if (btnClicked.getSource().equals(btnZipFolderLocation)) {
            this.zipFile = getZipFileDialog();
        } else if (btnClicked.getSource().equals(btnZipOutputLocation)) {
            this.zipOutputRootDirectory = getZipOutputDirectoryDialog();
        } else if (btnClicked.getSource().equals(btnClear)) {
            textFieldZipFolderLocation.clear();
            textFieldZipOutputLocation.clear();
            notificationArea.clear();
        }
    }

    private File getZipFileDialog(){
        FileChooser chooseZipFile = new FileChooser();
        chooseZipFile.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Zip", "*.zip"));
        File zipFile = chooseZipFile.showOpenDialog(mainGridPane.getScene().getWindow());
        if(zipFile != null){
            textFieldZipFolderLocation.setText(zipFile.getAbsolutePath());
            notificationArea.setText("Zip file selected.");
            return zipFile;
        } else {
            notificationArea.setText("Zip file not selected!");
            return null;
        }
    }

    private File getZipOutputDirectoryDialog(){
        DirectoryChooser chooseZipOutputDirectory = new DirectoryChooser();
        File zipOutputDirectory = chooseZipOutputDirectory.showDialog(mainGridPane.getScene().getWindow());
        if(zipOutputDirectory != null){
            textFieldZipOutputLocation.setText(zipOutputDirectory.getAbsolutePath());
            notificationArea.setText("Zip output folder selected.");
            return zipOutputDirectory;
        } else {
            notificationArea.setText("Zip output folder not selected!");
            return null;
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
            notificationArea.setText("Please select an input and output!");
        }
    }

    private void createOutputFolderTree(){
        this.zipOutputUncompressedDirectory = new File(this.zipOutputRootDirectory.getAbsolutePath() + File.separator + "00_uncompressedZipContents");
        this.zipOutputXMLDirectory = new File(this.zipOutputRootDirectory.getAbsolutePath() + File.separator + "01_xmlOutputContents");
        this.zipOutputUncompressedDirectory.mkdirs();
        this.zipOutputXMLDirectory.mkdirs();
        notificationArea.setText("Finished initial folder tree creation. Press Start Extraction button.");
    }

    private void startZipExtractionProcess(){
        ZipFinder zipSearch = new ZipFinder();
        File zipFileParentDirectory;
        do{
            Extractor.unZip(this.zipFile, this.zipOutputUncompressedDirectory, this.notificationArea);
            zipSearch.searchDirectoryListing(this.zipOutputUncompressedDirectory);
            this.zipFileList = zipSearch.getRecursiveZipSearchList();
            while(!this.zipFileList.isEmpty()){
                for (File zipFile:this.zipFileList){
                    zipFileParentDirectory = new File(zipFile.getParent());
                    Extractor.unZip(zipFile, zipFileParentDirectory, this.notificationArea);
                    zipFile.delete();
                }
                zipSearch.resetZipList();
                zipSearch.searchDirectoryListing(this.zipOutputUncompressedDirectory);
            }
        } while(!this.zipFileList.isEmpty());
    }

    private void searchDirectoriesForXMLFiles(){
        XMLFinder xmlSearch = new XMLFinder();
        xmlSearch.searchDirectoryListing(this.zipOutputUncompressedDirectory);
        this.xmlFileList = xmlSearch.getRecursiveXMLSearchList();
    }

    private void copyXMLToZipOutputXMLDirectory(){
        if(this.xmlFileList.isEmpty()){
            System.out.println("There are no .xml files in the directory tree");
        } else {
            for (File xmlFile:this.xmlFileList){
                Path sourcePath = Paths.get(xmlFile.getAbsolutePath());
                File destinationFile = new File(this.zipOutputXMLDirectory + File.separator + xmlFile.getName());
                Path destinationPath = Paths.get(destinationFile.getAbsolutePath());
                System.out.println("Copying " + xmlFile.getName() + " to xmlOutput directory.");
                this.notificationArea.appendText("Copying " + xmlFile.getName() + " to xmlOutput directory.\n");
                try{
                    Files.copy(sourcePath, destinationPath);
                } catch(IOException message){
                    message.getStackTrace();
                }
            }
            this.xmlFileList.clear();
        }
    }
}