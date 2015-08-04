package com.example.joel.cletapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joel.cletapp.R;

/**
 * Created by Joel on 04/08/2015.
 */
public class CrearRutinasFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rutina_crear, container, false);
        return root;
    }
}
