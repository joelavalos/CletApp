package com.example.joel.cletapp.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.ListView;

import com.example.joel.cletapp.CRUDDatabase.DesafioCRUD;
import com.example.joel.cletapp.CRUDDatabase.DesafioObjetivoCRUD;
import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.ClasesDataBase.DesafioObjetivo;
import com.example.joel.cletapp.Mensaje;
import com.example.joel.cletapp.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Joel on 05/08/2015.
 */
public class DialogoDesafioSelector extends DialogFragment {
    private ListView ListViewDesafiosRutina;
    private String[] camposDesafios;
    private String[] valoresDesafios;
    private String[] nombres;
    private String[] objetivos;
    private ArrayList<String> desafios = new ArrayList<>();
    private ArrayList<String> desafiosPresentar = new ArrayList<>();
    private AdapterDesafio adapterDesafio;
    private DecimalFormat df = new DecimalFormat("#.#");

    private int posicion;
    private int desafioSeleccion;
    private int ultimaSeleccion;
    private String ultimaSeleccionDesafio;

    private DesafioObjetivoCRUD desafioObjetivoCRUD;
    private DesafioCRUD desafioCRUD;
    private Desafio desafio;
    private DesafioObjetivo desafioObjetivo;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ListViewDesafiosRutina = (ListView) getActivity().findViewById(R.id.ListViewDesafiosRutina);
        camposDesafios = getArguments().getStringArray("camposDesafios");
        valoresDesafios = getArguments().getStringArray("valoresDesafios");
        nombres = getArguments().getStringArray("nombres");
        objetivos = getArguments().getStringArray("objetivos");
        posicion = getArguments().getInt("posicion");
        desafios = getArguments().getStringArrayList("desafios");
        desafioObjetivoCRUD = new DesafioObjetivoCRUD(getActivity().getApplicationContext());
        desafioCRUD = new DesafioCRUD(getActivity().getApplicationContext());

        for (int i = 0; i < desafios.size(); i++) {

            if (desafios.get(i).equals("Descansar")) {
                desafiosPresentar.add(desafios.get(i));
            } else {

                try {
                    desafio = desafioCRUD.buscarDesafioPorId(Long.parseLong(desafios.get(i).split("-")[0]));
                    desafioObjetivo = desafioObjetivoCRUD.buscarDesafioObjetivoPorIdDesafio(desafio);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (desafioObjetivo.getValor() > 1000){
                    desafiosPresentar.add(String.format("%-4s", df.format(desafioObjetivo.getValor()/1000)).replace(" ", " ") + " " + String.format("%-6s", "Km").replace(" ", " ") + desafios.get(i).split("-")[1]);
                } else{
                    desafiosPresentar.add(String.format("%-4s", df.format(desafioObjetivo.getValor())).replace(" ", " ") + " " + String.format("%-6s", "m").replace(" ", " ") + desafios.get(i).split("-")[1]);
                }
            }
        }

        desafioSeleccion = 0;
        if (valoresDesafios[posicion].equals("Descansar")) {
            desafioSeleccion = 0;
            ultimaSeleccion = 0;
        }
        ultimaSeleccionDesafio = valoresDesafios[posicion];
        //desafios.remove(valoresDesafios[posicion]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle("Seleccione desafio")
                .setItems(desafiosPresentar.toArray(new String[desafiosPresentar.size()]), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        ultimaSeleccion = whichButton;
                        valoresDesafios[posicion] = desafios.toArray(new String[desafios.size()])[ultimaSeleccion];
                        nombres[posicion] = desafios.toArray(new String[desafios.size()])[ultimaSeleccion];

                        if (valoresDesafios[posicion].equals("Descansar")) {
                            if (!desafios.contains(ultimaSeleccionDesafio) && !ultimaSeleccionDesafio.equals("")) {
                                desafios.add(ultimaSeleccionDesafio);
                                nombres[posicion] = "Descansar";
                                objetivos[posicion] = "";
                            }
                        } else {
                            nombres[posicion] = desafios.toArray(new String[desafios.size()])[ultimaSeleccion].split("-")[1];
                            try {
                                desafio = desafioCRUD.buscarDesafioPorId(Long.parseLong(desafios.toArray(new String[desafios.size()])[ultimaSeleccion].split("-")[0]));
                                desafioObjetivo = desafioObjetivoCRUD.buscarDesafioObjetivoPorIdDesafio(desafio);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            objetivos[posicion] = String.valueOf(Math.round(desafioObjetivo.getValor()) + " m");
                            desafios.remove(valoresDesafios[posicion]);
                            if (ultimaSeleccionDesafio.equals(valoresDesafios[posicion])) {

                            } else {
                                if (!ultimaSeleccionDesafio.equals("") && !ultimaSeleccionDesafio.equals("Descansar")) {
                                    desafios.add(ultimaSeleccionDesafio);
                                }
                            }
                        }

                        adapterDesafio = new AdapterDesafio(getActivity().getApplicationContext(), camposDesafios, valoresDesafios, nombres, objetivos);
                        ListViewDesafiosRutina.setAdapter(adapterDesafio);
                    }});
                /*.setItems()
                .setSingleChoiceItems(desafiosPresentar.toArray(new String[desafiosPresentar.size()]), desafioSeleccion, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ultimaSeleccion = whichButton;

                    }
                });*/
                /*.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        valoresDesafios[posicion] = desafios.toArray(new String[desafios.size()])[ultimaSeleccion];
                        nombres[posicion] = desafios.toArray(new String[desafios.size()])[ultimaSeleccion];

                        if (valoresDesafios[posicion].equals("Descansar")) {
                            if (!desafios.contains(ultimaSeleccionDesafio) && !ultimaSeleccionDesafio.equals("")) {
                                desafios.add(ultimaSeleccionDesafio);
                                nombres[posicion] = "Descansar";
                                objetivos[posicion] = "";
                            }
                        } else {
                            nombres[posicion] = desafios.toArray(new String[desafios.size()])[ultimaSeleccion].split("-")[1];
                            try {
                                desafio = desafioCRUD.buscarDesafioPorId(Long.parseLong(desafios.toArray(new String[desafios.size()])[ultimaSeleccion].split("-")[0]));
                                desafioObjetivo = desafioObjetivoCRUD.buscarDesafioObjetivoPorIdDesafio(desafio);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            objetivos[posicion] = String.valueOf(Math.round(desafioObjetivo.getValor()) + " m");
                            desafios.remove(valoresDesafios[posicion]);
                            if (ultimaSeleccionDesafio.equals(valoresDesafios[posicion])) {

                            } else {
                                if (!ultimaSeleccionDesafio.equals("") && !ultimaSeleccionDesafio.equals("Descansar")) {
                                    desafios.add(ultimaSeleccionDesafio);
                                }
                            }
                        }

                        adapterDesafio = new AdapterDesafio(getActivity().getApplicationContext(), camposDesafios, valoresDesafios, nombres, objetivos);
                        ListViewDesafiosRutina.setAdapter(adapterDesafio);
                    }
                });*/
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
