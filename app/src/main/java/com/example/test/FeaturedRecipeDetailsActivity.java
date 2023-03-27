package com.example.test;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.test.Ingredient;
import com.example.test.R;
import com.example.test.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FeaturedRecipeDetailsActivity extends AppCompatActivity {

    private static final String API_KEY = "3bd64de960774274af7148c4123df14a";
    private TextView titleTextView;
    private ImageView imageView;
    private TextView ingredientsTextView;
    private TextView instructionsTextView;
    private TextView nutritionTextView;
    private Recipe recipe;
    private ArrayList<Ingredient> pantryIngredients;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        int recipeId = getIntent().getIntExtra("recipeId", -1);
        if (recipeId == -1) {
            finish();
            return;
        }

        titleTextView = findViewById(R.id.recipe_title);
        imageView = findViewById(R.id.recipe_image);
        ingredientsTextView = findViewById(R.id.recipe_ingredients);
        instructionsTextView = findViewById(R.id.recipe_instructions);
        nutritionTextView = findViewById(R.id.recipe_nutrition);

        // Fetch recipe details from the API using recipeId
        String url = "https://api.spoonacular.com/recipes/" + recipeId + "/information?apiKey=" + API_KEY + "&includeNutrition=true";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                response -> {
                    try {
                        String title = response.getString("title");
                        String imageUrl = response.getString("image");
                        JSONArray ingredients = response.getJSONArray("extendedIngredients");
                        StringBuilder ingredientsText = new StringBuilder();
                        for (int i = 0; i < ingredients.length(); i++) {
                            JSONObject ingredient = ingredients.getJSONObject(i);
                            String ingredientName = ingredient.getString("name");
                            double amount = ingredient.getDouble("amount");
                            String unit = ingredient.getString("unit");
                            String ingredientText = String.format("%.1f %s %s", amount, unit, ingredientName);
                            ingredientsText.append(ingredientText);
                            ingredientsText.append("\n");
                        }
                        ingredientsTextView.setText(ingredientsText.toString());

                        JSONArray steps = response.getJSONArray("analyzedInstructions").getJSONObject(0).getJSONArray("steps");
                        StringBuilder instructionsText = new StringBuilder();
                        for (int i = 0; i < steps.length(); i++) {
                            JSONObject step = steps.getJSONObject(i);
                            int stepNumber = step.getInt("number");
                            String stepText = step.getString("step");
                            instructionsText.append(String.format("%d. %s\n", stepNumber, stepText));
                        }
                        instructionsTextView.setText(instructionsText.toString());

                        // Set the title
                        titleTextView.setText(title);

                        // Load the image
                        Picasso.get().load(imageUrl).into(imageView);

                        // Get and display nutritional information
                        JSONObject nutrition = response.getJSONObject("nutrition");
                        JSONArray nutrients = nutrition.getJSONArray("nutrients");
                        StringBuilder nutritionText = new StringBuilder();
                        for (int i = 0; i < nutrients.length(); i++) {
                            JSONObject nutrient = nutrients.getJSONObject(i);
                            String nutrientName = nutrient.getString("name");
                            double nutrientAmount = nutrient.getDouble("amount");
                            String nutrientUnit = nutrient.getString("unit");
                            nutritionText.append(String.format("%s: %.1f %s\n", nutrientName, nutrientAmount, nutrientUnit));
                        }
                        nutritionTextView.setText(nutritionText.toString());

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
