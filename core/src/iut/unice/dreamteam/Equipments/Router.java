package iut.unice.dreamteam.Equipments;

import iut.unice.dreamteam.Interfaces.*;
import iut.unice.dreamteam.Network;
import iut.unice.dreamteam.NetworkLayers.MacLayer;
import iut.unice.dreamteam.Utils.Debug;

import java.util.ArrayList;
import java.util.HashMap;


public class Router extends Equipment implements IncomingPacketInterface {
    private ArrayList<Route> routes;

    public Router(String name) {
        super(name);

        routes = new ArrayList<>();

        initialize(2, WiredInterface.class, this);
        setIncomingPacketInterface(this);
        setMultipleRoutes(true);
    }

    @Override
    public void onBroadcast(Interface i, Packet p) {

    }

    @Override
    public void onUnicast(Interface i, Packet p) {

    }

    @Override
    public void onForward(Interface i, Packet p) {
        Debug.log("onForward router");

        if (knowDestination(p)) {
            Debug.log("know network");

            Packet packet = new Packet();
            packet.setApplicationLayer(p.getApplicationLayer());
            packet.setTransportLayer(p.getTransportLayer());
            packet.setIpLayer(p.getIpLayer());

            sendPacket(packet);
        }
        else
            Debug.log("can't find destination");
    }

    @Override
    public void sendPacket(Packet packet) {
        getInterface(packet.getIpLayer().getDestination()).sendPacket(packet);
    }

    @Override
    public Interface getInterface(String ip) {
        if(directlyConnected(ip))
            return super.getInterface(ip);
        else {
            for (Route route : routes) {
                if(Network.isInSameNetwork(route.getNetwork(), ip, route.getMask()))
                    return super.getInterface(route.getNextHop());
            }

            return null;
        }
    }

    @Override
    public void onReceive(Interface i, Packet p) {
        Debug.log("onReceive router");
    }

    public Boolean knowDestination(Packet packet) {
        for (Interface i : getInterfaces()) {
            if(Network.isInSameNetwork(i.getIp(), packet.getIpLayer().getDestination(), i.getMask()))
                return true;
        }

        for (Route route : routes) {
            if(Network.isInSameNetwork(route.getNetwork(), packet.getIpLayer().getDestination(), route.getMask()))
                return true;
        }

        return false;
    }

    public Boolean directlyConnected(String ipDestination) {
        for (Interface i : getInterfaces()) {
            if(Network.isInSameNetwork(i.getIp(), ipDestination, i.getMask()))
                return true;
        }

        return false;
    }

    public void addRoute(String network, String mask, String nextHop) {
        routes.add(new Route(network, mask, nextHop));
    }

    public void deleteRoute(String network) {
        ArrayList<Route> routesRemove = new ArrayList<>();

        for (Route route : routes) {
            if(route.getNetwork().equals(network))
                routesRemove.add(route);
        }

        for (Route route : routesRemove)
            routes.remove(route);
    }

    @Override
    public String gatewayByRoutes(Interface i) {
        for (Route route : routes) {
            if(Network.isInSameNetwork(route.getNextHop(), i.getIp(), route.getMask()))
                return route.getNextHop();
        }

        return null;
    }
}
