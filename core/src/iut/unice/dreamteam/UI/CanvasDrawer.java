package iut.unice.dreamteam.UI;

import iut.unice.dreamteam.ApplicationStates;
import iut.unice.dreamteam.Equipments.Equipment;
import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Network;
import iut.unice.dreamteam.UI.ContextMenus.DeviceContextMenu;
import iut.unice.dreamteam.UI.ContextMenus.InterfaceContextMenu;
import iut.unice.dreamteam.Utils.Debug;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

import java.util.ArrayList;

/**
 * Created by Guillaume on 13/02/2017.
 */
public class CanvasDrawer {
    private final ArrayList<DrawableEquipment> elementsToDraw;
    private final AnchorPane mainPane;
    private final ArrayList<Equipment> equipments;
    private Network network;
    // private final Canvas canvas;
    public GraphicsContext gc;
    private AnimationTimer timer;
    private long startNanoTime;

    private Line tempLine;

    public CanvasDrawer(final AnchorPane mainPane, ArrayList<DrawableEquipment> n, Network network) {
        this.network = network;
        this.elementsToDraw = n;

        equipments = new ArrayList<>();
        this.mainPane = mainPane;

        tempLine = new Line();
        tempLine.setVisible(false);
        tempLine.setStrokeWidth(1.5);

        update();

        mainPane.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (ApplicationStates.getInstance().is(ApplicationStates.CONNECT))
                {
                    if (ApplicationStates.getInstance().getData() != null){
                        DrawableEquipment drawableEquipment = elementsToDraw.get(equipments.indexOf(((Interface) ApplicationStates.getInstance().getData()).getEquipment()));
                        tempLine.setStartX(drawableEquipment.getCenterPointX());
                        tempLine.setStartY(drawableEquipment.getCenterPointY());

                        tempLine.setEndX(event.getX());
                        tempLine.setEndY(event.getY());

                        tempLine.setVisible(true);
                    }
                    else {
                        tempLine.setVisible(false);
                    }
                }
                else {
                    tempLine.setVisible(false);
                }
            }
        });

    }

    public void stopRender() {
        if (timer != null) {
            timer.stop();
        }
    }

    public void startRender() {
        startNanoTime = System.nanoTime();
        timer = new AnimationTimer() {
            public void handle(long currentNanoTime) {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;

                //draw();
            }
        };
        timer.start();
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void update() {

        Debug.log("UPDATE ! ");

        equipments.clear();
        for (DrawableEquipment drawableEquipment : elementsToDraw)
            equipments.add(drawableEquipment.getEquipment());

        mainPane.getChildren().clear();
        mainPane.getChildren().add(tempLine);

        drawLinks();

        drawEquipements();

        //draw();
    }

    private void drawEquipements() {
        for (final DrawableEquipment e : elementsToDraw) {
            final ImageView i = new ImageView(e.getEquipmentDrawable());
            i.setPreserveRatio(true);

            AnchorPane.setLeftAnchor(i, (double) e.getX());
            AnchorPane.setTopAnchor(i, (double) e.getY());

            i.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getButton().equals(MouseButton.PRIMARY) && ApplicationStates.getInstance().is(ApplicationStates.CONNECT)) {
                        Debug.log("Create context...");
                        InterfaceContextMenu menu = new InterfaceContextMenu(e.getEquipment(), CanvasDrawer.this);
                        menu.show(i, event.getScreenX(), event.getScreenY());
                    } else if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                        Debug.log("Double click !");
                    }
                }
            });

            i.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {
                    DeviceContextMenu menu = new DeviceContextMenu(e.getEquipment());
                    menu.show(i, event.getScreenX(), event.getScreenY());
                }
            });

            mainPane.getChildren().add(i);

        }
    }

    private void drawLinks() {

        for (Equipment e : equipments) {
            for (Interface i : e.getInterfaces()) {
                    if (i.getLink() != null) {
                        Interface op = i.getLink().getOpositInterface(i);
                        int posEa = equipments.indexOf(e);
                        int posEb = equipments.indexOf(op.getEquipment());

                        DrawableEquipment dEa = elementsToDraw.get(posEa);
                        DrawableEquipment dEb = elementsToDraw.get(posEb);

                        //LinkPoint point = dEa.getLinkPointForInterface(i);

                        Line link = new Line(dEa.getCenterPointX(), dEa.getCenterPointY(), dEb.getCenterPointX(), dEb.getCenterPointY());
                        link.setStrokeWidth(1.5);
                        this.mainPane.getChildren().add(link);
                    }
            }

        }
    }

}

