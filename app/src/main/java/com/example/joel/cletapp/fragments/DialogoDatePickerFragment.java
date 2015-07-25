package com.example.joel.cletapp.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joel.cletapp.MainActivity;
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
    private int posicion;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
// Use the current date as the default date in the picker

        ListViewDatosPerfil = (ListView) getActivity().findViewById(R.id.ListViewDatosPerfil);
        campos = getArguments().getStringArray("campos");
        valores = getArguments().getStringArray("valores");
        posicion = getArguments().getInt("posicion");

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

// Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = sdf.format(c.getTime());

        valores[posicion] = formattedDate;
        AdapterPerfil adapterPerfil = new AdapterPerfil(getActivity().getApplicationContext(), campos, valores);
        ListViewDatosPerfil.setAdapter(adapterPerfil);
    }
}
