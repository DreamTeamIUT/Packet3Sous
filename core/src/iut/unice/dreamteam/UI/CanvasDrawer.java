package iut.unice.dreamteam.UI;

import iut.unice.dreamteam.Network;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by Guillaume on 13/02/2017.
 */
public class CanvasDrawer {
    private final ArrayList<DrawableEquipment> elementsToDraw;
    private Network network;
    private final Canvas canvas;
    public GraphicsContext gc;
    private AnimationTimer timer;
    private long startNanoTime;

    public CanvasDrawer(AnchorPane mainPane, ArrayList<DrawableEquipment> n, Network network, Canvas c) {
        this.network = network;
        this.elementsToDraw = n;
        this.canvas = c;

        gc = c.getGraphicsContext2D();

        mainPane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                System.out.println("Width: " + newSceneWidth);
                canvas.setWidth((Double) newSceneWidth);
            }
        });

        mainPane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                System.out.println("Height: " + newSceneHeight);
                canvas.setHeight((Double) newSceneHeight);
            }
        });

    }

    private void draw() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());


        for (DrawableEquipment e: this.elementsToDraw){
           gc.drawImage(e.getEquipmentDrawable(), e.getX(), e.getY());
        }

    }

    public void stopRender() {
        if (timer != null)
        {
            timer.stop();
        }
    }

    public void startRender() {
        startNanoTime = System.nanoTime();
        timer = new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;

                draw();
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
        draw();
    }
}
