package iut.unice.dreamteam.UI.ContextMenus;

import iut.unice.dreamteam.ApplicationStates;
import iut.unice.dreamteam.Equipments.Equipment;
import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Network;
import iut.unice.dreamteam.UI.CanvasDrawer;
import iut.unice.dreamteam.Utils.Debug;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.ImageCursor;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;

/**
 * Created by Guillaume on 24/02/2017.
 */
public class InterfaceContextMenu extends CustomContextMenu {
    private final Equipment equipement;
    private final CanvasDrawer canvas;

    public InterfaceContextMenu(Equipment e, CanvasDrawer canvasDrawer) {
        super("Interfaces");

        this.canvas = canvasDrawer;
        getScene().setCursor(new ImageCursor(new Image(this.getClass().getResource("/cursors/connect.png").toExternalForm())));

        this.equipement = e;

        addInterfaces();


    }

    private void addInterfaces() {

        if (equipement.getInterfaces().size() == 0){
            MenuItem empty = new MenuItem("no interfaces");
            empty.setDisable(true);
            getItems().add(empty);
            return;
        }


        for (int i = 0; i < equipement.getInterfaces().size(); i++) {

            if (equipement.getInterface(i).isConnected())
                continue;

            if (ApplicationStates.getInstance().getData() != null){
                if (equipement.getInterface(i).getMacAddress().equals(((Interface)ApplicationStates.getInstance().getData()).getMacAddress())) {
                    continue;
                }
            }


            MenuItem interfaceItem = new MenuItem("eth" + i);

            final int finalI = i;
            interfaceItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (ApplicationStates.getInstance().is(ApplicationStates.CONNECT)){
                        if (ApplicationStates.getInstance().getData() != null)
                        {
                            Network.linkInterfaces((Interface) ApplicationStates.getInstance().getData(), equipement.getInterface(finalI));
                            Debug.log("linked !");
                            ApplicationStates.getInstance().setState(ApplicationStates.NONE);
                            canvas.update();
                        }
                        else {
                            ApplicationStates.getInstance().setData(equipement.getInterface(finalI));
                        }
                    }
                }
            });

            getItems().add(interfaceItem);
        }
    }


}
