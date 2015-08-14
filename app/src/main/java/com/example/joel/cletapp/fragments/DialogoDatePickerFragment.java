package com.example.joel.cletapp.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.ListView;

import com.example.joel.cletapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Joel on 24/07/2015.
 */
public class DialogoDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private ListView ListViewDatosPerfil;
    private String[] campos;
    private String[] valores;
    private int posicion, year, month, day;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker

        ListViewDatosPerfil = (ListView) getActivity().findViewById(R.id.ListViewDatosPerfil);
        campos = getArguments().getStringArray("campos");
        valores = getArguments().getStringArray("valores");
        posicion = getArguments().getInt("posicion");

        if (valores[posicion].equals("")){
            year = Integer.parseInt("2000");
            month = Integer.parseInt("9");
            day = Integer.parseInt("17");
        }else{
            String[] fechaActual = valores[posicion].split("/");
            year = Integer.parseInt(fechaActual[2]);
            month = Integer.parseInt(fechaActual[1]) - 1;
            day = Integer.parseInt(fechaActual[0]);
        }

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = sdf.format(c.getTime());

        valores[posicion] = formattedDate;
        AdapterPerfil adapterPerfil = new AdapterPerfil(getActivity().getApplicationContext(), campos, valores);
        ListViewDatosPerfil.setAdapter(adapterPerfil);
    }
}
