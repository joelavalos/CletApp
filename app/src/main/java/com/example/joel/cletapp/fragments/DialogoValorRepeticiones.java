package com.example.joel.cletapp.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.example.joel.cletapp.R;

/**
 * Created by Joel on 20/11/2015.
 */
public class DialogoValorRepeticiones extends DialogFragment {

    private GridView GridViewDatosDesafio;
    private String[] campos;
    private String[] valores;
    private int posicion;
    private String titulo;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        GridViewDatosDesafio = (GridView) getActivity().findViewById(R.id.GridViewDatosDesafio);

        campos = getArguments().getStringArray("campos");
        valores = getArguments().getStringArray("valores");
        posicion = getArguments().getInt("posicion");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View viewDialogo = inflater.inflate(R.layout.input_dialog_peso, null);
        final EditText EditTextValorCampo = (EditText) viewDialogo.findViewById(R.id.EditTextValorCampo);
        final TextView TextViewUnidad = (TextView) viewDialogo.findViewById(R.id.TextViewUnidad);

        titulo = "Cantidad de repeticiones";
        EditTextValorCampo.setHint("Repeticiones");
        TextViewUnidad.setText("");

        builder.setTitle(titulo)
                .setView(viewDialogo)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        if (!EditTextValorCampo.getText().toString().equals("")) {
                            valores[posicion] = EditTextValorCampo.getText().toString();
                        }
                        AdapterCrearDesafio adapterCrearDesafio = new AdapterCrearDesafio(getActivity().getApplicationContext(), campos, valores);
                        GridViewDatosDesafio.setAdapter(adapterCrearDesafio);

                        InputMethodManager inputManager = (InputMethodManager)
                                getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(EditTextValorCampo.getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        return builder.create();
    }
}
