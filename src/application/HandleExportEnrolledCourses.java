package application;

import java.sql.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HandleExportEnrolledCourses {
    
    // File path for the exported enrolled courses text file
    private static final String FILE_PATH = "C:\\Users\\User\\eclipse-workspace\\Assingment2SceneBuilder\\resources\\enrolled_courses" + Student.getInstance().getStudentNumber() + ".txt"; //Change this as per your requirements
    
    /**
     * Exports the enrolled courses of a user to a text file.
     * 
     * @param userId the ID of the user
     */
    public static void exportEnrolledCourses(String userId) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDBconnection();

        String enrolledCoursesQuery = "SELECT FirstName, LastName, StudentNumber, CourseName, Capacity, Year, DeliveryMode, DayOfLecture, TimeOfLecture, DurationOfLecture "
                                    + "FROM Courses "
                                    + "INNER JOIN Enrollments ON CourseName = CourseId "
                                    + "INNER JOIN Users ON UserId = StudentNumber "
                                    + "WHERE UserId = ?";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(enrolledCoursesQuery);
            preparedStatement.setString(1, userId);

            ResultSet queryOutput = preparedStatement.executeQuery();

            // Create a BufferedWriter to write to the text file
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));
            
            // Iterate over the query result and write the enrolled course details to the file
            while(queryOutput.next()) {
                writer.write("FirstName: " + queryOutput.getString("FirstName"));
                writer.write(", LastName: " + queryOutput.getString("LastName"));
                writer.write(", StudentNumber: " + queryOutput.getString("StudentNumber"));
                writer.write(", CourseName: " + queryOutput.getString("CourseName"));
                writer.write(", Capacity: " + queryOutput.getInt("Capacity"));
                writer.write(", Year: " + queryOutput.getString("Year"));
                writer.write(", DeliveryMode: " + queryOutput.getString("DeliveryMode"));
                writer.write(", DayOfLecture: " + queryOutput.getString("DayOfLecture"));
                writer.write(", TimeOfLecture: " + queryOutput.getString("TimeOfLecture"));
                writer.write(", DurationOfLecture: " + queryOutput.getString("DurationOfLecture"));
                
                writer.newLine();
            }

            // Close the writer
            writer.close();
            
        } catch(SQLException e) {
            // Log any SQL exceptions that occur
            Logger.getLogger(HandleExportEnrolledCourses.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        } catch(IOException e) {
            // Log any IO exceptions that occur
            Logger.getLogger(HandleExportEnrolledCourses.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }
}
//This class contains a static method exportEnrolledCourses, which takes a userId as a parameter and exports the enrolled courses for that user to a text file. 
//
//The method first establishes a database connection using the DatabaseConnection class.
//It prepares a SQL query that retrieves the necessary information about the enrolled courses for the specified user.
//The query is executed, and the result set is obtained.
//A BufferedWriter is created to write to the text file specified by the FILE_PATH constant.
//The result set is iterated, and the details of each enrolled course are written to the file.
//Finally, the writer is closed to ensure proper resource handling.