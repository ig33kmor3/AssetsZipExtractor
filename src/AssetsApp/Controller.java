package AssetsApp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import java.io.File;

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

    @FXML
    public void initialize() {
        textFieldZipFolderLocation.setEditable(false);
        textFieldZipOutputLocation.setEditable(false);
        notificationArea.setEditable(false);
    }

    @FXML
    public void onButtonClick(ActionEvent btnClicked) {
        if (btnClicked.getSource().equals(btnZipFolderLocation)) {
            File zipFile = getZipFileDialog();
        } else if (btnClicked.getSource().equals(btnZipOutputLocation)) {
            File zipOutputDirectory = getZipOutputDirectoryDialog();
        } else if (btnClicked.getSource().equals(btnStartExtraction)) {
            notificationArea.setText("Starting Extraction ....");
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
            notificationArea.setText("Zip File Selected!");
            return zipFile;
        } else {
            notificationArea.setText("Zip File Not Selected!");
            return null;
        }
    }

    private File getZipOutputDirectoryDialog(){
        DirectoryChooser chooseZipOutputDirectory = new DirectoryChooser();
        File zipOutputDirectory = chooseZipOutputDirectory.showDialog(mainGridPane.getScene().getWindow());
        if(zipOutputDirectory != null){
            textFieldZipOutputLocation.setText(zipOutputDirectory.getAbsolutePath());
            notificationArea.setText("Zip Folder Output Selected!");
            return zipOutputDirectory;
        } else {
            notificationArea.setText("Zip Folder Output Not Selected!");
            return null;
        }
    }
}