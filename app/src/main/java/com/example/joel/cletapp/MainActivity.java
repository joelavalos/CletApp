package com.example.joel.cletapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.joel.cletapp.CRUDDatabase.DesafioCRUD;
import com.example.joel.cletapp.CRUDDatabase.DesafioObjetivoCRUD;
import com.example.joel.cletapp.CRUDDatabase.ObjetivoCRUD;
import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.ClasesDataBase.DesafioObjetivo;
import com.example.joel.cletapp.ClasesDataBase.Objetivo;
import com.example.joel.cletapp.fragments.MainFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar; //Variable para manejar la ToolBar
    private NavigationDrawerFragment drawerFargment; //Variable para manejar el Navegation
    private MainFragment mainFragment;
    private DesafioCRUD desafioCRUD;
    private DesafioObjetivoCRUD desafioObjetivoCRUD;
    private ObjetivoCRUD objetivoCRUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_appbar);

        Mensaje asdasd = new Mensaje(this, "MainActivity creado");

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_directions_bike_white_18dp);

        drawerFargment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFargment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        poblarBaseDatos();


        int seleccion = 0;
        drawerFargment.listOpciones.performItemClick(drawerFargment.listOpciones.getAdapter().getView(seleccion, null, null), seleccion, drawerFargment.listOpciones.getAdapter().getItemId(seleccion));
        //drawerFargment.cargarFragmento2(getMainFragment(), R.anim.activity_visible_salida_izquierda, R.anim.activity_nuevo_entrada_derecha);
    }

    private void poblarBaseDatos() {
        desafioCRUD = new DesafioCRUD(this);
        desafioObjetivoCRUD = new DesafioObjetivoCRUD(this);
        objetivoCRUD = new ObjetivoCRUD(this);

        List<Desafio> listaDesafios = new ArrayList<Desafio>();
        listaDesafios = desafioCRUD.buscarTodosLosDesafios();
        List<Objetivo> listObjetivo;

        listObjetivo = objetivoCRUD.buscarTodosLosObjetivos();

        if (listObjetivo.isEmpty()) {
            Objetivo objetivo = new Objetivo(0, "Distancia", "Distancia recorrida");
            objetivoCRUD.insertarObjetivo(objetivo);
        }

        Objetivo objetivo = null;
        try {
            objetivo = objetivoCRUD.buscarObjetivoPorNombre("Distancia");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (listaDesafios.isEmpty()){
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date parsedInicio = null;
            Date parsedFinal = null;
            String[] nombres = {"Desafio prueba 1", "Desafio prueba 2", "Desafio prueba 3", "Desafio prueba 4", "Desafio prueba 5"};
            String[] notas = {"Nota 1", "Nota prueba 2", "Sin nota", "Quiero bajar de peso", "Ja Ja Ja"};
            float[] valores = {125, 145, 160, 170, 200};
            char[] estado = {'T', 'P', 'P', 'T', 'T'};
            boolean[] exito = {true, false, false, false, true};

            try {
                parsedInicio = format.parse("01/01/2015");
                parsedFinal = format.parse("02/01/2015");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < nombres.length; i++) {

                Desafio desafio = new Desafio(0,
                        nombres[i],
                        notas[i],
                        new java.sql.Date(parsedInicio.getTime()),
                        new java.sql.Date(parsedFinal.getTime()),
                        estado[i],
                        exito[i]);

                desafio = desafioCRUD.insertarDesafio(desafio);

                DesafioObjetivo desafioObjetivo = new DesafioObjetivo(0, desafio, objetivo, valores[i]);
                desafioObjetivoCRUD.insertarDesafioObjetivo(desafioObjetivo);
            }
        }
    }

    public void cargarFragmento(Fragment fragment, int animacionSalida, int animacioEntrada) {
        FragmentManager manager = this.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(animacionSalida, animacioEntrada, animacionSalida, animacioEntrada);
        transaction.replace(R.id.contenedor, fragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Mensaje nuevoMensaje = new Mensaje(this, "boton apretado");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public MainFragment getMainFragment() {
        if (mainFragment == null) {
            mainFragment = new MainFragment();
        }
        return mainFragment;
    }

    /*@Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            //drawerFargment.listOpciones.setAdapter(drawerFargment.adapter);
            //getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }*/
}
