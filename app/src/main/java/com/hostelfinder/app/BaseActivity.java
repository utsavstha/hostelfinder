package com.hostelfinder.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.activity.result.ActivityResult;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG;
    protected FirebaseAuth mAuth;
    protected FirebaseFirestore db;
    protected final BetterActivityResult<Intent, ActivityResult> activityLauncher = BetterActivityResult.registerActivityForResult(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getView());
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        initView();
        setToolbar();
    }

    protected abstract int getView();
    protected abstract int getToolbar();
    protected void setToolbar(){
        if (getToolbar() != -1){
            Toolbar toolbar = findViewById(getToolbar());
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }
    protected abstract void setTag();
    protected abstract void initView();
    protected abstract boolean validate();

    protected CollectionReference getUserCollection(){
        return db.collection("users");
    }
    protected CollectionReference getHostelCollection(){
        return db.collection("hostel");
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
