package iut.unice.dreamteam;

import iut.unice.dreamteam.Equipments.Equipment;
import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Interfaces.InterfaceLink;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Network {

    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";


    private ArrayList<Equipment> equipments;

    public Network() {
        equipments = new ArrayList<>();
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


    public void removeEquipment(Equipment equipment) {
        for (Interface i : equipment.getInterfaces()) {
            if (i.getLink() != null)
                i.getLink().brakeLink();
        }

        this.equipments.remove(equipment);
    }

    public ArrayList getEquipmentByType(Class aClass) {
        ArrayList<Equipment> sameType = new ArrayList<>();
        for (Equipment equipment : equipments) {
            if (equipment.getClass().equals(aClass)) {
                sameType.add(equipment);
            }
        }
        return sameType;
    }

    public static String getNaturalMask(String text) {
        String tab[] = text.split(Pattern.quote("."));
        int firstByte = Integer.parseInt(tab[0]);
        if (firstByte <= 127) {
            return "255.0.0.0";
        } else if (firstByte >= 128 && firstByte <= 191) {
            return "255.255.0.0";
        } else {
            return "255.255.255.0";
        }
    }
    public static Boolean isNetworkAddress(String ip, String mask) {
        if (isValidMask(mask) && isValidIpFormat(ip)) {
            String binIp = ipToBinary(ip);
            String binMask = ipToBinary(mask);

            if (binMask.contains("0"))
            {
                return !binIp.substring(binMask.indexOf("0")).contains("1");
            }
            return true;
        }
        return false;

    }
    public static boolean isValidMask(String mask) {
        if (isValidIpFormat(mask)) {
            String binIp = ipToBinary(mask);

            if (binIp.contains("0")) {
                String gauche = binIp.substring(0, binIp.indexOf("0"));
                String droite = binIp.substring(binIp.indexOf("0"), binIp.length());

                return !gauche.contains("0") && !droite.contains("1");
            }

            return true;

        }
        return false;
    }
    public static Boolean isValidIpFormat(String ip) {
        return !ip.equals("") && Pattern.compile(IPADDRESS_PATTERN).matcher(ip).matches();
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

    public static String getBroadcastAddress(String ip, String mask){
        String binip = ipToBinary(ip);
        String binMask = ipToBinary(mask);

        if (binMask.contains("0"))
        {
            String gauche =  binip.substring(0, binMask.indexOf("0"));
            String droite = binip.substring(binMask.indexOf("0")).replace("0", "1");
            return binaryToIp(gauche+droite);
        }

        return ip;
    }
    public static String getNeworkAddress(String ip, String mask){
        String binip = ipToBinary(ip);
        String binMask = ipToBinary(mask);

        if (binMask.contains("0"))
        {
            String gauche =  binip.substring(0, binMask.indexOf("0"));
            String droite = binip.substring(binMask.indexOf("0")).replace("1", "0");
            return binaryToIp(gauche+droite);
        }

        return ip;
    }

    public static String binaryToIp(String s) {
        if (s.length() == 32){
            String a = s.substring(0,8);
            String b = s.substring(8,16);
            String c = s.substring(16,24);
            String d = s.substring(24,32);

            int d1 = Integer.parseInt(a, 2);
            int d2 = Integer.parseInt(b, 2);
            int d3 = Integer.parseInt(c, 2);
            int d4 = Integer.parseInt(d, 2);

            return d1 +"." + d2 +"." + d3 +"." + d4;
        }
        return null;
    }
}
