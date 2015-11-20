package com.example.joel.cletapp.CRUDDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.ClasesDataBase.Serie;
import com.example.joel.cletapp.Helper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 28/10/2015.
 */
public class SerieCRUD {
    public static final String TAG = "SerieCRUD";
    private SQLiteDatabase mDatabase;
    private Helper mDbHelper;
    private Context mContext;

    private String[] mAllColumns = {
            Helper.SERIE_ID,
            Helper.SERIE_DESAFIO_ID,};

    public SerieCRUD(Context context) {
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

    public Serie insertarSerie(Serie serie) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Helper.SERIE_DESAFIO_ID, serie.getDesafio().getDesafioId());

        long id = mDatabase.insert(mDbHelper.TABLA_SERIE, null, contentValues);

        Cursor cursor = mDatabase.query(Helper.TABLA_SERIE, mAllColumns, Helper.SERIE_ID + " ='" + id + "'", null, null, null, null);

        Serie newSerie = new Serie();

        while (cursor.moveToNext()) {
            try {
                newSerie = cursorToSerie(cursor);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.close();

        return newSerie;
    }

    public int eliminarSerie(Serie serie) {
        int id = mDatabase.delete(Helper.TABLA_SERIE, Helper.SERIE_ID + " ='" + serie.getSerieId() + "'", null);
        return id;
    }

    public Serie buscarSeriePorId(long ID) throws ParseException {
        Cursor cursor = mDatabase.query(Helper.TABLA_SERIE, mAllColumns, Helper.SERIE_ID + " ='" + ID + "'", null, null, null, null);
        List<String> stringBuffer = new ArrayList<String>();

        Serie serie = new Serie();

        while (cursor.moveToNext()) {
            serie = cursorToSerie(cursor);
        }

        cursor.close();

        return serie;
    }

    public List<Serie> buscarSeriePorIdDesafio(Desafio ID) throws ParseException {
        List<Serie> seriesDesafio = new ArrayList<>();
        Cursor cursor = mDatabase.query(Helper.TABLA_SERIE, mAllColumns, Helper.SERIE_DESAFIO_ID + " ='" + ID.getDesafioId() + "'", null, null, null, null);

        while (cursor.moveToNext()) {
            Serie serie = cursorToSerie(cursor);
            seriesDesafio.add(serie);
        }

        cursor.close();

        return seriesDesafio;
    }

    private Serie cursorToSerie(Cursor cursor) throws ParseException {
        Serie serie = new Serie();

        serie.setSerieId(cursor.getLong(0));

        //Obtener el ID del desafio
        long desafioId = cursor.getLong(1);
        DesafioCRUD desafioCRUD = new DesafioCRUD(mContext);
        Desafio desafio = desafioCRUD.buscarDesafioPorId(desafioId);
        serie.setDesafio(desafio);

        return serie;
    }
}
