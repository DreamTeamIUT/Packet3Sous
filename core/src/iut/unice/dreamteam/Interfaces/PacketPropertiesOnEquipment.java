package iut.unice.dreamteam.Interfaces;


public class PacketPropertiesOnEquipment {
    private String waitingId;

    private Boolean sent;

    public PacketPropertiesOnEquipment() {
        this.sent = false;
    }

    public String getWaitingId() {
        return waitingId;
    }

    public void setWaitingId(String waitingId) {
        this.waitingId = waitingId;
    }

    public Boolean isWaiting() {
        return waitingId != null;
    }

    public Boolean isSent() {
        return this.sent;
    }

    public void setSent() {
        this.sent = true;
    }
}
