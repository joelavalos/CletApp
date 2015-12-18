package com.example.joel.cletapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ActivityGlosario extends ActionBarActivity {

    private Toolbar toolbar; //Variable para manejar la ToolBar

    private ImageView ImageViewFotoEntrada;
    private TextView TextViewNombreEntrada;
    private TextView TextViewDescripccionEntrada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glosario);

        inicializarComponentes();

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

        Intent newIntent = getIntent();
        if (newIntent.getStringExtra("EntradaGlosario").equals("Rutina")) {
            TextViewNombreEntrada.setText(newIntent.getStringExtra("EntradaGlosario"));
            TextViewDescripccionEntrada.setText(R.string.descRutina);
            ImageViewFotoEntrada.setImageResource(R.drawable.glosario_rutina);

        } else if (newIntent.getStringExtra("EntradaGlosario").equals("Desafio")) {
            TextViewNombreEntrada.setText(newIntent.getStringExtra("EntradaGlosario"));
            TextViewDescripccionEntrada.setText(R.string.descDesafio);
            ImageViewFotoEntrada.setImageResource(R.drawable.glosario_desafio);
        }
    }

    private void inicializarComponentes() {
        ImageViewFotoEntrada = (ImageView) findViewById(R.id.ImageViewFotoEntrada);
        TextViewNombreEntrada = (TextView) findViewById(R.id.TextViewNombreEntrada);
        TextViewDescripccionEntrada = (TextView) findViewById(R.id.TextViewDescripccionEntrada);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_glosario, menu);
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
