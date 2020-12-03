package view.menu;

import controllers.Controller;
import model.project.Project;
import utilities.InputOutput;
import view.*;
import view.menu.loggedin.VMenuCreateProject;
import view.menu.loggedin.VMenuMessage;
import view.menu.loggedin.VMenuProject;
import view.menu.loggedin.VMenuAccount;

import java.util.ArrayList;

// When the user is logged in
public class VMenuLoggedIn extends VMenu {


    /**
     * Constructors
     */
    public VMenuLoggedIn(VMenu parent) {
        super(parent.getParentMenu());
        mMenuHeader = "Your Personal Menu";
        mMenuLabel = "Continue";
        mMenuQuestion = "Enter choice";
        mSubMenus = new ArrayList<>();
    }


    /**
     * Methods
     */
    @Override
    public void menuContent(Controller controller) {
        addSubMenus(controller);
    }

    @Override
    public VMenu chooseMenu(VMenu mParent, Controller controller) {
        VMenu chosenVMenu;

        int inputResult = InputOutput.inputInt(mMenuQuestion/*,1,mSubMenus.size() + 1*/);

        if (mSubMenus != null && inputResult > 0 && inputResult < mSubMenus.size() + 1) {
            chosenVMenu = mSubMenus.get(inputResult - 1);

            // User has chosen a project they want to work on in the menu and,
            // we set the project as the Current Active Project.
            int projectListSize = controller.getCurrentUser().getProjects().size();
            if ( projectListSize > (0) && !(inputResult > projectListSize)){
                setCurrentProject(controller, inputResult);
            }
        } else {
            chosenVMenu = mParent;
        }

        return chosenVMenu;
    }


    /**
     * Add subMenus
     */
    private void addSubMenus(Controller controller){
        mSubMenus.clear();
        ArrayList<Project> projectList = controller.getProjects();

        for (int i = 0; i < projectList.size(); i++) {
            Project currentProject = projectList.get(i);
            mSubMenus.add(new VMenuProject(this));
            mSubMenus.get(i).setmMenuLabel("View Project: " + currentProject.getProjectTitle());
            mSubMenus.get(i).setmMenuHeader("Project Page for " + currentProject.getProjectTitle());
        }

        mSubMenus.add(new VMenuCreateProject(this));
        mSubMenus.add(new VMenuMessage(this));
        mSubMenus.add(new VMenuAccount(this));
    }

    private void setCurrentProject(Controller controller, int chosenProject){
        controller.setCurrentProject(chosenProject - 1);
    }
}
