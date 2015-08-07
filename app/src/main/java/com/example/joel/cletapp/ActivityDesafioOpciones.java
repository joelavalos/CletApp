package com.example.joel.cletapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.example.joel.cletapp.CRUDDatabase.DesafioCRUD;
import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.fragments.DialogoConfirmacion;

import java.text.ParseException;


public class ActivityDesafioOpciones extends ActionBarActivity {
    private Toolbar toolbar; //Variable para manejar la ToolBar

    private EditText EditTextNombreDesafio;
    private EditText EditTextNotaDesafio;
    private Button ButtonActualizarDesafio;
    private Button ButtonEliminarDesafio;

    private Long idDesafio;
    private DesafioCRUD desafioCRUD;
    private Desafio buscadoDesafio;

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

        ButtonActualizarDesafio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ButtonEliminarDesafio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogoConfirmacion dialogoConfirmacion = new DialogoConfirmacion();
                //DialogoConfirmacion.setArguments(bundle);
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
    }

    private void inicializarComponentes() {
        EditTextNombreDesafio = (EditText) findViewById(R.id.EditTextNombreDesafio);
        EditTextNotaDesafio = (EditText) findViewById(R.id.EditTextNotaDesafio);
        ButtonActualizarDesafio = (Button) findViewById(R.id.ButtonActualizarDesafio);
        ButtonEliminarDesafio = (Button) findViewById(R.id.ButtonEliminarDesafio);

        EditTextNombreDesafio.setText(buscadoDesafio.getDesafioNombre());
        EditTextNotaDesafio.setText(buscadoDesafio.getDesafioDescripcion());
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
}
