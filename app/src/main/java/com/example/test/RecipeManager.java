package com.example.test;

import java.util.ArrayList;
import java.util.List;

public class RecipeManager {
    private final List<Recipe> allRecipes;

    public RecipeManager() {
        allRecipes = new ArrayList<>();
    }

    public void addRecipe(Recipe recipe) {
        allRecipes.add(recipe);
    }

    public List<Recipe> getRecipes() {
        return allRecipes;
    }

    private boolean recipeRequiresIngredients(Recipe recipe, List<Ingredient> ingredients) {
        List<String> recipeIngredients = recipe.getIngredients();
        for (String recipeIngredient : recipeIngredients) {
            if (!ingredients.contains(recipeIngredient)) {
                return true;
            }
        }
        return false;
    }
}
