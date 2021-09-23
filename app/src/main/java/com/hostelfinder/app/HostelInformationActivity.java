package com.hostelfinder.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hostelfinder.app.adapter.TabsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HostelInformationActivity extends BaseActivity {
    String TAG;
    ViewPager viewPager;
    TabLayout tabLayout;
    TabsAdapter tabsAdapter;
    Map<String, Object> data;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int EXTERNAL_STORAGE = 2;

    public String getDocumentId() {
        return documentId;
    }

    String documentId;
    List<String> images;

    public Map<String, Object> getData() {
        return data;
    }

    @Override
    protected int getView() {
        return R.layout.activity_hostel_information;
    }

    @Override
    protected int getToolbar() {
        return R.id.toolbar;
    }

    @Override
    protected void setTag() {
        TAG = HostelInformationActivity.class.getSimpleName();
    }

    @Override
    protected void initView() {

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
                                documentId = document.getId();
                                data = document.getData();
                                if (document.getData().containsKey("image")){
                                    images = (List<String>) document.getData().get("image");
                                }
                                viewPager = findViewById(R.id.pager);
                                tabsAdapter = new TabsAdapter(getSupportFragmentManager());
                                viewPager.setAdapter(tabsAdapter);
                                tabLayout = findViewById(R.id.tab_layout);
                                tabLayout.setupWithViewPager(viewPager);

                            }
                        } else {
                            Log.d("tag", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

}