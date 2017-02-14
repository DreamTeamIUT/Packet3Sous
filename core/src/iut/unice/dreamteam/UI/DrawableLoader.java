package iut.unice.dreamteam.UI;

import iut.unice.dreamteam.Equipments.*;
import javafx.scene.image.Image;

/**
 * Created by Guillaume on 14/02/2017.
 */
public class DrawableLoader {
    private Image routerImage;
    private Image hubImage;
    private Image switchImage;
    private Image computerImage;
    private Image ApImage;
    private static DrawableLoader loader;

    private DrawableLoader() {
        this.routerImage =  new Image(getClass().getResource("/devices/Router.png").toString());
        this.switchImage = new Image(getClass().getResource("/devices/Switch.png").toString());
        this.ApImage = new Image(getClass().getResource("/devices/Access Point.png").toString());
        this.hubImage = new Image(getClass().getResource("/devices/Hub.png").toString());
        this.computerImage = new Image(getClass().getResource("/devices/Computer.png").toString());

    }

    public static DrawableLoader getInstance() {
        if (loader == null)
            loader = new DrawableLoader();

        return loader;
    }

    public Image getEquipmentDrawable(Equipment e) {
        if (e instanceof Router)
            return routerImage;

        else if (e instanceof Switch)
            return switchImage;

        else if (e instanceof AccessPoint)
            return ApImage;

        else if (e instanceof Hub)
            return hubImage;

        else if (e instanceof Computer)
            return computerImage;

        return null;

    }
}
