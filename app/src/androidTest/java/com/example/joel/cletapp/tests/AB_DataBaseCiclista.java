package com.example.joel.cletapp.tests;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.ListView;

import com.example.joel.cletapp.CRUDDatabase.CiclistaCRUD;
import com.example.joel.cletapp.ClasesDataBase.Ciclista;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Joel on 28/01/2016.
 */
public class AB_DataBaseCiclista extends ApplicationTestCase<Application> {
    public AB_DataBaseCiclista() {
        super(Application.class);
    }

    @SmallTest
    public void test_AA_CrearCiclista() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date parsed = null;
        try {
            parsed = format.parse("01/01/1990");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        CiclistaCRUD ciclistaCRUD = new CiclistaCRUD(getContext());
        Ciclista ciclista = new Ciclista();
        ciclista = new Ciclista("0", "Joel", "Avalos", new java.sql.Date(parsed.getTime()), Float.parseFloat("95"), Float.parseFloat("195"), "M".charAt(0));
        long id =  ciclistaCRUD.insertarPerfil(ciclista);
        assertNotSame(-1, id);
    }

    @SmallTest
    public void test_AB_BuscarCiclista() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date parsed = null;
        try {
            parsed = format.parse("01/01/1990");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        CiclistaCRUD ciclistaCRUD = new CiclistaCRUD(getContext());
        Ciclista ciclista = new Ciclista();
        ciclista = new Ciclista("0", "Joel", "Avalos", new java.sql.Date(parsed.getTime()), Float.parseFloat("95"), Float.parseFloat("195"), "M".charAt(0));
        long id =  ciclistaCRUD.insertarPerfil(ciclista);
        try {
            ciclista = ciclistaCRUD.buscarCiclistaPorRut("0");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertEquals("0", ciclista.getCiclistRut());
    }

    @SmallTest
    public void test_AC_ModificarCiclista() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date parsed = null;
        try {
            parsed = format.parse("01/01/1990");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        CiclistaCRUD ciclistaCRUD = new CiclistaCRUD(getContext());
        Ciclista ciclista = new Ciclista();
        ciclista = new Ciclista("0", "Joel", "Avalos", new java.sql.Date(parsed.getTime()), Float.parseFloat("95"), Float.parseFloat("195"), "M".charAt(0));
        long id =  ciclistaCRUD.insertarPerfil(ciclista);
        try {
            ciclista = ciclistaCRUD.buscarCiclistaPorRut("0");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ciclista.setCiclistaNombre("JoelEditado");
        ciclistaCRUD.actualizarDatosCiclista(ciclista);

        try {
            ciclista = ciclistaCRUD.buscarCiclistaPorRut("0");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertNotSame("Joel", ciclista.getCiclistaNombre());
    }
}
