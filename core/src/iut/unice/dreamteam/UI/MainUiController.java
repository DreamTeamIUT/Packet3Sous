package iut.unice.dreamteam.UI;

import iut.unice.dreamteam.ApplicationStates;
import iut.unice.dreamteam.Equipments.Equipment;
import iut.unice.dreamteam.Network;
import iut.unice.dreamteam.UI.Dialogs.DirectorySelector;
import iut.unice.dreamteam.UI.Dialogs.EquipmentDialog;
import iut.unice.dreamteam.UI.Listeners.OnActionListener;
import iut.unice.dreamteam.UI.Listeners.OnUpdateListener;
import iut.unice.dreamteam.Utils.Debug;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainUiController implements Initializable {

    /* @FXML
     private Canvas mainCanvas;*/
    @FXML
    private ToggleButton connectEquipment, disconnectEquipment;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private AnchorPane canvasPane;
    @FXML
    private javafx.scene.control.ListView<String> deviceList;
    @FXML
    private Button startBtn;
    @FXML
    private Slider renderSlider;

    private CanvasDrawer canvasDrawer;
    private Network network;
    private ObservableList<String> deviceObservableList;
    private ArrayList<DrawableEquipment> drawableEquipments;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Test");
        network = new Network();
        drawableEquipments = new ArrayList<>();
        canvasDrawer = new CanvasDrawer(canvasPane, drawableEquipments, network);

        ApplicationStates.getInstance().addStateChangedListener(new ApplicationStates.StateChangeListener() {
            @Override
            public void stateChanged(int newState) {
                Debug.log("Received" + newState);
                switch (newState) {
                    case ApplicationStates.CONNECT:
                        connectEquipment.setSelected(true);
                        connectEquipment.setText("Cancel");
                        canvasPane.setCursor(new ImageCursor(new Image(getClass().getResource("/cursors/connect.png").toExternalForm())));
                        break;
                    case ApplicationStates.NONE:
                        connectEquipment.setSelected(false);
                        connectEquipment.setText("Connect");
                        disconnectEquipment.setSelected(false);
                        disconnectEquipment.setText("Disconnect");
                        canvasPane.setCursor(Cursor.DEFAULT);
                        break;
                    case ApplicationStates.DELETE:
                        disconnectEquipment.setSelected(true);
                        disconnectEquipment.setText("Cancel");
                        Image imgDelete = new Image(getClass().getResource("/cursors/delete.png").toExternalForm());
                        canvasPane.setCursor(new ImageCursor(imgDelete, imgDelete.getHeight()/2, imgDelete.getWidth()/2));
                        break;
                }
            }
        });

        addItemToListView();
        enableDragAndDrop();

     /*   renderSlider.setMin(5000);
        renderSlider.setMax(500);
        renderSlider.setBlockIncrement(500);

        renderSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Debug.log("update slider");
            }
        });*/
    }

    private void enableDragAndDrop() {
        canvasPane.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
            }
        });

        canvasPane.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                Debug.log("dropped on x:" + event.getSceneX() + " y:" + event.getSceneY());

                if (db.hasString()) {
                    Debug.log(db.getString());
                    event.setDropCompleted(true);

                    EquipmentDialog dialog = new EquipmentDialog(db.getString(), network.getEquipmentByType(Equipment.typeFromString(db.getString())).size());
                    dialog.showAndWait();

                    Equipment result = dialog.getResult();

                    if (result != null) {
                        addEquipmentToUI(result, (float) event.getX(), (float) event.getY());
                    }
                }
                event.setDropCompleted(false);
            }
        });
    }

    private void addEquipmentToUI(Equipment result, float x, float y) {
        Debug.log("Added new Equipment drawable");
        network.addEquipment(result);
        drawableEquipments.add(new DrawableEquipment(result)
                .setX(x)
                .setY(y)
                .setOnUpdateListener(new OnUpdateListener() {
                    @Override
                    public void onUpdate() {
                        canvasDrawer.update();
                    }
                })
                .setActionsListener(new OnActionListener() {
                    @Override
                    public void onDelete(DrawableEquipment drawableEquipment) {
                        drawableEquipments.remove(drawableEquipment);
                        network.removeEquipment(drawableEquipment.getEquipment());
                        canvasPane.getChildren().remove(drawableEquipment);
                        canvasDrawer.update();
                    }

                    @Override
                    public void onDuplicate(DrawableEquipment drawableEquipment) {
                        Equipment e = Equipment.clone(drawableEquipment.getEquipment());
                        if (e != null)
                            addEquipmentToUI(e, (float) drawableEquipment.getLayoutX() + 2 * (float) drawableEquipment.getEquipmentDrawable().getWidth(),
                                    (float) drawableEquipment.getLayoutY() + 2 * (float) drawableEquipment.getEquipmentDrawable().getHeight());
                    }
                })
        );
        canvasDrawer.update();
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

    public void startRender() {
        if (startBtn.getText().equals("Start")) {
            canvasDrawer.startRender();
            startBtn.setText("Stop");
        }
        else {
            canvasDrawer.stopRender();
            startBtn.setText("Start");
        }
    }

    public void makeLink() {
        if (connectEquipment.isSelected())
            ApplicationStates.getInstance().setState(ApplicationStates.CONNECT);
        else
            ApplicationStates.getInstance().setState(ApplicationStates.NONE);

    }

    public void breakLink(){
        if (disconnectEquipment.isSelected())
            ApplicationStates.getInstance().setState(ApplicationStates.DELETE);
        else
            ApplicationStates.getInstance().setState(ApplicationStates.NONE);
    }

    public void saveProject() {
        new DirectorySelector(new DirectorySelector.DirectorySelection() {
            @Override
            public void onSelect(String filePath) {
                Debug.log("file path : " + filePath);
            }
        });
    }
}
