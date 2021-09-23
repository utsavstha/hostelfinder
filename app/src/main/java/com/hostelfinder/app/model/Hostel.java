package com.hostelfinder.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class Hostel implements Parcelable {
    @PropertyName("address")
    public String address;

    @PropertyName("description")
    public String description;

    @PropertyName("rate")
    public String rate;

    @PropertyName("reviews")
    public List<Review> reviews;

    @PropertyName("appointments")
    public List<Appointment> appointments;

    @PropertyName("availableRooms")
    public String availableRooms;

    @PropertyName("totalRoom")
    public String totalRoom;

    @PropertyName("image")
    public List<String> image;

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

    @PropertyName("warden")
    public String warden;

    @PropertyName("uid")
    public String postedBy;
    public String documentId;
    public int index;
    public Hostel() {
    }


    public String getDescription() {
        return description;
    }

    public String getRate() {
        return rate;
    }

    public String getAvailableRooms() {
        return availableRooms;
    }

    public String getAddress() {
        return address;
    }

    public List<String> getImage() {
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

    public String getWarden() {
        return warden;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Hostel createFromParcel(Parcel in) {
            return new Hostel(in);
        }

        public Hostel[] newArray(int size) {
            return new Hostel[size];
        }
    };
    public Hostel(Parcel in){
        this.address = in.readString();
        this.availableRooms = in.readString();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.postedBy = in.readString();
        this.warden = in.readString();
        this.rating = in.readFloat();

//        this.image = in.readArrayList(String.class);

        this.name = in.readString();
        this.phone = in.readString();
        this.rate = in.readString();

        this.description = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.address);
        parcel.writeString(this.availableRooms);
        parcel.writeDouble(this.lat);
        parcel.writeDouble(this.lng);
        parcel.writeString(this.postedBy);
        parcel.writeString(this.warden);
        parcel.writeDouble(this.rating);

//        parcel.writeString(this.image);

        parcel.writeString(this.name);
        parcel.writeString(this.phone);
        parcel.writeString(this.rate);

        parcel.writeString(this.description);
    }
}
