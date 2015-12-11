package com.example.joel.cletapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.joel.cletapp.AdapterHistorial;
import com.example.joel.cletapp.Mensaje;
import com.example.joel.cletapp.R;

/**
 * Created by Joel on 21/07/2015.
 */
public class ListaDesafiosFragment extends Fragment {

    private ListView ListViewDesafios;

    //Borrar despues
    int[] imagenes = {R.drawable.ic_grade_black_48dp, R.drawable.ic_grade_black_48dp, R.drawable.ic_grade_black_48dp, R.drawable.ic_grade_black_48dp, R.drawable.ic_grade_black_48dp, R.drawable.ic_grade_black_48dp};
    String[] nombres = {"Desafio 1", "Desafio 2", "Desafio 3", "Desafio 4", "Desafio 5", "Desafio 6"};
    String[] fechas = {"15/07/2015", "18/07/2015", "20/07/2015", "22/07/2015", "27/07/2015", "30/07/2015"};
    String[] exitos = {"No logrado", "Logrado", "No logrado", "Logrado", "Logrado", "Logrado"};

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
        View root = inflater.inflate(R.layout.fragment_lista_desafios, container, false);

        ListViewDesafios = (ListView) root.findViewById(R.id.ListViewDesafios);

        AdapterHistorial adapter = new AdapterHistorial(getActivity().getApplicationContext(), imagenes, nombres, fechas, exitos);
        ListViewDesafios.setAdapter(adapter);

        Mensaje qwe = new Mensaje(getActivity().getApplicationContext(), "FragmentListDesafios creado");

        return root;
    }
}
