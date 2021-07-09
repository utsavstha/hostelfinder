package com.hostelfinder.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends BaseActivity {

    private EditText email, password;


    @Override
    protected int getView() {
        return R.layout.activity_login;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            isHostel(mAuth.getUid());
        }
    }

    private void isHostel(String uid){
        db.collection("hostel").whereEqualTo("uid", uid).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                count++;
                                break;
                            }
                            if (count<0){
                                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                finish();
                            }else{
                                startActivity(new Intent(LoginActivity.this, HostelDashboardActivity.class));
                                finish();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void gotoRegister(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    @Override
    protected void setTag() {
        TAG = LoginActivity.class.getSimpleName();
    }

    @Override
    protected void initView() {
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
    }

    @Override
    protected boolean validate() {
        if(email.getText().toString().isEmpty()){
            email.setError("Please enter your email Address");
            return false;
        }
        if(password.getText().toString().isEmpty()){
            password.setError("Please enter your Password");
            return false;
        }
        return true;
    }


    public void login(View view) {
        if(validate()){
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }

    }

    public void registerHostel(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterHostelActivity.class));
    }
}