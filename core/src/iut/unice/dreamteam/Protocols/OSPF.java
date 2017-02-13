package iut.unice.dreamteam.Protocols;

import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Interfaces.Packet;
import org.json.JSONObject;

/**
 * Created by Romain on 13/02/2017.
 */
public class OSPF extends ApplicationProtocol {
    public OSPF() {
        super("OSPF");
    }

    @Override
    public Packet initiate(Interface i, JSONObject parameters) {
        return null;
    }

    @Override
    public Packet processPacket(Interface i, Packet p) {
        return null;
    }
}
