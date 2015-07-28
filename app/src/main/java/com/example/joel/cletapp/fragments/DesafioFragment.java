package com.example.joel.cletapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.joel.cletapp.CRUDDatabase.DesafioCRUD;
import com.example.joel.cletapp.CRUDDatabase.DesafioObjetivoCRUD;
import com.example.joel.cletapp.CRUDDatabase.ObjetivoCRUD;
import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.ClasesDataBase.DesafioObjetivo;
import com.example.joel.cletapp.ClasesDataBase.Objetivo;
import com.example.joel.cletapp.Mensaje;
import com.example.joel.cletapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Joel on 21/07/2015.
 */
public class DesafioFragment extends Fragment {
    private Button ButtonCrearDesafios;
    private Button ButtonMisDesafios;
    private ImageView ImageViewBotonCrearDesafios;
    private ImageView ImageViewBotonMisDesafios;

    CrearDesafiosFragment crearDesafiosFragment;
    MisDesafiosFragment misDesafiosFragment;

    DesafioCRUD desafioCRUD;
    ObjetivoCRUD objetivoCRUD;
    DesafioObjetivoCRUD desafioObjetivoCRUD;


    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private Date parsed = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_desafio, container, false);

        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Desafios");
        ((ActionBarActivity) getActivity()).getSupportActionBar().setIcon(null);

        Mensaje qwe = new Mensaje(getActivity().getApplicationContext(), "FragmentDesafios creado");

        ButtonCrearDesafios = (Button) root.findViewById(R.id.ButtonCrearDesafios);
        ButtonMisDesafios = (Button) root.findViewById(R.id.ButtonMisDesafios);
        ImageViewBotonCrearDesafios = (ImageView) root.findViewById(R.id.ImageViewBotonCrearDesafios);
        ImageViewBotonMisDesafios = (ImageView) root.findViewById(R.id.ImageViewBotonMisDesafios);
        crearDesafiosFragment = null;
        misDesafiosFragment = null;

        desafioCRUD = new DesafioCRUD(getActivity().getApplicationContext());
        objetivoCRUD = new ObjetivoCRUD(getActivity().getApplicationContext());
        desafioObjetivoCRUD = new DesafioObjetivoCRUD(getActivity().getApplicationContext());

        cargarFragmento(getCrearDesafiosFragment(), R.anim.activity_visible_salida_derecha, R.anim.activity_nuevo_entrada_izquierda);

        //Esto se borrara
        /*try {
            parsed = format.parse("01/01/1990");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Desafio desafio = new Desafio(
                0,
                "Bicicleta",
                "Este",
                new java.sql.Date(parsed.getTime()),
                new java.sql.Date(parsed.getTime()),
                "C".charAt(0),
                true);

        Objetivo objetivo = new Objetivo(0, "Resistencia", "Distancia recorrida");

        long testeo =  desafioCRUD.insertarObjetivo(desafio);

        Desafio desafio1 = new Desafio();
        try {
            desafio1 = desafioCRUD.buscarDesafioPorId(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.v("asd", "Desafio1: " + desafio1.getDesafioNombre());
        Log.v("asd", "Desafio: " + testeo);
        testeo = objetivoCRUD.insertarObjetivo(objetivo);
        Log.v("asd", "Objetivo: " + testeo);

        List<Objetivo> listObjetivo = new ArrayList<Objetivo>();
        listObjetivo = objetivoCRUD.buscarTodosLosObjetivos();
        if (listObjetivo.isEmpty()){
            Log.v("vacio", "La lista esta vacia " + listObjetivo.size());
        }
        else {
            Log.v("vacio", "La lista tiene algo " + listObjetivo.size());
        }

        Objetivo objetivo1 = new Objetivo();
        try {
            objetivo1 = objetivoCRUD.buscarObjetivoPorId(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.v("asd", "Objetivo1: " + objetivo1.getObjetivoNombre());

        DesafioObjetivo desafioObjetivo = new DesafioObjetivo(0, desafio1, objetivo1, 120);

        testeo = desafioObjetivoCRUD.insertarDesafioObjetivo(desafioObjetivo);

        DesafioObjetivo desafioObjetivo1 = new DesafioObjetivo();
        try {
            desafioObjetivo1 = desafioObjetivoCRUD.buscarDesafioObjetivoPorIdDesafio(desafio1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.v("asd", "DesafioDesafio1: " + desafioObjetivo1.getDesafio().getDesafioNombre() + " DesafioObjetivo1: " + desafioObjetivo1.getObjetivo().getObjetivoNombre());*/


        //Hasta aca se borra

        ButtonCrearDesafios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageViewBotonCrearDesafios.setVisibility(View.VISIBLE);
                ImageViewBotonMisDesafios.setVisibility(View.INVISIBLE);
                cargarFragmento(getCrearDesafiosFragment(), R.anim.activity_visible_salida_derecha, R.anim.activity_nuevo_entrada_izquierda);

                ButtonMisDesafios.setClickable(false);
                ButtonMisDesafios.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ButtonMisDesafios.setClickable(true);
                    }
                }, 300);
            }
        });

        ButtonMisDesafios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageViewBotonMisDesafios.setVisibility(View.VISIBLE);
                ImageViewBotonCrearDesafios.setVisibility(View.INVISIBLE);
                cargarFragmento(getMisDesafiosFragment(), R.anim.activity_visible_salida_izquierda, R.anim.activity_nuevo_entrada_derecha);

                ButtonCrearDesafios.setClickable(false);
                ButtonCrearDesafios.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ButtonCrearDesafios.setClickable(true);
                    }
                }, 300);
            }
        });

        return root;
    }

    public void cargarFragmento(Fragment fragment, int animacionSalida, int animacioEntrada) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(animacionSalida, animacioEntrada);
        transaction.replace(R.id.contenedorDesafios, fragment);
        transaction.commit();
    }

    public CrearDesafiosFragment getCrearDesafiosFragment() {
        if (crearDesafiosFragment == null) {
            crearDesafiosFragment = new CrearDesafiosFragment();
        }
        return crearDesafiosFragment;
    }

    public MisDesafiosFragment getMisDesafiosFragment() {
        if (misDesafiosFragment == null) {
            misDesafiosFragment = new MisDesafiosFragment();
        }
        return misDesafiosFragment;
    }
}
