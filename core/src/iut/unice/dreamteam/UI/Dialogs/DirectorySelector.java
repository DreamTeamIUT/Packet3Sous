package iut.unice.dreamteam.UI.Dialogs;

import iut.unice.dreamteam.Utils.Debug;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by dd500076 on 06/03/17.
 */
public class DirectorySelector extends Stage {

    public DirectorySelector(DirectorySelection directorySelection) {
        File file = new DirectoryChooser().showDialog(this);

        if(file != null)
            directorySelection.onSelect(file.getAbsolutePath());
        else
            Debug.log("no selected file");
    }

    public interface DirectorySelection {
        void onSelect(String filePath);
    }
}
