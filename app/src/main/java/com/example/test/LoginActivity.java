package com.example.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private TextView mRegisterTextView;
    private Pattern mEmailPattern;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_chefhat);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //Find and store references to the email input field, password input field, and login button in the layout
        mEmailEditText = findViewById(R.id.email_input);
        mPasswordEditText = findViewById(R.id.password_input);
        mLoginButton = findViewById(R.id.login_button);

        //This is a regular expression pattern for validating email addresses
        mEmailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        mSharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        //Set the email and password input fields to the previously entered values
        String email = mSharedPreferences.getString("email", "");
        String password = mSharedPreferences.getString("password", "");

        mEmailEditText.setText(email);
        mPasswordEditText.setText(password);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the current values entered in the email and password input fields
                String email = mEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();

                //Validate the email address format using the regular expression pattern
                Matcher matcher = mEmailPattern.matcher(email);
                //Retrieve the saved email and password from the shared preferences file
                if (matcher.matches()) {
                    String savedEmail = mSharedPreferences.getString("email", "");
                    String savedPassword = mSharedPreferences.getString("password", "");

                    if (email.equals(savedEmail) && password.equals(savedPassword)) {
                        //If the entered email and password match the saved values, start the HomeActivity and finish the LoginActivity
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        //If the entered email or password do not match the saved values, display an error toast message
                        Toast.makeText(LoginActivity.this, "Invalid email or password. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //If the entered email is not in a valid format, display an error toast message
                    Toast.makeText(LoginActivity.this, "Invalid email. Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mRegisterTextView = findViewById(R.id.register_link);
        mRegisterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start the RegisterActivity
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }
}