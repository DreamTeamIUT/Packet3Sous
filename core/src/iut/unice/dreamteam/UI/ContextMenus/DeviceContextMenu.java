package iut.unice.dreamteam.UI.ContextMenus;

import iut.unice.dreamteam.Equipments.Equipment;
import iut.unice.dreamteam.Utils.Debug;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

/**
 * Created by Guillaume on 24/02/2017.
 */
public class DeviceContextMenu extends CustomContextMenu {
    private final Equipment equipement;

    public DeviceContextMenu(Equipment equipment) {
        super(equipment.getName());

        this.equipement = equipment;
        addMenuItems();
    }

    private void addMenuItems() {

        MenuItem edit = new MenuItem("Edit");
        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Debug.log("Edit menu");
            }
        });

        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Debug.log("Delete Element");
            }
        });

        MenuItem duplicate = new MenuItem("Duplicate");
        duplicate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Debug.log("Deplicate !");
            }
        });

        MenuItem properties = new MenuItem("Properties");
        properties.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Debug.log("Properties");
            }
        });

        getItems().addAll(edit, duplicate, delete, properties);
    }


}
