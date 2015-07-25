package com.example.joel.cletapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joel.cletapp.R;

/**
 * Created by Joel on 20/07/2015.
 */
public class RutinaFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rutina, container, false);

        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Rutinas");
        ((ActionBarActivity) getActivity()).getSupportActionBar().setIcon(null);

        return root;
    }
}
