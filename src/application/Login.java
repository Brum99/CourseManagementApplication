package application;

import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;


public class Login implements Initializable {
    
    @FXML 
    private Button CancelButtonLogin;
    @FXML 
    private Button LoginButtonLogin;
    @FXML 
    private Button getSettingsButton;
    
    @FXML 
    private Label ErrorLabel;
    @FXML
    private TextField UsernameLogin;
    @FXML 
    private PasswordField passwordLogin;
    @FXML 
    private Button SignUpButtonLogIn;
    
    private DatabaseConnection connectionProvider = new DatabaseConnection();
    
    public void DBUserOperations(DatabaseConnection connectionProvider) {
        this.connectionProvider = connectionProvider;
    }
    DBUserOperations dbUserOperations = new DBUserOperations(connectionProvider);
    
    /**
     * Initializes the login screen.
     *
     * @param location  The location used to resolve relative paths for the root object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LoginButtonLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dbUserOperations.login(event, UsernameLogin.getText(), passwordLogin.getText());
            }
        });

        SignUpButtonLogIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtilities.changeScene(event, "SignUp.fxml", "Orca University: Sign Up", null, null, null);
            }
        });
    }
    
    /**
     * Handles the login button click event.
     *
     * @param e The action event triggered by the login button.
     */
    public void loginButtonOnAction(ActionEvent e) {
        LoginButtonLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dbUserOperations.login(event, UsernameLogin.getText(), passwordLogin.getText());
            }
        });

        if (UsernameLogin.getText().isBlank() == false && passwordLogin.getText().isBlank() == false) {
            ErrorLabel.setText("Wrong username or password!");
        } else {
            ErrorLabel.setText("Please enter username and password");
        }
    }
    
    /**
     * Handles the cancel button click event.
     *
     * @param e The action event triggered by the cancel button.
     */
    public void CancelButtonLoginOnAction(ActionEvent e) {
        Stage stage = (Stage) CancelButtonLogin.getScene().getWindow();
        stage.close();
    }
    
}
//The class contains fields annotated with @FXML, which are references to UI elements defined in the corresponding FXML file.
//The DBUserOperations method sets the connectionProvider field with the provided DatabaseConnection instance.
//The initialize method is invoked when the login screen is initialized. It sets up event handlers for the login button and sign-up button.
//The loginButtonOnAction method handles the login button click event. It checks if the username and password fields are filled. If not, it displays an error message.
//The CancelButtonLoginOnAction method handles the cancel button click event. It closes the login screen window.
//The class interacts with the DBUserOperations instance to perform login functionality, passing the entered username and password.
//This code assumes the existence of a DatabaseConnection class and a DBUserOperations class, which handle the database connection and user operations, respectively. Please ensure these classes are implemented correctly.
//Make sure to define