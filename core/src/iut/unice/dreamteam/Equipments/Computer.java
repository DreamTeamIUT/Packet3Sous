package iut.unice.dreamteam.Equipments;

import iut.unice.dreamteam.Interfaces.IncomingPacketInterface;
import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Interfaces.Packet;
import iut.unice.dreamteam.Interfaces.WiredInterface;
import iut.unice.dreamteam.NetworkLayers.MacLayer;

import java.util.ArrayList;
import java.util.Arrays;



public class Computer extends Equipment implements IncomingPacketInterface {
    public Computer(String name) {
        super(name);

        initialize(1, WiredInterface.class, this);
        setIncomingPacketInterface(this);

        addProtocols(new ArrayList<>(Arrays.asList("FTP", "HTTP")));
    }

    @Override
    public void onBroadcast(Interface i, Packet p) {
        analyzeProtocol(i, p);
    }

    @Override
    public void onUnicast(Interface i, Packet p) {
        analyzeProtocol(i, p);
    }

    @Override
    public void onForward(Interface i, Packet p) {

    }

    @Override
    public void onReceive(Interface i, Packet p) {

    }
}
