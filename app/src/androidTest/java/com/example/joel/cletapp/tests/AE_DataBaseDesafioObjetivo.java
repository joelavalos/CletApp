package com.example.joel.cletapp.tests;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.example.joel.cletapp.CRUDDatabase.DesafioCRUD;
import com.example.joel.cletapp.CRUDDatabase.DesafioObjetivoCRUD;
import com.example.joel.cletapp.CRUDDatabase.ObjetivoCRUD;
import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.ClasesDataBase.DesafioObjetivo;
import com.example.joel.cletapp.ClasesDataBase.Objetivo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Joel on 28/01/2016.
 */
public class AE_DataBaseDesafioObjetivo extends ApplicationTestCase<Application> {
    public AE_DataBaseDesafioObjetivo() {
        super(Application.class);
    }

    @SmallTest
    public void test_AA_CrearDesafioObjetivo(){
        DesafioObjetivoCRUD desafioObjetivoCRUD = new DesafioObjetivoCRUD(getContext());
        DesafioCRUD desafioCRUD = new DesafioCRUD(getContext());
        ObjetivoCRUD objetivoCRUD = new ObjetivoCRUD(getContext());

        Desafio desafioBuscado = new Desafio();
        Objetivo objetivoBuscado = new Objetivo();
        try {
            desafioBuscado = desafioCRUD.buscarDesafioPorId(1);
            objetivoBuscado = objetivoCRUD.buscarObjetivoPorNombre("Distancia");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DesafioObjetivo desafioObjetivo = new DesafioObjetivo(0, desafioBuscado, objetivoBuscado, 60000);
        desafioObjetivo = desafioObjetivoCRUD.insertarDesafioObjetivo(desafioObjetivo);

        assertNotSame(-1, desafioObjetivo.getDesObjId());
    }

    @SmallTest
    public void test_AB_AsignarObjetvo_A_Desafio(){
        DesafioObjetivoCRUD desafioObjetivoCRUD = new DesafioObjetivoCRUD(getContext());
        DesafioCRUD desafioCRUD = new DesafioCRUD(getContext());
        ObjetivoCRUD objetivoCRUD = new ObjetivoCRUD(getContext());

        Desafio desafioBuscado = new Desafio();
        Objetivo objetivoBuscado = new Objetivo();
        try {
            desafioBuscado = desafioCRUD.buscarDesafioPorId(1);
            objetivoBuscado = objetivoCRUD.buscarObjetivoPorNombre("Distancia");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DesafioObjetivo desafioObjetivo = new DesafioObjetivo(0, desafioBuscado, objetivoBuscado, 60000);
        desafioObjetivoCRUD.insertarDesafioObjetivo(desafioObjetivo);

        assertNotSame(-1, desafioObjetivo.getDesafio().getDesafioId());
        assertNotSame(-1, desafioObjetivo.getObjetivo().getObjetivoId());
    }

    @SmallTest
    public void test_AC_BuscarDesafioObjetivoPorDesafio(){
        DesafioObjetivoCRUD desafioObjetivoCRUD = new DesafioObjetivoCRUD(getContext());
        DesafioCRUD desafioCRUD = new DesafioCRUD(getContext());

        Desafio desafioBuscado = new Desafio();
        try {
            desafioBuscado = desafioCRUD.buscarDesafioPorId(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DesafioObjetivo desafioObjetivo = new DesafioObjetivo();

        try {
            desafioObjetivo = desafioObjetivoCRUD.buscarDesafioObjetivoPorIdDesafio(desafioBuscado);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertNotSame(-1, desafioObjetivo.getDesObjId());
    }

    @SmallTest
    public void test_AD_BuscarTodosLosDesafioObjetivo(){
        DesafioObjetivoCRUD desafioObjetivoCRUD = new DesafioObjetivoCRUD(getContext());
        DesafioCRUD desafioCRUD = new DesafioCRUD(getContext());
        ObjetivoCRUD objetivoCRUD = new ObjetivoCRUD(getContext());

        List<DesafioObjetivo> listaDesafiosObjetivos = new ArrayList<>();
        listaDesafiosObjetivos = desafioObjetivoCRUD.buscarTodosLosDesafioObjetivos();

        assertNotSame(0, listaDesafiosObjetivos.size());
    }

    @SmallTest
    public void test_AE_ModificarDesafioObjetivo(){
        DesafioObjetivoCRUD desafioObjetivoCRUD = new DesafioObjetivoCRUD(getContext());
        DesafioCRUD desafioCRUD = new DesafioCRUD(getContext());
        ObjetivoCRUD objetivoCRUD = new ObjetivoCRUD(getContext());

        Desafio desafioBuscado = new Desafio();
        try {
            desafioBuscado = desafioCRUD.buscarDesafioPorId(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DesafioObjetivo desafioObjetivoBuscado = new DesafioObjetivo();

        try {
            desafioObjetivoBuscado = desafioObjetivoCRUD.buscarDesafioObjetivoPorIdDesafio(desafioBuscado);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        float valorAntiguo = desafioObjetivoBuscado.getValor();
        desafioObjetivoBuscado.setValor(100000);

        try {
            desafioObjetivoBuscado = desafioObjetivoCRUD.actualizarDatosDesafioObjetivo(desafioObjetivoBuscado);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertNotSame(valorAntiguo, desafioObjetivoBuscado.getValor());
    }
}
