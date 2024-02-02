package edu.school21.utils;

import com.beust.jcommander.Parameter;
import org.springframework.stereotype.Component;


@Component
public class Parametr {
    @Parameter(names = "--server-port")
    public int port;

}
