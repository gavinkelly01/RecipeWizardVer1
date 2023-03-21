package com.example.test;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private TextView mRegisterTextView;
    private String mEmail;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize the views
        mEmailEditText = findViewById(R.id.email_input);
        mPasswordEditText = findViewById(R.id.password_input);
        mLoginButton = findViewById(R.id.login_button);
        mRegisterTextView = findViewById(R.id.register_link);

        // Set the click listener for the login button
        mLoginButton.setOnClickListener(new View.OnClickListener() {
          @Override
            public void onClick(View view) {
                 //Validate the input fields
                mEmail = mEmailEditText.getText().toString().trim();
                mPassword = mPasswordEditText.getText().toString().trim();
                if (TextUtils.isEmpty(mEmail)) {
                    mEmailEditText.setError("Email is required");
                    mEmailEditText.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(mPassword)) {
                    mPasswordEditText.setError("Password is required");
                    mPasswordEditText.requestFocus();
                    return;
                }

                // Check if the email and password are correct
                if (mEmail.equals("test@example.com") && mPassword.equals("password")) {
                    // Login successful, start the main activity
                    Toast.makeText(LoginActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Login failed, display an error message
                    Toast.makeText(LoginActivity.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                }



            }

        });



        // Set the click listener for the register text view
        mRegisterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the register activity
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });


            mLoginButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Launch the register activity
            Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(homeIntent);
        }
    });
}

    public void openHomeActivity(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
