package iut.unice.dreamteam.Protocols;


import java.util.ArrayList;
import java.util.StringTokenizer;

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
                    return applicationProtocol;
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
}
