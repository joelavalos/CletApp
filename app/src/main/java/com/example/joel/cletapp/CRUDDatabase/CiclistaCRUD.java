package com.example.joel.cletapp.CRUDDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.joel.cletapp.ClasesDataBase.Ciclista;
import com.example.joel.cletapp.Helper;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 27/07/2015.
 */
public class CiclistaCRUD  {

    public static final String TAG = "CiclistaCRUD";

    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private SQLiteDatabase mDatabase;
    private Helper mDbHelper;
    private Context mContext;

    private String[] mAllColumns = { Helper.COL_1,
            Helper.COL_2, Helper.COL_3,
            Helper.COL_4,
            Helper.COL_5,
            Helper.COL_6,
            Helper.COL_7 };

    public CiclistaCRUD(Context context) {
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

    public long insertarPerfil(Ciclista ciclista) {
        //SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Helper.COL_1, ciclista.getCiclistRut());
        contentValues.put(Helper.COL_2, ciclista.getCiclistaNombre());
        contentValues.put(Helper.COL_3, ciclista.getCiclistaApellido());
        contentValues.put(Helper.COL_4, df.format(ciclista.getCiclistaFechaNacimiento()));
        contentValues.put(Helper.COL_5, ciclista.getCiclistaPeso());
        contentValues.put(Helper.COL_6, ciclista.getCiclistaAltura());
        contentValues.put(Helper.COL_7, String.valueOf(ciclista.getCiclistaSexo()));

        long id = mDatabase.insert(mDbHelper.TABLA_CICLISTA, null, contentValues);
        return id;
    }

    public Ciclista buscarCiclistaPorRut(String RUT) throws ParseException {
        //SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String[] columns = {Helper.COL_1, Helper.COL_2, Helper.COL_3, Helper.COL_4, Helper.COL_5, Helper.COL_6, Helper.COL_7};
        Cursor cursor = mDatabase.query(Helper.TABLA_CICLISTA, columns, Helper.COL_1 + " ='" + RUT + "'", null, null, null, null);
        List<String> stringBuffer = new ArrayList<String>();

        Ciclista ciclista = new Ciclista();

        while (cursor.moveToNext()) {
            Log.v("pls", "Estoy pasando por aca");
            ciclista = cursorToCiclista(cursor);
        }

        cursor.close();

        return ciclista;
    }

    public int actualizarDatosCiclista(Ciclista ciclista) {
        //SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues newValues = new ContentValues();

        newValues.put(Helper.COL_2, ciclista.getCiclistaNombre());
        newValues.put(Helper.COL_3, ciclista.getCiclistaApellido());
        newValues.put(Helper.COL_4, df.format(ciclista.getCiclistaFechaNacimiento()));
        newValues.put(Helper.COL_5, ciclista.getCiclistaPeso());
        newValues.put(Helper.COL_6, ciclista.getCiclistaAltura());
        newValues.put(Helper.COL_7, String.valueOf(ciclista.getCiclistaSexo()));

        String[] whereArgs = {ciclista.getCiclistRut()};

        int count = mDatabase.update(Helper.TABLA_CICLISTA, newValues, Helper.COL_1 + " =?", whereArgs);
        return count;
    }

    public Ciclista cursorToCiclista(Cursor cursor) throws ParseException {

        Ciclista ciclista = new Ciclista();
        ciclista.setCiclistRut(cursor.getString(0));
        ciclista.setCiclistaNombre(cursor.getString(1));
        ciclista.setCiclistaApellido(cursor.getString(2));
        ciclista.setCiclistaFechaNacimiento(new java.sql.Date(format.parse(cursor.getString(3)).getTime()));
        ciclista.setCiclistaPeso(Float.parseFloat(cursor.getString(4)));
        ciclista.setCiclistaAltura(Float.parseFloat(cursor.getString(5)));
        ciclista.setCiclistaSexo(cursor.getString(6).charAt(0));

        return ciclista;
    }
}
