package com.hostelfinder.app.model;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class Conversation {
    @PropertyName("users")
    public List<String> users;
    @PropertyName("chat")
    public List<Chat> chat;

    @PropertyName("hostelId")
    public String hostelId;


    public String documentId;
    public Conversation() {
    }

    public Conversation(List<String> users, List<Chat> chat) {
        this.users = users;
        this.chat = chat;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public List<String> getUsers() {
        return users;
    }

    public List<Chat> getChat() {
        return chat;
    }
}
