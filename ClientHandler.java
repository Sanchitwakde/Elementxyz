import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<ClientHandler> clients;

    public ClientHandler(Socket socket, ArrayList<ClientHandler> clients) {
        this.socket = socket;
        this.clients = clients;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println("Welcome to the chat!");

        } catch (IOException e) {
            System.out.println("ClientHandler Error: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        String message;

        try {
            while ((message = in.readLine()) != null) {
                broadcast(message);
            }

        } catch (IOException e) {
            System.out.println("Client disconnected.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) { }
        }
    }

    private void broadcast(String msg) {
        for (ClientHandler client : clients) {
            client.out.println(msg);
        }
    }
}
