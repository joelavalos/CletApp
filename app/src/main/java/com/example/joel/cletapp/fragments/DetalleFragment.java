package com.example.joel.cletapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.joel.cletapp.Communicator;
import com.example.joel.cletapp.R;

/**
 * Created by Joel on 13/12/2015.
 */
public class DetalleFragment extends Fragment {

    private Button ButtonOcultarFragment;
    private DetalleFragment detalleFragment;
    private Communicator communicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_detalle, container, false);

        communicator = (Communicator) getActivity();
        ButtonOcultarFragment = (Button) root.findViewById(R.id.ButtonOcultarFragment);

        ButtonOcultarFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.OcultarDetalle("hola");
            }
        });
        return root;
    }
}
