package edu.school21.sockets.utils;

import com.beust.jcommander.Parameter;
import org.springframework.stereotype.Component;


@Component
public class Parametr {
    @Parameter(names = "--port")
    public int port;

}
