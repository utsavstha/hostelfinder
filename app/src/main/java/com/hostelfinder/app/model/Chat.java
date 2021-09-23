package com.hostelfinder.app.model;

import com.google.firebase.firestore.PropertyName;

public class Chat {
    @PropertyName("sender")
    public String sender;

    @PropertyName("receiver")
    public String receiver;

    @PropertyName("messages")
    public String message;

    public Chat() {
    }

    public Chat(String sender, String receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }
}
