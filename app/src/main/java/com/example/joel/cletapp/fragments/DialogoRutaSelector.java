package com.example.joel.cletapp.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.joel.cletapp.Communicator;

import java.util.ArrayList;

/**
 * Created by Joel on 23/11/2015.
 */
public class DialogoRutaSelector extends DialogFragment {
    private Communicator comm;

    private ArrayList<String> rutas = new ArrayList<>();
    private int rutaSeleccion;
    private String titulo;
    private String nombreRutaSelccionada;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        comm = (Communicator) getActivity();
        rutas.add("Ruta 1");
        rutas.add("Ruta 2");
        rutas.add("Ruta 3");

        titulo = getArguments().getString("Titulo");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(titulo)
                .setSingleChoiceItems(rutas.toArray(new String[rutas.size()]), rutaSeleccion, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //valores[posicion] = rutas.toArray(new String[rutas.size()])[whichButton];
                        nombreRutaSelccionada = rutas.toArray(new String[rutas.size()])[whichButton];
                    }
                })
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        comm.SeleccionarRuta(nombreRutaSelccionada);
                        //AdapterCrearDesafio adapterCrearDesafio = new AdapterCrearDesafio(getActivity().getApplicationContext(), campos, valores);
                        //GridViewDatosDesafio.setAdapter(adapterCrearDesafio);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
