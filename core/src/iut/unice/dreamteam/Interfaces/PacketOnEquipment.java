package iut.unice.dreamteam.Interfaces;


public class PacketOnEquipment {
    private Packet packet;
    private PacketPropertiesOnEquipment packetPropertiesOnEquipment;

    public PacketOnEquipment(Packet packet)
    {
        this.packetPropertiesOnEquipment = new PacketPropertiesOnEquipment();
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }

    public PacketPropertiesOnEquipment getPacketProperties() {
        return packetPropertiesOnEquipment;
    }
}
