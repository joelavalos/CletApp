package com.example.joel.cletapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Joel on 24/07/2015.
 */
public class DataBaseAdapter {

    Helper helper;

    public DataBaseAdapter(Context context) {
        helper = new Helper(context);
    }

    public long insertData(String rut, String nombre, String apellido_pat, String fecha, String altura, String peso, String sexo) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Helper.COL_1, rut);
        contentValues.put(Helper.COL_2, nombre);
        contentValues.put(Helper.COL_3, apellido_pat);
        contentValues.put(Helper.COL_4, fecha);
        contentValues.put(Helper.COL_5, altura);
        contentValues.put(Helper.COL_6, peso);
        contentValues.put(Helper.COL_7, sexo);

        long id = db.insert(helper.TABLA_CICLISTA, null, contentValues);
        return id;
    }

    static class Helper extends SQLiteOpenHelper {

        public static final int VERSION = 1;
        public static final String DATABASE_NAME = "CletApp.db";
        public static final String TABLA_CICLISTA = "tabla_ciclista";
        public static final String COL_1 = "RUT";
        public static final String COL_2 = "NOMBRE";
        public static final String COL_3 = "APELLIDO_PAT";
        public static final String COL_4 = "FECHA_NACIMIENTO";
        public static final String COL_5 = "PESO";
        public static final String COL_6 = "ALTURA";
        public static final String COL_7 = "SEXO";
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLA_CICLISTA + " (" + COL_1 + " VARCHAR(255) PRIMARY KEY not null, " + COL_2 + " VARCHAR(255) not null, " + COL_3 + " VARCHAR(255), " + COL_4 + " VARCHAR(255) not null, " + COL_5 + " VARCHAR(255) not null, " + COL_6 + " VARCHAR(255) not null, " + COL_7 + " VARCHAR(255) not null);";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLA_CICLISTA + ";";
        private Context context;

        public Helper(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
            this.context = context;
            Mensaje asd = new Mensaje(context, "Constructor called");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATE_TABLE);
                Mensaje asd = new Mensaje(context, "OnCreate called");
            } catch (SQLException e) {
                Mensaje asd = new Mensaje(context, e + "");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE);
                Mensaje asd = new Mensaje(context, "OnUpgrade called");
                onCreate(db);
            } catch (SQLException e) {
                Mensaje asd = new Mensaje(context, e + "");
            }
        }
    }
}
