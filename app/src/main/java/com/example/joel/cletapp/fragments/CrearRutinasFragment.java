package com.example.joel.cletapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.example.joel.cletapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Joel on 04/08/2015.
 */
public class CrearRutinasFragment extends Fragment {
    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    private String[] campos = {"Fecha de inicio", "Fecha final"};
    private String[] valores = {format.format(c.getTime()), format.format(c.getTime())};
    private AdapterCrearRutina adapterCrearRutina;

    private GridView GridViewDatosRutina;
    private Button ButtonCrearRutina;
    private EditText EditTextNombreRutina;
    private EditText EditTextNotaRutina;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rutina_crear, container, false);

        inicializarComponentes(root);
        //inicializarBaseDeDatos();

        return root;
    }

    private void inicializarComponentes(View root) {
        GridViewDatosRutina = (GridView) root.findViewById(R.id.GridViewDatosRutina);
        ButtonCrearRutina = (Button) root.findViewById(R.id.ButtonCrearRutina);
        EditTextNombreRutina = (EditText) root.findViewById(R.id.EditTextNombreRutina);
        EditTextNotaRutina = (EditText) root.findViewById(R.id.EditTextNotaRutina);
        adapterCrearRutina = new AdapterCrearRutina(getActivity().getApplicationContext(), campos, valores);
        GridViewDatosRutina.setAdapter(adapterCrearRutina);
    }
}

class AdapterCrearRutinaRow {

    TextView nombreCampo;
    TextView valorCampo;

    public AdapterCrearRutinaRow(View view) {
        this.nombreCampo = (TextView) view.findViewById(R.id.TextViewNombreCampo);
        this.valorCampo = (TextView) view.findViewById(R.id.TextViewValorCampo);
    }
}

class AdapterCrearRutina extends ArrayAdapter<String> {

    Context context;
    String[] campos;
    String[] valores;

    public AdapterCrearRutina(Context c, String[] listaCampos, String[] listaValores) {
        super(c, R.layout.single_crear_desafio_row, R.id.TextViewNombreCampo, listaCampos);
        this.context = c;
        this.campos = listaCampos;
        this.valores = listaValores;
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AdapterCrearRutinaRow holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_crear_desafio_row, parent, false);
            holder = new AdapterCrearRutinaRow(row);
            row.setTag(holder);
        } else {
            holder = (AdapterCrearRutinaRow) row.getTag();
        }

        holder.nombreCampo.setText(campos[position]);
        holder.valorCampo.setText(valores[position]);

        return row;
    }
}
