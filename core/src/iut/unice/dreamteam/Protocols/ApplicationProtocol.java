package iut.unice.dreamteam.Protocols;

import iut.unice.dreamteam.Equipments.Equipment;
import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Interfaces.Packet;
import org.json.JSONObject;


public abstract class ApplicationProtocol {
    private String name;

    private Boolean waitForAnswer;

    public ApplicationProtocol(String name) {
        setName(name);
        setWaitForAnswer(false);
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

    public Boolean isWaitForAnswer() {
        return waitForAnswer;
    }

    public void setWaitForAnswer(Boolean waitForAnswer) {
        this.waitForAnswer = waitForAnswer;
    }
}
