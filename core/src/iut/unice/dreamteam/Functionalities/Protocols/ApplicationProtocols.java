package iut.unice.dreamteam.Functionalities.Protocols;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.json.*;

public class ApplicationProtocols {
    private static ApplicationProtocols applicationProtocols;
    private ArrayList<ApplicationProtocol> applicationProtocolArrayList;

    private ApplicationProtocols()
    {
        applicationProtocolArrayList = new ArrayList<>();
        applicationProtocolArrayList.add(new ARP());
        applicationProtocolArrayList.add(new ICMP());
    }

    public static ApplicationProtocols getInstance() {
        if(applicationProtocols == null)
            applicationProtocols = new ApplicationProtocols();

        return applicationProtocols;
    }

    public ApplicationProtocol find(JSONObject jsonObject, ArrayList<String> supportedProtocols) {
        if (jsonObject.has("protocol")) {
            for (ApplicationProtocol applicationProtocol : this.applicationProtocolArrayList) {
                if(applicationProtocol.getName().equals(((JSONObject)jsonObject.get("protocol")).getString("name")) && supportedProtocols.contains(applicationProtocol.getName()))
                    try {
                        return applicationProtocol.getClass().getConstructor().newInstance();
                    } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
            }
        }

        return null;
    }

    public ApplicationProtocol getProtocol(String protocolName, ArrayList<String> supportedProtocols) {
        for (ApplicationProtocol applicationProtocol : this.applicationProtocolArrayList) {
            if (applicationProtocol.getName().equals(protocolName) && supportedProtocols.contains(applicationProtocol.getName()))
                return applicationProtocol;
        }

        return null;
    }

    public ArrayList<ApplicationProtocol> getProtocols() {
        return this.applicationProtocolArrayList;
    }

    public Boolean existCommand(String command) {
        for (ApplicationProtocol applicationProtocol : ApplicationProtocols.getInstance().getProtocols()) {
            if (applicationProtocol.hasCommand(command))
                return true;
        }

        return false;
    }

    public ApplicationProtocol getProtocolFromCommand(String command) {
        for (ApplicationProtocol applicationProtocol : ApplicationProtocols.getInstance().getProtocols()) {
            if (applicationProtocol.hasCommand(command)) {
                try {
                    return applicationProtocol.getClass().getConstructor().newInstance();
                } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
