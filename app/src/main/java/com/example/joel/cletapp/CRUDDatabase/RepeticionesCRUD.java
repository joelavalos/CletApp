package com.example.joel.cletapp.CRUDDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.joel.cletapp.ClasesDataBase.Repeticiones;
import com.example.joel.cletapp.ClasesDataBase.Serie;
import com.example.joel.cletapp.Helper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 28/10/2015.
 */
public class RepeticionesCRUD {
    public static final String TAG = "SerieCRUD";
    private SQLiteDatabase mDatabase;
    private Helper mDbHelper;
    private Context mContext;

    private String[] mAllColumns = {
            Helper.REPETICIONES_ID,
            Helper.REPETICIONES_SERIE_ID,
            Helper.REPETICIONES_VALOR};

    public RepeticionesCRUD(Context context) {
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

    public Repeticiones insertarRepeticion(Repeticiones repeticion) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Helper.REPETICIONES_SERIE_ID, repeticion.getSerie().getSerieId());
        contentValues.put(Helper.REPETICIONES_VALOR, repeticion.getValor());

        long id = mDatabase.insert(mDbHelper.TABLA_REPETICIONES, null, contentValues);

        Cursor cursor = mDatabase.query(Helper.TABLA_REPETICIONES, mAllColumns, Helper.REPETICIONES_ID + " ='" + id + "'", null, null, null, null);

        Repeticiones newRepeticion = new Repeticiones();

        while (cursor.moveToNext()) {
            try {
                newRepeticion = cursorToRepeticiones(cursor);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.close();

        return newRepeticion;
    }

    public List<Repeticiones> buscarRepeticionesPorIdSerie(Serie ID) throws ParseException {
        List<Repeticiones> repeticionesDesafio = new ArrayList<>();
        Cursor cursor = mDatabase.query(Helper.TABLA_REPETICIONES, mAllColumns, Helper.REPETICIONES_SERIE_ID + " ='" + ID.getSerieId() + "'", null, null, null, null);

        while (cursor.moveToNext()) {
            Repeticiones repeticiones = cursorToRepeticiones(cursor);
            repeticionesDesafio.add(repeticiones);
        }

        cursor.close();

        return repeticionesDesafio;
    }

    public Repeticiones actualizarDatosRepeticion(Repeticiones repeticiones) throws ParseException {
        ContentValues newValues = new ContentValues();

        newValues.put(Helper.REPETICIONES_VALOR, repeticiones.getValor());

        String[] whereArgs = {String.valueOf(repeticiones.getRepeticionId())};

        int count = mDatabase.update(Helper.TABLA_REPETICIONES, newValues, Helper.REPETICIONES_ID + " =?", whereArgs);

        Cursor cursor = mDatabase.query(Helper.TABLA_REPETICIONES, mAllColumns, Helper.REPETICIONES_ID + " ='" + repeticiones.getRepeticionId() + "'", null, null, null, null);
        Repeticiones repeticionReturn = new Repeticiones();

        while (cursor.moveToNext()) {
            repeticionReturn = cursorToRepeticiones(cursor);
        }
        cursor.close();

        return repeticionReturn;
    }

    private Repeticiones cursorToRepeticiones(Cursor cursor) throws ParseException {
        Repeticiones repeticion = new Repeticiones();

        repeticion.setRepeticionId(cursor.getLong(0));

        //Obtener el ID de la serie
        long serieId = cursor.getLong(1);
        SerieCRUD serieCRUD = new SerieCRUD(mContext);
        Serie serie = serieCRUD.buscarSeriePorId(serieId);
        repeticion.setSerie(serie);

        repeticion.setValor(cursor.getFloat(2));

        return repeticion;
    }
}
