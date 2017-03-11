package iut.unice.dreamteam.UI.Dialogs;

import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Network;
import iut.unice.dreamteam.UI.Adapaters.TableInterface;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EditInterfaceDialog extends Stage implements Initializable {

    private TableInterface item;
    @FXML
    TextField ip;
    @FXML
    TextField mask;
    @FXML
    TextField name;
    @FXML
    ChoiceBox<String> types;
    @FXML
    Button okButton;
    @FXML
    Button cancelButton;
    @FXML
    CheckBox passiveInt;
    @FXML
    GridPane basicInfo;
    @FXML
    TextField gateway;


    public EditInterfaceDialog(TableInterface tableInterface) {
        setTitle("Edit an interface");
        this.item = tableInterface;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/interfaceDialog.fxml"));
        fxmlLoader.setController(this);

        try {
            setScene(new Scene((Parent) fxmlLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setupCheckbox();

        name.setText(item.getName());

        ip.setText(item.getIp());
        mask.setText(item.getMask());
        gateway.setText(item.getGateway());

        passiveInt.setSelected(item.isPassive());

        types.setItems(FXCollections.observableList(new ArrayList<String>() {{
            add(Interface.INTERFACE_TYPE_WIRED);
            add(Interface.INTERFACE_TYPE_WIRELESS);
        }}));

        types.getSelectionModel().select(item.getType().equals(Interface.INTERFACE_TYPE_WIRED) ? 0 : 1);


    }

    private void setupCheckbox() {

        passiveInt.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                passiveInt.setSelected(newValue);
                if (newValue) {
                    ip.setText("");
                    ip.setDisable(true);
                    mask.setDisable(true);
                    mask.setText("");
                    gateway.setDisable(true);
                    gateway.setText("");

                } else {
                    ip.setText("");
                    ip.setDisable(false);
                    mask.setDisable(false);
                    gateway.setDisable(false);
                    gateway.setText("");
                    mask.setText("");
                }
            }
        });

        passiveInt.setSelected(false);
    }

    public void validateDialog() {
        if ((ip.getText().equals("") || Network.isValidIpFormat(ip.getText()))
                && (mask.getText().equals("") || Network.isValidMask(mask.getText()))
                && (gateway.getText().equals("") || Network.isValidIpFormat(gateway.getText()))) {

            this.item.setIp(ip.getText());
            this.item.setMask(mask.getText());
            this.item.setPassive(passiveInt.isSelected());
            this.item.setType(types.getSelectionModel().getSelectedItem());
            this.item.setGateway(gateway.getText());

            this.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error !");
            alert.setHeaderText(null);
            alert.setContentText("Ip addresses are not in a valid format !");

            alert.showAndWait();
        }

    }

    public void cancelDialog() {
        this.close();
    }
}
