package iut.unice.dreamteam.UI;

import iut.unice.dreamteam.Interfaces.Packet;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * Created by Romain on 05/03/2017.
 */
public class DrawablePacket extends ImageView {
    private Packet packet;

    DrawablePacket(Packet packet) {
        super();
        setPreserveRatio(true);
        this.setImage(new Image(getClass().getResource("/packet/frame.png").toExternalForm()));

        this.packet = packet;
    }

    public DrawablePacket setX(float x) {
        super.setX(x);
        return this;
    }

    public DrawablePacket setY(float y) {
        super.setY(y);
        return this;
    }

    public void setTo(float x, float y) {
        AnchorPane.setLeftAnchor(this, (double) x);
        AnchorPane.setTopAnchor(this, (double) y);

        setX(x);
        setY(y);
    }
}
