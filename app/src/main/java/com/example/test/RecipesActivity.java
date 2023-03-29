package com.example.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipesActivity extends AppCompatActivity implements RecipeAdapter.OnRecipeClickListener {

    private static final String API_KEY = "3bd64de960774274af7148c4123df14a";
    private RecyclerView recyclerView;
    private SearchView searchView;
    private BottomNavigationView bottomNavMenu;
    private RecipeAdapter recipeAdapter;
    private final List<Recipe> recipes = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        recyclerView = findViewById(R.id.recycler_view);
        searchView = findViewById(R.id.search_view);
        recipeAdapter = new RecipeAdapter(recipes, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recipeAdapter);
        bottomNavMenu = findViewById(R.id.bottom_navigation_recipes);

        bottomNavMenu.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                Intent homeIntent = new Intent(RecipesActivity.this, HomeActivity.class);
                                startActivity(homeIntent);
                                return true;
                            case R.id.navigation_pantry:
                                Intent pantryIntent = new Intent(RecipesActivity.this, PantryActivity.class);
                                startActivity(pantryIntent);
                                return true;
                            case R.id.navigation_recipes:
                                Intent recipesIntent = new Intent(RecipesActivity.this, RecipesActivity.class);
                                startActivity(recipesIntent);
                                return true;
                            default:
                                return false;
                        }
                    }
                });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRecipes(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        recipeAdapter.setOnRecipeClickListener(this);
    }

    private void searchRecipes(String query) {
        String url = "https://api.spoonacular.com/recipes/search?apiKey=" + API_KEY + "&query=" + query;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                response -> {
                    recipes.clear();
                    try {
                        JSONArray results = response.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject result = results.getJSONObject(i);
                            int id = result.getInt("id");
                            String title = result.getString("title");
                            String imageUrl = "https://spoonacular.com/recipeImages/" + id + "-240x240.jpg";

                            // Get recipe details
                            List<String> instructions = new ArrayList<>();
                            List<String> ingredients = new ArrayList<>(); // Replace with the actual list of ingredients for the recipe
                            Map<String, String> nutrition = new HashMap<>(); // Replace with the actual nutrition data for the recipe

                            recipes.add(new Recipe(id, title, imageUrl, instructions, ingredients, nutrition));
                        }
                        recipeAdapter.notifyDataSetChanged();
                       } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Error searching recipes", Toast.LENGTH_SHORT).show();
                });
        queue.add(jsonObjectRequest);
    }

    public void onRecipeClick(int position) {
        Recipe recipe = recipes.get(position);
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra("recipe_id", recipe.getId());
        startActivity(intent);
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra("recipe", recipe);
        startActivity(intent);
    }


}
