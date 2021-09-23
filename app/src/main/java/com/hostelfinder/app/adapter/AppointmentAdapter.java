package com.hostelfinder.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hostelfinder.app.R;
import com.hostelfinder.app.model.Appointment;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder>{

    List<Appointment> appointmentList;
    private FirebaseFirestore db;

    public AppointmentAdapter(List<Appointment> appointmentList, FirebaseFirestore db) {
        this.appointmentList = appointmentList;
        this.db = db;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_item, parent, false);

        // Passing view to ViewHolder
        return new AppointmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(appointmentList.get(position));
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView dateTime, sender;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTime = itemView.findViewById(R.id.tvDateTime);
            sender = itemView.findViewById(R.id.tvSender);
        }

        public void bind(Appointment appointment){
            dateTime.setText(appointment.dateTime);
            db.collection("users").document(appointment.sender).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        sender.setText(document.getData().get("name").toString());
                    }
                }
            });
        }

    }
}
