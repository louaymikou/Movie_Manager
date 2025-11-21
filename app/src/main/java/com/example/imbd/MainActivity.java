package com.example.imbd;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    Button btnWatchList, btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnWatchList = findViewById(R.id.Watch_list_btn);
        btnHome = findViewById(R.id.homebtn);

        // Load Home by default
        replaceFrag(new HomeFragment());
        updateButtonStyles(true); // true = Home is active

        // === Navigation Logic ===

        btnHome.setOnClickListener(v -> {
            replaceFrag(new HomeFragment());
            updateButtonStyles(true);
        });

        btnWatchList.setOnClickListener(v -> {
            replaceFrag(new WachListFragmant());
            updateButtonStyles(false);
        });
    }

    private void replaceFrag(Fragment f) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, f)
                .addToBackStack(null) // Allows back button navigation
                .commit();
    }

    // Changes button colors: Active = Red, Inactive = Dark Gray
    private void updateButtonStyles(boolean isHomeActive) {
        int activeColor = Color.parseColor("#E50914"); // Netflix Red
        int inactiveColor = Color.parseColor("#333333"); // Dark Gray

        if (isHomeActive) {
            btnHome.setBackgroundTintList(ColorStateList.valueOf(activeColor));
            btnWatchList.setBackgroundTintList(ColorStateList.valueOf(inactiveColor));
        } else {
            btnHome.setBackgroundTintList(ColorStateList.valueOf(inactiveColor));
            btnWatchList.setBackgroundTintList(ColorStateList.valueOf(activeColor));
        }
    }
}
