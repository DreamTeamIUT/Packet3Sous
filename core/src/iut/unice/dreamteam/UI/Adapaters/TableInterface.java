package iut.unice.dreamteam.UI.Adapaters;

import iut.unice.dreamteam.Interfaces.Interface;
import iut.unice.dreamteam.Interfaces.WiredInterface;
import iut.unice.dreamteam.Interfaces.WirelessInterface;
import iut.unice.dreamteam.UI.Dialogs.EquipmentDialog;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;

public class TableInterface {
    private Interface iface;
    private SimpleStringProperty ip;
    private SimpleStringProperty mask;
    private SimpleStringProperty name;
    private SimpleStringProperty type;
    private SimpleStringProperty gateway;
    private SimpleObjectProperty<CheckBox> passive;
    private SimpleObjectProperty<CheckBox> defaultI;
    private EquipmentDialog updateListenner;
    private boolean updated = false;

    public TableInterface() {
    }

    public TableInterface(Interface i, int index){
        this.ip = new SimpleStringProperty(i.getIp());
        this.mask = new SimpleStringProperty(i.getMask());
        this.name = new SimpleStringProperty("eth" + index);
        this.type = new SimpleStringProperty((i instanceof WiredInterface)?Interface.INTERFACE_TYPE_WIRED : Interface.INTERFACE_TYPE_WIRELESS);
        this.gateway = new SimpleStringProperty(i.getGateway());

        final CheckBox checkBoxPassive = new CheckBox();
        checkBoxPassive.selectedProperty().setValue(i.isPassive());
        checkBoxPassive.setDisable(true);

        this.passive = new SimpleObjectProperty<CheckBox>(checkBoxPassive);

        final CheckBox checkBox = new CheckBox();
        checkBox.selectedProperty().setValue(false);
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (updateListenner != null)
                    updateListenner.checkboxUpdate(TableInterface.this);

                checkBox.setSelected(newValue);
            }
        });
        this.defaultI = new SimpleObjectProperty<>(checkBox);

        this.iface = i;
    }

    public TableInterface(String text, String text1, String value, String text2, boolean selected, String gateway) {
        this.ip = new SimpleStringProperty(text);
        this.mask = new SimpleStringProperty(text1);
        this.name = new SimpleStringProperty(text2);
        this.type = new SimpleStringProperty(value);

        final CheckBox checkBoxPassive = new CheckBox();
        checkBoxPassive.selectedProperty().setValue(selected);
        checkBoxPassive.setDisable(true);

        this.passive = new SimpleObjectProperty<CheckBox>(checkBoxPassive);

        this.gateway = new SimpleStringProperty(gateway);

        final CheckBox checkBox = new CheckBox();
        checkBox.selectedProperty().setValue(false);
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (updateListenner != null)
                    updateListenner.checkboxUpdate(TableInterface.this);
                checkBox.setSelected(newValue);
            }
        });

        this.defaultI = new SimpleObjectProperty<>(checkBox);

    }

    public String getIp() {
        return ip.get();
    }
    public String getMask() {
        return mask.get();
    }
    public String getName() {
        return name.get();
    }
    public String getType() {
        return type.get();
    }
    public Boolean isPassive() {
        return passive.get().selectedProperty().getValue();
    }
    public boolean isDefault(){
        return defaultI.get().selectedProperty().getValue();
    }
    public String getGateway() {
        return gateway.get();
    }


    public void setIp(String s) {
        ip.set(s);
        iface.setIp(s);
    }
    public void setMask(String s) {
        mask.set(s);
        iface.setMask(s);
    }
    public void setName(String name) {
        this.name.set(name);
    }
    public void setType(String type) {
        this.type.set(type);
    }
    public void setDefault(boolean d){
        this.defaultI.get().selectedProperty().set(d);
    }
    public void setPassive(boolean passive) {
        this.passive.get().selectedProperty().set(passive);
        iface.setPassive(passive);
    }
    public void setGateway(String gateway){
        this.gateway.set(gateway);
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }
    public SimpleStringProperty ipProperty() {
        return ip;
    }
    public SimpleStringProperty maskProperty() {
        return mask;
    }
    public SimpleStringProperty nameProperty() {
        return name;
    }
    public SimpleStringProperty gatewayProperty() {
        return gateway;
    }
    public SimpleObjectProperty<CheckBox> passiveProperty() {
        return passive;
    }
    public SimpleObjectProperty<CheckBox> defaultProperty() {
        return defaultI;
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


    public Interface getInterface() {
        if (iface != null)
            return iface;

        this.iface = getInterface(this.getType());

        iface.setMask(getMask());
        iface.setIp(getIp());
        iface.setPassive(isPassive());
        iface.setGateway(getGateway());
        iface.setUp(true);

        return iface;
    }

    public void setUpdateListenner(EquipmentDialog updateListenner) {
        this.updateListenner = updateListenner;
    }

    public void setUpdate(Boolean d){
        updated = d;
    }
}
