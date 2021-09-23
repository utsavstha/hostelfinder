package com.hostelfinder.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hostelfinder.app.adapter.CommentAdapter;
import com.hostelfinder.app.adapter.TabsAdapter;
import com.hostelfinder.app.model.Conversation;
import com.hostelfinder.app.model.Hostel;
import com.hostelfinder.app.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends BaseActivity {
    RecyclerView rvReview;
    private CommentAdapter commentAdapter;
    private List<Review> reviews;
    @Override
    protected int getView() {
        return R.layout.activity_review;
    }

    @Override
    protected int getToolbar() {
        return R.id.toolbar;
    }

    @Override
    protected void setTag() {

    }

    @Override
    protected void initView() {
        rvReview = findViewById(R.id.rvReviews);
        reviews = new ArrayList<>();

        commentAdapter = new CommentAdapter(reviews);
        rvReview.setLayoutManager(new LinearLayoutManager(this));
        rvReview.setAdapter(commentAdapter);


       fetchData();

    }

    private void fetchData() {
        CollectionReference hostelRef = db.collection("hostel");
        hostelRef.whereEqualTo("uid", mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("tag", document.getId() + " => " + document.getData());
                                reviews.clear();
                                Hostel hostel = document.toObject(Hostel.class);
                                reviews.addAll(hostel.reviews);
                                commentAdapter.notifyDataSetChanged();


                            }
                        } else {
                            Log.d("tag", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    protected boolean validate() {
        return false;
    }
}