package com.example.joel.cletapp;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Joel on 27/07/2015.
 */
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
