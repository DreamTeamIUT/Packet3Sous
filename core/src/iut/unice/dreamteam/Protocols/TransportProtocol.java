package iut.unice.dreamteam.Protocols;


public class TransportProtocol {
    private String name;

    public TransportProtocol(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
