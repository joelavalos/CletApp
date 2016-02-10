package com.example.joel.cletapp.tests;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.example.joel.cletapp.CRUDDatabase.RutaCRUD;
import com.example.joel.cletapp.ClasesDataBase.Ruta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 28/01/2016.
 */
public class AF_DataBaseRuta extends ApplicationTestCase<Application> {
    public AF_DataBaseRuta() {
        super(Application.class);
    }

    @SmallTest
    public void test_AA_CrearRuta() {
        RutaCRUD rutaCRUD = new RutaCRUD(getContext());
        Ruta nuevaRuta = new Ruta();
        nuevaRuta.setRutaNombre("Ruta larga");
        nuevaRuta.setRutaCordenadas("-33.54179451052759=-70.55631310000001X-33.54192417381435=-70.55499613537368X-33.53943371046656=-70.55632114662704");
        nuevaRuta = rutaCRUD.insertarRuta(nuevaRuta);
        assertNotSame(-1, nuevaRuta.getRutaId());
    }

    @SmallTest
    public void test_AB_BuscarTodasLasRutas() {
        RutaCRUD rutaCRUD = new RutaCRUD(getContext());

        List<Ruta> todasLasRutas = new ArrayList<>();
        todasLasRutas = rutaCRUD.buscarTodasLasRutas();
        assertNotSame(0, todasLasRutas.size());
    }
}
