package com.example.joel.cletapp.tests;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.example.joel.cletapp.CRUDDatabase.DesafioCRUD;
import com.example.joel.cletapp.CRUDDatabase.DesafioRutinaCRUD;
import com.example.joel.cletapp.CRUDDatabase.RutinaCRUD;
import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.ClasesDataBase.DesafioRutina;
import com.example.joel.cletapp.ClasesDataBase.Rutina;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Joel on 28/01/2016.
 */
public class AI_DataBaseDesafioRutina extends ApplicationTestCase<Application> {
    public AI_DataBaseDesafioRutina() {
        super(Application.class);
    }

    @SmallTest
    public void test_AA_CrearDesafiosRutina() {
        DesafioCRUD desafioCRUD = new DesafioCRUD(getContext());
        RutinaCRUD rutinaCRUD = new RutinaCRUD(getContext());

        Desafio buscadoDesafio = new Desafio();
        Rutina buscadoRutina = new Rutina();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date parsedInicio = null;
        Date parsedFinal = null;
        try {
            parsedInicio = format.parse("01/01/2015");
            parsedFinal = format.parse("02/01/2015");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            buscadoRutina = rutinaCRUD.buscarRutinaPorId(1);
            buscadoDesafio = desafioCRUD.buscarDesafioPorId(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Insertar desafioRutina
        DesafioRutinaCRUD desafioRutinaCRUD = new DesafioRutinaCRUD(getContext());

        DesafioRutina newDesafioRutina = new DesafioRutina(0, buscadoRutina, buscadoDesafio, new java.sql.Date(parsedInicio.getTime()));
        newDesafioRutina = desafioRutinaCRUD.insertarDesafioRutina(newDesafioRutina);

        assertEquals(buscadoDesafio.getDesafioId(), newDesafioRutina.getDesafio().getDesafioId());
    }

    @SmallTest
    public void test_AB_BuscarDesafiosDeUnaRutinaPorIdRutina() {
        RutinaCRUD rutinaCRUD = new RutinaCRUD(getContext());
        DesafioRutinaCRUD desafioRutinaCRUD = new DesafioRutinaCRUD(getContext());

        Desafio buscadoDesafio = new Desafio();
        Rutina buscadoRutina = new Rutina();

        try {
            buscadoRutina = rutinaCRUD.buscarRutinaPorId(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<DesafioRutina> listaDesafiosRutina = new ArrayList<>();

        try {
            listaDesafiosRutina = desafioRutinaCRUD.buscarDesafioRutinaPorIdRutina(buscadoRutina);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertNotSame(0, listaDesafiosRutina.size());
    }

    @SmallTest
    public void test_AC_OrdenarDesafiosRutinaPorFecha() {
        RutinaCRUD rutinaCRUD = new RutinaCRUD(getContext());
        DesafioRutinaCRUD desafioRutinaCRUD = new DesafioRutinaCRUD(getContext());

        Desafio buscadoDesafio = new Desafio();
        Rutina buscadoRutina = new Rutina();

        try {
            buscadoRutina = rutinaCRUD.buscarRutinaPorId(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<DesafioRutina> listaDesafiosRutina = new ArrayList<>();

        try {
            listaDesafiosRutina = desafioRutinaCRUD.buscarDesafioRutinaPorIdRutinaOrderByFecha(buscadoRutina);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertNotSame(0, listaDesafiosRutina.size());
    }
}
