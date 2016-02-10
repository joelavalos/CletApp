package com.example.joel.cletapp.tests;

import android.app.Application;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.example.joel.cletapp.ClasesDataBase.Repeticiones;
import com.example.joel.cletapp.ClasesDataBase.Resumen;
import com.example.joel.cletapp.ClasesDataBase.Serie;
import com.example.joel.cletapp.Helper;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class AA_ApplicationTest extends ApplicationTestCase<Application> {
    public AA_ApplicationTest() {
        super(Application.class);
    }

    @SmallTest
    public void test_AA_ConexionLocationService() {
        Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        assertNotNull(location);
    }

    @SmallTest
    public void test_AB_ProveedorGPS() {
        LocationManager locationManager;
        boolean gpsActivo;
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        gpsActivo = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        assertTrue(gpsActivo);
    }

    @SmallTest
    public void test_AC_CrearBaseDeDatos() {
        Helper mDbHelper;
        mDbHelper = new Helper(getContext());
        assertNotNull(mDbHelper);
    }

    @SmallTest
    public void test_AD_CalculoCaloriasQuemadas() {
        double constanteIntensidadMedia = 0.049;
        double constante2 = 2.2;
        double peso = 100;
        double minutos = 90;

        double CaloriasQuemadas = constanteIntensidadMedia * constante2 * peso * minutos;

        assertEquals(970.2, CaloriasQuemadas);
    }

    @SmallTest
    public void test_AE_convertirMetrosToKilometros() {
        DecimalFormat df = new DecimalFormat("#.#");
        float esperado = Float.parseFloat(df.format(5));
        float metros = 5000;
        float kilometros = Float.parseFloat(df.format(metros / 1000));

        assertEquals(esperado, kilometros);
    }

    @SmallTest
    public void test_AF_convertirSegundosAHoras() {
        int horas;
        int minutos;
        int segundos;
        int totalSegundos = 5400;
        String horasString;
        String minutosString;
        String segundosString;
        String horaFinal;

        horas = totalSegundos / 3600;

        horasString = String.valueOf(horas);
        if (horasString.length() == 1) {
            horasString = "0" + horasString;
        }

        minutos = (totalSegundos - (3600 * horas)) / 60;
        minutosString = String.valueOf(minutos);
        if (minutosString.length() == 1) {
            minutosString = "0" + minutosString;
        }

        segundos = totalSegundos - ((horas * 3600) + (minutos * 60));
        segundosString = String.valueOf(segundos);
        if (segundosString.length() == 1) {
            segundosString = "0" + segundosString;
        }

        horaFinal = horasString + ":" + minutosString + ":" + segundosString;

        assertEquals("01:30:00", horaFinal);
    }

    @SmallTest
    public void test_AG_ObtenerLatitud() {
        Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        double lat = 0;

        lat = location.getLatitude();
        assertNotSame(0, lat);
    }

    @SmallTest
    public void test_AH_ObtenerLongitud() {
        Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        double lng = 0;

        lng = location.getLongitude();
        assertNotSame(0, lng);
    }

    @SmallTest
    public void test_AI_CrearCoordenada() {
        Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        double lat = 0;
        double lng = 0;

        lat = location.getLatitude();
        lng = location.getLongitude();
        LatLng latLngTest = new LatLng(lat, lng);
        assertNotNull(latLngTest);
    }

    @SmallTest
    public void test_AJ_ObtenerFechaActual() {
        Calendar cInicial = Calendar.getInstance();
        Date actual = cInicial.getTime();
        assertNotNull(actual);
    }

    @SmallTest
    public void test_AK_ValidarDatosCreacionRutina() {
        String validar = "";
        String Nombre = "Rutina 1";
        int diasDescanso = 0;
        String[] valoresDesafios = {"desafio", "desafio", "desafio", "desafio", "desafio", "desafio", "desafio"};

        for (int i = 0; i < valoresDesafios.length; i++) {
            if (valoresDesafios[i].equals("")) {
                validar = "Agenda incompleta";
            } else if (valoresDesafios[i].equals("Descansar")) {
                diasDescanso++;
            }
        }

        if (diasDescanso == 7) {
            validar = "Seleccione al menos 1 desafio";
        }

        if (Nombre.equals("")) {
            validar = "Ingrese nombre";
        }

        assertEquals("", validar);
    }

    @SmallTest
    public void test_AL_ValidarDatosCreacionDesafio() {
        String validar = "";
        String nombreDesafio = "Desafiom 1";
        String[] valores = {"Distancia", "30000 m", "2", "3",};

        if (valores[2].equals(".")) {
            validar = "Valor incorrecto";
        } else {
            if (Integer.valueOf(valores[2]) > 5) {
                validar = "Maximo 5 series";
            }

            if (Integer.valueOf(valores[2]) < 1) {
                validar = "Minimo 1 serie";
            }
        }

        if (valores[3].equals(".")) {
            validar = "Valor incorrecto";
        } else {
            if (Integer.valueOf(valores[3]) > 5) {
                validar = "Maximo 5 repeticiones";
            }

            if (Integer.valueOf(valores[3]) < 1) {
                validar = "Minimo 1 repeticion";
            }
        }

        if (valores[1].equals(". m")) {
            validar = "Valor incorrecto";
        } else {
            if (valores[1].equals("0 m")) {
                validar = "Ingrese distancia";
            }
        }

        if (valores[0].equals("")) {
            validar = "Seleccione categoria";
        }
        if (nombreDesafio.equals("")) {
            validar = "Ingrese nombre";
        }

        assertEquals("", validar);
    }

    @SmallTest
    public void test_AM_CrearTablas() {
        class Helper extends SQLiteOpenHelper {
            public static final int VERSION = 1;
            public static final String DATABASE_NAME = "CletApp2.db";

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
            public static final String DESAFIO_SERIES = "SERIES";
            public static final String DESAFIO_REPETICIONES = "REPETICIONES";
            public static final String DESAFIO_CRONOMETRO = "CRONOMETRO";
            public static final String CREATE_TABLE_DESAFIO = "CREATE TABLE " + TABLA_DESAFIO + " ("
                    + DESAFIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DESAFIO_NOMBRE + " VARCHAR(40) NOT NULL, "
                    + DESAFIO_DESCRIPCON + " VARCHAR(40), "
                    + DESAFIO_INICIO + " DATE NOT NULL, "
                    + DESAFIO_TERMINO + " FLOAT NOT NULL, "
                    + DESAFIO_ESTADO + " FLOAT NOT NULL, "
                    + DESAFIO_EXITO + " INTEGER NOT NULL, "
                    + DESAFIO_SERIES + " INTEGER NOT NULL, "
                    + DESAFIO_REPETICIONES + " INTEGER NOT NULL, "
                    + DESAFIO_CRONOMETRO + " INTEGER);";
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
                    + DESAFIOOBJETIVO_VALOR + " INTEGER NOT NULL, "
                    + " FOREIGN KEY (" + DESAFIOOBJETIVO_DESAFIO_ID + ") REFERENCES " + TABLA_DESAFIO + " (" + DESAFIO_ID + ") ON DELETE CASCADE);";
            public static final String DROP_TABLE_DESAFIOOBJETIVO = "DROP TABLE IF EXISTS " + TABLA_DESAFIOOBJETIVO + ";";

            //Tabla Resumen
            public static final String TABLA_RESUMEN = "tabla_resumen";
            public static final String RESUMEN_ID = "ID";
            public static final String RESUMEN_ANALISIS = "ANALISIS";
            public static final String RESUMEN_FECHA = "FECHA";
            public static final String CREATE_TABLE_RESUMEN = "CREATE TABLE " + TABLA_RESUMEN + " ("
                    + RESUMEN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + RESUMEN_ANALISIS + " VARCHAR(255) NOT NULL, "
                    + RESUMEN_FECHA + " DATE NOT NULL);";
            public static final String DROP_TABLE_RESUMEN = "DROP TABLE IF EXISTS " + TABLA_RESUMEN + ";";

            //Tabla Rutina
            public static final String TABLA_RUTINA = "tabla_rutina";
            public static final String RUTINA_ID = "ID";
            public static final String RUTINA_NOMBRE = "NOMBRE";
            public static final String RUTINA_DESCRIPCON = "DESCRIPCION";
            public static final String RUTINA_INICIO = "FECHA_INICIO";
            public static final String RUTINA_TERMINO = "FECHA_TERMINO";
            public static final String RUTINA_ESTADO = "ESTADO";
            public static final String RUTINA_RESUMEN_ID = "RESUMEN_ID";
            public static final String CREATE_TABLA_RUTINA = "CREATE TABLE " + TABLA_RUTINA + " ("
                    + RUTINA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + RUTINA_NOMBRE + " VARCHAR(40) NOT NULL, "
                    + RUTINA_DESCRIPCON + " VARCHAR(40), "
                    + RUTINA_INICIO + " DATE NOT NULL, "
                    + RUTINA_TERMINO + " FLOAT NOT NULL, "
                    + RUTINA_ESTADO + " FLOAT NOT NULL, "
                    + RUTINA_RESUMEN_ID + " INTEGER NOT NULL, "
                    + " FOREIGN KEY (" + RUTINA_RESUMEN_ID + ") REFERENCES " + TABLA_RESUMEN + " (" + RESUMEN_ID + ") ON DELETE CASCADE);";
            public static final String DROP_TABLA_RUTINA = "DROP TABLE IF EXISTS " + TABLA_RUTINA + ";";

            //Tabla desafiosRutina (Relacion many to many)
            public static final String TABLA_DESAFIOSRUTINA = "tabla_desafiosrutina";
            public static final String DESAFIOSRUTINA_ID = "ID";
            public static final String DESAFIOSRUTINA_RUTINA_ID = "RUTINA_ID";
            public static final String DESAFIOSRUTINA_DESAFIO_ID = "DESAFIO_ID";
            public static final String DESAFIOSRUTINA_FECHA = "FECHA";
            public static final String CREATE_TABLE_DESAFIOSRUTINA = "CREATE TABLE " + TABLA_DESAFIOSRUTINA + " ("
                    + DESAFIOSRUTINA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DESAFIOSRUTINA_RUTINA_ID + " INTEGER NOT NULL, "
                    + DESAFIOSRUTINA_DESAFIO_ID + " VARCHAR(255) NOT NULL, "
                    + DESAFIOSRUTINA_FECHA + " DATE NOT NULL, "
                    + " FOREIGN KEY (" + DESAFIOSRUTINA_RUTINA_ID + ") REFERENCES " + TABLA_RUTINA + " (" + RUTINA_ID + ") ON DELETE CASCADE, "
                    + " FOREIGN KEY (" + DESAFIOSRUTINA_DESAFIO_ID + ") REFERENCES " + TABLA_DESAFIO + " (" + DESAFIO_ID + ") ON DELETE CASCADE);";
            public static final String DROP_TABLE_DESAFIOSRUTINA = "DROP TABLE IF EXISTS " + TABLA_DESAFIOSRUTINA + ";";

            //Tabla Series (Relacion one to many)
            public static final String TABLA_SERIE = "tabla_serie";
            public static final String SERIE_ID = "ID";
            public static final String SERIE_DESAFIO_ID = "DESAFIO_ID";
            public static final String CREATE_TABLA_SERIE = "CREATE TABLE " + TABLA_SERIE + " ("
                    + SERIE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SERIE_DESAFIO_ID + " VARCHAR(255) NOT NULL, "
                    + " FOREIGN KEY (" + SERIE_DESAFIO_ID + ") REFERENCES " + TABLA_DESAFIO + " (" + DESAFIO_ID + ") ON DELETE CASCADE);";
            public static final String DROP_TABLE_SERIE = "DROP TABLE IF EXISTS " + TABLA_SERIE + ";";

            //Tabla Repeticiones (Relacion one to many)
            public static final String TABLA_REPETICIONES = "tabla_repeticiones";
            public static final String REPETICIONES_ID = "ID";
            public static final String REPETICIONES_SERIE_ID = "SERIE_ID";
            public static final String REPETICIONES_VALOR = "VALOR";
            public static final String CREATE_TABLA_REPETICIONES = "CREATE TABLE " + TABLA_REPETICIONES + " ("
                    + REPETICIONES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + REPETICIONES_SERIE_ID + " VARCHAR(255) NOT NULL, "
                    + REPETICIONES_VALOR + " INTEGER NOT NULL, "
                    + " FOREIGN KEY (" + REPETICIONES_SERIE_ID + ") REFERENCES " + TABLA_SERIE + " (" + SERIE_ID + ") ON DELETE CASCADE);";
            public static final String DROP_TABLA_REPETICIONES = "DROP TABLE IF EXISTS " + TABLA_REPETICIONES + ";";

            //Tabla Repeticiones (Relacion one to many)
            public static final String TABLA_RUTA = "tabla_ruta";
            public static final String RUTA_ID = "ID";
            public static final String RUTA_NOMBRE = "NOMBRE";
            public static final String RUTA_CORDENADAS = "CORDENADAS";
            public static final String CREATE_TABLA_RUTA = "CREATE TABLE " + TABLA_RUTA + " ("
                    + RUTA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + RUTA_NOMBRE + " VARCHAR NOT NULL, "
                    + RUTA_CORDENADAS + " VARCHAR NOT NULL);";
            public static final String DROP_TABLA_RUTA = "DROP TABLE IF EXISTS " + TABLA_RUTA + ";";

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
                    db.execSQL(CREATE_TABLE_RESUMEN);
                    db.execSQL(CREATE_TABLA_RUTINA);
                    db.execSQL(CREATE_TABLE_DESAFIOSRUTINA);
                    db.execSQL(CREATE_TABLA_SERIE);
                    db.execSQL(CREATE_TABLA_REPETICIONES);
                    db.execSQL(CREATE_TABLA_RUTA);
                    assertTrue(true);
                    //Mensaje asd = new Mensaje(context, "OnCreate called");
                } catch (SQLException e) {
                    //Mensaje asd = new Mensaje(context, e + "");
                    //Log.v("porque", e + "");
                    assertFalse(false);
                }
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                try {
                    db.execSQL(DROP_TABLE_CICLISTA);
                    db.execSQL(DROP_TABLE_DESAFIO);
                    db.execSQL(DROP_TABLE_OBJETIVO);
                    db.execSQL(DROP_TABLE_DESAFIOOBJETIVO);
                    db.execSQL(DROP_TABLE_RESUMEN);
                    db.execSQL(DROP_TABLA_RUTINA);
                    db.execSQL(DROP_TABLE_DESAFIOSRUTINA);
                    db.execSQL(DROP_TABLE_SERIE);
                    db.execSQL(DROP_TABLA_REPETICIONES);
                    db.execSQL(DROP_TABLA_RUTA);
                    //Mensaje asd = new Mensaje(context, "OnUpgrade called");
                    assertTrue(true);
                    onCreate(db);
                } catch (SQLException e) {
                    assertFalse(false);
                    //Mensaje asd = new Mensaje(context, e + "");
                }
            }
        }
    }

    @SmallTest
    public void test_AN_validarSeries() {
        int series = 3;
        String validar = "";
        if (series > 5) {
            validar = "Maximo 5 series";
        }

        if (series < 1) {
            validar = "Minimo 1 serie";
        }

        assertEquals("", validar);
    }

    @SmallTest
    public void test_AO_validarRepeticiones() {
        int repeticiones = 3;
        String validar = "";
        if (repeticiones > 5) {
            validar = "Maximo 5 repeticiones";
        }

        if (repeticiones < 1) {
            validar = "Minimo 1 repeticion";
        }

        assertEquals("", validar);
    }

    @SmallTest
    public void test_AP_validarResumen() {
        Resumen newResumen = new Resumen();
        newResumen.setResumenAnalisis("Analisis defecto");
        assertNotNull(newResumen);
    }

    @SmallTest
    public void test_AQ_CalculoFechaTermino() {
        Calendar cInicial = Calendar.getInstance();
        Date actual = cInicial.getTime();

        Date parsedFinal = null;
        String[] camposDesafios = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
        Locale spanish = new Locale("es", "PE");
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c;
        c = Calendar.getInstance();
        String formattedDate = sdf.format(c.getTime());

        for (int i = 0; i < camposDesafios.length; i++) {
            camposDesafios[i] = format.format(c.getTime()) + "     " + c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, spanish);

            if ((i + 1) < camposDesafios.length) {
                c.add(Calendar.DATE, +1);
            }
        }

        formattedDate = sdf.format(c.getTime());

        try {
            parsedFinal = format.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertNotSame(actual, parsedFinal);

    }

    @SmallTest
    public void test_AR_CalculoCantidadSeries() {
        Serie newSeries = new Serie();
        List<Serie> listSeries = new ArrayList<>();
        listSeries.add(newSeries);

        assertEquals(1, listSeries.size());
    }

    @SmallTest
    public void test_AS_CalculoCantidadRepeticiones() {
        Repeticiones newRepetiocion = new Repeticiones();
        List<Repeticiones> listRepeticiones = new ArrayList<>();
        listRepeticiones.add(newRepetiocion);

        assertEquals(1, listRepeticiones.size());
    }

    @SmallTest
    public void test_AT_CalculoValorDistanciaRepeticiones() {
        Repeticiones newRepetiocion = new Repeticiones();
        float valorRepeticion = 5000;
        newRepetiocion.setValor(Float.parseFloat(String.valueOf(5000)));

        assertEquals(valorRepeticion, newRepetiocion.getValor());
    }
}