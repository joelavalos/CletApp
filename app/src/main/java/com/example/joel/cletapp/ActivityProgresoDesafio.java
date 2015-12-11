package com.example.joel.cletapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joel.cletapp.CRUDDatabase.CiclistaCRUD;
import com.example.joel.cletapp.CRUDDatabase.DesafioCRUD;
import com.example.joel.cletapp.CRUDDatabase.RepeticionesCRUD;
import com.example.joel.cletapp.CRUDDatabase.SerieCRUD;
import com.example.joel.cletapp.ClasesDataBase.Ciclista;
import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.ClasesDataBase.Repeticiones;
import com.example.joel.cletapp.ClasesDataBase.Serie;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class ActivityProgresoDesafio extends ActionBarActivity {
    private Toolbar toolbar; //Variable para manejar la ToolBar
    private DecimalFormat df = new DecimalFormat("#.#");

    private Long idDesafio;
    private List<String> nombresSeries;
    private List<Long> idSeries;

    List<Repeticiones> listaRepeticiones;

    private List<String> nombreRepeticiones;
    private List<Float> distanciaObtenida;
    private List<Integer> distanciaRequerida;
    private List<Float> evaluacionRepeticiones;

    private TextView TextViewNombreDesafio;
    private ListView ListViewSeriesDesafios;
    private ListView ListViewRepeticionesSeries;
    private AdapterDesafioSerie adapterDesafioSerie;
    private AdapterDesafioRepeticion adapterDesafioRepeticion;
    private Serie serieSeleccionada;
    private SerieCRUD serieCRUD;
    private RepeticionesCRUD repeticionesCRUD;
    private DesafioCRUD desafioCRUD;
    private Desafio desafioActual;
    private Ciclista ciclista;
    private CiclistaCRUD ciclistaCRUD;

    private int valorDesafio = 0;
    private View v = null;

    private TextView TextViewDistanciaValor;
    private TextView TextViewDuracionValor;
    private TextView TextViewCaloriasValor;

    private List<Serie> todasLasSeries;
    private List<Repeticiones> todasLasRepeticiones;
    private List<Repeticiones> todasLasRepeticionesDeLaSerie;
    private float distanciaTotal = 0;

    private double constanteIntensidadMedia = 0.049;
    private double constanteIntensidadAlta = 0.071;
    private double constante2 = 2.2;
    private double peso = 100;
    private double minutos = 90;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progreso_desafio);

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

        ListViewSeriesDesafios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (v == null) {
                    v = view;

                } else {
                    v.setBackgroundResource(0);
                    v = view;
                }
                view.setBackgroundResource(R.color.colorSeleccionado);

                AdapterDesafioSerie newASD = (AdapterDesafioSerie) parent.getAdapter();
                try {
                    serieSeleccionada = serieCRUD.buscarSeriePorId(newASD.getData(position));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                crearAdaptadorRepeticiones();
            }
        });
    }

    private void crearAdaptadorRepeticiones() {
        nombreRepeticiones.clear();
        distanciaObtenida.clear();
        distanciaRequerida.clear();
        evaluacionRepeticiones.clear();
        try {
            listaRepeticiones = repeticionesCRUD.buscarRepeticionesPorIdSerie(serieSeleccionada);

            for (int j = 0; j < listaRepeticiones.size(); j++) {
                nombreRepeticiones.add("Repeticion " + (j + 1));
                distanciaObtenida.add(listaRepeticiones.get(j).getValor());
                distanciaRequerida.add(valorDesafio);
                evaluacionRepeticiones.add(listaRepeticiones.get(j).getValor() - valorDesafio);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        adapterDesafioRepeticion = new AdapterDesafioRepeticion(this, nombreRepeticiones, distanciaObtenida, distanciaRequerida, evaluacionRepeticiones);
        ListViewRepeticionesSeries.setAdapter(adapterDesafioRepeticion);
    }

    private void inicializarComponentes() {
        TextViewNombreDesafio = (TextView) findViewById(R.id.TextViewNombreDesafio);
        TextViewDistanciaValor = (TextView) findViewById(R.id.TextViewDistanciaValor);
        TextViewDuracionValor = (TextView) findViewById(R.id.TextViewDuracionValor);
        TextViewCaloriasValor = (TextView) findViewById(R.id.TextViewCaloriasValor);
        ListViewSeriesDesafios = (ListView) findViewById(R.id.ListViewSeriesDesafios);
        ListViewRepeticionesSeries = (ListView) findViewById(R.id.ListViewRepeticionesSeries);

        nombresSeries = new ArrayList<>();
        idSeries = new ArrayList<>();
        listaRepeticiones = new ArrayList<>();

        nombreRepeticiones = new ArrayList<>();
        distanciaObtenida = new ArrayList<>();
        distanciaRequerida = new ArrayList<>();
        evaluacionRepeticiones = new ArrayList<>();

        TextViewNombreDesafio.setText(desafioActual.getDesafioNombre());
        List<Serie> seriesDesafio = new ArrayList<>();

        try {
            seriesDesafio = serieCRUD.buscarSeriePorIdDesafio(desafioActual);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < seriesDesafio.size(); i++) {
            nombresSeries.add("Serie: " + (i + 1));
            idSeries.add(seriesDesafio.get(i).getSerieId());
        }

        adapterDesafioSerie = new AdapterDesafioSerie(this, nombresSeries, idSeries);
        ListViewSeriesDesafios.setAdapter(adapterDesafioSerie);


        if (distanciaTotal >= 1000) {
            TextViewDistanciaValor.setText(convertirMetrosToKilometros(distanciaTotal) + " Km");
        } else {
            TextViewDistanciaValor.setText(df.format(distanciaTotal) + " m");
        }

        TextViewDuracionValor.setText(segundosToHorasCronometro(desafioActual.getCronometro()));
        minutos = desafioActual.getCronometro() / 60;

        try {
            ciclista = ciclistaCRUD.buscarCiclistaPorRut("0");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        peso = ciclista.getCiclistaPeso();

        if (peso == 0) {
            TextViewCaloriasValor.setText("Completar perfil");
        } else {
            //new Mensaje(getApplicationContext(), "Estoy mal comparado");
            TextViewCaloriasValor.setText(String.valueOf(df.format(constanteIntensidadMedia * constante2 * peso * minutos)) + " Kcal");
        }
        //TextViewCaloriasValor.setText(String.valueOf(df.format(constanteIntensidadMedia * constante2 * peso * minutos)) + " Kcal");
    }

    public float convertirMetrosToKilometros(float metros) {
        DecimalFormat df = new DecimalFormat("#.#");
        return Float.parseFloat(df.format(metros / 1000));
    }

    private void inicializarBaseDeDatos(Intent intent) {
        idDesafio = Long.parseLong(intent.getStringExtra("Desafio"));
        valorDesafio = Integer.parseInt(intent.getStringExtra("valorDesafio"));

        ciclistaCRUD = new CiclistaCRUD(this);
        serieCRUD = new SerieCRUD(this);
        desafioCRUD = new DesafioCRUD(this);
        repeticionesCRUD = new RepeticionesCRUD(this);
        todasLasSeries = new ArrayList<>();
        todasLasRepeticiones = new ArrayList<>();
        todasLasRepeticionesDeLaSerie = new ArrayList<>();

        try {
            desafioActual = desafioCRUD.buscarDesafioPorId(idDesafio);
            todasLasSeries = serieCRUD.buscarSeriePorIdDesafio(desafioActual);

            for (int i = 0; i < todasLasSeries.size(); i++) {
                todasLasRepeticionesDeLaSerie = repeticionesCRUD.buscarRepeticionesPorIdSerie(todasLasSeries.get(i));
                for (int j = 0; j < todasLasRepeticionesDeLaSerie.size(); j++) {
                    todasLasRepeticiones.add(todasLasRepeticionesDeLaSerie.get(j));
                    distanciaTotal = distanciaTotal + todasLasRepeticionesDeLaSerie.get(j).getValor();
                }
            }
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
        getMenuInflater().inflate(R.menu.menu_activity_progreso_desafio, menu);
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

    public String segundosToHorasCronometro(int totalSegundos) {
        int horas = totalSegundos / 3600;
        String horasString = String.valueOf(horas);
        if (horasString.length() == 1) {
            horasString = "0" + horasString;
        }

        int minutos = (totalSegundos - (3600 * horas)) / 60;
        String minutosString = String.valueOf(minutos);
        if (minutosString.length() == 1) {
            minutosString = "0" + minutosString;
        }

        int segundos = totalSegundos - ((horas * 3600) + (minutos * 60));
        String segundosString = String.valueOf(segundos);
        if (segundosString.length() == 1) {
            segundosString = "0" + segundosString;
        }

        return horasString + ":" + minutosString + ":" + segundosString;
    }
}

class AdapterDesafioHistorialSerieRow {

    TextView nombreSerie;

    public AdapterDesafioHistorialSerieRow(View view) {
        this.nombreSerie = (TextView) view.findViewById(R.id.TextViewNombreSerieRutina);

    }
}

class AdapterDesafioSerie extends ArrayAdapter<String> {

    Context context;
    List<String> campos;
    List<Long> idSeries;

    public AdapterDesafioSerie(Context c, List<String> listaCampos, List<Long> listaIds) {
        super(c, R.layout.single_desafio_progreso_desafio_row, R.id.TextViewNombreDesafioRutina, listaCampos);
        this.context = c;
        this.campos = listaCampos;
        this.idSeries = listaIds;
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    public Long getData(int position) {
        Long data = idSeries.get(position);

        return data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AdapterDesafioHistorialSerieRow holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_desafio_progreso_desafio_row, parent, false);
            holder = new AdapterDesafioHistorialSerieRow(row);
            row.setTag(holder);
        } else {
            holder = (AdapterDesafioHistorialSerieRow) row.getTag();
        }

        holder.nombreSerie.setText(campos.get(position));

        return row;
    }
}

class AdapterDesafioHistorialRepeticionRow {

    TextView TextViewNombreRepeticion;
    TextView TextViewValorDistanciaRepeticion;
    TextView TextViewValorRequeridoDistanciaRepeticion;
    TextView TextViewValorEvaluacionDistanciaRepeticion;

    public AdapterDesafioHistorialRepeticionRow(View view) {
        this.TextViewNombreRepeticion = (TextView) view.findViewById(R.id.TextViewNombreRepeticion);
        this.TextViewValorDistanciaRepeticion = (TextView) view.findViewById(R.id.TextViewValorDistanciaRepeticion);
        this.TextViewValorRequeridoDistanciaRepeticion = (TextView) view.findViewById(R.id.TextViewValorRequeridoDistanciaRepeticion);
        this.TextViewValorEvaluacionDistanciaRepeticion = (TextView) view.findViewById(R.id.TextViewValorEvaluacionDistanciaRepeticion);

    }
}

class AdapterDesafioRepeticion extends ArrayAdapter<String> {

    Context context;
    List<String> numeroRepeticion;
    List<Float> distanciaRepeticion;
    List<Integer> distanciaRequerida;
    List<Float> valorEvaluacion;

    public AdapterDesafioRepeticion(Context c, List<String> listaNumeroRepeticion, List<Float> listaDistanciaRepeticion, List<Integer> listaDistanciaRequerida, List<Float> listaValorEvaluacion) {
        super(c, R.layout.single_desafio_progreso_repeticion_row, R.id.TextViewNombreDesafioRutina, listaNumeroRepeticion);
        this.context = c;
        this.numeroRepeticion = listaNumeroRepeticion;
        this.distanciaRepeticion = listaDistanciaRepeticion;
        this.distanciaRequerida = listaDistanciaRequerida;
        this.valorEvaluacion = listaValorEvaluacion;
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    public Long getData(int position) {
        //Long data = idSeries.get(position);

        return null;
    }

    public float convertirMetrosToKilometros(float metros) {
        DecimalFormat df = new DecimalFormat("#.#");
        return Float.parseFloat(df.format(metros / 1000));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AdapterDesafioHistorialRepeticionRow holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_desafio_progreso_repeticion_row, parent, false);
            holder = new AdapterDesafioHistorialRepeticionRow(row);
            row.setTag(holder);
        } else {
            holder = (AdapterDesafioHistorialRepeticionRow) row.getTag();
        }

        holder.TextViewNombreRepeticion.setText(numeroRepeticion.get(position));
        DecimalFormat df = new DecimalFormat("#.#");

        if (distanciaRepeticion.get(position) >= 1000) {
            holder.TextViewValorDistanciaRepeticion.setText(String.valueOf(convertirMetrosToKilometros(distanciaRepeticion.get(position))) + " Km" + " de ");
        } else {
            holder.TextViewValorDistanciaRepeticion.setText(String.valueOf(df.format(distanciaRepeticion.get(position))) + " m" + " de ");
        }


        if (distanciaRequerida.get(position) >= 1000) {
            holder.TextViewValorRequeridoDistanciaRepeticion.setText(String.valueOf(convertirMetrosToKilometros(distanciaRequerida.get(position))) + " Km");
        } else {
            holder.TextViewValorRequeridoDistanciaRepeticion.setText(String.valueOf(distanciaRequerida.get(position)) + " m");
        }


        if (valorEvaluacion.get(position) >= 1000) {
            holder.TextViewValorEvaluacionDistanciaRepeticion.setText(String.valueOf(convertirMetrosToKilometros(valorEvaluacion.get(position))) + " Km");
        } else if (valorEvaluacion.get(position) <= -1000) {
            holder.TextViewValorEvaluacionDistanciaRepeticion.setText(String.valueOf(convertirMetrosToKilometros(valorEvaluacion.get(position))) + " Km");
        } else {
            holder.TextViewValorEvaluacionDistanciaRepeticion.setText(String.valueOf(df.format(valorEvaluacion.get(position))) + " m");
        }

        if (valorEvaluacion.get(position) < 0) {
            holder.TextViewValorEvaluacionDistanciaRepeticion.setTextColor(getContext().getResources().getColor(R.color.colorRojo));
        } else if (valorEvaluacion.get(position) > 0) {
            holder.TextViewValorEvaluacionDistanciaRepeticion.setTextColor(getContext().getResources().getColor(R.color.colorVerde));
        } else {
            holder.TextViewValorEvaluacionDistanciaRepeticion.setTextColor(getContext().getResources().getColor(R.color.colorNegro));
        }

        return row;
    }
}

