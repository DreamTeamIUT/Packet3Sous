package iut.unice.dreamteam.UI;

import iut.unice.dreamteam.Network;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;


public class MainUiController implements Initializable {

    @FXML
    private Canvas mainCanvas;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private javafx.scene.control.ListView<String> deviceList;

    private CanvasDrawer canvasDrawer;
    private Network network;
    private ObservableList<String> deviceObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Test");
        network = new Network();
        canvasDrawer = new CanvasDrawer(mainPane,network, mainCanvas);

        addItemToListView();

    }

    private void addItemToListView() {
        deviceObservableList = FXCollections.observableArrayList();

        //add some Students
        deviceObservableList.addAll(
                "Router",
                "Switch",
                "Hub",
                "Access Point",
                "Computer"
        );

        deviceList.setItems(deviceObservableList);
        deviceList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new DeviceCell();
            }
        });


    }

}
