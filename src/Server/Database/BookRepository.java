package Server.Database;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class BookRepository {
    public static String getAllBooks() {
        StringBuilder result = new StringBuilder();
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT title, author FROM books")) {

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                result.append("Title: ").append(title).append(", Author: ").append(author).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.append("Error fetching data from database: ").append(e.getMessage());
        }
        return result.toString();
    }

    public static  void main(String[] args) {
        System.out.println(getAllBooks());
    }
}