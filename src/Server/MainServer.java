package Server;

import Server.Handler.RequestHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import static Server.Handler.RequestHandler.handleClient;

public class MainServer {
    public static void main(String[] args) throws IOException {
        int port = 8081;
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
}
