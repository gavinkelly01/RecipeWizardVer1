package com.example.test;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Recipe implements Serializable {
    private int id;
    private String title;
    private String imageUrl;
    private List<String> instructions;
    private List<String> ingredients;
    private Map<String, String> nutrition;

    public Recipe(int id, String title, String imageUrl, List<String> instructions,
                  List<String> ingredients, Map<String, String> nutrition) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.instructions = instructions;
        this.ingredients = ingredients;
        this.nutrition = nutrition;
    }

    public Recipe(int id, String title, String imageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static Recipe fromJson(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, Recipe.class);
    }



    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public Map<String, String> getNutrition() {
        return nutrition;
    }

    public void setNutrition(Map<String, String> nutrition) {
        this.nutrition = nutrition;
    }

    public boolean hasAllIngredients(List<String> pantryIngredients) {
        return ingredients.containsAll(pantryIngredients);
    }
}