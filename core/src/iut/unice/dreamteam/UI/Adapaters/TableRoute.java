package iut.unice.dreamteam.UI.Adapaters;

import iut.unice.dreamteam.Interfaces.Route;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Guillaume on 10/03/2017.
 */
public class TableRoute {
    private Route route;
    private SimpleStringProperty network;
    private SimpleStringProperty mask;
    private SimpleStringProperty nextHop;


    {
        network = new SimpleStringProperty();
        mask = new SimpleStringProperty();
        nextHop = new SimpleStringProperty();
    }


    public TableRoute(){

    }

    public TableRoute(Route route) {
        if (route != null){
            this.route = route;
            setMask(route.getMask());
            setNetwork(route.getNetwork());
            setNextHop(route.getNextHop());
        }
    }


    public String getNetwork() {
        return network.get();
    }

    public SimpleStringProperty networkProperty() {
        return network;
    }

    public void setNetwork(String network) {
        this.network.set(network);
    }

    public String getMask() {
        return mask.get();
    }

    public SimpleStringProperty maskProperty() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask.set(mask);
    }

    public String getNextHop() {
        return nextHop.get();
    }

    public SimpleStringProperty nextHopProperty() {
        return nextHop;
    }

    public void setNextHop(String nextHop) {
        this.nextHop.set(nextHop);
    }

    public Route getRoute() {

        if (route != null) {
            route.setMask(getMask());
            route.setNetwork(getNetwork());
            route.setNextHop(getNextHop());
        } else {
            route = new Route(getNetwork(), getMask(), getNextHop());
        }

        return route;

    }
}
