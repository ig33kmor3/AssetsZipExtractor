package AssetsApp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private TextField textFieldZipFolderLocation;

    @FXML
    private Button btnZipFolderLocation;

    @FXML
    private TextField textFieldXMLOutputLocation;

    @FXML
    private Button btnXMLOutputLocation;

    @FXML
    private TextArea notificationArea;

    @FXML
    public void initialize(){
        textFieldZipFolderLocation.setEditable(false);
        textFieldXMLOutputLocation.setEditable(false);
        notificationArea.setEditable(false);
    }
}
