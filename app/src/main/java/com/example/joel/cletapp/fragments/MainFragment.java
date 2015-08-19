package com.example.joel.cletapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joel.cletapp.CRUDDatabase.RutinaCRUD;
import com.example.joel.cletapp.ClasesDataBase.Rutina;
import com.example.joel.cletapp.HeartRateMonitor;
import com.example.joel.cletapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 21/07/2015.
 */
public class MainFragment extends Fragment {

    private Button ButtonFrecuencioa;

    private ImageView ImageViewImagenDesafio2;
    private TextView TextViewNombreDesafio;
    private TextView TextViewCategoriaDesafio;
    private TextView TextViewValorDesafio;
    private TextView TextViewNotaDesafio;
    private TextView TextViewFechaDesafio;
    private TextView TextViewEstado;
    private TextView TextViewElegirRutina;

    private RutinaCRUD rutinaCRUD;
    private List<Rutina> listaRutinasIniciadas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("CletApp");
        ((ActionBarActivity) getActivity()).getSupportActionBar().setIcon(R.drawable.ic_directions_bike_white_18dp);

        inicializarBaseDeDatos();
        inicializarComponentes(root);

        TextViewElegirRutina.setOnClickListener(new View.OnClickListener() {
            Bundle bundle = new Bundle();

            @Override
            public void onClick(View v) {

                DialogoRutinaSelector dialogo = new DialogoRutinaSelector();
                dialogo.setArguments(bundle);
                dialogo.show(getFragmentManager(), "categoriaPicker");
            }
        });

        ButtonFrecuencioa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getActivity().getApplicationContext(), HeartRateMonitor.class);
                startActivity(newIntent);
            }
        });

        return root;
    }

    private void inicializarBaseDeDatos() {
        rutinaCRUD = new RutinaCRUD(getActivity().getApplicationContext());
        listaRutinasIniciadas = new ArrayList<>();

        listaRutinasIniciadas = rutinaCRUD.buscarTodasLasRutinasIniciadas();
    }

    private void inicializarComponentes(View root) {
        ButtonFrecuencioa = (Button) root.findViewById(R.id.ButtonFrecuencioa);
        ImageViewImagenDesafio2 = (ImageView) root.findViewById(R.id.ImageViewImagenDesafio2);
        TextViewNombreDesafio = (TextView) root.findViewById(R.id.TextViewNombreDesafio);
        TextViewCategoriaDesafio = (TextView) root.findViewById(R.id.TextViewCategoriaDesafio);
        TextViewValorDesafio = (TextView) root.findViewById(R.id.TextViewValorDesafio);
        TextViewNotaDesafio = (TextView) root.findViewById(R.id.TextViewNotaDesafio);
        TextViewFechaDesafio = (TextView) root.findViewById(R.id.TextViewFechaDesafio);
        TextViewEstado = (TextView) root.findViewById(R.id.TextViewEstado);
        TextViewElegirRutina = (TextView) root.findViewById(R.id.TextViewElegirRutina);

        if (listaRutinasIniciadas.isEmpty()) {
            ImageViewImagenDesafio2.setVisibility(View.INVISIBLE);
            TextViewNombreDesafio.setVisibility(View.INVISIBLE);
            TextViewCategoriaDesafio.setVisibility(View.INVISIBLE);
            TextViewValorDesafio.setVisibility(View.INVISIBLE);
            TextViewNotaDesafio.setVisibility(View.INVISIBLE);
            TextViewFechaDesafio.setVisibility(View.INVISIBLE);
            TextViewEstado.setVisibility(View.INVISIBLE);
        }
    }

    public void actualizarDatos(String data) {

        if (data.equals("")) {

        } else {
            TextViewElegirRutina.setText("");
            //TextViewElegirRutina.setVisibility(View.INVISIBLE);

            ImageViewImagenDesafio2.setVisibility(View.VISIBLE);
            TextViewNombreDesafio.setVisibility(View.VISIBLE);
            TextViewCategoriaDesafio.setVisibility(View.VISIBLE);
            TextViewValorDesafio.setVisibility(View.VISIBLE);
            TextViewNotaDesafio.setVisibility(View.VISIBLE);
            TextViewFechaDesafio.setVisibility(View.VISIBLE);
            TextViewEstado.setVisibility(View.VISIBLE);

            ImageViewImagenDesafio2 .setImageResource(Integer.parseInt(data.split("-")[1]));
            TextViewNombreDesafio.setText(data.split("-")[2]);
            TextViewCategoriaDesafio.setText(data.split("-")[3]);
            TextViewValorDesafio.setText(data.split("-")[4]);
            TextViewNotaDesafio.setText(data.split("-")[5]);
            TextViewFechaDesafio.setText(data.split("-")[6]);
            TextViewEstado.setText(data.split("-")[7]);
        }
    }
}
