import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatClientGUI {

    private JFrame frame = new JFrame("Java Chat");
    private JTextArea chatArea = new JTextArea();
    private JTextField inputField = new JTextField();
    private PrintWriter out;
    private String username;

    public ChatClientGUI() {
        username = JOptionPane.showInputDialog("Enter your username:");
        setupGUI();
        connectToServer();
    }

    private void setupGUI() {
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chatArea.setEditable(false);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 16));

        JScrollPane scrollPane = new JScrollPane(chatArea);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputField, BorderLayout.SOUTH);

        inputField.addActionListener(e -> {
            String msg = inputField.getText();
            out.println(username + ": " + msg);
            inputField.setText("");
        });

        frame.setVisible(true);
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 5000);
            out = new PrintWriter(socket.getOutputStream(), true);

            // send username first
            out.println(username);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Thread readerThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        chatArea.append(message + "\n");
                    }
                } catch (IOException e) {
                    chatArea.append("Disconnected from server.\n");
                }
            });

            readerThread.start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Unable to connect to server.");
        }
    }

    public static void main(String[] args) {
        new ChatClientGUI();
    }
}
