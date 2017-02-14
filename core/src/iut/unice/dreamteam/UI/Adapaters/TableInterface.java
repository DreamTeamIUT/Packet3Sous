package iut.unice.dreamteam.UI.Adapaters;

import iut.unice.dreamteam.Interfaces.Interface;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Guillaume on 13/02/2017.
 */
public class TableInterface {
    private SimpleStringProperty ip;
    private SimpleStringProperty mask;
    private SimpleStringProperty name;
    private SimpleStringProperty type;
    private SimpleBooleanProperty passive;

    public TableInterface() {
    }

    public TableInterface(String ip, String mask, String type) {
        this.ip = new SimpleStringProperty(ip);
        this.mask = new SimpleStringProperty(mask);
        this.name = new SimpleStringProperty("eth");
        this.type = new SimpleStringProperty("wired");
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
    }

    public String getMask() {
        return mask.get();
    }

    public void setMask(String s) {
        mask.set(s);
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

    public static Interface convertToInterface(TableInterface i) {
        Interface iface = new Interface();
        iface.setUp(false);
        iface.setPassive(i.isPassive());
        iface.setMask(i.getMask());
        iface.setIp(i.getIp());

        return iface;
    }

    public boolean isPassive() {
        return passive.get();
    }

    public void setPassive(boolean passive) {
        this.passive.set(passive);
    }
}
