package iut.unice.dreamteam.UI;

import iut.unice.dreamteam.Network;
import iut.unice.dreamteam.UI.Dialogs.NewEquipmentDialog;
import iut.unice.dreamteam.Utils.Debug;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.*;
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
        canvasDrawer = new CanvasDrawer(mainPane, network, mainCanvas);

        addItemToListView();

        enableDragAndDrop();

    }

    private void enableDragAndDrop() {
        mainCanvas.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
            }
        });

        mainCanvas.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    Debug.log(db.getString());
                    event.setDropCompleted(true);

                    NewEquipmentDialog dialog = new NewEquipmentDialog(db.getString());
                    dialog.showAndWait();


                }
                event.setDropCompleted(false);
            }
        });
    }

    private void addItemToListView() {
        deviceObservableList = FXCollections.observableArrayList();

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
                final DeviceCell cell = new DeviceCell();
                cell.setOnDragDetected(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                        ClipboardContent cc = new ClipboardContent();
                        cc.putString(cell.getItem());
                        db.setContent(cc);
                    }
                });
                return cell;
            }
        });


    }

}
