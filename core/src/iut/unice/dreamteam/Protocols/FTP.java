package iut.unice.dreamteam.Protocols;

import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Interfaces.Packet;
import iut.unice.dreamteam.NetworkLayers.ApplicationLayer;
import iut.unice.dreamteam.NetworkLayers.IpLayer;
import iut.unice.dreamteam.NetworkLayers.MacLayer;
import org.json.JSONObject;

/**
 * Created by Romain on 13/02/2017.
 */
public class FTP extends ApplicationProtocol {
    public FTP() {
        super("FTP");
    }

    @Override
    public Packet initiate(Interface i, JSONObject parameters) {
        Packet p = new Packet();

        JSONObject protocol = new JSONObject();
        protocol.put("name", "ARP");
        protocol.put("message", "Who is ?");
        protocol.put("ip-address", parameters.getString("ip-address"));
        protocol.put("port", parameters.getString("port"));

        JSONObject answer = new JSONObject();
        answer.put("protocol", protocol);

        p.setApplicationLayer(new ApplicationLayer(answer));
        p.setIpLayer(new IpLayer(i.getIp(), parameters.getString("ip-address")));
        p.setMacLayer(new MacLayer(i.getMacAddress(), MacLayer.getBroadcastAddress()));

        return p;
    }

    @Override
    public Packet processPacket(Interface i, Packet p) {
        return null;
    }
}
