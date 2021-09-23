package com.hostelfinder.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hostelfinder.app.R;
import com.hostelfinder.app.model.Review;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    List<Review> reviews;

    public CommentAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);

        // Passing view to ViewHolder
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView sender, reviewText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sender = itemView.findViewById(R.id.etUser);
            reviewText = itemView.findViewById(R.id.etComment);
        }

        public void bind(Review review){
            sender.setText(review.sender);
            reviewText.setText(review.review);
        }
    }
}
