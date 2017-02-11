package iut.unice.dreamteam.NetworkLayers;

import iut.unice.dreamteam.NetworkLayers.GenericLayer;
import iut.unice.dreamteam.Protocols.TransportProtocol;


public class TransportLayer extends GenericLayer {
    private int sourcePort;
    private int destinationPort;

    private TransportProtocol transportProtocol;

}
