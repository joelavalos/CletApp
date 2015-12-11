package com.example.joel.cletapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.joel.cletapp.CRUDDatabase.DesafioObjetivoCRUD;
import com.example.joel.cletapp.CRUDDatabase.DesafioRutinaCRUD;
import com.example.joel.cletapp.CRUDDatabase.ResumenCRUD;
import com.example.joel.cletapp.CRUDDatabase.RutinaCRUD;
import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.ClasesDataBase.DesafioObjetivo;
import com.example.joel.cletapp.ClasesDataBase.DesafioRutina;
import com.example.joel.cletapp.ClasesDataBase.Resumen;
import com.example.joel.cletapp.ClasesDataBase.Rutina;
import com.example.joel.cletapp.fragments.DialogoConfirmacion;
import com.example.joel.cletapp.fragments.DialogoDatePickerRutinaFragment;
import com.example.joel.cletapp.fragments.DialogoDesafioSelector;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ActivityRutinaOpciones extends ActionBarActivity implements Communicator {
    private Toolbar toolbar; //Variable para manejar la ToolBar
    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private boolean firstTime = true;

    private EditText EditTextNombreRutina;
    private EditText EditTextNotaRutina;
    private TextView TextViewEstadoRutina;
    private GridView GridViewDatosRutina;
    private Button ButtonActualizarRutina;
    private Button ButtonEliminarRutina;

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

    private Long idRutina;
    private RutinaCRUD rutinaCRUD;
    private Rutina buscadoRutina;
    private DesafioRutinaCRUD desafioRutinaCRUD;
    private DesafioRutina newDesafioRutina;
    private DesafioCRUD desafioCRUD;
    private ResumenCRUD resumenCRUD;
    private Resumen buscadoResumen;
    private DesafioObjetivoCRUD desafioObjetivoCRUD;
    private DesafioObjetivo buscadoDesafioObjetivo;
    private List<Desafio> listaDesafios;
    private ArrayList<String> listaDesafios2;
    private List<DesafioRutina> listaDesafiosRutina;
    private Desafio newDesafio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutina_opciones);
        Intent intent = getIntent();

        inicializarToolbar();
        inicializarBaseDeDatos(intent);
        inicializarComponentes();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        GridViewDatosRutina.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if ((buscadoRutina.getRutinaEstado() == 'P')) {
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
                            pickerInicio.show(getSupportFragmentManager(), "datePicker");
                            break;
                    }
                }
            }
        });

        ListViewDesafiosRutina.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if ((buscadoRutina.getRutinaEstado() == 'P')) {
                    if (firstTime == true) {
                        Bundle bundle = new Bundle();
                        bundle.putString("Accion", "Reiniciar");
                        bundle.putString("Mensaje", getResources().getString(R.string.dialogo_mensaje_reiniciar));
                        bundle.putString("Titulo", getResources().getString(R.string.dialogo_titulo_reiniciar));
                        DialogoConfirmacion dialogoConfirmacion = new DialogoConfirmacion();
                        dialogoConfirmacion.setArguments(bundle);
                        dialogoConfirmacion.show(getSupportFragmentManager(), "valorDialog");

                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putStringArray("camposDesafios", camposDesafios);
                        bundle.putStringArray("valoresDesafios", valoresDesafios);
                        bundle.putStringArray("nombres", nombres);
                        bundle.putStringArray("objetivos", objetivos);
                        bundle.putStringArrayList("desafios", listaDesafios2);
                        bundle.putInt("posicion", position);

                        DialogoDesafioSelector dialogo = new DialogoDesafioSelector();
                        dialogo.setArguments(bundle);
                        dialogo.show(getSupportFragmentManager(), "categoriaPicker");
                    }
                }
            }
        });

        ButtonActualizarRutina.setOnClickListener(new View.OnClickListener() {
            Bundle bundle = new Bundle();

            @Override
            public void onClick(View v) {
                bundle.putString("Accion", "Actualizar");
                bundle.putString("Mensaje", getResources().getString(R.string.dialogo_mensaje_actualizar_rutina));
                bundle.putString("Titulo", getResources().getString(R.string.dialogo_titulo_actualizar));
                DialogoConfirmacion dialogoConfirmacion = new DialogoConfirmacion();
                dialogoConfirmacion.setArguments(bundle);
                dialogoConfirmacion.show(getSupportFragmentManager(), "valorDialog");
            }
        });

        ButtonEliminarRutina.setOnClickListener(new View.OnClickListener() {
            Bundle bundle = new Bundle();

            @Override
            public void onClick(View v) {
                bundle.putString("Accion", "Eliminar");
                bundle.putString("Mensaje", getResources().getString(R.string.dialogo_mensaje_eliminar_rutina));
                bundle.putString("Titulo", getResources().getString(R.string.dialogo_titulo_eliminar));
                DialogoConfirmacion dialogoConfirmacion = new DialogoConfirmacion();
                dialogoConfirmacion.setArguments(bundle);
                dialogoConfirmacion.show(getSupportFragmentManager(), "valorDialog");
            }
        });
    }

    private void reiniciarDatos() {
        for (int i = 0; i < valoresDesafios.length; i++) {
            valoresDesafios[i] = "Descansar";
            //valoresDesafios[i] = "";
            nombres[i] = "Descansar";
            //nombres[i] = "";
            objetivos[i] = "";
        }
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        c = Calendar.getInstance();
        String fecha = format.format(buscadoRutina.getRutinaInicio());
        c.set(Integer.parseInt(fecha.split("/")[2]), Integer.parseInt(fecha.split("/")[1]) - 1, Integer.parseInt((fecha.split("/")[0])));
        valores[0] = format.format(c.getTime());
        actualizarFechas();
        valores[1] = format.format(c.getTime());
        fechasDesafios = new ArrayList<>();

        EditTextNombreRutina = (EditText) findViewById(R.id.EditTextNombreRutina);
        EditTextNotaRutina = (EditText) findViewById(R.id.EditTextNotaRutina);
        TextViewEstadoRutina = (TextView) findViewById(R.id.TextViewEstadoRutina);
        GridViewDatosRutina = (GridView) findViewById(R.id.GridViewDatosRutina);
        ListViewDesafiosRutina = (ListView) findViewById(R.id.ListViewDesafiosRutina);
        ButtonActualizarRutina = (Button) findViewById(R.id.ButtonActualizarRutina);
        ButtonEliminarRutina = (Button) findViewById(R.id.ButtonEliminarRutina);

        adapterCrearRutina = new AdapterCrearRutina(this, campos, valores);
        GridViewDatosRutina.setAdapter(adapterCrearRutina);

        if (firstTime == true) {
            EditTextNombreRutina.setText(buscadoRutina.getRutinaNombre());
            EditTextNotaRutina.setText(buscadoRutina.getRutinaDescripcion());
            for (int i = 0; i < camposDesafios.length; i++) {
                for (int j = 0; j < listaDesafiosRutina.size(); j++) {
                    if (format.format(listaDesafiosRutina.get(j).getFecha()).equals(camposDesafios[i].split("     ")[0])) {
                        nombres[i] = listaDesafiosRutina.get(j).getDesafio().getDesafioNombre();
                        valoresDesafios[i] = listaDesafiosRutina.get(j).getDesafio().getDesafioId() + "-" + listaDesafiosRutina.get(j).getDesafio().getDesafioNombre();
                        try {
                            buscadoDesafioObjetivo = desafioObjetivoCRUD.buscarDesafioObjetivoPorIdDesafio(listaDesafiosRutina.get(j).getDesafio());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        objetivos[i] = String.valueOf(Math.round(buscadoDesafioObjetivo.getValor())) + " m";
                        j = listaDesafiosRutina.size();
                    } else {
                        nombres[i] = "Descansar";
                        valoresDesafios[i] = "Descansar";
                    }
                }
            }
            adapterDesafio = new AdapterDesafio(this, camposDesafios, valoresDesafios, nombres, objetivos);
            ListViewDesafiosRutina.setAdapter(adapterDesafio);
        } else {
            adapterDesafio = new AdapterDesafio(this, camposDesafios, valoresDesafios, nombres, objetivos);
            ListViewDesafiosRutina.setAdapter(adapterDesafio);
        }

        if (!(buscadoRutina.getRutinaEstado() == 'P')) {
            ButtonActualizarRutina.setVisibility(View.INVISIBLE);
            ButtonEliminarRutina.setVisibility(View.INVISIBLE);
            ButtonActualizarRutina.setClickable(false);
            ButtonEliminarRutina.setClickable(false);
            EditTextNombreRutina.setEnabled(false);
            EditTextNotaRutina.setEnabled(false);
            EditTextNombreRutina.setTextColor(getResources().getColor(R.color.colorNegro));
            EditTextNotaRutina.setTextColor(getResources().getColor(R.color.colorNegro));

            TextViewEstadoRutina.setText("Terminada");
            TextViewEstadoRutina.setTextColor(getResources().getColor(R.color.colorVerde));
        }else{
            TextViewEstadoRutina.setText("Pendiente");
            TextViewEstadoRutina.setTextColor(getResources().getColor(R.color.colorMorado));
        }
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

    private void inicializarBaseDeDatos(Intent intent) {
        idRutina = Long.parseLong(intent.getStringExtra("idRutina"));

        rutinaCRUD = new RutinaCRUD(this);
        try {
            buscadoRutina = rutinaCRUD.buscarRutinaPorId(idRutina);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        desafioCRUD = new DesafioCRUD(this);
        listaDesafios = desafioCRUD.buscarTodosLosDesafiosPendientes();

        listaDesafios2 = new ArrayList<>();
        listaDesafios2.add("Descansar");
        for (int i = 0; i < listaDesafios.size(); i++) {
            listaDesafios2.add(listaDesafios.get(i).getDesafioId() + "-" + listaDesafios.get(i).getDesafioNombre());
        }

        desafioRutinaCRUD = new DesafioRutinaCRUD(this);
        listaDesafiosRutina = new ArrayList<>();
        try {
            listaDesafiosRutina = desafioRutinaCRUD.buscarDesafioRutinaPorIdRutina(buscadoRutina);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        desafioObjetivoCRUD = new DesafioObjetivoCRUD(this);

        resumenCRUD = new ResumenCRUD(this);
        try {
            buscadoResumen = resumenCRUD.buscarResumenPorId(buscadoRutina.getResumen().getResumenId());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void inicializarToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_rutina_opciones, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void Actualizar(String data) {
        if (data.equals("Aceptar")) {
            if (!validarCreacion().equals("")) {
                Mensaje asd = new Mensaje(getApplicationContext(), validarCreacion());
            } else {
                try {
                    parsedInicio = format.parse(valores[0]);
                    parsedFinal = format.parse(valores[1]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                buscadoRutina.setRutinaNombre(EditTextNombreRutina.getText().toString());
                buscadoRutina.setRutinaDescripcion(EditTextNotaRutina.getText().toString());
                buscadoRutina.setRutinaInicio(new java.sql.Date(parsedInicio.getTime()));
                buscadoRutina.setRutinaTermino(new java.sql.Date(parsedFinal.getTime()));

                try {
                    buscadoRutina = rutinaCRUD.actualizarRutina(buscadoRutina);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < listaDesafios.size(); i++) {
                    desafioRutinaCRUD.eliminarDesafioRutina(listaDesafios.get(i), buscadoRutina);
                }

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
                    newDesafioRutina = new DesafioRutina(0, buscadoRutina, listaDesafios.get(i), new java.sql.Date(parsedDesafio.getTime()));
                    newDesafioRutina = desafioRutinaCRUD.insertarDesafioRutina(newDesafioRutina);
                }

                new Mensaje(this, "Rutina actualizada");
                onBackPressed();
            }
        } else {

        }
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

    @Override
    public void Eliminar(String data) {
        if (data.equals("Aceptar")) {
            rutinaCRUD.eliminarRutina(buscadoRutina);
            resumenCRUD.eliminarResumen(buscadoRutina.getResumen());
            onBackPressed();
            new Mensaje(this, "Rutina eliminada");
        } else {

        }
    }

    @Override
    public void Reiniciar(String data) {
        if (data.equals("Aceptar")) {
            firstTime = false;
            reiniciarDatos();
        } else {

        }
    }

    @Override
    public void TerminarRutina(String data) {

    }

    @Override
    public void DiasSeleccionados(ArrayList<Integer> data) {

    }

    @Override
    public void SeleccionarRuta(String data) {

    }

    @Override
    public void GuardarRuta(String data) {

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

class AdapterDesafioHistorialRow {

    TextView nombreCampo;
    TextView valorCampo;
    TextView objetivoCampo;
    ImageView imagenCampo;

    public AdapterDesafioHistorialRow(View view) {
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
        AdapterDesafioHistorialRow holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_desafio_rutina_row, parent, false);
            holder = new AdapterDesafioHistorialRow(row);
            row.setTag(holder);
        } else {
            holder = (AdapterDesafioHistorialRow) row.getTag();
        }

        holder.nombreCampo.setText(campos[position]);
        holder.valorCampo.setText(soloNombre[position]);
        if (soloNombre[position].equals("Descansar")){
            holder.valorCampo.setTextColor(context.getResources().getColor(R.color.colorGris2));
        }
        holder.objetivoCampo.setText(objetivo[position]);
        holder.imagenCampo.setImageResource(R.drawable.ic_star_black_24dp);

        return row;
    }
}
