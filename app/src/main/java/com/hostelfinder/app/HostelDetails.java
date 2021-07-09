package com.hostelfinder.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class HostelDetails extends AppCompatActivity {

    Hostel hostel;
    TextView hostelName, rate, rating, address, rooms, description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel_details);
        hostel = getIntent().getExtras().getParcelable("hostel");
        ImageView hostelImage = findViewById(R.id.imgHostel);
        Picasso.get()
                .load(hostel.getImage())
                .into(hostelImage);

        hostelName = findViewById(R.id.txtHostelName);
        rate = findViewById(R.id.txtHostelRate);
        rating = findViewById(R.id.txtRating);
        address = findViewById(R.id.tvAddress);
        rooms = findViewById(R.id.tvRooms);
        description = findViewById(R.id.tvDescription);

        hostelName.setText(hostel.getName());
//        rate.setText(String.valueOf(hostel.getRate()));
//        rating.setText(String.valueOf(hostel.getRating()));
        address.setText(hostel.getAddress());
//        rooms.setText(String.valueOf(hostel.getAvailableRooms()));
//        description.setText(hostel.getDescription());

        findViewById(R.id.ivBack).setOnClickListener(v->{
            finish();
        });
    }

    public void book(View view) {
    }
}