package edu.school21.sockets.message;

public class MessageJsonClient {
    private String username;
    private String message;

    public MessageJsonClient() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
