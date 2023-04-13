package com.example.test;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeViewHolder extends RecyclerView.ViewHolder {

    private final ImageView recipeImage;
    private final TextView recipeTitle;

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        recipeImage = itemView.findViewById(R.id.recipe_image);
        recipeTitle = itemView.findViewById(R.id.recipe_title);
    }



    public void bind(Recipe recipe) {
        recipeTitle.setText(recipe.getTitle());
        recipeImage.setTag(null); // clear any previous image tags
        if (recipe.getImageUrl() != null) {
        }
    }
}

