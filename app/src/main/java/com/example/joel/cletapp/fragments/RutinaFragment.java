package com.example.joel.cletapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.joel.cletapp.R;

/**
 * Created by Joel on 20/07/2015.
 */
public class RutinaFragment extends Fragment {
    private Button ButtonCrearRutinas;
    private Button ButtonMisRutinas;
    private ImageView ImageViewBotonCrearRutina;
    private ImageView ImageViewBotonMisRutinas;

    private CrearRutinasFragment crearRutinasFragment;
    private MisRutinasFragment misRutinasFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rutina, container, false);

        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Rutinas");
        ((ActionBarActivity) getActivity()).getSupportActionBar().setIcon(null);

        inicializarComponentes(root);
        inicializarFragments();

        cargarFragmento(getCrearRutinasFragment(), R.anim.activity_visible_salida_derecha, R.anim.activity_nuevo_entrada_izquierda);

        ButtonCrearRutinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageViewBotonCrearRutina.setVisibility(View.VISIBLE);
                ImageViewBotonMisRutinas.setVisibility(View.INVISIBLE);

                cargarFragmento(getCrearRutinasFragment(), R.anim.activity_visible_salida_derecha, R.anim.activity_nuevo_entrada_izquierda);

                ButtonMisRutinas.setClickable(false);
                ButtonMisRutinas.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ButtonMisRutinas.setClickable(true);
                    }
                }, 300);
            }
        });

        ButtonMisRutinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageViewBotonCrearRutina.setVisibility(View.INVISIBLE);
                ImageViewBotonMisRutinas.setVisibility(View.VISIBLE);

                cargarFragmento(getMisRutinasFragment(), R.anim.activity_visible_salida_izquierda, R.anim.activity_nuevo_entrada_derecha);

                ButtonCrearRutinas.setClickable(false);
                ButtonCrearRutinas.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ButtonCrearRutinas.setClickable(true);
                    }
                }, 300);
            }
        });

        return root;
    }

    private void inicializarFragments() {
        crearRutinasFragment = null;
        misRutinasFragment = null;
    }

    private void inicializarComponentes(View root) {
        ButtonCrearRutinas = (Button) root.findViewById(R.id.ButtonCrearRutinas);
        ButtonMisRutinas = (Button) root.findViewById(R.id.ButtonMisRutinas);
        ImageViewBotonCrearRutina = (ImageView) root.findViewById(R.id.ImageViewBotonCrearRutina);
        ImageViewBotonMisRutinas = (ImageView) root.findViewById(R.id.ImageViewBotonMisRutinas);
    }

    public void cargarFragmento(Fragment fragment, int animacionSalida, int animacioEntrada) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(animacionSalida, animacioEntrada);
        transaction.replace(R.id.contenedorRutinas, fragment);
        transaction.commit();
    }

    public CrearRutinasFragment getCrearRutinasFragment() {
        if (crearRutinasFragment == null) {
            crearRutinasFragment = new CrearRutinasFragment();
        }
        return crearRutinasFragment;
    }

    public MisRutinasFragment getMisRutinasFragment() {
        if (misRutinasFragment == null) {
            misRutinasFragment = new MisRutinasFragment();
        }
        return misRutinasFragment;
    }
}
