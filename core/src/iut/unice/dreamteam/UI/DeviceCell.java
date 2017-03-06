package iut.unice.dreamteam.UI;

import iut.unice.dreamteam.Utils.Debug;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DeviceCell extends ListCell<String> {
    @FXML
    private Label deviceName;

    @FXML
    private ImageView deviceImg;


    @FXML
    private AnchorPane cellPane;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(String name, boolean empty) {
        super.updateItem(name, empty);

        if (empty || name == null) {
            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/deviceCell.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


            deviceName.setText(name);
            Debug.log(name+".png");
            deviceImg.setImage(new Image(getClass().getResource("/devices/"+name+".png").toString()));

            setText(null);
            setGraphic(cellPane);
        }

    }
}
