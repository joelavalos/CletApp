package com.example.joel.cletapp.tests;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.example.joel.cletapp.CRUDDatabase.RepeticionesCRUD;
import com.example.joel.cletapp.CRUDDatabase.RutaCRUD;
import com.example.joel.cletapp.CRUDDatabase.SerieCRUD;
import com.example.joel.cletapp.ClasesDataBase.Repeticiones;
import com.example.joel.cletapp.ClasesDataBase.Ruta;
import com.example.joel.cletapp.ClasesDataBase.Serie;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 28/01/2016.
 */
public class AK_DataBaseRepeticiones extends ApplicationTestCase<Application> {
    public AK_DataBaseRepeticiones() {
        super(Application.class);
    }

    @SmallTest
    public void test_AA_CrearRepeticion(){
        SerieCRUD serieCRUD = new SerieCRUD(getContext());
        RepeticionesCRUD repeticionesCRUD = new RepeticionesCRUD(getContext());

        Serie buscadoSerie = new Serie();

        try {
            buscadoSerie = serieCRUD.buscarSeriePorId(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Repeticiones addRepeticiones = new Repeticiones(0, buscadoSerie, 0);
        addRepeticiones.setValor(60000);
        addRepeticiones = repeticionesCRUD.insertarRepeticion(addRepeticiones);

        assertNotSame(-1, addRepeticiones.getRepeticionId());
    }

    @SmallTest
    public void test_AB_AsignarRepeticionSerie(){
        SerieCRUD serieCRUD = new SerieCRUD(getContext());
        RepeticionesCRUD repeticionesCRUD = new RepeticionesCRUD(getContext());

        Serie buscadoSerie = new Serie();

        List<Repeticiones> listaRepeticiones = new ArrayList<>();

        try {
            buscadoSerie = serieCRUD.buscarSeriePorId(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            listaRepeticiones = repeticionesCRUD.buscarRepeticionesPorIdSerie(buscadoSerie);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertNotSame(0, listaRepeticiones.size());
    }

    @SmallTest
    public void test_AC_BuscarTodasLasRepeticiones(){
        SerieCRUD serieCRUD = new SerieCRUD(getContext());
        RepeticionesCRUD repeticionesCRUD = new RepeticionesCRUD(getContext());

        Serie buscadoSerie = new Serie();

        List<Repeticiones> listaRepeticiones = new ArrayList<>();

        try {
            buscadoSerie = serieCRUD.buscarSeriePorId(1);
            listaRepeticiones = repeticionesCRUD.buscarRepeticionesPorIdSerie(buscadoSerie);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertNotSame(0, listaRepeticiones.size());
    }
}
