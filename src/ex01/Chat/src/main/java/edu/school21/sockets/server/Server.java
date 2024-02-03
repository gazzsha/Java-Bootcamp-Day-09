package edu.school21.sockets.server;

import edu.school21.sockets.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class Server {

    @Autowired
    private UserService userService;

    @Value("${port}")
    private String port;


    final List<BufferedWriter> users = new ArrayList<>();


    private final ReentrantLock lock = new ReentrantLock();

    public void init() throws IOException {

        ExecutorService executorService = Executors.newCachedThreadPool();

        try (ServerSocket server = new ServerSocket(Integer.parseInt(port))) {

            while (true) {
                Socket clientSocket = server.accept();

                executorService.execute(() -> {
                    try (Socket socket = clientSocket) {
                        work(socket);
                    } catch (IOException e) {
                        Logger.getLogger(Server.class.getName()).log(Level.INFO, "Something went wrong...");
                    }
                });
            }
        }
    }


    void work(Socket client) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
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
        } else if ("signIn".equalsIgnoreCase(methodUser)) {
            out.write("Write username" + '\n');
            out.flush();
            String username = in.readLine();
            out.write("Write password" + '\n');
            out.flush();
            String password = in.readLine();
            if (userService.SignIn(username, password)) {
                synchronized (users) {
                    users.add(out);
                }
                out.write("Start messaging" + '\n');
                out.flush();
                echoServer(in);
            } else out.write("Something went wrong..." + '\n');
        } else {
            out.write("No such method, bye!");
            out.flush();
        }
    }

    void echoServer(BufferedReader in) throws IOException {
        while (true) {
            String message = in.readLine();
            if (message != null && !users.isEmpty()) {
                for (BufferedWriter out : users) {
                    try {
                        out.write(message + "\n");
                        out.flush();
                    } catch (IOException ioException) {
                        synchronized (users) {
                            users.remove(out);
                        }
                    }
                }
                userService.SendMessageToDataBase(message.split(":")[0],message.split(": ")[1], LocalDateTime.now());
                System.out.println("Server input: " + message);
            }
        }
    }
}

