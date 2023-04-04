// Generated by view binder compiler. Do not edit!
package com.example.test.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.test.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySavedRecipesBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final RecyclerView savedRecipesRecyclerView;

  @NonNull
  public final TextView titleTextView;

  private ActivitySavedRecipesBinding(@NonNull ConstraintLayout rootView,
      @NonNull RecyclerView savedRecipesRecyclerView, @NonNull TextView titleTextView) {
    this.rootView = rootView;
    this.savedRecipesRecyclerView = savedRecipesRecyclerView;
    this.titleTextView = titleTextView;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySavedRecipesBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySavedRecipesBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_saved_recipes, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySavedRecipesBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.saved_recipes_recycler_view;
      RecyclerView savedRecipesRecyclerView = ViewBindings.findChildViewById(rootView, id);
      if (savedRecipesRecyclerView == null) {
        break missingId;
      }

      id = R.id.title_text_view;
      TextView titleTextView = ViewBindings.findChildViewById(rootView, id);
      if (titleTextView == null) {
        break missingId;
      }

      return new ActivitySavedRecipesBinding((ConstraintLayout) rootView, savedRecipesRecyclerView,
          titleTextView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
