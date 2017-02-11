package iut.unice.dreamteam.NetworkLayers;

import org.json.JSONObject;

public class ApplicationLayer extends GenericLayer {
    private JSONObject content;

    public ApplicationLayer(JSONObject content) {
        super();

        setContent(content);
    }

    public JSONObject getContent() {
        return content;
    }

    public void setContent(JSONObject content) {
        this.content = content;
    }
}
