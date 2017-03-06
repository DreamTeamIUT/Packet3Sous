package iut.unice.dreamteam.Functionalities.Protocols;


public class UDP extends TransportProtocol {
    public UDP() {
        super("UDP");
    }

    public UDP(int port) {
        super("UDP");

        setPort(port);
    }
}
