package com.example.joel.cletapp.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.ListView;

import com.example.joel.cletapp.Mensaje;
import com.example.joel.cletapp.R;

import java.util.ArrayList;

/**
 * Created by Joel on 05/08/2015.
 */
public class DialogoDesafioSelector extends DialogFragment {
    private ListView ListViewDesafiosRutina;
    private String[] camposDesafios;
    private String[] valoresDesafios;
    private int[] idDesafios;
    private ArrayList<String> desafios = new ArrayList<>();
    private AdapterDesafio adapterDesafio;

    private int posicion;
    private int desafioSeleccion;
    private int ultimaSeleccion;
    private String ultimaSeleccionDesafio;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Mensaje asd = new Mensaje(getActivity().getApplicationContext(), "Inicializo el dialogo");
        ListViewDesafiosRutina = (ListView) getActivity().findViewById(R.id.ListViewDesafiosRutina);
        camposDesafios = getArguments().getStringArray("camposDesafios");
        valoresDesafios = getArguments().getStringArray("valoresDesafios");
        idDesafios = getArguments().getIntArray("idDesafios");
        posicion = getArguments().getInt("posicion");
        desafios = getArguments().getStringArrayList("desafios");

        desafioSeleccion = -1;
        if (valoresDesafios[posicion].equals("Descansar")) {
            desafioSeleccion = 0;
            ultimaSeleccion = 0;
        }
        ultimaSeleccionDesafio = valoresDesafios[posicion];

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Seleccione desafio")
                .setSingleChoiceItems(desafios.toArray(new String[desafios.size()]), desafioSeleccion, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ultimaSeleccion = whichButton;
                    }
                })
                .setPositiveButton("Escoger", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        valoresDesafios[posicion] = desafios.toArray(new String[desafios.size()])[ultimaSeleccion];

                        if (valoresDesafios[posicion].equals("Descansar")) {
                            if (!desafios.contains(ultimaSeleccionDesafio) && !ultimaSeleccionDesafio.equals("")) {
                                desafios.add(ultimaSeleccionDesafio);
                                idDesafios[posicion] = -1;
                            }
                        } else {
                            idDesafios[posicion] = Integer.parseInt(desafios.toArray(new String[desafios.size()])[ultimaSeleccion].split("-")[0]);
                            desafios.remove(valoresDesafios[posicion]);
                            if (ultimaSeleccionDesafio.equals(valoresDesafios[posicion])) {

                            } else {
                                if (!ultimaSeleccionDesafio.equals("") && !ultimaSeleccionDesafio.equals("Descansar")) {
                                    desafios.add(ultimaSeleccionDesafio);
                                }
                            }
                        }

                        adapterDesafio = new AdapterDesafio(getActivity().getApplicationContext(), camposDesafios, valoresDesafios, idDesafios);
                        ListViewDesafiosRutina.setAdapter(adapterDesafio);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
