package com.example.assignment_two_mobile_app_dev_100656810.ui.fragments;

import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.assignment_two_mobile_app_dev_100656810.R;
import com.example.assignment_two_mobile_app_dev_100656810.SQLHelper;

import java.io.IOException;
import java.util.Locale;


public class NewLocationFragment extends Fragment {
    Button delete, save;
    TextView title;
    EditText latitude, longitude;

    SQLHelper db;

    public static NewLocationFragment newInstance() {
        return new NewLocationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View mainView = inflater.inflate(R.layout.new_location_fragment, container, false);
        db = new SQLHelper(getContext());

        title = (TextView) mainView.findViewById(R.id.main_title);
        latitude = (EditText) mainView.findViewById(R.id.latitude_val);
        longitude = (EditText) mainView.findViewById(R.id.longitude_val);

        save = (Button) mainView.findViewById(R.id.save_loc);
        delete = (Button) mainView.findViewById(R.id.delete_loc);

        save.setOnClickListener((view) -> {
            onDonePressed(latitude, longitude);
        });

        delete.setOnClickListener((view) -> {
            Fragment locationsFragment = new LocationsFragment();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.location_main, locationsFragment);
            transaction.disallowAddToBackStack();
            transaction.commit();
        });

        return mainView;
    }

    public void onDonePressed(EditText latitude, EditText longitude) {
        // if input fields are not empty
        if (latitude.getText().toString().length() > 0 && longitude.getText().toString().length() > 0) {
            try { // if the input is valid
                db.addNewLoc(new Geocoder(getContext(), Locale.getDefault()).getFromLocation(Double.parseDouble(latitude.getText().toString()), Double.parseDouble(longitude.getText().toString()), 1).get(0).getAddressLine(0).trim(),
                        Double.parseDouble(latitude.getText().toString().trim()),
                        Double.parseDouble(longitude.getText().toString().trim()));

                Fragment locationsFragment = new LocationsFragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.location_main, locationsFragment);
                transaction.disallowAddToBackStack();
                transaction.commit();
            } catch (IOException e) { // if the input is invalid
                Toast.makeText(getContext(), "You must provide a VALID longitude and latitude to save a new location", Toast.LENGTH_LONG).show();
                return;
            }
        } else { // if an input field was empty
            Toast.makeText(getContext(), "You must provide a longitude and latitude to save a new location", Toast.LENGTH_LONG).show();
        }
    }
}
