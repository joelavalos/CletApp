package com.example.joel.cletapp.tests;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.example.joel.cletapp.CRUDDatabase.DesafioCRUD;
import com.example.joel.cletapp.ClasesDataBase.Desafio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Joel on 28/01/2016.
 */
public class AC_DataBaseDesafio extends ApplicationTestCase<Application> {
    public AC_DataBaseDesafio() {
        super(Application.class);
    }

    @SmallTest
    public void test_AA_CrearDesafio() {
        DesafioCRUD desafioCRUD = new DesafioCRUD(getContext());
        Date parsedInicio = null;
        Date parsedFinal = null;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            parsedInicio = format.parse("01/01/2015");
            parsedFinal = format.parse("02/01/2015");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Desafio desafio = new Desafio(0,
                "Desafio prueba",
                "Nota prueba",
                new java.sql.Date(parsedInicio.getTime()),
                new java.sql.Date(parsedFinal.getTime()),
                'P',
                0,
                2,
                3,
                5400);

        desafio = desafioCRUD.insertarDesafio(desafio);
        assertNotSame(-1, desafio.getDesafioId());
    }

    @SmallTest
    public void test_AB_BuscarDesafio() {
        DesafioCRUD desafioCRUD = new DesafioCRUD(getContext());
        Desafio buscadoDesafio = new Desafio();
        try {
            buscadoDesafio = desafioCRUD.buscarDesafioPorId(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertNotSame(-1, buscadoDesafio.getDesafioId());
    }

    @SmallTest
    public void test_AC_EditarDesafio() {
        DesafioCRUD desafioCRUD = new DesafioCRUD(getContext());
        Desafio buscadoDesafio = new Desafio();

        try {
            buscadoDesafio = desafioCRUD.buscarDesafioPorId(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String nombreAntiguo = buscadoDesafio.getDesafioNombre();
        buscadoDesafio.setDesafioNombre("Nombre Editado");

        try {
            desafioCRUD.actualizarDatosDesafio(buscadoDesafio);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            buscadoDesafio = desafioCRUD.buscarDesafioPorId(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertNotSame(nombreAntiguo, buscadoDesafio.getDesafioNombre());
    }

    @SmallTest
    public void test_AD_ActualizarEstadoDesafio() {
        DesafioCRUD desafioCRUD = new DesafioCRUD(getContext());
        Desafio buscadoDesafio = new Desafio();

        try {
            buscadoDesafio = desafioCRUD.buscarDesafioPorId(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        buscadoDesafio.setEstadoDesafio('T');

        try {
            buscadoDesafio = desafioCRUD.actualizarDatosDesafio(buscadoDesafio);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            buscadoDesafio = desafioCRUD.buscarDesafioPorId(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertEquals('T', buscadoDesafio.getEstadoDesafio());
    }

    @SmallTest
    public void test_AE_ActualizarExitoDesafio() {
        DesafioCRUD desafioCRUD = new DesafioCRUD(getContext());
        Desafio buscadoDesafio = new Desafio();

        try {
            buscadoDesafio = desafioCRUD.buscarDesafioPorId(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        buscadoDesafio.setExitoDesafio(1);

        try {
            buscadoDesafio = desafioCRUD.actualizarDatosDesafio(buscadoDesafio);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            buscadoDesafio = desafioCRUD.buscarDesafioPorId(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertEquals(1, buscadoDesafio.getExitoDesafio());
    }

    @SmallTest
    public void test_AF_BuscarTodosLosDesafiosterminados() {
        DesafioCRUD desafioCRUD = new DesafioCRUD(getContext());
        List<Desafio> listaDesafios = new ArrayList<>();

        listaDesafios = desafioCRUD.buscarTodosLosDesafios();

        assertNotSame(0, listaDesafios.size());
    }

    @SmallTest
    public void test_AG_BuscarTodosLosDesafiosLogrados() {
        DesafioCRUD desafioCRUD = new DesafioCRUD(getContext());
        List<Desafio> listaDesafios = new ArrayList<>();

        listaDesafios = desafioCRUD.buscarTodosLosDesafiosTerminadosLogrados();

        assertNotSame(0, listaDesafios.size());
    }

    @SmallTest
    public void test_AG_BuscarTodosLosDesafiosNoLogrados() {
        DesafioCRUD desafioCRUD = new DesafioCRUD(getContext());
        List<Desafio> listaDesafios = new ArrayList<>();

        listaDesafios = desafioCRUD.buscarTodosLosDesafiosTerminadosNoLogrados();

        assertEquals(0, listaDesafios.size());
    }
}
