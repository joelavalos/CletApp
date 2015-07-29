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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.example.joel.cletapp.CRUDDatabase.DesafioCRUD;
import com.example.joel.cletapp.CRUDDatabase.DesafioObjetivoCRUD;
import com.example.joel.cletapp.CRUDDatabase.ObjetivoCRUD;
import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.ClasesDataBase.DesafioObjetivo;
import com.example.joel.cletapp.ClasesDataBase.Objetivo;
import com.example.joel.cletapp.Mensaje;
import com.example.joel.cletapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Joel on 27/07/2015.
 */
public class CrearDesafiosFragment extends Fragment {
    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private String[] campos = {"Fecha de inicio", "Fecha final", "Categoria", "Valor"};
    private String[] valores = {format.format(c.getTime()), format.format(c.getTime()), "", "0 m"};
    private AdapterCrearDesafio adapterCrearDesafio;
    private ArrayList<String> categorias;

    private ObjetivoCRUD objetivoCRUD;
    private DesafioCRUD desafioCRUD;
    private DesafioObjetivoCRUD desafioObjetivoCRUD;

    private GridView GridViewDatosDesafio;
    private Button ButtonCrearDesafio;
    private EditText EditTextNombreDesafio;
    private EditText EditTextNotaDesafio;


    private Date parsedInicio = null;
    private Date parsedFinal = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_desafio_crear, container, false);
        Mensaje qwe = new Mensaje(getActivity().getApplicationContext(), "FragmentCrearDesafios creado");

        GridViewDatosDesafio = (GridView) root.findViewById(R.id.GridViewDatosDesafio);
        ButtonCrearDesafio = (Button) root.findViewById(R.id.ButtonCrearDesafio);
        EditTextNombreDesafio = (EditText) root.findViewById(R.id.EditTextNombreDesafio);
        EditTextNotaDesafio = (EditText) root.findViewById(R.id.EditTextNotaDesafio);

        objetivoCRUD = new ObjetivoCRUD(getActivity().getApplicationContext());
        desafioCRUD = new DesafioCRUD(getActivity().getApplicationContext());
        desafioObjetivoCRUD = new DesafioObjetivoCRUD(getActivity().getApplicationContext());

        adapterCrearDesafio = new AdapterCrearDesafio(getActivity().getApplicationContext(), campos, valores);
        GridViewDatosDesafio.setAdapter(adapterCrearDesafio);
        categorias = new ArrayList<>();

        inicializarBaseDeDatos();
        validarCreacion();

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

        ButtonCrearDesafio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validarCreacion().equals("")) {
                    Mensaje mensajeNoSePudoCrear = new Mensaje(getActivity().getApplicationContext(), validarCreacion());
                } else {

                    //Se busca el objetivo
                    Objetivo objetivo = null;
                    try {
                        objetivo = objetivoCRUD.buscarObjetivoPorNombre(valores[2]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    try {
                        parsedInicio = format.parse(valores[0]);
                        parsedFinal = format.parse(valores[1]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //Se crea el desafio
                    Desafio desafio = new Desafio(0,
                            EditTextNombreDesafio.getText().toString(),
                            EditTextNotaDesafio.getText().toString(),
                            new java.sql.Date(parsedInicio.getTime()),
                            new java.sql.Date(parsedFinal.getTime()),
                            'P',
                            false);
                    desafio = desafioCRUD.insertarDesafio(desafio);

                    DesafioObjetivo desafioObjetivo = new DesafioObjetivo(0, desafio, objetivo, Float.parseFloat(valores[3].split(" ")[0]));
                    desafioObjetivo = desafioObjetivoCRUD.insertarDesafioObjetivo(desafioObjetivo);

                    Log.v("asd", "Insertado desafio: " + desafio.getDesafioId() + " desafioObjetivo: " + desafioObjetivo.getDesObjId());
                    Mensaje mensajeCreado = new Mensaje(getActivity().getApplicationContext(), "Desafio creado");

                    reiniciarDatos();
                }
            }
        });

        return root;
    }

    private void reiniciarDatos() {
        EditTextNombreDesafio.setText("");
        EditTextNotaDesafio.setText("");
        valores[0] = format.format(c.getTime());
        valores[1] = format.format(c.getTime());
        valores[2] = "";
        valores[3] = "0 m";
        adapterCrearDesafio = new AdapterCrearDesafio(getActivity().getApplicationContext(), campos, valores);
        GridViewDatosDesafio.setAdapter(adapterCrearDesafio);
    }

    private String validarCreacion() {
        String validar = "";

        try {
            parsedInicio = format.parse(valores[0]);
            parsedFinal = format.parse(valores[1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date dateInicio = new java.sql.Date(parsedInicio.getTime());
        Date dateFin = new java.sql.Date(parsedFinal.getTime());

        if (dateInicio.getTime() >= dateFin.getTime()) {
            validar = "La fecha final debe ser despues de: " + valores[0];
        }

        if (valores[3].equals("0 m")) {
            validar = "Ingrese distancia";
        }
        if (valores[2].equals("")) {
            validar = "Seleccione categoria";
        }
        if (EditTextNombreDesafio.getText().toString().equals("")) {
            validar = "Ingrese nombre";
        }

        return validar;
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
