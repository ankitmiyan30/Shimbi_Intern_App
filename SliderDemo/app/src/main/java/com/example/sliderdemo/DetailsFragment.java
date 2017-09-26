package com.example.sliderdemo;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailsFragment extends Fragment {

    private TextView details;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle args) {
        View view = inflater.inflate(R.layout.activity_details_fragment, container, false);
        details= view.findViewById(R.id.display_slider);
        String menus=getArguments().getString("menus");
        details.setText(menus);
        return view;
    }
}
