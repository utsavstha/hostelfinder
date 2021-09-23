package com.hostelfinder.app.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hostelfinder.app.HostelInformationActivity;
import com.hostelfinder.app.R;

import java.util.HashMap;
import java.util.Map;

public class HostelLocationFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
    private GoogleMap mMap;
    protected FirebaseFirestore db;
    LatLng latLng;
    public HostelLocationFragment() {
        // Required empty public constructor
    }

    public static HostelLocationFragment newInstance() {
        HostelLocationFragment fragment = new HostelLocationFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hostel_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        db = FirebaseFirestore.getInstance();

        view.findViewById(R.id.btnLocate).setOnClickListener(v->{
            updateLocation();
        });
    }

    private void updateLocation(){
        String documentId = ((HostelInformationActivity) getActivity()).getDocumentId();
        DocumentReference documentReference = db.collection("hostel").document(documentId);

        Map<String, Object> data = new HashMap<>();

        data.put("lat", this.latLng.latitude);
        data.put("long", this.latLng.longitude);

        documentReference.update(data).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(getContext(), "Data updated successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        this.latLng = latLng;
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Hostel Location"));
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {

        return true;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);

        LatLng sydney = new LatLng(27.7089559, 85.2911131);
//        mMap.addMarker(new MarkerOptions()
//                .position(sydney)
//                .title("Marker in Sydney"));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(zoom);

    }
}