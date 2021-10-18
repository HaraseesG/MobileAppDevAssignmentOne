package com.example.mobileappdevassignmentone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mobileappdevassignmentone.ui.main.CalculatorFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CalculatorFragment.newInstance())
                    .commitNow();
        }
    }
}