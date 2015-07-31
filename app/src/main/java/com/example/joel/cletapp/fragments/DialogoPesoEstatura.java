package com.example.joel.cletapp.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joel.cletapp.R;

/**
 * Created by Joel on 24/07/2015.
 */
public class DialogoPesoEstatura extends DialogFragment {

    private ListView ListViewDatosPerfil;
    private String[] campos;
    private String[] valores;
    private int posicion;
    private String titulo;
    private String unidad;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ListViewDatosPerfil = (ListView) getActivity().findViewById(R.id.ListViewDatosPerfil);

        campos = getArguments().getStringArray("campos");
        valores = getArguments().getStringArray("valores");
        posicion = getArguments().getInt("posicion");

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View viewDialogo = inflater.inflate(R.layout.input_dialog_peso, null);
        final EditText EditTextValorCampo = (EditText) viewDialogo.findViewById(R.id.EditTextValorCampo);
        final TextView TextViewUnidad = (TextView) viewDialogo.findViewById(R.id.TextViewUnidad);

        if (posicion == 3) {
            titulo = "Ingrese su peso";
            EditTextValorCampo.setHint("Peso");
            TextViewUnidad.setText("Kg");
            unidad = " Kg";
        } else {
            titulo = "Ingrese su estatura";
            EditTextValorCampo.setHint("Estatura");
            TextViewUnidad.setText("m");
            unidad = " m";
        }

        builder.setTitle(titulo)
                .setView(viewDialogo)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        if (!EditTextValorCampo.getText().toString().equals("")) {
                            valores[posicion] = EditTextValorCampo.getText().toString() + unidad;
                        }
                        AdapterPerfil adapterPerfil = new AdapterPerfil(getActivity().getApplicationContext(), campos, valores);
                        ListViewDatosPerfil.setAdapter(adapterPerfil);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
