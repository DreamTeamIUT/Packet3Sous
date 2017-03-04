package iut.unice.dreamteam.Protocols;


import iut.unice.dreamteam.Interfaces.Packet;
import iut.unice.dreamteam.NetworkLayers.ApplicationLayer;
import iut.unice.dreamteam.NetworkLayers.IpLayer;
import iut.unice.dreamteam.NetworkLayers.MacLayer;
import org.json.JSONObject;

import java.util.ArrayList;

public class TCP extends TransportProtocol {
    public TCP() {
        super("TCP");
    }

    public TCP(int port){
        super("TCP");

        setPort(port);
    }

    public static Boolean isProtocol(Packet p) {
        return p.getTransportLayer().getTransportProtocol().getName().equals("TCP");
    }

    public static Boolean isPacket(Packet p) {
        JSONObject jsonObject = p.getApplicationLayer().getContent();

        return jsonObject.has("type") && jsonObject.getString("type").equals("TCP") && jsonObject.has("status");
    }

    public static Boolean isConnectionPacket(Packet p) {
        JSONObject jsonObject = p.getApplicationLayer().getContent();

        return isPacket(p) && (jsonObject.getString("status").equals("SIN") || jsonObject.get("status").equals("SIN ACK"));
    }

    public static Boolean isTransmissionPacket(Packet p) {
        JSONObject jsonObject = p.getApplicationLayer().getContent();

        return isPacket(p) && jsonObject.getString("status").equals("ACK");
    }

    public static Packet ackPacket(Packet p) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "TCP");
        jsonObject.put("status", "ACK");
        jsonObject.put("packet-id", p.getPacketId());

        Packet generatedPacket = ApplicationService.getReturnPacket(p);
        generatedPacket.setApplicationLayer(new ApplicationLayer(jsonObject));

        return generatedPacket;
    }
}
