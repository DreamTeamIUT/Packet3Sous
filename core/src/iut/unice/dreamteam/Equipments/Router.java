package iut.unice.dreamteam.Equipments;

import iut.unice.dreamteam.Interfaces.IncomingPacketInterface;
import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Interfaces.Packet;
import iut.unice.dreamteam.Interfaces.WiredInterface;
import iut.unice.dreamteam.NetworkLayers.MacLayer;


public class Router extends Equipment implements IncomingPacketInterface {
    public Router(String name) {
        super(name);

        initialize(2, WiredInterface.class, this);
        setIncomingPacketInterface(this);
    }

    @Override
    public void onBroadcast(Interface i, Packet p) {

    }

    @Override
    public void onUnicast(Interface i, Packet p) {

    }

    @Override
    public void onForward(Interface i, Packet p) {
        Packet packet = new Packet();
        packet.setApplicationLayer(p.getApplicationLayer());
        packet.setTransportLayer(p.getTransportLayer());
        packet.setIpLayer(p.getIpLayer());

        sendPacket(packet);
    }

    @Override
    public void onReceive(Interface i, Packet p) {

    }
}
