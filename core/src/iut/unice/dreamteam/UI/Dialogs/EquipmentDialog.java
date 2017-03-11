package iut.unice.dreamteam.UI.Dialogs;

import iut.unice.dreamteam.Equipments.Equipment;
import iut.unice.dreamteam.Equipments.Router;
import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Interfaces.Route;
import iut.unice.dreamteam.UI.Adapaters.TableInterface;
import iut.unice.dreamteam.UI.Adapaters.TableRoute;
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
    private List<TableInterface> interfaceList;
    private List<TableRoute> routeList;

    private Equipment result;

    @FXML
    ImageView equipmentImage;
    @FXML
    TextField configEquipmentName;
    @FXML
    Button addInterface, delInterface;

    @FXML
    Button okButton, cancelButton;

    @FXML
    TableView<TableInterface> interfaceTable;
    @FXML
    TableColumn interfaceC, ipC, typeC, maskC, passiveC, gatewayC, defaultC;
    @FXML
    Button delRoute, addRoute;


    @FXML
    TableView<TableRoute> routesTables;
    @FXML
    TableColumn routeNextHop, routeNetwork, routeMask;
    @FXML
    Tab routesTab;


    private ArrayList<Interface> interfaceToAdd;
    private ArrayList<Interface> interfaceToRemove;

    private ArrayList<Route> routeToAdd;
    private ArrayList<Route> routeToRemove;

    private int numberEquipment;

    {
        this.interfaceList = new ArrayList<>();
        this.interfaceToAdd = new ArrayList<>();
        this.interfaceToRemove = new ArrayList<>();

        this.routeList = new ArrayList<>();
        this.routeToAdd = new ArrayList<>();
        this.routeToRemove = new ArrayList<>();
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

    public EquipmentDialog(String equipement, int numberEquipment) {
        this.numberEquipment = numberEquipment;
        setTitle("Add a new " + equipement);
        this.equipmentName = equipement;

        init();
        addInterface();
    }

    public EquipmentDialog(Equipment e) {

        if (e != null) {

            setTitle("Edit " + e.getName());
            this.result = e;

            for (Interface i : e.getInterfaces()) {
                TableInterface tableInterface = new TableInterface(i, e.getInterfaces().indexOf(i));
                if (e.getDefaultGateway().equals(i))
                    tableInterface.setDefault(true);

                interfaceList.add(tableInterface);
            }

            if (e instanceof Router){
                Debug.log("This is a router ! ");
                Router tmp = (Router)e;
                for (Route r : tmp.getRoutes()){
                    TableRoute tableRoute = new TableRoute(r);
                    routeList.add(tableRoute);
                }
            }

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


        configEquipmentName.setText("" + equipmentName + " " + String.valueOf(numberEquipment));
        if (result != null)
            configEquipmentName.setText(result.getName());

        delInterface.setDisable(true);
        if (result != null && result.getInterfaces().size() > 0)
            delInterface.setDisable(false);

        if (this.result != null)
            okButton.setText("Save");
        else {
            this.result = Equipment.fromString(this.equipmentName);
            this.result.clearInterfaces();
        }

        if (this.result instanceof Router)
            routesTab.setDisable(false);

        setupInterfaceTable();
        setupRouteTable();


    }

    private void setupRouteTable() {
        routeNetwork.setCellValueFactory(new PropertyValueFactory<TableInterface, String>("network"));
        routeMask.setCellValueFactory(new PropertyValueFactory<TableInterface, String>("mask"));
        routeNextHop.setCellValueFactory(new PropertyValueFactory<TableInterface, String>("nextHop"));


        routesTables.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                int ix = newValue.intValue();
                if ((ix < 0) || (ix >= routeList.size()))
                    return; // invalid data
                delRoute.setDisable(false);

            }
        });

        routesTables.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    Debug.log("Ok open route editor !");
                    RouteDialog dialog = new RouteDialog(routesTables.getSelectionModel().getSelectedItem());
                    dialog.showAndWait();

                    updateRouteTable();
                }
            }
        });

        updateRouteTable();
    }

    private void setupInterfaceTable() {


        interfaceC.setCellValueFactory(new PropertyValueFactory<TableInterface, String>("name"));
        ipC.setCellValueFactory(new PropertyValueFactory<TableInterface, String>("ip"));
        maskC.setCellValueFactory(new PropertyValueFactory<TableInterface, String>("mask"));
        typeC.setCellValueFactory(new PropertyValueFactory<TableInterface, String>("type"));
        passiveC.setCellValueFactory(new PropertyValueFactory<TableInterface, CheckBox>("passive"));
        defaultC.setCellValueFactory(new PropertyValueFactory<TableInterface, CheckBox>("default"));
        gatewayC.setCellValueFactory(new PropertyValueFactory<TableInterface, String>("gateway"));


        interfaceTable.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                int ix = newValue.intValue();

                if ((ix < 0) || (ix >= interfaceList.size())) {

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
                    dialog.showAndWait();

                    updateInterfaceTable();
                }
            }
        });


        updateInterfaceTable();
    }

    public void delInterface() {
        Debug.log("Del Clicked !");
        if (interfaceToAdd.contains(interfaceTable.getSelectionModel().getSelectedItem().getInterface()))
            interfaceToAdd.remove(interfaceTable.getSelectionModel().getSelectedItem().getInterface());
        else
            interfaceToRemove.add(interfaceTable.getSelectionModel().getSelectedItem().getInterface());

        interfaceList.remove(interfaceTable.getSelectionModel().getSelectedItem());
        updateInterfaceTable();
    }

    public void addInterface() {
        Debug.log("addClicked");

        NewInterfaceDialog dialog = new NewInterfaceDialog(interfaceList.size(), result);
        dialog.showAndWait();

        ArrayList<TableInterface> interfaceToInsert = dialog.getResult();
        if (interfaceToInsert != null) {
            for (TableInterface tableInterface : interfaceToInsert) {
                interfaceList.add(tableInterface);
                tableInterface.getInterface().setEquipment(this.result);
                tableInterface.setUpdateListenner(this);
                tableInterface.setDefault(true);
                interfaceToAdd.add(tableInterface.getInterface());
            }
        }


        updateInterfaceTable();
    }

    public void addRoute() {
        Debug.log("Add route clicked");

        RouteDialog dialog = new RouteDialog();
        dialog.showAndWait();

        TableRoute tableRoute = dialog.getResult();
        if (tableRoute != null) {
            routeList.add(tableRoute);
            routeToAdd.add(tableRoute.getRoute());
        }

        updateRouteTable();
    }

    public void delRoute() {
        Debug.log("Del route clicked");

        if (routeToAdd.contains(routesTables.getSelectionModel().getSelectedItem().getRoute()))
            routeToAdd.remove(routesTables.getSelectionModel().getSelectedItem().getRoute());
        else
            routeToRemove.add(routesTables.getSelectionModel().getSelectedItem().getRoute());

        routeList.remove(routesTables.getSelectionModel().getSelectedItem());
        updateRouteTable();
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

        for (Interface r : interfaceToRemove)
            this.result.removeInterface(r);

        for (Interface i : interfaceToAdd) {
            this.result.addInterface(i);
        }

        if (result instanceof Router) {
            Router tmp = (Router) result;
            for (Route r : routeToRemove)
                tmp.deleteRoute(r);

            for (Route i : routeToAdd)
                tmp.addRoute(i);
        }


        for (TableInterface i : interfaceList) {
            if (i.isDefault())
                this.result.setDefaultGateway(i.getInterface());
        }

        this.close();
    }

    public Equipment getResult() {
        return this.result;
    }

    private void updateInterfaceTable() {
        Debug.log("update !");
        for (int i = 0; i < interfaceList.size(); i++)
            interfaceList.get(i).setName("eth" + i);

        ObservableList<TableInterface> observableInterfacesList = FXCollections.observableList(interfaceList);
        interfaceTable.setItems(observableInterfacesList);
    }

    private void updateRouteTable() {
        Debug.log("update route !");

        if (routeList.size() > 0){

        }

        ObservableList<TableRoute> observableRouteList = FXCollections.observableList(routeList);
        routesTables.setItems(observableRouteList);
    }

    public void checkboxUpdate(TableInterface i) {
        Debug.log("Chekbox update");
        for (TableInterface tab : interfaceList) {
            if (!tab.equals(i)) {
                tab.setDefault(false);
            }
        }
    }
}