package com.hostelfinder.app;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.PropertyName;

public class Hostel implements Parcelable {
    @PropertyName("address")
    public String address;

    @PropertyName("description")
    public String description;

    @PropertyName("rate")
    public double rate;

    @PropertyName("availableRooms")
    public int availableRooms;

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

    @PropertyName("warden")
    public String warden;

    public int index;
    public Hostel() {
    }

    public Hostel(String address, String image, double lat, double lng,
                  String name, String phone, float rating, String warden) {
        this.address = address;
        this.image = image;
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.phone = phone;
        this.rating = rating;
        this.warden = warden;
    }

    public String getDescription() {
        return description;
    }

    public double getRate() {
        return rate;
    }

    public int getAvailableRooms() {
        return availableRooms;
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

    public String getWarden() {
        return warden;
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
        this.image = in.readString();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.name = in.readString();
        this.phone = in.readString();
        this.rating = in.readFloat();
        this.rate = in.readDouble();
        this.availableRooms = in.readInt();
        this.warden = in.readString();

//        this.description = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.address);
        parcel.writeString(this.image);
        parcel.writeDouble(this.lat);
        parcel.writeDouble(this.lng);
        parcel.writeString(this.name);
        parcel.writeString(this.phone);
        parcel.writeDouble(this.rating);
        parcel.writeDouble(this.rate);
        parcel.writeInt(this.availableRooms);
        parcel.writeString(this.warden);

//        parcel.writeString(this.description);
    }
}
