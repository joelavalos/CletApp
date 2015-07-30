package com.example.joel.cletapp.CRUDDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.joel.cletapp.ClasesDataBase.DesafioObjetivo;
import com.example.joel.cletapp.ClasesDataBase.Objetivo;
import com.example.joel.cletapp.Helper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 28/07/2015.
 */
public class ObjetivoCRUD {
    public static final String TAG = "ObjetivoCRUD";
    private SQLiteDatabase mDatabase;
    private Helper mDbHelper;
    private Context mContext;

    private String[] mAllColumns = {
            Helper.OBJETIVO_ID,
            Helper.OBJETIVO_NOMBRE,
            Helper.OBJETIVO_DESCRIPCON};

    public ObjetivoCRUD(Context context) {
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
    }

    public void close() {
        mDbHelper.close();
    }

    public long insertarObjetivo(Objetivo objetivo) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Helper.OBJETIVO_NOMBRE, objetivo.getObjetivoNombre());
        contentValues.put(Helper.OBJETIVO_DESCRIPCON, objetivo.getObjetivoDescripcion());

        long id = mDatabase.insert(mDbHelper.TABLA_OBJETIVO, null, contentValues);
        return id;
    }

    public Objetivo buscarObjetivoPorId(long ID) throws ParseException {
        String[] columns = {Helper.OBJETIVO_ID, Helper.OBJETIVO_NOMBRE, Helper.OBJETIVO_DESCRIPCON};
        Cursor cursor = mDatabase.query(Helper.TABLA_OBJETIVO, columns, Helper.OBJETIVO_ID + " ='" + ID + "'", null, null, null, null);
        List<String> stringBuffer = new ArrayList<String>();

        Objetivo objetivo = new Objetivo();

        while (cursor.moveToNext()) {
            objetivo = cursorToObjetivo(cursor);
        }

        cursor.close();

        return objetivo;
    }

    public Objetivo buscarObjetivoPorIdDesafioObjetivo(DesafioObjetivo ID) throws ParseException {
        String[] columns = {Helper.OBJETIVO_ID, Helper.OBJETIVO_NOMBRE, Helper.OBJETIVO_DESCRIPCON};
        Cursor cursor = mDatabase.query(Helper.TABLA_OBJETIVO, columns, Helper.OBJETIVO_ID + " ='" + ID.getObjetivo().getObjetivoId() + "'", null, null, null, null);

        Objetivo objetivo = new Objetivo();

        while (cursor.moveToNext()) {
            objetivo = cursorToObjetivo(cursor);
        }

        cursor.close();

        return objetivo;
    }

    public Objetivo buscarObjetivoPorNombre(String Nombre) throws ParseException {
        String[] columns = {Helper.OBJETIVO_ID, Helper.OBJETIVO_NOMBRE, Helper.OBJETIVO_DESCRIPCON};
        Cursor cursor = mDatabase.query(Helper.TABLA_OBJETIVO, columns, Helper.OBJETIVO_NOMBRE + " ='" + Nombre + "'", null, null, null, null);

        Objetivo objetivo = new Objetivo();

        while (cursor.moveToNext()) {
            objetivo = cursorToObjetivo(cursor);
        }

        cursor.close();

        return objetivo;
    }

    public int actualizarDatosDesafio(Objetivo objetivo) {
        ContentValues newValues = new ContentValues();

        newValues.put(Helper.OBJETIVO_NOMBRE, objetivo.getObjetivoNombre());
        newValues.put(Helper.OBJETIVO_DESCRIPCON, objetivo.getObjetivoDescripcion());

        String[] whereArgs = {String.valueOf(objetivo.getObjetivoId())};

        int count = mDatabase.update(Helper.TABLA_OBJETIVO, newValues, Helper.OBJETIVO_ID + " =?", whereArgs);
        return count;
    }

    public List<Objetivo> buscarTodosLosObjetivos(){
        List<Objetivo> listObjetivos = new ArrayList<Objetivo>();

        Cursor cursor = mDatabase.query(Helper.TABLA_OBJETIVO, mAllColumns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            try {
                Objetivo objetivo = cursorToObjetivo(cursor);
                listObjetivos.add(objetivo);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return listObjetivos;
    }

    public Objetivo cursorToObjetivo(Cursor cursor) throws ParseException {

        Objetivo objetivo = new Objetivo();
        objetivo.setObjetivoId(cursor.getLong(0));
        objetivo.setObjetivoNombre(cursor.getString(1));
        objetivo.setObjetivoDescripcion(cursor.getString(2));

        return objetivo;
    }
}
