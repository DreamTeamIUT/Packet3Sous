package iut.unice.dreamteam.Utils;

import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Created by Guillaume on 05/03/2017.
 */
public class ColorUtils {
    public static Color getRandomColor(){
        Random rand = new Random();

        float r = rand.nextFloat() / 2f + 0.5f;
        float g = rand.nextFloat() / 2f + 0.5f;
        float b = rand.nextFloat() / 2f + 0.5f;

        return Color.color(r,g,b);
    }
}
