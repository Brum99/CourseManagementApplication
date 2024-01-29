package application;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;


public class FontSettingController implements Initializable {
	private String FirstName;
	private String LastName;
	@FXML
	private ComboBox<String> fontType; 
	@FXML 
	private Button LogOutLoggedIn;
	@FXML 
	private Label WelcomeMessageLoggedIn;
	@FXML 
	private MenuItem EnrollmentsMenuItem;
	@FXML 
	private MenuItem TimeTableMenuItem;
	@FXML
	private MenuItem ExportEnrolledCourses;

    @FXML
    private Slider fontSize;

    @FXML
    private ColorPicker fontColor;
	
	
    private List<Scene> allScenes = new ArrayList<>(); // Assuming you store all scenes here

    private Iterable<Scene> getAllScenes() {
        return allScenes;
    }
	ObservableList<CourseSearchModel> courseSearchModelObservableList = FXCollections.observableArrayList();
	
	public void setUserInformation(String FirstName, String LastName,String StudentNumber) {
		WelcomeMessageLoggedIn.setText(FirstName + " " + LastName+" s" + Student.getInstance().getStudentNumber());
		this.FirstName = FirstName;
		this.LastName = LastName;
		}
	 	// Add available font families to the ComboBox
		@Override
		public void initialize(URL url, ResourceBundle resource) {
		    fontType.getItems().addAll(Font.getFamilies());
				
		    // Add a listener to apply the selected settings immediately (optional)
		    fontType.valueProperty().addListener((observable, oldValue, newValue) -> applySettings());
		    fontSize.valueProperty().addListener((observable, oldValue, newValue) -> applySettings());
		    fontColor.valueProperty().addListener((observable, oldValue, newValue) -> applySettings());
			
			LogOutLoggedIn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					DBUtilities.changeScene(event, "LogIn.fxml", "Orca University: Log In", null, null,null);
				}
			
			});
			EnrollmentsMenuItem.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					DBUtilities.changeSceneForMenuItem(event, "Enrollments.fxml", "Orca University: Enrollments", FirstName, LastName,Student.getInstance().getStudentNumber());
				}
			});
			TimeTableMenuItem.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					DBUtilities.changeSceneForMenuItem(event, "Timetable.fxml", "Orca University: Timetable",FirstName, LastName, Student.getInstance().getStudentNumber());
				}
			});
			ExportEnrolledCourses.setOnAction(new EventHandler<ActionEvent>() {
			    @Override
			    public void handle(ActionEvent event) {
			        HandleExportEnrolledCourses.exportEnrolledCourses(Student.getInstance().getStudentNumber());
			    }
			});
}

		private void applySettings() {
		    String family = fontType.getValue();
		    double size = fontSize.getValue();
		    Color color = fontColor.getValue();

		    // Create a CSS string with the new settings
		    String css = String.format(".label { -fx-font-family: '%s'; -fx-font-size: %.0fpx; -fx-text-fill: %s; }",
		            family, size, color.toString().replace("0x", "#"));

		    try {
		        // Write the CSS string to a temporary file
		        File temp = File.createTempFile("styles", ".css");
		        temp.deleteOnExit(); // Make sure the file is deleted on exit
		        try (PrintWriter out = new PrintWriter(temp)) {
		            out.println(css);
		        }

		        // Now apply this CSS to all scenes
		        for (Scene scene : getAllScenes()) {
		            // Clear previous stylesheets
		            scene.getStylesheets().clear();

		            // Add the original stylesheet
		            scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());

		            // Add the new temporary stylesheet
		            scene.getStylesheets().add("file:///" + temp.getAbsolutePath().replace("\\", "/"));
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
}


