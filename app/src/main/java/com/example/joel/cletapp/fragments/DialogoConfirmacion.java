package com.example.joel.cletapp.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.joel.cletapp.Communicator;

/**
 * Created by Joel on 07/08/2015.
 */
public class DialogoConfirmacion extends DialogFragment {
    private Communicator comm;
    private String accion = "";
    private String mensaje = "";
    private String titulo = "";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        accion = getArguments().getString("Accion");
        mensaje = getArguments().getString("Mensaje");
        titulo = getArguments().getString("Titulo");

        /*if (accion.equals("Actualizar")) {
            mensaje = "Seguro que desea actualizar el desafio?";
        } else if (accion.equals("Reiniciar")) {
            mensaje = "Esta accion reiniciara la agenda, desesa continuar?";
        } else {
            mensaje = "Seguro que desea eliminar el desafio?";
        }*/


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //View viewDialogo = inflater.inflate(R.layout.input_dialog_peso, null);

        comm = (Communicator) getActivity();

        builder.setTitle(titulo)
                //.setView(viewDialogo)
                .setMessage(mensaje)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (accion.equals("Actualizar")) {
                            comm.Actualizar("Aceptar");
                        } else if (accion.equals("Reiniciar")) {
                            comm.Reiniciar("Aceptar");
                        } else {
                            comm.Eliminar("Aceptar");
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (accion.equals("Actualizar")) {
                            comm.Actualizar("Cancelar");
                        } else if (accion.equals("Reiniciar")) {
                            comm.Reiniciar("Cancelar");
                        } else {
                            comm.Eliminar("Cancelar");
                        }
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
