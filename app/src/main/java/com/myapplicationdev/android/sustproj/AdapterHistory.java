package com.myapplicationdev.android.sustproj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterHistory extends ArrayAdapter<Prescription> {
    private Context context;
    private ArrayList<Prescription> objects;
    private TextView tv1, tv2, tv3;

    public AdapterHistory(Context context, int resource, ArrayList<Prescription> stores) {
        super(context, resource, stores);
        this.context = context;
        objects = stores;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_history, parent, false);
        tv1 = rowView.findViewById(R.id.tvBnfName2);
        tv2 = rowView.findViewById(R.id.tvQuantity2);
        tv3 = rowView.findViewById(R.id.tvDate);


        //Match the UI components with Java variables

        Prescription prescription = objects.get(position);
        tv1.setText(prescription.getBnfName());
        tv2.setText("Quantity: " + prescription.getQuantity());
        tv3.setText(prescription.getDate());

        return rowView;
    }
}