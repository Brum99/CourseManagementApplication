package application;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TimeTableController implements Initializable {
    // Instance variables for user information
    private String firstName;
    private String lastName;

    // FXML elements
    @FXML
    private Button LogOutLoggedIn;

    @FXML
    private Label WelcomeMessageLoggedIn;

    @FXML
    private MenuItem ShowCoursesMenuItem;

    @FXML
    private MenuItem EditProfileButton;

    @FXML
    private MenuItem EnrollmentsMenuItem;

    @FXML
    private TableView<TimetableRowModel> timetableTableView;

    @FXML
    private TableColumn<TimetableRowModel, String> timeColumn;

    @FXML
    private TableColumn<TimetableRowModel, String> mondayColumn;

    @FXML
    private TableColumn<TimetableRowModel, String> tuesdayColumn;

    @FXML
    private TableColumn<TimetableRowModel, String> wednesdayColumn;

    @FXML
    private TableColumn<TimetableRowModel, String> thursdayColumn;

    @FXML
    private TableColumn<TimetableRowModel, String> fridayColumn;

    // Initialize the controller with user information
    public void setUserInformation(String firstName, String lastName, String studentNumber) {
        WelcomeMessageLoggedIn.setText(firstName + " " + lastName + " s" + studentNumber);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Initialization logic when the view is loaded
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set event handlers for buttons and menu items
        LogOutLoggedIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtilities.changeScene(event, "LogIn.fxml", "Orca University: Log In", null, null, null);
            }
        });

        ShowCoursesMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtilities.changeSceneForMenuItem(event, "CourseDisplay.fxml", "Orca University: Course Display",
                        firstName, lastName, Student.getInstance().getStudentNumber());
            }
        });

        EditProfileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtilities.changeSceneForMenuItem(event, "EditProfile.fxml", "Orca University: Edit Profile",
                        firstName, lastName, Student.getInstance().getStudentNumber());
            }
        });

        EnrollmentsMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtilities.changeSceneForMenuItem(event, "Enrollments.fxml", "Orca University: Enrollments",
                        firstName, lastName, Student.getInstance().getStudentNumber());
            }
        });

        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        mondayColumn.setCellValueFactory(new PropertyValueFactory<>("monday"));
        tuesdayColumn.setCellValueFactory(new PropertyValueFactory<>("tuesday"));
        wednesdayColumn.setCellValueFactory(new PropertyValueFactory<>("wednesday"));
        thursdayColumn.setCellValueFactory(new PropertyValueFactory<>("thursday"));
        fridayColumn.setCellValueFactory(new PropertyValueFactory<>("friday"));

        // Create and add rows to the table view
        ObservableList<TimetableRowModel> timetableRows = FXCollections.observableArrayList();

        // Create time value variables
        int startHour = 8;
        int startMinute = 0;
        int intervalMinutes = 30;

        // Create 24 empty rows with time values
        for (int i = 0; i < 24; i++) {
            String hourString = String.valueOf(startHour);
            String minuteString = startMinute == 0 ? "00" : String.valueOf(startMinute);
            String time = hourString + ":" + minuteString;
            timetableRows.add(new TimetableRowModel(time));

            // Update time values for the next row
            startMinute += intervalMinutes;
            if (startMinute >= 60) {
                startHour++;
                startMinute = 0;
            }
        }

        // Bind the timetableRows to the timetableTableView
        timetableTableView.setItems(timetableRows);

        // Connect to the database
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDBconnection();

        // Query the enrolled courses for the user
        String enrolledCoursesQuery = "SELECT CourseName, DayOfLecture, TimeOfLecture, DurationOfLecture FROM Courses "
                + "INNER JOIN Enrollments ON Courses.CourseName = Enrollments.CourseId " + "WHERE Enrollments.UserId = ?";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(enrolledCoursesQuery);
            preparedStatement.setString(1, Student.getInstance().getStudentNumber());

            ResultSet queryOutput = preparedStatement.executeQuery();

            // Process the query results and update the timetableRows
            while (queryOutput.next()) {
                String queryCourseName = queryOutput.getString("CourseName");
                String queryDayOfLecture = queryOutput.getString("DayOfLecture");
                String queryTimeOfLecture = queryOutput.getString("TimeOfLecture");
                double queryDurationOfLecture = queryOutput.getDouble("DurationOfLecture");

                // Find the row index based on the time of lecture
                int rowIndex = -1;
                for (int i = 0; i < timetableRows.size(); i++) {
                    TimetableRowModel rowModel = timetableRows.get(i);
                    if (rowModel.getTime().equals(queryTimeOfLecture)) {
                        rowIndex = i;
                        break;
                    }
                }

                // Find the column index based on the day of lecture
                int columnIndex = -1;
                switch (queryDayOfLecture) {
                    case "Monday":
                        columnIndex = 1; // Set the column index for Monday
                        break;
                    case "Tuesday":
                        columnIndex = 2; // Set the column index for Tuesday
                        break;
                    case "Wednesday":
                        columnIndex = 3; // Set the column index for Wednesday
                        break;
                    case "Thursday":
                        columnIndex = 4; // Set the column index for Thursday
                        break;
                    case "Friday":
                        columnIndex = 5; // Set the column index for Friday
                        break;
                    
                }

                // Add the course name to the appropriate rows and columns for the duration
                if (rowIndex != -1 && columnIndex != -1) {
                    for (int i = 0; i < (queryDurationOfLecture * 60) / 30; i++) {
                        int currentRowIndex = rowIndex + i;
                        if (currentRowIndex >= timetableRows.size()) {
                            break; // Skip if the row index exceeds the table size
                        }
                        TimetableRowModel currentRowModel = timetableRows.get(currentRowIndex);
                        switch (columnIndex) {
                            case 1:
                                currentRowModel.setMonday(queryCourseName);
                                break;
                            case 2:
                                currentRowModel.setTuesday(queryCourseName);
                                break;
                            case 3:
                                currentRowModel.setWednesday(queryCourseName);
                                break;
                            case 4:
                                currentRowModel.setThursday(queryCourseName);
                                break;
                            case 5:
                                currentRowModel.setFriday(queryCourseName);
                                break;
                           
                        }
                    }
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(TimeTableController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }
}
//The TimeTableController class manages the time table view in the application. It handles events and displays the schedule of courses for the user.
//
//The instance variables firstName and lastName store the user's first name and last name, respectively.
//The @FXML annotations are used to inject UI components from the corresponding FXML file.
//The setUserInformation method sets the user's information in the welcome label.
//The initialize method is called when the view is loaded. It sets up event handlers for buttons and menu items, initializes table columns, creates time rows, and retrieves the user's enrolled courses from the database.
//The timetableTableView displays the timetable data using the TimetableRowModel class to represent each row.
//The generateUniqueCode method generates a unique student number for new sign-ups.
//The changeScene method is used to navigate to different views in the application.
//The DatabaseConnection class handles the connection to the database.