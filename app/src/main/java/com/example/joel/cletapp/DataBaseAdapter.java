package com.example.joel.cletapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 24/07/2015.
 */
public class DataBaseAdapter {

    Helper helper;

    public DataBaseAdapter(Context context) {
        helper = new Helper(context);
    }

    public long insertarPerfil(String rut, String nombre, String apellido_pat, String fecha, String peso, String altura , String sexo) {

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

    public String obtenerTodosPerfiles(){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Helper.COL_1, Helper.COL_2, Helper.COL_3, Helper.COL_4, Helper.COL_5, Helper.COL_6, Helper.COL_7};
        Cursor cursor = db.query(Helper.TABLA_CICLISTA, columns, null, null, null, null, null);
        StringBuffer stringBuffer = new StringBuffer();

        while (cursor.moveToNext()){
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

            stringBuffer.append(rut + " " + nombre + " " + apellido+ " " + fecha_nacimiento+ " " + peso+ " " + altura+ " " + sexo + "\n");
        }

        return stringBuffer.toString();
    }

    public List<String> buscarPerfil(String RUT){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Helper.COL_1, Helper.COL_2, Helper.COL_3, Helper.COL_4, Helper.COL_5, Helper.COL_6, Helper.COL_7};
        Cursor cursor = db.query(Helper.TABLA_CICLISTA, columns, Helper.COL_1+" ='"+RUT+"'", null, null, null, null);
        List<String> stringBuffer = new ArrayList<String>();

        while (cursor.moveToNext()){
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

            //stringBuffer.add(rut);
            stringBuffer.add(nombre);
            stringBuffer.add(apellido);
            stringBuffer.add(fecha_nacimiento);
            stringBuffer.add(peso);
            stringBuffer.add(altura);
            stringBuffer.add(sexo);

        }
        return stringBuffer;
    }

    public int actualizarPerfil(String RUT, String newNombre, String newApellido, String newFechaNacimiento, String newPeso, String newAltura, String newSexo){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues newValues = new ContentValues();

        newValues.put(Helper.COL_2, newNombre);
        newValues.put(Helper.COL_3, newApellido);
        newValues.put(Helper.COL_4, newFechaNacimiento);
        newValues.put(Helper.COL_5, newPeso);
        newValues.put(Helper.COL_6, newAltura);
        newValues.put(Helper.COL_7, newSexo);

        String[] whereArgs = {RUT};

        int count = db.update(Helper.TABLA_CICLISTA, newValues, Helper.COL_1+" =?", whereArgs);
        return count;
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
        public static final String CREATE_TABLE = "CREATE TABLE " + TABLA_CICLISTA + " ("
                + COL_1 + " VARCHAR(11) PRIMARY KEY not null, "
                + COL_2 + " VARCHAR(40) not null, "
                + COL_3 + " VARCHAR(40), "
                + COL_4 + " DATE not null, "
                + COL_5 + " FLOAT not null, "
                + COL_6 + " FLOAT not null, "
                + COL_7 + " CHARACTER(1) not null);";
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
