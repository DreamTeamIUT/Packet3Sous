package iut.unice.dreamteam.UI.Dialogs;

import iut.unice.dreamteam.Equipments.Equipment;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EquipmentApplicationDialog extends Stage implements Initializable {


    private final Equipment equipment;

    public EquipmentApplicationDialog(Equipment equipment){
        this.equipment = equipment;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/equipementApplication.fxml"));
        fxmlLoader.setController(this);

        try {
            setScene(new Scene((Parent) fxmlLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
