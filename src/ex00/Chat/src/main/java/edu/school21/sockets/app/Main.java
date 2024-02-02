package edu.school21.sockets.app;

import com.beust.jcommander.JCommander;
import edu.school21.sockets.server.Server;
import edu.school21.sockets.utils.Parametr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.io.IOException;

@ComponentScan("edu.school21.sockets")
@Component
public class Main {


    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        Parametr parametr = context.getBean("parametr", Parametr.class);
        JCommander.newBuilder()
                .addObject(parametr)
                .build()
                .parse(args);
        Server server = context.getBean("server", Server.class);
        server.init(parametr.port);
    }
}