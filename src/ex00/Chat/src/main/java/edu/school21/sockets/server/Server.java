package edu.school21.sockets.server;

import edu.school21.sockets.services.UserService;
import edu.school21.sockets.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class Server {

    @Autowired
    private UserService userService;

    public void init(int port) throws IOException {
        try (ServerSocket server = new ServerSocket(port);
             Socket client = server.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))) {
            System.out.println("Hello from Server");
            String methodUser = in.readLine();
            if ("signUp".equalsIgnoreCase(methodUser)) {
                out.write("Write username");
                out.flush();
                String username = in.readLine();
                out.write("Write password");
                out.flush();
                String password = in.readLine();
                if (userService.SignUp(username,password)) {
                    out.write("Successful!");
                } else out.write("Something went wrong...");
                out.flush();
            }
        }
    }
}
