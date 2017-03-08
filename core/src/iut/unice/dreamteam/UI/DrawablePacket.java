package iut.unice.dreamteam.UI;

import iut.unice.dreamteam.Interfaces.Packet;
import iut.unice.dreamteam.Utils.ColorUtils;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.json.JSONObject;

public class DrawablePacket extends Pane {
    private int SHIFT_LABEL_X = 25;

    private Packet packet;

    private ImageView imageView;
    private Label label;

    public DrawablePacket(Packet packet) {
        super();

        setPacket(packet);
        loadGraphics();

        setLabel(((JSONObject) packet.getApplicationLayer().getContent().get("protocol")).getString("name"));
    }

    public Packet getPacket() {
        return this.packet;
    }

    private void setPacket(Packet packet) {
        this.packet = packet;
    }

    private void loadGraphics() {
        this.imageView = new ImageView();
        this.imageView.setPreserveRatio(true);
        this.imageView.setImage(new Image(getClass().getResource("/packet/frame.png").toExternalForm()));

        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(1.0);
        lighting.setSpecularConstant(0.0);
        lighting.setSpecularExponent(0.0);
        lighting.setSurfaceScale(0.0);
        lighting.setLight(new Light.Distant(45, 45, ColorUtils.getColor(packet.getPacketId())));

        this.imageView.setEffect(lighting);

        this.label = new Label();
        this.label.setLayoutX(SHIFT_LABEL_X);

        getChildren().add(this.imageView);
        getChildren().add(this.label);
    }

    public DrawablePacket setX(float x) {
        setLayoutX(x);

        return this;
    }

    public DrawablePacket setY(float y) {
        setLayoutY(y);

        return this;
    }

    public void setTo(float x, float y) {
        AnchorPane.setLeftAnchor(this, (double) x);
        AnchorPane.setTopAnchor(this, (double) y);

        setX(x);
        setY(y);
    }

    public void setLabel(String label) {
        this.label.setText(label);
    }
}
