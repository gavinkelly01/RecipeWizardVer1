package com.example.test;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PantryActivity extends AppCompatActivity {
    //This code sets up the PantryActivity user interface, including a list of ingredients that can be added, removed, and used to suggest recipes.
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

        //Initialize an ArrayList to hold the ingredients and an ArrayAdapter to populate the list view
        ingredients = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ingredients);
        listView.setAdapter(adapter);

        //Set up the bottom navigation menu to switch between activities.
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

        //Set the "pantry" item in the bottom navigation menu to be checked by default.
        bottomNavMenu.getMenu().findItem(R.id.navigation_pantry).setChecked(true);

        //Initialize SharedPreferences and Gson objects for storing and retrieving the list of ingredients.
        sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        gson = new Gson();

        //Check if the ingredients list has been previously saved in SharedPreferences, and if so, add those ingredients to the list.
        String ingredientsJson = sharedPreferences.getString("ingredients", null);
        if (ingredientsJson != null) {
            Type type = new TypeToken<List<Ingredient>>() {
            }.getType();
            List<Ingredient> savedIngredients = gson.fromJson(ingredientsJson, type);
            ingredients.addAll(savedIngredients);
            adapter.notifyDataSetChanged();
        }

        //Set up the "Add" button to add a new ingredient to the list and save the updated list to SharedPreferences.
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                if (!name.isEmpty()) {
                    Ingredient ingredient = new Ingredient(name);
                    ingredients.add(ingredient);
                    adapter.notifyDataSetChanged();
                    editText.setText("");

                    //Convert the updated list of ingredients to JSON and save it to SharedPreferences.
                    String ingredientsJson = gson.toJson(ingredients);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("ingredients", ingredientsJson);
                    editor.apply();

                    //Display a message indicating that the ingredient was added.
                    Toast.makeText(PantryActivity.this, "Ingredient added to pantry", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Set up the "Find Recipes" button to launch the RecipeSuggestionsActivity with recipes they can make using their ingredients.
        Button recipesButton = findViewById(R.id.recipes_button);
        recipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingredientsJson = sharedPreferences.getString("ingredients", null);
                Type type = new TypeToken<List<Ingredient>>() {
                }.getType();
                List<Ingredient> savedIngredients = gson.fromJson(ingredientsJson, type);
                Intent intent = new Intent(PantryActivity.this, RecipeSuggestionsActivity.class);
                intent.putExtra("ingredients", gson.toJson(savedIngredients));
                startActivity(intent);
            }
        });

        //Set up the ListView to remove an ingredient from the list and update SharedPreferences when it is clicked.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Remove the ingredient at the clicked position from the ingredients ArrayList
                ingredients.remove(position);
                //Notify the adapter that the data set has changed
                adapter.notifyDataSetChanged();

                //// Convert the ingredients ArrayList to a JSON string using Gson
                String ingredientsJson = gson.toJson(ingredients);
                //Update the "ingredients" in the sharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("ingredients", ingredientsJson);
                editor.apply();

                //Display a toast message to indicate that the ingredient has been removed from the pantry
                Toast.makeText(PantryActivity.this, "Ingredient removed from pantry", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
