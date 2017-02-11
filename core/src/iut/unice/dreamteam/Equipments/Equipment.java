package iut.unice.dreamteam.Equipments;

import iut.unice.dreamteam.Interfaces.IncomingPacketInterface;
import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Network;
import iut.unice.dreamteam.Interfaces.Packet;
import iut.unice.dreamteam.NetworkLayers.MacLayer;
import iut.unice.dreamteam.Protocols.ApplicationProtocol;
import iut.unice.dreamteam.Protocols.ApplicationProtocols;

import java.util.ArrayList;
import java.util.HashMap;


public abstract class Equipment {
    private String name;
    private String gateway;
    private ArrayList<Interface> interfaces;
    private ArrayList<String> supportedProtocols;
    private HashMap<String, String> arpAssociation;

    private IncomingPacketInterface incomingPacketInterface;

    public Equipment(String name){
        this.name = name;

        this.interfaces = new ArrayList<>();

        this.supportedProtocols = new ArrayList<>();
        this.supportedProtocols.add("ARP");

        this.arpAssociation = new HashMap<>();
    }

    public Interface getInterface(int i){
        return interfaces.get(i);
    }

    public Interface getInterface(String ip) {
        for (Interface i: interfaces){
            if (Network.isInSameNetwork(i.getIp(), ip, i.getMask()))
                return i;
        }
        return getInterface(getGateway());
    }

    public void initialize(int size, Class< ? extends Interface> p, Equipment equipment, Boolean passiveInterfaces){
        for (int i=0;i<size;i++){
            try {
                Interface equipmentInterface = p.newInstance();
                equipmentInterface.setEquipment(equipment);
                equipmentInterface.setPassive(passiveInterfaces);
                equipmentInterface.setUp(true);

                interfaces.add(equipmentInterface);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void initialize(int size, Class< ? extends Interface> p, Equipment equipment) {
        initialize(size, p, equipment, false);
    }

    protected ArrayList<Interface> getInterfaces() {
        return interfaces;
    }

    public void receivePacket(Interface i, Packet p) {
        System.out.println("--------");
        System.out.println(getName() + " : receive packet\n" + p);

        if (MacLayer.isBroadcastAddress(p.getMacLayer().getSource())) {
            System.out.println("broadcast packet");

            if(this.incomingPacketInterface != null)
                this.incomingPacketInterface.onBroadcast(i, p);
        }
        else {
            if (i.getMacAddress().equals(p.getMacLayer().getDestination())) {
                if (i.getIp().equals(p.getIpLayer().getDestination())) {
                    System.out.println("destined packet");

                    if(this.incomingPacketInterface != null)
                        this.incomingPacketInterface.onUnicast(i, p);
                }
                else {
                    System.out.println("forward packet");

                    if(this.incomingPacketInterface != null)
                        this.incomingPacketInterface.onForward(i, p);
                }
            }
            else {
                System.out.println("packet not for us.");

                if(this.incomingPacketInterface != null)
                    this.incomingPacketInterface.onReceive(i, p);
            }
        }
    }

    public void setIncomingPacketInterface(IncomingPacketInterface incomingPacketInterface) {
        this.incomingPacketInterface = incomingPacketInterface;
    }

    public void analyzeProtocol(Interface i, Packet p) {
        ApplicationProtocol applicationProtocol = ApplicationProtocols.getInstance().find(p.getApplicationLayer().getContent(), supportedProtocols);

        if(applicationProtocol != null) {
            Packet packet = applicationProtocol.processPacket(i, p);

            if(packet != null)
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
        //if(!isSendingPacket()) {

            //TIMEOUT
            System.out.println("sending packet from eth" + interfaces.indexOf(getInterface(packet.getIpLayer().getDestination())) + " to " + packet.getIpLayer().getDestination());
            getInterface(packet.getIpLayer().getDestination()).sendPacket(packet);
            //packets.remove(packet);

            //if(packets.size() > 0)
                //sendPacket(packets.get(0));
            //END TIMEOUT
        //}
        //else
            //packets.add(packet);
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
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
}
