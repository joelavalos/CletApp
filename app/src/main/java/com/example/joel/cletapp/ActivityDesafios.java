package com.example.joel.cletapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.example.joel.cletapp.CRUDDatabase.ObjetivoCRUD;
import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.ClasesDataBase.DesafioObjetivo;
import com.example.joel.cletapp.ClasesDataBase.Objetivo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class ActivityDesafios extends ActionBarActivity {

    private Toolbar toolbar; //Variable para manejar la ToolBar
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    private AdapterListaDesafios adapterListaDesafios;
    private ListView ListViewDesafiosPendientes;
    private List<Desafio> listDesafiosPendientes;
    private DesafioObjetivo desafioObjetivoBuscado;
    private Objetivo objetivoBuscado;
    private String buscar;

    private ArrayList<Integer> imagenes = new ArrayList<>();
    private ArrayList<String> nombreDesafios = new ArrayList<>();
    private ArrayList<String> categoriaDesafios = new ArrayList<>();
    private ArrayList<String> valorDesafios = new ArrayList<>();
    private ArrayList<String> notaDesafios = new ArrayList<>();
    private ArrayList<String> fechaDesafios = new ArrayList<>();
    private ArrayList<String> estadoDesafios = new ArrayList<>();
    private ArrayList<String> idDesafios = new ArrayList<>();

    private DesafioCRUD desafioCRUD;
    private ObjetivoCRUD objetivoCRUD;
    private DesafioObjetivoCRUD desafioObjetivoCRUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desafios);

        Bundle extras = getIntent().getExtras();
        buscar = extras.getString("Estado");

        inicializarToolbar();
        inicializarBaseDeDatos();
        inicializarComponentes();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ListViewDesafiosPendientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idDesafio = (String) parent.getAdapter().getItem(position);
                Log.v("testeando", idDesafio);
            }
        });
    }

    private void inicializarBaseDeDatos() {
        desafioCRUD = new DesafioCRUD(this);
        objetivoCRUD = new ObjetivoCRUD(this);
        desafioObjetivoCRUD = new DesafioObjetivoCRUD(this);

        if (buscar.equals("Pendiente")) {
            listDesafiosPendientes = desafioCRUD.buscarTodosLosDesafiosPendientes();
        } else if (buscar.equals("Terminado")) {
            listDesafiosPendientes = desafioCRUD.buscarTodosLosDesafiosTerminados();
        } else if (buscar.equals("TerminadoLogrado")) {
            listDesafiosPendientes = desafioCRUD.buscarTodosLosDesafiosTerminadosLogrados();
        } else {
            listDesafiosPendientes = desafioCRUD.buscarTodosLosDesafiosTerminadosNoLogrados();
        }

        for (int i = 0; i < listDesafiosPendientes.size(); i++) {
            idDesafios.add(String.valueOf(listDesafiosPendientes.get(i).getDesafioId()));
            imagenes.add(R.drawable.ic_grade_black_48dp);
            nombreDesafios.add(listDesafiosPendientes.get(i).getDesafioNombre());
            notaDesafios.add(listDesafiosPendientes.get(i).getDesafioDescripcion());
            fechaDesafios.add("Desde: " + format.format(listDesafiosPendientes.get(i).getInicioDesafio()) + " hasta: " + format.format(listDesafiosPendientes.get(i).getTerminoDesafio()));

            Log.v("asd", listDesafiosPendientes.get(i).getExitoDesafio() + "");
            if (listDesafiosPendientes.get(i).getEstadoDesafio() == 'P') {
                estadoDesafios.add("Pendiente");
            } else {
                if (listDesafiosPendientes.get(i).getExitoDesafio()) {
                    estadoDesafios.add("Logrado");
                } else {
                    estadoDesafios.add("No logrado");
                }
            }

            try {
                desafioObjetivoBuscado = desafioObjetivoCRUD.buscarDesafioObjetivoPorIdDesafio(listDesafiosPendientes.get(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                objetivoBuscado = objetivoCRUD.buscarObjetivoPorIdDesafioObjetivo(desafioObjetivoBuscado);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valorDesafios.add(String.valueOf(Math.round(desafioObjetivoBuscado.getValor())) + " m");
            categoriaDesafios.add(objetivoBuscado.getObjetivoNombre());
        }
    }

    private void inicializarToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void inicializarComponentes() {
        ListViewDesafiosPendientes = (ListView) findViewById(R.id.ListViewDesafiosPendientes);
        adapterListaDesafios = new AdapterListaDesafios(this, imagenes, nombreDesafios, categoriaDesafios, valorDesafios, notaDesafios, fechaDesafios, estadoDesafios, idDesafios);
        ListViewDesafiosPendientes.setAdapter(adapterListaDesafios);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_desafios, menu);
        return true;
    }

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

class AdapterDesafioRow {

    ImageView ImageViewImagenDesafio;
    TextView TextViewNombreDesafio;
    TextView TextViewCategoriaDesafio;
    TextView TextViewValorDesafio;
    TextView TextViewNotaDesafio;
    TextView TextViewFechaDesafio;
    TextView TextViewEstado;

    public AdapterDesafioRow(View view) {
        ImageViewImagenDesafio = (ImageView) view.findViewById(R.id.ImageViewImagenDesafio2);
        TextViewNombreDesafio = (TextView) view.findViewById(R.id.TextViewNombreDesafio);
        TextViewCategoriaDesafio = (TextView) view.findViewById(R.id.TextViewCategoriaDesafio);
        TextViewValorDesafio = (TextView) view.findViewById(R.id.TextViewValorDesafio);
        TextViewNotaDesafio = (TextView) view.findViewById(R.id.TextViewNotaDesafio);
        TextViewFechaDesafio = (TextView) view.findViewById(R.id.TextViewFechaDesafio);
        TextViewEstado = (TextView) view.findViewById(R.id.TextViewEstado);
    }
}

class AdapterListaDesafios extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> idDesafios;
    ArrayList<Integer> images = new ArrayList<>();
    ArrayList<String> nombreDesafio = new ArrayList<>();
    ArrayList<String> categoriaDesafio = new ArrayList<>();
    ArrayList<String> valorDesafio = new ArrayList<>();
    ArrayList<String> notaDesafio = new ArrayList<>();
    ArrayList<String> fechaDesafio = new ArrayList<>();
    ArrayList<String> estadoDesafio = new ArrayList<>();

    public AdapterListaDesafios(Context c, ArrayList<Integer> ima, ArrayList<String> nombres, ArrayList<String> categorias, ArrayList<String> valores, ArrayList<String> notas, ArrayList<String> fechas, ArrayList<String> estados, ArrayList<String> IDs) {
        super(c, R.layout.single_desafio_row, R.id.TextViewNombreDesafio, nombres);
        this.context = c;
        images = ima;
        nombreDesafio = nombres;
        categoriaDesafio = categorias;
        valorDesafio = valores;
        notaDesafio = notas;
        fechaDesafio = fechas;
        estadoDesafio = estados;
        idDesafios = IDs;
    }

    @Override
    public String getItem(int position) {
        return idDesafios.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        AdapterDesafioRow holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_desafio_row, parent, false);
            holder = new AdapterDesafioRow(row);
            row.setTag(holder);
        } else {
            holder = (AdapterDesafioRow) row.getTag();
        }

        holder.ImageViewImagenDesafio.setImageResource(R.drawable.ic_grade_black_48dp);
        holder.TextViewNombreDesafio.setText(nombreDesafio.get(position));
        holder.TextViewCategoriaDesafio.setText(categoriaDesafio.get(position));
        holder.TextViewValorDesafio.setText(valorDesafio.get(position));
        holder.TextViewNotaDesafio.setText(notaDesafio.get(position));
        holder.TextViewFechaDesafio.setText(fechaDesafio.get(position));
        holder.TextViewEstado.setText(estadoDesafio.get(position));

        if (estadoDesafio.get(position).equals("Pendiente")) {
            holder.TextViewEstado.setTextColor(context.getResources().getColor(R.color.colorMorado));
        } else if (estadoDesafio.get(position).equals("Logrado")) {
            holder.TextViewEstado.setTextColor(context.getResources().getColor(R.color.colorVerde));
        } else {
            holder.TextViewEstado.setTextColor(context.getResources().getColor(R.color.colorRojo));
        }

        return row;
    }
}
