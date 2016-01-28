package com.example.joel.cletapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.joel.cletapp.CRUDDatabase.RepeticionesCRUD;
import com.example.joel.cletapp.CRUDDatabase.SerieCRUD;
import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.ClasesDataBase.DesafioObjetivo;
import com.example.joel.cletapp.ClasesDataBase.Objetivo;
import com.example.joel.cletapp.ClasesDataBase.Repeticiones;
import com.example.joel.cletapp.ClasesDataBase.Serie;
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
    private String[] campos = {"Categoria", "Valor", "Series", "Repeticiones"};
    private String[] valores = {"", "0 m", "1", "1"};
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

    private SerieCRUD serieCRUD;
    private RepeticionesCRUD repeticionesCRUD;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_desafio_crear, container, false);

        inicializarComponentes(root);
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
                        DialogoCategoriaSelector dialogo = new DialogoCategoriaSelector();
                        dialogo.setArguments(bundle);
                        dialogo.show(getFragmentManager(), "categoriaPicker");
                        break;

                    case 1:
                        DialogoValorObjetivo dialogoValorObjetivo = new DialogoValorObjetivo();
                        dialogoValorObjetivo.setArguments(bundle);
                        dialogoValorObjetivo.show(getFragmentManager(), "valorDialog");
                        break;

                    case 2:
                        DialogoValorSerie dialogoValorSerie = new DialogoValorSerie();
                        dialogoValorSerie.setArguments(bundle);
                        dialogoValorSerie.show(getFragmentManager(), "serieDialog");
                        break;

                    case 3:
                        DialogoValorRepeticiones dialogoValorRepeticion = new DialogoValorRepeticiones();
                        dialogoValorRepeticion.setArguments(bundle);
                        dialogoValorRepeticion.show(getFragmentManager(), "repeticionDialog");
                        break;
                }
            }
        });

        ButtonCrearDesafio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validarCreacion().equals("")) {
                    new Mensaje(getActivity().getApplicationContext(), validarCreacion());
                } else {

                    //Se busca el objetivo
                    Objetivo objetivo = null;
                    try {
                        objetivo = objetivoCRUD.buscarObjetivoPorNombre(valores[0]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    try {
                        parsedInicio = format.parse(format.format(c.getTime()));
                        parsedFinal = format.parse(format.format(c.getTime()));
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
                            0,
                            1,
                            1,
                            1870);
                    desafio = desafioCRUD.insertarDesafio(desafio);

                    DesafioObjetivo desafioObjetivo = new DesafioObjetivo(0, desafio, objetivo, Float.parseFloat(valores[1].split(" ")[0]));
                    desafioObjetivoCRUD.insertarDesafioObjetivo(desafioObjetivo);

                    crearSeriesRepeticiones(desafio, Integer.parseInt(valores[2]), Integer.parseInt(valores[3]));

                    Mensaje mensajeCreado = new Mensaje(getActivity().getApplicationContext(), "Desafio creado");

                    reiniciarDatos();
                }
            }
        });

        return root;
    }

    private void crearSeriesRepeticiones(Desafio desafio, int series, int repeticiones) {
        for (int h = 0; h < series; h++) {
            Serie serie = new Serie(0, desafio);
            serie = serieCRUD.insertarSerie(serie);

            for (int j = 0; j < repeticiones; j++) {
                Repeticiones addRepeticiones = new Repeticiones(0, serie, 0);
                repeticionesCRUD.insertarRepeticion(addRepeticiones);
            }
        }
    }

    private void inicializarComponentes(View root) {
        GridViewDatosDesafio = (GridView) root.findViewById(R.id.GridViewDatosDesafio);
        ButtonCrearDesafio = (Button) root.findViewById(R.id.ButtonCrearDesafio);
        EditTextNombreDesafio = (EditText) root.findViewById(R.id.EditTextNombreDesafio);
        EditTextNotaDesafio = (EditText) root.findViewById(R.id.EditTextNotaDesafio);
        adapterCrearDesafio = new AdapterCrearDesafio(getActivity().getApplicationContext(), campos, valores);
        GridViewDatosDesafio.setAdapter(adapterCrearDesafio);
    }

    private void inicializarBaseDeDatos() {
        objetivoCRUD = new ObjetivoCRUD(getActivity().getApplicationContext());
        desafioCRUD = new DesafioCRUD(getActivity().getApplicationContext());
        desafioObjetivoCRUD = new DesafioObjetivoCRUD(getActivity().getApplicationContext());
        serieCRUD = new SerieCRUD(getActivity().getApplicationContext());
        repeticionesCRUD = new RepeticionesCRUD(getActivity().getApplicationContext());

        categorias = new ArrayList<>();
        List<Objetivo> listObjetivo;
        listObjetivo = objetivoCRUD.buscarTodosLosObjetivos();

        for (int i = 0; i < listObjetivo.size(); i++) {
            categorias.add(listObjetivo.get(i).getObjetivoNombre());
        }
    }

    private void reiniciarDatos() {
        EditTextNombreDesafio.setText("");
        EditTextNotaDesafio.setText("");
        //valores[0] = format.format(c.getTime());
        //valores[1] = format.format(c.getTime());
        valores[0] = "";
        valores[1] = "0 m";
        valores[2] = "1";
        valores[3] = "1";
        adapterCrearDesafio = new AdapterCrearDesafio(getActivity().getApplicationContext(), campos, valores);
        GridViewDatosDesafio.setAdapter(adapterCrearDesafio);
    }

    private String validarCreacion() {
        String validar = "";

        if (valores[2].equals(".")) {
            validar = "Valor incorrecto";
        } else {
            if (Integer.valueOf(valores[2]) > 5) {
                validar = "Maximo 5 series";
            }

            if (Integer.valueOf(valores[2]) < 1) {
                validar = "Minimo 1 serie";
            }
        }

        if (valores[3].equals(".")) {
            validar = "Valor incorrecto";
        } else {
            if (Integer.valueOf(valores[3]) > 5) {
                validar = "Maximo 5 repeticiones";
            }

            if (Integer.valueOf(valores[3]) < 1) {
                validar = "Minimo 1 repeticion";
            }
        }

        if (valores[1].equals(". m")) {
            validar = "Valor incorrecto";
        } else {
            if (valores[1].equals("0 m")) {
                validar = "Ingrese distancia";
            }
        }

        if (valores[0].equals("")) {
            validar = "Seleccione categoria";
        }
        if (EditTextNombreDesafio.getText().toString().equals("")) {
            validar = "Ingrese nombre";
        }

        return validar;
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
        holder.nombreCampo.setSingleLine();
        holder.valorCampo.setText(valores[position]);

        return row;
    }
}
