package com.hostelfinder.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hostelfinder.app.adapter.TabsAdapter;

import java.util.HashMap;
import java.util.Map;

public class ManageRoomActivity extends BaseActivity {

    EditText etTotal, etAvailable;
    String documentId;
    @Override
    protected int getView() {
        return R.layout.activity_manage_room;
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
        etTotal = findViewById(R.id.etTotalRooms);
        etAvailable = findViewById(R.id.etAvailableRooms);
        fetchData();
    }

    @Override
    protected boolean validate() {
        if (etTotal.getText().toString().isEmpty()){
            etTotal.setError("Total rooms can not be empty");
            return false;
        }
        if (etAvailable.getText().toString().isEmpty()){
            etAvailable.setError("Available rooms can not be empty");
            return false;
        }
        try{
            int total = Integer.parseInt(etTotal.getText().toString());
            int available = Integer.parseInt(etAvailable.getText().toString());
            if (available > total){
                Toast.makeText(getApplicationContext(), "Available rooms can not be greater than total rooms", Toast.LENGTH_SHORT).show();
                return false;
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    private void fetchData(){
        CollectionReference hostelRef = db.collection("hostel");
        hostelRef.whereEqualTo("uid", mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("tag", document.getId() + " => " + document.getData());
                                Map<String, Object>  data = document.getData();
                                documentId = document.getId();

                                if(data.containsKey("totalRoom")){
                                    etTotal.setText(data.get("totalRoom").toString());
                                }

                                if(data.containsKey("availableRooms")){
                                    etAvailable.setText(data.get("availableRooms").toString());
                                }


                            }
                        } else {
                            Log.d("tag", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void update(View view) {
        if (validate()){
            updateData();
        }
    }
    private void updateData(){
        DocumentReference documentReference = db.collection("hostel").document(documentId);

        Map<String, Object> data = new HashMap<>();

        data.put("totalRoom", etTotal.getText().toString());
        data.put("availableRooms", etAvailable.getText().toString());
        documentReference.update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Data updated successfully", Toast.LENGTH_SHORT).show();
//                    etChat.setText("");
                }
            }
        });
    }
}