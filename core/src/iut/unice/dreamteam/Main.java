package iut.unice.dreamteam;

/*
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
*/

import iut.unice.dreamteam.Equipments.Computer;
import iut.unice.dreamteam.Equipments.Equipment;
import iut.unice.dreamteam.Equipments.Router;
import iut.unice.dreamteam.Equipments.Switch;
import iut.unice.dreamteam.Interfaces.Packet;
import iut.unice.dreamteam.NetworkLayers.ApplicationLayer;
import iut.unice.dreamteam.NetworkLayers.IpLayer;
import iut.unice.dreamteam.NetworkLayers.TransportLayer;
import iut.unice.dreamteam.Protocols.TCP;
import iut.unice.dreamteam.Utils.Debug;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/mainUi.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);

        final Network n = new Network();

        Equipment computer1 = new Computer("PC 01");

        computer1.getInterface(0).setIp("192.168.0.2");
        computer1.getInterface(0).setMask("255.255.255.0");
        computer1.getInterface(0).setGateway("192.168.0.254");

        computer1.setDefaultGateway(computer1.getInterface(0));

        Equipment computer2 = new Computer("PC 02");
        computer2.getInterface(0).setIp("192.168.10.5");
        computer2.getInterface(0).setMask("255.255.255.0");
        computer2.getInterface(0).setGateway("192.168.10.254");

        computer2.setDefaultGateway(computer2.getInterface(0));


        Router router1 = new Router("Router 01");
        router1.getInterface(0).setIp("192.168.0.254");
        router1.getInterface(0).setMask("255.255.255.0");

        router1.getInterface(1).setIp("192.168.2.254");
        router1.getInterface(1).setMask("255.255.255.0");

        router1.addRoute("192.168.10.0", "255.255.255.0", "192.168.2.253");

        Router router2 = new Router("Router 02");
        router2.getInterface(0).setIp("192.168.2.253");
        router2.getInterface(0).setMask("255.255.255.0");

        router2.getInterface(1).setIp("192.168.10.254");
        router2.getInterface(1).setMask("255.255.255.0");

        Switch switchEquipment = new Switch("Switch 01");

        n.addEquipment(router1);
        n.addEquipment(router2);
        n.addEquipment(computer1);
        n.addEquipment(computer2);



        Network.linkInterfaces(computer1.getInterface(0), router1.getInterface(0));
        Network.linkInterfaces(router1.getInterface(1), router2.getInterface(0));
        Network.linkInterfaces(router2.getInterface(1), computer2.getInterface(0));

        Packet p = new Packet();

        p.setTransportLayer(new TransportLayer(new TCP()));
        IpLayer ip = new IpLayer();

        ip.setSource(computer1.getInterface(0).getIp());
        ip.setDestination(computer2.getInterface(0).getIp());
        p.setIpLayer(ip);

        JSONObject protocolObject = new JSONObject();
        protocolObject.put("name", "TEST");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("protocol", protocolObject);
        jsonObject.put("data", "NONE");

        p.setApplicationLayer(new ApplicationLayer(jsonObject));

        computer1.sendPacket(p);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Debug.log("loop");
                n.updateEquipments();
            }
        }, 0, 5000);
    }
}


