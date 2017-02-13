package iut.unice.dreamteam.UI;

import iut.unice.dreamteam.Network;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Guillaume on 13/02/2017.
 */
public class CanvasDrawer {
    private Network network;
    private final Canvas canvas;
    public GraphicsContext gc;
    private Timer timer;

    public CanvasDrawer(AnchorPane mainPane, Network n, Canvas c) {
        this.network = n;
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
        System.out.println("draw ");
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());


    }

    public void stopRender() {
        if (timer != null)
        {
            timer.cancel();
            timer.purge();
        }
    }

    public void startRender() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                draw();
            }
        }, 0, 120);
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }
}
