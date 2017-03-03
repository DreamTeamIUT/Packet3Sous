package iut.unice.dreamteam.UI;

import iut.unice.dreamteam.ApplicationStates;
import iut.unice.dreamteam.Equipments.Equipment;
import iut.unice.dreamteam.UI.ContextMenus.DeviceContextMenu;
import iut.unice.dreamteam.UI.ContextMenus.InterfaceContextMenu;
import iut.unice.dreamteam.Utils.Debug;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Created by Guillaume on 14/02/2017.
 */
public class DrawableEquipment extends ImageView{


    private Equipment equipment;
    private OnUpdateListener updateListenner;
    private double mouseX;
    private double mouseY;

    public DrawableEquipment(Equipment e) {
        super(DrawableLoader.getInstance().getEquipmentDrawable(e));
        this.equipment = e;


        setPreserveRatio(true);

        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY) && ApplicationStates.getInstance().is(ApplicationStates.CONNECT)) {
                    Debug.log("Create context...");
                    InterfaceContextMenu menu = new InterfaceContextMenu(equipment, new OnUpdateListener() {
                        @Override
                        public void onUpdate() {
                            if (updateListenner != null)
                                updateListenner.onUpdate();
                        }
                    });
                    menu.show(DrawableEquipment.this, event.getScreenX(), event.getScreenY());
                } else if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                    Debug.log("Double click !");
                }
            }
        });

        setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                DeviceContextMenu menu = new DeviceContextMenu(getEquipment());
                menu.show(DrawableEquipment.this, event.getScreenX(), event.getScreenY());
            }
        });

        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseX = event.getSceneX() ;
                mouseY = event.getSceneY() ;
            }
        });

        setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double deltaX = event.getSceneX() - mouseX ;
                double deltaY = event.getSceneY() - mouseY ;

                double nx = getX() + deltaX;
                double ny = getY() + deltaY;

                setTo((float)nx, (float)ny);

                //relocate(nx, ny);

                mouseX = event.getSceneX() ;
                mouseY = event.getSceneY() ;

                updateListenner.onUpdate();
            }
        });

    }


    public Equipment getEquipment() {
        return equipment;
    }


    public DrawableEquipment setX(float x) {
        super.setX(x);
        return this;
    }

    public DrawableEquipment setY(float y) {
        super.setY(y);
        return this;
    }

    public double getCenterPointX(){
        return (float) ((getEquipmentDrawable().getWidth()/2) + getX());
    }

    public double getCenterPointY(){
        return (float) ((getEquipmentDrawable().getHeight()/2) + getY());
    }

    public Image getEquipmentDrawable() {
        return this.getImage();
    }

    public DrawableEquipment setUpdateListener(OnUpdateListener listener){
        this.updateListenner = listener;
        return this;
    }

    public void setTo(float x, float y) {
        AnchorPane.setLeftAnchor(this, (double) x);
        AnchorPane.setTopAnchor(this, (double) y);

        setX(x);
        setY(y);
    }
}
