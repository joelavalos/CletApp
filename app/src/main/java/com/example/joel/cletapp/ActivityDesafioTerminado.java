package com.example.joel.cletapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class ActivityDesafioTerminado extends ActionBarActivity {
    private Toolbar toolbar; //Variable para manejar la ToolBar

    private long idDesafio;
    private String duracion;
    private double calorias;
    private int distancia;
    private int pulso;
    private String nombreDesafio;
    private String notaDesafio;
    private String fechaDesafio;

    private TextView TextViewNombreDesafio, TextViewNotaDesafio, TextViewFechaTerminoDesafio, TextViewDuracionValor, TextViewDistanciaValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desafio_terminado);

        obtenerExtras();

        inicializarToolbar();
        inicializarComponentes();
        cargarDatos();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void cargarDatos() {
        TextViewNombreDesafio.setText(nombreDesafio);
        TextViewNotaDesafio.setText(notaDesafio);
        TextViewFechaTerminoDesafio.setText(fechaDesafio);
        TextViewDuracionValor.setText(duracion);
        TextViewDistanciaValor.setText("800 de " + distancia +" m");
    }

    private void obtenerExtras() {
        Bundle extras = getIntent().getExtras();
        idDesafio = Long.parseLong(extras.getString("Desafio"));
        duracion = extras.getString("Duracion");
        distancia = Integer.parseInt(extras.getString("Distancia"));
        calorias = extras.getInt("Calorias");
        pulso = extras.getInt("Pulso");
        nombreDesafio = extras.getString("Nombre");
        notaDesafio = extras.getString("Nota");
        fechaDesafio = extras.getString("Fecha");
    }

    private void inicializarToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void inicializarComponentes() {
        TextViewNombreDesafio = (TextView) findViewById(R.id.TextViewNombreDesafio);
        TextViewNotaDesafio = (TextView) findViewById(R.id.TextViewNotaDesafio);
        TextViewFechaTerminoDesafio = (TextView) findViewById(R.id.TextViewFechaTerminoDesafio);
        TextViewDuracionValor = (TextView) findViewById(R.id.TextViewDuracionValor);
        TextViewDistanciaValor = (TextView) findViewById(R.id.TextViewDistanciaValor);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_desafio_terminado, menu);
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
