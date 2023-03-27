package com.example.test;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

    public class PrefManager {
        private final SharedPreferences pref;
        private final SharedPreferences.Editor editor;
        private final Context context;
        private final int PRIVATE_MODE = 0;
        private static final String PREF_NAME = "my_pref";
        private static final String INGREDIENTS_KEY = "ingredients";

        public PrefManager(Context context) {
            this.context = context;
            pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            editor = pref.edit();
        }

        public void saveIngredients(ArrayList<Ingredient> ingredients) {
            Gson gson = new Gson();
            String json = gson.toJson(ingredients);
            editor.putString(INGREDIENTS_KEY, json);
            editor.apply();
        }

        public ArrayList<Ingredient> getIngredients() {
            Gson gson = new Gson();
            String json = pref.getString(INGREDIENTS_KEY, "");
            Type type = new TypeToken<ArrayList<Ingredient>>() {}.getType();
            return gson.fromJson(json, type);
        }
    }


