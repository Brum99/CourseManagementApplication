package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class EditProfileController implements Initializable {

    private String firstName;
    private String lastName;

    @FXML
    private Button LogOutLoggedIn;
    @FXML
    private Label WelcomeMessageLoggedIn;
    @FXML
    private TextField NewUsernameTF;
    @FXML
    private TextField NewFirstNameTF;
    @FXML
    private TextField NewLastNameTF;
    @FXML
    private TextField NewPasswordTF;
    @FXML
    private Button ChangeButton;
    @FXML
    private Text ChangesText;
    @FXML
    private MenuItem ShowCoursesMenuItem;
    @FXML
    private MenuItem EnrollmentsMenuItem;
    @FXML
    private MenuItem TimeTableMenuItem;
    @FXML
    private MenuItem ExportEnrolledCourses;

    private DatabaseConnection connectionProvider = new DatabaseConnection();

    /**
     * Sets the database connection provider.
     *
     * @param connectionProvider The DatabaseConnection object used for database operations.
     */
    public void DBUserOperations(DatabaseConnection connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    // Create an instance of DBUserOperations using the connection provider
    DBUserOperations dbUserOperations = new DBUserOperations(connectionProvider);

    /**
     * Sets the user information (first name, last name) in the controller and displays it on the UI.
     *
     * @param FirstName The first name of the user.
     * @param LastName  The last name of the user.
     */
    public void setUserInformation(String FirstName, String LastName) {
        WelcomeMessageLoggedIn.setText(FirstName + " " + LastName + " s" + Student.getInstance().getStudentNumber());

        this.firstName = FirstName;
        this.lastName = LastName;
    }

    /**
     * Initializes the controller and sets up event handlers for UI components.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Event handler for Log Out button
        LogOutLoggedIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Call the DBUtilities class method to change the scene to the LogIn.fxml file
                DBUtilities.changeScene(event, "LogIn.fxml", "Orca University: Log In", null, null, null);
            }
        });

        // Event handler for Change button
        ChangeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Call the method to update user details
                updateUserDetails();
            }
        });

        // Event handlers for menu items
        ShowCoursesMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Call the DBUtilities class method to change the scene to the CourseDisplay.fxml file
                DBUtilities.changeSceneForMenuItem(event, "CourseDisplay.fxml", "Orca University: Course Display", firstName, lastName, Student.getInstance().getStudentNumber());
            }
        });

        EnrollmentsMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Call the DBUtilities class method to change the scene to the Enrollments.fxml file
                DBUtilities.changeSceneForMenuItem(event, "Enrollments.fxml", "Orca University: Enrollments", firstName, lastName, Student.getInstance().getStudentNumber());
            }

        });

        TimeTableMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Call the DBUtilities class method to change the scene to the Timetable.fxml file
                DBUtilities.changeSceneForMenuItem(event, "Timetable.fxml", "Orca University: Timetable", firstName, lastName, Student.getInstance().getStudentNumber());
            }

        });

        ExportEnrolledCourses.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Call the HandleExportEnrolledCourses class method to export enrolled courses
                HandleExportEnrolledCourses.exportEnrolledCourses(Student.getInstance().getStudentNumber());
            }
        });
    }

    /**
     * Updates the user details based on the values entered in the text fields.
     */
    private void updateUserDetails() {
        String newUsername = NewUsernameTF.getText().isEmpty() ? null : NewUsernameTF.getText();
        String newFirstName = NewFirstNameTF.getText().isEmpty() ? null : NewFirstNameTF.getText();
        String newLastName = NewLastNameTF.getText().isEmpty() ? null : NewLastNameTF.getText();
        String newPassword = NewPasswordTF.getText().isEmpty() ? null : NewPasswordTF.getText();

        // Check if the new username is unique
        if (newUsername != null && !dbUserOperations.isUsernameUnique(newUsername)) {
            ChangesText.setText("Username already exists, please choose another one.");
            return;
        }

        // Update user details in the database
        boolean updated = dbUserOperations.updateUserDetails(Student.getInstance().getStudentNumber(), newFirstName, newLastName, newUsername, newPassword);

        if (updated) {
            ChangesText.setText("Details updated successfully.");

            // Update local variables if new values are provided
            if (newFirstName != null) {
                this.firstName = newFirstName;
            }

            if (newLastName != null) {
                this.lastName = newLastName;
            }

            // Update the welcome message with the updated user information
            WelcomeMessageLoggedIn.setText(firstName + " " + lastName + " s" + Student.getInstance().getStudentNumber());
        } else {
            ChangesText.setText("No details were updated.");
        }
    }
}
