package iut.unice.dreamteam.UI;

import iut.unice.dreamteam.ApplicationStates;
import iut.unice.dreamteam.Equipments.Equipment;
import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Network;
import iut.unice.dreamteam.Utils.Debug;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CanvasDrawer {
    private final ArrayList<DrawableEquipment> elementsToDraw;
    private final AnchorPane mainPane;
    private final ArrayList<Equipment> equipments;
    private Network network;
    private AnimationTimer timer;
    private long startNanoTime;

    private Line tempLine;
    private ArrayList<Line> links;

    private Timer networkTimer;
    private int timeRender;

    private Timer interfaceTimer;
    private int timeInterface;

    public CanvasDrawer(final AnchorPane mainPane, ArrayList<DrawableEquipment> n, Network network) {
        this.network = network;
        this.elementsToDraw = n;

        equipments = new ArrayList<>();
        links = new ArrayList<>();
        this.mainPane = mainPane;

        tempLine = new Line();
        tempLine.setVisible(false);
        tempLine.setStrokeWidth(1.5);

        timeRender = 2000;
        timeInterface = 500;

        mainPane.getChildren().add(tempLine);

        update();

        mainPane.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (ApplicationStates.getInstance().is(ApplicationStates.CONNECT)) {
                    if (ApplicationStates.getInstance().getData() != null) {
                        DrawableEquipment drawableEquipment = elementsToDraw.get(equipments.indexOf(((Interface) ApplicationStates.getInstance().getData()).getEquipment()));
                        tempLine.setStartX(drawableEquipment.getCenterPointX());
                        tempLine.setStartY(drawableEquipment.getCenterPointY());

                        tempLine.setEndX(event.getX());
                        tempLine.setEndY(event.getY());

                        tempLine.setVisible(true);
                    } else {
                        tempLine.setVisible(false);
                    }
                } else {
                    tempLine.setVisible(false);
                }
            }
        });

    }

    public void stopRender() {
        /*
        if (timer != null) {
            timer.stop();
        }
        */

        if(networkTimer != null)
            networkTimer.cancel();

        if(interfaceTimer != null)
            interfaceTimer.cancel();
    }

    public void startRender() {
        /*
        startNanoTime = System.nanoTime();
        timer = new AnimationTimer() {
            public void handle(long currentNanoTime) {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;

                //draw();

                network.updateEquipments();
            }
        };
        timer.start();
        */

        stopRender();

        networkTimer = new Timer();

        networkTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Debug.log("loop");
                network.updateEquipments();

                /*
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //drawEquipments();

                        /*
                        for (DrawableEquipment d : elementsToDraw) {
                            for (DrawablePacket dp : d.getDrawablePackets()) {
                                for (Interface i : d.getEquipment().getInterfaces()){
                                    if(i.getLink() != null) {
                                        int index = equipments.indexOf(i.getLink().getOpositInterface(i).getEquipment());
                                        DrawableEquipment nextHop = elementsToDraw.get(index);
                                        dp.setTo((float)(nextHop.getX()),(float) (nextHop.getY()));
                                    }
                                }
                            }
                        }
                        */
                /*
                    }
                });
        */
            }
        }, 0, timeRender);

        interfaceTimer = new Timer();

        interfaceTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        drawEquipments();
                    }
                });
            }
        }, 0, timeInterface);
    }

    public Boolean startedRender() {
        return networkTimer != null;
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

        updateLinks();

        draw();
    }

    public void draw() {
        drawLinks();
        drawEquipments();
    }

    private void drawEquipments() {
        for (final DrawableEquipment e : elementsToDraw) {
            if (!mainPane.getChildren().contains(e)) {
                mainPane.getChildren().add(e);
            }

            e.setLabel(e.getEquipment().getName());
            e.toFront();

            drawPackets(e);
        }
    }

    private void drawPackets(DrawableEquipment e) {
        for (DrawablePacket drawablePacket : e.getDrawablePackets()){
            mainPane.getChildren().remove(drawablePacket);
        }

        e.update();

        for (DrawablePacket drawablePacket : e.getDrawablePackets()){
            mainPane.getChildren().add(drawablePacket);
            drawablePacket.toFront();
        }
    }

    private void drawLinks() {
        for (Line l : links) {
            if (!mainPane.getChildren().contains(l))
                mainPane.getChildren().add(l);
        }
    }

    private void updateLinks() {
        for (Line l : links)
            mainPane.getChildren().remove(l);

        links.clear();

        for (Equipment e : equipments) {
            for (final Interface i : e.getInterfaces()) {
                if (i.getLink() != null) {
                    Interface op = i.getLink().getOpositInterface(i);
                    int posEa = equipments.indexOf(e);
                    int posEb = equipments.indexOf(op.getEquipment());

                    DrawableEquipment dEa = elementsToDraw.get(posEa);
                    DrawableEquipment dEb = elementsToDraw.get(posEb);


                    //LinkPoint point = dEa.getLinkPointForInterface(i);

                    Line link = new Line(dEa.getCenterPointX(), dEa.getCenterPointY(), dEb.getCenterPointX(), dEb.getCenterPointY());
                    link.setStrokeWidth(1.5);

                    link.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (ApplicationStates.getInstance().is(ApplicationStates.DELETE)){
                                i.getLink().brakeLink();
                            }
                            update();
                        }
                    });

                    this.links.add(link);
                }
            }

        }
    }

}