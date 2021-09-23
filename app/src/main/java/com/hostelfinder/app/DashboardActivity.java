package com.hostelfinder.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.hostelfinder.app.adapter.HostelAdapter;
import com.hostelfinder.app.model.Hostel;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends BaseActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, View.OnClickListener, HostelAdapter.OnHostelClickListener {

    private GoogleMap mMap;
    private List<Hostel> hostelList;
    private Marker mSelectedMarker;
    private HostelAdapter hostelAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private LinearLayoutCompat layoutHome, layoutConversation, layoutFavorites, layoutLogout;
    private EditText etSearch;
    private RecyclerView rvHostels;

    @Override
    protected int getView() {
        return R.layout.activity_dashboard;
    }

    @Override
    protected int getToolbar() {
        return -1;
    }

    @Override
    protected void initView() {
        hostelList = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        etSearch = findViewById(R.id.etSearch);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
        mDrawerToggle.syncState();
        rvHostels = findViewById(R.id.rvHostels);
        rvHostels.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        hostelAdapter = new HostelAdapter(hostelList, this);
        rvHostels.setAdapter(hostelAdapter);

        fetchData();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);

        fetchNavigationItems();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s.length() != 0){
                    hostelAdapter.filter(s.toString());
                }else{
                    hostelAdapter.filter("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void fetchNavigationItems() {
        layoutHome = navigationView.findViewById(R.id.layoutHome);
        layoutConversation = navigationView.findViewById(R.id.layoutConversation);
        layoutFavorites = navigationView.findViewById(R.id.layoutFavorites);
        layoutLogout = navigationView.findViewById(R.id.layoutLogout);

        layoutHome.setOnClickListener(this);
        layoutConversation.setOnClickListener(this);
        layoutFavorites.setOnClickListener(this);
        layoutLogout.setOnClickListener(this);

    }
    private void fetchData() {
        getHostelCollection()
                .get()
                .addOnCompleteListener(task -> {
                    hostelList.clear();
                    if (task.isSuccessful()) {
                        int index = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            Hostel hostel = document.toObject(Hostel.class);
                            hostel.index = index;
                            hostel.documentId = document.getId();
                            hostelList.add(hostel);
                            index++;
                        }
                        addMarkers();
                        hostelAdapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    @Override
    protected void setTag() {
        TAG = DashboardActivity.class.getSimpleName();
    }



    @Override
    protected boolean validate() {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
    }

    public MarkerOptions createMarker(LatLng point, float ratingValue) {
        MarkerOptions marker = new MarkerOptions();
        marker.position(point);
        int px = 70;
        View markerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker, null);
        markerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        markerView.layout(0, 0, px + 10, px);
        markerView.buildDrawingCache();
        TextView rating = markerView.findViewById(R.id.rating);
        Bitmap mDotMarkerBitmap = Bitmap.createBitmap(px + 10, px, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mDotMarkerBitmap);
        rating.setText(String.valueOf(ratingValue));
        markerView.draw(canvas);
        marker.icon(BitmapDescriptorFactory.fromBitmap(mDotMarkerBitmap));
        return marker;
    }

    private void addMarkers() {

        for (Hostel hostel : hostelList) {
            LatLng loc = new LatLng(hostel.lat, hostel.lng);
            MarkerOptions marker = new MarkerOptions().position(loc).title(hostel.name);
            marker.icon(getBitmap(this, R.drawable.ic_bed));
            mMap.addMarker(marker).setTag(hostel.index);

        }
        LatLng loc = new LatLng(hostelList.get(0).lat, hostelList.get(0).lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        if (null != mSelectedMarker) {
            mSelectedMarker.setIcon(getBitmap(this, R.drawable.ic_bed));
        }
        mSelectedMarker = marker;
        mSelectedMarker.setIcon(getBitmap(this, R.drawable.ic_bed_2));
        rvHostels.smoothScrollToPosition((int) marker.getTag());
        return false;
    }

    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    private static BitmapDescriptor getBitmap(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable instanceof BitmapDrawable) {
            return BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), drawableId));
        } else if (drawable instanceof VectorDrawable) {
            return BitmapDescriptorFactory.fromBitmap(getBitmap((VectorDrawable) drawable));
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        if (null != mSelectedMarker) {
            mSelectedMarker.setIcon(getBitmap(this, R.drawable.ic_bed_2));
        }
        mSelectedMarker = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notification:
//                displayFilterOptions();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutHome:
                break;
            case R.id.layoutConversation:
                startActivity(new Intent(DashboardActivity.this, ConversationActivity.class));
                break;
            case R.id.layoutFavorites:
                break;
            case R.id.layoutLogout:
                mAuth.signOut();
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                finish();
                break;

        }


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onClick(int index) {
        Intent intent = new Intent(DashboardActivity.this, HostelDetails.class);
        AppCache.setHostel(hostelList.get(index));
        intent.putExtra("uid", hostelList.get(index).documentId);
        startActivity(intent);
    }
}