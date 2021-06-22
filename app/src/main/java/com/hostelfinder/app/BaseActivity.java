package com.hostelfinder.app;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG;
    protected FirebaseAuth mAuth;
    protected FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getView());
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        initView();
    }

    protected abstract int getView();

    protected abstract void setTag();
    protected abstract void initView();
    protected abstract boolean validate();

    protected CollectionReference getUserCollection(){
        return db.collection("users");
    }
    protected CollectionReference getHostelCollection(){
        return db.collection("hostel");
    }

}
