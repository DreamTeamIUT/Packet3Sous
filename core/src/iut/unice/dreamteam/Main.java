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
import iut.unice.dreamteam.Functionalities.Protocols.ICMP;
import iut.unice.dreamteam.Functionalities.Protocols.TCP;
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
        primaryStage.setTitle("Packet3Sous");
        primaryStage.setScene(new Scene(root, 500, 275));
        primaryStage.getScene().getStylesheets().add(getClass().getResource("/css/titleMenuItem.css").toExternalForm());
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);

        /*
        final Network n = new Network();

        Equipment computer1 = new Computer("PC 01");

        computer1.getInterface(0).setIp("192.168.0.2");
        computer1.getInterface(0).setMask("255.255.255.0");
        computer1.getInterface(0).setGateway("192.168.0.254");

        //computer1.setDefaultGateway(computer1.getInterface(0));

        //computer1.startService("ICMP", true);
        //computer1.startService("ICMP");

        Equipment computer2 = new Computer("PC 02");
        computer2.getInterface(0).setIp("192.168.10.5");
        computer2.getInterface(0).setMask("255.255.255.0");
        computer2.getInterface(0).setGateway("192.168.10.254");

        //computer2.setDefaultGateway(computer2.getInterface(0));

        //computer2.startService("ICMP", true);
        //computer2.startService("ICMP");


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

        router2.addRoute("192.168.0.0", "255.255.255.0", "192.168.2.254");

        //Switch switchEquipment = new Switch("Switch 01");

        n.addEquipment(router1);
        n.addEquipment(router2);
        n.addEquipment(computer1);
        n.addEquipment(computer2);

        Network.linkInterfaces(computer1.getInterface(0), router1.getInterface(0));
        Network.linkInterfaces(router1.getInterface(1), router2.getInterface(0));
        Network.linkInterfaces(router2.getInterface(1), computer2.getInterface(0));

        /*
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

        Packet packet = new ICMP().initiate(computer1.getInterface(0), new JSONObject().put("ip-address", computer2.getInterface(0).getIp()));

        computer1.sendPacket(packet);
        */

        //Debug.log("exist ICMP service : " + computer1.existService("icmp-client"));

        /*
        if (computer1.existService("icmp-client")) {
            computer1.getService("icmp-client").initiateProtocol(computer1, computer1.getInterface(0), new JSONObject().put("ip-address", computer2.getInterface(0).getIp()));
        }
        */

        /*
        Debug.log("exist ICMP service : " + router1.existService("icmp-client"));

        if (router1.existService("icmp-client")) {
            router1.getService("icmp-client").initiateProtocol(router1, router1.getInterface(1), new JSONObject().put("ip-address", router2.getInterface(0).getIp()));
        }

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Debug.log("loop");
                n.updateEquipments();
            }
        }, 0, 100);
        */
    }
}


