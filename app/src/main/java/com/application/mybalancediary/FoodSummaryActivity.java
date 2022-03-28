package com.application.mybalancediary;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class FoodSummaryActivity extends AppCompatActivity {
    float food_fat;
    float food_carbs;
    float food_protein;
    float max_fat = 70f;
    float max_carbs = 300f;
    float max_protein = 180f;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodsummary);
        final ProgressBar fats = findViewById(R.id.fats_progress);
        final ProgressBar carbs = findViewById(R.id.carbs_progress);
        final ProgressBar protein = findViewById(R.id.protein_progress);
        final TextView  text_fats=findViewById(R.id.text_view_fats);
        final TextView  text_carbs=findViewById(R.id.text_view_carbs);
        final TextView  text_protein=findViewById(R.id.text_view_protein);

        food_carbs = Food_MyRecyclerViewAdapter.totalcarbs;
        food_fat = Food_MyRecyclerViewAdapter.totalfat;
        food_protein = Food_MyRecyclerViewAdapter.totalprotein;
        Log.d("Food Summary", String.valueOf(food_carbs) + String.valueOf(food_fat) + String.valueOf(food_protein));

        // Animation
        TranslateAnimation translation;
        translation = new TranslateAnimation(0f, 0F, 0f, 180);
        translation.setStartOffset(100);
        translation.setDuration(2000);
        translation.setFillAfter(true);
        translation.setInterpolator(new BounceInterpolator());

        TranslateAnimation translation1;
        translation1 = new TranslateAnimation(0f, 0F, 0f, 370);
        translation1.setStartOffset(100);
        translation1.setDuration(2000);
        translation1.setFillAfter(true);
        translation1.setInterpolator(new BounceInterpolator());

        // Fats Progress Bar
        if (food_fat > 0) {
            fats.setProgress(Math.round((100 * (food_fat)) / max_fat));
        } else
            fats.setProgress(Math.round((100 * (LoginActivity.user_fat)) / max_fat));
        if (food_fat > 0) {
            text_fats.setText(String.valueOf(food_fat));
        } else {
            text_fats.setText(String.valueOf(LoginActivity.user_fat));
        }
        fats.setBackgroundColor(Color.LTGRAY);
        fats.startAnimation(translation);

        // Carbs Progress Bar
        if (food_carbs > 0) {
            carbs.setProgress(Math.round((100 * (food_carbs)) / max_carbs));
        } else
            carbs.setProgress(Math.round((100 * (LoginActivity.user_carbs)) / max_carbs));
        carbs.startAnimation(translation);
        if (food_carbs > 0) {
            text_carbs.setText(String.valueOf(food_carbs));
        } else {
           text_carbs.setText(String.valueOf(LoginActivity.user_carbs));
        }
        carbs.setBackgroundColor(Color.LTGRAY);

        // protein Progress Bar
        if (food_protein > 0) {
            protein.setProgress(Math.round((100 * (food_protein)) / max_protein));
        } else
            protein.setProgress(Math.round((100 * (LoginActivity.user_protein)) / max_protein));
        if (food_protein > 0) {
            text_protein.setText(String.valueOf(food_protein));
        } else {
            text_protein.setText(String.valueOf(LoginActivity.user_protein));
        }
        protein.setBackgroundColor(Color.LTGRAY);
        protein.setAnimation(translation1);

    }
    private int getDisplayHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }
}
