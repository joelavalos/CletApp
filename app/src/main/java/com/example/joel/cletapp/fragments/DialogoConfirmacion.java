package com.example.joel.cletapp.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.example.joel.cletapp.R;

/**
 * Created by Joel on 07/08/2015.
 */
public class DialogoConfirmacion extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //View viewDialogo = inflater.inflate(R.layout.input_dialog_peso, null);

        builder.setTitle("Dialogo")
                //.setView(viewDialogo)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        /*if (!EditTextValorCampo.getText().toString().equals("")) {
                            valores[posicion] = EditTextValorCampo.getText().toString() + unidad;
                        }
                        AdapterCrearDesafio adapterCrearDesafio = new AdapterCrearDesafio(getActivity().getApplicationContext(), campos, valores);
                        GridViewDatosDesafio.setAdapter(adapterCrearDesafio);*/
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
