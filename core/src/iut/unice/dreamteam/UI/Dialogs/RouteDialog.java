package iut.unice.dreamteam.UI.Dialogs;

import iut.unice.dreamteam.Network;
import iut.unice.dreamteam.UI.Adapaters.TableRoute;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Guillaume on 10/03/2017.
 */
public class RouteDialog extends Stage implements Initializable {

    private TableRoute result;

    @FXML
    TextField nexthop, mask, network;


    public RouteDialog() {

        init();

    }

    public RouteDialog(TableRoute route) {
        if (route != null) {
            this.result = route;
        }
        init();
    }

    private void init() {
        setTitle("Add a new Route");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/routeDialog.fxml"));
        fxmlLoader.setController(this);

        try {
            setScene(new Scene((Parent) fxmlLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (result != null) {
            mask.setText(result.getMask());
            network.setText(result.getNetwork());
            nexthop.setText(result.getNextHop());
        } else {
            result = new TableRoute();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void validateDialog() {
        if (Network.isValidIpFormat(network.getText())
                && Network.isValidIpFormat(mask.getText())
                && Network.isValidIpFormat(nexthop.getText())
                ) {
            result.setNetwork(network.getText());
            result.setMask(mask.getText());
            result.setNextHop(nexthop.getText());
            this.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error !");
            alert.setHeaderText(null);
            alert.setContentText("Ip addresses are not in a valid format !");

            alert.showAndWait();
        }
    }

    public void cancelDialog() {
        close();
    }

    public TableRoute getResult() {
        return result;
    }
}
