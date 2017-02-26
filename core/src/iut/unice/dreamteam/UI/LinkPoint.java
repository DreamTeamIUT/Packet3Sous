package iut.unice.dreamteam.UI;

/**
 * Created by Guillaume on 26/02/2017.
 */
public class LinkPoint {
    private float y;
    private float x;

    public LinkPoint(){

    }

    public LinkPoint(float x, float y){
        this.x = x;
        this.y = y;
    }


    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }
}
