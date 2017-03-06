package iut.unice.dreamteam.Functionalities;

import iut.unice.dreamteam.Equipments.Equipment;
import iut.unice.dreamteam.Functionalities.Protocols.ApplicationProtocol;
import iut.unice.dreamteam.Functionalities.Protocols.ApplicationProtocols;
import iut.unice.dreamteam.Utils.Debug;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandInterpreter {
    private Equipment equipment;
    private String executionId;

    private ResultCommandListener resultCommandListener;

    public CommandInterpreter(Equipment equipment, ResultCommandListener resultCommandListener) {
        this.equipment = equipment;
        this.executionId = null;

        this.resultCommandListener = resultCommandListener;
    }

    public void executeCommand(String enteredCommand) {
        String[] parsedCommand = enteredCommand.split(" ");

        if(parsedCommand.length < 1)
            return;

        String command = parsedCommand[0];
        ArrayList<String> arguments = new ArrayList<>();

        arguments.addAll(Arrays.asList(parsedCommand).subList(1, parsedCommand.length));

        /*
        for (ApplicationProtocol applicationProtocol : ApplicationProtocols.getInstance().getProtocols()) {
            if (applicationProtocol.hasCommand(command)) {
                executionId = applicationProtocol.getExecutionId();

                applicationProtocol.executeCommand(equipment, command, arguments);

                return;
            }
        }*/

        if (ApplicationProtocols.getInstance().existCommand(command)) {
            Debug.log("exist command : " + command);

            /*
            ApplicationProtocol applicationProtocol = ApplicationProtocols.getInstance().getProtocolFromCommand(command);

            executionId = applicationProtocol.getExecutionId();

            applicationProtocol.setCommandInterpreter(this);
            applicationProtocol.executeCommand(equipment, command, arguments);
            */

            executionId = ApplicationProtocols.getInstance().getProtocolFromCommand(command).executeCommand(equipment, command, arguments, this);

            Debug.log("executionId: " + executionId);
        }
    }

    public void resultFromCommand(ApplicationProtocol applicationProtocol, String text) {
        Debug.log("resultFromCommand : " + text + " " + applicationProtocol.getExecutionId());

        if (!applicationProtocol.getExecutionId().equals(executionId) && !applicationProtocol.usedAsServer())
            return;

        Debug.log("result from command : " + text);

        resultCommandListener.onMessage(text);

        //DISPLAY TEXT
    }

    public interface CommandExecution {
        String execute(Equipment equipment, String command, ArrayList<String> arguments, CommandInterpreter commandInterpreter);
    }

    public interface ResultCommandListener {
        void onMessage(String text);
    }
}
