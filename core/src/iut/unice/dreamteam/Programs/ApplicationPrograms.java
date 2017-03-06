package iut.unice.dreamteam.Programs;

import iut.unice.dreamteam.Protocols.ApplicationProtocol;

import java.util.ArrayList;

/**
 * Created by Dylan on 05/03/2017.
 */
public class ApplicationPrograms {
    private static ApplicationPrograms applicationPrograms;
    private ArrayList<ApplicationProgram> applicationProgramArrayList;

    private ApplicationPrograms() {
        applicationProgramArrayList = new ArrayList<>();
    }

    public static ApplicationPrograms getInstance() {
        if (applicationPrograms == null)
            applicationPrograms = new ApplicationPrograms();

        return applicationPrograms;
    }

    public ArrayList<ApplicationProgram> getPrograms() {
        return this.applicationProgramArrayList;
    }
}
