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

/**
 * Created by Guillaume on 13/02/2017.
 */
public class NewInterfaceDialog extends Stage implements Initializable {

    private int currentInterfaceNumber;
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
    TextField numberRepeat;
    @FXML
    CheckBox repeat;
    @FXML
    CheckBox passiveInt;
    @FXML
    GridPane basicInfo;


    private ArrayList<TableInterface> tableInterfaces;

    public NewInterfaceDialog(int currentInterfaceNumber) {
        setTitle("Add a new interface");
        this.currentInterfaceNumber = currentInterfaceNumber;
        this.tableInterfaces = new ArrayList<>();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/newInterfaceDialog.fxml"));
        fxmlLoader.setController(this);

        try {
            setScene(new Scene((Parent) fxmlLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        name.setText("eth" + this.currentInterfaceNumber);

        types.setItems(FXCollections.observableList(new ArrayList<String>() {{
            add(Interface.INTERFACE_TYPE_WIRED);
            add(Interface.INTERFACE_TYPE_WIRELESS);
        }}));

        types.getSelectionModel().select(0);

        setupCheckbox();
        setupNumberTextField();

    }

    private void setupNumberTextField() {
        numberRepeat.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    numberRepeat.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        numberRepeat.setDisable(true);
    }

    private void setupCheckbox() {
        repeat.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                repeat.setSelected(newValue);
                if (newValue) {
                    ip.setText("");
                    ip.setDisable(true);
                    mask.setDisable(true);
                    mask.setText("");
                    numberRepeat.setDisable(false);

                } else {
                    ip.setText("");
                    ip.setDisable(false);
                    mask.setDisable(false);
                    mask.setText("");
                    numberRepeat.setDisable(true);
                    numberRepeat.setText("1");
                }
            }
        });

        passiveInt.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                passiveInt.setSelected(newValue);
                if (newValue) {
                    ip.setText("");
                    ip.setDisable(true);
                    mask.setDisable(true);
                    mask.setText("");
                    numberRepeat.setText("1");

                } else {
                    numberRepeat.setDisable(true);
                    ip.setText("");
                    ip.setDisable(false);
                    mask.setDisable(false);
                    mask.setText("");
                    numberRepeat.setText("1");
                }
            }
        });

        passiveInt.setSelected(false);
        repeat.setSelected(false);
    }

    public void validateDialog() {
        if ((ip.getText().equals("") || Network.isValidIpFormat(ip.getText())) && (mask.getText().equals("") || Network.isValidIpFormat(mask.getText()))) {
            for (int i = 0; i < Integer.parseInt(numberRepeat.getText()); i++) {
                tableInterfaces.add(new TableInterface(ip.getText(), mask.getText(), types.getValue(), "eth" + (this.currentInterfaceNumber + i), passiveInt.isSelected()));
            }
            this.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error !");
            alert.setHeaderText(null);
            alert.setContentText("Ip addresses are not in a valid format !");

            alert.showAndWait();
        }

    }

    public ArrayList<TableInterface> getResult() {
        return this.tableInterfaces;
    }

    public void cancelDialog() {
        this.close();
    }
}
