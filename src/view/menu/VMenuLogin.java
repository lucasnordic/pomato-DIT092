package view.menu;

import controllers.Controller;
import utilities.InputOutput;
import view.VMenu;
import view.menu.loggedin.VMenuLoggedIn;

import java.util.ArrayList;

public class VMenuLogin extends VMenu {


    /**
     * Constructors
     */
    public VMenuLogin(VMenu parent) {
        super(parent);
        mMenuHeader = "Log-in";
        mMenuLabel = "Log-In";
        mMenuQuestion = "Enter choice";
        mMenuChoice = "L";
        mSubMenus = new ArrayList<>();
//        subMenus.add(new VMenuCommentBoard(this));
//        subMenu = true;
    }


    /**
     * Methods
     */
    @Override
    public void menuContent(Controller controller) {

        mSubMenus.clear();
        String userName = InputOutput.inputString("Enter Username");
        String password = InputOutput.inputString("Enter Password");

        String response = controller.logInUser(userName, password);
        System.out.println(response);

        if (response.equals("Bravo! You logged in.")) {
          mSubMenus.add(new VMenuLoggedIn(this));
        }

        System.out.println(" ");
    }

}
