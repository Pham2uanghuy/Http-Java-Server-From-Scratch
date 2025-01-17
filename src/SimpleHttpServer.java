import javax.xml.transform.Result;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class SimpleHttpServer {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/MyLibrary";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "12345";
    public static void main(String[] args) throws IOException {
        int port = 8080;
        String customIp = "192.168.0.107";
        try(ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName(customIp))) {
            System.out.println("Server is listening on port " + port);

            while(true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                // handle client request
                handleClient(socket);
            }
        } catch (IOException | SQLException ex) {
            System.out.println("Server exception: "+ ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) throws IOException, SQLException {
        try(InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream()) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;

            while((line = reader.readLine())!=null && !line.isEmpty()) {
                System.out.println(line);
            }
            String books = fetchBooksFromDatabase();
            String httpResponse = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "Content-Length: " + books.length() + "\r\n" +
                    "\r\n" +
                    books;

            out.write(httpResponse.getBytes());
            out.flush();
        } catch(IOException ex) {
            System.out.println("Client handler exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String fetchBooksFromDatabase() throws IOException, SQLException {
        StringBuilder result = new StringBuilder();
        try(Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT title, author FROM books");

            while(resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                result.append("Title: ").append(title).append(", Author: ").append(author).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.append("error fetching data from database: ").append(e.getMessage());
        }

        return result.toString();
    }
}

