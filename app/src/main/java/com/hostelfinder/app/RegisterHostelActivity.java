package com.hostelfinder.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class RegisterHostelActivity extends BaseActivity {
    String TAG;
    private EditText etWardenName, etHostelName, etEmail, etHostelAddress, etContactNumber, etPassword, etConfirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getView() {
        return R.layout.activity_register_hostel;
    }

    @Override
    protected void setTag() {
        TAG = RegisterHostelActivity.class.getSimpleName();
    }

    @Override
    protected void initView() {
        etWardenName = findViewById(R.id.etWardenName);
        etHostelName = findViewById(R.id.etHostelName);
        etEmail = findViewById(R.id.etEmail);
        etHostelAddress = findViewById(R.id.etAddress);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etContactNumber = findViewById(R.id.etContact);
    }

    @Override
    protected boolean validate() {
        if (etWardenName.getText().toString().isEmpty()){
            etWardenName.setError("Please enter Warden Name");
            return false;
        }

        if (etHostelName.getText().toString().isEmpty()){
            etHostelName.setError("Please enter Hostel Name");
            return false;
        }

        if (etEmail.getText().toString().isEmpty()){
            etEmail.setError("Please enter contact email");
            return false;
        }

        if (etHostelAddress.getText().toString().isEmpty()){
            etHostelAddress.setError("Please enter Hostel Address");
            return false;
        }

        if (etContactNumber.getText().toString().isEmpty()){
            etContactNumber.setError("Please enter Contact number");
            return false;
        }

        if (etPassword.getText().toString().isEmpty()){
            etPassword.setError("Please enter Password");
            return false;
        }

        if (etConfirmPassword.getText().toString().isEmpty()){
            etConfirmPassword.setError("Please enter confirm password");
            return false;
        }

        if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())){
            etPassword.setError("Passwords do not match");
            etConfirmPassword.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    public void register(View view) {
        if (validate()){
            mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                Map<String, Object> data = new HashMap<>();
                                data.put("address", etHostelAddress.getText().toString());
                                data.put("contact", etContactNumber.getText().toString());
                                data.put("name", etHostelName.getText().toString());
                                data.put("uid", user.getUid());
                                data.put("email", etEmail.getText().toString());
                                data.put("warden", etWardenName.getText().toString());

                                db.collection("hostel")
                                        .add(data)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(RegisterHostelActivity.this,
                                                        "Hostel Registered",
                                                        Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(RegisterHostelActivity.this,
                                                        HostelDashboardActivity.class));
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error adding document", e);
                                            }
                                        });

                            } else {
                                Toast.makeText(RegisterHostelActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterHostelActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }
}