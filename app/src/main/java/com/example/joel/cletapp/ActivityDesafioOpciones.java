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
import android.widget.TextView;

import com.example.joel.cletapp.CRUDDatabase.DesafioCRUD;
import com.example.joel.cletapp.CRUDDatabase.DesafioObjetivoCRUD;
import com.example.joel.cletapp.CRUDDatabase.ObjetivoCRUD;
import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.ClasesDataBase.DesafioObjetivo;
import com.example.joel.cletapp.ClasesDataBase.Objetivo;
import com.example.joel.cletapp.fragments.DialogoCategoriaSelector;
import com.example.joel.cletapp.fragments.DialogoConfirmacion;
import com.example.joel.cletapp.fragments.DialogoDatePickerDesafioFragment;
import com.example.joel.cletapp.fragments.DialogoValorObjetivo;

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

    private String[] campos = {"Fecha de inicio", "Fecha final", "Categoria", "Valor"};
    private String[] valores = {format.format(c.getTime()), format.format(c.getTime()), "", "0 m"};
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
                            DialogFragment pickerInicio = new DialogoDatePickerDesafioFragment();
                            pickerInicio.setArguments(bundle);
                            pickerInicio.show(getSupportFragmentManager(), "datePicker");
                            break;

                        case 1:
                            DialogFragment pickerFinal = new DialogoDatePickerDesafioFragment();
                            pickerFinal.setArguments(bundle);
                            pickerFinal.show(getSupportFragmentManager(), "datePicker2");
                            break;

                        case 2:
                            DialogoCategoriaSelector dialogo = new DialogoCategoriaSelector();
                            dialogo.setArguments(bundle);
                            dialogo.show(getSupportFragmentManager(), "categoriaPicker");
                            break;

                        case 3:
                            DialogoValorObjetivo dialogoValorObjetivo = new DialogoValorObjetivo();
                            dialogoValorObjetivo.setArguments(bundle);
                            dialogoValorObjetivo.show(getSupportFragmentManager(), "valorDialog");
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

        valores[0] = format.format(buscadoDesafio.getInicioDesafio().getTime());
        valores[1] = format.format(buscadoDesafio.getTerminoDesafio().getTime());
        valores[2] = buscadoDesafioObjetivo.getObjetivo().getObjetivoNombre();
        valores[3] = String.valueOf(Math.round(buscadoDesafioObjetivo.getValor())) + " m";

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

            if (buscadoDesafio.getExitoDesafio() == true) {
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
                    buscadoObjetivo = objetivoCRUD.buscarObjetivoPorNombre(valores[2]);
                    buscadoDesafio.setDesafioNombre(EditTextNombreDesafio.getText().toString());
                    buscadoDesafio.setDesafioDescripcion(EditTextNotaDesafio.getText().toString());
                    buscadoDesafio.setInicioDesafio(new java.sql.Date(format.parse(valores[0]).getTime()));
                    buscadoDesafio.setTerminoDesafio(new java.sql.Date(format.parse(valores[1]).getTime()));
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
                buscadoDesafioObjetivo.setValor(Float.parseFloat(valores[3].split(" ")[0]));

                try {
                    buscadoDesafioObjetivo = desafioObjetivoCRUD.actualizarDatosDesafioObjetivo(buscadoDesafioObjetivo);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new Mensaje(this, "Desafio actualizado");
                onBackPressed();
            }
        } else {

        }
    }

    @Override
    public void Eliminar(String data) {
        if (data.equals("Aceptar")) {
            desafioCRUD.eliminarDesafio(buscadoDesafio);
            onBackPressed();
            new Mensaje(this, "Desafio eliminado");
        } else {

        }
    }

    @Override
    public void Reiniciar(String data) {

    }

    private String validarCreacion() {
        String validar = "";

        try {
            parsedInicio = format.parse(valores[0]);
            parsedFinal = format.parse(valores[1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date dateInicio = new java.sql.Date(parsedInicio.getTime());
        Date dateFin = new java.sql.Date(parsedFinal.getTime());

        if (dateInicio.getTime() >= dateFin.getTime()) {
            validar = "La fecha final debe ser despues de: " + valores[0];
        }

        if (valores[3].equals("0 m")) {
            validar = "Ingrese distancia";
        }
        if (valores[2].equals("")) {
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