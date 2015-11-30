package com.example.joel.cletapp.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.joel.cletapp.Communicator;
import com.example.joel.cletapp.R;

/**
 * Created by Joel on 30/11/2015.
 */
public class DialogoNombreRuta extends DialogFragment {
    private Communicator comm;
    private EditText EditTextValorCampo;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View viewDialogo = inflater.inflate(R.layout.input_dialog_ruta, null);
        comm = (Communicator) getActivity();
        EditTextValorCampo = (EditText) viewDialogo.findViewById(R.id.EditTextValorCampo);
        EditTextValorCampo.setText("");
        EditTextValorCampo.setHint("Nombre");

        builder.setTitle("Nombre de la ruta")
                .setView(viewDialogo)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        if (!EditTextValorCampo.getText().toString().equals("")){
                            comm.GuardarRuta(EditTextValorCampo.getText().toString());
                        }
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
