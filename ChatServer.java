import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    private static final int PORT = 5000;
    private static ArrayList<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Server started...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket);

                ClientHandler client = new ClientHandler(socket, clients);
                clients.add(client);

                Thread t = new Thread(client);
                t.start();
            }

        } catch (IOException e) {
            System.out.println("Server Error: " + e.getMessage());
        }
    }
}
