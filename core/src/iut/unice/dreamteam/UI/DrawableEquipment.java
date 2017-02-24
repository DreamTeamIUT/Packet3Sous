package iut.unice.dreamteam.UI;

import iut.unice.dreamteam.Equipments.Equipment;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;

/**
 * Created by Guillaume on 14/02/2017.
 */
public class DrawableEquipment extends Circle{


    private Equipment equipment;
    private float x;
    private float y;
    private Image drawable;

    public DrawableEquipment(Equipment e) {
        super();
        this.equipment = e;
        this.drawable = DrawableLoader.getInstance().getEquipmentDrawable(this.equipment);
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

    public double getCenterPointX(){
        return (float) ((getEquipmentDrawable().getWidth()/2) + getX());
    }

    public double getCenterPointY(){
        return (float) ((getEquipmentDrawable().getHeight()/2) + getY());
    }

    public Image getEquipmentDrawable() {
        return this.drawable;
    }
}
