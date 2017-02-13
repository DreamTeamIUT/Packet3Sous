package iut.unice.dreamteam.Protocols;


import iut.unice.dreamteam.Equipments.Equipment;

import java.util.Map;

public class UDP extends TransportProtocol {
    public UDP() {
        super("UDP");
    }

    public static int getRandomPort(Equipment equipment) {
        int port = 1;

        while (equipment.usedPort(port))
            port++;

        if(port <= 65535)
            return port;

        return -1;
    }
}
