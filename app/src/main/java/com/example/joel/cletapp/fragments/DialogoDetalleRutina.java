package com.example.joel.cletapp.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.joel.cletapp.Communicator;
import com.example.joel.cletapp.R;

import java.util.ArrayList;

/**
 * Created by Joel on 14/12/2015.
 */
public class DialogoDetalleRutina extends DialogFragment {
    private Button ButtonRuttinaRapida;
    private Button ButtonRuttinaPersonalizada;
    private boolean estado = false;
    private View view;

    private String[] diasSemana = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
    private ArrayList<Integer> selectedItems = new ArrayList();
    private boolean[] selecion = {false, false, false, false, false, false, false};
    private Communicator comm;
    private String diasSeleccionados = "Lunes";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        estado = getArguments().getBoolean("estado");

        if (estado == true) {
            view = inflater.inflate(R.layout.fragment_detalle_rutina, null);
            ButtonRuttinaRapida = (Button) view.findViewById(R.id.ButtonRuttinaRapida);
            ButtonRuttinaPersonalizada = (Button) view.findViewById(R.id.ButtonRuttinaPersonalizada);

            ButtonRuttinaRapida.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogoMultiSelect dialogo = new DialogoMultiSelect();
                    dialogo.show(getFragmentManager(), "rutinaRapida");
                }
            });

            ButtonRuttinaPersonalizada.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogoRutinaSelector dialogo = new DialogoRutinaSelector();
                    dialogo.show(getFragmentManager(), "rutinaPersonalizada");
                }
            });
        } else {
            view = inflater.inflate(R.layout.fragment_detalle_rutina2, null);
        }
        builder.setView(view);

        return builder.create();
    }
}
