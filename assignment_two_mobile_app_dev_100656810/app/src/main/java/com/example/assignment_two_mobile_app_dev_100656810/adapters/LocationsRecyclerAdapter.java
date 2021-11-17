package com.example.assignment_two_mobile_app_dev_100656810.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_two_mobile_app_dev_100656810.R;
import com.example.assignment_two_mobile_app_dev_100656810.objects.Location;

import java.util.ArrayList;

public class LocationsRecyclerAdapter extends RecyclerView.Adapter<LocationsRecyclerAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private CharSequence previousSearch;
    private ArrayList<Location> display_locs;
    private ArrayList<Location> all_locs;
    private OnLocClickListener onLocClickListener;

    public LocationsRecyclerAdapter(Context context, ArrayList<Location> availableLocs, OnLocClickListener onLocClickListener) {
        this.context = context;
        this.display_locs = availableLocs;
        this.all_locs = new ArrayList<>(display_locs);
        this.onLocClickListener = onLocClickListener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView addr, latitude, longitude;
        CardView cardView;
        OnLocClickListener onLocClickListener;

        public MyViewHolder(@NonNull View itemView, OnLocClickListener onLocClickListener) {
            super(itemView);
            addr = itemView.findViewById(R.id.full_addr);
            latitude = itemView.findViewById(R.id.latitude);
            longitude = itemView.findViewById(R.id.longitude);
            cardView = itemView.findViewById(R.id.recycler_row_container);
            itemView.setOnClickListener(this);
            this.onLocClickListener = onLocClickListener;
        }

        @Override
        public void onClick(View v) {
            this.onLocClickListener.onLocClick(getAdapterPosition());
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.loc_recycler_row, parent, false);
        return new MyViewHolder(view, this.onLocClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.addr.setText(display_locs.get(position).getAddr());
        holder.latitude.setText(Double.toString(display_locs.get(position).getLatitude()));
        holder.longitude.setText(Double.toString(display_locs.get(position).getLongitude()));
    }

    @Override
    public int getItemCount() {
        return display_locs.size();
    }

    public Location getLoc(int position) {
        return display_locs.get(position);
    }

    /* Filter Logic for SearchView
    *       Note: Filter functionality executes on a separate thread*/
    @Override
    public Filter getFilter() {
        return locationFilter;
    }

    private Filter locationFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Location> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(all_locs);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (int i = 0; i < all_locs.size(); i++) {
                    if (all_locs.get(i).getAddr().toLowerCase().contains(filterPattern)) {
                        filteredList.add(all_locs.get(i));
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (filterResults.values != null) {
                previousSearch = charSequence;
                display_locs.clear();
                display_locs.addAll((ArrayList<Location>) filterResults.values);
                notifyDataSetChanged();
            } else if (!charSequence.equals(previousSearch)) {
                display_locs.clear();
                notifyDataSetChanged();
            }
        }
    };

    public interface OnLocClickListener{
        void onLocClick(int position);
    }
}
