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
public class ListaRutinasFragment extends Fragment {

    private ListView ListViewRutinas;

    //Borrar despues
    int[] imagenes = {R.drawable.ic_directions_bike_black_48dp, R.drawable.ic_directions_bike_black_48dp, R.drawable.ic_directions_bike_black_48dp, R.drawable.ic_directions_bike_black_48dp};
    String[] nombres = {"Rutina 1", "Rutina 2", "Rutina 3", "Rutina 4"};
    String[] fechas = {"15/07/2015", "18/07/2015", "20/07/2015", "22/07/2015"};
    String[] exitos = {"No logrado", "Logrado", "No logrado", "No logrado"};

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
        View root = inflater.inflate(R.layout.fragment_lista_rutinas, container, false);

        ListViewRutinas = (ListView) root.findViewById(R.id.ListViewRutinas);

        AdapterHistorial adapter = new AdapterHistorial(getActivity().getApplicationContext(), imagenes, nombres, fechas, exitos);
        ListViewRutinas.setAdapter(adapter);

        Mensaje qwe = new Mensaje(getActivity().getApplicationContext(), "FragmentListRutinas creado");

        return root;
    }
}
