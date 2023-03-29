package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        mEmailEditText = findViewById(R.id.email_input);
        mPasswordEditText = findViewById(R.id.password_input);
        mLoginButton = findViewById(R.id.login_button);

        mEmailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        mSharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        String email = mSharedPreferences.getString("email", "");
        String password = mSharedPreferences.getString("password", "");

        mEmailEditText.setText(email);
        mPasswordEditText.setText(password);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();

                Matcher matcher = mEmailPattern.matcher(email);
                if (matcher.matches()) {
                    String savedEmail = mSharedPreferences.getString("email", "");
                    String savedPassword = mSharedPreferences.getString("password", "");

                    if (email.equals(savedEmail) && password.equals(savedPassword)) {

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid email or password. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid email. Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mRegisterTextView = findViewById(R.id.register_link);
        mRegisterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }
}