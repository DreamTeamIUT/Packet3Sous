package iut.unice.dreamteam.Interfaces;

import java.util.ArrayList;


public class PacketsManager {
    private ArrayList<PacketOnEquipment> packets;

    public PacketsManager() {
        this.packets = new ArrayList<>();
    }

    public ArrayList<PacketOnEquipment> getPackets() {
        return packets;
    }

    public void addPacket(Packet packet) {
        this.packets.add(new PacketOnEquipment(packet));
    }

    public PacketOnEquipment getPacket(String packetId) {
        for (PacketOnEquipment packetOnEquipment : packets) {
            if(packetOnEquipment.getPacket().getPacketId().equals(packetId))
                return packetOnEquipment;
        }

        return null;
    }

    public Boolean existPacket(String packetId) {
        for (PacketOnEquipment packetOnEquipment : packets) {
            if(packetOnEquipment.getPacket().getPacketId().equals(packetId))
                return true;
        }

        return false;
    }

    public Boolean everythingSent() {
        for (PacketOnEquipment packetOnEquipment : packets) {
            if(!packetOnEquipment.getPacketProperties().isSent() && !packetOnEquipment.getPacketProperties().isWaiting())
                return false;
        }

        return true;
    }

    public void clearSentPackets() {
        ArrayList<PacketOnEquipment> temporaryPackets = new ArrayList<>();

        for (PacketOnEquipment packetOnEquipment : packets) {
            if(packetOnEquipment.getPacketProperties().isSent())
                temporaryPackets.add(packetOnEquipment);
        }

        for (PacketOnEquipment packetOnEquipment : temporaryPackets)
            packets.remove(packetOnEquipment);
    }
}
