package iut.unice.dreamteam;

import iut.unice.dreamteam.Equipments.Equipment;
import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Interfaces.InterfaceLink;
import iut.unice.dreamteam.Interfaces.Packet;

import java.net.InetAddress;
import java.util.ArrayList;


public class Network {

    private ArrayList<Equipment> equipments;

    public Network() {
        equipments = new ArrayList<>();
    }

    public static Boolean isInSameNetwork(String ipSource, String ipDest, String mask) {

        String binarySource = ipToBinary(ipSource);
        String binaryDest = ipToBinary(ipDest);
        String binaryMask = ipToBinary(mask);

        int maskInt = binaryMask.contains("0") ? (binaryMask.length() - (binaryMask.length() - binaryMask.indexOf("0"))) : binaryMask.length();

        return binarySource.substring(0, maskInt).equals(binaryDest.substring(0, maskInt));
    }

    public static String ipToBinary(String ipAddress) {
        try {
            InetAddress ip = InetAddress.getByName(ipAddress);


            byte[] bytes = ip.getAddress();
            String addressBytes = "";
            for (byte b : bytes) {
                String part = Integer.toBinaryString(b & 0xFF);
                while (part.length() < 8) {
                    part = "0" + part;
                }

                addressBytes += part;
            }

            return addressBytes;
        } catch (Exception ignored) {
        }
        return null;
    }

    public static void linkInterfaces(Interface a, Interface b) {
        InterfaceLink link = new InterfaceLink(a, b);
        a.setLink(link);
        b.setLink(link);
    }

    public void addEquipment(Equipment e) {
        equipments.add(e);
    }

    public void updateEquipments() {
        for (Equipment equipment : this.equipments)
            equipment.updateInterfaces();
    }
}