package view;


import controllers.Controller;
import view.submenu.VMenuLoggedIn;

import java.util.ArrayList;

public class VMenuMain extends VMenu{

//    ArrayList<VMenu> mChildren;

    /**
     * Contructors
     */
    public VMenuMain(VMenu parent) {
        super(null);
        menuHeader = "Main Menu";
        menuLabel = "Go back to Main Menu";
        menuQuestion = "Enter choice";
        menuChoice = "M";

        subMenus = new ArrayList<VMenu>();
        subMenus.add(new VMenuLoggedIn(this));
        subMenus.add(new VMenuRegister(this));
        subMenus.add(new VMenuLogin(this));
        subMenus.add(new VMenuManual(this));
        subMenus.add(new VMenuExit(this));
        subMenu = true;
    }

    @Override
    public void menuContent(Controller controller) {

    }


    public void renderExit() {
        System.out.println("Exiting the system. Goodbye!");
    }

    public void renderError() {
        System.out.println("Invalid selection, restarting...");
    }

}
