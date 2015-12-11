package com.example.joel.cletapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.joel.cletapp.R;

/**
 * Created by Joel on 20/07/2015.
 */
public class HistorialFragment extends Fragment {

    private Button ButtonHistorialDesafios;
    private Button ButtonHistorialRutinas;
    private ImageView ImageViewBotonDesafios;
    private ImageView ImageViewBotonRutinas;

    private ListaDesafiosFragment listaDesafiosFragment;
    private ListaRutinasFragment listaRutinasFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem itemOculto = menu.findItem(R.id.action_search);
        itemOculto.setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_historial, container, false);

        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Historial");
        ((ActionBarActivity) getActivity()).getSupportActionBar().setIcon(null);

        inicializarComponentes(root);

        cargarFragmento(getListaDesafiosFragment(), R.anim.activity_visible_salida_derecha, R.anim.activity_nuevo_entrada_izquierda);

        ButtonHistorialDesafios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageViewBotonDesafios.setVisibility(View.VISIBLE);
                ImageViewBotonRutinas.setVisibility(View.INVISIBLE);
                cargarFragmento(getListaDesafiosFragment(), R.anim.activity_visible_salida_derecha, R.anim.activity_nuevo_entrada_izquierda);

                ButtonHistorialRutinas.setClickable(false);
                ButtonHistorialRutinas.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ButtonHistorialRutinas.setClickable(true);
                    }
                }, 300);
            }
        });

        ButtonHistorialRutinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageViewBotonDesafios.setVisibility(View.INVISIBLE);
                ImageViewBotonRutinas.setVisibility(View.VISIBLE);
                cargarFragmento(getListaRutinasFragment(), R.anim.activity_visible_salida_izquierda, R.anim.activity_nuevo_entrada_derecha);

                ButtonHistorialDesafios.setClickable(false);
                ButtonHistorialDesafios.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ButtonHistorialDesafios.setClickable(true);
                    }
                }, 300);
            }
        });

        return root;
    }

    private void inicializarComponentes(View root) {
        ButtonHistorialDesafios = (Button) root.findViewById(R.id.ButtonHistorialDesafios);
        ButtonHistorialRutinas = (Button) root.findViewById(R.id.ButtonHistorialRutinas);
        ImageViewBotonDesafios = (ImageView) root.findViewById(R.id.ImageViewBotonDesafios);
        ImageViewBotonRutinas = (ImageView) root.findViewById(R.id.ImageViewBotonRutinas);

        listaDesafiosFragment = null;
        listaRutinasFragment = null;
    }

    public void cargarFragmento(Fragment fragment, int animacionSalida, int animacioEntrada) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(animacionSalida, animacioEntrada);
        transaction.replace(R.id.contenedorHistorial, fragment);
        transaction.commit();
    }

    public ListaDesafiosFragment getListaDesafiosFragment() {
        if (listaDesafiosFragment == null) {
            listaDesafiosFragment = new ListaDesafiosFragment();
        }
        return listaDesafiosFragment;
    }

    public ListaRutinasFragment getListaRutinasFragment() {
        if (listaRutinasFragment == null) {
            listaRutinasFragment = new ListaRutinasFragment();
        }

        return listaRutinasFragment;
    }
}
