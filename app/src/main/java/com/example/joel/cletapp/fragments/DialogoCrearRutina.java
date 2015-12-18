package com.example.joel.cletapp.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joel.cletapp.CRUDDatabase.DesafioCRUD;
import com.example.joel.cletapp.CRUDDatabase.DesafioRutinaCRUD;
import com.example.joel.cletapp.CRUDDatabase.ResumenCRUD;
import com.example.joel.cletapp.CRUDDatabase.RutinaCRUD;
import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.ClasesDataBase.DesafioRutina;
import com.example.joel.cletapp.ClasesDataBase.Resumen;
import com.example.joel.cletapp.ClasesDataBase.Rutina;
import com.example.joel.cletapp.Communicator;
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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        comm = (Communicator) getActivity();
        view = inflater.inflate(R.layout.fragment_detalle_crear_rutina, null);
        builder.setView(view);

        inicializarBaseDeDatos();
        inicializarComponentes(view);

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

        ListViewDesafiosRutina.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putStringArray("camposDesafios", camposDesafios);
                bundle.putStringArray("valoresDesafios", valoresDesafios);
                bundle.putStringArray("nombres", nombres);
                bundle.putStringArray("objetivos", objetivos);
                bundle.putStringArrayList("desafios", listaDesafios2);
                bundle.putInt("posicion", position);

                //Borrar luego
                //AdapterDesafio pepelota = (AdapterDesafio) ListViewDesafiosRutina.getAdapter();
                //Mensaje asd = new Mensaje(getActivity().getApplicationContext(), pepelota.getData(position));

                DialogoDesafioSelector dialogo = new DialogoDesafioSelector();
                dialogo.setArguments(bundle);
                dialogo.show(getFragmentManager(), "categoriaPicker");
            }
        });

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //comm.pruebaDialogToDialog("Exito ql");

            }
        });
        return builder.create();
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

    public void actualizarAdapter(String[] lista1, String[] lista2, String[] lista3, String[] lista4) {
        //adapterDesafio = new AdapterDesafio(getActivity().getApplicationContext(), lista1, lista2, lista3, lista4);
        //list
    }

    public GridView retornarGridview(){
        return GridViewDatosRutina;
    }

    public ListView retornarListView(){
        return ListViewDesafiosRutina;
    }
}

