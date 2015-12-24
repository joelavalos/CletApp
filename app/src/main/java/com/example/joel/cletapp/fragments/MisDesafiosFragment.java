package com.example.joel.cletapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.joel.cletapp.ActivityDesafios;
import com.example.joel.cletapp.ActivityDesafiosTerminados;
import com.example.joel.cletapp.Mensaje;
import com.example.joel.cletapp.R;

/**
 * Created by Joel on 27/07/2015.
 */
public class MisDesafiosFragment extends Fragment {

    //private Button ButtonDesafiosPendientes;
    private Button ButtonDesafiosTerminados;
    private Button ButtonDesafiosLogrados;
    private Button ButtonDesafiosFallados;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_desafios_mis_desafios, container, false);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Desafios");
        ((ActionBarActivity) getActivity()).getSupportActionBar().setIcon(null);

        inicializarComponentes(root);

        /*
        ButtonDesafiosPendientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getActivity().getApplicationContext(), ActivityDesafios.class);
                newIntent.putExtra("Estado", "Pendiente");
                startActivity(newIntent);
            }
        });
        */

        ButtonDesafiosTerminados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getActivity().getApplicationContext(), ActivityDesafiosTerminados.class);
                newIntent.putExtra("Estado", "Terminado");
                startActivity(newIntent);
            }
        });

        ButtonDesafiosLogrados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getActivity().getApplicationContext(), ActivityDesafiosTerminados.class);
                newIntent.putExtra("Estado", "TerminadoLogrado");
                startActivity(newIntent);
            }
        });

        ButtonDesafiosFallados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getActivity().getApplicationContext(), ActivityDesafiosTerminados.class);
                newIntent.putExtra("Estado", "TerminadoNoLogrado");
                startActivity(newIntent);
            }
        });

        return root;
    }

    private void inicializarComponentes(View root) {
        //ButtonDesafiosPendientes = (Button) root.findViewById(R.id.ButtonDesafiosPendientes);
        ButtonDesafiosTerminados = (Button) root.findViewById(R.id.ButtonDesafiosTerminados);
        ButtonDesafiosLogrados = (Button) root.findViewById(R.id.ButtonDesafiosLogrados);
        ButtonDesafiosFallados = (Button) root.findViewById(R.id.ButtonDesafiosFallados);
    }
}
