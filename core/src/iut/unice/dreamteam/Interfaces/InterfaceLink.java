package iut.unice.dreamteam.Interfaces;


public class InterfaceLink {
    private Interface a;
    private Interface b;

    public InterfaceLink(Interface interfaceA, Interface interfaceB) {
        this.a = interfaceA;
        this.b = interfaceB;
    }

    public void sendPacket(Packet p) {
        if (p.getMacLayer().getDestination().equals(a.getMacAddress())){
            a.receivePacket(p);
        }
        else {
            b.receivePacket(p);
        }
    }

    public String getDestination(Interface anInterface) {
        if (anInterface.getMacAddress().equals(a.getMacAddress())){
            return b.getMacAddress();
        }
        else {
            return a.getMacAddress();
        }
    }
}
