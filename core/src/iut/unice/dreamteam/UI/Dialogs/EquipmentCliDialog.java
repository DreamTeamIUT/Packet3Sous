package iut.unice.dreamteam.UI.Dialogs;

import iut.unice.dreamteam.Equipments.Equipment;
import iut.unice.dreamteam.Utils.Debug;
import iut.unice.dreamteam.Utils.TextUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Guillaume on 05/03/2017.
 */
public class EquipmentCliDialog extends Stage implements Initializable {
    private final Equipment equipment;

    @FXML
    TextArea result;
    @FXML
    TextField prompt;
    @FXML
    TextField command;

    int increment;
    ArrayList<String> history;

    public EquipmentCliDialog(Equipment e) {

        this.history = new ArrayList<>();
        this.equipment = e;
        setTitle("Command line interface for " + e.getName());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/cliDialog.fxml"));
        fxmlLoader.setController(this);

        try {
            setScene(new Scene((Parent) fxmlLoader.load()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        prompt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ob, String o,
                                String n) {
                prompt.setPrefWidth(TextUtils.computeTextWidth(prompt.getFont(),
                        prompt.getText(), 0.0D) + 20);
            }
        });
        prompt.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                command.requestFocus();
            }
        });
        prompt.setEditable(false);
        prompt.setText(equipment.getName() + ">");

        command.setText("");
        command.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    result.appendText("\n" + prompt.getText() + " " + command.getText());
                    history.add(command.getText());

                    //TODO add the code to execute command.

                    increment = 0;
                    command.setText("");
                } else if (event.getCode() == KeyCode.UP) {
                    Debug.log("up !");

                    if (history.size() == 0)
                        return;

                    if (increment < history.size())
                        increment++;

                    command.setText(history.get(history.size()-increment));

                } else if (event.getCode() == KeyCode.DOWN) {
                    Debug.log("Down !");
                    if (history.size() == 0)
                        return;

                    if (increment <= 1)
                    {
                        command.setText("");
                        return;
                    }

                    if (increment > 1)
                        increment--;
                    command.setText(history.get(history.size() - increment));
                }

            }
        });

        result.setText("");
        result.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                command.requestFocus();
            }
        });
        result.setEditable(false);
    }
}
