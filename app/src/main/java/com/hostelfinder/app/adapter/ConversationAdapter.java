package com.hostelfinder.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.hostelfinder.app.ChatActivity;
import com.hostelfinder.app.R;
import com.hostelfinder.app.RoundedTransformation;
import com.hostelfinder.app.model.Conversation;
import com.squareup.picasso.Picasso;


import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    private List<Conversation> conversationList;
    private FirebaseFirestore db;
    private Activity context;

    public ConversationAdapter(Activity context, List<Conversation> conversationList, FirebaseFirestore db) {
        this.conversationList = conversationList;
        this.db = db;
        this.context = context;
    }

    @NonNull
    @Override
    public ConversationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_item, parent, false);

        // Passing view to ViewHolder
        return new ConversationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(conversationList.get(position));
    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvUser, tvLastMessage;
        private ImageView productImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUser = itemView.findViewById(R.id.tvUser);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            productImage = itemView.findViewById(R.id.ivProductImage);

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("userOne", conversationList.get(getAdapterPosition()).users.get(0));
                intent.putExtra("userTwo", conversationList.get(getAdapterPosition()).users.get(1));
                intent.putExtra("conversationId", conversationList.get(getAdapterPosition()).documentId);
                context.startActivity(intent);
            });
        }

        public void bindView(Conversation conversation){
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


            String sender = "";
            for (String user: conversation.getUsers()){
                if (!user.equalsIgnoreCase(currentUser.getUid())){
                    sender = user;
                }
            }

            db.collection("users").document(sender).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        tvUser.setText(document.getData().get("name").toString());
                        tvLastMessage.setText(conversation.getChat().get(conversation.getChat().size() - 1).message);
                    }
                }
            });



        }
    }
}