package com.example.joel.cletapp.CRUDDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.joel.cletapp.ClasesDataBase.Dia;
import com.example.joel.cletapp.ClasesDataBase.DiaRutina;
import com.example.joel.cletapp.ClasesDataBase.Rutina;
import com.example.joel.cletapp.Helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Joel on 04/08/2015.
 */
public class DiaRutinaCRUD {
    public static final String TAG = "DiaRutinaCRUD";
    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private SQLiteDatabase mDatabase;
    private Helper mDbHelper;
    private Context mContext;

    private String[] mAllColumns = {
            Helper.DIASRUTINA_ID,
            Helper.DIASRUTINA_RUTINA_ID,
            Helper.DIASRUTINA_DIA_ID,
            Helper.DIASRUTINA_FECHA};

    public DiaRutinaCRUD(Context context) {
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

    public DiaRutina insertarDiaRutina(DiaRutina diaRutina) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Helper.DIASRUTINA_RUTINA_ID, diaRutina.getRutina().getRutinaId());
        contentValues.put(Helper.DIASRUTINA_DIA_ID, diaRutina.getDia().getDiaNombre());
        contentValues.put(Helper.DIASRUTINA_FECHA, df.format(diaRutina.getFecha()));

        long id = mDatabase.insert(mDbHelper.TABLA_DIASRUTINA, null, contentValues);

        Cursor cursor = mDatabase.query(Helper.TABLA_DIASRUTINA, mAllColumns, Helper.DIASRUTINA_ID + " ='" + id + "'", null, null, null, null);

        DiaRutina newDiaRutina = new DiaRutina();

        while (cursor.moveToNext()) {
            try {
                newDiaRutina = cursorToDiaRutina(cursor);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.close();

        return newDiaRutina;
    }

    public DiaRutina cursorToDiaRutina(Cursor cursor) throws ParseException {

        DiaRutina diaRutina = new DiaRutina();

        diaRutina.setDiaRutinaId(cursor.getLong(0));

        //Obtener el ID de al Rutina
        long rutinaId = cursor.getLong(1);
        RutinaCRUD rutinaCRUD = new RutinaCRUD(mContext);
        Rutina rutina = rutinaCRUD.buscarRutinaPorId(rutinaId);
        diaRutina.setRutina(rutina);

        //Obtener el ID del dia
        String diaId = cursor.getString(2);
        DiaCRUD diaCRUD = new DiaCRUD(mContext);
        Dia dia = diaCRUD.buscarDiaPorId(diaId);
        diaRutina.setDia(dia);

        diaRutina.setFecha(new java.sql.Date(format.parse(cursor.getString(3)).getTime()));

        return diaRutina;
    }
}
