package controllers;

import model.project.*;
import model.users.User;
import view.VMenu;
import view.menu.VMenuMain;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Controller {


    //Attributes
    Database mDatabase;
    VMenu mCurrentMenu;
    private User mCurrentUser;
    private Project mCurrentProject;


    //Constructor
    public Controller() {
        mDatabase = new Database();
        mCurrentMenu = new VMenuMain(null);
        mCurrentUser = null;
        mCurrentProject = null;
    }


    /**
     * Methods
     * I don't know if I like to have the loop like this here but it keeps everything very simple.
     * This loop is what makes moving through the menu's possible.
     */
    public void executeViewsAndDatabase(Controller controller) {

        //Loading the database once, when the program starts
        loadDatabase();

        // This is the loop that keeps us within the different menu's
        // Since we are always in a menu this will always run.
        while (mCurrentMenu != null) {

            // Right now we are saving the database every time we switch view
            saveDatabase();

            // The method ".executeMenu" in the class "VMenu" returns the "chosenMenu",
            // which means that "mCurrentMenu" becomes the "chosenMenu".
            // So if we choose the "VMenuLogin" then we execute that menu.
            mCurrentMenu = mCurrentMenu.executeMenu(controller);
        }
    }


    /**
     * Handling Task/Tasks
     */
    public void addTask(String title, String description, LocalDate dueDate, LocalDate startDate, double estimatedTime, int priority) {
        Task task = new Task(title, description, dueDate, startDate, estimatedTime, priority);
        getCurrentProject().addTaskToList(task);
    }

    public String removeTask(String taskId) {
        int taskListSize = getTaskListFromCurrentProject().size();

        for (int i = 0; i < taskListSize; i++) {
            UUID stringUuid = getTaskListFromCurrentProject().get(i).getId();

            if ( stringUuid.toString().equals(taskId)) {
                getCurrentProject().removeTask(i);
                return "Task with ID: " + taskId + " has been removed";
            }
        }
        return "Task with ID: " + taskId + " was not found";
    }

    public Task getTaskById(String taskId) {
        int taskListSize = getTaskListFromCurrentProject().size();
        for (int i = 0; i < taskListSize; i++) {
            UUID stringUuid = getTaskListFromCurrentProject().get(i).getId();
            if (stringUuid.toString().equals(taskId)) {
                return getTaskListFromCurrentProject().get(i);
            }
        }
        return null;
    }

    public ArrayList<Task> getTaskListFromCurrentProject() {

        return getCurrentProject().getTaskList();
    }

   /* public ArrayList<Checklist> getChecklistFromCurrentTask(){
        return getCurrentProject().getTaskList().get
    }*/

    public String addChecklist(String name, String taskId, ArrayList<String> itemStringList) {
        Task task = getTaskById(taskId);
        Checklist checklist = new Checklist(name);


        if (!(itemStringList.isEmpty())) {
            ArrayList<ChecklistItem> checklistItemList = new ArrayList<>();

            for (String s : itemStringList) {
                checklistItemList.add(new ChecklistItem(s));
            }
            checklist.setItems(checklistItemList);
            task.addChecklist(checklist);

            return "Checklist with name: " + name + " and " + checklistItemList.size() + " item(s), has successfully been created";
        } else {
            task.addChecklist(checklist);

            return "Checklist with name: " + name + " has successfully been created";
        }
    }

    /**
     * Updating Task
     */
    public void updateTaskStatus(String updatedStatus, String taskId){
    Task task = getTaskById(taskId);
    task.setStatus(updatedStatus);
    }
    public void updateTaskTitle(String updatedTitle, String taskId){
        Task task = getTaskById(taskId);
        task.setTitle(updatedTitle);
    }
    public void updateTaskDescription(String updatedDescription, String taskId){
        Task task = getTaskById(taskId);
        task.setDescription(updatedDescription);
    }
    public void updateTaskPriority(int updatedPriority, String taskId){
        Task task = getTaskById(taskId);
        task.setPriority(updatedPriority);
    }
    public void updateTaskDueDate(LocalDate dueDate, String taskId){
        Task task = getTaskById(taskId);
        task.setDueDate(dueDate);
    }
    public void updateTaskEstimatedTime(Double estimatedTime, String taskId){
        Task task = getTaskById(taskId);
        task.setEstimatedTime(estimatedTime);
    }


    /**
     *  Handling user
     */

    public String checkUsername(String enteredUsername) {
        Collection<User> userList = mDatabase.getUserList();

        for (Iterator<User> iterator = userList.iterator(); iterator.hasNext(); ) {
            User someOne = iterator.next();
            if(someOne.getUserName().equals(enteredUsername)) {
                return "This username is taken before. Please select another username.";
            }
        }
        return enteredUsername;
    }


    public Project searchProjectByTitle(String title) {
        Collection<Project> projectList = mDatabase.getProjectList();
        for (Iterator<Project> iterator = projectList.iterator(); iterator.hasNext(); ) {
            Project project = iterator.next();
            if(project.getProjectTitle().equals(title)) {
                return project;
            }
        }
        Project newProject = new Project(title);
        mDatabase.addProject(newProject);
        return newProject;
    }


    public void addUser(String userName, String firstName, String lastName, String password, String companyName, double jobTitle,
            String hourlyWage ) {
        User user = new User(userName, firstName, lastName, password, companyName, jobTitle, hourlyWage);
        mDatabase.addUser( user );
        System.out.println("Your username is: " + user.getUserName() + "\nYour password is: " + user.getPassword());
    }

    public void removeUser(String id) {
        mDatabase.removeUser(UUID.fromString(id));
    }


    public String logInUser(String enteredUserName, String enteredPassword) {
        Collection<User> userList = mDatabase.getUserList();

        for (Iterator<User> iterator = userList.iterator(); iterator.hasNext();) {
            User someOne  = iterator.next();
            if (someOne.getUserName().equals(enteredUserName)) {
                if (someOne.getPassword().equals(enteredPassword)) {
                    setCurrentUser(someOne);

                    return "Bravo! You logged in.";
                } else {

                    return "Password is incorrect";
                }
            }
        }
        return "Username is incorrect";
    }

    /**
     * Methods for Current Logged-In User
     */
    public void setCurrentUser(User currentUser) {
        mCurrentUser = currentUser;
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }


    /**
     * Handling Project
     */
    public String createProject(String title, String description, ArrayList<String> enteredIds,
                                LocalDate startDate, LocalDate dueDate) {

        Project project = new Project(title, description, startDate, dueDate);
        String projectId = project.getId();

        Collection<User> userList = mDatabase.getUserList();

        for (int i = 0; i < enteredIds.size(); i++) {
            for (Iterator<User> iterator = userList.iterator(); iterator.hasNext(); ) {
                User someOne = iterator.next();

                if (someOne.getId().equals(enteredIds.get(i))) {
                   project.getProjectMembers().add(someOne);
                   someOne.getProjects().add(project);
                   someOne.addRole(projectId);
                }
            }
        }
        mCurrentUser.changeRole(projectId);

        return "\nProject " + project.getProjectTitle() + " is created successfully! You are the manager of this project now ;)";
    }

    public ArrayList<Project> getProjects() {
        return getCurrentUser().getProjects();
    }

    public void addMembers(ArrayList<String> newMembersIds) {

        Collection<User> userList = mDatabase.getUserList();

        for (int i = 0; i < newMembersIds.size(); i++) {
            for (Iterator<User> iterator = userList.iterator(); iterator.hasNext(); ) {
                User someOne = iterator.next();

                if (someOne.getId().equals(newMembersIds.get(i))) {
                    mCurrentProject.getProjectMembers().add(someOne);
                    someOne.getProjects().add(mCurrentProject);
                    someOne.addRole(mCurrentProject.getId());
                }
            }
        }


    }

    public void changeRoles(ArrayList<String> memberIds) {

        for(int i = 0; i < memberIds.size(); i++) {
            for(int j = 0; j < getCurrentProject().getProjectMembers().size(); j++) {
                if(memberIds.get(i).equals(getCurrentProject().getProjectMembers().get(j).getId())) {
                    getCurrentProject().getProjectMembers().get(j).changeRole(getCurrentProject().getId());
                }
            }
        }
        System.out.println("Roles are successfully changed");
    }

    /**
     * Methods for Current Project
     */
    public void setCurrentProject(int chosenProject) {
        mCurrentProject = getCurrentUser().getProjects().get(chosenProject);

        saveDatabase(); // We save the project once it has been accessed
    }

    public Project getCurrentProject() {
        return mCurrentProject;
    }


    /**
     * Method for saving DATABASE to a file:
     */

    //TODO Save the Database with all the users
    public void saveDatabase() {
        String fileLocation = "data/database.ser";

        try {
            FileOutputStream fileOut = new FileOutputStream(fileLocation);
            ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
            outStream.writeObject(mDatabase);
            outStream.close();
            fileOut.close();
//            System.out.println("Changes are saved in " + fileLocation);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadDatabase() {

        String STORAGE = "./src/STORAGE.csv";

        try {
            File customerFile = new File(STORAGE);
            FileReader fileReader = new FileReader(customerFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] retrievedInfo = line.split(";");
                if (retrievedInfo[0].equals("User")) {
                    User user = new User(retrievedInfo);
                    for(int i = 0; i < (retrievedInfo.length - 8); i = i + 2) {
                        Project project = searchProjectByTitle(retrievedInfo[i + 8]);
                        user.getProjects().add(project);
                        project.getProjectMembers().add(user);
                        user.addRole(project.getId());
                        if (!(user.getRole(project.getId()).equals(retrievedInfo[i + 9]))) {
                            user.changeRole(project.getId());
                        }
                    }
                    mDatabase.addUser(user);
                    System.out.println("Added: " + Arrays.toString(retrievedInfo));
                }
            }

        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    void doMainMenu() {
//        mVMenuMain.renderMenu(true);
//
//        UUID id = null;
//        String password;
//
//        int mainMenuSelect = mVMenuMain.readInput();
//
//        switch (mainMenuSelect) {
//            case 1 -> {
//                //Register menu
//                mVMenuRegister.renderMenu(true);
//                User user = mVMenuRegister.getUserData();
//
//                mDatabase.addUser( user );
//                mVMenuRegister.registerSuccess();
//                doMainMenu();
//            }
//            case 2 -> {
//                mVMenuLogin.renderMenu(true);
//                mVMenuLogin.readInput();
//            }
//            case 3 -> {
//                mVMenuManual.renderMenu(true);
//                mVMenuManual.readInput();
//            }
//            case 4 -> {
//                mVMenuExit.renderMenu(true);
//                mVMenuExit.readInput();
//            }
//            default -> {
//                mVMenuMain.renderError();
//                doMainMenu();
//            }
//        }
//        doMainMenu();
//    }
}
