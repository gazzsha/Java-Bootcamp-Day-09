package edu.school21.sockets.server;

import edu.school21.sockets.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class Server {

    @Autowired
    private UserService userService;

    @Value("${port}")
    private String port;

    public void init() throws IOException {
        try (ServerSocket server = new ServerSocket(Integer.parseInt(port));
             Socket client = server.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))) {
            out.write("Hello from Server" + '\n');
            out.flush();
            String methodUser = in.readLine();
            if ("signUp".equalsIgnoreCase(methodUser)) {
                out.write("Write username" + '\n');
                out.flush();
                String username = in.readLine();
                out.write("Write password" + '\n');
                out.flush();
                String password = in.readLine();
                if (userService.SignUp(username, password)) {
                    out.write("Successful!" + '\n');
                } else out.write("Something went wrong..." + '\n');
                out.flush();
            } else {
                out.write("No such method, bye!");
            }
        }
    }
}
