package edu.school21.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

@Component
public class Client {

    @Value("${server-port}")
    private String port;

    public void init() throws IOException {
        try (Socket clientSocket = new Socket("localhost", Integer.parseInt(port));
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));) {
            String helloFromServer = in.readLine();
            System.out.println(helloFromServer);
            String method = reader.readLine();
            out.write(method + '\n');
            out.flush();
            String serverLine = in.readLine();
            System.out.println(serverLine);
            String username = reader.readLine();
            out.write(username + '\n');
            out.flush();
            serverLine = in.readLine();
            System.out.println(serverLine);
            String password = reader.readLine();
            out.write(password + '\n');
            out.flush();
            serverLine = in.readLine();
            System.out.println(serverLine);
            if ("Start messaging".equalsIgnoreCase(serverLine)) {
                String userMessage = null;
                Thread thread = new Thread(() -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            String message;
                            while ((message = in.readLine()) != null) {
                                System.out.println(message);
                            }
                        } catch (IOException ignore) {}
                    }
                });
                thread.start();
                while (!"Exit".equalsIgnoreCase(userMessage)) {
                    userMessage = reader.readLine();
                    out.write(username + ": " + userMessage + "\n");
                    out.flush();
                }
                System.out.println("You have left the chat");
                thread.interrupt();
            }
        } catch (SocketException e) {
            System.out.println("Server closed connection");
        }
    }
}
