package com.hostelfinder.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hostelfinder.app.adapter.CommentAdapter;
import com.hostelfinder.app.model.Chat;
import com.hostelfinder.app.model.Conversation;
import com.hostelfinder.app.model.Hostel;
import com.hostelfinder.app.model.Review;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HostelDetails extends BaseActivity {
    String TAG;
    Hostel hostel;
    TextView hostelName, rate, rating, address, rooms, description;
    EditText etComment;
    private FirebaseFirestore db;
    FirebaseUser user;
    private RecyclerView rvComments;
    private CommentAdapter commentAdapter;
    private String documentId;
    private List<Review> reviews;

    @Override
    protected int getView() {
        return R.layout.activity_hostel_details;
    }

    @Override
    protected int getToolbar() {
        return -1;
    }

    @Override
    protected void setTag() {
        TAG = HostelDetails.class.getSimpleName();
    }

    @Override
    protected void initView() {
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reviews = new ArrayList<>();
        hostel = AppCache.getHostel();
        documentId = getIntent().getExtras().getString("uid");
        ImageView hostelImage = findViewById(R.id.imgHostel);
        if (hostel.getImage() != null && !hostel.getImage().isEmpty() && hostel.getImage().size() > 0) {
            Picasso.get()
                    .load(hostel.getImage().get(0))
                    .resize(800, 800).centerCrop()
                    .onlyScaleDown() // the image will only be resized if it's bigger than 6000x2000 pixels.
                    .into(hostelImage);
        }


        hostelName = findViewById(R.id.txtHostelName);
        rate = findViewById(R.id.txtHostelRate);
        rating = findViewById(R.id.txtRating);
        address = findViewById(R.id.tvAddress);
        rooms = findViewById(R.id.tvRooms);
        description = findViewById(R.id.tvDescription);
        etComment = findViewById(R.id.etComment);
        rvComments = findViewById(R.id.rvComments);

        hostelName.setText(hostel.getName());
        rate.setText("NPR. " + hostel.getRate());
//        rating.setText(String.valueOf(hostel.getRating()));
        address.setText(hostel.getAddress());
        rooms.setText(String.valueOf(hostel.getAvailableRooms()));
        description.setText(hostel.getDescription());


        commentAdapter = new CommentAdapter(reviews);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        rvComments.setAdapter(commentAdapter);

        findViewById(R.id.ivBack).setOnClickListener(v -> {
            finish();
        });
        db.collection("hostel").document(documentId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                reviews.clear();
                reviews.addAll(value.toObject(Hostel.class).reviews);
                commentAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected boolean validate() {
        return false;
    }

    public void book(View view) {
        startConversation();
    }

    /**
     * This method will create a new conversation if a conversation doesn't exist
     * else it will navigate the user to that conversation
     */
    public void startConversation() {
        //Gets a list of conversation which has currently logged in user
        db.collection("conversations")
                .whereArrayContains("users", user.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String documentId = null, userOne = null, userTwo = null;
                    //If the fetched conversations include the user id for the seller
                    //it is assumed that a conversation exists and the user is navigated to it
                    //else a new conversation is created
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        List<String> users = (List<String>) document.getData().get("users");
                        for (int i = 0; i < users.size(); i++) {
                            if (users.get(i).equalsIgnoreCase(hostel.getPostedBy())) {
                                documentId = document.getId();
                                break;
                            }
                        }
                        if (documentId != null) {
                            userOne = users.get(0);
                            userTwo = users.get(1);
                            break;
                        }
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    if (documentId != null) {
                        Intent intent = new Intent(HostelDetails.this, ChatActivity.class);
                        intent.putExtra("conversationId", documentId);
                        intent.putExtra("userOne", userOne);
                        intent.putExtra("userTwo", userTwo);
                        startActivity(intent);

                    } else {
                        //Creates a new conversation and send a "Hello" message
                        DocumentReference conversationId = db.collection("conversations").document();
                        Map<String, Object> data = new HashMap<>(); //Map for conversation object
                        Map<String, Object> message = new HashMap<>(); //Map for chat object

                        message.put("messages", "Hello");
                        message.put("sender", user.getUid());
                        message.put("receiver", hostel.getPostedBy());
                        data.put("users", Arrays.asList(hostel.getPostedBy(), user.getUid()));
                        data.put("hostelId", hostel.documentId);
                        data.put("chat", FieldValue.arrayUnion(message)); //array union appends the chat message to already existing chat array
                        conversationId.set(data);
                        DocumentReference postedUser = db.collection("users").document(hostel.getPostedBy());
                        postedUser.update("conversations", FieldValue.arrayUnion(conversationId));
//
                        DocumentReference userDoc = db.collection("users").document(user.getUid());
                        userDoc.update("conversations", FieldValue.arrayUnion(conversationId)).addOnSuccessListener(aVoid -> {
                            //After a new conversation is created user is take to it
                            Intent intent = new Intent(HostelDetails.this, ChatActivity.class);
                            intent.putExtra("conversationId", conversationId.getId());
                            intent.putExtra("userOne", user.getUid());
                            intent.putExtra("userTwo", hostel.getPostedBy());
                            startActivity(intent);
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                }
            }
        });


    }

    public void postComment(View view) {
        if (etComment.getText().toString().isEmpty()){
            etComment.setError("Please write a review");
            return;
        }
        DocumentReference documentReference = db.collection("hostel").document(documentId);
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> message = new HashMap<>();

        message.put("sender", mAuth.getCurrentUser().getDisplayName());
        message.put("review", etComment.getText().toString());
        data.put("reviews",  FieldValue.arrayUnion(message));
        documentReference.update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    etComment.setText("");
                }
            }
        });
    }
}