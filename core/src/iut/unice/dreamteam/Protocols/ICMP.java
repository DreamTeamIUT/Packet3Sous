package iut.unice.dreamteam.Protocols;

import iut.unice.dreamteam.Equipments.Equipment;
import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Interfaces.Packet;
import iut.unice.dreamteam.Interfaces.PacketOnEquipment;
import iut.unice.dreamteam.Network;
import iut.unice.dreamteam.NetworkLayers.ApplicationLayer;
import iut.unice.dreamteam.NetworkLayers.IpLayer;
import iut.unice.dreamteam.NetworkLayers.MacLayer;
import iut.unice.dreamteam.Utils.Debug;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Romain on 13/02/2017.
 */
public class ICMP extends ApplicationProtocol {

    public ICMP() {
        super("ICMP");

        setUDP(501);

        addCommand("ping", new CommandInterpreter.CommandExecution() {
            @Override
            public Boolean execute(Equipment equipment, String command, ArrayList<String> arguments) {
                if (arguments.size() < 1)
                    return false;

                Interface sourceInterface = (arguments.size() == 2 && arguments.get(1).contains("eth")) ? equipment.getInterface(arguments.get(1).substring(3, 1)) : equipment.getDefaultGateway();

                equipment.sendPacket(new ICMP().initiate(sourceInterface, new JSONObject().put("ip-address", arguments.get(0))));

                return true;
            }
        });
    }

    @Override
    public Packet initiate(Interface i, JSONObject parameters) {
        Packet p = new Packet();

        JSONObject protocol = new JSONObject();
        protocol.put("name", "ICMP");
        protocol.put("message", "ECHO-REQUEST");
        //protocol.put("ip-address", parameters.getString("ip-address"));

        JSONObject answer = new JSONObject();
        answer.put("protocol", protocol);

        p.setApplicationLayer(new ApplicationLayer(answer));
        p.setIpLayer(new IpLayer(i.getIp(), parameters.getString("ip-address")));

        return p;
    }

    @Override
    public Packet processPacket(Interface i, Packet p) {
        JSONObject protocol = (JSONObject) p.getApplicationLayer().getContent().get("protocol");

        if (protocol.getString("message").equals("ECHO-REQUEST")) {
            Debug.protocol(getName(), "reply from " + p.getIpLayer().getSource(), i.getEquipment());

            Debug.protocol(getName(), "message echo REQUEST from " + p.getIpLayer().getSource(), i.getEquipment());

            JSONObject protocolAnswer = new JSONObject();
            protocolAnswer.put("name", "ICMP");
            protocolAnswer.put("message", "ECHO-REPLY");

            JSONObject answer = new JSONObject();
            answer.put("protocol", protocolAnswer);

            Packet backPacket = new Packet();
            backPacket.setApplicationLayer(new ApplicationLayer(answer));
            backPacket.setIpLayer(new IpLayer(i.getIp(), p.getIpLayer().getSource()));
            return backPacket;
        }
        return null;
    }
}