package com.example.test;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

//This class displays the details of a featured recipe, including its title, image, ingredients, instructions, and nutrition information
public class FeaturedRecipeDetailsActivity extends AppCompatActivity {
    //This is our spoonacular api key to retrieve the recipe information
    private static final String API_KEY = "3bd64de960774274af7148c4123df14a";
    private TextView titleTextView;
    private ImageView imageView;
    private TextView ingredientsTextView;
    private TextView instructionsTextView;
    private TextView nutritionTextView;
    private TextView servingTextView;
    private TextView cookingTimeTextView;
    private Recipe recipe;
    private ArrayList<Ingredient> pantryIngredients;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        //These just show the logo in the top left corner
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_chefhat);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //This gets the Recipe ID of the featured recipe from the intent
        int recipeId = getIntent().getIntExtra("recipeId", -1);
        //If the recipe ID is invalid, finish this activity and return the user to the previous activity
        if (recipeId == -1) {
            finish();
            return;
        }
        //This finds the GUI components used to display the recipe details
        titleTextView = findViewById(R.id.recipe_title);
        imageView = findViewById(R.id.recipe_image);
        ingredientsTextView = findViewById(R.id.recipe_ingredients);
        instructionsTextView = findViewById(R.id.recipe_instructions);
        nutritionTextView = findViewById(R.id.recipe_nutrition);
        servingTextView = findViewById(R.id.recipe_serving);
        cookingTimeTextView = findViewById(R.id.recipe_cooking_time);

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the root layout of the activity
                View rootView = getWindow().getDecorView().getRootView();

                // Create a Bitmap of the root layout
                rootView.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
                rootView.setDrawingCacheEnabled(false);

                // Save the Bitmap to the user's device
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String fileName = "Recipe_" + timeStamp + ".jpg";
                File picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File recipeImageFile = new File(picturesDirectory, fileName);

                try {
                    // Save the Bitmap to the file path
                    FileOutputStream fos = new FileOutputStream(recipeImageFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                    fos.flush();
                    fos.close();

                    // Show a success message to the user
                    Toast.makeText(getApplicationContext(), "Recipe saved to Pictures directory", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    // Show an error message to the user
                    Toast.makeText(getApplicationContext(), "Error saving recipe", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        //This is the URL used to retrieve the recipe details from the Spoonacular API
        String url = "https://api.spoonacular.com/recipes/" + recipeId + "/information?apiKey=" + API_KEY + "&includeNutrition=true";
        //Create a new RequestQueue to handle our connection to the JSON Object below
        RequestQueue queue = Volley.newRequestQueue(this);
        //Create a new JsonObjectRequest to retrieve the recipe details from the Spoonacular API
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                response -> {
                    try {
                        //This extracts the recipe title, image url, instructions and nutritonal info from the JSON Object
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
                        int cookingTime = response.getInt("readyInMinutes");
                        int servingSize = response.getInt("servings");
                        servingTextView.setText("Serving Size: " + servingSize);
                        cookingTimeTextView.setText("Cooking Time: " + cookingTime + " minutes");
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
                        titleTextView.setText(title);
                        Glide.with(this).load(imageUrl).into(imageView);
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
