package iut.unice.dreamteam.Interfaces;


public interface IncomingPacketInterface {
    void onBroadcast(Interface i, Packet p);
    void onUnicast(Interface i, Packet p);
    void onForward(Interface i, Packet p);
    void onReceive(Interface i, Packet p);
}
