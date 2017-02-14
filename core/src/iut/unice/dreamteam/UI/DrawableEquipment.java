package iut.unice.dreamteam.UI;

import iut.unice.dreamteam.Equipments.*;
import javafx.scene.image.Image;

/**
 * Created by Guillaume on 14/02/2017.
 */
public class DrawableEquipment {
    private Equipment equipment;
    private float x;
    private float y;

    public DrawableEquipment(Equipment e) {
        this.equipment = e;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public float getX() {
        return x;
    }

    public DrawableEquipment setX(float x) {
        this.x = x;
        return this;
    }

    public float getY() {
        return y;
    }

    public DrawableEquipment setY(float y) {
        this.y = y;
        return this;
    }

    public static Image getEquipmentDrawable(Equipment e) {
        if (e instanceof Router)
            return new Image(e.getClass().getResource("/devices/Router.png").toString());

        else if (e instanceof Switch)
            return new Image(e.getClass().getResource("/devices/Switch.png").toString());

        else if (e instanceof AccessPoint)
            return new Image(e.getClass().getResource("/devices/Access Point.png").toString());

        else if (e instanceof Hub)
            return new Image(e.getClass().getResource("/devices/Hub.png").toString());

        else if (e instanceof Computer)
            return new Image(e.getClass().getResource("/devices/Computer.png").toString());

        return null;

    }
}
