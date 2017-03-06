package iut.unice.dreamteam.UI.Adapaters;

import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Interfaces.WiredInterface;
import iut.unice.dreamteam.Interfaces.WirelessInterface;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class TableInterface {
    private Interface iface;
    private SimpleStringProperty ip;
    private SimpleStringProperty mask;
    private SimpleStringProperty name;
    private SimpleStringProperty type;
    private SimpleBooleanProperty passive;

    public TableInterface() {
    }

    public TableInterface(Interface i, int index){
        this.ip = new SimpleStringProperty(i.getIp());
        this.mask = new SimpleStringProperty(i.getMask());
        this.name = new SimpleStringProperty("eth" + index);
        this.type = new SimpleStringProperty((i instanceof WiredInterface)?Interface.INTERFACE_TYPE_WIRED : Interface.INTERFACE_TYPE_WIRELESS);
        this.passive = new SimpleBooleanProperty(i.isPassive());

        this.iface = i;
    }

    public TableInterface(String ip, String mask, String type) {
        this.ip = new SimpleStringProperty(ip);
        this.mask = new SimpleStringProperty(mask);
        this.name = new SimpleStringProperty("eth");
        this.type = new SimpleStringProperty(Interface.INTERFACE_TYPE_WIRED);
    }

    public TableInterface(String text, String text1, String value, String text2, boolean selected) {
        this.ip = new SimpleStringProperty(text);
        this.mask = new SimpleStringProperty(text1);
        this.name = new SimpleStringProperty(text2);
        this.type = new SimpleStringProperty(value);
        this.passive = new SimpleBooleanProperty(selected);
    }

    public String getIp() {
        return ip.get();
    }

    public void setIp(String s) {
        ip.set(s);
        iface.setIp(s);
    }

    public String getMask() {
        return mask.get();
    }

    public void setMask(String s) {
        mask.set(s);
        iface.setMask(s);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    private static Interface getInterface(String type) {
        Interface iface;
        switch (type) {
            case Interface.INTERFACE_TYPE_WIRELESS:
                iface = new WirelessInterface();
                break;
            case Interface.INTERFACE_TYPE_WIRED:
            default:
                iface = new WiredInterface();
        }

        return iface;
    }

    public boolean isPassive() {
        return passive.get();
    }

    public void setPassive(boolean passive) {
        this.passive.set(passive);
        iface.setPassive(passive);
    }

    public Interface getInterface() {
        if (iface != null)
            return iface;

        this.iface = getInterface(this.getType());

        iface.setMask(getMask());
        iface.setIp(getIp());
        iface.setPassive(isPassive());
        iface.setUp(true);

        return iface;
    }
}
