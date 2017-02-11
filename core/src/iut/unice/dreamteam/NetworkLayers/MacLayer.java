package iut.unice.dreamteam.NetworkLayers;

import iut.unice.dreamteam.NetworkLayers.GenericLayer;


public class MacLayer extends GenericLayer {
    private static final String MAC_BROADCAST = "FF:FF:FF:FF:FF";

    private String source;
    private String destination;
    private int lenght;

    public MacLayer(String macAddress, String destination) {
        super();

        this.source = macAddress;
        this.destination = destination;
    }


    public void setToBroadCast(){
        destination = MAC_BROADCAST;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getLenght() {
        return lenght;
    }

    public static Boolean isBroadcastAddress(String macAddress) {
        return macAddress.equals(MAC_BROADCAST);
    }

    public static String getBroadcastAddress() {
        return MAC_BROADCAST;
    }
}
