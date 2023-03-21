package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavMenu = findViewById(R.id.bottom_navigation_pantry);

        bottomNavMenu.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(homeIntent);
                                return true;
                            case R.id.navigation_pantry:
                                Intent pantryIntent = new Intent(MainActivity.this, PantryActivity.class);
                                startActivity(pantryIntent);
                                return true;
                            case R.id.navigation_recipes:
                                Intent recipesIntent = new Intent(MainActivity.this, RecipesActivity.class);
                                startActivity(recipesIntent);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
    }
}