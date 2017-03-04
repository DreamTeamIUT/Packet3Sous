package iut.unice.dreamteam.Interfaces;

import iut.unice.dreamteam.NetworkLayers.IpLayer;
import iut.unice.dreamteam.NetworkLayers.MacLayer;
import iut.unice.dreamteam.NetworkLayers.ApplicationLayer;
import iut.unice.dreamteam.NetworkLayers.TransportLayer;

import java.util.UUID;


public class Packet {
    private String packetId;

    private MacLayer macLayer;
    private IpLayer ipLayer;
    private TransportLayer transportLayer;
    private ApplicationLayer applicationLayer;

    public Packet() {
        packetId = UUID.randomUUID().toString();
    }

    public MacLayer getMacLayer() {
        return macLayer;
    }

    public void setMacLayer(MacLayer macLayer) {
        this.macLayer = macLayer;
    }

    public IpLayer getIpLayer() {
        return ipLayer;
    }

    public void setIpLayer(IpLayer ipLayer) {
        this.ipLayer = ipLayer;
    }

    public TransportLayer getTransportLayer() {
        return transportLayer;
    }

    public void setTransportLayer(TransportLayer transportLayer) {
        this.transportLayer = transportLayer;
    }

    public ApplicationLayer getApplicationLayer() {
        return applicationLayer;
    }

    public void setApplicationLayer(ApplicationLayer applicationLayer) {
        this.applicationLayer = applicationLayer;
    }

    @Override
    public String toString() {
        return "-------- Packet " + getPacketId()
                + "\nMAC : " + ((getMacLayer() != null) ? ("source : " + getMacLayer().getSource() + ", destination : " + getMacLayer().getDestination()) : "empty")
                + "\nIP : source : " + getIpLayer().getSource() + ", destination :  " + getIpLayer().getDestination()
                + "\nTRANSPORT : " + ((getTransportLayer() != null) ? ("protocol : " + getTransportLayer().getTransportProtocol().getName() + ", source : " + getTransportLayer().getSourcePort() + ", destination : " + getTransportLayer().getDestinationPort()) : "empty")
                + "\nAPPLICATION : " + ((getApplicationLayer() != null) ? getApplicationLayer().getContent().toString() : "empty")
                + "\n--------";
    }

    public String getPacketId() {
        return packetId;
    }
}
