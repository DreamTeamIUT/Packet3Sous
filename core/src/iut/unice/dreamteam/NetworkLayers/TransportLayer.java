package iut.unice.dreamteam.NetworkLayers;

import iut.unice.dreamteam.NetworkLayers.GenericLayer;
import iut.unice.dreamteam.Protocols.TransportProtocol;


public class TransportLayer extends GenericLayer {
    private int sourcePort;
    private int destinationPort;

    private TransportProtocol transportProtocol;

    public TransportLayer(TransportProtocol transportProtocol) {
        setTransportProtocol(transportProtocol);
    }

    public TransportLayer(TransportProtocol transportProtocol, int sourcePort, int destinationPort) {
        setTransportProtocol(transportProtocol);
        setSourcePort(sourcePort);
        setDestinationPort(destinationPort);
    }

    public TransportProtocol getTransportProtocol() {
        return transportProtocol;
    }

    public void setTransportProtocol(TransportProtocol transportProtocol) {
        this.transportProtocol = transportProtocol;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(int sourcePort) {
        this.sourcePort = sourcePort;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(int destinationPort) {
        this.destinationPort = destinationPort;
    }
}
