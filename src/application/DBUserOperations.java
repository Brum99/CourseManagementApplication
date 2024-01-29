package application;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class DBUserOperations {
    private final DatabaseConnection connectionProvider;
    private static final String USERNAME_UNIQUE_QUERY = "SELECT COUNT(*) FROM Users WHERE Username = ?";

    /**
     * Constructs a DBUserOperations object with the given DatabaseConnection provider.
     *
     * @param connectionProvider the provider for obtaining database connections
     */
    public DBUserOperations(DatabaseConnection connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    /**
     * Registers a new user with the provided information.
     *
     * @param event         the action event triggering the sign-up process
     * @param FirstName     the first name of the user
     * @param LastName      the last name of the user
     * @param Username      the desired username for the user
     * @param Password      the password for the user
     * @param StudentNumber the student number of the user
     * @return true if the sign-up process is successful, false otherwise
     */
    public boolean signUpUser(ActionEvent event, String FirstName, String LastName, String Username, String Password, String StudentNumber) {
        boolean success = false;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;
        Connection connection = null;
        try {
            connection = connectionProvider.getDBconnection();

            psCheckUserExists = connection.prepareStatement("SELECT * FROM Users WHERE Username = ?");
            psCheckUserExists.setString(1, Username);
            resultSet = psCheckUserExists.executeQuery();

            if (resultSet.isBeforeFirst()) {
                System.out.println("User already exists!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use this username.");
                alert.show();
            } else {
                String hashedPassword = hashPassword(Password);
                psInsert = connection.prepareStatement("INSERT INTO Users (Username,Password,FirstName,LastName,StudentNumber) VALUES (?,?,?,?,?)");
                psInsert.setString(1, Username);
                psInsert.setString(2, hashedPassword);
                psInsert.setString(3, FirstName);
                psInsert.setString(4, LastName);
                psInsert.setString(5, StudentNumber);
                Student.getInstance().setStudentNumber(StudentNumber);
                psInsert.executeUpdate();

                DBUtilities.changeScene(event, "CourseDisplay.fxml","Orca University: Home page", FirstName, LastName, StudentNumber);
                success = true;
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to SQLite database");
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (psCheckUserExists != null) psCheckUserExists.close();
                if (psInsert != null) psInsert.close();
                DatabaseConnection.close(connection);
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    /**
     * Performs the login process with the provided credentials.
     *
     * @param event    the action event triggering the login process
     * @param Username the username of the user
     * @param Password the password of the user
     * @return true if the login is successful, false otherwise
     */
    public boolean login(ActionEvent event, String Username, String Password) {
        boolean success = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionProvider.getDBconnection();

            preparedStatement = connection.prepareStatement("SELECT Password, FirstName, LastName, StudentNumber FROM Users WHERE Username = ?");
            preparedStatement.setString(1, Username);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found in the database!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect!");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String retrievedHashedPassword = resultSet.getString("Password");
                    String hashedInputPassword = hashPassword(Password);

                    if (retrievedHashedPassword.equals(hashedInputPassword)) {
                        String FirstName = resultSet.getString("FirstName");
                        String LastName = resultSet.getString("LastName");
                        String StudentNumber = resultSet.getString("StudentNumber");
                        Student.getInstance().setStudentNumber(StudentNumber);
                        DBUtilities.changeScene(event, "CourseDisplay.fxml", "Orca University: Home Page", FirstName, LastName, StudentNumber);
                        success = true;
                    } else {
                        System.out.println("Passwords did not match!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The provided credentials are incorrect!");
                        alert.show();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                DatabaseConnection.close(connection);
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    /**
     * Hashes the given password using SHA-256 algorithm.
     *
     * @param password the password to be hashed
     * @return the hashed password
     */
    public static String hashPassword(String password) {
        byte[] digest = getSha256Digest(password);
        return encodeBase64(digest);
    }

    private static byte[] getSha256Digest(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error initializing SHA-256", e);
        }
    }

    private static String encodeBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * Checks if the given username is unique.
     *
     * @param username the username to be checked
     * @return true if the username is unique, false otherwise
     */
    public boolean isUsernameUnique(String username) {
        try (Connection connection = connectionProvider.getDBconnection();
             PreparedStatement statement = connection.prepareStatement(USERNAME_UNIQUE_QUERY)) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() && rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            Logger.getLogger(DBUtilities.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates the user's details with the provided values.
     *
     * @param StudentNumber the student number of the user
     * @param newFirstName  the new first name of the user (or null if not to be updated)
     * @param newLastName   the new last name of the user (or null if not to be updated)
     * @param newUsername   the new username of the user (or null if not to be updated)
     * @param newPassword   the new password of the user (or null if not to be updated)
     * @return true if any field is updated, false otherwise
     */
    public boolean updateUserDetails(String StudentNumber, String newFirstName, String newLastName, String newUsername, String newPassword) {
        boolean isUpdated = false;
        String studentNumber = Student.getInstance().getStudentNumber();

        try (Connection connection = connectionProvider.getDBconnection()) {
            if (newFirstName != null) {
                isUpdated = updateField(connection, "FirstName", newFirstName, studentNumber) || isUpdated;
            }
            if (newLastName != null) {
                isUpdated = updateField(connection, "LastName", newLastName, studentNumber) || isUpdated;
            }
            if (newUsername != null) {
                isUpdated = updateField(connection, "Username", newUsername, studentNumber) || isUpdated;
            }
            if (newPassword != null) {
                String hashedNewPassword = hashPassword(newPassword);
                isUpdated = updateField(connection, "Password", hashedNewPassword, studentNumber) || isUpdated;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isUpdated;
    }

    private static boolean updateField(Connection connection, String fieldName, String newValue, String studentNumber) throws SQLException {
        String sql = String.format("UPDATE Users SET %s = ? WHERE StudentNumber = ?", fieldName);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newValue);
            preparedStatement.setString(2, studentNumber);
            return preparedStatement.executeUpdate() > 0;
        }
    }
}
//The DBUserOperations class is responsible for handling user operations such as user sign-up, login, and updating user details. 
//
//signUpUser: Registers a new user with the provided information. It checks if the username already exists in the database before creating a new user.
//login: Performs the login process by verifying the provided username and password against the database records.
//hashPassword: Hashes the given password using the SHA-256 algorithm.
//isUsernameUnique: Checks if the given username is unique in the database.
//updateUserDetails: Updates the user's details with the provided values (e.g., first name, last name, username, password).
//The class interacts with the database using the DatabaseConnection class to obtain database connections and execute SQL statements. It also utilizes utility methods to hash passwords and encode data in Base64 format.