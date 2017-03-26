package iut.unice.dreamteam.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Guillaume on 26/03/2017.
 */
public class PingFactory {

    private ArrayList<String> addressToPing;
    private ArrayList<String> exclude;
    private PingResultListenner resultListenner;
    private ThreadFinished threadFinishedListenner;
    private Iterator<IpState> ipStateIterator;
    private ArrayList<IpState> ipStates;

    public PingFactory() {
        addressToPing = new ArrayList<>();
        addressToPing = new ArrayList<>();
        exclude = new ArrayList<>();
        threadFinishedListenner = new ThreadFinished() {
            @Override
            public void onFinish() {
                nextPing();
            }
        };
    }

    public void setResultListener(PingResultListenner resultListenner) {
        this.resultListenner = resultListenner;
    }

    public void exclude(String ip) {
        exclude.add(ip);
    }

    public void queue(String ip) {
        addressToPing.add(ip);
    }

    public void pingAll() {
        ArrayList<String> toRemove = new ArrayList<>();
        for (String ip : addressToPing) {
            if (exclude.contains(ip))
                toRemove.add(ip);
        }
        addressToPing.removeAll(toRemove);

        ipStates = new ArrayList<>();
        for (String ip : addressToPing)
            ipStates.add(new IpState(ip));

        ipStateIterator = ipStates.iterator();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++)
                    nextPing();
            }
        }).start();
    }

    private void nextPing() {
        if (ipStateIterator.hasNext())
            ping(ipStateIterator.next());
    }


    private void ping(final IpState host) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Boolean isReachable = false;
                try {
                    long start = System.currentTimeMillis();
                    Process proc = new ProcessBuilder("ping", host.host, getCountParam(), "1", "-w", "1000").start();

                    int exitValue = proc.waitFor();


                    host.isReachable = (exitValue == 0);

                    resultListenner.pingFinished(host);


                } catch (IOException e1) {
                    Debug.log("Erreur I/O" + e1);
                } catch (InterruptedException e) {
                    Debug.log("Interruption de la commande" + e);
                }
                threadFinishedListenner.onFinish();
            }
        }).start();

    }

    private static boolean isWindows() {
        String OS = System.getProperty("os.name").toLowerCase();
        return (OS.indexOf("win") >= 0);
    }

    private static String getCountParam() {
        String result = "-c";
        if (isWindows())
            result = "-n";
        return result;
    }


    public interface PingResultListenner {
        void pingFinished(IpState ipStates);
    }

    interface ThreadFinished {
        void onFinish();
    }

    public class IpState {
        public String host;
        public Boolean isReachable = false;

        public IpState(String ip) {
            host = ip;
        }
    }
}
