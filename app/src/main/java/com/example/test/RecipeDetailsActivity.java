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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE = "com.example.test.RECIPE";
    private static final String API_KEY = "56f3fc3c51b7482c8fa50e5a1b6c61b1";
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
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_chefhat);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        recipe = (Recipe) getIntent().getSerializableExtra("recipe");
        if (recipe == null) {
            finish();
            return;
        }

        titleTextView = findViewById(R.id.recipe_title);
        imageView = findViewById(R.id.recipe_image);
        ingredientsTextView = findViewById(R.id.recipe_ingredients);
        instructionsTextView = findViewById(R.id.recipe_instructions);
        nutritionTextView = findViewById(R.id.recipe_nutrition);

        // Load pantry ingredients from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        gson = new Gson();
        String pantryIngredientsJson = sharedPreferences.getString("ingredients", null);
        if (pantryIngredientsJson != null) {
            Type type = new TypeToken<List<Ingredient>>() {
            }.getType();
            List<Ingredient> savedPantryIngredients = gson.fromJson(pantryIngredientsJson, type);
            pantryIngredients = new ArrayList<>(savedPantryIngredients);
        } else {
            pantryIngredients = new ArrayList<>();
        }

        int recipeId = recipe.getId();

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

                            // Check if the ingredient contains the pantry ingredient name
                            boolean isFoundInPantry = false;
                            for (Ingredient pantryIngredient : pantryIngredients) {
                                if (ingredientName.toLowerCase().contains(pantryIngredient.getName().toLowerCase())) {
                                    isFoundInPantry = true;
                                    break;
                                }
                            }

                            // Highlight pantry ingredients in the ingredient list
                            if (isFoundInPantry) {
                                ingredientsText.append("<b>").append(ingredientText).append("</b><br>");
                            } else {
                                ingredientsText.append(ingredientText).append("<br>");
                            }
                        }

                        JSONArray instructions = response.getJSONArray("analyzedInstructions")
                                .getJSONObject(0)
                                .getJSONArray("steps");
                        StringBuilder instructionsText = new StringBuilder();
                        for (int i = 0; i < instructions.length(); i++) {
                            JSONObject step = instructions.getJSONObject(i);
                            int stepNumber = step.getInt("number");
                            String instruction = step.getString("step");
                            instructionsText.append("").append(stepNumber).append(". ").append(instruction).append("<br>");
                            // Highlight pantry ingredients in the instructions
                            for (Ingredient pantryIngredient : pantryIngredients) {
                                String ingredientName = pantryIngredient.getName().toLowerCase();
                                if (instruction.toLowerCase().contains(ingredientName.toLowerCase())) {
                                    instruction = instruction.replaceAll("(?i)" + ingredientName, "<b>" + ingredientName + "</b>");
                                }
                            }
                            instructionsText.append(instruction).append("<br>");
                        }

                        JSONArray nutrients = response.getJSONObject("nutrition").getJSONArray("nutrients");
                        StringBuilder nutritionText = new StringBuilder();
                        for (int i = 0; i < nutrients.length(); i++) {
                            JSONObject nutrient = nutrients.getJSONObject(i);
                            String name = nutrient.getString("name");
                            double amount = nutrient.getDouble("amount");
                            String unit = nutrient.getString("unit");
                            String nutrientText = String.format("%s: %.1f %s<br>", name, amount, unit);
                            nutritionText.append(nutrientText);
                        }

                        String ingredientsString = ingredientsText.toString();
                        String instructionsString = instructionsText.toString();
                        String nutritionString = nutritionText.toString();

                        runOnUiThread(() -> {
                            titleTextView.setText(title);
                            Glide.with(this).load(imageUrl).into(imageView);
                            ingredientsTextView.setText(Html.fromHtml(ingredientsString));
                            instructionsTextView.setText(Html.fromHtml(instructionsString));
                            nutritionTextView.setText(Html.fromHtml(nutritionString));
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