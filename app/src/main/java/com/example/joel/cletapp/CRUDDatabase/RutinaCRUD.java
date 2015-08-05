package com.example.joel.cletapp.CRUDDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.joel.cletapp.ClasesDataBase.Resumen;
import com.example.joel.cletapp.ClasesDataBase.Rutina;
import com.example.joel.cletapp.Helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Joel on 04/08/2015.
 */
public class RutinaCRUD {
    public static final String TAG = "RutinaCRUD";

    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private SQLiteDatabase mDatabase;
    private Helper mDbHelper;
    private Context mContext;

    private String[] mAllColumns = {
            Helper.RUTINA_ID,
            Helper.RUTINA_NOMBRE,
            Helper.RUTINA_DESCRIPCON,
            Helper.RUTINA_INICIO,
            Helper.RUTINA_TERMINO,
            Helper.RUTINA_ESTADO,
            Helper.RUTINA_RESUMEN_ID};

    public RutinaCRUD(Context context) {
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

    public Rutina insertarRutina(Rutina rutina) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(Helper.RUTINA_NOMBRE, rutina.getRutinaNombre());
        contentValues.put(Helper.RUTINA_DESCRIPCON, rutina.getRutinaDescripcion());
        contentValues.put(Helper.RUTINA_INICIO, df.format(rutina.getRutinaInicio()));
        contentValues.put(Helper.RUTINA_TERMINO, df.format(rutina.getRutinaTermino()));
        contentValues.put(Helper.RUTINA_ESTADO, String.valueOf(rutina.getRutinaEstado()));
        contentValues.put(Helper.RUTINA_RESUMEN_ID, rutina.getResumen().getResumenId());

        long id = mDatabase.insert(mDbHelper.TABLA_RUTINA, null, contentValues);

        Cursor cursor = mDatabase.query(Helper.TABLA_RUTINA, mAllColumns, Helper.RUTINA_ID + " ='" + id + "'", null, null, null, null);

        Rutina newRutina = new Rutina();

        while (cursor.moveToNext()) {

            try {
                newRutina = cursorToRutina(cursor);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return newRutina;
    }

    public Rutina buscarRutinaPorId(long ID) throws ParseException {
        Cursor cursor = mDatabase.query(Helper.TABLA_RUTINA, mAllColumns, Helper.RUTINA_ID + " ='" + ID + "'", null, null, null, null);

        Rutina rutina = new Rutina();

        while (cursor.moveToNext()) {
            rutina = cursorToRutina(cursor);
        }
        cursor.close();

        return rutina;
    }

    public Rutina cursorToRutina(Cursor cursor) throws ParseException {

        Rutina rutina = new Rutina();
        rutina.setRutinaId(cursor.getLong(0));
        rutina.setRutinaNombre(cursor.getString(1));
        rutina.setRutinaDescripcion(cursor.getString(2));
        rutina.setRutinaInicio(new java.sql.Date(format.parse(cursor.getString(3)).getTime()));
        rutina.setRutinaTermino(new java.sql.Date(format.parse(cursor.getString(4)).getTime()));
        rutina.setRutinaEstado(cursor.getString(5).charAt(0));

        long resumenId = cursor.getLong(6);
        ResumenCRUD resumenCRUD = new ResumenCRUD(mContext);
        Resumen resumen = resumenCRUD.buscarResumenPorId(resumenId);
        rutina.setResumen(resumen);

        return rutina;
    }
}