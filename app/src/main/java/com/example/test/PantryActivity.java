package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class PantryActivity extends AppCompatActivity {
    private EditText editText;
    private Button addButton;
    private ListView listView;
    private ArrayList<Ingredient> ingredients;
    private ArrayAdapter<Ingredient> adapter;
    private BottomNavigationView bottomNavMenu;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        editText = findViewById(R.id.edit_text);
        addButton = findViewById(R.id.add_button);
        listView = findViewById(R.id.list_view);

        ingredients = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ingredients);
        listView.setAdapter(adapter);
        bottomNavMenu = findViewById(R.id.bottom_navigation_pantry);
        bottomNavMenu.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                Intent homeIntent = new Intent(PantryActivity.this, HomeActivity.class);
                                startActivity(homeIntent);
                                return true;
                            case R.id.navigation_pantry:
                                Intent pantryIntent = new Intent(PantryActivity.this, PantryActivity.class);
                                startActivity(pantryIntent);
                                return true;
                            case R.id.navigation_recipes:
                                Intent recipesIntent = new Intent(PantryActivity.this, RecipesActivity.class);
                                startActivity(recipesIntent);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                if (!name.isEmpty()) {
                    Ingredient ingredient = new Ingredient(name);
                    ingredients.add(ingredient);
                    adapter.notifyDataSetChanged();
                    editText.setText("");
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ingredients.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

    }
    public void openRecipesOwnedIngActivity(View view) {
        Intent intent = new Intent(this, RecipesOwnedIngActivity.class);
        startActivity(intent);
    }
}