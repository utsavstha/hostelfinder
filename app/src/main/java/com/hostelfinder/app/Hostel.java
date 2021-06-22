package com.hostelfinder.app;

import com.google.firebase.firestore.PropertyName;

public class Hostel {
    @PropertyName("address")
    public String address;

    @PropertyName("image")
    public String image;

    @PropertyName("lat")
    public double lat;

    @PropertyName("long")
    public double lng;

    @PropertyName("name")
    public String name;

    @PropertyName("phone")
    public String phone;

    @PropertyName("rating")
    public float rating;

    @PropertyName("wardenName")
    public String wardenName;

    public int index;
    public Hostel() {
    }

    public Hostel(String address, String image, double lat, double lng,
                  String name, String phone, float rating, String wardenName) {
        this.address = address;
        this.image = image;
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.phone = phone;
        this.rating = rating;
        this.wardenName = wardenName;
    }

    public String getAddress() {
        return address;
    }

    public String getImage() {
        return image;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public float getRating() {
        return rating;
    }

    public String getWardenName() {
        return wardenName;
    }
}
