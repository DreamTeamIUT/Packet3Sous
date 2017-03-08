package iut.unice.dreamteam.Interfaces;


public class PacketPropertiesOnEquipment {
    private String waitingId;

    private Boolean sent;
    private Boolean justCreated;
    private Boolean displayed;

    public PacketPropertiesOnEquipment() {
        this.sent = false;
        this.displayed = false;

        this.justCreated = true;
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

    public Boolean isDisplayed() {
        return displayed;
    }

    public void setDisplayed() {
        this.displayed = true;
    }

    public Boolean isJustCreated() {
        return justCreated;
    }

    public void setNotJustCreated() {
        this.justCreated = false;
    }
}
