package com.example.joel.cletapp.tests;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.example.joel.cletapp.CRUDDatabase.DesafioCRUD;
import com.example.joel.cletapp.CRUDDatabase.SerieCRUD;
import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.ClasesDataBase.Serie;

import junit.framework.Assert;

import java.text.ParseException;

/**
 * Created by Joel on 28/01/2016.
 */
public class AJ_DataBaseSerie extends ApplicationTestCase<Application> {
    public AJ_DataBaseSerie() {
        super(Application.class);
    }

    @SmallTest
    public void test_AA_CrearSerie(){
        DesafioCRUD desafioCRUD = new DesafioCRUD(getContext());
        SerieCRUD serieCRUD = new SerieCRUD(getContext());
        Desafio desafioBuscado = new Desafio();

        try {
            desafioBuscado = desafioCRUD.buscarDesafioPorId(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Serie serie = new Serie(0, desafioBuscado);
        serie = serieCRUD.insertarSerie(serie);

        assertNotSame(-1, serie.getSerieId());
    }

    @SmallTest
    public void test_AB_AsignarSerieDesafio(){
        DesafioCRUD desafioCRUD = new DesafioCRUD(getContext());
        SerieCRUD serieCRUD = new SerieCRUD(getContext());
        Desafio desafioBuscado = new Desafio();
        Serie serie = new Serie();

        try {
            desafioBuscado = desafioCRUD.buscarDesafioPorId(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            serie = serieCRUD.buscarSeriePorId(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertEquals(desafioBuscado.getDesafioId(), serie.getDesafio().getDesafioId());
    }
}
