package com.hostelfinder.app;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BaseActivity {

    private EditText fName, lName, email, password, cPassword;

    @Override
    protected int getView() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        fName = findViewById(R.id.etFirstName);
        lName = findViewById(R.id.etLastName);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        cPassword = findViewById(R.id.etConfirmPassword);
    }

    @Override
    public void setTag(){
        TAG = RegisterActivity.class.getSimpleName();
    }
    @Override
    public boolean validate() {
        if (fName.getText().toString().isEmpty()) {
            fName.setError("Please enter your First Name");
            return false;
        }

        if (lName.getText().toString().isEmpty()) {
            lName.setError("Please enter your Last Name");
            return false;
        }

        if (email.getText().toString().isEmpty()) {
            email.setError("Please enter your Email");
            return false;
        }

        if (password.getText().toString().isEmpty()) {
            password.setError("Please enter your Password");
            return false;
        }

        if (cPassword.getText().toString().isEmpty()) {
            cPassword.setError("Please enter your Confirm Password");
            return false;
        }

        if (!cPassword.getText().toString().equals(cPassword.getText().toString())) {
            password.setError("Password do not match");
            cPassword.setError("Password do not match");
            return false;
        }

        return true;
    }

    public void register(View v) {
        if (validate()) {
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(fName.getText().toString() + " " + lName.getText().toString()).build();

                                user.updateProfile(profileUpdates);

                                Map<String, Object> userObj = new HashMap<>();
                                userObj.put("firstName", fName.getText().toString());
                                userObj.put("lastName", lName.getText().toString());
                                userObj.put("email", email.getText().toString());
                                getUserCollection().add(userObj).addOnSuccessListener(documentReference -> {
                                    Toast.makeText(getApplicationContext(), "goto dashboard", Toast.LENGTH_SHORT).show();

                                })
                                .addOnFailureListener(e -> {
                                    Log.w(TAG, e);
                                });

                            } else {
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}