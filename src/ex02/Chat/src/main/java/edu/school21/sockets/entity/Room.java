package edu.school21.sockets.entity;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Room {
    private final String name;
    private final List<String> messages;

    public CopyOnWriteArrayList<BufferedWriter> listOutSockets;

    public Room(String name) {
        this.name = name;
        messages = new ArrayList<>();
        listOutSockets = new CopyOnWriteArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<String> getMessages() {
        return messages;
    }

    public List<BufferedWriter> getListOutSockets() {
        return listOutSockets;
    }
}
