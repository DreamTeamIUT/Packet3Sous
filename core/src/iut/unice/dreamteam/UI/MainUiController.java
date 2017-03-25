package iut.unice.dreamteam.UI;

import iut.unice.dreamteam.ApplicationStates;
import iut.unice.dreamteam.Equipments.Computer;
import iut.unice.dreamteam.Equipments.Equipment;
import iut.unice.dreamteam.Equipments.Router;
import iut.unice.dreamteam.Equipments.Switch;
import iut.unice.dreamteam.Network;
import iut.unice.dreamteam.UI.Dialogs.DirectorySelector;
import iut.unice.dreamteam.UI.Dialogs.EquipmentDialog;
import iut.unice.dreamteam.UI.Listeners.OnActionListener;
import iut.unice.dreamteam.UI.Listeners.OnUpdateListener;
import iut.unice.dreamteam.Utils.Debug;
import javafx.application.Platform;
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
import sun.security.x509.NetscapeCertTypeExtension;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private boolean isReachable;
    private long responseTime;
    private Logger log;
    private int y;
    private float height ;
    private float width ;
    private int k;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Test");
        network = new Network();
        drawableEquipments = new ArrayList<>();
        canvasDrawer = new CanvasDrawer(canvasPane, drawableEquipments, network);
        y = 0;
        k=0;
        height = (float) canvasPane.getHeight() / 20;
        width = (float) canvasPane.getWidth() / 20;


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

    /*private boolean ping(String host){
        int timeOut = 3000; // I recommend 3 seconds at least int timeout = 3000 / / Je recommande 3 secondes au moins
        boolean status=false;
        try {
            status = InetAddress.getByName(host).isReachable(timeOut);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return status;
    }
    */

    private static boolean isWindows() {
        String OS = System.getProperty("os.name").toLowerCase();
        return (OS.indexOf("win") >= 0);
    }

    private String getCountParam() {
        String result = "-c";
        if (isWindows())
            result = "-n";
        return result;
    }

    public boolean ping(String host) {
        isReachable = false;
        try {
            long start = System.currentTimeMillis();

            Process proc = new ProcessBuilder("ping", host, getCountParam(), "1").start();

            int exitValue = proc.waitFor();
            responseTime = System.currentTimeMillis() - start;
            isReachable = (exitValue == 0);
            if (isReachable)
                Debug.log("Ping ok vers " + host + ", temps de réponse : " + responseTime + " ms");
            else
                Debug.log("Impossible de joindre l'hôte destination : " + host);
        } catch (IOException e1) {
            Debug.log( "Erreur I/O" + e1);
        } catch (InterruptedException e) {
            Debug.log("Interruption de la commande" + e);
        }
        return isReachable;
    }
    public void analysedPing() {
        final int compteur = 0;
        String thisIp = InetAddress.getLoopbackAddress().getHostAddress();
            if (thisIp != null){
                Debug.log(thisIp);
                //String ipAdr =  thisIp.split("/")[1];

                List<String> choices = new ArrayList<>();
                Enumeration<NetworkInterface> interfaces = null;
                try {
                    interfaces = NetworkInterface.getNetworkInterfaces();
                    while (interfaces.hasMoreElements()) {
                        NetworkInterface interfaceN = interfaces.nextElement();
                        System.out.println("----> " + interfaceN.getDisplayName());
                        List<InterfaceAddress> iaList= interfaceN.getInterfaceAddresses();
                        for (InterfaceAddress interfaceAddress : iaList) {
                            if (!interfaceAddress.getAddress().getHostAddress().contains(":"))
                                choices.add(interfaceAddress.getAddress().getHostAddress());
                        }
                    }

                ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(1), choices);

                dialog.setTitle("Ping généralisé");
                dialog.setHeaderText("Nous allons ping l'ensemble des machines de votre réseau");
                dialog.setContentText("choisissez l'adresse ip avec laquelle vous souhaitez ping son réseau :  ");

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()){
                    String ipAdr = result.toString().substring(9, result.toString().length() - 1);
                    Debug.log("adresse : " + ipAdr);
                    final String mask = Network.getNaturalMask(ipAdr);
                    final String broadcast = Network.getBroadcastAddress(ipAdr, mask);
                    final String network = Network.getNeworkAddress(ipAdr, mask);



                    final Switch commutateur = new Switch("switch1");
                    addEquipmentToUI(commutateur, (float) canvasPane.getWidth() / 2, (float) canvasPane.getHeight() / 2);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Long i;
                            String ip;


                            for (i = Long.parseLong(Network.ipToBinary(network), 2); i < Long.parseLong(Network.ipToBinary(broadcast), 2); i++){
                                ip = Long.toString(i,2);
                                if (ip.length() != 32){
                                    while (ip.length()!= 32){
                                        ip = "0" + ip;
                                    }
                                }
                                else {
                                    if (ping(Network.binaryToIp(ip))) {
                                        y++;
                                        height = height + 20;
                                        width = width + 20;

                                        final String ipAddress = Network.binaryToIp(ip);
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                Computer computer = new Computer("computer" + y);
                                                computer.getInterface(0).setIp(ipAddress);
                                                computer.getInterface(0).setMask(mask);
                                                Network.linkInterfaces(computer.getInterface(0), commutateur.getInterface(0 + k));
                                                addEquipmentToUI(computer, height, width);
                                                k++;
                                            }
                                        });

                                    }
                                }

                            }
                        }
                    }).start();
                    // 255.0.0.0 -> 192.0.0.0 -> 192.255.255.255
                    // 192.168.0.0
                    // 1111011111110111111010 => binaryToip => 192.168.0.25
                    //
                } else {
                    dialog.close();
                }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }
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
