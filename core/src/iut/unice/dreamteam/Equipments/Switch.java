package iut.unice.dreamteam.Equipments;

import iut.unice.dreamteam.Utils.Debug;
import iut.unice.dreamteam.Interfaces.IncomingPacketInterface;
import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Interfaces.Packet;
import iut.unice.dreamteam.Interfaces.WiredInterface;
import iut.unice.dreamteam.NetworkLayers.MacLayer;

import java.util.HashMap;


public class Switch extends Equipment implements IncomingPacketInterface {
    private HashMap<String, Interface> macTable;

    public Switch(String name) {
        super(name);

        initialize(24, WiredInterface.class, this, true);
        setIncomingPacketInterface(this);

        macTable = new HashMap<>();

    }

    @Override
    public void onBroadcast(Interface i, Packet p) {

    }

    @Override
    public void onUnicast(Interface i, Packet p) {

    }

    @Override
    public void onForward(Interface i, Packet p) {

    }

    @Override
    public void onReceive(Interface i, Packet p) {
        Debug.log("For the switch !");
        updateTable(i, p);

        if (macTable.containsKey(p.getMacLayer().getDestination())){
            macTable.get(p.getMacLayer().getDestination()).sendPacket(p);
        }
        else {
            for (Interface iface : getInterfaces()){
                if (!iface.getMacAddress().equals(i.getMacAddress()))
                    iface.sendPacket(p);
            }
        }

    }

    private void updateTable(Interface i, Packet p) {
        if (macTable.containsKey(p.getMacLayer().getSource())){
            macTable.remove(p.getMacLayer().getSource());
        }

        if (!MacLayer.isBroadcastAddress(p.getMacLayer().getSource()))
            macTable.put(p.getMacLayer().getSource(), i);

    }
}
