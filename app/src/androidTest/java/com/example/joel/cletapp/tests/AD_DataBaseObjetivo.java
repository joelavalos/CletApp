package com.example.joel.cletapp.tests;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.example.joel.cletapp.CRUDDatabase.ObjetivoCRUD;
import com.example.joel.cletapp.ClasesDataBase.Objetivo;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 28/01/2016.
 */
public class AD_DataBaseObjetivo extends ApplicationTestCase<Application> {
    public AD_DataBaseObjetivo() {
        super(Application.class);
    }

    @SmallTest
    public void test_AA_CrearObjetivo(){
        ObjetivoCRUD objetivoCRUD = new ObjetivoCRUD(getContext());
        Objetivo objetivo = new Objetivo(0, "Distancia", "Distancia recorrida");
        long id = objetivoCRUD.insertarObjetivo(objetivo);
        assertNotSame(-1, id);
    }

    @SmallTest
    public void test_AB_BuscarTodosLosObjetivos(){
        ObjetivoCRUD objetivoCRUD = new ObjetivoCRUD(getContext());

        List<Objetivo> listObjetivo = new ArrayList<>();
        listObjetivo = objetivoCRUD.buscarTodosLosObjetivos();
        assertNotSame(0, listObjetivo.size());
    }

    @SmallTest
    public void test_AC_BuscarObjetivo(){
        ObjetivoCRUD objetivoCRUD = new ObjetivoCRUD(getContext());

        Objetivo objetivoBuscado = new Objetivo();
        try {
            objetivoBuscado = objetivoCRUD.buscarObjetivoPorNombre("Distancia");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertNotSame(-1, objetivoBuscado.getObjetivoId());
    }
}
