package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("login.fxml"));
            primaryStage.initStyle(StageStyle.DECORATED); // Set the stage style to DECORATED
            Scene scene = new Scene(root, 814, 503);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Orca University: Log In"); // Set the stage title
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The main method, which launches the JavaFX application.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
//This class serves as the entry point for the JavaFX application.
//
//The class extends the Application class, which is a base class for JavaFX applications.
//The start method is the entry point for the JavaFX application. It is automatically called when the application is launched.
//Inside the start method, the code loads the login.fxml file using the FXMLLoader class and sets it as the root of the BorderPane.
//The primaryStage is created and initialized with the StageStyle.DECORATED style, which provides the standard window decorations.
//A Scene is created with the BorderPane root and a specified width and height.
//The CSS stylesheet application.css is added to the scene.
//The scene is set on the primary stage, and the stage is given a title.
//Finally, the primary stage is displayed by calling the show method.
//The main method is the entry point for the application. It launches the JavaFX application by calling launch(args).