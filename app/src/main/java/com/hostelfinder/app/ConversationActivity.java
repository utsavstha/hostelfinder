package com.hostelfinder.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hostelfinder.app.adapter.ConversationAdapter;
import com.hostelfinder.app.model.Conversation;

import java.util.ArrayList;
import java.util.List;

public class ConversationActivity extends BaseActivity {
    String TAG;
    private RecyclerView rvConversations;
    private ConversationAdapter conversationAdapter;
    private FirebaseFirestore db;
    private List<Conversation> conversationList;
    @Override
    protected int getView() {
        return R.layout.activity_conversation;
    }

    @Override
    protected int getToolbar() {
        return -1;
    }

    @Override
    protected void setTag() {
        TAG = ConversationActivity.class.getSimpleName();
    }

    @Override
    protected void initView() {
        db = FirebaseFirestore.getInstance();

        conversationList = new ArrayList<>();

        rvConversations = findViewById(R.id.rvConversations);
        conversationAdapter = new ConversationAdapter(this, conversationList, db);
        rvConversations.setLayoutManager(new LinearLayoutManager(this));
        rvConversations.setAdapter(conversationAdapter);

        getAllConversation();
    }

    private void getAllConversation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("conversations")
                .whereArrayContains("users", user.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    conversationList.clear();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Conversation conversation = document.toObject(Conversation.class);
                        conversation.documentId = document.getId();
                        conversationList.add(conversation);
                    }
                    conversationAdapter.notifyDataSetChanged();
                } else {
                }
            }
        });
    }

    @Override
    protected boolean validate() {
        return false;
    }
}