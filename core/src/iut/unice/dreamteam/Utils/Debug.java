package iut.unice.dreamteam.Utils;

import iut.unice.dreamteam.Equipments.Equipment;
import iut.unice.dreamteam.Interfaces.Packet;

/**
 * Created by Guillaume on 11/02/2017.
 */
public class Debug {
    public static void equipment(Equipment equipment, String message) {
        System.out.println("EQUIPMENT " + equipment.getName() + " : " + message);
    }

    public static void packet(Packet packet) {
        System.out.println(packet);
    }

    public static void protocol(String protocol, String message) {
        System.out.println("PROTOCOL " + protocol + " : " + message);
    }

    public static void protocol(String protocol, String message, Equipment equipment) {
        Debug.equipment(equipment, "PROTOCOL " + protocol + " : " + message);
    }

    public static void log(String message) {
        System.out.println(message);
    }
}
