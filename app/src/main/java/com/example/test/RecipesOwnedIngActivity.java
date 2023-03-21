package com.example.test;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipesOwnedIngActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipesowneding);

        String[] ingredients = {
                "3 eggs, beaten",
                "1 tsp sunflower oil",
                "1 tsp butter",
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ingredients);
        ListView listView = findViewById(R.id.ingredientsList);
        listView.setAdapter(adapter);
    }
}