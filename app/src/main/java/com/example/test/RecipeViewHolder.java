package com.example.test;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class RecipeViewHolder extends RecyclerView.ViewHolder {

    private ImageView recipeImage;
    private TextView recipeTitle;

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        recipeImage = itemView.findViewById(R.id.recipe_image);
        recipeTitle = itemView.findViewById(R.id.recipe_title);
        /*
        recipeImage.setImageResource(R.drawable.placeholder_image);

         */
    }

    public void bind(Recipe recipe) {
        recipeTitle.setText(recipe.getTitle());
        recipeImage.setTag(null); // clear any previous image tags
        if (recipe.getImageUrl() != null) {
            /*
            Glide.with(recipeImage.getContext())
                    .load(recipe.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .into(recipeImage);

             */
        }
    }
}

