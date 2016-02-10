package com.example.joel.cletapp.tests;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.example.joel.cletapp.CRUDDatabase.ResumenCRUD;
import com.example.joel.cletapp.ClasesDataBase.Resumen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Joel on 28/01/2016.
 */
public class AG_DataBaseResumen extends ApplicationTestCase<Application> {
    public AG_DataBaseResumen() {
        super(Application.class);
    }

    @SmallTest
    public void test_AA_CrearResumen(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date parsedFechaDesafio = null;
        try {
            parsedFechaDesafio = format.parse("01/01/2015");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ResumenCRUD resumenCRUD = new ResumenCRUD(getContext());
        Resumen newResumen = new Resumen(0, "Resumen defecto", new java.sql.Date(parsedFechaDesafio.getTime()));
        newResumen = resumenCRUD.insertarResumen(newResumen);

        assertNotSame(-1, newResumen.getResumenId());
    }

    @SmallTest
    public void test_AB_BuscarResumen(){
        ResumenCRUD resumenCRUD = new ResumenCRUD(getContext());
        Resumen buscadoResumen = new Resumen();

        try {
            buscadoResumen = resumenCRUD.buscarResumenPorId(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertNotSame(-1, buscadoResumen.getResumenId());
    }

}
