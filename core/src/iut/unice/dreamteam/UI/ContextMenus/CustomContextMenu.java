package iut.unice.dreamteam.UI.ContextMenus;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 * Created by Guillaume on 24/02/2017.
 */
public class CustomContextMenu extends ContextMenu{
    private static CustomContextMenu oldMenu;
    private MenuItem menuTitle;

    public CustomContextMenu(){
        super();
        init();
    }

    private void init(){

        if (oldMenu != null)
            oldMenu.hide();


        oldMenu = this;

        menuTitle = new MenuItem("");
        menuTitle.setDisable(true);
        menuTitle.getStyleClass().add("menu-title");

        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();

        getItems().addAll(menuTitle, separatorMenuItem);
    }

    public CustomContextMenu(String title){
        super();
        init();
        setTitle(title);
    }


    public void setTitle(String title) {
        menuTitle.setText(title);
    }
}
