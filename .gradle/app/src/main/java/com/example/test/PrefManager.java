package com.example.test;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

//Class to manage shared preferences for the ingredients list
public class PrefManager {

    //Initialize variables for shared preferences and editor
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final Context context;

    //Set private mode for shared preferences
    private final int PRIVATE_MODE = 0;
    //Set name for shared preferences and keys for ingredients and recipes
    private static final String PREF_NAME = "my_pref";
    private static final String INGREDIENTS_KEY = "ingredients";
    private static final String RECIPES_KEY = "recipes";

    //Constructor to initialize shared preferences and editor
    public PrefManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Method to save ingredients list to shared preferences
    public void saveIngredients(ArrayList<Ingredient> ingredients) {
        // Convert ingredients list to JSON format and store in shared preferences
        Gson gson = new Gson();
        String json = gson.toJson(ingredients);
        editor.putString(INGREDIENTS_KEY, json);
        editor.apply();
    }

    //Method to retrieve ingredients list from shared preferences
    public ArrayList<Ingredient> getIngredients() {
        // Retrieve ingredients list from shared preferences and convert from JSON format
        Gson gson = new Gson();
        String json = pref.getString(INGREDIENTS_KEY, "");
        Type type = new TypeToken<ArrayList<Ingredient>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    //Method to save recipes list to shared preferences
    public void saveRecipes(ArrayList<Recipe> recipes) {
        Gson gson = new Gson();
        String json = gson.toJson(recipes);
        editor.putString("recipes", json);
        editor.apply();
    }


    //Method to retrieve recipes list from shared preferences
    public ArrayList<Recipe> getRecipes() {
        Gson gson = new Gson();
        String json = pref.getString("recipes", "");
        Type type = new TypeToken<ArrayList<Recipe>>() {}.getType();
        ArrayList<Recipe> recipes = gson.fromJson(json, type);
        if (recipes == null) {
            recipes = new ArrayList<>();
        }
        return recipes;
    }

}
