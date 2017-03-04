package iut.unice.dreamteam.Protocols;

import iut.unice.dreamteam.Utils.Debug;
import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Interfaces.Packet;
import iut.unice.dreamteam.Interfaces.PacketOnEquipment;
import iut.unice.dreamteam.Network;
import iut.unice.dreamteam.NetworkLayers.ApplicationLayer;
import iut.unice.dreamteam.NetworkLayers.IpLayer;
import iut.unice.dreamteam.NetworkLayers.MacLayer;
import org.json.JSONObject;


public class ARP extends ApplicationProtocol {
    public ARP() {
        super("ARP");

        setUDP(500);
    }

    @Override
    public Packet initiate(Interface i, JSONObject parameters) {
        Packet p = new Packet();

        JSONObject protocol = new JSONObject();
        protocol.put("name", "ARP");
        protocol.put("message", "Who is ?");
        protocol.put("ip-address", parameters.getString("ip-address"));

        JSONObject answer = new JSONObject();
        answer.put("protocol", protocol);

        p.setApplicationLayer(new ApplicationLayer(answer));
        p.setIpLayer(new IpLayer(i.getIp(), parameters.getString("ip-address")));
        p.setMacLayer(new MacLayer(i.getMacAddress(), MacLayer.getBroadcastAddress()));

        return p;
    }

    @Override
    public Packet processPacket(Interface i, Packet packet) {
        JSONObject protocol = (JSONObject)packet.getApplicationLayer().getContent().get("protocol");

        if (protocol.getString("message").equals("Who is ?")) {
            Debug.protocol(getName(), "receiving broadcast from " + packet.getIpLayer().getSource(), i.getEquipment());

            String ipAddress = protocol.getString("ip-address");

            if(ipAddress.equals(i.getIp())) {
                Debug.protocol(getName(), "message who is ? from " + packet.getIpLayer().getSource(), i.getEquipment());

                JSONObject protocolAnswer = new JSONObject();
                protocolAnswer.put("name", "ARP");
                protocolAnswer.put("message", "I am");
                //protocolAnswer.put("packet-id", packet.getPacketId());

                JSONObject answer = new JSONObject();
                answer.put("protocol", protocolAnswer);

                Packet backPacket = new Packet();
                backPacket.setApplicationLayer(new ApplicationLayer(answer));
                backPacket.setIpLayer(new IpLayer(i.getIp(), packet.getIpLayer().getSource()));
                backPacket.setMacLayer(new MacLayer(i.getMacAddress(), packet.getMacLayer().getSource()));

                return backPacket;
            }
        }
        else if(protocol.getString("message").equals("I am")) {
            Debug.protocol(getName(), "message I am from " + packet.getIpLayer().getSource(), i.getEquipment());

            i.getEquipment().addArpAssociation(packet.getIpLayer().getSource(), packet.getMacLayer().getSource());

            Debug.protocol(getName(), "adding entry " + packet.getIpLayer().getSource() + " for " + packet.getMacLayer().getSource(), i.getEquipment());

            for (PacketOnEquipment packetOnEquipment : i.getPacketsManager().getPackets()) {
                if (!packetOnEquipment.getPacketProperties().isSent()) {
                    if (packetOnEquipment.getPacket().getIpLayer().getDestination().equals(packet.getIpLayer().getSource())) {
                        Debug.log("updating packet 1 " + packetOnEquipment.getPacket().getApplicationLayer().getContent());

                        packetOnEquipment.getPacket().setMacLayer(new MacLayer(i.getMacAddress(), packet.getMacLayer().getSource()));
                        packetOnEquipment.getPacketProperties().setWaitingId(null);
                    }
                    else if (packet.getIpLayer().getSource().equals(i.getGateway()) && !Network.isInSameNetwork(packetOnEquipment.getPacket().getIpLayer().getDestination(), i.getIp(), i.getMask())) {
                        Debug.log("updating packet 2 " + packetOnEquipment.getPacket().getApplicationLayer().getContent());

                        packetOnEquipment.getPacket().setMacLayer(new MacLayer(i.getMacAddress(), packet.getMacLayer().getSource()));
                        packetOnEquipment.getPacketProperties().setWaitingId(null);

                        Debug.packet(packetOnEquipment.getPacket());
                    }
                }
            }

            /*
            if(i.getPacketsManager().existPacket(protocol.getString("packet-id"))) {
                Debug.protocol(getName(), "adding entry " + packet.getIpLayer().getSource() + " for " + packet.getMacLayer().getSource(), i.getEquipment());

                i.getPacketsManager().getPacket(protocol.getString("packet-id")).getPacketProperties().setWaitingId(null);
                //i.launchQueue();
            }
            */
        }

        return null;
    }
}
