package edu.school21.sockets.entity;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Room {

    private int id;
    private String name;
    private CopyOnWriteArrayList<String> messages = new CopyOnWriteArrayList<>();

    public CopyOnWriteArrayList<BufferedWriter> listOutSockets = new CopyOnWriteArrayList<>();


    public Room() {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addMessage(String message) {
        if (messages.size() == 30) {
            messages.removeFirst();
            messages.add(message);
        } else messages.add(message);
    }

    public void setListOutSockets(CopyOnWriteArrayList<BufferedWriter> listOutSockets) {
        this.listOutSockets = listOutSockets;
    }
}
