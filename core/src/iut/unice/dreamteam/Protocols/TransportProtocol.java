package iut.unice.dreamteam.Protocols;


import iut.unice.dreamteam.Equipments.Equipment;
import iut.unice.dreamteam.Utils.Debug;

public class TransportProtocol {
    private String name;
    private int port;

    public TransportProtocol(String name) {
        setName(name);
        setPort(-1);
    }

    public static Boolean isTCP(TransportProtocol transportProtocol) {
        return transportProtocol.getName().equals("TCP");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static Boolean validPort(int port) {
        return port > 0 && port < 65535;
    }

    public static int getRandomPort(Equipment equipment) {
        int port = 1;

        while (equipment.usedPort(port))
            port++;


        if (validPort(port))
            return port;

        return -1;
    }

    @Override
    public String toString() {
        return getName();
    }
}
