package iut.unice.dreamteam.UI.ContextMenus;

import iut.unice.dreamteam.Equipments.Equipment;
import iut.unice.dreamteam.UI.Dialogs.EquipmentCliDialog;
import iut.unice.dreamteam.UI.Dialogs.EquipmentDialog;
import iut.unice.dreamteam.UI.Listeners.OnUpdateListener;
import iut.unice.dreamteam.Utils.Debug;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import org.json.JSONObject;

import java.util.Optional;

public class DeviceContextMenu extends CustomContextMenu {
    private final Equipment equipement;
    private MenuItem editItem;
    private MenuItem deleteItem;
    private MenuItem duplicateItem;
    private MenuItem CliItem;
    private MenuItem pingItem;
    private OnUpdateListener updateListenner;

    public DeviceContextMenu(Equipment equipment) {
        super(equipment.getName());
        this.equipement = equipment;
        addMenuItems();
    }

    public void setEditAction(EventHandler<ActionEvent> event){
        editItem.setOnAction(event);
    }

    public void setDeleteAction(EventHandler<ActionEvent> event){
        deleteItem.setOnAction(event);
    }

    public void setDuplicateAction(EventHandler<ActionEvent> event){
        duplicateItem.setOnAction(event);
    }


    private void addMenuItems() {

        editItem = new MenuItem("Edit");
        editItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Debug.log("Edit " + equipement.getName());
                EquipmentDialog dialog  = new EquipmentDialog(equipement);

                dialog.showAndWait();

                updateListenner.onUpdate();
            }
        });

        deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Debug.log("Delete " + equipement.getName());
            }
        });

        duplicateItem = new MenuItem("Duplicate");
        duplicateItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Debug.log("Deplicate !");
            }
        });

        CliItem = new MenuItem("Cli");
        CliItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                EquipmentCliDialog cli = new EquipmentCliDialog(equipement);
                cli.show();
            }
        });

        pingItem = new MenuItem("Ping");
        pingItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextInputDialog dialog = new TextInputDialog("");
                dialog.setTitle("Ping");
                dialog.setHeaderText("");
                dialog.setContentText("destination");

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()){
                    Debug.log("resultat : " + (String) result.toString().substring(9, result.toString().length() - 1));

                    if (equipement.existService("icmp-client")) {
                        equipement.getService("icmp-client").initiateProtocol(equipement, equipement.getInterface(0), new JSONObject().put("ip-address", result.toString().substring(9, result.toString().length() - 1)));
                    }

                    if (updateListenner != null)
                        updateListenner.onUpdate();
                }

                dialog.close();
            }
        });
        getItems().addAll(editItem, duplicateItem, deleteItem, CliItem, pingItem);
    }

    public void setUpdateListenner(OnUpdateListener updateListenner) {
        this.updateListenner = updateListenner;
    }
}
