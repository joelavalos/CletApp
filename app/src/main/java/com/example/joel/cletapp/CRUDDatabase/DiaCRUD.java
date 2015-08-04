package com.example.joel.cletapp.CRUDDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.joel.cletapp.ClasesDataBase.Dia;
import com.example.joel.cletapp.Helper;

import java.text.ParseException;

/**
 * Created by Joel on 04/08/2015.
 */
public class DiaCRUD {
    public static final String TAG = "DiaCRUD";

    private SQLiteDatabase mDatabase;
    private Helper mDbHelper;
    private Context mContext;

    private String[] mAllColumns = {
            Helper.DIA_NOMBRE};

    public DiaCRUD(Context context) {
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

    public Dia insertarDia(Dia dia) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(Helper.DIA_NOMBRE, dia.getDiaNombre());

        long id = mDatabase.insert(mDbHelper.TABLA_DIA, null, contentValues);
        String nombre = dia.getDiaNombre();

        Cursor cursor = mDatabase.query(Helper.TABLA_DIA, mAllColumns, Helper.DIA_NOMBRE + " ='" + nombre + "'", null, null, null, null);

        Dia newDia = new Dia();

        while (cursor.moveToNext()){

            try {
                newDia = cursorToDia(cursor);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return newDia;
    }

    public Dia buscarDiaPorId(String nombre) throws ParseException {
        Cursor cursor = mDatabase.query(Helper.TABLA_DIA, mAllColumns, Helper.DIA_NOMBRE + " ='" + nombre + "'", null, null, null, null);

        Dia dia = new Dia();

        while (cursor.moveToNext()) {
            dia = cursorToDia(cursor);
        }
        cursor.close();

        return dia;
    }

    public Dia cursorToDia(Cursor cursor) throws ParseException {

        Dia dia = new Dia();
        dia.setDiaNombre(cursor.getString(0));

        return dia;
    }
}
