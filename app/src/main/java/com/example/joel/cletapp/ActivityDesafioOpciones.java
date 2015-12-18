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
import com.example.joel.cletapp.fragments.DialogoCategoriaSelector;
import com.example.joel.cletapp.fragments.DialogoConfirmacion;
import com.example.joel.cletapp.fragments.DialogoValorObjetivo;
import com.example.joel.cletapp.fragments.DialogoValorRepeticiones;
import com.example.joel.cletapp.fragments.DialogoValorSerie;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ActivityDesafioOpciones extends ActionBarActivity implements Communicator {
    private Toolbar toolbar; //Variable para manejar la ToolBar
    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    private TextView TextViewEstadoDesafio;
    private EditText EditTextNombreDesafio;
    private EditText EditTextNotaDesafio;
    private Button ButtonActualizarDesafio;
    private Button ButtonEliminarDesafio;
    private GridView GridViewDatosDesafio;

    private String[] campos = {"Categoria", "Valor", "Series", "Repeticiones"};
    private String[] valores = {"", "0 m", "1", "1"};
    private Date parsedInicio = null;
    private Date parsedFinal = null;
    private ArrayList<String> categorias;

    private Long idDesafio;
    private DesafioCRUD desafioCRUD;
    private Desafio buscadoDesafio;
    private DesafioObjetivoCRUD desafioObjetivoCRUD;
    private DesafioObjetivo buscadoDesafioObjetivo;
    private ObjetivoCRUD objetivoCRUD;
    private Objetivo buscadoObjetivo;
    private AdapterCrearDesafio adapterCrearDesafio;

    private List<Serie> seriesActuales;
    private SerieCRUD serieCRUD;
    private List<Repeticiones> repeticionesActuales;
    private RepeticionesCRUD repeticionesCRUD;
    private List<Repeticiones> repeticionesActualesTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desafio_opciones);
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

        if (!(buscadoDesafio.getEstadoDesafio() == 'P')) {
            GridViewDatosDesafio.setSelector(R.color.colorTransparente);
        }

        GridViewDatosDesafio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if ((buscadoDesafio.getEstadoDesafio() == 'P')) {
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("campos", campos);
                    bundle.putStringArray("valores", valores);
                    bundle.putStringArrayList("categorias", categorias);
                    bundle.putInt("posicion", position);

                    switch (position) {
                        case 0:
                            DialogoCategoriaSelector dialogo = new DialogoCategoriaSelector();
                            dialogo.setArguments(bundle);
                            dialogo.show(getSupportFragmentManager(), "categoriaPicker");
                            break;

                        case 1:
                            DialogoValorObjetivo dialogoValorObjetivo = new DialogoValorObjetivo();
                            dialogoValorObjetivo.setArguments(bundle);
                            dialogoValorObjetivo.show(getSupportFragmentManager(), "valorDialog");
                            break;

                        case 2:
                            DialogoValorSerie dialogoValorSerie = new DialogoValorSerie();
                            dialogoValorSerie.setArguments(bundle);
                            dialogoValorSerie.show(getSupportFragmentManager(), "serieDialog");
                            break;

                        case 3:
                            DialogoValorRepeticiones dialogoValorRepeticion = new DialogoValorRepeticiones();
                            dialogoValorRepeticion.setArguments(bundle);
                            dialogoValorRepeticion.show(getSupportFragmentManager(), "repeticionDialog");
                            break;
                    }
                }
            }
        });

        ButtonActualizarDesafio.setOnClickListener(new View.OnClickListener() {
            Bundle bundle = new Bundle();

            @Override
            public void onClick(View v) {
                bundle.putString("Accion", "Actualizar");
                bundle.putString("Mensaje", getResources().getString(R.string.dialogo_mensaje_actualizar_desafio));
                bundle.putString("Titulo", getResources().getString(R.string.dialogo_titulo_actualizar));
                DialogoConfirmacion dialogoConfirmacion = new DialogoConfirmacion();
                dialogoConfirmacion.setArguments(bundle);
                dialogoConfirmacion.show(getSupportFragmentManager(), "valorDialog");
            }
        });

        ButtonEliminarDesafio.setOnClickListener(new View.OnClickListener() {
            Bundle bundle = new Bundle();

            @Override
            public void onClick(View v) {
                bundle.putString("Accion", "Eliminar");
                bundle.putString("Mensaje", getResources().getString(R.string.dialogo_mensaje_eliminar_desafio));
                bundle.putString("Titulo", getResources().getString(R.string.dialogo_titulo_eliminar));
                DialogoConfirmacion dialogoConfirmacion = new DialogoConfirmacion();
                dialogoConfirmacion.setArguments(bundle);
                dialogoConfirmacion.show(getSupportFragmentManager(), "valorDialog");
            }
        });
    }

    private void inicializarBaseDeDatos(Intent intent) {
        idDesafio = Long.parseLong(intent.getStringExtra("Desafio"));

        seriesActuales = new ArrayList<>();
        serieCRUD = new SerieCRUD(getBaseContext());
        repeticionesActuales = new ArrayList<>();
        repeticionesCRUD = new RepeticionesCRUD(getBaseContext());
        repeticionesActualesTotal = new ArrayList<>();

        desafioCRUD = new DesafioCRUD(this);
        try {
            buscadoDesafio = desafioCRUD.buscarDesafioPorId(idDesafio);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        desafioObjetivoCRUD = new DesafioObjetivoCRUD(this);

        try {
            buscadoDesafioObjetivo = desafioObjetivoCRUD.buscarDesafioObjetivoPorIdDesafio(buscadoDesafio);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        categorias = new ArrayList<>();
        List<Objetivo> listObjetivo;
        objetivoCRUD = new ObjetivoCRUD(this);
        listObjetivo = objetivoCRUD.buscarTodosLosObjetivos();

        for (int i = 0; i < listObjetivo.size(); i++) {
            categorias.add(listObjetivo.get(i).getObjetivoNombre());
        }
    }

    private void inicializarComponentes() {
        GridViewDatosDesafio = (GridView) findViewById(R.id.GridViewDatosDesafio);
        EditTextNombreDesafio = (EditText) findViewById(R.id.EditTextNombreDesafio);
        EditTextNotaDesafio = (EditText) findViewById(R.id.EditTextNotaDesafio);
        ButtonActualizarDesafio = (Button) findViewById(R.id.ButtonActualizarDesafio);
        ButtonEliminarDesafio = (Button) findViewById(R.id.ButtonEliminarDesafio);
        TextViewEstadoDesafio = (TextView) findViewById(R.id.TextViewEstadoDesafio);

        EditTextNombreDesafio.setText(buscadoDesafio.getDesafioNombre());
        EditTextNotaDesafio.setText(buscadoDesafio.getDesafioDescripcion());

        //valores[0] = format.format(buscadoDesafio.getInicioDesafio().getTime());
        //valores[1] = format.format(buscadoDesafio.getTerminoDesafio().getTime());
        valores[0] = buscadoDesafioObjetivo.getObjetivo().getObjetivoNombre();
        valores[1] = String.valueOf(Math.round(buscadoDesafioObjetivo.getValor())) + " m";

        try {
            seriesActuales = serieCRUD.buscarSeriePorIdDesafio(buscadoDesafio);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (int j = 0; j < seriesActuales.size(); j++) {
            try {
                repeticionesActuales = repeticionesCRUD.buscarRepeticionesPorIdSerie(seriesActuales.get(j));
                for (int i = 0; i < repeticionesActuales.size(); i++) {
                    repeticionesActualesTotal.add(repeticionesActuales.get(i));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //Obtener series y repeticiones
        valores[2] = String.valueOf(seriesActuales.size());
        valores[3] = String.valueOf(repeticionesActualesTotal.size() / seriesActuales.size());

        adapterCrearDesafio = new AdapterCrearDesafio(this, campos, valores);
        GridViewDatosDesafio.setAdapter(adapterCrearDesafio);

        if (!(buscadoDesafio.getEstadoDesafio() == 'P')) {
            ButtonActualizarDesafio.setVisibility(View.INVISIBLE);
            ButtonEliminarDesafio.setVisibility(View.INVISIBLE);
            ButtonActualizarDesafio.setClickable(false);
            ButtonEliminarDesafio.setClickable(false);
            EditTextNombreDesafio.setEnabled(false);
            EditTextNotaDesafio.setEnabled(false);
            EditTextNombreDesafio.setTextColor(getResources().getColor(R.color.colorNegro));
            EditTextNotaDesafio.setTextColor(getResources().getColor(R.color.colorNegro));

            if (buscadoDesafio.getExitoDesafio() == 1) {
                TextViewEstadoDesafio.setText("Logrado");
                TextViewEstadoDesafio.setTextColor(getResources().getColor(R.color.colorVerde));
            } else {
                TextViewEstadoDesafio.setText("No logrado");
                TextViewEstadoDesafio.setTextColor(getResources().getColor(R.color.colorRojo));
            }
        } else {
            TextViewEstadoDesafio.setText("Pendiente");
            TextViewEstadoDesafio.setTextColor(getResources().getColor(R.color.colorMorado));
        }
    }

    private void inicializarToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_desafio_opciones, menu);
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

    @Override
    public void Actualizar(String data) {
        if (data.equals("Aceptar")) {

            if (!validarCreacion().equals("")) {
                new Mensaje(this, validarCreacion());
            } else {
                try {
                    buscadoObjetivo = objetivoCRUD.buscarObjetivoPorNombre(valores[0]);
                    buscadoDesafio.setDesafioNombre(EditTextNombreDesafio.getText().toString());
                    buscadoDesafio.setDesafioDescripcion(EditTextNotaDesafio.getText().toString());
                    buscadoDesafio.setInicioDesafio(new java.sql.Date(format.parse(format.format(c.getTime())).getTime()));
                    buscadoDesafio.setTerminoDesafio(new java.sql.Date(format.parse(format.format(c.getTime())).getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    buscadoDesafio = desafioCRUD.actualizarDatosDesafio(buscadoDesafio);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                buscadoDesafioObjetivo.setDesafio(buscadoDesafio);
                buscadoDesafioObjetivo.setObjetivo(buscadoObjetivo);
                buscadoDesafioObjetivo.setValor(Float.parseFloat(valores[1].split(" ")[0]));

                try {
                    buscadoDesafioObjetivo = desafioObjetivoCRUD.actualizarDatosDesafioObjetivo(buscadoDesafioObjetivo);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //Hay que borrer las series y repeticiones antiguas
                for (int j = 0; j < seriesActuales.size(); j++) {
                    serieCRUD.eliminarSerie(seriesActuales.get(j));
                }
                //

                crearSeriesRepeticiones(buscadoDesafio, Integer.parseInt(valores[2]), Integer.parseInt(valores[3]));

                new Mensaje(this, "Desafio actualizado");
                onBackPressed();
            }
        } else {

        }
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

    @Override
    public void Eliminar(String data) {
        if (data.equals("Aceptar")) {
            if (buscadoDesafio.getEstadoDesafio() == 'I') {
                new Mensaje(this, "El desafio esta en uso");
            } else {
                desafioCRUD.eliminarDesafio(buscadoDesafio);
                onBackPressed();
                new Mensaje(this, "Desafio eliminado");
            }
        } else {

        }
    }

    @Override
    public void Reiniciar(String data) {

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

    @Override
    public void OcultarDetalle(String data) {

    }

    @Override
    public void pruebaDialogToDialog(String[] desafios, String[] valores, String[] nombres, String[] objetivos) {

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
        holder.valorCampo.setText(valores[position]);

        return row;
    }
}