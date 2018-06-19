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
    private TextField textFieldXMLOutputLocation;
    @FXML
    private Button btnXMLOutputLocation;
    @FXML
    private Button btnStartExtraction;
    @FXML
    private Button btnClear;
    @FXML
    private TextArea notificationArea;

    @FXML
    public void initialize() {
        textFieldZipFolderLocation.setEditable(false);
        textFieldXMLOutputLocation.setEditable(false);
        notificationArea.setEditable(false);
    }

    @FXML
    public void onButtonClick(ActionEvent btnClicked) {
        if (btnClicked.getSource().equals(btnZipFolderLocation)) {
            File zipFile = getZipFileDialog();
        } else if (btnClicked.getSource().equals(btnXMLOutputLocation)) {
            File xmlDirectory = getXMLDirectoryDialog();
        } else if (btnClicked.getSource().equals(btnStartExtraction)) {
            notificationArea.setText("Starting Extraction ....");
        } else if (btnClicked.getSource().equals(btnClear)) {
            textFieldZipFolderLocation.clear();
            textFieldXMLOutputLocation.clear();
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

    private File getXMLDirectoryDialog(){
        DirectoryChooser chooseXMLDirectory = new DirectoryChooser();
        File xmlDirectory = chooseXMLDirectory.showDialog(mainGridPane.getScene().getWindow());
        if(xmlDirectory != null){
            textFieldXMLOutputLocation.setText(xmlDirectory.getAbsolutePath());
            notificationArea.setText("XML Folder Selected!");
            return xmlDirectory;
        } else {
            notificationArea.setText("XML Folder Not Selected!");
            return null;
        }
    }
}