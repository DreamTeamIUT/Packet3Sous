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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class DrawableEquipment extends Pane implements OnUpdateListener {

    private final int SHIFT_LABEL_Y = 31;

    private final int SHIFT_PACKET = 20;
    private final int SHIFT_NEW_PACKET = 15;

    private Equipment equipment;
    private OnUpdateListener onUpdateListener;
    private double mouseX;
    private double mouseY;
    private OnActionListener actionsListener;

    private ImageView imageView;
    private Label label;

    private ArrayList<DrawablePacket> packets;
    private int shiftQueue;

    public DrawableEquipment(Equipment e) {
        super();

        this.equipment = e;
        this.equipment.setInterfaceUpdater(this);

        this.packets = new ArrayList<>();
        this.shiftQueue = 0;

        loadGraphics();
        setLabel(e.getName());
    }

    private void loadGraphics() {
        this.imageView = new ImageView();
        this.imageView.setPreserveRatio(true);
        this.imageView.setImage(DrawableLoader.getInstance().getEquipmentDrawable(this.equipment));

        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY) && ApplicationStates.getInstance().is(ApplicationStates.CONNECT)) {
                    Debug.log("Create context...");
                    InterfaceContextMenu menu = new InterfaceContextMenu(equipment, new OnUpdateListener() {
                        @Override
                        public void onUpdate() {
                            if (onUpdateListener != null)
                                onUpdateListener.onUpdate();
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
                menu.setUpdateListenner(onUpdateListener);
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

                double nx = getLayoutX() + deltaX;
                double ny = getLayoutY() + deltaY;

                if (nx <= 0 || ny<= 0 )
                    return;

                setTo((float) nx, (float) ny);

                mouseX = event.getSceneX();
                mouseY = event.getSceneY();

                onUpdateListener.onUpdate();
            }
        });

        this.label = new Label();
        this.label.setLayoutY(SHIFT_LABEL_Y);

        getChildren().add(this.imageView);
        getChildren().add(this.label);
    }

    public void update() {
        Debug.log("UPDATING PACKETS ON " + equipment.getName());

        packets.clear();
        shiftQueue = 0;

        for (Interface element : equipment.getInterfaces()) {
            for (PacketOnEquipment packetOnEquipment : element.getPacketsManager().getPackets()) {
                if (!packetOnEquipment.getPacketProperties().isSent()) {
                    Debug.log("PACKET WAITING FOR SEND ON " + equipment.getName());

                    DrawablePacket dp = new DrawablePacket(packetOnEquipment.getPacket());
                    dp.setTo((float) this.getLayoutX(), (float) this.getLayoutY() - SHIFT_PACKET - shiftQueue);
                    packets.add(dp);

                    shiftQueue += SHIFT_NEW_PACKET;
                }
            }

            for (PacketOnEquipment packetOnEquipment : element.getPacketsManager().getReceivedPackets()) {
                if (!packetOnEquipment.getPacketProperties().isDisplayed()) {
                    Debug.log("PACKET RECEIVED ON " + equipment.getName());

                    packetOnEquipment.getPacketProperties().setDisplayed();

                    DrawablePacket dp = new DrawablePacket(packetOnEquipment.getPacket());
                    dp.setTo((float) this.getLayoutX(), (float) this.getLayoutY() - SHIFT_PACKET - shiftQueue);
                    packets.add(dp);

                    shiftQueue += SHIFT_NEW_PACKET;
                }
            }
        }
    }

    public void setLabel(String label) {
        this.label.setText(label);
    }

    public ArrayList<DrawablePacket> getDrawablePackets(){
        return packets;
    }

    public Equipment getEquipment() {
        return equipment;
    }


    public DrawableEquipment setX(float x) {
        super.setLayoutX(x);

        return this;
    }

    public DrawableEquipment setY(float y) {
        super.setLayoutY(y);

        return this;
    }

    public double getCenterPointX() {
        return (float) ((getEquipmentDrawable().getWidth() / 2) + getLayoutX());
    }

    public double getCenterPointY() {
        return (float) ((getEquipmentDrawable().getHeight() / 2) + getLayoutY());
    }

    public Image getEquipmentDrawable() {
        return this.imageView.getImage();
    }

    public void updateListener() {
        if (this.onUpdateListener != null)
            this.onUpdateListener.onUpdate();
    }

    public DrawableEquipment setOnUpdateListener(OnUpdateListener listener) {
        this.onUpdateListener = listener;
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

    @Override
    public void onUpdate() {
        Debug.log("on update drawable equipment");

        update();
        updateListener();
    }
}
