package iut.unice.dreamteam.Functionalities.Programs;

import java.util.ArrayList;

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
