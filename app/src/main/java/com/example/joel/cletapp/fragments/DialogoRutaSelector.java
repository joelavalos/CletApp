package com.example.joel.cletapp.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.joel.cletapp.CRUDDatabase.RutaCRUD;
import com.example.joel.cletapp.Communicator;

import java.util.ArrayList;

/**
 * Created by Joel on 23/11/2015.
 */
public class DialogoRutaSelector extends DialogFragment {
    private Communicator comm;

    private ArrayList<String> rutas = new ArrayList<>();
    private ArrayList<String> rutasCoordenadas = new ArrayList<>();
    private int rutaSeleccion;
    private String titulo;
    private String nombreRutaSelccionada;
    private RutaCRUD rutaCRUD;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        comm = (Communicator) getActivity();
        rutaCRUD = new RutaCRUD(getActivity().getApplicationContext());
        rutaSeleccion = -1;

        for (int i = 0; i < rutaCRUD.buscarTodasLasRutas().size(); i++){
            rutas.add(rutaCRUD.buscarTodasLasRutas().get(i).getRutaNombre());
            rutasCoordenadas.add(rutaCRUD.buscarTodasLasRutas().get(i).getRutaCordenadas());
        }

        titulo = getArguments().getString("Titulo");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(titulo)
                .setSingleChoiceItems(rutas.toArray(new String[rutas.size()]), rutaSeleccion, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //valores[posicion] = rutas.toArray(new String[rutas.size()])[whichButton];
                        nombreRutaSelccionada = rutasCoordenadas.toArray(new String[rutasCoordenadas.size()])[whichButton];
                    }
                })
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        comm.SeleccionarRuta(nombreRutaSelccionada);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
