package com.example.test;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeDetailsActivity extends AppCompatActivity {

    private static final String API_KEY = "3bd64de960774274af7148c4123df14a";
    private TextView titleTextView;
    private ImageView imageView;
    private TextView ingredientsTextView;
    private TextView instructionsTextView;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        recipe = (Recipe) getIntent().getSerializableExtra("recipe");
        if (recipe == null) {
               finish();
            return;
        }

        titleTextView = findViewById(R.id.recipe_title);
        imageView = findViewById(R.id.recipe_image);
        ingredientsTextView = findViewById(R.id.recipe_ingredients);
        instructionsTextView = findViewById(R.id.recipe_instructions);

        int recipeId = recipe.getId();

        String url = "https://api.spoonacular.com/recipes/" + recipeId + "/information?apiKey=" + API_KEY;

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
                            ingredientsText.append(ingredientText).append("\n");
                        }

                        JSONArray instructions = response.getJSONArray("analyzedInstructions")
                                .getJSONObject(0)
                                .getJSONArray("steps");
                        StringBuilder instructionsText = new StringBuilder();
                        for (int i = 0; i < instructions.length(); i++) {
                            JSONObject step = instructions.getJSONObject(i);
                            String stepText = step.getString("step");
                            instructionsText.append(i + 1).append(". ").append(stepText).append("\n\n");
                        }

                        Glide.with(this)
                                .load(imageUrl)
                                .placeholder(R.drawable.ic_launcher_background)
                                .error(R.drawable.ic_launcher_background)
                                .into(imageView);

                        titleTextView.setText(title);
                        ingredientsTextView.setText(ingredientsText.toString());
                        instructionsTextView.setText(instructionsText.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                          finish();
                    }
                },
                error -> {
                    error.printStackTrace();

                     finish();
                });
        queue.add(jsonObjectRequest);
    }
}