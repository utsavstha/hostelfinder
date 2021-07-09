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

import java.util.ArrayList;
import java.util.List;

public class HostelAdapter extends RecyclerView.Adapter<HostelAdapter.ViewHolder>{

    List<Hostel> hostelList;
    List<Hostel> filtered;
    OnHostelClickListener onHostelClickListener;

    public HostelAdapter(List<Hostel> hostelList, OnHostelClickListener onHostelClickListener) {
        this.hostelList = hostelList;
        this.filtered = hostelList;
        this.onHostelClickListener = onHostelClickListener;
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
        holder.bindView(filtered.get(position));
    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }

    public void filter(String s){
        List<Hostel> filtered = new ArrayList<>();
        for (Hostel hostel : hostelList){
            if (hostel.name.toLowerCase().contains(s.toLowerCase())){
                filtered.add(hostel);
            }
        }
        this.filtered = filtered;
        notifyDataSetChanged();
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

            itemView.setOnClickListener(view -> {
                onHostelClickListener.onClick(getAdapterPosition());
            });
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

    public interface OnHostelClickListener{
        void onClick(int index);
    }
}
