package com.hostelfinder.app;

import android.content.Intent;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.hostelfinder.app.adapter.HostelDashboardAdapter;

import java.util.ArrayList;
import java.util.List;

public class HostelDashboardActivity extends BaseActivity implements HostelDashboardAdapter.OnHostelDashboardClickListener {
    String TAG;
    RecyclerView rvInfo;
    HostelDashboardAdapter adapter;


    @Override
    protected int getView() {
        return R.layout.activity_hostel_dashboard;
    }

    @Override
    protected void setTag() {
        TAG = HostelDashboardActivity.class.getSimpleName();
    }

    @Override
    protected void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rvInfo = findViewById(R.id.rvHostelDashboard);
        rvInfo.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new HostelDashboardAdapter(this, prepareMenu(), this);
        rvInfo.setAdapter(adapter);
    }

    private List<HostelInfo> prepareMenu(){
        List<HostelInfo> hostelInfo = new ArrayList<>();
        hostelInfo.add(new HostelInfo("Information", R.drawable.ic_info));
        hostelInfo.add(new HostelInfo("Manage Rooms", R.drawable.ic_bed));
        hostelInfo.add(new HostelInfo("Chat", R.drawable.ic_chat));
        hostelInfo.add(new HostelInfo("Manage Students", R.drawable.ic_home));
        hostelInfo.add(new HostelInfo("Reviews", R.drawable.ic_info));
        hostelInfo.add(new HostelInfo("Logout", R.drawable.ic_logout));

        return hostelInfo;
    }
    @Override
    protected boolean validate() {
        return false;
    }

    @Override
    public void onClick(int index) {
        if (index == 5){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HostelDashboardActivity.this, LoginActivity.class));
            finish();
        }
    }
}