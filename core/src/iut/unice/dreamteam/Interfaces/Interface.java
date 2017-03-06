package iut.unice.dreamteam.Interfaces;

import iut.unice.dreamteam.Equipments.Equipment;
import iut.unice.dreamteam.Network;
import iut.unice.dreamteam.NetworkLayers.MacLayer;
import iut.unice.dreamteam.Functionalities.Protocols.ARP;
import iut.unice.dreamteam.Utils.Debug;
import org.json.JSONObject;

import java.util.Random;


public class Interface {
    public static final String INTERFACE_TYPE_WIRED = "Wired";
    public static final String INTERFACE_TYPE_WIRELESS = "Wireless";
    private String ip;
    private String mask;
    private String gateway;

    private String macAddress;

    private float speed;
    private Boolean state;

    private InterfaceLink link;
    private Equipment equipment;
    private PacketsManager packetsManager;

    private ARP arp;
    private Boolean passive;

    public Interface() {
        this.macAddress = this.randomMACAddress();

        this.state = false;

        this.packetsManager = new PacketsManager();

        this.arp = new ARP();
        this.passive = false;
    }

    public Boolean isPassive() {
        return this.passive;
    }

    public void setPassive(Boolean passive){
        this.passive = passive;
    }

    private String randomMACAddress() {
        Random rand = new Random();
        byte[] macAddr = new byte[6];
        rand.nextBytes(macAddr);

        macAddr[0] = (byte) (macAddr[0] & (byte) 254);  //zeroing last 2 bytes to make it unicast and locally adminstrated

        StringBuilder sb = new StringBuilder(18);
        for (byte b : macAddr) {

            if (sb.length() > 0)
                sb.append(":");

            sb.append(String.format("%02x", b));
        }


        return sb.toString();
    }

    public Boolean isUp() {
        return state;
    }

    public void setUp(Boolean state) {
        this.state = state;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Boolean isConnected() {
        return this.link != null;
    }

    public void setLink(InterfaceLink link) {
        this.link = link;
    }

    public void removeLink() {
        this.link.brakeLink();
    }

    public InterfaceLink getLink() {
        return link;
    }

    public void receivePacket(Packet p) {
        Debug.equipment(getEquipment(), "receive packet " + p.getPacketId());

        if(arp.isProtocol(p) && !isPassive()) {
            Packet packet = arp.processPacket(this, p);

            if(packet != null)
                sendPacket(packet);
        }
        else
            this.equipment.receivePacket(this, p);
    }

    public void sendPacket(Packet packet) {
        Debug.equipment(getEquipment(), "new packet in queue");
        Debug.packet(packet);

        getPacketsManager().addPacket(packet);

        if (packet.getMacLayer() == null) {
            if(Network.isInSameNetwork(getIp(), packet.getIpLayer().getDestination(), getMask())) {
                if(getEquipment().existArpAssociation(packet.getIpLayer().getDestination())) {
                    packet.setMacLayer(new MacLayer(getMacAddress(), getEquipment().getMacArpAssociations(packet.getIpLayer().getDestination())));
                }
                else {
                    Packet arpPacket = arp.initiate(this, new JSONObject().put("ip-address", packet.getIpLayer().getDestination()));

                    if(arpPacket != null) {
                        Debug.equipment(getEquipment(), "new packet in queue");
                        Debug.packet(arpPacket);

                        getPacketsManager().addPacket(arpPacket);
                        getPacketsManager().getPacket(packet.getPacketId()).getPacketProperties().setWaitingId(arpPacket.getPacketId());
                    }
                }
            }
            else {
                if(getEquipment().existArpAssociation(getGateway())) {
                    packet.setMacLayer(new MacLayer(getMacAddress(), getEquipment().getMacArpAssociations(getGateway())));
                }
                else {
                    Packet arpPacket = arp.initiate(this, new JSONObject().put("ip-address", getGateway()));

                    if(arpPacket != null) {
                        Debug.equipment(getEquipment(), "new packet in queue");
                        Debug.packet(arpPacket);

                        getPacketsManager().addPacket(arpPacket);
                        getPacketsManager().getPacket(packet.getPacketId()).getPacketProperties().setWaitingId(arpPacket.getPacketId());
                    }
                }
            }
        }

        //launchQueue();
    }

    /*
    public void launchQueue() {
        Debug.equipment(getEquipment(), "launch queue " + launchedQueue);

        if(!launchedQueue) {
            launchedQueue = true;

            //getPacketsManager().clearSentPackets();

            for (PacketOnEquipment packetOnEquipment : packetsManager.getPackets()) {
                Debug.equipment(getEquipment(), "queuing");
                Debug.packet(packetOnEquipment.getPacket());

                if (!packetOnEquipment.getPacketProperties().isSent() && !packetOnEquipment.getPacketProperties().isWaiting()) {
                    Debug.equipment(getEquipment(), "send packet " + packetOnEquipment.getPacket().getPacketId());

                    //TIMEOUT
                    link.sendPacket(packetOnEquipment.getPacket());

                    packetOnEquipment.getPacketProperties().setSent();

                    Debug.equipment(getEquipment(), "everything sent " + getPacketsManager().everythingSent());

                    if(!getPacketsManager().everythingSent())
                        launchQueue();
                    else
                        launchedQueue = false;
                    //END TIMEOUT

                    return;
                }
            }
        }
    }
    */

    public void launchQueue() {
        for (PacketOnEquipment packetOnEquipment : packetsManager.getPackets()) {
            //Debug.equipment(getEquipment(), "queuing");
            //Debug.log(packetOnEquipment.getPacket().getApplicationLayer().getContent() + " " + packetOnEquipment.getPacketProperties().isSent() + " " + packetOnEquipment.getPacketProperties().isWaiting());

            if (!packetOnEquipment.getPacketProperties().isSent() && !packetOnEquipment.getPacketProperties().isWaiting()) {
                Debug.equipment(getEquipment(), "send packet " + packetOnEquipment.getPacket().getPacketId());

                packetOnEquipment.getPacketProperties().setSent();

                if(isUp() && isConnected())
                    link.sendPacket(this, packetOnEquipment.getPacket());

                return;
            }
        }
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Equipment getEquipment() {
        return this.equipment;
    }

    public PacketsManager getPacketsManager() {
        return packetsManager;
    }

    public String getGateway() {
        if(getEquipment().hasMultipleRoutes())
            return getEquipment().gatewayByRoutes(this);

        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public static Interface clone(Interface i){
        Interface newInterface = new Interface();

        newInterface.setPassive(i.isPassive());
        newInterface.setIp(i.getIp());
        newInterface.setMask(i.getMask());
        newInterface.setGateway(i.getGateway());

        return newInterface;
    }
}
