package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PantryActivity extends AppCompatActivity {
    private EditText editText;
    private Button addButton;
    private ListView listView;
    private ArrayList<Ingredient> ingredients;
    private ArrayAdapter<Ingredient> adapter;
    private BottomNavigationView bottomNavMenu;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_chefhat);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

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

        bottomNavMenu.getMenu().findItem(R.id.navigation_pantry).setChecked(true);

        sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        gson = new Gson();

        String ingredientsJson = sharedPreferences.getString("ingredients", null);
        if (ingredientsJson != null) {
            Type type = new TypeToken<List<Ingredient>>(){}.getType();
            List<Ingredient> savedIngredients = gson.fromJson(ingredientsJson, type);
            ingredients.addAll(savedIngredients);
            adapter.notifyDataSetChanged();
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                if (!name.isEmpty()) {
                    Ingredient ingredient = new Ingredient(name);
                    ingredients.add(ingredient);
                    adapter.notifyDataSetChanged();
                    editText.setText("");

                    String ingredientsJson = gson.toJson(ingredients);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("ingredients", ingredientsJson);
                    editor.apply();

                    Toast.makeText(PantryActivity.this, "Ingredient added to pantry", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button recipesButton = findViewById(R.id.recipes_button);
        recipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingredientsJson = sharedPreferences.getString("ingredients", null);
                Type type = new TypeToken<List<Ingredient>>(){}.getType();
                List<Ingredient> savedIngredients = gson.fromJson(ingredientsJson, type);

                Intent intent = new Intent(PantryActivity.this, RecipeSuggestionsActivity.class);
                intent.putExtra("ingredients", gson.toJson(savedIngredients));
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ingredients.remove(position);
                adapter.notifyDataSetChanged();

                String ingredientsJson = gson.toJson(ingredients);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("ingredients", ingredientsJson);
                editor.apply();

                Toast.makeText(PantryActivity.this, "Ingredient removed from pantry", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
