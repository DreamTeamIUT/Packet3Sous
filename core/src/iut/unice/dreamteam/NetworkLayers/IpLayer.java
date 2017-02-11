package iut.unice.dreamteam.NetworkLayers;

import iut.unice.dreamteam.NetworkLayers.GenericLayer;


public class IpLayer extends GenericLayer {
    private String source;
    private String destination;

    public IpLayer() {
        super();
    }

    public IpLayer(String source, String destination) {
        super();

        setSource(source);
        setDestination(destination);
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
