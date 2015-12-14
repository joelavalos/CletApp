package com.example.joel.cletapp.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.example.joel.cletapp.Communicator;

import java.util.ArrayList;

/**
 * Created by Joel on 23/10/2015.
 */
public class DialogoMultiSelect extends DialogFragment {
    private String[] diasSemana = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
    private ArrayList<Integer> selectedItems = new ArrayList();
    private boolean[] selecion = {false, false, false, false, false, false, false};
    private Communicator comm;
    private String diasSeleccionados = "Lunes";



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        comm = (Communicator) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Dias disponibles")
                .setMultiChoiceItems(diasSemana, selecion, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            selectedItems.add(which);
                        } else if (selectedItems.contains(which)) {
                            selectedItems.remove(Integer.valueOf(which));
                        }
                    }
                }).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                comm.DiasSeleccionados(selectedItems);

                Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("detalleRutina");
                if (prev != null) {
                    DialogFragment df = (DialogFragment) prev;
                    df.dismiss();
                }

            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }
}
