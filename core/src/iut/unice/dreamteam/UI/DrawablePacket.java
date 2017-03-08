package iut.unice.dreamteam.UI;

import iut.unice.dreamteam.Interfaces.Packet;
import iut.unice.dreamteam.Utils.ColorUtils;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class DrawablePacket extends ImageView {
    private Packet packet;

    DrawablePacket(Packet packet) {
        super();
        setPreserveRatio(true);
        setImage(new Image(getClass().getResource("/packet/frame.png").toExternalForm()));
        this.packet = packet;
        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(1.0);
        lighting.setSpecularConstant(0.0);
        lighting.setSpecularExponent(0.0);
        lighting.setSurfaceScale(0.0);
        lighting.setLight(new Light.Distant(45, 45, ColorUtils.getColor(packet.getPacketId())));

        setEffect(lighting);


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
