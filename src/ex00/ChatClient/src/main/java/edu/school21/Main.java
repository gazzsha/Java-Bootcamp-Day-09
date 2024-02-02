package edu.school21;

import com.beust.jcommander.JCommander;
import edu.school21.utils.Parametr;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;

@Component
@ComponentScan("edu.school")

public class Main {
    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Main.class);
        Parametr parametr = context.getBean("parametr", Parametr.class);
        JCommander.newBuilder()
                .addObject(parametr)
                .build()
                .parse(args);
        try (Socket clientSocket = new Socket("localhost", parametr.port);
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));) {
            String method = reader.readLine();
            out.write(method);
            out.flush();
            String serverLine = in.readLine();
            System.out.println(serverLine);
            String username = reader.readLine();
            out.write(username);
            out.flush();
            serverLine = in.readLine();
            System.out.println(serverLine);
            String password = reader.readLine();
            out.write(password);
            out.flush();
            serverLine = in.readLine();
            System.out.println(serverLine);
        }

    }
}