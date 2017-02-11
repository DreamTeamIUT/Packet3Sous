package iut.unice.dreamteam.Equipments;

import iut.unice.dreamteam.Interfaces.IncomingPacketInterface;
import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Interfaces.Packet;
import iut.unice.dreamteam.Interfaces.WiredInterface;
import iut.unice.dreamteam.Protocols.ApplicationProtocol;

import java.util.ArrayList;


public class Switch extends Equipment implements IncomingPacketInterface {
    private ArrayList<String> equipments;

    public Switch(String name) {
        super(name);

        initialize(24, WiredInterface.class, this);
        setIncomingPacketInterface(this);
    }

    public void getEquipmentTable() {
        for (Interface i : getInterfaces()) {
            String macAddress = "";

            if(!equipments.contains(macAddress))
                equipments.add(macAddress);
        }
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

    }
}
