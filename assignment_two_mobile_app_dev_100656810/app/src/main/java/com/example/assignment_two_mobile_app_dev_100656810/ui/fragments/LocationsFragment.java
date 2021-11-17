package com.example.assignment_two_mobile_app_dev_100656810.ui.fragments;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.assignment_two_mobile_app_dev_100656810.R;
import com.example.assignment_two_mobile_app_dev_100656810.SQLHelper;
import com.example.assignment_two_mobile_app_dev_100656810.adapters.LocationsRecyclerAdapter;
import com.example.assignment_two_mobile_app_dev_100656810.objects.Location;

import java.util.ArrayList;

public class LocationsFragment extends Fragment implements LocationsRecyclerAdapter.OnLocClickListener {
    RecyclerView recyclerView;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    int version = 1;

    ArrayList<Location> availableLocations = new ArrayList<>();
    LocationsRecyclerAdapter locationsRecyclerAdapter;

    public static LocationsFragment newInstance() {
        return new LocationsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.locations_fragment, container, false);
    }

    @Nullable
    @Override
    public void onViewCreated(View mainView, @Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SQLHelper db = new SQLHelper(getContext());
        db.getReadableDatabase();

        ImageView addLocation = (ImageView) mainView.findViewById(R.id.addLoc);
        SearchView searchView = (SearchView) mainView.findViewById(R.id.searchLocs);
        recyclerView = mainView.findViewById(R.id.main_content);

        displayLocations(db);

        locationsRecyclerAdapter = new LocationsRecyclerAdapter(getContext(), availableLocations, this);
        recyclerView.setAdapter(locationsRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addLocation.setOnClickListener((view) -> {
            Fragment locationFragment = new NewLocationFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

            transaction.replace(R.id.main, locationFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String userSelection) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String userSelection) {
                locationsRecyclerAdapter.getFilter().filter(userSelection);
                return false;
            }
        });
    }

    private void displayLocations(SQLHelper db) {
        Cursor cursor = db.readAll();
        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "no data.", Toast.LENGTH_SHORT).show();
        } else {
            availableLocations = new ArrayList<>();
            while (cursor.moveToNext()) {
                Location temp_loc = new Location(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Double.parseDouble(cursor.getString(2)), Double.parseDouble(cursor.getString(3)));
                if (!availableLocations.contains(temp_loc)) {
                    availableLocations.add(temp_loc);
                }
            }
            Toast.makeText(getContext(), Integer.toString(availableLocations.size()), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocClick(int position) {
        Location location = locationsRecyclerAdapter.getLoc(position);

        Bundle result = new Bundle();
        String[] parameters = {String.valueOf(location.getId()), location.getAddr(), Double.toString(location.getLatitude()), Double.toString(location.getLongitude())};
        result.putStringArray("params", parameters);
        getParentFragmentManager().setFragmentResult("parameters_to_update", result);

        Fragment locationFragment = new UpdateLocationFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        transaction.replace(R.id.main, locationFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}