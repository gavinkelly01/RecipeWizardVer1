package com.example.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecipeSuggestionsActivity extends AppCompatActivity implements RecipeAdapter.OnRecipeClickListener {
    private BottomNavigationView bottomNavMenu;
    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipes;
    private List<String> pantryIngredients;
    public static final String EXTRA_RECIPE = "com.example.test.RECIPE";
    private final String API_KEY = "56f3fc3c51b7482c8fa50e5a1b6c61b1"; // replace with your Spoonacular API key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipessuggestions);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_chefhat);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        recyclerView = findViewById(R.id.recipe_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recipes = new ArrayList<>(); // initialize recipes with an empty list
        recipeAdapter = new RecipeAdapter(recipes, this);
        recyclerView.setAdapter(recipeAdapter);

        pantryIngredients = getPantryIngredients(); // initialize pantryIngredients
        loadRecipes();
    }

    private List<String> getPantryIngredients() {
        Intent intent = getIntent();
        String ingredientsJson = intent.getStringExtra("ingredients");
        if (ingredientsJson == null) {
            // handle null input
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<Ingredient>>(){}.getType();
        Gson gson = new Gson();
        List<Ingredient> ingredients = gson.fromJson(ingredientsJson, type);
        return ingredients.stream()
                .map(Ingredient::getName)
                .collect(Collectors.toList());
    }


    private void loadRecipes() {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.spoonacular.com/recipes/findByIngredients").newBuilder();
        urlBuilder.addQueryParameter("apiKey", API_KEY);
        urlBuilder.addQueryParameter("ingredients", String.join(",", pantryIngredients));
        urlBuilder.addQueryParameter("number", "10");

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    Type recipeListType = new TypeToken<List<Recipe>>() {
                    }.getType();
                    List<Recipe> recipeList = gson.fromJson(response.body().string(), recipeListType);

                    recipes.addAll(recipeList);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recipeAdapter.notifyDataSetChanged();
                        }
                    });

                } else {
                    throw new IOException("Unexpected code " + response);
                }
            }
        });
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


    @Override
    public void onResume() {
        super.onResume();
        loadRecipes();
    }

    private void loadSavedRecipes() {
        // retrieve saved recipes from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("saved_recipes", MODE_PRIVATE);
        String savedRecipesJson = sharedPreferences.getString("recipes", null);

        if (savedRecipesJson != null) {
            Gson gson = new Gson();
            Type recipeListType = new TypeToken<List<Recipe>>() {
            }.getType();
            List<Recipe> savedRecipes = gson.fromJson(savedRecipesJson, recipeListType);
            recipes.addAll(savedRecipes);
            recipeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
