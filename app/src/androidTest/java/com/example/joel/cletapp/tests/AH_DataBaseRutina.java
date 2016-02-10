package com.example.joel.cletapp.tests;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.example.joel.cletapp.CRUDDatabase.DesafioCRUD;
import com.example.joel.cletapp.CRUDDatabase.DesafioRutinaCRUD;
import com.example.joel.cletapp.CRUDDatabase.ResumenCRUD;
import com.example.joel.cletapp.CRUDDatabase.RutinaCRUD;
import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.ClasesDataBase.DesafioRutina;
import com.example.joel.cletapp.ClasesDataBase.Resumen;
import com.example.joel.cletapp.ClasesDataBase.Rutina;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Joel on 28/01/2016.
 */
public class AH_DataBaseRutina extends ApplicationTestCase<Application> {
    public AH_DataBaseRutina() {
        super(Application.class);
    }

    @SmallTest
    public void test_AA_CrearRutina() {
        Date parsedFechaDesafio = null;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        RutinaCRUD rutinaCRUD = new RutinaCRUD(getContext());

        try {
            parsedFechaDesafio = format.parse("01/01/2015");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ResumenCRUD resumenCRUD = new ResumenCRUD(getContext());
        Resumen newResumen = new Resumen(0, "Resumen defecto", new java.sql.Date(parsedFechaDesafio.getTime()));
        newResumen = resumenCRUD.insertarResumen(newResumen);

        //Insertar rutina
        Date parsedInicio = null;
        Date parsedFinal = null;
        try {
            parsedInicio = format.parse("01/01/2015");
            parsedFinal = format.parse("02/01/2015");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Rutina newRutina = new Rutina(0, "Rutina defecto", "Nota defecto", new java.sql.Date(parsedInicio.getTime()), new java.sql.Date(parsedFinal.getTime()), 'T', newResumen);
        newRutina = rutinaCRUD.insertarRutina(newRutina);

        assertNotSame(-1, newRutina.getRutinaId());
    }

    @SmallTest
    public void test_AB_AsignarResumenRutina() {
        Date parsedFechaDesafio = null;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        RutinaCRUD rutinaCRUD = new RutinaCRUD(getContext());

        try {
            parsedFechaDesafio = format.parse("01/01/2015");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ResumenCRUD resumenCRUD = new ResumenCRUD(getContext());
        Resumen newResumen = new Resumen(0, "Resumen defecto", new java.sql.Date(parsedFechaDesafio.getTime()));
        newResumen = resumenCRUD.insertarResumen(newResumen);

        //Insertar rutina
        Date parsedInicio = null;
        Date parsedFinal = null;
        try {
            parsedInicio = format.parse("01/01/2015");
            parsedFinal = format.parse("02/01/2015");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Rutina newRutina = new Rutina(0, "Rutina defecto 3", "Nota defecto", new java.sql.Date(parsedInicio.getTime()), new java.sql.Date(parsedFinal.getTime()), 'P', newResumen);
        newRutina = rutinaCRUD.insertarRutina(newRutina);

        assertEquals(newResumen.getResumenId(), newRutina.getResumen().getResumenId());
    }

    @SmallTest
    public void test_AC_AsignarDesafiosRutina() {
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

        Date parsedFechaDesafio = null;
        RutinaCRUD rutinaCRUD = new RutinaCRUD(getContext());

        try {
            parsedFechaDesafio = format.parse("01/01/2015");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ResumenCRUD resumenCRUD = new ResumenCRUD(getContext());
        Resumen newResumen = new Resumen(0, "Resumen defecto", new java.sql.Date(parsedFechaDesafio.getTime()));
        newResumen = resumenCRUD.insertarResumen(newResumen);

        //Insertar rutina
        parsedInicio = null;
        parsedFinal = null;
        try {
            parsedInicio = format.parse("01/01/2015");
            parsedFinal = format.parse("02/01/2015");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Rutina newRutina = new Rutina(0, "Rutina defecto 2", "Nota defecto", new java.sql.Date(parsedInicio.getTime()), new java.sql.Date(parsedFinal.getTime()), 'I', newResumen);
        newRutina = rutinaCRUD.insertarRutina(newRutina);

        //Insertar desafioRutina
        DesafioRutinaCRUD desafioRutinaCRUD = new DesafioRutinaCRUD(getContext());

        DesafioRutina newDesafioRutina = new DesafioRutina(0, newRutina, desafio, new java.sql.Date(parsedInicio.getTime()));
        newDesafioRutina = desafioRutinaCRUD.insertarDesafioRutina(newDesafioRutina);

        assertEquals(desafio.getDesafioId(), newDesafioRutina.getDesafio().getDesafioId());
    }

    @SmallTest
    public void test_AD_BuscarRutinaPorId() {
        RutinaCRUD rutinaCRUD = new RutinaCRUD(getContext());
        Rutina newRutina = new Rutina();

        try {
            newRutina = rutinaCRUD.buscarRutinaPorId(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertEquals("Rutina defecto", newRutina.getRutinaNombre());
    }

    @SmallTest
    public void test_AE_BuscarUltimaRutinaTerminada() {
        RutinaCRUD rutinaCRUD = new RutinaCRUD(getContext());
        List<Rutina> listaRutinas = new ArrayList<>();

        try {
            listaRutinas = rutinaCRUD.buscarUltimaRutina();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertEquals("Rutina defecto", listaRutinas.get(0).getRutinaNombre());
    }

    @SmallTest
    public void test_AF_BuscarTodasLasRutinas() {
        RutinaCRUD rutinaCRUD = new RutinaCRUD(getContext());

        List<Rutina> listaRutinas = new ArrayList<>();
        listaRutinas = rutinaCRUD.buscarTodasLasRutinas();
        assertEquals(3, listaRutinas.size());
    }

    @SmallTest
    public void test_AG_BuscarTodasLasRutinasPendientes() {
        RutinaCRUD rutinaCRUD = new RutinaCRUD(getContext());

        List<Rutina> listaRutinas = new ArrayList<>();
        listaRutinas = rutinaCRUD.buscarTodasLasRutinasPendientes();

        assertEquals(1, listaRutinas.size());
    }

    @SmallTest
    public void test_AH_BuscarTodasLasRutinasTerminadas() {
        RutinaCRUD rutinaCRUD = new RutinaCRUD(getContext());

        List<Rutina> listaRutinas = new ArrayList<>();
        listaRutinas = rutinaCRUD.buscarTodasLasRutinasTerminadas();

        assertEquals(1, listaRutinas.size());
    }

    @SmallTest
    public void test_AI_BuscarTodasLasRutinasIniciadas() {
        RutinaCRUD rutinaCRUD = new RutinaCRUD(getContext());

        List<Rutina> listaRutinas = new ArrayList<>();
        listaRutinas = rutinaCRUD.buscarTodasLasRutinasIniciadas();

        assertEquals(1, listaRutinas.size());
    }

    @SmallTest
    public void test_AJ_CambiarEstadoRutina() {
        RutinaCRUD rutinaCRUD = new RutinaCRUD(getContext());
        Rutina buscadoRutina = new Rutina();

        try {
            buscadoRutina = rutinaCRUD.buscarRutinaPorId(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        char estadoAntiguo = buscadoRutina.getRutinaEstado();
        buscadoRutina.setRutinaEstado('I');

        try {
            buscadoRutina = rutinaCRUD.actualizarRutina(buscadoRutina);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            buscadoRutina = rutinaCRUD.buscarRutinaPorId(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertNotSame(estadoAntiguo, buscadoRutina.getRutinaEstado());
    }
}
