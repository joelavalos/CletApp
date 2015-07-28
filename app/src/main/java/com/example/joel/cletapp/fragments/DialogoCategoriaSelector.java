package com.example.joel.cletapp.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.ListView;

import com.example.joel.cletapp.R;

import java.util.ArrayList;

/**
 * Created by Joel on 28/07/2015.
 */
public class DialogoCategoriaSelector extends DialogFragment {

    private ListView ListViewCrearDesafio;
    private String[] campos;
    private String[] valores;
    private ArrayList<String> categorias = new ArrayList<>();
    //private String[] stockArr;
    private int posicion;
    private int categoriaSeleccion;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ListViewCrearDesafio = (ListView) getActivity().findViewById(R.id.ListViewCrearDesafio);

        campos = getArguments().getStringArray("campos");
        valores = getArguments().getStringArray("valores");
        posicion = getArguments().getInt("posicion");
        categorias = getArguments().getStringArrayList("categorias");

        if (valores[posicion].equals("Distancia")) {
            categoriaSeleccion = 0;
        } else {
            categoriaSeleccion = -1;
        }

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Seleccione categoria")
                .setSingleChoiceItems(categorias.toArray(new String[categorias.size()]), categoriaSeleccion, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        valores[posicion] = categorias.toArray(new String[categorias.size()])[whichButton];
                    }
                })
                .setPositiveButton("Escoger", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        AdapterCrearDesafio adapterCrearDesafio = new AdapterCrearDesafio(getActivity().getApplicationContext(), campos, valores);
                        ListViewCrearDesafio.setAdapter(adapterCrearDesafio);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
