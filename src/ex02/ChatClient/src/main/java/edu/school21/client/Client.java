package edu.school21.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.school21.message.MessageJSON;
import edu.school21.message.MessageJsonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

@Component
public class Client {


    @Autowired
    ObjectMapper objectMapper;

    @Value("${server-port}")
    private String port;

    public void init() throws IOException {
        try (Socket clientSocket = new Socket("localhost", Integer.parseInt(port));
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));) {
            String helloFromServer = null;
            String method = null;
//            for (helloFromServer = in.readLine(); in.lines().; helloFromServer = in.readLine())
//            {
//                System.out.println(helloFromServer);
//            }
//            in.lines().filter(line -> !line.isEmpty()) //Filtering empty lines
//                    .limit(4)
//                    .forEach(System.out::println);
//            method = reader.readLine();
//            out.write(method + '\n');
//            out.flush();
//            if (Integer.parseInt(method) == 3) clientSocket.close();
//            String serverLine = in.readLine();
//            System.out.println(serverLine);
//            String username = reader.readLine();
//            out.write(username + '\n');
//            out.flush();
//            serverLine = in.readLine();
//            System.out.println(serverLine);
//            String password = reader.readLine();
//            out.write(password + '\n');
//            out.flush();
//            in.lines().filter(line -> !line.isEmpty()) //Filtering empty lines
//                    .limit(3)
//                    .forEach(System.out::println);
//            serverLine = in.readLine();
//            System.out.println(serverLine);
//            method = reader.readLine();
//            out.write(method + '\n');
//            out.flush();
//
//            if ("Start messaging".equalsIgnoreCase(serverLine)) {
//                String userMessage = null;
//                Thread thread = new Thread(() -> {
//                    while (!Thread.currentThread().isInterrupted()) {
//                        try {
//                            String message;
//                            while ((message = in.readLine()) != null) {
//                                System.out.println(message);
//                            }
//                        } catch (IOException ignore) {
//                        }
//                    }
//                });
//                thread.start();
//                while (!"Exit".equalsIgnoreCase(userMessage)) {
//                    userMessage = reader.readLine();
//                    out.write(username + ": " + userMessage + "\n");
//                    out.flush();
//                }
//                System.out.println("You have left the chat");
//                thread.interrupt();
//            }
            String username = null;
            Boolean flagUsername = false;
            while (true) {
                for (helloFromServer = in.readLine(); !helloFromServer.equals("Stop") && !helloFromServer.equals("Exit") && !helloFromServer.equals("Chat"); helloFromServer = in.readLine()) {
                    if (helloFromServer.equals("Write username")) flagUsername = true;
                    System.out.println(helloFromServer);
                }
                if (helloFromServer.equalsIgnoreCase("Exit")) {
                    clientSocket.close();
                } else if (helloFromServer.equalsIgnoreCase("Chat")) {
                    String userMessage = null;
                    Thread thread = new Thread(() -> {
                        while (!Thread.currentThread().isInterrupted()) {
                            try {
                                String message;
                                while ((message = in.readLine()) != null) {
                                    MessageJSON messageJSON = objectMapper.readValue(message, MessageJSON.class);
                                    System.out.println(messageJSON.getUsername() + ": " + messageJSON.getMessage());
                                }
                            } catch (IOException ignore) {
                            }
                        }
                    });
                    thread.start();
                    while (!"Exit".equalsIgnoreCase(userMessage)) {
                        userMessage = reader.readLine();
                        MessageJsonClient message = new MessageJsonClient();
                        message.setUsername(username);
                        message.setMessage(userMessage);
                        String json = objectMapper.writeValueAsString(message);
                        out.write(json + "\n");
                        out.flush();
                    }
                    System.out.println("You have left the chat");
                    thread.interrupt();
                    clientSocket.close();
                    return;
                } else {
                    String userMessage = reader.readLine();
                    if (flagUsername) {
                        username = userMessage;
                        flagUsername = false;
                    }
                    out.write(userMessage + '\n');
                    out.flush();
                }
            }
        } catch (SocketException e) {
            System.out.println("Server closed connection");
        }
    }
}
