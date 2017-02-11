package iut.unice.dreamteam;

import iut.unice.dreamteam.Equipments.*;
import iut.unice.dreamteam.Interfaces.Packet;
import iut.unice.dreamteam.NetworkLayers.ApplicationLayer;
import iut.unice.dreamteam.NetworkLayers.IpLayer;
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
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);

        final Network n = new Network();

        Equipment e = new Computer("PC 01");
        e.setGateway("192.168.0.254");
        e.getInterface(0).setIp("192.168.0.2");
        e.getInterface(0).setMask("255.255.255.0");

        Equipment ea = new Computer("PC 02");
        ea.setGateway("192.168.0.254");
        ea.getInterface(0).setIp("192.168.0.5");
        ea.getInterface(0).setMask("255.255.255.0");


       /* Router router = new Router("Router 01");
        router.getInterface(0).setIp("192.168.0.254");
        router.getInterface(0).setMask("255.255.255.0");

        router.getInterface(1).setIp("192.168.2.254");
        router.getInterface(1).setMask("255.255.255.0");*/

       //Hub hub = new Hub("Hub 01");
       Switch hub = new Switch("Switch 01");


        //n.addEquipment(router);
        n.addEquipment(hub);
        n.addEquipment(e);
        n.addEquipment(ea);



        Network.linkInterfaces(e.getInterface(0), hub.getInterface(0));
        Network.linkInterfaces( ea.getInterface(0), hub.getInterface(1));

        Packet p = new Packet();
        IpLayer ip = new IpLayer();

        ip.setSource(e.getInterface(0).getIp());
        ip.setDestination(ea.getInterface(0).getIp());
        p.setIpLayer(ip);

        JSONObject protocolObject = new JSONObject();
        protocolObject.put("name", "TEST");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("protocol", protocolObject);
        jsonObject.put("data", "NONE");

        p.setApplicationLayer(new ApplicationLayer(jsonObject));

        e.sendPacket(p);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Debug.log("loop");
                n.updateEquipments();
            }
        }, 0, 5000);

    }
}


