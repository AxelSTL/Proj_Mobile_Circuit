package com.example.book2run.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.book2run.R;
import com.example.book2run.model.Circuit;
import com.example.book2run.ui.dashboard.DashboardFragment;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<Circuit> {

    public ListViewAdapter(@NonNull Context context, ArrayList<Circuit> items) {
        super(context, 0, items);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listCircuits = convertView;
        if (listCircuits == null) {
            listCircuits = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dashboard, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        Circuit currentCircuit = getItem(position);

        //ImageView picture = listItem.findViewById(R.id.IvPicture);
        //picture.setBackgroundResource(currentItem.getPicture());

        TextView title = listCircuits.findViewById(R.id.listCircuit_name);
        title.setText(currentCircuit.getNom());

        TextView description = listCircuits.findViewById(R.id.listCircuit_description);
        title.setText(currentCircuit.getDescription());


        return listCircuits;
    }

}