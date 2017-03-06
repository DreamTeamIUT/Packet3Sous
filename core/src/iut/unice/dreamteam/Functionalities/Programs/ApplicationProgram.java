package iut.unice.dreamteam.Functionalities.Programs;

import iut.unice.dreamteam.Functionalities.Protocols.ApplicationProtocol;

import java.util.UUID;

public class ApplicationProgram {
    private String executionId;

    public ApplicationProgram() {
        setExecutionId(UUID.randomUUID().toString());
    }

    public String getExecutionId() {
        return executionId;
    }

    private void setExecutionId(String executionId) {
        this.executionId = executionId;
    }
}
