package iut.unice.dreamteam.Interfaces;


public class InterfaceLink {
    private Interface a;
    private Interface b;

    public InterfaceLink(Interface interfaceA, Interface interfaceB) {
        this.a = interfaceA;
        this.b = interfaceB;
    }

    public void sendPacket(Interface i, Packet p) {
        if (i.getMacAddress().equals(a.getMacAddress())){
            b.receivePacket(p);
        }
        else {
            a.receivePacket(p);
        }
    }
}
