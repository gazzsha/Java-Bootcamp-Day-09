package edu.school21.sockets.app;

import edu.school21.sockets.server.Server;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@ComponentScan("edu.school21.sockets")
@Component
public class Main {


    public static void main(String[] args) throws IOException {

        PropertySource theSource = new SimpleCommandLinePropertySource(args);
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();
        context.getEnvironment().getPropertySources().addFirst(theSource);
        context.register(Main.class);
        context.refresh();
        Server server = context.getBean("server", Server.class);
        server.init();
    }
}