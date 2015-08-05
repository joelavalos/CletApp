package com.example.joel.cletapp.CRUDDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.ClasesDataBase.DesafioRutina;
import com.example.joel.cletapp.ClasesDataBase.Rutina;
import com.example.joel.cletapp.Helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Joel on 05/08/2015.
 */
public class DesafioRutinaCRUD {
    public static final String TAG = "DesafioRutinaCRUD";
    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private SQLiteDatabase mDatabase;
    private Helper mDbHelper;
    private Context mContext;

    private String[] mAllColumns = {
            Helper.DESAFIOSRUTINA_ID,
            Helper.DESAFIOSRUTINA_RUTINA_ID,
            Helper.DESAFIOSRUTINA_DESAFIO_ID,
            Helper.DESAFIOSRUTINA_FECHA};

    public DesafioRutinaCRUD(Context context) {
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

    public DesafioRutina insertarDesafioRutina(DesafioRutina desafioRutina) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Helper.DESAFIOSRUTINA_RUTINA_ID, desafioRutina.getRutina().getRutinaId());
        contentValues.put(Helper.DESAFIOSRUTINA_DESAFIO_ID, desafioRutina.getDesafio().getDesafioId());
        contentValues.put(Helper.DESAFIOSRUTINA_FECHA, df.format(desafioRutina.getFecha()));

        long id = mDatabase.insert(mDbHelper.TABLA_DESAFIOSRUTINA, null, contentValues);

        Cursor cursor = mDatabase.query(Helper.TABLA_DESAFIOSRUTINA, mAllColumns, Helper.DESAFIOSRUTINA_ID + " ='" + id + "'", null, null, null, null);

        DesafioRutina newDesafioRutina = new DesafioRutina();

        while (cursor.moveToNext()) {
            try {
                newDesafioRutina = cursorToDesafioRutina(cursor);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.close();

        return newDesafioRutina;
    }

    public DesafioRutina cursorToDesafioRutina(Cursor cursor) throws ParseException {

        DesafioRutina newDesafioRutina = new DesafioRutina();

        newDesafioRutina.setDesafioRutinaId(cursor.getLong(0));

        //Obtener el ID de la Rutina
        long rutinaId = cursor.getLong(1);
        RutinaCRUD rutinaCRUD = new RutinaCRUD(mContext);
        Rutina rutina = rutinaCRUD.buscarRutinaPorId(rutinaId);
        newDesafioRutina.setRutina(rutina);

        //Obtener el ID del desafio
        long desafioId = cursor.getLong(2);
        DesafioCRUD desafioCRUD = new DesafioCRUD(mContext);
        Desafio desafio = desafioCRUD.buscarDesafioPorId(desafioId);
        newDesafioRutina.setDesafio(desafio);

        newDesafioRutina.setFecha(new java.sql.Date(format.parse(cursor.getString(3)).getTime()));

        return newDesafioRutina;
    }
}