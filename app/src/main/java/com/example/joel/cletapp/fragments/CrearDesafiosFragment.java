package com.example.joel.cletapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joel.cletapp.CRUDDatabase.ObjetivoCRUD;
import com.example.joel.cletapp.ClasesDataBase.Objetivo;
import com.example.joel.cletapp.Mensaje;
import com.example.joel.cletapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 27/07/2015.
 */
public class CrearDesafiosFragment extends Fragment {

    ObjetivoCRUD objetivoCRUD;

    private String[] campos = {"Fecha de inicio", "Fecha final", "Categoria", "Valor"};
    private String[] valores = {"01/01/1990", "02/01/1990", "", "0 m"};
    private ArrayList<String> categorias;


    //testeando otra cosa
    GridView GridViewDatosDesafio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_desafio_crear, container, false);
        Mensaje qwe = new Mensaje(getActivity().getApplicationContext(), "FragmentCrearDesafios creado");

        GridViewDatosDesafio = (GridView) root.findViewById(R.id.GridViewDatosDesafio);
        objetivoCRUD = new ObjetivoCRUD(getActivity().getApplicationContext());
        AdapterCrearDesafio adapterCrearDesafio = new AdapterCrearDesafio(getActivity().getApplicationContext(), campos, valores);
        GridViewDatosDesafio.setAdapter(adapterCrearDesafio);
        categorias = new ArrayList<>();

        inicializarBaseDeDatos();

        GridViewDatosDesafio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putStringArray("campos", campos);
                bundle.putStringArray("valores", valores);
                bundle.putStringArrayList("categorias", categorias);
                bundle.putInt("posicion", position);

                switch (position) {
                    case 0:
                        DialogFragment pickerInicio = new DialogoDatePickerDesafioFragment();
                        pickerInicio.setArguments(bundle);
                        pickerInicio.show(getFragmentManager(), "datePicker");
                        break;

                    case 1:
                        DialogFragment pickerFinal = new DialogoDatePickerDesafioFragment();
                        pickerFinal.setArguments(bundle);
                        pickerFinal.show(getFragmentManager(), "datePicker2");
                        break;

                    case 2:
                        DialogoCategoriaSelector dialogo = new DialogoCategoriaSelector();
                        dialogo.setArguments(bundle);
                        dialogo.show(getFragmentManager(), "categoriaPicker");
                        break;

                    case 3:
                        DialogoValorObjetivo dialogoValorObjetivo;
                        dialogoValorObjetivo = new DialogoValorObjetivo();
                        dialogoValorObjetivo.setArguments(bundle);
                        dialogoValorObjetivo.show(getFragmentManager(), "valorDialog");
                        break;
                }
            }
        });

        return root;
    }

    private void inicializarBaseDeDatos() {
        List<Objetivo> listObjetivo;
        listObjetivo = objetivoCRUD.buscarTodosLosObjetivos();

        if (listObjetivo.isEmpty()) {
            Objetivo objetivo = new Objetivo(0, "Distancia", "Distancia recorrida");
            long testeo = objetivoCRUD.insertarObjetivo(objetivo);
            Log.v("vacio", "Objetivo insertado: " + testeo);
        }

        listObjetivo = objetivoCRUD.buscarTodosLosObjetivos();

        for (int i = 0; i < listObjetivo.size(); i++) {
            categorias.add(listObjetivo.get(i).getObjetivoNombre());
        }
    }
}

class AdapterCrearDesafioRow {

    TextView nombreCampo;
    TextView valorCampo;

    public AdapterCrearDesafioRow(View view) {
        this.nombreCampo = (TextView) view.findViewById(R.id.TextViewNombreCampo);
        this.valorCampo = (TextView) view.findViewById(R.id.TextViewValorCampo);
    }
}

class AdapterCrearDesafio extends ArrayAdapter<String> {

    Context context;
    String[] campos;
    String[] valores;

    public AdapterCrearDesafio(Context c, String[] listaCampos, String[] listaValores) {
        super(c, R.layout.single_crear_desafio_row, R.id.TextViewNombreCampo, listaCampos);
        this.context = c;
        this.campos = listaCampos;
        this.valores = listaValores;
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    public String[] getCampos() {
        return campos;
    }

    public String[] getValores() {
        return valores;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        AdapterCrearDesafioRow holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_crear_desafio_row, parent, false);
            holder = new AdapterCrearDesafioRow(row);
            row.setTag(holder);
        } else {
            holder = (AdapterCrearDesafioRow) row.getTag();
        }

        holder.nombreCampo.setText(campos[position]);
        holder.valorCampo.setText(valores[position]);

        return row;
    }
}
