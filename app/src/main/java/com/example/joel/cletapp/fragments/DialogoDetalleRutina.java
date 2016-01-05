package com.example.joel.cletapp.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.joel.cletapp.Communicator;
import com.example.joel.cletapp.R;

import java.util.ArrayList;

/**
 * Created by Joel on 14/12/2015.
 */
public class DialogoDetalleRutina extends DialogFragment {
    private View view;
    private Button ButtonRuttinaRapida;
    private Button ButtonRuttinaPersonalizada;
    private TextView TextViewNombreRutina;
    private TextView TextViewNombreDesafio;
    private TextView TextViewNumeroSerieTotal;
    private TextView TextViewNumeroRepeticionTotal;
    private TextView TextViewNumeroSerieActual;
    private TextView TextViewNumeroRepeticionActual;
    private TextView TextViewCancelarRutina;
    private TextView TextViewCabezera;
    private TextView TextViewDias;
    private TextView TextViewDiasString;

    private boolean estado = false;
    private String nombreRutina = "";
    private String nombreDesafio = "";
    private String seriesDesafioTotal = "";
    private String repeticionesDesafioTotal = "";
    private String seriesDesafio = "";
    private String repeticionesDesafio = "";
    private String diffDesafio = "";


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
        diffDesafio = getArguments().getString("diffString");

        if (diffDesafio.equals("")) {
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
                        /*DialogoRutinaSelector dialogo = new DialogoRutinaSelector();
                        dialogo.show(getFragmentManager(), "rutinaPersonalizada");*/

                        DialogoCrearRutina dialogo = new DialogoCrearRutina();
                        dialogo.show(getFragmentManager(), "crearRutina");
                    }
                });

            } else {
                view = inflater.inflate(R.layout.fragment_detalle_rutina2, null);
                TextViewCancelarRutina = (TextView) view.findViewById(R.id.TextViewCancelarRutina);
                TextViewNombreRutina = (TextView) view.findViewById(R.id.TextViewNombreRutina);
                TextViewNombreDesafio = (TextView) view.findViewById(R.id.TextViewNombreDesafio);
                TextViewNumeroSerieTotal = (TextView) view.findViewById(R.id.TextViewNumeroSerieTotal);
                TextViewNumeroRepeticionTotal = (TextView) view.findViewById(R.id.TextViewNumeroRepeticionTotal);
                TextViewNumeroSerieActual = (TextView) view.findViewById(R.id.TextViewNumeroSerieActual);
                TextViewNumeroRepeticionActual = (TextView) view.findViewById(R.id.TextViewNumeroRepeticionActual);

                nombreRutina = getArguments().getString("nombreRutina");
                nombreDesafio = getArguments().getString("nombreDesafio");
                seriesDesafioTotal = getArguments().getString("seriesDesafioTotal");
                repeticionesDesafioTotal = getArguments().getString("repeticionesDesafioTotal");
                seriesDesafio = getArguments().getString("seriesDesafio");
                repeticionesDesafio = getArguments().getString("repeticionesDesafio");

                TextViewNombreRutina.setText(nombreRutina);
                TextViewNombreDesafio.setText(nombreDesafio);
                TextViewNumeroSerieTotal.setText("/" + seriesDesafioTotal);
                TextViewNumeroRepeticionTotal.setText("/" + repeticionesDesafioTotal);
                TextViewNumeroSerieActual.setText(seriesDesafio);
                TextViewNumeroRepeticionActual.setText(repeticionesDesafio);

                TextViewCancelarRutina.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("Accion", "DetenerRutina");
                        bundle.putString("Mensaje", "Detener la rutina forzara la evaluacion con el progreso actual");
                        bundle.putString("Titulo", "Detener rutina");

                        DialogoConfirmacion dialogo = new DialogoConfirmacion();
                        dialogo.setArguments(bundle);
                        dialogo.show(getFragmentManager(), "categoriaPicker");
                    }
                });
            }
        } else {
            view = inflater.inflate(R.layout.fragment_detalle_rutina3, null);

            nombreRutina = getArguments().getString("nombreRutina");

            TextViewCancelarRutina = (TextView) view.findViewById(R.id.TextViewCancelarRutina);
            TextViewCabezera = (TextView) view.findViewById(R.id.TextViewCabezera);
            TextViewDias = (TextView) view.findViewById(R.id.TextViewDias);
            TextViewDiasString = (TextView) view.findViewById(R.id.TextViewDiasString);
            TextViewNombreDesafio = (TextView) view.findViewById(R.id.TextViewNombreDesafio);
            TextViewNombreRutina = (TextView) view.findViewById(R.id.TextViewNombreRutina);

            TextViewNombreDesafio.setText("");
            TextViewNombreRutina.setText(nombreRutina);

            if (diffDesafio.equals("No quedan desafios")) {
                TextViewCabezera.setText("No quedan desafios");
                TextViewDias.setText("Descansa!");
                TextViewDiasString.setText("");
            } else {
                TextViewDias.setText(diffDesafio);
                if (diffDesafio.equals("1")) {
                    TextViewDiasString.setText(getResources().getString(R.string.diaString));
                }
            }

            TextViewCancelarRutina.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("Accion", "DetenerRutina");
                    bundle.putString("Mensaje", "Detener la rutina forzara la evaluacion con el progreso actual");
                    bundle.putString("Titulo", "Detener rutina");

                    DialogoConfirmacion dialogo = new DialogoConfirmacion();
                    dialogo.setArguments(bundle);
                    dialogo.show(getFragmentManager(), "categoriaPicker");
                }
            });
        }

        builder.setView(view);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }

    public void cambiarTextoPrueba(String data){
        ButtonRuttinaPersonalizada.setText(data);
    }
}
