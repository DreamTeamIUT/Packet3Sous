package iut.unice.dreamteam.Equipments;

import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Interfaces.Packet;


public class AccessPoint extends Equipment {
    public AccessPoint(String name) {
        super(name);
    }

    @Override
    public void receivePacket(Interface i, Packet p) {

    }
}
