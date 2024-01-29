package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

public class SignUpController implements Initializable {
    @FXML 
    private TextField FirstNameSignUp;
    @FXML 
    private TextField LastNameSignUp;
    @FXML
    private TextField UsernameSignUp;
    @FXML 
    private PasswordField PasswordSignUp;
    @FXML 
    private Button CreateAccountSignUp;
    @FXML 
    private Button LogInSignUp;
    
    private DatabaseConnection connectionProvider = new DatabaseConnection();
    
    // Inject the DatabaseConnection dependency
    public void DBUserOperations(DatabaseConnection connectionProvider) {
        this.connectionProvider = connectionProvider;
    }
    
    // Create an instance of DBUserOperations to perform user-related operations
    DBUserOperations dbUserOperations = new DBUserOperations(connectionProvider);
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Event handler for the CreateAccountSignUp button
        CreateAccountSignUp.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                // Check if all the fields are filled
                if (!UsernameSignUp.getText().trim().isEmpty() && !FirstNameSignUp.getText().trim().isEmpty() &&
                        !LastNameSignUp.getText().trim().isEmpty() && !PasswordSignUp.getText().trim().isEmpty()) {
                    // Generate a unique student number
                    String studentNumber = StudentNumberGenerator.generateUniqueCode();

                    if (studentNumber != null) {
                        // Sign up the user with the provided information
                        dbUserOperations.signUpUser(event, FirstNameSignUp.getText(), LastNameSignUp.getText(),
                                UsernameSignUp.getText(), PasswordSignUp.getText(), studentNumber);
                    } else {
                        System.out.println("Failed to generate a unique student number.");
                        // Display an error alert if the student number generation fails
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Failed to generate a unique student number.");
                        alert.show();
                    }
                } else {
                    System.out.println("Please fill in all information");
                    // Display an error alert if any of the fields are empty
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all information to sign up!");
                    alert.show();
                }
            }
        });
        
        // Event handler for the LogInSignUp button
        LogInSignUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Redirect the user to the login screen
                DBUtilities.changeScene(event, "login.fxml", "Orca University: Log In", null, null,null);
            }
        });
    }
}

//The SignUpController class handles the sign-up functionality in the application. 
//
//The class implements the Initializable interface, which allows initialization of the controller's JavaFX components.
//The FXML fields are annotated with @FXML to enable the injection of the corresponding GUI components defined in the associated FXML file.
//The DatabaseConnection object is created to establish a connection with the database.
//The DBUserOperations object is created to perform user-related operations.
//The initialize method is overridden and called when the controller is initialized.
//Inside the initialize method, the CreateAccountSignUp button's action is handled by adding an EventHandler to it. When the button is clicked, the provided information is validated, and if all fields are filled, a unique student number is generated. The signUpUser method is then called to register the user with the provided information.
//If the student number generation fails, an error alert is displayed.
//If any of the fields are empty, an error alert is displayed prompting the user to fill in all the information.
//The LogInSignUp button's action is handled similarly, redirecting the user to the login screen when clicked.
//Overall, this class controls the sign-up process by validating the user input, generating a unique student number, and registering the user in the database.
