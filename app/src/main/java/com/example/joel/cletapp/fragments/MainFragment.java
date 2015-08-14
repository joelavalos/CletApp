package com.example.joel.cletapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.joel.cletapp.HeartRateMonitor;
import com.example.joel.cletapp.Mensaje;
import com.example.joel.cletapp.R;

/**
 * Created by Joel on 21/07/2015.
 */
public class MainFragment extends Fragment {

    private Button ButtonFrecuencioa;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("CletApp");
        ((ActionBarActivity) getActivity()).getSupportActionBar().setIcon(R.drawable.ic_directions_bike_white_18dp);

        ButtonFrecuencioa = (Button) root.findViewById(R.id.ButtonFrecuencioa);

        ButtonFrecuencioa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Mensaje(getActivity().getApplicationContext(), "Hola mundo");
                Intent newIntent = new Intent(getActivity().getApplicationContext(), HeartRateMonitor.class);
                startActivity(newIntent);
            }
        });

        return root;
    }
}
