package com.example.voluntr.ChatBox;

public class ChatBoxObject {
    private String message;
    private Boolean currentUser;
    public ChatBoxObject(String message, Boolean currentUser) {
        this.currentUser=currentUser;
        this.message=message;
    }
    public String getMessage(){return message;}
    public void setMessage(String message){this.message=message;}

    public Boolean getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Boolean currentUser) {
        this.currentUser = currentUser;
    }
}
