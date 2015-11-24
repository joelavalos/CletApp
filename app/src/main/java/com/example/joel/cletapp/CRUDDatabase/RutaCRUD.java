package com.example.joel.cletapp.CRUDDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.joel.cletapp.ClasesDataBase.Ruta;
import com.example.joel.cletapp.ClasesDataBase.Rutina;
import com.example.joel.cletapp.Helper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 24/11/2015.
 */
public class RutaCRUD {
    public static final String TAG = "RutaCRUD";

    private SQLiteDatabase mDatabase;
    private Helper mDbHelper;
    private Context mContext;

    private String[] mAllColumns = {
            Helper.RUTA_ID,
            Helper.RUTA_NOMBRE,
            Helper.RUTA_CORDENADAS};

    public RutaCRUD(Context context) {
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

    public Ruta insertarRuta(Ruta ruta) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(Helper.RUTA_NOMBRE, ruta.getRutaNombre());
        contentValues.put(Helper.RUTA_CORDENADAS, ruta.getRutaCordenadas());

        long id = mDatabase.insert(mDbHelper.TABLA_RUTA, null, contentValues);

        Cursor cursor = mDatabase.query(Helper.TABLA_RUTA, mAllColumns, Helper.RUTA_ID + " ='" + id + "'", null, null, null, null);

        Ruta newRuta = new Ruta();

        while (cursor.moveToNext()) {

            try {
                newRuta = cursorToRuta(cursor);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return newRuta;
    }

    public Ruta buscarRutaPorId(long ID) throws ParseException {
        Cursor cursor = mDatabase.query(Helper.TABLA_RUTA, mAllColumns, Helper.RUTA_ID + " ='" + ID + "'", null, null, null, null);

        Ruta ruta = new Ruta();

        while (cursor.moveToNext()) {
            ruta = cursorToRuta(cursor);
        }
        cursor.close();

        return ruta;
    }

    public List<Ruta> buscarTodasLasRutas() {
        List<Ruta> listRutas = new ArrayList<Ruta>();
        Cursor cursor = mDatabase.query(Helper.TABLA_RUTA, mAllColumns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            try {
                Ruta ruta = cursorToRuta(cursor);
                listRutas.add(ruta);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return listRutas;
    }

    public Ruta actualizarRuta(Ruta ruta) throws ParseException {
        ContentValues newValues = new ContentValues();

        newValues.put(Helper.RUTA_NOMBRE, ruta.getRutaNombre());
        newValues.put(Helper.RUTA_CORDENADAS, ruta.getRutaCordenadas());

        String[] whereArgs = {String.valueOf(ruta.getRutaId())};

        int count = mDatabase.update(Helper.TABLA_RUTA, newValues, Helper.RUTA_ID + " =?", whereArgs);

        Cursor cursor = mDatabase.query(Helper.TABLA_RUTA, mAllColumns, Helper.RUTA_ID + " ='" + ruta.getRutaId() + "'", null, null, null, null);
        Ruta ruta1 = new Ruta();

        while (cursor.moveToNext()) {
            ruta1 = cursorToRuta(cursor);
        }
        cursor.close();

        return ruta1;
    }

    public Ruta cursorToRuta(Cursor cursor) throws ParseException {

        Ruta ruta = new Ruta();
        ruta.setRutaId(cursor.getLong(0));
        ruta.setRutaNombre(cursor.getString(1));
        ruta.setRutaCordenadas(cursor.getString(2));

        return ruta;
    }
}
