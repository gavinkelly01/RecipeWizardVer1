package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavMenu;

    // The following code overrides the onCreate method of the Activity class
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_chefhat);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //Create an Intent to launch the LoginActivity
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);

        //Start the LoginActivity
        startActivity(loginIntent);
        finish();

// Find the BottomNavigationView with the ID bottom_navigation_pantry
        bottomNavMenu = findViewById(R.id.bottom_navigation_pantry);

// Set the OnNavigationItemSelectedListener of the BottomNavigationView
        bottomNavMenu.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // Check which item in the BottomNavigationView was selected
                        switch (item.getItemId()) {
                            // If the Home item was selected
                            case R.id.navigation_home:
                                // Create an Intent to launch the HomeActivity
                                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                                // Start the HomeActivity
                                startActivity(homeIntent);
                                return true;
                            // If the Pantry item was selected
                            case R.id.navigation_pantry:
                                // Create an Intent to launch the PantryActivity
                                Intent pantryIntent = new Intent(MainActivity.this, PantryActivity.class);
                                // Start the PantryActivity
                                startActivity(pantryIntent);
                                return true;
                            // If the Recipes item was selected
                            case R.id.navigation_recipes:
                                //Create an Intent to launch the RecipesActivity
                                Intent recipesIntent = new Intent(MainActivity.this, RecipesActivity.class);
                                //Start the RecipesActivity
                                startActivity(recipesIntent);
                                return true;
                            //If no item was selected, return false
                            default:
                                return false;
                        }
                    }
                });
    }
}