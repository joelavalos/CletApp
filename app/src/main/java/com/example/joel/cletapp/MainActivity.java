package com.example.joel.cletapp;

import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
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

import com.example.joel.cletapp.fragments.MainFragment;
import com.example.joel.cletapp.fragments.PerfilFragment;


public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar; //Variable para manejar la ToolBar
    private NavigationDrawerFragment drawerFargment; //Variable para manejar el Navegation
    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_appbar);

        Mensaje asdasd = new Mensaje(this, "MainActivity creado");

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_directions_bike_white_18dp);

        Bundle bundle = new Bundle();
        String myMessage = "Stackoverflow is cool!";
        bundle.putString("message", myMessage);


        drawerFargment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFargment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        int seleccion = 0;
        drawerFargment.listOpciones.performItemClick(drawerFargment.listOpciones.getAdapter().getView(seleccion, null, null), seleccion, drawerFargment.listOpciones.getAdapter().getItemId(seleccion));
        //drawerFargment.cargarFragmento2(getMainFragment(), R.anim.activity_visible_salida_izquierda, R.anim.activity_nuevo_entrada_derecha);

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
