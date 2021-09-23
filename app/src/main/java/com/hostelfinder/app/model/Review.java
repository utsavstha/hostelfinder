package com.hostelfinder.app.model;

import com.google.firebase.firestore.PropertyName;

public class Review {
    @PropertyName("sender")
    public String sender;

    @PropertyName("review")
    public String review;
}
