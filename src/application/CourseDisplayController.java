package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseDisplayController implements Initializable {
    // Variables to store user information
    private String FirstName;
    private String LastName;

    // FXML elements
    @FXML 
    private Button LogOutLoggedIn;
    @FXML 
    private Label WelcomeMessageLoggedIn;
    @FXML 
    private MenuItem EditProfileButton;
    @FXML 
    private TableView<CourseSearchModel> courseTableView;
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
    private TextField keywordTextField;
    @FXML
    private Label feedbackLabel;
    @FXML 
    private MenuItem EnrollmentsMenuItem;
    @FXML 
    private MenuItem TimeTableMenuItem;
    @FXML
    private MenuItem ExportEnrolledCourses;

    // ObservableList to store course search results
    ObservableList<CourseSearchModel> courseSearchModelObservableList = FXCollections.observableArrayList();

    // Method to set user information
    public void setUserInformation(String FirstName, String LastName, String StudentNumber) {
        WelcomeMessageLoggedIn.setText(FirstName + " " + LastName + " s" + Student.getInstance().getStudentNumber());
        this.FirstName = FirstName;
        this.LastName = LastName;
    }

    // Service class for course-related operations
    CourseService courseService = new CourseService();
    List<CourseSearchModel> Courses = courseService.getCourses();

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        // Event handler for Log Out button
        LogOutLoggedIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtilities.changeScene(event, "LogIn.fxml", "Orca University: Log In", null, null, null);
            }
        });

        // Event handler for Enrollments menu item
        EnrollmentsMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtilities.changeSceneForMenuItem(event, "Enrollments.fxml", "Orca University: Enrollments", FirstName, LastName, Student.getInstance().getStudentNumber());
            }
        });

        // Event handler for Edit Profile menu item
        EditProfileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtilities.changeSceneForMenuItem(event, "EditProfile.fxml", "Orca University: Edit Profile", FirstName, LastName, Student.getInstance().getStudentNumber());
            }
        });

        // Event handler for double-clicking on a table row (enrollment)
        courseTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (!courseTableView.getSelectionModel().isEmpty())) {
                CourseSearchModel selectedCourse = courseTableView.getSelectionModel().getSelectedItem();
                String feedbackMessage = courseService.enrollCourse(selectedCourse, courseTableView);
                feedbackLabel.setText(feedbackMessage);
            }
        });

        // Event handler for TimeTable menu item
        TimeTableMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtilities.changeSceneForMenuItem(event, "Timetable.fxml", "Orca University: Timetable", FirstName, LastName, Student.getInstance().getStudentNumber());
            }
        });

        // Event handler for ExportEnrolledCourses menu item
        ExportEnrolledCourses.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                HandleExportEnrolledCourses.exportEnrolledCourses(Student.getInstance().getStudentNumber());
            }
        });

        // Create a database connection
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDBconnection();

        // SQL query to retrieve course information
        String CourseViewQuery = "SELECT CourseName, Capacity, Year, DeliveryMode, DayOfLecture, TimeOfLecture, DurationOfLecture, TimeInMinutes, DurationOfLectureInMinutes FROM Courses";

        try {
            // Create a statement and execute the query
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(CourseViewQuery);

            // Process the query results and populate the course search model observable list
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

            // Set cell value factories for table columns
            CourseNameColumn.setCellValueFactory(new PropertyValueFactory<>("CourseName"));
            CapacityColumn.setCellValueFactory(new PropertyValueFactory<>("Capacity"));
            YearColumn.setCellValueFactory(new PropertyValueFactory<>("Year"));
            DeliveryModeColumn.setCellValueFactory(new PropertyValueFactory<>("DeliveryMode"));
            DayOfLectureColumn.setCellValueFactory(new PropertyValueFactory<>("DayOfLecture"));
            TimeOfLectureColumn.setCellValueFactory(new PropertyValueFactory<>("TimeOfLecture"));
            DurationOfLectureColumn.setCellValueFactory(new PropertyValueFactory<>("DurationOfLecture"));

            courseTableView.setItems(courseSearchModelObservableList);

            // Create a filtered list for search functionality
            FilteredList<CourseSearchModel> filteredData = new FilteredList<>(courseSearchModelObservableList, b -> true);

            // Add a listener to the search text field to apply filtering
            keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(courseSearchModel -> {
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }
                    String searchKeyword = newValue.toLowerCase();
                    if (courseSearchModel.getCourseName().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true; // Match in CourseName
                    } else if (courseSearchModel.getCapacity().toString().indexOf(searchKeyword) > -1) {
                        return true; // Match in Capacity
                    } else if (courseSearchModel.getYear().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true; // Match in Year
                    } else if (courseSearchModel.getDeliveryMode().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true; // Match in DeliveryMode
                    } else if (courseSearchModel.getDayOfLecture().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true; // Match in Day Of Lecture
                    } else if (courseSearchModel.getTimeOfLecture().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true; // Match in TimeOfLecture
                    } else if (courseSearchModel.getDurationOfLecture().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true; // Match in DurationOfLecture
                    } else {
                        return false; // No match found
                    }
                });
            });

            SortedList<CourseSearchModel> sortedData = new SortedList<>(filteredData);

            // Bind sorted result with Table view
            sortedData.comparatorProperty().bind(courseTableView.comparatorProperty());

            // Apply filtered and sorted data to the Table View
            courseTableView.setItems(sortedData);

            // Set row factory to customize table row styles
            courseTableView.setRowFactory(row -> new TableRow<CourseSearchModel>() {
                @Override
                public void updateItem(CourseSearchModel item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setStyle(""); // Default style if there's no item
                    } else {
                        // Here we check the conditions and change the background color if they are true
                        if (item.getCapacity() == 0 && item.getDeliveryMode().equals("Face-to-face")) {
                            setStyle("-fx-background-color: red;"); // Set background color to red
                        } else {
                            setStyle(""); // Default style in other cases
                        }
                    }
                }
            });
        } catch (SQLException e) {
            Logger.getLogger(CourseDisplayController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }
}
//Import Statements:
//
//The necessary JavaFX and database-related import statements are included.
//Class Declaration:
//
//The class is declared as CourseDisplayController and implements the Initializable interface.
//Instance Variables:
//
//FirstName and LastName variables are used to store user information.
//FXML elements (buttons, labels, menu items, table view, table columns, text field) are annotated with @FXML.
//ObservableList:
//
//courseSearchModelObservableList is an ObservableList that will store the course search results.
//setUserInformation Method:
//
//This method sets the user information (first name, last name) displayed in the welcome message.
//CourseService and Courses:
//
//CourseService is an instance of the service class responsible for course-related operations.
//Courses is a list that retrieves the courses from the CourseService.
//initialize Method:
//
//This method is called when the FXML view is loaded and initializes the controller.
//It sets up event handlers, retrieves course data from the database, populates the table view, and adds search and sorting functionality.
//Event Handlers:
//
//Event handlers are defined for the log out button, enrollments menu item, edit profile button, double-clicking on a table row, time table menu item, and export enrolled courses menu item.
//Each event handler calls a method to handle the corresponding action.
//Database Connection and SQL Query:
//
//A DatabaseConnection object is created to establish a connection to the database.
//The SQL query to retrieve course information is defined as CourseViewQuery.
//Query Execution and Result Processing:
//
//A statement is created using the database connection, and the query is executed.
//The result set is processed using a while loop to extract the course information.
//Each course's data is added to the courseSearchModelObservableList as a CourseSearchModel object.
//Table View and Cell Value Factories:
//Cell value factories are set for each table column to define how the data from the courseSearchModelObservableList should be displayed in the table view.
//Filtered and Sorted List:
//A FilteredList is created based on the courseSearchModelObservableList to enable search functionality.
//A listener is added to the text field to apply the filtering based on the entered search keyword.
//A SortedList is created based on the filtered data to enable sorting of the table view.
//Table Row Customization:
//The setRowFactory method is used to customize the style of table rows based on specific conditions.
//In this case, if the course capacity is 0 and the delivery mode is "Face-to-face," the row's background color is set to red.
//Exception Handling:
//Any SQLException that occurs during the execution of the database query is caught and logged.
//The stack trace is also printed.