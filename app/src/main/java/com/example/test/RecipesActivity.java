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

    private static final String API_KEY = "08559c5dec9b416e8483d2f2a53148f5";
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
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_chefhat);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);
        searchView = findViewById(R.id.search_view);
        recipeAdapter = new RecipeAdapter(recipes, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recipeAdapter);
        bottomNavMenu = findViewById(R.id.bottom_navigation_recipes);

//This is the standard bottom navigation that has been used in the home activity, pantry activity and this activity aswell
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

        bottomNavMenu.getMenu().findItem(R.id.navigation_recipes).setChecked(true);

        //This here sets up the search view and searches recipes based on the text entered into this search textfield.
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
        //This is the url, with the API key and query which consists of the dietary restriction and text searched.
        String url = "https://api.spoonacular.com/recipes/search?apiKey=" + API_KEY + "&query=" + query;
        //This requests a queue for handling the response from the spoonacular API
        RequestQueue queue = Volley.newRequestQueue(this);
        //This gets a json object from the API so it can be easily accessed by us
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                //This handles the response from the spoonacular API
                response -> {
            //This then clears the list of the recipes so it doesnt show old recipes previously searched
                    recipes.clear();
                    try {
                        //This gets a list of results from the response from the API
                        JSONArray results = response.getJSONArray("results");
                        //This for loop goes through the API results and adds them to the list of recipes
                        for (int i = 0; i < results.length(); i++) {
                            //This gets the other details of the object
                            JSONObject result = results.getJSONObject(i);
                            //The result is the information gained from each specific recipe including their ID, title and image URL.
                            int id = result.getInt("id");
                            String title = result.getString("title");
                            String imageUrl = "https://spoonacular.com/recipeImages/" + id + "-240x240.jpg";

                            //This gets the instructions and ingredients details
                            List<String> instructions = new ArrayList<>();
                            List<String> ingredients = new ArrayList<>(); // Replace with the actual list of ingredients for the recipe
                            Map<String, String> nutrition = new HashMap<>(); // Replace with the actual nutrition data for the recipe
                            //This creates a new recipe object and adds the recipe to the list of recipes found after the user searches
                            recipes.add(new Recipe(id, title, imageUrl, instructions, ingredients, nutrition));
                        }
                        //This notifies the recipe adapter that the information has been changed
                        recipeAdapter.notifyDataSetChanged();

                        //This loops through each recipe and finds their nutritional information.
                        //We had to do this seperately due to the spoonacular API having a different url for nutritional information
                        for (Recipe recipe : recipes) {
                            //This is the url which uses the recipe ID found in the result object above to get the nutritional information of the recipe
                            String nutritionUrl = "https://api.spoonacular.com/recipes/" + recipe.getId() + "/nutritionWidget.json?apiKey=" + API_KEY;
                            //This is a new instance of a json object request for the nutrional information
                            JsonObjectRequest nutritionJsonObjectRequest = new JsonObjectRequest(nutritionUrl, null,
                                    //This handles the response for the nutrional API
                                    nutritionResponse -> {
                                        try {
                                            //This gets the list of nutrients from the response
                                            JSONArray nutrients = nutritionResponse.getJSONArray("nutrients");

                                            //This loops through each information and gets its name, amount and units and adds it to the Map
                                            Map<String, String> nutritionData = new HashMap<>();
                                            for (int j = 0; j < nutrients.length(); j++) {
                                                JSONObject nutrient = nutrients.getJSONObject(j);
                                                String name = nutrient.getString("name");
                                                String amount = nutrient.getString("amount");
                                                String unit = nutrient.getString("unit");
                                                nutritionData.put(name, amount + " " + unit);
                                            }
                                            //This sets the nutritional info for the recipe and notifies the adapter that data has changed
                                            recipe.setNutrition(nutritionData);
                                            recipeAdapter.notifyDataSetChanged();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    },
                                    //This handles errors from the API request
                                    error -> {
                                        error.printStackTrace();
                                        Toast.makeText(this, "Error retrieving nutrition data", Toast.LENGTH_SHORT).show();
                                    });
                            //Adds the nutrional information to the request queue
                            queue.add(nutritionJsonObjectRequest);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                //Another error handling for the spoonacular API
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Error searching recipes", Toast.LENGTH_SHORT).show();
                });
        //This adds the request to the queue
        queue.add(jsonObjectRequest);
    }

    //This recipe Click allows the user to click on a recipe to be brought to the recipe details activity page where they can see the whole recipe details.
    //This also gets the recipe id and allows it to be accessed in the recipe details activity class.
    public void onRecipeClick(int position) {
        Recipe recipe = recipes.get(position);
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra("recipe_id", recipe.getId());
        startActivity(intent);
    }

    @Override
    public void onRecipeLongClick(int position) {
    }

    //This is the same as the previous onRecipeClick seen above.
    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra("recipe", recipe);
        startActivity(intent);
    }
}
