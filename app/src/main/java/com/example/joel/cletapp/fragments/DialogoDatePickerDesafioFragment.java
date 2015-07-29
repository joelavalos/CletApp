package com.example.joel.cletapp.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ListView;

import com.example.joel.cletapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Joel on 29/07/2015.
 */
public class DialogoDatePickerDesafioFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private GridView GridViewDatosDesafio;
    private String[] campos;
    private String[] valores;
    private int posicion;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        GridViewDatosDesafio = (GridView) getActivity().findViewById(R.id.GridViewDatosDesafio);
        campos = getArguments().getStringArray("campos");
        valores = getArguments().getStringArray("valores");
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

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = sdf.format(c.getTime());

        valores[posicion] = formattedDate;
        AdapterCrearDesafio adapterCrearDesafio = new AdapterCrearDesafio(getActivity().getApplicationContext(), campos, valores);
        GridViewDatosDesafio.setAdapter(adapterCrearDesafio);

    }
}
