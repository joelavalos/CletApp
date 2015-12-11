package com.example.joel.cletapp.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.ListView;

import com.example.joel.cletapp.R;

/**
 * Created by Joel on 24/07/2015.
 */
public class DialogoListSelector extends DialogFragment {

    private ListView ListViewDatosPerfil;
    private String[] campos;
    private String[] valores;
    private String[] sexo = {"Hombre", "Mujer", "Indefinido"};
    private int posicion;
    private int sexoSeleccion;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ListViewDatosPerfil = (ListView) getActivity().findViewById(R.id.ListViewDatosPerfil);

        campos = getArguments().getStringArray("campos");
        valores = getArguments().getStringArray("valores");
        posicion = getArguments().getInt("posicion");

        if (valores[posicion].equals("Hombre")) {
            sexoSeleccion = 0;
        } else if (valores[posicion].equals("Mujer")) {
            sexoSeleccion = 1;
        } else if (valores[posicion].equals("Indefinido")) {
            sexoSeleccion = 2;
        } else {
            sexoSeleccion = -1;
        }

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Seleccione sexo")
                .setSingleChoiceItems(sexo, sexoSeleccion, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        valores[posicion] = sexo[whichButton];
                    }
                })
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        AdapterPerfil adapterPerfil = new AdapterPerfil(getActivity().getApplicationContext(), campos, valores);
                        ListViewDatosPerfil.setAdapter(adapterPerfil);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
