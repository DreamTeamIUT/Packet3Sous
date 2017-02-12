package iut.unice.dreamteam.Equipments;

import iut.unice.dreamteam.Utils.Debug;
import iut.unice.dreamteam.Interfaces.IncomingPacketInterface;
import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Interfaces.Packet;
import iut.unice.dreamteam.Interfaces.WiredInterface;

import java.util.ArrayList;


public class Hub extends Equipment implements IncomingPacketInterface {
    private static final int INTERFACE_NUMBER = 10;
    private ArrayList<Interface> interfaces;

    public Hub(String name) {
        super(name);

        initialize(INTERFACE_NUMBER, WiredInterface.class, this, true);
        setIncomingPacketInterface(this);
    }

    @Override
    public void sendPacket(Packet packet) {
        super.sendPacket(packet);
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
        Debug.log("On the Hub !");

        for (Interface iface : getInterfaces()){
            if (!iface.getMacAddress().equals(i.getMacAddress()))
                iface.sendPacket(p);
        }
    }
}
