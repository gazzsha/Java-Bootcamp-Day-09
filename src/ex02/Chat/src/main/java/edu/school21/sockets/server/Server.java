package edu.school21.sockets.server;

import edu.school21.sockets.entity.Room;
import edu.school21.sockets.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
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

    final CopyOnWriteArrayList<Room> rooms = new CopyOnWriteArrayList<>();

    final Map<String, BufferedWriter> roomsOut = new HashMap<>();


    public void init() throws IOException {

        ExecutorService executorService = Executors.newCachedThreadPool();

        try (ServerSocket server = new ServerSocket(Integer.parseInt(port))) {

            while (true) {
                Socket clientSocket = server.accept();

                executorService.execute(() -> {
                    try (Socket socket = clientSocket) {
                        work(socket);
                    } catch (IOException e) {
                      e.printStackTrace();
                    }
                });
            }
        }
    }


    void startPage(BufferedWriter out) {
        try {
            out.write("Hello from Server\n");
            out.write("1. SignIn\n");
            out.write("2. SignUp\n");
            out.write("3. Exit\n");
            out.write("Stop\n");
            out.flush();
        } catch (IOException ignore) {
        }
    }

    void roomMenu(BufferedWriter out, BufferedReader in, Socket socket) throws IOException {
        try {
            out.write("1. Create room\n");
            out.write("2. Choose room\n");
            out.write("3. Exit\n");
            out.write("Stop\n");
            out.flush();
            String userChoose = in.readLine();
            switch (Integer.parseInt(userChoose)) {
                case 1: {
                    createRoom(in, out, socket);
                    break;
                }
                case 2: {
                    roomUserChoose(out, in, socket);
                    break;
                }
                case 3: {
                    exitSocket(socket, out);
                    break;
                }
            }
        } catch (IOException ignore) {
        }
    }

    void createRoom(BufferedReader in, BufferedWriter out, Socket socket) throws IOException {
        out.write("Name for room\n");
        out.write("Stop\n");
        out.flush();
        String name = in.readLine();
        synchronized (rooms) {
            Room room = new Room(name);
            rooms.add(room);
        }
        roomMenu(out, in, socket);
    }

    void exitSocket(Socket socket, BufferedWriter out) throws IOException {
        out.write("Exit\n");
        out.flush();
        socket.close();
    }

    void roomUserChoose(BufferedWriter out, BufferedReader in, Socket socket) throws IOException {
        int end = 0;
        for (int i = 0; i != rooms.size(); i++) {
            out.write(i + 1 + ". " + rooms.get(i).getName() + "\n");
            end = i + 1;
        }
        end++;
        out.write(end + ". " + "Exit" + "\n");
        out.write("Stop" + '\n');
        out.flush();
        String userChoose = in.readLine();
        int userChooseInt = Integer.parseInt(userChoose);
        if (userChooseInt == end) exitSocket(socket, out);
        else {
            if (rooms.get(userChooseInt - 1) != null) {
                rooms.get(userChooseInt - 1).listOutSockets.add(out);
                out.write("Chat\n");
                out.write(rooms.get(userChooseInt - 1).getName() + "\n");
                out.flush();
                echoServer(in, rooms.get(userChooseInt - 1));
            }
        }
    }

    void signInForUser(BufferedReader in, BufferedWriter out, Socket socket) throws IOException {
        out.write("Write username" + '\n');
        out.write("Stop" + '\n');
        out.flush();
        String username = in.readLine();
        out.write("Write password" + '\n');
        out.write("Stop" + '\n');
        out.flush();
        String password = in.readLine();
        if (userService.SignIn(username, password)) {
            synchronized (users) {
                users.add(out);
            }
            roomMenu(out, in, socket);
        }
    }

    void signUpForUser(BufferedWriter out, BufferedReader in) throws IOException {
        out.write("Write username" + '\n');
        out.write("Stop" + '\n');
        out.flush();
        String username = in.readLine();
        out.write("Write password" + '\n');
        out.write("Stop" + '\n');
        out.flush();
        String password = in.readLine();
        if (userService.SignUp(username, password)) {
            out.write("Successful!" + '\n');
            out.write("Stop" + '\n');
        } else out.write("Something went wrong..." + '\n');
        out.flush();
    }


    void work(Socket client) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        startPage(out);
        String userChoose = in.readLine();
        switch (Integer.parseInt(userChoose)) {
            case 1: {
                signInForUser(in, out, client);
                break;
            }
            case 2: {
                signUpForUser(out, in);
                break;

            }
            case 3: {
                exitSocket(client, out);
                break;
            }
            default: {
                break;
            }
        }
    }

    void echoServer(BufferedReader in, Room room) throws IOException {
        while (true) {
            String message = in.readLine();
            if (message != null && !room.listOutSockets.isEmpty()) {
                for (BufferedWriter out : room.listOutSockets) {
                    try {
                        out.write(message + "\n");
                        out.flush();
                    } catch (IOException ioException) {
                        out.close();
                        room.listOutSockets.remove(out);
                    }
                }
                System.out.println("Server input: " + message);
            }
        }
    }
}

