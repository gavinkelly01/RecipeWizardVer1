package com.example.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private final List<Recipe> recipes;
    private final Context context;
    private final ImageLoader imageLoader;
    private boolean isVeganChecked = false;
    private boolean isVegetarianChecked = false;
    private boolean isGlutenFreeChecked = false;
    private boolean isPaleoChecked = false;
    private OnRecipeClickListener onRecipeClickListener;
    private OnItemClickListener listener;

    //RecipeAdapter is a class that takes a list of recipes and displays them in a RecyclerView.
    public RecipeAdapter(List<Recipe> recipes, Context context) {
        this.recipes = recipes;
        this.context = context;
        this.imageLoader = new ImageLoader(Volley.newRequestQueue(context), new ImageLoader.ImageCache() {
            //LRUCache is a dependency I used to show more than 20 images at once, otherwise this would exceed memory usage.
            //LRUCache and Bitmap implementation was found from http://android-er.blogspot.com/2012/07/caching-bitmaps-with-lrucache.html
            private final LruCache<String, Bitmap> cache = new LruCache<>(20);
            //Implement getBitmap() and putBitmap() to get and put images in the cache

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Recipe recipe, boolean isSaved);
    }


    //Create a RecipeViewHolder for each item in the RecyclerView.
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //This inflates the layout for each recipe item so each image and title fit perfectly.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    //This below binds the data to the view for each recipe item so that the user can interact with only one recipe item.
    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        //Get the recipe object at the specified position in the list
        Recipe recipe = recipes.get(position);
        //Set the title of the recipe in the TextView
        holder.titleTextView.setText(recipe.getTitle());
        //This constructs the URL for the recipe image using the ID of the recipe

        String imageUrl = String.format("https://spoonacular.com/recipeImages/%d-240x150.%s", recipe.getId(), "jpg"); // added file extension
        //Loads the image to the placeholder on the recipe item location using the ImageLoader and sets the image in the ImageView when it loads successfully.
        imageLoader.get(imageUrl, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bitmap = response.getBitmap();
                holder.imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RecipeAdapter", "Error loading image: " + error.getMessage());
            }
        });
        //Calls the onRecipeClickListener's onRecipeClick() method when clicked
         holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecipeClickListener.onRecipeClick(recipe);
            }
        });
    }

    //Gets the number of items in the RecyclerView.
    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void setOnRecipeClickListener(OnRecipeClickListener listener) {
        this.onRecipeClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);

        void onRecipeClick(int position);

        void onRecipeLongClick(int position);
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView imageView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.recipe_title);
            imageView = itemView.findViewById(R.id.recipe_image);
        }
    }

    public void setVeganChecked(boolean isChecked) {
        isVeganChecked = isChecked;
    }

    public void setVegetarianChecked(boolean isChecked) {
        isVegetarianChecked = isChecked;
    }

    public void setGlutenFreeChecked(boolean isChecked) {
        isGlutenFreeChecked = isChecked;
    }

    public void setPaleoChecked(boolean isChecked) {
        isPaleoChecked = isChecked;
    }
}
