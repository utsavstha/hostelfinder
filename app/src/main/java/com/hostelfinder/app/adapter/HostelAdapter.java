package com.hostelfinder.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hostelfinder.app.Hostel;
import com.hostelfinder.app.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HostelAdapter extends RecyclerView.Adapter<HostelAdapter.ViewHolder>{

    List<Hostel> hostelList;

    public HostelAdapter(List<Hostel> hostelList) {
        this.hostelList = hostelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hostel_item, parent, false);

        // Passing view to ViewHolder
        return new HostelAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(hostelList.get(position));
    }

    @Override
    public int getItemCount() {
        return hostelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView hostelImage;
        private TextView name, address, rating;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            hostelImage = itemView.findViewById(R.id.ivHostel);
            name = itemView.findViewById(R.id.tvHostelName);
            address = itemView.findViewById(R.id.tvAddress);
            rating = itemView.findViewById(R.id.tvRating);
        }

        public void bindView(Hostel hostel){
            Picasso.get()
                    .load(hostel.getImage())
                    .into(hostelImage);
            name.setText(hostel.name);
            address.setText(hostel.address);
            rating.setText(String.valueOf(hostel.rating));
        }
    }
}
