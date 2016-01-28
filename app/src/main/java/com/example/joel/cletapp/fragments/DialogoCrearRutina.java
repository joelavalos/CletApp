package com.example.joel.cletapp.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import com.example.joel.cletapp.CRUDDatabase.DesafioCRUD;
import com.example.joel.cletapp.CRUDDatabase.DesafioObjetivoCRUD;
import com.example.joel.cletapp.CRUDDatabase.DesafioRutinaCRUD;
import com.example.joel.cletapp.CRUDDatabase.ObjetivoCRUD;
import com.example.joel.cletapp.CRUDDatabase.RepeticionesCRUD;
import com.example.joel.cletapp.CRUDDatabase.ResumenCRUD;
import com.example.joel.cletapp.CRUDDatabase.RutinaCRUD;
import com.example.joel.cletapp.CRUDDatabase.SerieCRUD;
import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.ClasesDataBase.DesafioObjetivo;
import com.example.joel.cletapp.ClasesDataBase.DesafioRutina;
import com.example.joel.cletapp.ClasesDataBase.Objetivo;
import com.example.joel.cletapp.ClasesDataBase.Repeticiones;
import com.example.joel.cletapp.ClasesDataBase.Resumen;
import com.example.joel.cletapp.ClasesDataBase.Rutina;
import com.example.joel.cletapp.ClasesDataBase.Serie;
import com.example.joel.cletapp.Communicator;
import com.example.joel.cletapp.Mensaje;
import com.example.joel.cletapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Joel on 17/12/2015.
 */
public class DialogoCrearRutina extends DialogFragment {
    private View view;
    private Communicator comm;

    private Calendar c;
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    private String[] campos = {"Fecha de inicio", "Fecha final"};
    private String[] valores = {"", ""};
    private Date parsedInicio;
    private Date parsedFinal;
    private Date parsedDesafio;
    private List<String> fechasDesafios;
    private AdapterCrearRutina adapterCrearRutina;

    private ListView ListViewDesafiosRutina;
    private String[] camposDesafios = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
    //private String[] valoresDesafios = {"", "", "", "", "", "", ""};
    private String[] valoresDesafios = {"Descansar", "Descansar", "Descansar", "Descansar", "Descansar", "Descansar", "Descansar"};
    //private String[] nombres = {"", "", "", "", "", "", ""};
    private String[] nombres = {"Descansar", "Descansar", "Descansar", "Descansar", "Descansar", "Descansar", "Descansar"};
    private String[] objetivos = {"", "", "", "", "", "", ""};
    private String[] series = {"1", "1", "1", "1", "1", "1", "1"};
    private String[] repeticiones = {"1", "1", "1", "1", "1", "1", "1"};
    private AdapterDesafio adapterDesafio;

    private GridView GridViewDatosRutina;
    private EditText EditTextNombreRutina;
    private EditText EditTextNotaRutina;

    private ResumenCRUD resumenCRUD;
    private Resumen newResumen;
    private RutinaCRUD rutinaCRUD;
    private Rutina newRutina;
    private DesafioCRUD desafioCRUD;
    private Desafio newDesafio;
    private DesafioRutinaCRUD desafioRutinaCRUD;
    private DesafioRutina newDesafioRutina;
    private List<Desafio> listaDesafios;
    private ArrayList<String> listaDesafios2;

    private ObjetivoCRUD objetivoCRUD;
    private DesafioObjetivoCRUD desafioObjetivoCRUD;
    private SerieCRUD serieCRUD;
    private RepeticionesCRUD repeticionesCRUD;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        comm = (Communicator) getActivity();
        view = inflater.inflate(R.layout.fragment_detalle_crear_rutina, null);
        builder.setView(view);

        inicializarBaseDeDatos();
        inicializarComponentes(view);

        /*
        GridViewDatosRutina.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putStringArray("campos", campos);
                bundle.putStringArray("valores", valores);
                bundle.putStringArray("camposDesafios", camposDesafios);
                bundle.putStringArray("valoresDesafios", valoresDesafios);
                bundle.putStringArray("nombres", nombres);
                bundle.putStringArray("objetivos", objetivos);
                bundle.putInt("posicion", position);

                switch (position) {
                    case 0:
                        DialogFragment pickerInicio = new DialogoDatePickerRutinaFragment();
                        pickerInicio.setArguments(bundle);
                        pickerInicio.show(getFragmentManager(), "datePicker");
                        break;
                }
            }
        });
        */

        ListViewDesafiosRutina.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Bundle bundle = new Bundle();

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bundle.putStringArray("valoresDesafios", valoresDesafios);
                bundle.putStringArray("nombres", nombres);
                bundle.putStringArray("objetivos", objetivos);
                bundle.putStringArray("series", series);
                bundle.putStringArray("repeticiones", repeticiones);

                bundle.putInt("posicion", position);
                DialogoDetalleDesafio dialogoDetalleDesafio = new DialogoDetalleDesafio();
                dialogoDetalleDesafio.setArguments(bundle);
                dialogoDetalleDesafio.show(getFragmentManager(), "detalleDesafio");

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
                        Mensaje asd = new Mensaje(getActivity().getApplicationContext(), validarCreacion());
                    } else {

                        listaDesafios.clear();
                        for (int i = 0; i < nombres.length; i++) {

                            if (nombres[i].equals("Descansar")) {

                            } else {

                                Objetivo objetivo = null;

                                try {
                                    objetivo = objetivoCRUD.buscarObjetivoPorNombre("Distancia");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    parsedInicio = format.parse(format.format(c.getTime()));
                                    parsedFinal = format.parse(format.format(c.getTime()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                Desafio desafio = new Desafio(0,
                                        nombres[i],
                                        "Sin nota",
                                        new java.sql.Date(parsedInicio.getTime()),
                                        new java.sql.Date(parsedFinal.getTime()),
                                        'P',
                                        0,
                                        1,
                                        1,
                                        1870);

                                desafio = desafioCRUD.insertarDesafio(desafio);

                                DesafioObjetivo desafioObjetivo = new DesafioObjetivo(0, desafio, objetivo, Float.parseFloat(objetivos[i].split(" ")[0])*1000);
                                desafioObjetivoCRUD.insertarDesafioObjetivo(desafioObjetivo);

                                crearSeriesRepeticiones(desafio, Integer.parseInt(series[i]), Integer.parseInt(repeticiones[i]));

                                listaDesafios.add(desafio);
                                fechasDesafios.add(camposDesafios[i].split(" ")[0]);
                            }
                        }

                        //Parte de la creacion de la rutina
                        try {
                            parsedInicio = format.parse(valores[0]);
                            parsedFinal = format.parse(valores[1]);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        newResumen = new Resumen(0, "Rutina en curso", new java.sql.Date(parsedInicio.getTime()));
                        newResumen = resumenCRUD.insertarResumen(newResumen);

                        newRutina = new Rutina(0,
                                EditTextNombreRutina.getText().toString(),
                                EditTextNotaRutina.getText().toString(),
                                new java.sql.Date(parsedInicio.getTime()),
                                new java.sql.Date(parsedFinal.getTime()),
                                'P',
                                newResumen);
                        newRutina = rutinaCRUD.insertarRutina(newRutina);

                        for (int i = 0; i < listaDesafios.size(); i++) {

                            try {
                                parsedDesafio = format.parse(fechasDesafios.get(i));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            newDesafioRutina = new DesafioRutina(0, newRutina, listaDesafios.get(i), new java.sql.Date(parsedDesafio.getTime()));
                            newDesafioRutina = desafioRutinaCRUD.insertarDesafioRutina(newDesafioRutina);
                        }
                        Mensaje asd = new Mensaje(getActivity().getApplicationContext(), "Rutina creada");
                        String datos = getDatos();
                        comm.Actualizar(datos);
                        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("detalleRutina");
                        if (prev != null) {
                            DialogFragment df = (DialogFragment) prev;
                            df.dismiss();
                        }
                        dismiss();
                    }
                }
            });
        }
    }

    public String getDatos() {
        String returnData = "";
        returnData = String.valueOf(newRutina.getRutinaId());
        returnData = returnData + "-" + R.drawable.ic_directions_bike_black_48dp;
        returnData = returnData + "-" + newRutina.getRutinaNombre();
        returnData = returnData + "-" + "Desafios";
        returnData = returnData + "-" + Math.round(listaDesafios.size());
        returnData = returnData + "-" + newRutina.getRutinaDescripcion();
        returnData = returnData + "-" + "Desde: " + format.format(newRutina.getRutinaInicio()) + " hasta: " + format.format(newRutina.getRutinaTermino());
        returnData = returnData + "-" + newRutina.getRutinaEstado();

        return returnData;
    }

    private void crearSeriesRepeticiones(Desafio desafio, int series, int repeticiones) {
        Log.v("asd", "series: " + series);
        Log.v("asd", "repeticiones: " + repeticiones);
        for (int h = 0; h < series; h++) {
            Serie serie = new Serie(0, desafio);
            serie = serieCRUD.insertarSerie(serie);

            for (int j = 0; j < repeticiones; j++) {
                Repeticiones addRepeticiones = new Repeticiones(0, serie, 0);
                repeticionesCRUD.insertarRepeticion(addRepeticiones);
            }
        }
    }

    private void reiniciarDatos(View root) {
        EditTextNombreRutina.setText("");
        EditTextNotaRutina.setText("");

        c = Calendar.getInstance();
        valores[0] = format.format(c.getTime());
        valores[1] = format.format(c.getTime());

        for (int i = 0; i < valoresDesafios.length; i++) {
            valoresDesafios[i] = "";
            nombres[i] = "";
            objetivos[i] = "";
        }

        inicializarBaseDeDatos();
        inicializarComponentes(root);
    }

    private String validarCreacion() {
        String validar = "";
        int diasDescanso = 0;

        for (int i = 0; i < valoresDesafios.length; i++) {
            if (valoresDesafios[i].equals("")) {
                validar = "Agenda incompleta";
            } else if (valoresDesafios[i].equals("Descansar")) {
                diasDescanso++;
            }
        }

        if (diasDescanso == 7) {
            validar = "Seleccione al menos 1 desafio";
        }

        try {
            parsedInicio = format.parse(valores[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cInicial = Calendar.getInstance();
        cInicial.add(Calendar.DATE, -1);
        Date dateInicio = new java.sql.Date(parsedInicio.getTime());
        Date actual = cInicial.getTime();

        if (dateInicio.getTime() < actual.getTime()) {
            validar = "La fecha inicial debe ser despues de: " + format.format(actual.getTime());
        }

        if (EditTextNombreRutina.getText().toString().equals("")) {
            validar = "Ingrese nombre";
        }

        return validar;
    }

    private void inicializarBaseDeDatos() {
        resumenCRUD = new ResumenCRUD(getActivity().getApplicationContext());
        desafioObjetivoCRUD = new DesafioObjetivoCRUD(getActivity().getApplicationContext());
        serieCRUD = new SerieCRUD(getActivity().getApplicationContext());
        repeticionesCRUD = new RepeticionesCRUD(getActivity().getApplicationContext());
        objetivoCRUD = new ObjetivoCRUD(getActivity().getApplicationContext());
        rutinaCRUD = new RutinaCRUD(getActivity().getApplicationContext());
        desafioCRUD = new DesafioCRUD(getActivity().getApplicationContext());
        desafioRutinaCRUD = new DesafioRutinaCRUD(getActivity().getApplicationContext());
        listaDesafios = desafioCRUD.buscarTodosLosDesafiosPendientes();
        listaDesafios2 = new ArrayList<>();

        listaDesafios2.add("Descansar");
        for (int i = 0; i < listaDesafios.size(); i++) {
            listaDesafios2.add(listaDesafios.get(i).getDesafioId() + "-" + listaDesafios.get(i).getDesafioNombre());
        }
    }

    private void inicializarComponentes(View root) {
        c = Calendar.getInstance();
        valores[0] = format.format(c.getTime());
        actualizarFechas();
        valores[1] = format.format(c.getTime());
        fechasDesafios = new ArrayList<>();

        EditTextNombreRutina = (EditText) root.findViewById(R.id.EditTextNombreRutina);
        GridViewDatosRutina = (GridView) root.findViewById(R.id.GridViewDatosRutina);
        ListViewDesafiosRutina = (ListView) root.findViewById(R.id.ListViewDesafiosRutina);
        EditTextNombreRutina = (EditText) root.findViewById(R.id.EditTextNombreRutina);
        EditTextNotaRutina = (EditText) root.findViewById(R.id.EditTextNotaRutina);
        adapterCrearRutina = new AdapterCrearRutina(getActivity().getApplicationContext(), campos, valores);
        GridViewDatosRutina.setAdapter(adapterCrearRutina);

        adapterDesafio = new AdapterDesafio(getActivity().getApplicationContext(), camposDesafios, valoresDesafios, nombres, objetivos);
        ListViewDesafiosRutina.setAdapter(adapterDesafio);
    }

    private void actualizarFechas() {
        Locale spanish = new Locale("es", "PE");
        for (int i = 0; i < camposDesafios.length; i++) {
            camposDesafios[i] = format.format(c.getTime()) + "     " + c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, spanish);

            if ((i + 1) < camposDesafios.length) {
                c.add(Calendar.DATE, +1);
            }
        }
    }

    public void actualizarAdapter(String[] valoresDesafiosLista, String[] nombresDesafiosLista, String[] objetivosDesafiosLista, String[] seriesDesafiosLista, String[] repeticionesDesafiosListas) {
        valoresDesafios = valoresDesafiosLista;
        nombres = nombresDesafiosLista;
        objetivos = objetivosDesafiosLista;
        series = seriesDesafiosLista;
        repeticiones = repeticionesDesafiosListas;

        adapterDesafio = new AdapterDesafio(getActivity().getApplicationContext(), camposDesafios, valoresDesafios, nombres, objetivos);
        ListViewDesafiosRutina.setAdapter(adapterDesafio);
    }

    public GridView retornarGridview() {
        return GridViewDatosRutina;
    }

    public ListView retornarListView() {
        return ListViewDesafiosRutina;
    }
}

