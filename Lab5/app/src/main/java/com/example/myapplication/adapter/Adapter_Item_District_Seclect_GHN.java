package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.model.District;
import com.example.myapplication.R;
import com.example.myapplication.model.Province;

import java.util.ArrayList;

public class Adapter_Item_District_Seclect_GHN extends ArrayAdapter<District> {
    private Context context;
    private ArrayList<District> list;

    public Adapter_Item_District_Seclect_GHN(@NonNull Context context, ArrayList<District> list) {
        super(context, 0 , list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.simple_spinner_item, parent, false);
        }

        District district = this.getItem(position);

        TextView textView = view.findViewById(R.id.textSpinnerItem);
        textView.setText(district != null ? district.getDistrictName() : "");

        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
