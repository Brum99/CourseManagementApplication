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

public class EnrollmentsController implements Initializable {

    private String FirstName;
    private String LastName;

    @FXML
    private Label feedbackLabel;
    @FXML
    private Button LogOutLoggedIn;
    @FXML
    private Label WelcomeMessageLoggedIn;
    @FXML
    private TableColumn<CourseSearchModel, String> CourseNameColumn;
    @FXML
    private TableColumn<CourseSearchModel, Integer> CapacityColumn;
    @FXML
    private TableColumn<CourseSearchModel, String> YearColumn;
    @FXML
    private TableColumn<CourseSearchModel, String> DeliveryModeColumn;
    @FXML
    private TableColumn<CourseSearchModel, String> DayOfLectureColumn;
    @FXML
    private TableColumn<CourseSearchModel, String> TimeOfLectureColumn;
    @FXML
    private TableColumn<CourseSearchModel, String> DurationOfLectureColumn;
    @FXML
    private MenuItem ShowCoursesMenuItem;
    @FXML
    private MenuItem EditProfileButton;
    @FXML
    private TableView<CourseSearchModel> courseTableView;
    @FXML
    private MenuItem TimeTableMenuItem;
    @FXML
    private MenuItem ExportEnrolledCourses;

    ObservableList<CourseSearchModel> courseSearchModelObservableList = FXCollections.observableArrayList();

    /**
     * Sets the user information (first name, last name) in the controller and displays it on the UI.
     *
     * @param FirstName     The first name of the user.
     * @param LastName      The last name of the user.
     * @param StudentNumber The student number of the user.
     */
    public void setUserInformation(String FirstName, String LastName, String StudentNumber) {
        WelcomeMessageLoggedIn.setText(FirstName + " " + LastName + " s" + Student.getInstance().getStudentNumber());
        this.FirstName = FirstName;
        this.LastName = LastName;
    }

    /**
     * Initializes the controller and sets up event handlers for UI components.
     *
     * @param arg0      The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param arg1      The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Event handler for Log Out button
        LogOutLoggedIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Call the DBUtilities class method to change the scene to the LogIn.fxml file
                DBUtilities.changeScene(event, "LogIn.fxml", "Orca University: Log In", null, null, null);
            }
        });

        // Event handler for Show Courses menu item
        ShowCoursesMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Call the DBUtilities class method to change the scene to the CourseDisplay.fxml file
                DBUtilities.changeSceneForMenuItem(event, "CourseDisplay.fxml", "Orca University: Course Display", FirstName, LastName, Student.getInstance().getStudentNumber());
            }
        });

        // Event handler for Edit Profile menu item
        EditProfileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Call the DBUtilities class method to change the scene to the EditProfile.fxml file
                DBUtilities.changeSceneForMenuItem(event, "EditProfile.fxml", "Orca University: Edit Profile", FirstName, LastName, Student.getInstance().getStudentNumber());
            }
        });

        // Event handler for double-clicking on a course in the table view (withdraw course)
        courseTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (!courseTableView.getSelectionModel().isEmpty())) {
                withdrawCourse();
            }
        });

        // Event handler for Timetable menu item
        TimeTableMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Call the DBUtilities class method to change the scene to the Timetable.fxml file
                DBUtilities.changeSceneForMenuItem(event, "Timetable.fxml", "Orca University: Timetable", FirstName, LastName, Student.getInstance().getStudentNumber());
            }

        });

        // Event handler for Export Enrolled Courses menu item
        ExportEnrolledCourses.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Call the HandleExportEnrolledCourses class method to export enrolled courses
                HandleExportEnrolledCourses.exportEnrolledCourses(Student.getInstance().getStudentNumber());
            }
        });

        // Database connection and query to retrieve enrolled courses for the user
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDBconnection();

        String enrolledCoursesQuery = "SELECT CourseName, Capacity, Year, DeliveryMode, DayOfLecture, TimeOfLecture, DurationOfLecture, TimeInMinutes, DurationOfLectureInMinutes FROM Courses "
                + "INNER JOIN Enrollments ON Courses.CourseName = Enrollments.CourseId "
                + "WHERE Enrollments.UserId = ?";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(enrolledCoursesQuery);
            preparedStatement.setString(1, Student.getInstance().getStudentNumber());

            ResultSet queryOutput = preparedStatement.executeQuery();

            // Populate the observable list with the retrieved courses
            while (queryOutput.next()) {
                String queryCourseName = queryOutput.getString("CourseName");
                Integer queryCapacity = queryOutput.getInt("Capacity");
                String queryYear = queryOutput.getString("Year");
                String queryDeliveryMode = queryOutput.getString("DeliveryMode");
                String queryDayOfLecture = queryOutput.getString("DayOfLecture");
                String queryTimeOfLecture = queryOutput.getString("TimeOfLecture");
                String queryDurationOfLecture = queryOutput.getString("DurationOfLecture");
                int queryTimeInMinutes = queryOutput.getInt("TimeInMinutes");
                int queryDurationOfLectureInMinutes = queryOutput.getInt("DurationOfLectureInMinutes");
                courseSearchModelObservableList.add(new CourseSearchModel(queryCourseName, queryCapacity, queryYear, queryDeliveryMode, queryDayOfLecture, queryTimeOfLecture, queryDurationOfLecture, queryTimeInMinutes, queryDurationOfLectureInMinutes));
            }

            // Bind the table columns with the respective properties in the CourseSearchModel class
            CourseNameColumn.setCellValueFactory(new PropertyValueFactory<>("CourseName"));
            CapacityColumn.setCellValueFactory(new PropertyValueFactory<>("Capacity"));
            YearColumn.setCellValueFactory(new PropertyValueFactory<>("Year"));
            DeliveryModeColumn.setCellValueFactory(new PropertyValueFactory<>("DeliveryMode"));
            DayOfLectureColumn.setCellValueFactory(new PropertyValueFactory<>("DayOfLecture"));
            TimeOfLectureColumn.setCellValueFactory(new PropertyValueFactory<>("TimeOfLecture"));
            DurationOfLectureColumn.setCellValueFactory(new PropertyValueFactory<>("DurationOfLecture"));

            // Set the items of the table view to the populated observable list
            courseTableView.setItems(courseSearchModelObservableList);
        } catch (SQLException e) {
            Logger.getLogger(EnrollmentsController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }

    /**
     * Withdraws a selected course from the enrolled courses.
     * Updates the database and refreshes the table view.
     */
    private void withdrawCourse() {
        // Get the selected course from the table view
        CourseSearchModel selectedCourse = courseTableView.getSelectionModel().getSelectedItem();

        if (selectedCourse != null) {
            String selectedCourseName = selectedCourse.getCourseName();
            String selectedCourseDeliveryMode = selectedCourse.getDeliveryMode();
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getDBconnection();

            String withdrawalQuery = "DELETE FROM Enrollments WHERE CourseId = ? AND UserId = ?";
            String increaseCapacityQuery = "UPDATE Courses SET Capacity = Capacity + 1 WHERE CourseName = ? AND DeliveryMode <> 'Online'";

            try {
                // Start of transaction
                connectDB.setAutoCommit(false);

                // Execute withdrawal
                PreparedStatement withdrawalStatement = connectDB.prepareStatement(withdrawalQuery);
                withdrawalStatement.setString(1, selectedCourseName);
                withdrawalStatement.setString(2, Student.getInstance().getStudentNumber());

                int rowsAffected = withdrawalStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // Only update capacity for non-online courses
                    if (!selectedCourseDeliveryMode.equalsIgnoreCase("Online")) {
                        // Execute capacity increase
                        PreparedStatement increaseCapacityStatement = connectDB.prepareStatement(increaseCapacityQuery);
                        increaseCapacityStatement.setString(1, selectedCourseName);

                        int rowsAffectedCapacity = increaseCapacityStatement.executeUpdate();

                        if (rowsAffectedCapacity > 0) {
                            // Commit the transaction if both operations were successful
                            connectDB.commit();
                        } else {
                            // If the capacity update fails, roll back the transaction
                            connectDB.rollback();
                        }
                    } else {
                        // Commit the transaction if it's an online course
                        connectDB.commit();
                    }

                    // The withdrawal was successful, remove the course from the view
                    courseSearchModelObservableList.remove(selectedCourse);
                    courseTableView.refresh();
                    feedbackLabel.setText("You have withdrawn from " + selectedCourseName + "!");
                }

                // End of transaction
                connectDB.setAutoCommit(true);

            } catch (SQLException e) {
                // In case of any exception, roll back the transaction
                try {
                    connectDB.rollback();
                } catch (SQLException rollbackExc) {
                    // Log the rollback exception if any
                    Logger.getLogger(EnrollmentsController.class.getName()).log(Level.SEVERE, null, rollbackExc);
                    rollbackExc.printStackTrace();
                }

                Logger.getLogger(EnrollmentsController.class.getName()).log(Level.SEVERE, null, e);
                e.printStackTrace();
            }
        }
    }
}