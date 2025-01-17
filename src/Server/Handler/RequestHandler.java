package Server.Handler;

import Server.Database.BookRepository;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.sql.SQLException;

public class RequestHandler {
    public static void handleClient(Socket socket) throws IOException, SQLException {
        try(InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while((line = reader.readLine())!=null && !line.isEmpty()) {
                System.out.println(line);
            }

            // get data from db
            String books = BookRepository.getAllBooks();

            // create http response
            String httpResponse = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "Content-Length: " + books.length() + "\r\n" +
                    "\r\n" +
                    books;
            out.write(httpResponse.getBytes());
            out.flush();
            System.out.println("sent package to client");

        } catch (IOException ex) {
            System.out.println("Request handler exception: " + ex.getMessage());
        } finally {
            socket.close();
        }
    }

}
