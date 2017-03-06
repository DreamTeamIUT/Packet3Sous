package iut.unice.dreamteam.UI;

import iut.unice.dreamteam.ApplicationStates;
import iut.unice.dreamteam.Equipments.Equipment;
import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Interfaces.PacketOnEquipment;
import iut.unice.dreamteam.UI.ContextMenus.DeviceContextMenu;
import iut.unice.dreamteam.UI.ContextMenus.InterfaceContextMenu;
import iut.unice.dreamteam.UI.Listeners.OnActionListener;
import iut.unice.dreamteam.UI.Listeners.OnUpdateListener;
import iut.unice.dreamteam.Utils.Debug;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

/**
 * Created by Guillaume on 14/02/2017.
 */
public class DrawableEquipment extends ImageView {


    private Equipment equipment;
    private OnUpdateListener updateListenner;
    private double mouseX;
    private double mouseY;
    private OnActionListener actionsListener;

    private ArrayList<DrawablePacket> packets;

    public DrawableEquipment(Equipment e) {
        super(DrawableLoader.getInstance().getEquipmentDrawable(e));
        this.equipment = e;

        packets = new ArrayList<>();

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
                menu.setUpdateListenner(updateListenner);
                menu.setDeleteAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (actionsListener != null)
                            actionsListener.onDelete(DrawableEquipment.this);
                    }
                });

                menu.setDuplicateAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        if (actionsListener != null)
                            actionsListener.onDuplicate(DrawableEquipment.this);
                    }
                });

                menu.show(DrawableEquipment.this, event.getScreenX(), event.getScreenY());
            }
        });

        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
            }
        });

        setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double deltaX = event.getSceneX() - mouseX;
                double deltaY = event.getSceneY() - mouseY;

                double nx = getX() + deltaX;
                double ny = getY() + deltaY;

                if (nx <= 0 || ny<= 0 )
                    return;

                setTo((float) nx, (float) ny);

                mouseX = event.getSceneX();
                mouseY = event.getSceneY();

                updateListenner.onUpdate();
            }
        });

    }

    public void update() {
        packets.clear();
        for (Interface element : equipment.getInterfaces()) {
            for (PacketOnEquipment packetOnEquipment : element.getPacketsManager().getPackets()) {
                DrawablePacket drawablePacket = new DrawablePacket(packetOnEquipment.getPacket());
                drawablePacket.setTo((float) this.getX(), (float) this.getY());
                packets.add(drawablePacket);
            }
        }
    }

    public  ArrayList<DrawablePacket> getDrawablePackets(){
        return packets;
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

    public double getCenterPointX() {
        return (float) ((getEquipmentDrawable().getWidth() / 2) + getX());
    }

    public double getCenterPointY() {
        return (float) ((getEquipmentDrawable().getHeight() / 2) + getY());
    }

    public Image getEquipmentDrawable() {
        return this.getImage();
    }

    public DrawableEquipment setUpdateListener(OnUpdateListener listener) {
        this.updateListenner = listener;
        return this;
    }

    public void setTo(float x, float y) {
        AnchorPane.setLeftAnchor(this, (double) x);
        AnchorPane.setTopAnchor(this, (double) y);

        setX(x);
        setY(y);
    }

    public DrawableEquipment setActionsListener(OnActionListener actionsListener) {
        this.actionsListener = actionsListener;
        return this;
    }
}
