package com.hostelfinder.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hostelfinder.app.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{
    List<String> images;
    ImageClick imageClick;

    public ImageAdapter(List<String> images, ImageClick imageClick) {
        this.images = images;
        this.imageClick = imageClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);

        // Passing view to ViewHolder
        return new ImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView hostelImage, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hostelImage = itemView.findViewById(R.id.ivHostelImage);
            delete = itemView.findViewById(R.id.ivDelete);
            delete.setOnClickListener(v -> {
                imageClick.onDelete(getAdapterPosition());
            });
        }

        public void bindView(String url){
            Picasso.get()
                    .load(url)
                    .into(hostelImage);
        }
    }
    public interface ImageClick{
        void onDelete(int position);
    }
}

