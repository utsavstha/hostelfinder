package com.hostelfinder.app.model;

import com.google.firebase.firestore.PropertyName;

public class Appointment {
    @PropertyName("sender")
    public String sender;

    @PropertyName("dateTime")
    public String dateTime;

    @PropertyName("senderName")
    public String senderName;
}
