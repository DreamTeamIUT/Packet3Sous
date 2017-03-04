package iut.unice.dreamteam.Protocols;


import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Interfaces.Packet;
import org.json.JSONObject;


public abstract class ApplicationProtocol {
    private String name;
    private TransportProtocol transportProtocol;

    public ApplicationProtocol(String name) {
        setName(name);
        setUDP();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract Packet initiate(Interface i, JSONObject parameters);

    public abstract Packet processPacket(Interface i, Packet p);

    public Boolean isProtocol(Packet p) {
        return ((JSONObject)p.getApplicationLayer().getContent().get("protocol")).getString("name").equals(getName());
    }

    public TransportProtocol getTransportProtocol() {
        return transportProtocol;
    }

    public void setTransportProtocol(TransportProtocol transportProtocol) {
        this.transportProtocol = transportProtocol;
    }

    public void setTCP(int defaultPort) {
        setTransportProtocol(new TCP(defaultPort));
    }

    public void setUDP() {
        setTransportProtocol(new UDP());
    }

    public void setUDP(int defaultPort) {
        setTransportProtocol(new UDP(defaultPort));
    }
}
