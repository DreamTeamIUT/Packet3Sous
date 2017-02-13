package iut.unice.dreamteam.UI.Dialogs;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Guillaume on 13/02/2017.
 */
public class NewEquipmentDialog extends Stage implements Initializable {

    public NewEquipmentDialog(String equipement){
        setTitle("Add a new " + equipement);


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/newEquimentDialog.fxml"));
        fxmlLoader.setController(this);

        try
        {
            setScene(new Scene((Parent) fxmlLoader.load()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
