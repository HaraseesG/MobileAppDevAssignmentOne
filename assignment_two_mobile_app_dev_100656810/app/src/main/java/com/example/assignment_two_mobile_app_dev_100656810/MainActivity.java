package com.example.assignment_two_mobile_app_dev_100656810;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Geocoder;
import android.os.Bundle;

import com.example.assignment_two_mobile_app_dev_100656810.ui.fragments.LocationsFragment;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            SQLHelper db = new SQLHelper(MainActivity.this);
            db.getWritableDatabase();

            try {
                if (!(db.getReadableDatabase().rawQuery("SELECT * FROM locsDB;", null).getCount() > 0)) {
                    db.onCreate(db.getWritableDatabase());
                    db.initLoc(new Geocoder(MainActivity.this, Locale.getDefault()));
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, LocationsFragment.newInstance())
                        .commitNow();
            } catch (Exception e) {
                db.onCreate(db.getWritableDatabase());
                db.initLoc(new Geocoder(MainActivity.this, Locale.getDefault()));
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, LocationsFragment.newInstance())
                        .commitNow();
                return;
            }
        }
    }
}

