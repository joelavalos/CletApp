package com.example.joel.cletapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joel.cletapp.Mensaje;
import com.example.joel.cletapp.R;

/**
 * Created by Joel on 27/07/2015.
 */
public class CrearDesafiosFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_desafio_crear, container, false);
        Mensaje qwe = new Mensaje(getActivity().getApplicationContext(), "FragmentCrearDesafios creado");

        return root;
    }
}
