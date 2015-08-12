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
public class ResumenCRUD {
    public static final String TAG = "ResumenCRUD";

    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private SQLiteDatabase mDatabase;
    private Helper mDbHelper;
    private Context mContext;

    private String[] mAllColumns = {
            Helper.RESUMEN_ID,
            Helper.RESUMEN_ANALISIS,
            Helper.RESUMEN_FECHA};

    public ResumenCRUD(Context context) {
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

    public Resumen insertarResumen(Resumen resumen) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(Helper.RESUMEN_ANALISIS, resumen.getResumenAnalisis());
        contentValues.put(Helper.RESUMEN_FECHA, df.format(resumen.getResumenFecha()));

        long id = mDatabase.insert(mDbHelper.TABLA_RESUMEN, null, contentValues);

        Cursor cursor = mDatabase.query(Helper.TABLA_RESUMEN, mAllColumns, Helper.RESUMEN_ID + " ='" + id + "'", null, null, null, null);

        Resumen newResumen = new Resumen();

        while (cursor.moveToNext()){

            try {
                newResumen = cursorToResumen(cursor);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return newResumen;
    }

    public int eliminarResumen(Resumen resumen) {
        int id = mDatabase.delete(Helper.TABLA_RESUMEN, Helper.RESUMEN_ID + " ='" + resumen.getResumenId() + "'", null);
        return id;
    }

    public Resumen buscarResumenPorId(long ID) throws ParseException {
        //String[] columns = {Helper.DESAFIO_ID, Helper.DESAFIO_NOMBRE, Helper.DESAFIO_DESCRIPCON, Helper.DESAFIO_INICIO, Helper.DESAFIO_TERMINO, Helper.DESAFIO_ESTADO, Helper.DESAFIO_EXITO};
        Cursor cursor = mDatabase.query(Helper.TABLA_RESUMEN, mAllColumns, Helper.RESUMEN_ID + " ='" + ID + "'", null, null, null, null);

        Resumen resumen = new Resumen();

        while (cursor.moveToNext()) {
            resumen = cursorToResumen(cursor);
        }
        cursor.close();

        return resumen;
    }

    public Resumen cursorToResumen(Cursor cursor) throws ParseException {

        Resumen resumen = new Resumen();
        resumen.setResumenId(cursor.getLong(0));
        resumen.setResumenAnalisis(cursor.getString(1));
        resumen.setResumenFecha(new java.sql.Date(format.parse(cursor.getString(2)).getTime()));

        return resumen;
    }
}
