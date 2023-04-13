package com.example.test;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecipeDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_RECIPE = "com.example.test.RECIPE";
    private static final String API_KEY = "ec0037e7aa88429bb9dcea25e871d93d";
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
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_chefhat);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //Get the recipe object passed in from the previous activity
        recipe = (Recipe) getIntent().getSerializableExtra("recipe");
        //If recipe is null, finish the activity and return
        if (recipe == null) {
            finish();
            return;
        }

        //Initialize views
        titleTextView = findViewById(R.id.recipe_title);
        imageView = findViewById(R.id.recipe_image);
        ingredientsTextView = findViewById(R.id.recipe_ingredients);
        instructionsTextView = findViewById(R.id.recipe_instructions);
        nutritionTextView = findViewById(R.id.recipe_nutrition);
        servingTextView = findViewById(R.id.recipe_serving);
        cookingTimeTextView = findViewById(R.id.recipe_cooking_time);

        //Load pantry ingredients from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        gson = new Gson();
        String pantryIngredientsJson = sharedPreferences.getString("ingredients", null);

        //If pantryIngredientsJson is not null, parse JSON string and store in savedPantryIngredients
        if (pantryIngredientsJson != null) {
            Type type = new TypeToken<List<Ingredient>>() {}.getType();
            List<Ingredient> savedPantryIngredients = gson.fromJson(pantryIngredientsJson, type);
            pantryIngredients = new ArrayList<>(savedPantryIngredients);
        } else { // If pantryIngredientsJson is null, create new empty list
            pantryIngredients = new ArrayList<>();
        }

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


        //Get the ID of the recipe
        int recipeId = recipe.getId();

        //Create URL to fetch recipe information from Spoonacular API
        String url = "https://api.spoonacular.com/recipes/" + recipeId + "/information?apiKey=" + API_KEY + "&includeNutrition=true";

        //Initialize a new RequestQueue for network requests
        RequestQueue queue = Volley.newRequestQueue(this);

        //Initialize a new JsonObjectRequest to fetch recipe information from Spoonacular API
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                response -> {
                    try {
                        //Parse recipe information from JSON response
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

                        //Get the instructions for the recipe from the JSON response
                        JSONArray instructions = response.getJSONArray("analyzedInstructions")
                                .getJSONObject(0)
                                .getJSONArray("steps");

                        //Create a StringBuilder to hold the formatted instructions
                        StringBuilder instructionsText = new StringBuilder();

                        //Loop through each instruction step in the JSONArray
                        for (int i = 0; i < instructions.length(); i++) {
                            JSONObject step = instructions.getJSONObject(i);
                            // Get the step number and instruction text for the current step
                            int stepNumber = step.getInt("number");
                            String instruction = step.getString("step");

                            //Add the step number and instruction text to the StringBuilder
                            instructionsText.append("").append(stepNumber).append(". ").append(instruction).append("<br>");

                            //Highlight pantry ingredients in the instruction text
                            for (Ingredient pantryIngredient : pantryIngredients) {
                                String ingredientName = pantryIngredient.getName().toLowerCase();

                                //Check if the ingredient name appears in the instruction text
                                if (instruction.toLowerCase().contains(ingredientName.toLowerCase())) {
                                    // If it does, replace all instances of the name with a bolded version of the name
                                    instruction = instruction.replaceAll("(?i)" + ingredientName, "<b>" + ingredientName + "</b>");
                                }
                            }
                            //Add the formatted instruction text to the StringBuilder
                            instructionsText.append(instruction).append("<br>");
                        }

                        JSONArray nutrients = response.getJSONObject("nutrition").getJSONArray("nutrients");
                        //Retrieve the nutrition information for the recipe using Spoonacular API
                        StringBuilder nutritionText = new StringBuilder();
                        //Iterate through each nutrient and format the information as a string
                        for (int i = 0; i < nutrients.length(); i++) {
                            JSONObject nutrient = nutrients.getJSONObject(i);
                            String name = nutrient.getString("name");
                            double amount = nutrient.getDouble("amount");
                            String unit = nutrient.getString("unit");
                            String nutrientText = String.format("%s: %.1f %s<br>", name, amount, unit);
                            nutritionText.append(nutrientText);
                        }
                        int cookingTime = response.getInt("readyInMinutes");
                        int servingSize = response.getInt("servings");

                        //Convert the ingredients, instructions, and nutrition information to string format
                        String ingredientsString = ingredientsText.toString();
                        String instructionsString = instructionsText.toString();
                        String nutritionString = nutritionText.toString();

                        //Update the GUI with the recipe information
                        //How to update gui using runOnUiThread https://stackoverflow.com/questions/62234856/how-to-use-runonuithread-for-updating-a-textview
                        runOnUiThread(() -> {
                            titleTextView.setText(title);
                            Glide.with(this).load(imageUrl).into(imageView);
                            servingTextView.setText("Serving Size: " + servingSize);
                            cookingTimeTextView.setText("Cooking Time: " + cookingTime + " minutes");
                            ingredientsTextView.setText(Html.fromHtml(ingredientsString));
                            instructionsTextView.setText(Html.fromHtml(instructionsString));
                            nutritionTextView.setText(Html.fromHtml(nutritionString));
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    //If there is an error retrieving the recipe details, display this error message below
                    Toast.makeText(this, "Error retrieving recipe details", Toast.LENGTH_SHORT).show();
                });
        queue.add(jsonObjectRequest);
    }
    }

