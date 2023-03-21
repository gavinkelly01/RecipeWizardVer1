package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mRegisterButton;
    private Pattern mEmailPattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        mEmailEditText = findViewById(R.id.register_email);
        mPasswordEditText = findViewById(R.id.register_password);
        mRegisterButton = findViewById(R.id.register_button);

        mEmailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();

                Matcher matcher = mEmailPattern.matcher(email);
                if (matcher.matches()) {
                    // Email is valid, proceed to sign-up with Firebase
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign-up successful, proceed to main activity
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // Sign-up failed, display error message
                                        Toast.makeText(RegisterActivity.this, "Sign-up failed. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    // Email is invalid, display error message
                    Toast.makeText(RegisterActivity.this, "Invalid email. Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

