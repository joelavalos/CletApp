package com.example.joel.cletapp.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

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
                        } else if (accion.equals("DetenerRutina")) {
                            comm.Eliminar("Aceptar");
                        }else if (accion.equals("RutinaTerminada")) {
                            comm.TerminarRutina("Aceptar");
                        } else {
                            comm.Eliminar("Aceptar");
                        }
                        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("detalleRutina");
                        if (prev != null) {
                            DialogFragment df = (DialogFragment) prev;
                            df.dismiss();
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (accion.equals("Actualizar")) {
                            comm.Actualizar("Cancelar");
                        } else if (accion.equals("Reiniciar")) {
                            comm.Reiniciar("Cancelar");
                        } else if (accion.equals("DetenerRutina")) {
                            comm.Eliminar("Cancelar");
                        }else if (accion.equals("RutinaTerminada")) {
                            comm.TerminarRutina("Cancelar");
                        } else {
                            comm.Eliminar("Cancelar");
                        }
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
