package com.example.test;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class FeaturedRecipesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featuredrecipes);

        String[] ingredients = {
                "6 tbsp / 90g unsalted butter, softened (or salted butter)",
                "2 tsp parsley, finely chopped",
                "2 garlic cloves, very finely minced (2 tsp)",
                "1/4 tsp salt (skip if using salted butter)",
                "2 x 220 – 250g (7 – 8oz) chicken breast, skinless and boneless (Note 1)",
                "1/2 tsp salt",
                "1/4 tsp pepper",
                "1 egg, lightly whisked",
                "35 g flour",
                "50 g panko breadcrumbs (Note 2)",
                "Oil for frying, canola or vegetable oil (~4 cups / 1 litre)"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ingredients);
        ListView listView = findViewById(R.id.ingredientsList);
        listView.setAdapter(adapter);
    }
}
