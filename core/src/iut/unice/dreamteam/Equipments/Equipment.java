package iut.unice.dreamteam.Equipments;

import iut.unice.dreamteam.Interfaces.IncomingPacketInterface;
import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Interfaces.Packet;
import iut.unice.dreamteam.Network;
import iut.unice.dreamteam.NetworkLayers.MacLayer;
import iut.unice.dreamteam.Protocols.ApplicationProtocol;
import iut.unice.dreamteam.Protocols.ApplicationProtocols;
import iut.unice.dreamteam.Protocols.TransportProtocol;
import iut.unice.dreamteam.Utils.Debug;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;


public abstract class Equipment {
    private String name;
    private Interface defaultGateway;
    private ArrayList<Interface> interfaces;
    private ArrayList<String> supportedProtocols;
    private HashMap<String, String> arpAssociation;

    private IncomingPacketInterface incomingPacketInterface;

    private Boolean multipleRoutes;

    private HashMap<Integer, TransportProtocol> usedPorts;

    public Equipment(String name) {
        this.name = name;

        this.interfaces = new ArrayList<>();

        this.supportedProtocols = new ArrayList<>();
        this.supportedProtocols.add("ARP");

        this.arpAssociation = new HashMap<>();

        this.multipleRoutes = false;

        usedPorts = new HashMap<>();
    }

    public Interface getInterface(int i) {
        return interfaces.get(i);
    }

    public Interface getInterface(String ip) {
        for (Interface i : interfaces) {
            if (Network.isInSameNetwork(i.getIp(), ip, i.getMask()))
                return i;
        }

        return getDefaultGateway();
    }

    public void removeInterface(Interface i){
        if (i == null)
            return;

        if (i.getLink() != null)
            i.getLink().brakeLink();

        interfaces.remove(i);
    }

    public void initialize(int size, Class<? extends Interface> p, Equipment equipment, Boolean passiveInterfaces) {
        for (int i = 0; i < size; i++) {
            try {
                Interface equipmentInterface = p.newInstance();
                equipmentInterface.setEquipment(equipment);
                equipmentInterface.setPassive(passiveInterfaces);
                equipmentInterface.setUp(true);

                if (defaultGateway == null)
                    defaultGateway = equipmentInterface;

                interfaces.add(equipmentInterface);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void initialize(int size, Class<? extends Interface> p, Equipment equipment) {
        initialize(size, p, equipment, false);
    }

    public ArrayList<Interface> getInterfaces() {
        return interfaces;
    }

    public void receivePacket(Interface i, Packet p) {
        System.out.println("--------");
        System.out.println(getName() + " : receive packet\n" + p);

        if (MacLayer.isBroadcastAddress(p.getMacLayer().getSource())) {
            System.out.println("broadcast packet");

            if (this.incomingPacketInterface != null)
                this.incomingPacketInterface.onBroadcast(i, p);
        } else {
            if (i.getMacAddress().equals(p.getMacLayer().getDestination())) {
                if (i.getIp().equals(p.getIpLayer().getDestination())) {
                    System.out.println("destined packet");

                    if (this.incomingPacketInterface != null)
                        this.incomingPacketInterface.onUnicast(i, p);
                } else {
                    System.out.println("forward packet");

                    if (this.incomingPacketInterface != null)
                        this.incomingPacketInterface.onForward(i, p);
                }
            } else {
                System.out.println("packet not for us.");

                if (this.incomingPacketInterface != null)
                    this.incomingPacketInterface.onReceive(i, p);
            }
        }
    }

    public void setIncomingPacketInterface(IncomingPacketInterface incomingPacketInterface) {
        this.incomingPacketInterface = incomingPacketInterface;
    }

    public void analyzeProtocol(Interface i, Packet p) {
        ApplicationProtocol applicationProtocol = ApplicationProtocols.getInstance().find(p.getApplicationLayer().getContent(), supportedProtocols);

        if (applicationProtocol != null) {
            Packet packet = applicationProtocol.processPacket(i, p);

            if (packet != null)
                sendPacket(packet);
        }
    }

    public void addProtocols(ArrayList<String> protocols) {
        this.supportedProtocols.addAll(protocols);
    }

    public String getMacArpAssociations(String ipAddress) {
        return this.arpAssociation.get(ipAddress);
    }

    public Boolean existArpAssociation(String ipAddress) {
        return this.arpAssociation.containsKey(ipAddress);
    }

    public void addArpAssociation(String ipAddress, String macAddress) {
        this.arpAssociation.put(ipAddress, macAddress);
    }

    public void sendPacket(Packet packet) {
        System.out.println("sending packet from eth" + interfaces.indexOf(getInterface(packet.getIpLayer().getDestination())) + " to " + packet.getIpLayer().getDestination());
        getInterface(packet.getIpLayer().getDestination()).sendPacket(packet);
    }

    public Interface getDefaultGateway() {
        return defaultGateway;
    }

    public void setDefaultGateway(Interface defaultGateway) {
        this.defaultGateway = defaultGateway;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void updateInterfaces() {
        for (Interface i : this.interfaces)
            i.launchQueue();
    }

    public Boolean hasMultipleRoutes() {
        return multipleRoutes;
    }

    public void setMultipleRoutes(Boolean multipleRoutes) {
        this.multipleRoutes = multipleRoutes;
    }

    public String gatewayByRoutes(Interface i) {
        return null;
    }

    public Boolean usedPort(int port) {
        return usedPorts.containsKey(port);
    }

    public Boolean newConnection(int port, TransportProtocol transportProtocol) {
        Debug.log("new connection");

        if (!usedPort(port)) {
            usedPorts.put(port, transportProtocol);

            return true;
        }

        return false;
    }

    public void applyTransportProtocol(Packet packet) {

    }

    public void clearInterfaces() {
        for (Interface i : interfaces) {
            if (i.getLink() != null)
                i.getLink().brakeLink();
        }
        interfaces.clear();
    }

    public boolean addInterface(Interface i) {
        return getInterfaces().add(i);
    }

    public static Equipment clone(Equipment e) {
        Equipment equipment;
        try {
            equipment = e.getClass().getDeclaredConstructor(String.class).newInstance(e.getName() + " cpy");


            equipment.getInterfaces().clear();

            for (Interface i : e.getInterfaces()) {
                Debug.log("Clone iface");
                Interface toAdd = Interface.clone(i);
                toAdd.setEquipment(equipment);
                equipment.addInterface(toAdd);

                if (e.getInterfaces().indexOf(e.getDefaultGateway()) == equipment.getInterfaces().size() - 1)
                {
                    Debug.log("Set Gateway !");
                    equipment.setDefaultGateway(toAdd);
                }
            }

            return equipment;

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e1) {
            e1.printStackTrace();
        }

        return null;
    }

    public static Equipment fromString(String name) {
        switch (name) {
            case "Router":
                return new Router("");
            case "Switch":
                return new Switch("");
            case "Hub":
                return new Hub("");
            case "Computer":
                return new Computer("");
            case "Access Point":
                return new AccessPoint("");
        }
        return null;
    }
}
