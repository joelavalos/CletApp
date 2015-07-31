package com.example.joel.cletapp.CRUDDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.ClasesDataBase.DesafioObjetivo;
import com.example.joel.cletapp.ClasesDataBase.Objetivo;
import com.example.joel.cletapp.Helper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 28/07/2015.
 */
public class DesafioObjetivoCRUD {
    public static final String TAG = "DesafioObjetivoCRUD";
    private SQLiteDatabase mDatabase;
    private Helper mDbHelper;
    private Context mContext;

    private String[] mAllColumns = {
            Helper.DESAFIOOBJETIVO_ID,
            Helper.DESAFIOOBJETIVO_DESAFIO_ID,
            Helper.DESAFIOOBJETIVO_OBJETIVO_ID,
            Helper.DESAFIOOBJETIVO_VALOR};

    public DesafioObjetivoCRUD(Context context) {
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

    public DesafioObjetivo insertarDesafioObjetivo(DesafioObjetivo desafioObjetivo) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Helper.DESAFIOOBJETIVO_DESAFIO_ID, desafioObjetivo.getDesafio().getDesafioId());
        contentValues.put(Helper.DESAFIOOBJETIVO_OBJETIVO_ID, desafioObjetivo.getObjetivo().getObjetivoId());
        contentValues.put(Helper.DESAFIOOBJETIVO_VALOR, desafioObjetivo.getValor());

        long id = mDatabase.insert(mDbHelper.TABLA_DESAFIOOBJETIVO, null, contentValues);

        Cursor cursor = mDatabase.query(Helper.TABLA_DESAFIOOBJETIVO, mAllColumns, Helper.DESAFIOOBJETIVO_ID + " ='" + id + "'", null, null, null, null);

        DesafioObjetivo newDesafioObjetivo = new DesafioObjetivo();

        while (cursor.moveToNext()) {
            try {
                newDesafioObjetivo = cursorToDesafioObjetivo(cursor);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.close();

        return newDesafioObjetivo;
    }

    public DesafioObjetivo buscarDesafioObjetivoPorIdDesafio(Desafio ID) throws ParseException {
        String[] columns = {Helper.DESAFIOOBJETIVO_ID, Helper.DESAFIOOBJETIVO_DESAFIO_ID, Helper.DESAFIOOBJETIVO_OBJETIVO_ID, Helper.DESAFIOOBJETIVO_VALOR};
        Cursor cursor = mDatabase.query(Helper.TABLA_DESAFIOOBJETIVO, columns, Helper.DESAFIOOBJETIVO_DESAFIO_ID + " ='" + ID.getDesafioId() + "'", null, null, null, null);

        DesafioObjetivo desafioObjetivo = new DesafioObjetivo();

        while (cursor.moveToNext()) {
            desafioObjetivo = cursorToDesafioObjetivo(cursor);
        }

        cursor.close();

        return desafioObjetivo;
    }

    public DesafioObjetivo buscarDesafioObjetivoPorId(long ID) throws ParseException {
        String[] columns = {Helper.DESAFIOOBJETIVO_ID, Helper.DESAFIOOBJETIVO_DESAFIO_ID, Helper.DESAFIOOBJETIVO_OBJETIVO_ID, Helper.DESAFIOOBJETIVO_VALOR};
        Cursor cursor = mDatabase.query(Helper.TABLA_DESAFIOOBJETIVO, columns, Helper.DESAFIOOBJETIVO_ID + " ='" + ID + "'", null, null, null, null);

        DesafioObjetivo desafioObjetivo = new DesafioObjetivo();

        while (cursor.moveToNext()) {
            desafioObjetivo = cursorToDesafioObjetivo(cursor);
        }

        cursor.close();

        return desafioObjetivo;
    }

    public List<DesafioObjetivo> buscarTodosLosDesafioObjetivos() {
        List<DesafioObjetivo> listDesafioObjetivos = new ArrayList<DesafioObjetivo>();

        Cursor cursor = mDatabase.query(Helper.TABLA_DESAFIOOBJETIVO, mAllColumns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            try {
                DesafioObjetivo desafioObjetivo = cursorToDesafioObjetivo(cursor);
                listDesafioObjetivos.add(desafioObjetivo);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return listDesafioObjetivos;
    }

    public DesafioObjetivo cursorToDesafioObjetivo(Cursor cursor) throws ParseException {

        DesafioObjetivo desafioObjetivo = new DesafioObjetivo();

        desafioObjetivo.setDesObjId(cursor.getLong(0));

        //Obtener el ID del desafio
        long desafioId = cursor.getLong(1);
        DesafioCRUD desafioCRUD = new DesafioCRUD(mContext);
        Desafio desafio = desafioCRUD.buscarDesafioPorId(desafioId);
        desafioObjetivo.setDesafio(desafio);

        //Obtener el ID del objetivo
        long objetivoId = cursor.getLong(2);
        ObjetivoCRUD objetivoCRUD = new ObjetivoCRUD(mContext);
        Objetivo objetivo = objetivoCRUD.buscarObjetivoPorId(objetivoId);
        desafioObjetivo.setObjetivo(objetivo);

        desafioObjetivo.setValor(cursor.getFloat(3));

        return desafioObjetivo;
    }
}
