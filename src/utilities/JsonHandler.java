package utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.google.gson.reflect.TypeToken;
import controllers.Controller;
import model.project.Database;
import model.project.Project;
import model.users.Role;
import model.users.User;

import javax.xml.crypto.Data;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class JsonHandler {

    Database mFoundDatabase;

    public void saveDatabase(Controller controller) {
        JsonHandler jsonHandler = new JsonHandler();
        Database database = controller.getDatabase();

        try {
            jsonHandler.saveToJson(database);

        } catch (FileNotFoundException notFoundException){
            System.out.println("File was not found.");
            notFoundException.printStackTrace();
        } catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    private <T> void saveToJson(T anyT) throws IOException {
        String fileLocation = "data/database.json";

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new DateSerializer());

        Gson gson = gsonBuilder.create();

        FileWriter fileWriter = new FileWriter(fileLocation);
        fileWriter.write(gson.toJson(anyT));
        fileWriter.close();
    }

    public boolean isDatabaseNull (Database database) {
        JsonHandler jsonHandler = new JsonHandler();
        if ( database == null) {
            return true;
        }

        return false;
    }

    public Database getFoundDatabase() {
        return mFoundDatabase;
    }

    public void setFoundDatabase(Database database) {
        this.mFoundDatabase = database;
    }

    public void loadDatabase() throws FileNotFoundException {
        String fileLocation = "data/database.json";
        Database database = new Database();

        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(LocalDate.class, new DateDeserializer());

            Gson gson = gsonBuilder.setPrettyPrinting().create();

            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileLocation));

            database = gson.fromJson(bufferedReader, Database.class);

            // Since we do not save ProjectMembers we have to add them back when we launch the app again.
            addUsersToTheirProjects(database);

            if (isDatabaseNull(database) == false) {
                setFoundDatabase(database);
            }

        } catch (FileNotFoundException notFoundException){
            System.out.println("File was not found.");
            notFoundException.printStackTrace();
        } catch (IOException ioException){
            ioException.printStackTrace();
        } catch (NullPointerException nullEx){
            System.out.println("OBS! Cannot load database since it is empty");
//            nullEx.printStackTrace();
        }
    }

    // TODO Refactor this monster. It is not working properly.
    //  This nested for loop is supposed to add a User back to a Project,
    //  since we do not save the ProjectMembers in the Project class.
    private void addUsersToTheirProjects(Database database) {
        Collection<User> userList = database.getUserList();
        for (User currentUser : userList) {
            for (int i = 0; i < currentUser.getRoles().size(); i++) {
                String currentRoleId = currentUser.getRoles().get(i).getProjectId();

                for (int j = 0; j < currentUser.getProjects().size(); j++) {
                    Project currentProject = currentUser.getProjects().get(j);
                    String currentProjectId = currentProject.getId().toString();

                    if (currentProjectId.equals(currentRoleId)) {

                        if (currentProject.getProjectMembers() == null) {
                            ArrayList<User> tempUserList = new ArrayList<>();
                            tempUserList.add(currentUser);
                            currentProject.setProjectMembers(tempUserList);
                        } else {
                            currentProject.getProjectMembers().add(currentUser);
                        }
                    }
                }
            }
        }

    }

}
