package Server.Database;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
public class DatabaseConnection {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/MyLibrary";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "12345";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }
}
