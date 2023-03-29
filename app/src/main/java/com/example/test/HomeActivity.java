package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.annotation.NonNull;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.bumptech.glide.Glide;


public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavMenu;
    private ImageView recipeImage;
    private TextView recipeName;
    private Button viewRecipeButton;
    private Gson gson;
    private static final String API_KEY = "3bd64de960774274af7148c4123df14a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavMenu = findViewById(R.id.bottom_navigation_home);
        recipeImage = findViewById(R.id.featured_recipe_image);
        recipeName = findViewById(R.id.recipe_name);
        viewRecipeButton = findViewById(R.id.random_recipe_button);
        viewRecipeButton.setTag(-1); // set default tag to -1

        bottomNavMenu.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                loadRandomRecipe();
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

        viewRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int recipeId = viewRecipeButton.getTag() != null ? (int) viewRecipeButton.getTag() : -1;
                if (recipeId != -1) {
                    Toast.makeText(HomeActivity.this, "Viewing recipe with ID " + recipeId, Toast.LENGTH_SHORT).show();
                    Intent recipeDetailsIntent = new Intent(HomeActivity.this, FeaturedRecipeDetailsActivity.class);
                    recipeDetailsIntent.putExtra("recipeId", recipeId);
                    startActivity(recipeDetailsIntent);
                }
            }
        });


        loadRandomRecipe();
    }

    private void loadRandomRecipe() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.spoonacular.com/recipes/random?apiKey=" + API_KEY;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                response -> {
                    try {
                        JSONObject recipeObject = response.getJSONArray("recipes").getJSONObject(0);
                        int recipeId = recipeObject.getInt("id");
                        String recipeImageUrl = recipeObject.getString("image");
                        String recipeTitle = recipeObject.getString("title");

                        runOnUiThread(() -> {
                            recipeName.setText(recipeTitle);
                            Glide.with(this).load(recipeImageUrl).into(recipeImage);
                            viewRecipeButton.setTag(recipeId);
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error retrieving recipe details", Toast.LENGTH_SHORT).show();
                });
        queue.add(jsonObjectRequest);
    }
}