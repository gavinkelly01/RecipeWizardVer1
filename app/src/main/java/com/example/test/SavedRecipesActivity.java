package com.example.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SavedRecipesActivity extends AppCompatActivity implements RecipeAdapter.OnRecipeClickListener {

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private final List<Recipe> savedRecipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_recipes);

        recyclerView = findViewById(R.id.saved_recipes_recycler_view);
        recipeAdapter = new RecipeAdapter(savedRecipes, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recipeAdapter);

        // Load saved recipes from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        ArrayList<Recipe> savedRecipesList = new ArrayList<>();

        try {
            String savedRecipesJson = sharedPreferences.getString("saved_recipes", "");

            if (!savedRecipesJson.isEmpty()) {
                Type type = new TypeToken<List<Recipe>>() {
                }.getType();
                savedRecipesList = new Gson().fromJson(savedRecipesJson, type);
            }
        } catch (JsonSyntaxException e) {
            Log.e("SavedRecipesActivity", "Error parsing JSON data", e);
            // Display an error message to the user
        }

        savedRecipes.addAll(savedRecipesList);
        recipeAdapter.notifyDataSetChanged();
    }

    // Add a new method to handle saving recipes
    public void saveRecipe(Recipe recipe) {
        savedRecipes.add(recipe);
        SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        String savedRecipesJson = new Gson().toJson(savedRecipes);
        sharedPreferences.edit().putString("saved_recipes", savedRecipesJson).apply();
        recipeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        // Handle clicking on a recipe item
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra("recipe", recipe);
        startActivity(intent);
    }

    @Override
    public void onRecipeClick(int position) {

    }

    @Override
    public void onRecipeLongClick(int position) {
        // Handle long clicking on a recipe item to remove it from saved recipes
        savedRecipes.remove(position);
        SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        String savedRecipesJson = new Gson().toJson(savedRecipes);
        sharedPreferences.edit().putString("saved_recipes", savedRecipesJson).apply();
        recipeAdapter.notifyItemRemoved(position);
    }
}
