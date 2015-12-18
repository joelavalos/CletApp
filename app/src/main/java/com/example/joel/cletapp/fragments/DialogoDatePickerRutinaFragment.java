package com.example.joel.cletapp.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ListView;

import com.example.joel.cletapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Joel on 05/08/2015.
 */
public class DialogoDatePickerRutinaFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private GridView GridViewDatosRutina;
    private String[] campos;
    private String[] valores;
    AdapterCrearDesafio adapterCrearDesafio;
    private int posicion;

    private String[] camposDesafios;
    private String[] valoresDesafios;
    private String[] nombres;
    private String[] objetivos;
    private ListView ListViewDesafiosRutina;
    AdapterDesafio adapterDesafio;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        DialogoCrearRutina asd = (DialogoCrearRutina) manager.findFragmentByTag("crearRutina");

        GridViewDatosRutina = (GridView) getActivity().findViewById(R.id.GridViewDatosRutina);
        GridViewDatosRutina = asd.retornarGridview();
        ListViewDesafiosRutina = (ListView) getActivity().findViewById(R.id.ListViewDesafiosRutina);
        ListViewDesafiosRutina = asd.retornarListView();
        campos = getArguments().getStringArray("campos");
        valores = getArguments().getStringArray("valores");
        camposDesafios = getArguments().getStringArray("camposDesafios");
        valoresDesafios = getArguments().getStringArray("valoresDesafios");
        nombres = getArguments().getStringArray("nombres");
        objetivos = getArguments().getStringArray("objetivos");
        posicion = getArguments().getInt("posicion");

        String[] fechaActual = valores[posicion].split("/");
        int year = Integer.parseInt(fechaActual[2]);
        int month = Integer.parseInt(fechaActual[1]) - 1;
        int day = Integer.parseInt(fechaActual[0]);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        String formattedDate = sdf.format(c.getTime());
        valores[posicion] = formattedDate;

        actualizarFechas(c);

        formattedDate = sdf.format(c.getTime());
        valores[posicion + 1] = formattedDate;

        adapterCrearDesafio = new AdapterCrearDesafio(getActivity().getApplicationContext(), campos, valores);
        GridViewDatosRutina.setAdapter(adapterCrearDesafio);
        adapterDesafio = new AdapterDesafio(getActivity().getApplicationContext(), camposDesafios, valoresDesafios, nombres, objetivos);
        ListViewDesafiosRutina.setAdapter(adapterDesafio);
    }

    private void actualizarFechas(Calendar c) {
        Locale spanish = new Locale("es", "PE");
        for (int i = 0; i < camposDesafios.length; i++) {
            camposDesafios[i] = sdf.format(c.getTime()) + " " + c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, spanish);

            if ((i + 1) < camposDesafios.length) {
                c.add(Calendar.DATE, +1);
            }
        }
    }
}
