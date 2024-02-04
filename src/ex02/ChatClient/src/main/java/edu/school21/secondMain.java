package edu.school21;


import edu.school21.client.Client;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@ComponentScan("edu.school21")
public class secondMain {
    public static void main(String[] args) throws IOException {
        PropertySource theSource = new SimpleCommandLinePropertySource(args);
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext();
        context.getEnvironment().getPropertySources().addFirst(theSource);
        context.register(Main.class);
        context.refresh();

        Client client = context.getBean("client", Client.class);
        client.init();
    }
}