package com.example.joel.cletapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joel.cletapp.ActivityGlosario;
import com.example.joel.cletapp.ActivityProgresoRutina;
import com.example.joel.cletapp.Mensaje;
import com.example.joel.cletapp.R;

/**
 * Created by Joel on 20/07/2015.
 */
public class GlosarioFragment extends Fragment {
    private ListView LisViewGlosario;
    private String[] entradas = {"Rutina", "Desafio"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_glosario, container, false);

        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Glosario");
        ((ActionBarActivity) getActivity()).getSupportActionBar().setIcon(null);

        LisViewGlosario = (ListView) root.findViewById(R.id.LisViewGlosario);

        AdapterGlosario adapter = new AdapterGlosario(getActivity().getApplicationContext(), entradas);
        LisViewGlosario.setAdapter(adapter);

        LisViewGlosario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = ((TextView) view.findViewById(R.id.TextViewEntradaGlosario)).getText().toString();
                Intent nuevoItent = new Intent(getActivity().getApplicationContext(), ActivityGlosario.class);
                nuevoItent.putExtra("EntradaGlosario", selected);
                startActivity(nuevoItent);
            }
        });

        return root;
    }
}

class AdapterGlosarioRow {

    TextView entradaGlosario;

    public AdapterGlosarioRow(View view) {
        this.entradaGlosario = (TextView) view.findViewById(R.id.TextViewEntradaGlosario);
    }
}

class AdapterGlosario extends ArrayAdapter<String> {

    Context context;
    String[] entradas;

    public AdapterGlosario(Context c, String[] entradas) {
        super(c, R.layout.single_glosario_row, R.id.TextViewEntradaGlosario, entradas);
        this.context = c;
        this.entradas = entradas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        AdapterGlosarioRow holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_glosario_row, parent, false);
            holder = new AdapterGlosarioRow(row);
            row.setTag(holder);
        } else {
            holder = (AdapterGlosarioRow) row.getTag();
        }

        holder.entradaGlosario.setText(entradas[position]);

        return row;
    }
}


