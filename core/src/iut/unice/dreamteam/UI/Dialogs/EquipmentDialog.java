package iut.unice.dreamteam.UI.Dialogs;

import iut.unice.dreamteam.Equipments.Equipment;
import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.UI.Adapaters.TableInterface;
import iut.unice.dreamteam.Utils.Debug;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class EquipmentDialog extends Stage implements Initializable {

    private String equipmentName;
    private ObservableList observableInterfacesList;
    private List<TableInterface> list;
    private Equipment result;
    @FXML
    private
    ImageView equipmentImage;
    @FXML
    private
    TextField configEquipmentName;
    @FXML
    Button addInterface;
    @FXML
    Button delInterface;
    @FXML
    Button okButton;
    @FXML
    Button cancelButton;
    @FXML
    TableView<TableInterface> interfaceTable;
    @FXML
    TableColumn interfaceC;
    @FXML
    TableColumn ipC;
    @FXML
    TableColumn typeC;
    @FXML
    TableColumn maskC;

    private ArrayList<Interface> toAdd;
    private ArrayList<Interface> toRemove;


    {
        this.list = new ArrayList<>();
        this.toAdd = new ArrayList<>();
        this.toRemove = new ArrayList<>();
    }

    private void init() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/equimentDialog.fxml"));
        fxmlLoader.setController(this);

        try {
            setScene(new Scene((Parent) fxmlLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public EquipmentDialog(String equipement) {
        setTitle("Add a new " + equipement);
        this.equipmentName = equipement;

        init();

    }

    public EquipmentDialog(Equipment e) {

        if (e != null) {

            setTitle("Edit " + e.getName());
            this.result = e;

            for (Interface i : e.getInterfaces())
                list.add(new TableInterface(i, e.getInterfaces().indexOf(i)));

        }

        init();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {



        try {
            equipmentImage.setImage(new Image(getClass().getResource("/devices/" + this.equipmentName + ".png").toString()));
        } catch (Exception e) {
            Debug.log("IMAGE CANNOT BE ADDED ... ");
        }


        if (this.result != null)
            okButton.setText("Save");
        else {
            this.result = Equipment.fromString(this.equipmentName);
            this.result.clearInterfaces();
        }



        configEquipmentName.setPromptText("" + equipmentName + " 01");
        if (result != null)
            configEquipmentName.setText(result.getName());

        delInterface.setDisable(true);
        if (result != null && result.getInterfaces().size() > 0)
            delInterface.setDisable(false);

        setupInterfaceTable();

    }

    private void setupInterfaceTable() {
        interfaceC.setCellValueFactory(new PropertyValueFactory<TableInterface, String>("name"));
        ipC.setCellValueFactory(new PropertyValueFactory<TableInterface, String>("ip"));
        maskC.setCellValueFactory(new PropertyValueFactory<TableInterface, String>("mask"));
        typeC.setCellValueFactory(new PropertyValueFactory<TableInterface, String>("type"));


        interfaceTable.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                int ix = newValue.intValue();

                if ((ix < 0) || (ix >= list.size())) {

                    return; // invalid data
                }

                delInterface.setDisable(false);

            }
        });

        interfaceTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    Debug.log("Ok open interface editor !");
                    EditInterfaceDialog dialog = new EditInterfaceDialog(interfaceTable.getSelectionModel().getSelectedItem());
                    dialog.show();
                }
            }
        });


        updateTable();
    }

    public void delInterface() {
        Debug.log("Del Clicked !");
        if (toAdd.contains(interfaceTable.getSelectionModel().getSelectedItem().getInterface()))
            toAdd.remove(interfaceTable.getSelectionModel().getSelectedItem().getInterface());
        else
            toRemove.add(interfaceTable.getSelectionModel().getSelectedItem().getInterface());

        list.remove(interfaceTable.getSelectionModel().getSelectedItem());
        updateTable();
    }

    public void addInterface() {
        Debug.log("addClicked");

        NewInterfaceDialog dialog = new NewInterfaceDialog(list.size());
        dialog.showAndWait();

        ArrayList<TableInterface> interfaceToInsert = dialog.getResult();
        if (interfaceToInsert != null){
            for (TableInterface tableInterface : interfaceToInsert)
            {
                list.add(tableInterface);
                tableInterface.getInterface().setEquipment(this.result);
                toAdd.add(tableInterface.getInterface());
            }
        }


        updateTable();
    }

    public void cancelDialog() {
        this.result = null;
        this.close();
    }

    public void validateDialog() {
        if (!Pattern.compile("\\w{3,}").matcher(configEquipmentName.getText().replace(" ", "")).matches()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error !");
            alert.setHeaderText(null);
            alert.setContentText("The equipment name must be longer than 3 characters...");

            alert.showAndWait();
            return;
        }

        this.result.setName(configEquipmentName.getText());

        for (Interface r : toRemove)
            this.result.removeInterface(r);

        for (Interface i : toAdd)
            this.result.addInterface(i);

        this.close();
    }

    public Equipment getResult() {
        return this.result;
    }

    private void updateTable() {
        for (int i = 0; i < list.size(); i++)
            list.get(i).setName("eth" + i);

        observableInterfacesList = FXCollections.observableList(list);
        interfaceTable.setItems(observableInterfacesList);
    }
}