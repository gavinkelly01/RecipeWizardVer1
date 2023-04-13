package com.example.test;

import android.content.Intent;
import android.os.Bundle;

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
    private final String API_KEY = "3bd64de960774274af7148c4123df14a"; // replace with your Spoonacular API key




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
        recipes = new ArrayList<>();
        pantryIngredients = getPantryIngredients();
        recipeAdapter = new RecipeAdapter(recipes, this);
        recipeAdapter.setOnRecipeClickListener(this);
        recyclerView.setAdapter(recipeAdapter);
        loadRecipes();

    }



    private List<String> getPantryIngredients() {
        Intent intent = getIntent();
        String ingredientsJson = intent.getStringExtra("ingredients");
        Type type = new TypeToken<List<Ingredient>>(){}.getType();
        Gson gson = new Gson();
        List<Ingredient> ingredients = gson.fromJson(ingredientsJson, type);
        //This maps the ingredients to their names and then returns this list
        return ingredients.stream()
                .map(Ingredient::getName)
                .collect(Collectors.toList());
    }

    private void loadRecipes() {
        OkHttpClient client = new OkHttpClient();
    //This here is the URL for the api request to show 10 recipes using the items in the pantry
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
                    //This adds the response into the list of objects for the recipe.
                    Gson gson = new Gson();
                    Type recipeListType = new TypeToken<List<Recipe>>(){}.getType();
                    List<Recipe> recipeList = gson.fromJson(response.body().string(), recipeListType);
                    //This adds the recipes to the list and then it updates the UI
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
    public void onRecipeLongClick(int position) {

    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra("recipe", recipe);
        startActivity(intent);
    }


}
