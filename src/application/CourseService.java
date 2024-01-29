package application;


	

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;


public class CourseService {
		 List<CourseSearchModel> courseSearchModelList = new ArrayList<>();
		 DatabaseConnection connectNow = new DatabaseConnection();
		 Connection connectDB = connectNow.getDBconnection();
		 ObservableList<CourseSearchModel> courseSearchModelObservableList = FXCollections.observableArrayList();
		 public List<CourseSearchModel> getCourses() {
			
			//SQL query 
			String CourseViewQuery = "SELECT CourseName,Capacity,Year, DeliveryMode, DayOfLecture,TimeOfLecture,DurationOfLecture,TimeInMinutes,DurationOfLectureInMinutes FROM Courses";
			
	
			
			try {
				Statement statement = connectDB.createStatement();
				ResultSet queryOutput = statement.executeQuery(CourseViewQuery);
				while(queryOutput.next()) {
					String queryCourseName = queryOutput.getString("CourseName");
					Integer queryCapacity = queryOutput.getInt("Capacity");
					String queryYear = queryOutput.getString("Year");
					String queryDeliveryMode = queryOutput.getString("DeliveryMode");
					String queryDayOfLecture = queryOutput.getString("DayOfLecture");
					String queryTimeOfLecture = queryOutput.getString("TimeOfLecture");
					String queryDurationOfLecture = queryOutput.getString("DurationOfLecture");
					int queryTimeInMinutes = queryOutput.getInt("TimeInMinutes");
		            int queryDurationOfLectureInMinutes = queryOutput.getInt("DurationOfLectureInMinutes");
		            courseSearchModelObservableList.add(new CourseSearchModel(queryCourseName,queryCapacity,queryYear,queryDeliveryMode,queryDayOfLecture,queryTimeOfLecture,queryDurationOfLecture,queryTimeInMinutes,queryDurationOfLectureInMinutes));
					
				}
			} catch(SQLException e) {
				Logger.getLogger(CourseDisplayController.class.getName()).log(Level.SEVERE, null, e);
				e.printStackTrace();
			}
			return courseSearchModelList;
			
			}
			private boolean hasOverlap(String studentNumber, String courseName) {
			    DatabaseConnection connectNow = new DatabaseConnection();
			    Connection connectDB = connectNow.getDBconnection();
			    String overlapQuery = "SELECT COUNT(*) AS overlapCount " +
			                          "FROM Enrollments E " +
			                          "JOIN Courses C1 ON E.CourseId = C1.CourseName " +
			                          "JOIN Courses C2 ON ? = C2.CourseName " +
			                          "WHERE E.UserId = ? " +
			                          "AND C1.DayOfLecture = C2.DayOfLecture " +
			                          "AND (C1.TimeInMinutes <= C2.TimeInMinutes AND C1.TimeInMinutes + C1.DurationOfLectureInMinutes > C2.TimeInMinutes " +
			                          "OR  C1.TimeInMinutes < C2.TimeInMinutes + C2.DurationOfLectureInMinutes AND C1.TimeInMinutes + C1.DurationOfLectureInMinutes >= C2.TimeInMinutes + C2.DurationOfLectureInMinutes)";
			    try (PreparedStatement statement = connectDB.prepareStatement(overlapQuery)) {
			        statement.setString(1, courseName);
			        statement.setString(2, studentNumber);
			        ResultSet rs = statement.executeQuery();
			        if (rs.next() && rs.getInt("overlapCount") > 0) {
			            return true;
			        }
			    } catch (SQLException e) {
			        Logger.getLogger(CourseDisplayController.class.getName()).log(Level.SEVERE, null, e);
			        e.printStackTrace();
			    }
			    return false;
			}
			
			public String enrollCourse(CourseSearchModel selectedCourse, TableView<CourseSearchModel> courseTableView) {
			    if (selectedCourse != null) {
			        String selectedCourseName = selectedCourse.getCourseName();
			        String selectedCourseDeliveryMode = selectedCourse.getDeliveryMode();
			        
			        // Check if student is already enrolled
			        DatabaseConnection connectNow = new DatabaseConnection();
			        Connection connectDB = connectNow.getDBconnection();
			        String checkEnrollmentQuery = "SELECT COUNT(*) FROM Enrollments WHERE CourseId = ? AND UserId = ?";
			        
			        try (PreparedStatement statement = connectDB.prepareStatement(checkEnrollmentQuery)) {
			            statement.setString(1, selectedCourseName);
			            statement.setString(2, Student.getInstance().getStudentNumber());
			            ResultSet rs = statement.executeQuery();
			            
			            if (rs.next() && rs.getInt(1) > 0) {
			                // Student is already enrolled
			                return "You are already enrolled in " + selectedCourseName + "!";
			            }
			        } catch (SQLException e) {
			            Logger.getLogger(CourseDisplayController.class.getName()).log(Level.SEVERE, null, e);
			            e.printStackTrace();
			        }

			        // Check for time overlap
			        if (hasOverlap(Student.getInstance().getStudentNumber(), selectedCourseName)) {
			            return "You are already enrolled in a course that overlaps with " + selectedCourseName + "!";
			        }

			        // Check capacity
			     // Check capacity
			        if (selectedCourse.getCapacity() > 0 || selectedCourseDeliveryMode.equalsIgnoreCase("Online")) {
			            // Reduce the capacity if not an online course
			            if (!selectedCourseDeliveryMode.equalsIgnoreCase("Online")) {
			                selectedCourse.setCapacity(selectedCourse.getCapacity() - 1);
			                // Refresh the TableView to reflect the updated capacity
			                courseTableView.refresh();

			                // Update the course capacity in the database
			                String updateCapacityQuery = "UPDATE Courses SET Capacity = ? WHERE CourseName = ?";
			                try (PreparedStatement statement = connectDB.prepareStatement(updateCapacityQuery)) {
			                    statement.setInt(1, selectedCourse.getCapacity());
			                    statement.setString(2, selectedCourseName);
			                    statement.executeUpdate();
			                } catch (SQLException e) {
			                    Logger.getLogger(CourseDisplayController.class.getName()).log(Level.SEVERE, null, e);
			                    e.printStackTrace();
			                    return "Capacity update failed for " + selectedCourseName + "!";
			                }
			            }
			            
			            // Insert enrollment record into the database
			            String enrollmentQuery = "INSERT INTO Enrollments (CourseId, UserId) VALUES (?, ?)";

			            try (PreparedStatement statement = connectDB.prepareStatement(enrollmentQuery)) {
			                statement.setString(1, selectedCourseName);
			                statement.setString(2, Student.getInstance().getStudentNumber());
			                statement.executeUpdate();

			                return "You have enrolled in " + selectedCourseName + "!";
			            } catch (SQLException e) {
			                Logger.getLogger(CourseDisplayController.class.getName()).log(Level.SEVERE, null, e);
			                e.printStackTrace();
			                return "Enrollment failed for " + selectedCourseName + "!";
			            }
			        } else {
			            // Course is full
			            return "The course " + selectedCourseName + " is full!";
			        }
			    }

			    return "Double click to enroll in a course";
			}
}
		
	
	
