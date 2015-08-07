package com.example.joel.cletapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
 * Created by Joel on 04/08/2015.
 */
public class CrearRutinasFragment extends Fragment {
    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    private String[] campos = {"Fecha de inicio", "Fecha final"};
    private String[] valores = {format.format(c.getTime()), format.format(c.getTime())};
    private Date parsedInicio;
    private Date parsedFinal;
    private Date parsedDesafio;
    private List<String> fechasDesafios;
    private AdapterCrearRutina adapterCrearRutina;

    private ListView ListViewDesafiosRutina;
    private String[] camposDesafios = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
    private String[] valoresDesafios = {"", "", "", "", "", "", ""};
    private String[] nombres = {"", "", "", "", "", "", ""};
    private String[] objetivos = {"", "", "", "", "", "", ""};
    private AdapterDesafio adapterDesafio;

    private GridView GridViewDatosRutina;
    private Button ButtonCrearRutina;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_rutina_crear, container, false);

        inicializarBaseDeDatos();
        inicializarComponentes(root);

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

        ButtonCrearRutina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validarCreacion().equals("")) {
                    Mensaje asd = new Mensaje(getActivity().getApplicationContext(), validarCreacion());
                } else {

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

                    listaDesafios.clear();

                    for (int i = 0; i < valoresDesafios.length; i++) {
                        if (!valoresDesafios[i].equals("Descansar")) {
                            try {
                                newDesafio = desafioCRUD.buscarDesafioPorId(Long.parseLong(valoresDesafios[i].split("-")[0]));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            listaDesafios.add(newDesafio);
                            fechasDesafios.add(camposDesafios[i].split(" ")[0]);
                        }
                    }

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
                    reiniciarDatos(root);
                }
            }
        });

        return root;
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
        actualizarFechas();
        valores[1] = format.format(c.getTime());
        fechasDesafios = new ArrayList<>();

        EditTextNombreRutina = (EditText) root.findViewById(R.id.EditTextNombreRutina);
        ButtonCrearRutina = (Button) root.findViewById(R.id.ButtonCrearRutina);
        GridViewDatosRutina = (GridView) root.findViewById(R.id.GridViewDatosRutina);
        ListViewDesafiosRutina = (ListView) root.findViewById(R.id.ListViewDesafiosRutina);
        ButtonCrearRutina = (Button) root.findViewById(R.id.ButtonCrearRutina);
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

class AdapterDesafioRow {

    TextView nombreCampo;
    TextView valorCampo;
    TextView objetivoCampo;
    ImageView imagenCampo;

    public AdapterDesafioRow(View view) {
        this.nombreCampo = (TextView) view.findViewById(R.id.TextViewNombreDesafioRutina);
        this.valorCampo = (TextView) view.findViewById(R.id.TextViewValorDesafioRutina);
        this.objetivoCampo = (TextView) view.findViewById(R.id.TextViewObjetivoDesafioRutina);
        this.imagenCampo = (ImageView) view.findViewById(R.id.ImageViewImagenDesafioRutina);
    }
}

class AdapterDesafio extends ArrayAdapter<String> {

    Context context;
    String[] campos;
    String[] valores;
    String[] soloNombre;
    String[] objetivo;

    public AdapterDesafio(Context c, String[] listaCampos, String[] listaValores, String[] listaNombres, String[] listaObjetivos) {
        super(c, R.layout.single_desafio_rutina_row, R.id.TextViewNombreDesafioRutina, listaCampos);
        this.context = c;
        this.campos = listaCampos;
        this.valores = listaValores;
        this.soloNombre = listaNombres;
        this.objetivo = listaObjetivos;
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    public String getData(int position) {
        String data = valores[position] + " " + soloNombre[position];

        return data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AdapterDesafioRow holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_desafio_rutina_row, parent, false);
            holder = new AdapterDesafioRow(row);
            row.setTag(holder);
        } else {
            holder = (AdapterDesafioRow) row.getTag();
        }

        holder.nombreCampo.setText(campos[position]);
        holder.valorCampo.setText(soloNombre[position]);
        holder.objetivoCampo.setText(objetivo[position]);
        holder.imagenCampo.setImageResource(R.drawable.ic_star_black_24dp);

        return row;
    }
}
