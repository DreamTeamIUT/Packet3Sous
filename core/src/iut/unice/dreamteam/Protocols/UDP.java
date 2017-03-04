package iut.unice.dreamteam.Protocols;


import iut.unice.dreamteam.Equipments.Equipment;

import java.util.Map;

public class UDP extends TransportProtocol {
    public UDP() {
        super("UDP");
    }

    public UDP(int port) {
        super("UDP");

        setPort(port);
    }
}
