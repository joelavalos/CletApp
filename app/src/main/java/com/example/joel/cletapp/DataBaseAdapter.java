package com.example.joel.cletapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.joel.cletapp.ClasesDataBase.Ciclista;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 24/07/2015.
 */
/*public class DataBaseAdapter {

    Helper helper;
    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    public DataBaseAdapter(Context context) {
        helper = new Helper(context);
    }

    public long insertarPerfil(Ciclista ciclista) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Helper.COL_1, ciclista.getCiclistRut());
        contentValues.put(Helper.COL_2, ciclista.getCiclistaNombre());
        contentValues.put(Helper.COL_3, ciclista.getCiclistaApellido());
        contentValues.put(Helper.COL_4, df.format(ciclista.getCiclistaFechaNacimiento()));
        contentValues.put(Helper.COL_5, ciclista.getCiclistaPeso());
        contentValues.put(Helper.COL_6, ciclista.getCiclistaAltura());
        contentValues.put(Helper.COL_7, String.valueOf(ciclista.getCiclistaSexo()));

        long id = db.insert(helper.TABLA_CICLISTA, null, contentValues);
        return id;
    }

    public String obtenerTodosPerfiles() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Helper.COL_1, Helper.COL_2, Helper.COL_3, Helper.COL_4, Helper.COL_5, Helper.COL_6, Helper.COL_7};
        Cursor cursor = db.query(Helper.TABLA_CICLISTA, columns, null, null, null, null, null);
        StringBuffer stringBuffer = new StringBuffer();

        while (cursor.moveToNext()) {
            int index_1 = cursor.getColumnIndex(Helper.COL_1);
            int index_2 = cursor.getColumnIndex(Helper.COL_2);
            int index_3 = cursor.getColumnIndex(Helper.COL_3);
            int index_4 = cursor.getColumnIndex(Helper.COL_4);
            int index_5 = cursor.getColumnIndex(Helper.COL_5);
            int index_6 = cursor.getColumnIndex(Helper.COL_6);
            int index_7 = cursor.getColumnIndex(Helper.COL_7);

            String rut = cursor.getString(index_1);
            String nombre = cursor.getString(index_2);
            String apellido = cursor.getString(index_3);
            String fecha_nacimiento = cursor.getString(index_4);
            String peso = cursor.getString(index_5);
            String altura = cursor.getString(index_6);
            String sexo = cursor.getString(index_7);

            stringBuffer.append(rut + " " + nombre + " " + apellido + " " + fecha_nacimiento + " " + peso + " " + altura + " " + sexo + "\n");
        }

        return stringBuffer.toString();
    }

    public Ciclista buscarCiclistaPorRut(String RUT) throws ParseException {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Helper.COL_1, Helper.COL_2, Helper.COL_3, Helper.COL_4, Helper.COL_5, Helper.COL_6, Helper.COL_7};
        Cursor cursor = db.query(Helper.TABLA_CICLISTA, columns, Helper.COL_1 + " ='" + RUT + "'", null, null, null, null);
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
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues newValues = new ContentValues();

        newValues.put(Helper.COL_2, ciclista.getCiclistaNombre());
        newValues.put(Helper.COL_3, ciclista.getCiclistaApellido());
        newValues.put(Helper.COL_4, df.format(ciclista.getCiclistaFechaNacimiento()));
        newValues.put(Helper.COL_5, ciclista.getCiclistaPeso());
        newValues.put(Helper.COL_6, ciclista.getCiclistaAltura());
        newValues.put(Helper.COL_7, String.valueOf(ciclista.getCiclistaSexo()));

        String[] whereArgs = {ciclista.getCiclistRut()};

        int count = db.update(Helper.TABLA_CICLISTA, newValues, Helper.COL_1 + " =?", whereArgs);
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

    public class Helper extends SQLiteOpenHelper {
        public static final int VERSION = 1;
        public static final String DATABASE_NAME = "CletApp.db";

        //Tabla ciclista
        public static final String TABLA_CICLISTA = "tabla_ciclista";
        public static final String COL_1 = "RUT";
        public static final String COL_2 = "NOMBRE";
        public static final String COL_3 = "APELLIDO_PAT";
        public static final String COL_4 = "FECHA_NACIMIENTO";
        public static final String COL_5 = "PESO";
        public static final String COL_6 = "ALTURA";
        public static final String COL_7 = "SEXO";
        public static final String CREATE_TABLE_CICLISTA = "CREATE TABLE " + TABLA_CICLISTA + " ("
                + COL_1 + " VARCHAR(11) PRIMARY KEY not null, "
                + COL_2 + " VARCHAR(40) not null, "
                + COL_3 + " VARCHAR(40), "
                + COL_4 + " DATE not null, "
                + COL_5 + " FLOAT not null, "
                + COL_6 + " FLOAT not null, "
                + COL_7 + " CHARACTER(1) not null);";
        public static final String DROP_TABLE_CICLISTA = "DROP TABLE IF EXISTS " + TABLA_CICLISTA + ";";
        private Context context;

        public Helper(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
            this.context = context;
            Mensaje asd = new Mensaje(context, "Constructor called");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATE_TABLE_CICLISTA);
                Mensaje asd = new Mensaje(context, "OnCreate called");
            } catch (SQLException e) {
                Mensaje asd = new Mensaje(context, e + "");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE_CICLISTA);
                Mensaje asd = new Mensaje(context, "OnUpgrade called");
                onCreate(db);
            } catch (SQLException e) {
                Mensaje asd = new Mensaje(context, e + "");
            }
        }
    }
}*/
