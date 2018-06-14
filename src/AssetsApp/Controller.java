package AssetsApp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

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
            notificationArea.setText("Open Zip Folder Location");
        } else if (btnClicked.getSource().equals(btnXMLOutputLocation)) {
            notificationArea.setText("Set XML Output Location");
        } else if (btnClicked.getSource().equals(btnStartExtraction)) {
            notificationArea.setText("Starting Extraction ....");
        } else if (btnClicked.getSource().equals(btnClear)) {
            textFieldZipFolderLocation.clear();
            textFieldXMLOutputLocation.clear();
            notificationArea.clear();
        }
    }
}