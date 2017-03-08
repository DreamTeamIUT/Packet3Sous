package iut.unice.dreamteam.Functionalities.Protocols;

import iut.unice.dreamteam.Equipments.Equipment;
import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Interfaces.Packet;
import iut.unice.dreamteam.NetworkLayers.IpLayer;
import iut.unice.dreamteam.NetworkLayers.MacLayer;
import iut.unice.dreamteam.NetworkLayers.TransportLayer;
import iut.unice.dreamteam.Utils.Debug;
import org.json.JSONObject;

public class ApplicationService {
    private String name;
    private ApplicationProtocol applicationProtocol;

    private Boolean started;
    private Boolean isServer;

    private int usedPort;

    private String lastPacketId;
    private Boolean waiting;

    public ApplicationService(Equipment equipment, ApplicationProtocol applicationProtocol, Boolean isServer, int wantedPort) {
        Debug.log("EQUIPMENT " + equipment.getName() + " request start service for protocol " + applicationProtocol.getName() + " as " + (isServer ? "server" : "client") + (TransportProtocol.validPort(wantedPort) ? (" on port " + wantedPort) : ""));
        setName(applicationProtocol.getName().toLowerCase() + "-" + (isServer ? "server" : "client"));
        setApplicationProtocol(applicationProtocol);

        applicationProtocol.setUsedAsServer(isServer);

        this.isServer = isServer;
        this.usedPort = (isServer && TransportProtocol.validPort(wantedPort)) ? wantedPort : getNewPort(equipment);

        this.lastPacketId = null;
        this.waiting = false;

        if (!equipment.usedPort(this.usedPort))
            this.started = true;

        Debug.log("EQUIPMENT " + equipment.getName() + " SERVICE " + getName() + " for protocol " + applicationProtocol.getName() + " " + (this.started ? "started" : "failed start") + " on port " + this.usedPort);
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public ApplicationProtocol getApplicationProtocol() {
        return applicationProtocol;
    }

    public void setApplicationProtocol(ApplicationProtocol applicationProtocol) {
        this.applicationProtocol = applicationProtocol;
    }

    public Boolean isServer() {
        return isServer;
    }

    public Integer getUsedPort() {
        return usedPort;
    }

    public Integer getNewPort(Equipment equipment) {
        return (TransportProtocol.validPort(applicationProtocol.getTransportProtocol().getPort()) && isServer) ? applicationProtocol.getTransportProtocol().getPort() : TransportProtocol.getRandomPort(equipment);
    }

    public static ApplicationService start(Equipment equipment, ApplicationProtocol applicationProtocol, Boolean isServer, int wantedPort) {
        return new ApplicationService(equipment, applicationProtocol, isServer, wantedPort);
    }

    public static ApplicationService start(Equipment equipment, ApplicationProtocol applicationProtocol, Boolean isServer) {
        return start(equipment, applicationProtocol, isServer, -1);
    }

    public static ApplicationService start(Equipment equipment, ApplicationProtocol applicationProtocol) {
        return start(equipment, applicationProtocol, false, -1);
    }

    public void stop() {
        started = false;
    }

    public void initiateProtocol(Equipment equipment, Interface i, JSONObject jsonObject, int wantedDestinationPort) {
        Packet packet = getApplicationProtocol().initiate(i, jsonObject);
        packet.setTransportLayer(new TransportLayer(getApplicationProtocol().getTransportProtocol(), usedPort, (TransportProtocol.validPort(wantedDestinationPort) ? wantedDestinationPort : getApplicationProtocol().getTransportProtocol().getPort())));

        equipment.sendPacket(packet);
        Debug.log("yessssssss");
        equipment.updateInterface();
    }

    public void initiateProtocol(Equipment equipment, Interface i, JSONObject jsonObject) {
        initiateProtocol(equipment, i, jsonObject, -1);
    }

    public void newPacket(Equipment equipment, Interface i, Packet packet) {
        if(!this.started) {
            Debug.log("EQUIPMENT " + equipment.getName() + " SERVICE " + getName() + " not started");

            return;
        }

        if (TCP.isConnectionPacket(packet)) {
            Debug.log("EQUIPMENT " + equipment.getName() + " SERVICE " + getName() + " TCP CONNECTION");

            return;
        }

        if(TCP.isTransmissionPacket(packet)) {
            Debug.log("EQUIPMENT " + equipment.getName() + " SERVICE " + getName() + " RETURN ACK");

            if(packet.getApplicationLayer().getContent().getString("packet-id").equals(this.lastPacketId))
                this.waiting = false;
        }

        if(TCP.isProtocol(packet)) {
            equipment.sendPacket(applyTransportLayer(TCP.ackPacket(packet), packet.getTransportLayer().getSourcePort()));

            if(this.waiting)
                return;
        }

        Packet generatedPacket = applyTransportLayer(getApplicationProtocol().processPacket(i, packet), packet.getTransportLayer().getSourcePort());

        if(generatedPacket != null) {
            this.lastPacketId = generatedPacket.getPacketId();
            this.waiting = true;

            equipment.sendPacket(generatedPacket);
        }
    }

    public Packet applyTransportLayer(Packet p, int sourcePort) {
        if(p != null)
            p.setTransportLayer(new TransportLayer(getApplicationProtocol().getTransportProtocol(), this.usedPort, sourcePort));

        return p;
    }

    public static Packet getReturnPacket(Packet p) {
        Packet generatedPacket = new Packet();
        generatedPacket.setMacLayer(new MacLayer(p.getMacLayer().getDestination(), p.getMacLayer().getSource()));
        generatedPacket.setIpLayer(new IpLayer(p.getIpLayer().getDestination(), p.getIpLayer().getDestination()));

        return generatedPacket;
    }

    public Boolean isStarted() {
        return started;
    }
}
