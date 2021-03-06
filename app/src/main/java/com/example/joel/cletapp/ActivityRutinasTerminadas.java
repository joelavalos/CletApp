package com.example.joel.cletapp;

import android.content.Context;
import android.content.Intent;
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

import com.example.joel.cletapp.CRUDDatabase.DesafioRutinaCRUD;
import com.example.joel.cletapp.CRUDDatabase.ResumenCRUD;
import com.example.joel.cletapp.CRUDDatabase.RutinaCRUD;
import com.example.joel.cletapp.ClasesDataBase.DesafioRutina;
import com.example.joel.cletapp.ClasesDataBase.Rutina;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class ActivityRutinasTerminadas extends ActionBarActivity {
    private Toolbar toolbar; //Variable para manejar la ToolBar
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    private ListView ListViewRutinasTerminadas;
    private AdapterListaRutinas adapterListaRutinas;
    private String buscar;

    private ArrayList<Integer> imagenes = new ArrayList<>();
    private ArrayList<String> nombreRutinas = new ArrayList<>();
    private ArrayList<String> categoriaRutinas = new ArrayList<>();
    private ArrayList<String> valorRutinas = new ArrayList<>();
    private ArrayList<String> notaRutinas = new ArrayList<>();
    private ArrayList<String> fechaRutinas = new ArrayList<>();
    private ArrayList<String> estadoRutinas = new ArrayList<>();
    private ArrayList<String> idRutinas = new ArrayList<>();

    private RutinaCRUD rutinaCRUD;
    private DesafioRutinaCRUD desafioRutinaCRUD;
    private ResumenCRUD resumenCRUD;
    private List<Rutina> listaRutinas;
    private List<DesafioRutina> listaDesafioRutinas;

    @Override
    protected void onResume() {
        super.onResume();
        //inicializarBaseDeDatos();
        //inicializarComponentes();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutinas_terminadas);

        Bundle extras = getIntent().getExtras();
        buscar = extras.getString("Estado");

        inicializarToolbar();
        inicializarBaseDeDatos();
        inicializarComponentes();

        ListViewRutinasTerminadas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idRutina = (String) parent.getAdapter().getItem(position);
                Intent newIntent = new Intent(getApplicationContext(), ActivityProgresoRutina.class);
                newIntent.putExtra("idRutina", idRutina);
                startActivity(newIntent);
            }
        });
    }

    private void inicializarBaseDeDatos() {
        rutinaCRUD = new RutinaCRUD(this);
        desafioRutinaCRUD = new DesafioRutinaCRUD(this);
        resumenCRUD = new ResumenCRUD(this);

        listaRutinas = new ArrayList<>();
        idRutinas = new ArrayList<>();
        imagenes = new ArrayList<>();
        nombreRutinas = new ArrayList<>();
        notaRutinas = new ArrayList<>();
        fechaRutinas = new ArrayList<>();
        estadoRutinas = new ArrayList<>();
        valorRutinas = new ArrayList<>();
        categoriaRutinas = new ArrayList<>();

        if (buscar.equals("Pendiente")) {
            listaRutinas = rutinaCRUD.buscarTodasLasRutinasPendientes();
        } else {
            listaRutinas = rutinaCRUD.buscarTodasLasRutinasTerminadas();
        }

        for (int i = 0; i < listaRutinas.size(); i++) {
            try {
                listaDesafioRutinas = desafioRutinaCRUD.buscarDesafioRutinaPorIdRutina(listaRutinas.get(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (listaDesafioRutinas.isEmpty()) {
                resumenCRUD.eliminarResumen(listaRutinas.get(i).getResumen());
                rutinaCRUD.eliminarRutina(listaRutinas.get(i));
            } else {
                idRutinas.add(String.valueOf(listaRutinas.get(i).getRutinaId()));
                imagenes.add(R.drawable.ic_directions_bike_black_48dp);
                nombreRutinas.add(listaRutinas.get(i).getRutinaNombre());
                notaRutinas.add(listaRutinas.get(i).getRutinaDescripcion());
                fechaRutinas.add("Desde: " + format.format(listaRutinas.get(i).getRutinaInicio()) + " hasta: " + format.format(listaRutinas.get(i).getRutinaTermino()));

                if (listaRutinas.get(i).getRutinaEstado() == 'P') {
                    estadoRutinas.add("Pendiente");
                } else {
                    estadoRutinas.add("Terminada");
                }

                valorRutinas.add(String.valueOf(Math.round(listaDesafioRutinas.size())));
                categoriaRutinas.add("Desafios");
            }
        }
    }

    private void inicializarComponentes() {
        ListViewRutinasTerminadas = (ListView) findViewById(R.id.ListViewRutinasTerminadas);
        adapterListaRutinas = new AdapterListaRutinas(this, imagenes, nombreRutinas, categoriaRutinas, valorRutinas, notaRutinas, fechaRutinas, estadoRutinas, idRutinas);
        ListViewRutinasTerminadas.setAdapter(adapterListaRutinas);
    }

    private void inicializarToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_rutinas_terminadas, menu);
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

class AdapterRutinaRowTerminada {

    ImageView ImageViewImagenRutina;
    TextView TextViewNombreRutina;
    TextView TextViewCategoriaRutina;
    TextView TextViewValorRutina;
    TextView TextViewNotaRutina;
    TextView TextViewFechaRutina;
    TextView TextViewEstadoRutina;

    public AdapterRutinaRowTerminada(View view) {
        ImageViewImagenRutina = (ImageView) view.findViewById(R.id.ImageViewImagenRutina);
        TextViewNombreRutina = (TextView) view.findViewById(R.id.TextViewNombreRutina);
        TextViewCategoriaRutina = (TextView) view.findViewById(R.id.TextViewCategoriaRutina);
        TextViewValorRutina = (TextView) view.findViewById(R.id.TextViewValorRutina);
        TextViewNotaRutina = (TextView) view.findViewById(R.id.TextViewNotaRutina);
        TextViewFechaRutina = (TextView) view.findViewById(R.id.TextViewFechaRutina);
        TextViewEstadoRutina = (TextView) view.findViewById(R.id.TextViewEstadoRutina);
    }
}

class AdapterListaRutinasTerminadas extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> idRutinas;
    ArrayList<Integer> images = new ArrayList<>();
    ArrayList<String> nombreRutina = new ArrayList<>();
    ArrayList<String> categoriaRutina = new ArrayList<>();
    ArrayList<String> valorRutina = new ArrayList<>();
    ArrayList<String> notaRutina = new ArrayList<>();
    ArrayList<String> fechaRutina = new ArrayList<>();
    ArrayList<String> estadoRutina = new ArrayList<>();

    public AdapterListaRutinasTerminadas(Context c, ArrayList<Integer> ima, ArrayList<String> nombres, ArrayList<String> categorias, ArrayList<String> valores, ArrayList<String> notas, ArrayList<String> fechas, ArrayList<String> estados, ArrayList<String> IDs) {
        super(c, R.layout.single_desafio_row, R.id.TextViewNombreDesafio, nombres);
        this.context = c;
        images = ima;
        nombreRutina = nombres;
        categoriaRutina = categorias;
        valorRutina = valores;
        notaRutina = notas;
        fechaRutina = fechas;
        estadoRutina = estados;
        idRutinas = IDs;
    }

    @Override
    public String getItem(int position) {
        return idRutinas.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        AdapterRutinaRowTerminada holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_rutina_row, parent, false);
            holder = new AdapterRutinaRowTerminada(row);
            row.setTag(holder);
        } else {
            holder = (AdapterRutinaRowTerminada) row.getTag();
        }

        holder.ImageViewImagenRutina.setImageResource(R.drawable.ic_directions_bike_black_48dp);
        holder.TextViewNombreRutina.setText(nombreRutina.get(position));
        holder.TextViewCategoriaRutina.setText(categoriaRutina.get(position));
        holder.TextViewValorRutina.setText(valorRutina.get(position));
        holder.TextViewNotaRutina.setText(notaRutina.get(position));
        holder.TextViewFechaRutina.setText(fechaRutina.get(position));
        holder.TextViewEstadoRutina.setText(estadoRutina.get(position));

        if (estadoRutina.get(position).equals("Pendiente")) {
            holder.TextViewEstadoRutina.setTextColor(context.getResources().getColor(R.color.colorMorado));
        } else if (estadoRutina.get(position).equals("Terminada")) {
            holder.TextViewEstadoRutina.setTextColor(context.getResources().getColor(R.color.colorVerde));
        } else {
            holder.TextViewEstadoRutina.setTextColor(context.getResources().getColor(R.color.colorRojo));
        }

        return row;
    }
}
