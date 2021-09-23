package com.hostelfinder.app.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hostelfinder.app.HostelInformationActivity;
import com.hostelfinder.app.R;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HostelInformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HostelInformationFragment extends Fragment {

    private EditText etWardenName, etHostelName, etEmail, etHostelAddress, etContactNumber, etPricePerPerson, etDescription;
    protected FirebaseFirestore db;
    public HostelInformationFragment() {
        // Required empty public constructor
    }

    public static HostelInformationFragment newInstance() {
        HostelInformationFragment fragment = new HostelInformationFragment();
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
        return inflater.inflate(R.layout.fragment_hoste_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etWardenName = view.findViewById(R.id.etWardenName);
        etHostelName = view.findViewById(R.id.etHostelName);
        etEmail = view.findViewById(R.id.etEmail);
        etHostelAddress = view.findViewById(R.id.etAddress);
        etContactNumber = view.findViewById(R.id.etContact);
        etPricePerPerson = view.findViewById(R.id.etRate);
        etDescription = view.findViewById(R.id.etDescription);
        db = FirebaseFirestore.getInstance();
        fetchData();


        view.findViewById(R.id.btnUpdate).setOnClickListener(v -> {
            if (validate()){
                updateData();
            }
        });
    }

    private boolean validate(){
        if (isEmpty(etWardenName) || isEmpty(etHostelName) || isEmpty(etEmail) || isEmpty(etHostelAddress)
                || isEmpty(etContactNumber) || isEmpty(etPricePerPerson) || isEmpty(etDescription)){
            Toast.makeText(getContext(), "Please make sure no fields are empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isEmpty(EditText editText){
        return editText.getText().toString().isEmpty();
    }

    private void fetchData() {
        Map<String, Object> data = ((HostelInformationActivity) getActivity()).getData();
        etWardenName.setText(data.get("warden").toString());
        etHostelName.setText(data.get("name").toString());
        etEmail.setText(data.get("email").toString());
        etHostelAddress.setText(data.get("address").toString());
        etContactNumber.setText(data.get("contact").toString());
        if (data.containsKey("description")){
            etDescription.setText(data.get("description").toString());
        }
        if (data.containsKey("rate")){
            etPricePerPerson.setText(data.get("rate").toString());

        }

    }

    private void updateData(){
        String documentId = ((HostelInformationActivity) getActivity()).getDocumentId();
        DocumentReference documentReference = db.collection("hostel").document(documentId);

        Map<String, Object> data = new HashMap<>();

        data.put("address", etHostelAddress.getText().toString());
        data.put("contact", etContactNumber.getText().toString());
        data.put("email", etEmail.getText().toString());
        data.put("name", etHostelName.getText().toString());
        data.put("warden", etWardenName.getText().toString());
        data.put("description", etDescription.getText().toString());
        data.put("rate", etPricePerPerson.getText().toString());
        documentReference.update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(), "Data updated successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}