package iut.unice.dreamteam.UI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class MainUiController implements Initializable{

    @FXML
    private Canvas mainCanvas;
    @FXML
    private AnchorPane mainPane;
    @FXML
    public GraphicsContext gc;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Test");
        initGraphics();

        mainPane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                System.out.println("Width: " + newSceneWidth);
                mainCanvas.setWidth((Double) newSceneWidth);
                drawClicked();
            }
        });
        mainPane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                System.out.println("Height: " + newSceneHeight);
                mainCanvas.setHeight((Double) newSceneHeight);
                drawClicked();
            }
        });

    }

    public void initGraphics() {
        gc = mainCanvas.getGraphicsContext2D();
        drawClicked();

    }

    public void drawClicked() {
        gc.clearRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
    }
}
