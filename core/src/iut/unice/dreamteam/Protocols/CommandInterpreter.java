package iut.unice.dreamteam.Protocols;

import iut.unice.dreamteam.Equipments.Equipment;
import iut.unice.dreamteam.Programs.ApplicationProgram;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Dylan on 05/03/2017.
 */
public class CommandInterpreter {
    private Equipment equipment;
    private String currentProtocol;

    public void executeCommand(String enteredCommand) {
        String[] parsedCommand = enteredCommand.split(" ");

        if(parsedCommand.length < 1)
            return;

        String command = parsedCommand[0];
        ArrayList<String> arguments = new ArrayList<>();

        arguments.addAll(Arrays.asList(parsedCommand).subList(1, parsedCommand.length));

        for (ApplicationProtocol applicationProtocol : ApplicationProtocols.getInstance().getProtocols()) {
            if (applicationProtocol.hasCommand(command)) {
                currentProtocol = applicationProtocol.getName();

                applicationProtocol.executeCommand(equipment, command, arguments);

                return;
            }
        }
    }

    public void resultFromCommand(ApplicationProtocol applicationProtocol, String text) {
        if (!applicationProtocol.getName().equals(currentProtocol))
            return;

        //DISPLAY TEXT
    }

    public interface CommandExecution {
        Boolean execute(Equipment equipment, String command, ArrayList<String> arguments);
    }
}
