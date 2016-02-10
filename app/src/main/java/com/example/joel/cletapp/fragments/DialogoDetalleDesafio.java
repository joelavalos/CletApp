package com.example.joel.cletapp.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.joel.cletapp.ClasesDataBase.Objetivo;
import com.example.joel.cletapp.ClasesDataBase.Repeticiones;
import com.example.joel.cletapp.ClasesDataBase.Serie;
import com.example.joel.cletapp.Communicator;
import com.example.joel.cletapp.Mensaje;
import com.example.joel.cletapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Joel on 18/12/2015.
 */
public class DialogoDetalleDesafio extends DialogFragment {
    private View view;
    private Communicator comm;

    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private String[] campos = {"Categoria", "Distancia repeticion", "Series", "Repeticiones"};
    private String[] valores = {"", "0 Km", "1", "1"};
    private AdapterCrearDesafio adapterCrearDesafio;
    private ArrayList<String> categorias;

    private ObjetivoCRUD objetivoCRUD;
    private DesafioCRUD desafioCRUD;
    private DesafioObjetivoCRUD desafioObjetivoCRUD;

    private GridView GridViewDatosDesafio;
    private EditText EditTextNombreDesafio;
    private EditText EditTextNotaDesafio;

    private Date parsedInicio = null;
    private Date parsedFinal = null;

    private SerieCRUD serieCRUD;
    private RepeticionesCRUD repeticionesCRUD;

    private String[] valoresDesafios = {"Descansar", "Descansar", "Descansar", "Descansar", "Descansar", "Descansar", "Descansar"};
    private String[] nombres = {"Descansar", "Descansar", "Descansar", "Descansar", "Descansar", "Descansar", "Descansar"};
    private String[] objetivos = {"", "", "", "", "", "", ""};
    private String[] series = {"1", "1", "1", "1", "1", "1", "1"};
    private String[] repeticiones = {"1", "1", "1", "1", "1", "1", "1"};
    private int posicion = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        comm = (Communicator) getActivity();

        posicion = getArguments().getInt("posicion", 0);

        valoresDesafios = getArguments().getStringArray("valoresDesafios");
        nombres = getArguments().getStringArray("nombres");
        objetivos = getArguments().getStringArray("objetivos");
        series = getArguments().getStringArray("series");
        repeticiones = getArguments().getStringArray("repeticiones");

        view = inflater.inflate(R.layout.fragment_detalle_desafio, null);
        builder.setView(view);

        inicializarComponentes(view);
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

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!validarCreacion().equals("")) {
                        new Mensaje(getActivity().getApplicationContext(), validarCreacion());
                    } else {
                        nombres[posicion] = EditTextNombreDesafio.getText().toString();
                        valoresDesafios[posicion] = "0" + "-" + EditTextNombreDesafio.getText().toString();
                        objetivos[posicion] = valores[1];
                        series[posicion] = valores[2];
                        repeticiones[posicion] = valores[3];

                        comm.pruebaDialogToDialog(valoresDesafios, nombres, objetivos, series, repeticiones);

                        dismiss();
                    }
                }
            });
        }
    }

    public GridView retornarGridView() {
        return GridViewDatosDesafio;
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
        valores[1] = "0 Km";
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
            if (Integer.valueOf(valores[2]) > 3) {
                validar = "Maximo 3 series";
            }

            if (Integer.valueOf(valores[2]) < 1) {
                validar = "Minimo 1 serie";
            }
        }

        if (valores[3].equals(".")) {
            validar = "Valor incorrecto";
        } else {
            if (Integer.valueOf(valores[3]) > 4) {
                validar = "Maximo 4 repeticiones";
            }

            if (Integer.valueOf(valores[3]) < 2) {
                validar = "Minimo 2 repeticion";
            }
        }

        if (valores[1].equals(". m")) {
            validar = "Valor incorrecto";
        } else {
            if (valores[1].equals("0 Km")) {
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
