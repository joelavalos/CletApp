package com.example.joel.cletapp;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
            + COL_1 + " VARCHAR(11) PRIMARY KEY NOT NULL, "
            + COL_2 + " VARCHAR(40) NOT NULL, "
            + COL_3 + " VARCHAR(40), "
            + COL_4 + " DATE NOT NULL, "
            + COL_5 + " FLOAT NOT NULL, "
            + COL_6 + " FLOAT NOT NULL, "
            + COL_7 + " CHARACTER(1) NOT NULL);";
    public static final String DROP_TABLE_CICLISTA = "DROP TABLE IF EXISTS " + TABLA_CICLISTA + ";";

    //Tabla desafios
    public static final String TABLA_DESAFIO = "tabla_desafio";
    public static final String DESAFIO_ID = "ID";
    public static final String DESAFIO_NOMBRE = "NOMBRE";
    public static final String DESAFIO_DESCRIPCON = "DESCRIPCION";
    public static final String DESAFIO_INICIO = "FECHA_INICIO";
    public static final String DESAFIO_TERMINO = "FECHA_TERMINO";
    public static final String DESAFIO_ESTADO = "ESTADO";
    public static final String DESAFIO_EXITO = "EXITO";
    public static final String CREATE_TABLE_DESAFIO = "CREATE TABLE " + TABLA_DESAFIO + " ("
            + DESAFIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DESAFIO_NOMBRE + " VARCHAR(40) NOT NULL, "
            + DESAFIO_DESCRIPCON + " VARCHAR(40), "
            + DESAFIO_INICIO + " DATE NOT NULL, "
            + DESAFIO_TERMINO + " FLOAT NOT NULL, "
            + DESAFIO_ESTADO + " FLOAT NOT NULL, "
            + DESAFIO_EXITO + " CHARACTER(1) NOT NULL);";
    public static final String DROP_TABLE_DESAFIO = "DROP TABLE IF EXISTS " + TABLA_DESAFIO + ";";

    //Tabla objetivo
    public static final String TABLA_OBJETIVO = "tabla_objetivo";
    public static final String OBJETIVO_ID = "ID";
    public static final String OBJETIVO_NOMBRE = "NOMBRE";
    public static final String OBJETIVO_DESCRIPCON = "DESCRIPCION";
    public static final String CREATE_TABLE_OBJETIVO = "CREATE TABLE " + TABLA_OBJETIVO + " ("
            + OBJETIVO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + OBJETIVO_NOMBRE + " VARCHAR(40) NOT NULL, "
            + OBJETIVO_DESCRIPCON + " VARCHAR(255) NOT NULL);";
    public static final String DROP_TABLE_OBJETIVO = "DROP TABLE IF EXISTS " + TABLA_OBJETIVO + ";";

    //Tabla desafioObjetivos (Relacion many to many)
    public static final String TABLA_DESAFIOOBJETIVO = "tabla_desafioobjetivo";
    public static final String DESAFIOOBJETIVO_ID = "ID";
    public static final String DESAFIOOBJETIVO_DESAFIO_ID = "DESAFIO_ID";
    public static final String DESAFIOOBJETIVO_OBJETIVO_ID = "OBJETIVO_ID";
    public static final String DESAFIOOBJETIVO_VALOR = "VALOR";
    public static final String CREATE_TABLE_DESAFIOOBJETIVO = "CREATE TABLE " + TABLA_DESAFIOOBJETIVO + " ("
            + DESAFIOOBJETIVO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DESAFIOOBJETIVO_DESAFIO_ID + " INTEGER NOT NULL, "
            + DESAFIOOBJETIVO_OBJETIVO_ID + " INTEGER NOT NULL, "
            + DESAFIOOBJETIVO_VALOR + " INTEGER NOT NULL);";
    public static final String DROP_TABLE_DESAFIOOBJETIVO = "DROP TABLE IF EXISTS " + TABLA_DESAFIOOBJETIVO + ";";

    private Context context;

    public Helper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
        //Mensaje asd = new Mensaje(context, "Constructor called");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(CREATE_TABLE_CICLISTA);
            db.execSQL(CREATE_TABLE_DESAFIO);
            db.execSQL(CREATE_TABLE_OBJETIVO);
            db.execSQL(CREATE_TABLE_DESAFIOOBJETIVO);
            //Mensaje asd = new Mensaje(context, "OnCreate called");
        } catch (SQLException e) {
            //Mensaje asd = new Mensaje(context, e + "");
            Log.v("porque", e + "");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TABLE_CICLISTA);
            db.execSQL(DROP_TABLE_DESAFIO);
            db.execSQL(DROP_TABLE_OBJETIVO);
            db.execSQL(DROP_TABLE_DESAFIOOBJETIVO);
            //Mensaje asd = new Mensaje(context, "OnUpgrade called");
            onCreate(db);
        } catch (SQLException e) {
            Mensaje asd = new Mensaje(context, e + "");
        }
    }
}
