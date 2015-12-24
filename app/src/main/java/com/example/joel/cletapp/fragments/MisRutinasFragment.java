package com.example.joel.cletapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.joel.cletapp.ActivityRutinas;
import com.example.joel.cletapp.ActivityRutinasTerminadas;
import com.example.joel.cletapp.R;

/**
 * Created by Joel on 04/08/2015.
 */
public class MisRutinasFragment extends Fragment {
    //private Button ButtonRutinasPendientes;
    private Button ButtonRutinasFinalizadas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem itemOculto = menu.findItem(R.id.action_search);
        itemOculto.setVisible(false);

        itemOculto = menu.findItem(R.id.action_view);
        itemOculto.setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rutinas_mis_rutinas, container, false);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Rutinas");
        ((ActionBarActivity) getActivity()).getSupportActionBar().setIcon(null);

        inicializarComponentes(root);

        /*
        ButtonRutinasPendientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getActivity().getApplicationContext(), ActivityRutinas.class);
                newIntent.putExtra("Estado", "Pendiente");
                startActivity(newIntent);
            }
        });
        */

        ButtonRutinasFinalizadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getActivity().getApplicationContext(), ActivityRutinasTerminadas.class);
                newIntent.putExtra("Estado", "Terminada");
                startActivity(newIntent);
            }
        });

        return root;
    }

    private void inicializarComponentes(View root) {
        //ButtonRutinasPendientes = (Button) root.findViewById(R.id.ButtonRutinasPendientes);
        ButtonRutinasFinalizadas = (Button) root.findViewById(R.id.ButtonRutinasFinalizadas);
    }
}
