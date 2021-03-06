package com.example.joel.cletapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joel.cletapp.CRUDDatabase.DesafioCRUD;
import com.example.joel.cletapp.CRUDDatabase.DesafioObjetivoCRUD;
import com.example.joel.cletapp.CRUDDatabase.DesafioRutinaCRUD;
import com.example.joel.cletapp.CRUDDatabase.RepeticionesCRUD;
import com.example.joel.cletapp.CRUDDatabase.RutinaCRUD;
import com.example.joel.cletapp.CRUDDatabase.SerieCRUD;
import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.ClasesDataBase.DesafioObjetivo;
import com.example.joel.cletapp.ClasesDataBase.DesafioRutina;
import com.example.joel.cletapp.ClasesDataBase.Repeticiones;
import com.example.joel.cletapp.ClasesDataBase.Rutina;
import com.example.joel.cletapp.ClasesDataBase.Serie;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ActivityProgresoRutina extends ActionBarActivity {
    private Toolbar toolbar; //Variable para manejar la ToolBar
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private Calendar c = Calendar.getInstance();

    private Long idRutina;

    private TextView TextViewNombreRutina, TextViewFechaInicio, TextViewFechaFin;

    private ListView ListViewDesafiosRutina;
    private String[] camposDesafios = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
    private String[] estadoDesafio = {"P", "P", "P", "P", "P", "P", "P"};
    private String[] exitoDesafio = {"", "", "", "", "", "", ""};
    private int[] series = {0, 0, 0, 0, 0, 0, 0};
    private int[] repeticiones = {0, 0, 0, 0, 0, 0, 0};
    private String[] valoresDesafios = {"valor", "", "", "", "", "", ""};
    private String[] nombres = {"nombre", "", "", "", "", "", ""};
    private Long[] idDesafios = {Long.valueOf(-1), Long.valueOf(-1), Long.valueOf(-1), Long.valueOf(-1), Long.valueOf(-1), Long.valueOf(-1), Long.valueOf(-1)};
    private String[] objetivos = {"objetivo", "", "", "", "", "", ""};
    private AdapterDesafioProgreso adapterDesafio;
    private List<DesafioRutina> listaDesafiosRutina;

    private RutinaCRUD rutinaCRUD;
    private Rutina buscadoRutina;
    private DesafioRutinaCRUD desafioRutinaCRUD;
    private DesafioObjetivo buscadoDesafioObjetivo;
    private DesafioObjetivoCRUD desafioObjetivoCRUD;
    private SerieCRUD serieCRUD;
    private RepeticionesCRUD repeticionesCRUD;
    private DesafioCRUD desafioCRUD;

    private ImageView ImageViewResultado;
    private TextView TextViewResultado;
    private TextView TextViewResultadoDetalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progreso_rutina);
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

        ListViewDesafiosRutina.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AdapterDesafioProgreso adapterDesafioProgresoGet = (AdapterDesafioProgreso) parent.getAdapter();

                if (adapterDesafioProgresoGet.getData(position) != -1) {
                    String idDesafio = String.valueOf(adapterDesafioProgresoGet.getData(position));
                    String valorDesafio = adapterDesafioProgresoGet.getDataValorObjetivo(position);
                    Intent newIntent = new Intent(getApplicationContext(), ActivityProgresoDesafio.class);
                    newIntent.putExtra("Desafio", idDesafio);
                    newIntent.putExtra("valorDesafio", valorDesafio);
                    startActivity(newIntent);
                } else {

                }
            }
        });
    }

    private void inicializarComponentes() {
        ImageViewResultado = (ImageView) findViewById(R.id.ImageViewResultado);
        TextViewResultado = (TextView) findViewById(R.id.TextViewResultado);
        TextViewResultadoDetalle = (TextView) findViewById(R.id.TextViewResultadoDetalle);

        TextViewNombreRutina = (TextView) findViewById(R.id.TextViewNombreRutina);
        TextViewNombreRutina.setText(buscadoRutina.getRutinaNombre());

        TextViewFechaInicio = (TextView) findViewById(R.id.TextViewFechaInicio);
        String fecha = format.format(buscadoRutina.getRutinaInicio());
        TextViewFechaInicio.setText(fecha);

        TextViewFechaFin = (TextView) findViewById(R.id.TextViewFechaFin);
        fecha = format.format(buscadoRutina.getRutinaTermino());
        TextViewFechaFin.setText(fecha);

        c = Calendar.getInstance();
        fecha = format.format(buscadoRutina.getRutinaInicio());
        c.set(Integer.parseInt(fecha.split("/")[2]), Integer.parseInt(fecha.split("/")[1]) - 1, Integer.parseInt((fecha.split("/")[0])));

        actualizarFechas();

        ListViewDesafiosRutina = (ListView) findViewById(R.id.ListViewDesafiosRutina);
        for (int i = 0; i < camposDesafios.length; i++) {
            for (int j = 0; j < listaDesafiosRutina.size(); j++) {
                if (format.format(listaDesafiosRutina.get(j).getFecha()).equals(camposDesafios[i].split("     ")[0])) {
                    nombres[i] = listaDesafiosRutina.get(j).getDesafio().getDesafioNombre();
                    estadoDesafio[i] = String.valueOf(listaDesafiosRutina.get(j).getDesafio().getEstadoDesafio());
                    exitoDesafio[i] = String.valueOf(listaDesafiosRutina.get(j).getDesafio().getExitoDesafio());
                    idDesafios[i] = listaDesafiosRutina.get(j).getDesafio().getDesafioId();

                    List<Serie> seriesDesafio = new ArrayList<>();
                    List<Repeticiones> repeticionesDesafio = new ArrayList<>();
                    int seriesTotal, repeticionesTotal = 0;
                    try {
                        seriesDesafio = serieCRUD.buscarSeriePorIdDesafio(listaDesafiosRutina.get(j).getDesafio());
                        if (!seriesDesafio.isEmpty()) {
                            repeticionesDesafio = repeticionesCRUD.buscarRepeticionesPorIdSerie(seriesDesafio.get(0));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    seriesTotal = seriesDesafio.size();
                    repeticionesTotal = repeticionesDesafio.size();

                    series[i] = seriesTotal;
                    repeticiones[i] = repeticionesTotal;
                    valoresDesafios[i] = listaDesafiosRutina.get(j).getDesafio().getDesafioId() + "-" + listaDesafiosRutina.get(j).getDesafio().getDesafioNombre();
                    try {
                        buscadoDesafioObjetivo = desafioObjetivoCRUD.buscarDesafioObjetivoPorIdDesafio(listaDesafiosRutina.get(j).getDesafio());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    objetivos[i] = String.valueOf(Math.round(buscadoDesafioObjetivo.getValor())) + " m";
                    j = listaDesafiosRutina.size();
                } else {
                    idDesafios[i] = (long) -1;
                    nombres[i] = "Descansar";
                    valoresDesafios[i] = "Descansar";
                }
            }
        }
        adapterDesafio = new AdapterDesafioProgreso(this, camposDesafios, valoresDesafios, nombres, objetivos, estadoDesafio, series, repeticiones, idDesafios);
        ListViewDesafiosRutina.setAdapter(adapterDesafio);


        if (buscadoRutina.getRutinaEstado() != 'T') {
            ImageViewResultado.setImageResource(R.drawable.ic_info_outline_black_24px);
            TextViewResultado.setText("En curso");
            TextViewResultadoDetalle.setText("");
        } else {
            int exitos = 0;
            int fallos = 0;
            int total = 0;
            for (int i = 0; i < exitoDesafio.length; i++) {
                if (exitoDesafio[i].equals("1")) {
                    exitos++;
                } else if (exitoDesafio[i].equals("0")) {
                    fallos++;
                }
                Log.v("asd", exitoDesafio[i]);
            }
            total = exitos + fallos;

            if (exitos > fallos) {
                ImageViewResultado.setImageResource(R.drawable.ic_mood_black_36px);
                TextViewResultado.setText("Buen trabajo!");
            }
            if (exitos < fallos) {
                ImageViewResultado.setImageResource(R.drawable.ic_mood_bad_black_36px);
                TextViewResultado.setText("Esfuerzate mas!");
            }
            if (exitos == fallos) {
                ImageViewResultado.setImageResource(R.drawable.ic_mood_semi_black_36px);
                TextViewResultado.setText("Puedes mejorar!");
            }
            TextViewResultadoDetalle.setText("Completaste " + exitos + "/" + total + " exitosamente");
        }
    }

    private void inicializarBaseDeDatos(Intent intent) {
        idRutina = Long.parseLong(intent.getStringExtra("idRutina"));

        rutinaCRUD = new RutinaCRUD(this);
        desafioCRUD = new DesafioCRUD(this);
        serieCRUD = new SerieCRUD(this);
        repeticionesCRUD = new RepeticionesCRUD(this);

        try {
            buscadoRutina = rutinaCRUD.buscarRutinaPorId(idRutina);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        listaDesafiosRutina = new ArrayList<>();
        desafioRutinaCRUD = new DesafioRutinaCRUD(this);
        try {
            listaDesafiosRutina = desafioRutinaCRUD.buscarDesafioRutinaPorIdRutina(buscadoRutina);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        desafioObjetivoCRUD = new DesafioObjetivoCRUD(this);
    }

    private void inicializarToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_progreso_rutina, menu);
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
}

class AdapterDesafioHistorialProgresoRow {

    TextView nombreCampo;
    TextView valorCampo;
    TextView objetivoCampo;
    TextView TextViewNombreObjetivoDesafioRutina;
    TextView TextViewRepeticionesDesafioRutina;
    ImageView imagenCampo;
    ImageView imagenEstado;
    TextView TextViewCategoriaDesafioRutina;

    public AdapterDesafioHistorialProgresoRow(View view) {
        this.nombreCampo = (TextView) view.findViewById(R.id.TextViewNombreDesafioRutina);
        this.valorCampo = (TextView) view.findViewById(R.id.TextViewValorDesafioRutina);
        this.objetivoCampo = (TextView) view.findViewById(R.id.TextViewObjetivoDesafioRutina);
        this.TextViewNombreObjetivoDesafioRutina = (TextView) view.findViewById(R.id.TextViewNombreObjetivoDesafioRutina);
        this.TextViewRepeticionesDesafioRutina = (TextView) view.findViewById(R.id.TextViewRepeticionesDesafioRutina);
        this.imagenCampo = (ImageView) view.findViewById(R.id.ImageViewImagenDesafioRutina);
        this.imagenEstado = (ImageView) view.findViewById(R.id.ImageViewImagenEstadoDesafio);
        this.TextViewCategoriaDesafioRutina = (TextView) view.findViewById(R.id.TextViewCategoriaDesafioRutina);
    }
}

class AdapterDesafioProgreso extends ArrayAdapter<String> {

    Context context;
    String[] campos;
    String[] valores;
    String[] soloNombre;
    String[] objetivo;
    String[] estadoDesafio;
    int[] series;
    int[] repeticiones;
    Long[] idDesafios;
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    public AdapterDesafioProgreso(Context c, String[] listaCampos, String[] listaValores, String[] listaNombres, String[] listaObjetivos, String[] listaEstadoDesafio, int[] listaSeries, int[] listaRepeticiones, Long[] listaIdDesafios) {
        super(c, R.layout.single_desafio_rutina_row, R.id.TextViewNombreDesafioRutina, listaCampos);
        this.context = c;
        this.campos = listaCampos;
        this.valores = listaValores;
        this.soloNombre = listaNombres;
        this.objetivo = listaObjetivos;
        this.estadoDesafio = listaEstadoDesafio;
        this.series = listaSeries;
        this.repeticiones = listaRepeticiones;
        this.idDesafios = listaIdDesafios;
    }

    public float convertirMetrosToKilometros(float metros) {
        DecimalFormat df = new DecimalFormat("#.#");
        return Float.parseFloat(df.format(metros / 1000));
    }

    DecimalFormat df = new DecimalFormat("#.#");

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    public Long getData(int position) {
        Long data = idDesafios[position];

        return data;
    }

    public String getDataValorObjetivo(int position) {
        String data = objetivo[position].split(" ")[0];
        return data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AdapterDesafioHistorialProgresoRow holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_desafio_progreso_rutina_row, parent, false);
            holder = new AdapterDesafioHistorialProgresoRow(row);
            row.setTag(holder);
        } else {
            holder = (AdapterDesafioHistorialProgresoRow) row.getTag();
        }

        holder.nombreCampo.setText(campos[position]);

        Calendar cInicial = Calendar.getInstance();
        Date actual = cInicial.getTime();
        String fechaActual = format.format(actual);

        holder.valorCampo.setText(soloNombre[position]);
        if (soloNombre[position].equals("Descansar")) {
            holder.valorCampo.setTextColor(context.getResources().getColor(R.color.colorGris2));
            holder.imagenCampo.setImageResource(R.drawable.hdpi_ic_beach_access_black_24dp);
            holder.nombreCampo.setTextColor(context.getResources().getColor(R.color.colorRojo));
            holder.TextViewCategoriaDesafioRutina.setText("");
        } else {
            holder.valorCampo.setTextColor(context.getResources().getColor(R.color.colorNegro));
            holder.imagenCampo.setImageResource(R.drawable.ic_star_black_24dp);
            holder.nombreCampo.setTextColor(context.getResources().getColor(R.color.colorVerde));
        }
        //row.setBackgroundResource(R.color.colorVerde);
        if (series[position] == 0) {
            holder.TextViewNombreObjetivoDesafioRutina.setText("");
            holder.TextViewRepeticionesDesafioRutina.setText("");
            holder.objetivoCampo.setText("");
        } else {
            holder.TextViewNombreObjetivoDesafioRutina.setText(series[position] + " series");
            holder.TextViewRepeticionesDesafioRutina.setText(repeticiones[position] + " repeticiones de: ");

            float distancia = Float.parseFloat(objetivo[position].split(" ")[0]);

            if (distancia >= 1000) {
                holder.objetivoCampo.setText(String.valueOf(convertirMetrosToKilometros(distancia)) + " Km");
            } else if (distancia <= -1000) {
                holder.objetivoCampo.setText(String.valueOf(convertirMetrosToKilometros(distancia)) + " Km");
            } else {
                holder.objetivoCampo.setText(String.valueOf(df.format(distancia)) + " m");
            }

            //holder.objetivoCampo.setText(objetivo[position]);
        }

        if (campos[position].split("     ")[0].equals(fechaActual) && !estadoDesafio[position].equals("T")){
            holder.nombreCampo.setTextColor(context.getResources().getColor(R.color.colorAzul));
        }

        //holder.imagenCampo.setImageResource(R.drawable.ic_star_black_24dp);

        if (estadoDesafio[position].equals("T")) {
            Desafio temporalDesafio = null;
            DesafioCRUD desafioCRUD;
            desafioCRUD = new DesafioCRUD(getContext());
            try {
                temporalDesafio = desafioCRUD.buscarDesafioPorId(idDesafios[position]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (temporalDesafio.getExitoDesafio() == 1) {
                holder.imagenEstado.setImageResource(R.drawable.ic_done_black_24px);
            } else {
                holder.imagenEstado.setImageResource(R.drawable.ic_close_black_24px);
            }
        } else {
            //holder.imagenEstado.setImageResource(R.drawable.pending);
        }


        // Set the background color to white
        //holder.imagenCampo.setBackgroundColor(Color.WHITE);
        // Parse the SVG file from the resource
        //SVG svg = SVGParser.getSVGFromResource(row.getResources(), R.drawable.test_icon);
        // Get a drawable from the parsed SVG and set it as the drawable for the ImageView
        //holder.imagenCampo.setImageDrawable(svg.createPictureDrawable());
        // Set the ImageView as the content view for the Activity
        //setContentView(holder.imagenCampo);

        return row;
    }
}
