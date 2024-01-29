package application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class StudentNumberGenerator {
    private static final String DB_URL = "jdbc:sqlite::resource:EnrollmentProgramdb.db";

    public static void main(String[] args) {
        String uniqueCode = generateUniqueCode();
        System.out.println("Generated Unique Code: " + uniqueCode);
    }

    public static String generateUniqueCode() {
        Random random = new Random();
        String code = null; // Initialize code to null
        int count = 0;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            do {
                // Generate a 7-digit random code
                code = String.format("%07d", random.nextInt(10000000));

                // Check if the code already exists in the database
                String query = "SELECT COUNT(*) FROM Users WHERE StudentNumber = '" + code + "'";
                ResultSet resultSet = stmt.executeQuery(query);
                count = resultSet.getInt(1);

                // Continue generating a new code if it already exists in the database
            } while (count > 0);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return code;
    }
}
//The class StudentNumberGenerator contains a public static method generateUniqueCode() which returns a unique student code.
//
//The method uses a Random object to generate a random number between 0 and 9,999,999, and then formats it as a 7-digit string using String.format().
//
//It establishes a connection to the SQLite database specified by the DB_URL constant.
//
//Inside a do-while loop, it executes a SQL query to check if the generated code already exists in the database. The query counts the number of records in the Users table with a matching student number.
//
//If the count is greater than 0, indicating that the code already exists, the loop generates a new code and repeats the process until a unique code is found.
//
//Once a unique code is generated, it is returned by the method.
//
//The main method demonstrates the usage of the generateUniqueCode() method by generating and printing a unique code to the console.