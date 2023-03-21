package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        bottomNavMenu = findViewById(R.id.bottom_navigation_home);

        bottomNavMenu.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                Intent homeIntent = new Intent(HomeActivity.this, HomeActivity.class);
                                startActivity(homeIntent);
                                return true;
                            case R.id.navigation_pantry:
                                Intent pantryIntent = new Intent(HomeActivity.this, PantryActivity.class);
                                startActivity(pantryIntent);
                                return true;
                            case R.id.navigation_recipes:
                                Intent recipesIntent = new Intent(HomeActivity.this, RecipesActivity.class);
                                startActivity(recipesIntent);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
    }
    public void openFeaturedRecipeActivity(View view) {
        Intent intent = new Intent(this, FeaturedRecipesActivity.class);
        startActivity(intent);
    }
}