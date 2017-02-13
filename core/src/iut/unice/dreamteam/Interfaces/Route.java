package iut.unice.dreamteam.Interfaces;

/**
 * Created by dd500076 on 13/02/17.
 */
public class Route {
    private String network;
    private String mask;
    private String nextHop;

    public Route(String network, String mask, String nextHop) {
        setNetwork(network);
        setMask(mask);
        setNextHop(nextHop);
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getNextHop() {
        return nextHop;
    }

    public void setNextHop(String nextHop) {
        this.nextHop = nextHop;
    }
}
