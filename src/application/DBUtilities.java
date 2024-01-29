package application;

import java.io.IOException;


import javafx.scene.control.MenuItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.ContextMenu;
import javafx.stage.Stage;

public class DBUtilities {

    /**
     * Changes the scene to the specified FXML file when an event occurs, such as a button click.
     * It sets the user information (first name, last name, student number) in the target controller if necessary.
     *
     * @param event         The ActionEvent that triggers the scene change.
     * @param fxmlFile      The name of the target FXML file.
     * @param title         The title to be set for the new scene.
     * @param FirstName     The first name of the user.
     * @param LastName      The last name of the user.
     * @param StudentNumber The student number of the user.
     */
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String FirstName, String LastName, String StudentNumber) {
        Parent root = null;

        try {
            FXMLLoader loader = new FXMLLoader(DBUtilities.class.getResource(fxmlFile));
            root = loader.load();

            // Set user information in the target controller if necessary
            if (fxmlFile.equals("EditProfile.fxml")) {
                EditProfileController editProfileController = loader.getController();
                editProfileController.setUserInformation(FirstName, LastName);
            } else if (fxmlFile.equals("CourseDisplay.fxml")) {
                CourseDisplayController courseDisplayController = loader.getController();
                courseDisplayController.setUserInformation(FirstName, LastName, StudentNumber);
            }
            // Add additional conditions for other FXML files if needed

        } catch (IOException e) {
            e.printStackTrace();
        }

        Node sourceNode = (Node) event.getSource();
        Scene scene = sourceNode.getScene();
        Stage stage = (Stage) scene.getWindow();

        stage.setTitle(title);
        Scene newScene = new Scene(root, 813, 503);
        newScene.getStylesheets().add(DBUtilities.class.getResource("application.css").toExternalForm()); // Add this line
        stage.setScene(newScene);
        stage.show();
    }

    /**
     * Changes the scene to the specified FXML file when a menu item is selected.
     * It sets the user information (first name, last name, student number) in the target controller if necessary.
     *
     * @param event         The ActionEvent that triggers the scene change.
     * @param fxmlFile      The name of the target FXML file.
     * @param title         The title to be set for the new scene.
     * @param FirstName     The first name of the user.
     * @param LastName      The last name of the user.
     * @param StudentNumber The student number of the user.
     */
    public static void changeSceneForMenuItem(ActionEvent event, String fxmlFile, String title, String FirstName, String LastName, String StudentNumber) {
        MenuItem menuItem = (MenuItem) event.getSource();
        ContextMenu contextMenu = menuItem.getParentPopup();
        Node ownerNode = contextMenu.getOwnerNode();
        Scene scene = ownerNode.getScene();
        Stage stage = (Stage) scene.getWindow();

        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(DBUtilities.class.getResource(fxmlFile));
            root = loader.load();

            // Set user information in the target controller if necessary
            if (fxmlFile.equals("EditProfile.fxml")) {
                EditProfileController editProfileController = loader.getController();
                editProfileController.setUserInformation(FirstName, LastName);
            } else if (fxmlFile.equals("CourseDisplay.fxml")) {
                CourseDisplayController courseDisplayController = loader.getController();
                courseDisplayController.setUserInformation(FirstName, LastName, StudentNumber);
            } else if (fxmlFile.equals("Enrollments.fxml")) {
                EnrollmentsController enrollmentsController = loader.getController();
                enrollmentsController.setUserInformation(FirstName, LastName, StudentNumber);
            } else if (fxmlFile.equals("Timetable.fxml")) {
                TimeTableController timeTableController = loader.getController();
                timeTableController.setUserInformation(FirstName, LastName, StudentNumber);
                // Add additional conditions for other FXML files if needed
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setTitle(title);
        Scene newScene = new Scene(root, 813, 503); // Change scene dimensions as required
        newScene.getStylesheets().add(DBUtilities.class.getResource("application.css").toExternalForm()); // Add this line
        stage.setScene(newScene);
        stage.show();
    }
}
//The DBUtilities class provides utility methods for changing scenes in a JavaFX application. Here is the documentation for the class and its methods:
//
//Class: DBUtilities
//Methods:
//changeScene(ActionEvent event, String fxmlFile, String title, String FirstName, String LastName, String StudentNumber): Changes the scene to the specified FXML file when an event occurs, such as a button click. It sets the user information (first name, last name, student number) in the target controller if necessary.
//
//Parameters:
//event: The ActionEvent that triggers the scene change.
//fxmlFile: The name of the target FXML file.
//title: The title to be set for the new scene.
//FirstName: The first name of the user.
//LastName: The last name of the user.
//StudentNumber: The student number of the user.
//changeSceneForMenuItem(ActionEvent event, String fxmlFile, String title, String FirstName, String LastName, String StudentNumber): Changes the scene to the specified FXML file when a menu item is selected. It sets the user information (first name, last name, student number) in the target controller if necessary.
//
//Parameters:
//event: The ActionEvent that triggers the scene change.
//fxmlFile: The name of the target FXML file.
//title: The title to be set for the new scene.
//FirstName: The first name of the user.
//LastName: The last name of the user.
//StudentNumber: The student number of the user.
	

