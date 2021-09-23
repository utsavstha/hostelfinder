package com.hostelfinder.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hostelfinder.app.adapter.AppointmentAdapter;
import com.hostelfinder.app.adapter.TabsAdapter;
import com.hostelfinder.app.model.Appointment;
import com.hostelfinder.app.model.Hostel;

import java.util.ArrayList;
import java.util.List;

public class AppointmentActivity extends BaseActivity {

    RecyclerView rvAppointments;
    AppointmentAdapter appointmentAdapter;
    List<Appointment> appointmentList;
    @Override
    protected int getView() {
        return R.layout.activity_appointment;
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
        appointmentList = new ArrayList<>();
        rvAppointments = findViewById(R.id.rvAppointments);
        rvAppointments.setLayoutManager(new LinearLayoutManager(this));
        appointmentAdapter = new AppointmentAdapter(appointmentList, FirebaseFirestore.getInstance());
        rvAppointments.setAdapter(appointmentAdapter);
        fetchData();
    }

    @Override
    protected boolean validate() {
        return false;
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
                                Hostel hostel = document.toObject(Hostel.class);
                                appointmentList.clear();
                                appointmentList.addAll(hostel.appointments);
                                appointmentAdapter.notifyDataSetChanged();

                            }
                        } else {
                            Log.d("tag", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}