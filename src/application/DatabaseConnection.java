package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * The DatabaseConnection class handles the connection to the SQLite database.
 */
public class DatabaseConnection {
    /**
     * The database URL.
     */
	private static final String DB_FILE_NAME = "EnrollmentProgramdb.db";
	private static final String DB_URL = "jdbc:sqlite::resource:" + DB_FILE_NAME;

    /**
     * Establishes a connection to the SQLite database.
     *
     * @return The Connection object representing the database connection.
     */
    public Connection getDBconnection() {
        Connection connection = null;

        try {
            // Register the JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Open a connection to the database
            connection = DriverManager.getConnection(DB_URL);

        } catch (ClassNotFoundException e) {
            System.out.println("Failed to load SQLite JDBC driver.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
        }

        return connection;
    }

    /**
     * Closes the database connection.
     *
     * @param connection The Connection object to be closed.
     */
    public static void close(Connection connection) {
        // Close the connection
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

//
//Class Description:
//
//The DatabaseConnection class handles the connection to the SQLite database.
//Constants:
//
//DB_URL: The database URL for the SQLite database.
//getDBconnection() Method:
//
//Establishes a connection to the SQLite database.
//Returns the Connection object representing the database connection.
//Throws a ClassNotFoundException if the SQLite JDBC driver fails to load.
//Throws a SQLException if the connection to the database fails.
//close(Connection connection) Method:
//
//Closes the database connection.
//Accepts a Connection object as a parameter.
//Checks if the connection is not null before closing it.
//Catches any SQLException that occurs during the closing of the connection and prints the stack trace.
