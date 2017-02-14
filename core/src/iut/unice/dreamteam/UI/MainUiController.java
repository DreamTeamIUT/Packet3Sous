package iut.unice.dreamteam.UI;

import iut.unice.dreamteam.Equipments.Equipment;
import iut.unice.dreamteam.Network;
import iut.unice.dreamteam.UI.Dialogs.NewEquipmentDialog;
import iut.unice.dreamteam.Utils.Debug;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class MainUiController implements Initializable {

    @FXML
    private Canvas mainCanvas;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private javafx.scene.control.ListView<String> deviceList;
    @FXML
    private Button startBtn;
    @FXML
    private Button stopBtn;

    private CanvasDrawer canvasDrawer;
    private Network network;
    private ObservableList<String> deviceObservableList;
    private ArrayList<DrawableEquipment> drawableEquipments;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Test");
        network = new Network();
        drawableEquipments =  new ArrayList<>();
        canvasDrawer = new CanvasDrawer(mainPane, drawableEquipments, network, mainCanvas);

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
                Debug.log("droped on x:" + event.getSceneX() + " y:" + event.getSceneY());

                if (db.hasString()) {
                    Debug.log(db.getString());
                    event.setDropCompleted(true);

                    NewEquipmentDialog dialog = new NewEquipmentDialog(db.getString());
                    dialog.showAndWait();

                    Equipment result = dialog.getResult();
                    if (result != null){
                        Debug.log("Added new Equipment drawable");
                        network.addEquipment(result);
                        drawableEquipments.add(new DrawableEquipment(result)
                                .setX((float) event.getSceneX())
                                .setY((float) event.getSceneY())
                        );
                        canvasDrawer.update();
                    }


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

    public void startRender(){
        canvasDrawer.startRender();
    }

    public void stopRender(){
        canvasDrawer.stopRender();
    }
}
