package com.example.joel.cletapp.CRUDDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.Helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 27/07/2015.
 */
public class DesafioCRUD {

    public static final String TAG = "DesafioCRUD";

    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private SQLiteDatabase mDatabase;
    private Helper mDbHelper;
    private Context mContext;

    private String[] mAllColumns = {
            Helper.DESAFIO_ID,
            Helper.DESAFIO_NOMBRE,
            Helper.DESAFIO_DESCRIPCON,
            Helper.DESAFIO_INICIO,
            Helper.DESAFIO_TERMINO,
            Helper.DESAFIO_ESTADO,
            Helper.DESAFIO_EXITO,
            Helper.DESAFIO_SERIES,
            Helper.DESAFIO_REPETICIONES};

    public DesafioCRUD(Context context) {
        this.mContext = context;
        mDbHelper = new Helper(context);
        // open the database
        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
        mDatabase.execSQL("PRAGMA foreign_keys=ON;");
    }

    public void close() {
        mDbHelper.close();
    }

    public Desafio insertarDesafio(Desafio desafio) {

        ContentValues contentValues = new ContentValues();
        //contentValues.put(Helper.DESAFIO_ID, desafio.getDesafioId());
        contentValues.put(Helper.DESAFIO_NOMBRE, desafio.getDesafioNombre());
        contentValues.put(Helper.DESAFIO_DESCRIPCON, desafio.getDesafioDescripcion());
        contentValues.put(Helper.DESAFIO_INICIO, df.format(desafio.getInicioDesafio()));
        contentValues.put(Helper.DESAFIO_TERMINO, df.format(desafio.getTerminoDesafio()));
        contentValues.put(Helper.DESAFIO_ESTADO, String.valueOf(desafio.getEstadoDesafio()));
        contentValues.put(Helper.DESAFIO_EXITO, desafio.getExitoDesafio());
        contentValues.put(Helper.DESAFIO_SERIES, desafio.getSeries());
        contentValues.put(Helper.DESAFIO_REPETICIONES, desafio.getRepeticiones());

        long id = mDatabase.insert(mDbHelper.TABLA_DESAFIO, null, contentValues);

        Cursor cursor = mDatabase.query(Helper.TABLA_DESAFIO, mAllColumns, Helper.DESAFIO_ID + " ='" + id + "'", null, null, null, null);

        Desafio newDesafio = new Desafio();

        while (cursor.moveToNext()) {

            try {
                newDesafio = cursorToDesafio(cursor);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return newDesafio;
    }

    public int eliminarDesafio(Desafio desafio) {
        int id = mDatabase.delete(Helper.TABLA_DESAFIO, Helper.DESAFIO_ID + " ='" + desafio.getDesafioId() + "'", null);
        return id;
    }

    public Desafio buscarDesafioPorId(long ID) throws ParseException {
        //String[] columns = {Helper.DESAFIO_ID, Helper.DESAFIO_NOMBRE, Helper.DESAFIO_DESCRIPCON, Helper.DESAFIO_INICIO, Helper.DESAFIO_TERMINO, Helper.DESAFIO_ESTADO, Helper.DESAFIO_EXITO};
        Cursor cursor = mDatabase.query(Helper.TABLA_DESAFIO, mAllColumns, Helper.DESAFIO_ID + " ='" + ID + "'", null, null, null, null);
        List<String> stringBuffer = new ArrayList<String>();

        Desafio desafio = new Desafio();

        while (cursor.moveToNext()) {
            desafio = cursorToDesafio(cursor);
        }

        cursor.close();

        return desafio;
    }

    public Desafio actualizarDatosDesafio(Desafio desafio) throws ParseException {
        ContentValues newValues = new ContentValues();

        newValues.put(Helper.DESAFIO_NOMBRE, desafio.getDesafioNombre());
        newValues.put(Helper.DESAFIO_DESCRIPCON, desafio.getDesafioDescripcion());
        newValues.put(Helper.DESAFIO_INICIO, df.format(desafio.getInicioDesafio()));
        newValues.put(Helper.DESAFIO_TERMINO, df.format(desafio.getTerminoDesafio()));
        newValues.put(Helper.DESAFIO_ESTADO, String.valueOf(desafio.getEstadoDesafio()));
        newValues.put(Helper.DESAFIO_EXITO, Boolean.parseBoolean(String.valueOf(desafio.getExitoDesafio())));
        newValues.put(Helper.DESAFIO_SERIES, desafio.getSeries());
        newValues.put(Helper.DESAFIO_EXITO, desafio.getRepeticiones());

        String[] whereArgs = {String.valueOf(desafio.getDesafioId())};

        int count = mDatabase.update(Helper.TABLA_DESAFIO, newValues, Helper.DESAFIO_ID + " =?", whereArgs);

        Cursor cursor = mDatabase.query(Helper.TABLA_DESAFIO, mAllColumns, Helper.DESAFIO_ID + " ='" + desafio.getDesafioId() + "'", null, null, null, null);
        Desafio desafioReturn = new Desafio();

        while (cursor.moveToNext()) {
            desafioReturn = cursorToDesafio(cursor);
        }
        cursor.close();

        return desafioReturn;
    }

    public List<Desafio> buscarTodosLosDesafiosPendientes() {
        List<Desafio> listDesafios = new ArrayList<Desafio>();
        Cursor cursor = mDatabase.query(Helper.TABLA_DESAFIO, mAllColumns, Helper.DESAFIO_ESTADO + " ='" + "P" + "'", null, null, null, null);

        while (cursor.moveToNext()) {
            try {
                Desafio desafio = cursorToDesafio(cursor);
                listDesafios.add(desafio);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return listDesafios;
    }

    public List<Desafio> buscarTodosLosDesafiosTerminados() {
        List<Desafio> listDesafios = new ArrayList<Desafio>();
        Cursor cursor = mDatabase.query(Helper.TABLA_DESAFIO, mAllColumns, Helper.DESAFIO_ESTADO + " ='" + "T" + "'", null, null, null, null);

        while (cursor.moveToNext()) {
            try {
                Desafio desafio = cursorToDesafio(cursor);
                listDesafios.add(desafio);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return listDesafios;
    }

    public List<Desafio> buscarTodosLosDesafiosTerminadosLogrados() {
        List<Desafio> listDesafios = new ArrayList<Desafio>();
        String[] condiciones = {"T", "1"};
        Cursor cursor = mDatabase.query(Helper.TABLA_DESAFIO, mAllColumns, Helper.DESAFIO_ESTADO + " =? AND " + Helper.DESAFIO_EXITO + " =?", condiciones, null, null, null, null);

        while (cursor.moveToNext()) {
            try {
                Desafio desafio = cursorToDesafio(cursor);
                listDesafios.add(desafio);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return listDesafios;
    }

    public List<Desafio> buscarTodosLosDesafiosTerminadosNoLogrados() {
        List<Desafio> listDesafios = new ArrayList<Desafio>();
        String[] condiciones = {"T", "0"};
        Cursor cursor = mDatabase.query(Helper.TABLA_DESAFIO, mAllColumns, Helper.DESAFIO_ESTADO + " =? AND " + Helper.DESAFIO_EXITO + " =?", condiciones, null, null, null, null);

        while (cursor.moveToNext()) {
            try {
                Desafio desafio = cursorToDesafio(cursor);
                listDesafios.add(desafio);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return listDesafios;
    }

    public List<Desafio> buscarTodosLosDesafios() {
        List<Desafio> listDesafios = new ArrayList<Desafio>();
        String[] condiciones = {"T", "0"};
        Cursor cursor = mDatabase.query(Helper.TABLA_DESAFIO, mAllColumns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            try {
                Desafio desafio = cursorToDesafio(cursor);
                listDesafios.add(desafio);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return listDesafios;
    }

    /*public int actualizarDatosDesafio(Desafio desafio) {
        ContentValues newValues = new ContentValues();

        newValues.put(Helper.DESAFIO_NOMBRE, desafio.getDesafioNombre());
        newValues.put(Helper.DESAFIO_DESCRIPCON, desafio.getDesafioDescripcion());
        newValues.put(Helper.DESAFIO_INICIO, df.format(desafio.getInicioDesafio()));
        newValues.put(Helper.DESAFIO_TERMINO, df.format(desafio.getTerminoDesafio()));
        newValues.put(Helper.DESAFIO_ESTADO, String.valueOf(desafio.getEstadoDesafio()));
        newValues.put(Helper.DESAFIO_EXITO, String.valueOf(desafio.getExitoDesafio()));

        String[] whereArgs = {String.valueOf(desafio.getDesafioId())};

        int count = mDatabase.update(Helper.TABLA_CICLISTA, newValues, Helper.COL_1 + " =?", whereArgs);
        return count;
    }*/

    public Desafio cursorToDesafio(Cursor cursor) throws ParseException {

        Desafio desafio = new Desafio();
        desafio.setDesafioId(cursor.getLong(0));
        desafio.setDesafioNombre(cursor.getString(1));
        desafio.setDesafioDescripcion(cursor.getString(2));
        desafio.setInicioDesafio(new java.sql.Date(format.parse(cursor.getString(3)).getTime()));
        desafio.setTerminoDesafio(new java.sql.Date(format.parse(cursor.getString(4)).getTime()));
        desafio.setEstadoDesafio(cursor.getString(5).charAt(0));
        if (cursor.getString(6).equals("1")) {
            desafio.setExitoDesafio(true);
        } else {
            desafio.setExitoDesafio(false);
        }
        desafio.setSeries(cursor.getInt(7));
        desafio.setRepeticiones(cursor.getInt(8));
        //desafio.setExitoDesafio(Boolean.parseBoolean(String.valueOf(cursor.getString(6))));
        return desafio;
    }
}
