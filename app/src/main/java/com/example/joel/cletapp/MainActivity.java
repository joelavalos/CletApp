package com.example.joel.cletapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.joel.cletapp.CRUDDatabase.DesafioCRUD;
import com.example.joel.cletapp.CRUDDatabase.DesafioObjetivoCRUD;
import com.example.joel.cletapp.CRUDDatabase.DesafioRutinaCRUD;
import com.example.joel.cletapp.CRUDDatabase.ObjetivoCRUD;
import com.example.joel.cletapp.CRUDDatabase.RepeticionesCRUD;
import com.example.joel.cletapp.CRUDDatabase.ResumenCRUD;
import com.example.joel.cletapp.CRUDDatabase.RutaCRUD;
import com.example.joel.cletapp.CRUDDatabase.RutinaCRUD;
import com.example.joel.cletapp.CRUDDatabase.SerieCRUD;
import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.ClasesDataBase.DesafioObjetivo;
import com.example.joel.cletapp.ClasesDataBase.DesafioRutina;
import com.example.joel.cletapp.ClasesDataBase.Objetivo;
import com.example.joel.cletapp.ClasesDataBase.Repeticiones;
import com.example.joel.cletapp.ClasesDataBase.Resumen;
import com.example.joel.cletapp.ClasesDataBase.Ruta;
import com.example.joel.cletapp.ClasesDataBase.Rutina;
import com.example.joel.cletapp.ClasesDataBase.Serie;
import com.example.joel.cletapp.fragments.Cronometro;
import com.example.joel.cletapp.fragments.DialogoCrearRutina;
import com.example.joel.cletapp.fragments.DialogoDetalleDesafio;
import com.example.joel.cletapp.fragments.MainFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity implements Communicator {

    private Toolbar toolbar; //Variable para manejar la ToolBar
    private NavigationDrawerFragment drawerFargment; //Variable para manejar el Navegation
    private MainFragment mainFragment;
    private DesafioCRUD desafioCRUD;
    private DesafioObjetivoCRUD desafioObjetivoCRUD;
    private ObjetivoCRUD objetivoCRUD;
    private int seleccion;

    private SerieCRUD serieCRUD;
    private RepeticionesCRUD repeticionesCRUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_appbar);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_directions_bike_white_18dp);

        inicializarComponentes();
        poblarBaseDatos();

        seleccion = 0;
        drawerFargment.listOpciones.performItemClick(drawerFargment.listOpciones.getAdapter().getView(seleccion, null, null), seleccion, drawerFargment.listOpciones.getAdapter().getItemId(seleccion));
        //drawerFargment.cargarFragmento2(getMainFragment(), R.anim.activity_visible_salida_izquierda, R.anim.activity_nuevo_entrada_derecha);
    }

    private void inicializarComponentes() {
        drawerFargment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFargment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
    }

    private void poblarBaseDatos() {
        desafioCRUD = new DesafioCRUD(this);
        desafioObjetivoCRUD = new DesafioObjetivoCRUD(this);
        objetivoCRUD = new ObjetivoCRUD(this);

        serieCRUD = new SerieCRUD(this);
        repeticionesCRUD = new RepeticionesCRUD(this);

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

        if (listaDesafios.isEmpty()) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date parsedInicio = null;
            Date parsedFinal = null;
            String[] nombres = {"Desafio prueba 1", "Desafio prueba 2", "Desafio prueba 3", "Desafio prueba 4", "Desafio prueba 5"};
            String[] notas = {"Nota 1", "Nota prueba 2", "Sin nota", "Quiero bajar de peso", "Ja Ja Ja"};
            float[] valores = {10000, 25000, 30000, 46000, 60000};
            char[] estado = {'T', 'P', 'P', 'T', 'T'};
            int[] exito = {1, 0, 0, 0, 1};
            int[] series = {2, 2, 3, 4, 5};
            int[] repeticiones = {3, 4, 3, 2, 1};

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
                        exito[i],
                        series[i],
                        repeticiones[i],
                        5400);

                desafio = desafioCRUD.insertarDesafio(desafio);

                DesafioObjetivo desafioObjetivo = new DesafioObjetivo(0, desafio, objetivo, valores[i]);
                desafioObjetivoCRUD.insertarDesafioObjetivo(desafioObjetivo);

                for (int h = 0; h < series[i]; h++) {
                    Serie serie = new Serie(0, desafio);
                    serie = serieCRUD.insertarSerie(serie);

                    for (int j = 0; j < repeticiones[i]; j++) {
                        Repeticiones addRepeticiones = new Repeticiones(0, serie, 0);
                        if (j == 0 && h == 0){
                            addRepeticiones.setValor(13000);
                        }

                        if (j == 1 && h == 0){
                            addRepeticiones.setValor(14580);
                        }

                        if (j == 2 && h == 0){
                            addRepeticiones.setValor(25900);
                        }
                        repeticionesCRUD.insertarRepeticion(addRepeticiones);
                    }
                }
            }
        }

        RutinaCRUD rutinaCRUD = new RutinaCRUD(this);
        List<Rutina> listaRutinas = rutinaCRUD.buscarTodasLasRutinas();

        if (listaRutinas.isEmpty()) {
            //Insertar resumen
            Date parsedFechaDesafio = null;
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                parsedFechaDesafio = format.parse("01/01/2015");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ResumenCRUD resumenCRUD = new ResumenCRUD(this);
            Resumen newResumen = new Resumen(0, "Resumen defecto", new java.sql.Date(parsedFechaDesafio.getTime()));
            newResumen = resumenCRUD.insertarResumen(newResumen);

            //Insertar rutina
            Date parsedInicio = null;
            Date parsedFinal = null;
            try {
                parsedInicio = format.parse("01/01/2015");
                parsedFinal = format.parse("02/01/2015");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Rutina newRutina = new Rutina(0, "Rutina 1", "Rutina defecto", new java.sql.Date(parsedInicio.getTime()), new java.sql.Date(parsedFinal.getTime()), 'T', newResumen);
            newRutina = rutinaCRUD.insertarRutina(newRutina);

            //Buscar desafio
            DesafioCRUD desafioCRUD = new DesafioCRUD(this);
            Desafio newDesafio = null;
            try {
                newDesafio = desafioCRUD.buscarDesafioPorId(1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //Insertar desafioRutina
            DesafioRutinaCRUD desafioRutinaCRUD = new DesafioRutinaCRUD(this);
            DesafioRutina newDesafioRutina = new DesafioRutina(0, newRutina, newDesafio, new java.sql.Date(parsedInicio.getTime()));
            newDesafioRutina = desafioRutinaCRUD.insertarDesafioRutina(newDesafioRutina);
        }

        RutaCRUD rutaCRUD = new RutaCRUD(this);

        if (rutaCRUD.buscarTodasLasRutas().isEmpty()){
            Ruta nuevaRuta = new Ruta();
            nuevaRuta.setRutaNombre("Ruta larga");
            nuevaRuta.setRutaCordenadas("-33.54179451052759=-70.55631310000001X-33.54192417381435=-70.55499613537368X-33.53943371046656=-70.55632114662704");
            rutaCRUD.insertarRuta(nuevaRuta);
            nuevaRuta = new Ruta();
            nuevaRuta.setRutaNombre("Ruta media");
            nuevaRuta.setRutaCordenadas("-29.60880770194574=133.24786600000004X-39.313839609651154=-71.05658075000002X46.433078443460715=2.20882574999996");
            rutaCRUD.insertarRuta(nuevaRuta);
            nuevaRuta = new Ruta();
            nuevaRuta.setRutaNombre("Ruta corta");
            nuevaRuta.setRutaCordenadas("37.945467316675405=104.13611175000005X-16.395887034238665=-63.5493965");
            rutaCRUD.insertarRuta(nuevaRuta);
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

        MenuItem itemOculto = menu.findItem(R.id.action_settings);
        itemOculto.setVisible(false);
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

        if (id == R.id.action_search) {
            FragmentManager manager = this.getSupportFragmentManager();
            MainFragment asd = (MainFragment) manager.findFragmentByTag("TagPrincipal");
            asd.caminoDeRuta();
            return true;
        }

        if (id == R.id.action_view) {
            FragmentManager manager = this.getSupportFragmentManager();
            MainFragment asd = (MainFragment) manager.findFragmentByTag("TagPrincipal");
            asd.estadoRutinaActual();
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

    @Override
    public void Actualizar(String data) {
        FragmentManager manager = this.getSupportFragmentManager();
        MainFragment asd = (MainFragment) manager.findFragmentByTag("TagPrincipal");
        asd.actualizarDatos(data);
    }

    @Override
    public void Eliminar(String data) {
        FragmentManager manager = this.getSupportFragmentManager();
        MainFragment asd = (MainFragment) manager.findFragmentByTag("TagPrincipal");
        asd.detenerRutina(data);
    }

    @Override
    public void Reiniciar(String data) {
        new Mensaje(this, data);
    }

    @Override
    public void TerminarRutina(String data) {
        FragmentManager manager = this.getSupportFragmentManager();
        MainFragment asd = (MainFragment) manager.findFragmentByTag("TagPrincipal");
        asd.terminarRutina(data);
    }

    @Override
    public void DiasSeleccionados(ArrayList<Integer> data) {
        FragmentManager manager = this.getSupportFragmentManager();
        MainFragment asd = (MainFragment) manager.findFragmentByTag("TagPrincipal");
        asd.diasSeleccionados(data);
    }

    @Override
    public void SeleccionarRuta(String data) {
        FragmentManager manager = this.getSupportFragmentManager();
        MainFragment asd = (MainFragment) manager.findFragmentByTag("TagPrincipal");
        asd.actualizarRuta(data);
    }

    @Override
    public void GuardarRuta(String data) {
        FragmentManager manager = this.getSupportFragmentManager();
        MainFragment asd = (MainFragment) manager.findFragmentByTag("TagPrincipal");
        asd.guardarRuta(data);
    }

    @Override
    public void OcultarDetalle(String data) {
        //FragmentManager manager = this.getSupportFragmentManager();
        //MainFragment asd = (MainFragment) manager.findFragmentByTag("TagPrincipal");
        //asd.ocultar();
    }

    @Override
    public void pruebaDialogToDialog(String[] valoresDesafios, String[] nombres, String[] objetivos, String[] series, String[] repeticiones) {
        FragmentManager manager = this.getSupportFragmentManager();
        DialogoCrearRutina asd = (DialogoCrearRutina) manager.findFragmentByTag("crearRutina");
        //Fragment prev = this.getSupportFragmentManager().findFragmentByTag("rutinaPersonalizada");
        //if (prev != null) {
            //DialogFragment df = (DialogFragment) prev;
            //df.dismiss();
            asd.actualizarAdapter(valoresDesafios, nombres, objetivos, series, repeticiones);
        //}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //guardarEstadoDesafioDetenido();
        //guardarEstadoDesafioNoPause();
        //pararCronometro();
    }

    private void guardarEstadoDesafioDetenido() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("estadoDesafio", "detenido");
        editor.commit();
    }

    private void guardarEstadoDesafioNoPause() {
        //pararCronometro();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("estadoDesafioPause", "nopause");
        editor.putInt("valorDesafioPause", 0);
        editor.commit();
    }

    private void pararCronometro() {
        Intent service = new Intent(getBaseContext(), Cronometro.class);
        stopService(service);
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
