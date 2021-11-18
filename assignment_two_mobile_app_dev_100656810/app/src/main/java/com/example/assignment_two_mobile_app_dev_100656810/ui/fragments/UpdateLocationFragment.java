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


public class UpdateLocationFragment extends Fragment {
    int id;

    Button delete, save;
    TextView title;
    EditText latitude, longitude;

    SQLHelper db;

    public static UpdateLocationFragment newInstance() {
        return new UpdateLocationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Work in onCreateView to use the data from the LocationsFragment
        View mainView = inflater.inflate(R.layout.update_location_fragment, container, false);
        db = new SQLHelper(getContext());

        title = (TextView) mainView.findViewById(R.id.main_title);
        latitude = (EditText) mainView.findViewById(R.id.latitude_val);
        longitude = (EditText) mainView.findViewById(R.id.longitude_val);

        save = (Button) mainView.findViewById(R.id.save_loc);
        delete = (Button) mainView.findViewById(R.id.delete_loc);

        getParentFragmentManager().setFragmentResultListener("parameters_to_update", this, (requestKey, bundle) -> {
            String[] parameters = bundle.getStringArray("params");

            setCurrId(Integer.parseInt(parameters[0]));
            title.setText(parameters[1].trim());
            latitude.setText(parameters[2].trim());
            longitude.setText(parameters[3].trim());
        });

        save.setOnClickListener((view) -> {
            onDonePressed(latitude, longitude);
        });

        delete.setOnClickListener((view) -> {
            try {
                db.deleteLoc(Integer.toString(getCurrId()));
                Toast.makeText(getContext(), "Deletion successful", Toast.LENGTH_LONG).show();
                Fragment locationsFragment = new LocationsFragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.location_main, locationsFragment);
                transaction.disallowAddToBackStack();
                transaction.commit();
            } catch (Exception e) { // We return to allow the app to continue running without crashing.
                Toast.makeText(getContext(), "Deletion did not succeed at this time", Toast.LENGTH_LONG).show();
                return;
            }
        });

        return mainView;
    }

    public void onDonePressed(EditText latitude, EditText longitude) {
        // If the edit text fields are not empty
        if (latitude.getText().toString().length() > 0 && longitude.getText().toString().length() > 0) {
            try { // update the location and return to the main fragment
                db.updateLoc(Integer.toString(getCurrId()), new Geocoder(getContext(), Locale.getDefault()).getFromLocation(Double.parseDouble(latitude.getText().toString()), Double.parseDouble(longitude.getText().toString()), 1).get(0).getAddressLine(0).trim(), Double.parseDouble(latitude.getText().toString().trim()), Double.parseDouble(longitude.getText().toString().trim()));

                Fragment locationsFragment = new LocationsFragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.location_main, locationsFragment);
                transaction.disallowAddToBackStack();
                transaction.commit();
            } catch (IOException e) { // if the provided input was invalid
                Toast.makeText(getContext(), "You must provide a VALID longitude and latitude to save a new location", Toast.LENGTH_LONG).show();
                return;
            }
        } else { // if any input field was left empty
            Toast.makeText(getContext(), "You must provide a longitude and latitude to save a new location", Toast.LENGTH_LONG).show();
        }
    }

    // given Id of resource to be edited
    public int getCurrId() {
        return id;
    }

    // setter for id of resource to be edited
    public void setCurrId(int id) {
        this.id = id;
    }
}
