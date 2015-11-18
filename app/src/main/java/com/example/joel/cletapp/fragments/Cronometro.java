package com.example.joel.cletapp.fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.example.joel.cletapp.CRUDDatabase.DesafioCRUD;
import com.example.joel.cletapp.CRUDDatabase.RepeticionesCRUD;
import com.example.joel.cletapp.CRUDDatabase.SerieCRUD;
import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.ClasesDataBase.Repeticiones;
import com.example.joel.cletapp.ClasesDataBase.Serie;
import com.example.joel.cletapp.MainActivity;
import com.example.joel.cletapp.R;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Joel on 05/09/2015.
 */
public class Cronometro extends Service implements LocationListener {
    private Timer temporizador = new Timer();
    private static final long INTERVALO_ACTUALIZACION = 1000; // En ms
    public static MainFragment UPDATE_LISTENER;
    private int cronometro = 0;
    private int cronometroRepeticion = 0;

    private int cronometroDescansoSerie = 20;
    private int cronometroDescansoRepeticion = 10;
    private int cronometroValorActualDescansoSerie = 0;
    private int cronometroValorActualDescansoRepeticion = 0;
    private int valorMaxcronometroValorActualDescansoRepeticion = 0;

    private int cronometroCordenadas = 0;
    private int series = 0;
    private int repeticiones = 0;
    private int serieActual = 1;
    private int repActual = 1;

    private Handler handler;
    private Vibrator alertaTermino;

    private Intent resultIntent;
    private PendingIntent resultPendingIntent;

    private Notification n;
    private NotificationManager notifManager; // notifManager IS GLOBAL
    private NotificationCompat.Builder mBuilder;
    private NotificationCompat.Builder mBuilderForeground;
    private int tiempoLimite = 1800;
    public static int tiempoLimiteTotal = 0;
    private int tiempoLimiteRepeticiones = 0;

    private double longitud, latitud;
    private LocationManager locationManager;
    private Location location;
    private Location locationAntigua;
    private boolean gpsActivo;
    public List<LatLng> cordenadas;
    private LatLng coordinate;
    private float distance = 0;
    private float distanceSerie = 0;
    private int numeroRepeticion = 0;
    private List<Float> distanciasSerie;

    private DecimalFormat df = new DecimalFormat("#.##");

    //Base de datos
    private Desafio desafioActual;
    private List<Serie> seriesActuales;
    private List<Repeticiones> repeticionesActuales;
    private List<Repeticiones> repeticionesActualesTotal;
    private Repeticiones repeticionActual;

    private DesafioCRUD desafioCRUD;
    private SerieCRUD serieCRUD;
    private RepeticionesCRUD repeticionesCRUD;

    private boolean pendienteDescansar = false;
    private boolean pendienteSerieTerminada = false;
    private boolean inicioSerie = false;

    public static void setUpdateListener(MainFragment poiService, int valorTiempoLimite) {
        UPDATE_LISTENER = poiService;
        tiempoLimiteTotal = valorTiempoLimite;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //final MediaPlayer desafioTerminado = MediaPlayer.create(getBaseContext(), R.drawable.desafio_terminado);
        //desafioTerminado.setVolume(15.0f, 15.0f);
        //desafioTerminado.start();
        pararCronometro();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        resultIntent = new Intent(getBaseContext(), MainActivity.class);
        resultPendingIntent = PendingIntent.getActivity(getBaseContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilderForeground = new NotificationCompat.Builder(getBaseContext())
                .setSmallIcon(R.drawable.ic_directions_bike_white_24dp)
                .setContentTitle("Rutina en curso")
                .setContentText("Texto")
                .setContentIntent(resultPendingIntent)
                .setColor(getResources().getColor(R.color.colorPrimary));

        int mNotificationId = 9;
        n = mBuilderForeground.build();
        notifManager.notify(mNotificationId, n);

        cordenadas = new ArrayList<>();

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        gpsActivo = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (gpsActivo) {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 1000 * 1/*1 minuto*/, 1/*Metros*/, this);
            location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);

            if (location != null) {
                locationAntigua = new Location("puntoViejo");
                locationAntigua.setLatitude(location.getLatitude());
                locationAntigua.setLongitude(location.getLongitude());

                latitud = location.getLatitude();
                longitud = location.getLongitude();
            } else {

                locationAntigua = new Location("puntoViejo");
                locationAntigua.setLatitude(0);
                locationAntigua.setLongitude(0);

                latitud = 0;
                longitud = 0;
            }
        }

        startForeground(mNotificationId, n);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                UPDATE_LISTENER.actualizarCronometro(cronometro, cronometroRepeticion, cronometroValorActualDescansoRepeticion);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Arreglado
        iniciarBaseDeDatos();
        iniciarCronometro();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (UPDATE_LISTENER != null) {
                    UPDATE_LISTENER.actualizarCronometro(cronometro, cronometroRepeticion, cronometroValorActualDescansoRepeticion);
                    UPDATE_LISTENER.actualizarSeriesRepeticiones(serieActual, repActual, numeroRepeticion);

                    if (cronometroValorActualDescansoRepeticion > 0) {
                        UPDATE_LISTENER.actualizarColorCronometro(1);
                    }
                    if (cronometroValorActualDescansoRepeticion > cronometroDescansoSerie) {
                        UPDATE_LISTENER.actualizarColorCronometro(2);
                    }
                    if (cronometroValorActualDescansoRepeticion == 0) {
                        UPDATE_LISTENER.actualizarColorCronometro(0);
                    }

                    if (cronometro >= tiempoLimiteTotal) {
                        crearNotificacion();
                        UPDATE_LISTENER.completarDesafio();
                        alertaTermino = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        alertaTermino.vibrate(1000);
                        guardarRepeticionReinicio();
                        stopSelf();
                    }
                }
            }
        };
        return START_REDELIVER_INTENT;
    }

    private void iniciarBaseDeDatos() {
        desafioCRUD = new DesafioCRUD(getBaseContext());
        serieCRUD = new SerieCRUD(getBaseContext());
        repeticionesCRUD = new RepeticionesCRUD(getBaseContext());
        seriesActuales = new ArrayList<>();
        repeticionesActuales = new ArrayList<>();
        repeticionesActualesTotal = new ArrayList<>();

        desafioActual = UPDATE_LISTENER.pasarDesafioActual();
        try {
            seriesActuales = serieCRUD.buscarSeriePorIdDesafio(desafioActual);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (int j = 0; j < seriesActuales.size(); j++) {
            try {
                repeticionesActuales = repeticionesCRUD.buscarRepeticionesPorIdSerie(seriesActuales.get(j));
                for (int i = 0; i < repeticionesActuales.size(); i++) {
                    repeticionesActualesTotal.add(repeticionesActuales.get(i));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        tiempoLimiteRepeticiones = tiempoLimite / repeticionesActualesTotal.size();
        int asd = cronometroDescansoRepeticion * (repeticionesActualesTotal.size() - 1);
        int asdA = cronometroDescansoSerie * (2 - 1);
        int xcv = asd * asdA;
    }

    private void iniciarCronometro() {
        final MediaPlayer desafioIniciado = MediaPlayer.create(getBaseContext(), R.drawable.desafio_iniciado);
        desafioIniciado.setVolume(15.0f, 15.0f);
        desafioIniciado.start();

        cronometro = UPDATE_LISTENER.intValorCronometro;
        cronometroRepeticion = UPDATE_LISTENER.cronometroRepeticion;
        repActual = UPDATE_LISTENER.cronoServiceRepeticion;
        serieActual = UPDATE_LISTENER.cronoServiceSerie;
        series = UPDATE_LISTENER.seriesTotal;
        repeticiones = UPDATE_LISTENER.repeticionesTotal;
        distance = UPDATE_LISTENER.cronoServiceDistanciaTotal;
        distanciasSerie = UPDATE_LISTENER.cronoServiceDistanciaSerie;
        numeroRepeticion = UPDATE_LISTENER.cronoServiceNumeroRepeticion;
        cronometroValorActualDescansoRepeticion = UPDATE_LISTENER.cronoServiceValorActualDescansoRepeticion;

        final MediaPlayer repeticionTerminada = MediaPlayer.create(getBaseContext(), R.drawable.repeticion_terminada);

        temporizador.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                cronometro += 1;
                cronometroCordenadas += 1;
                cronometroRepeticion += 1;

                int mNotificationId = 9;
                mBuilderForeground.setContentText(segundosToHorasCronometro(cronometro));
                n = mBuilderForeground.build();
                notifManager.notify(mNotificationId, n);

                final MediaPlayer descansa = MediaPlayer.create(getBaseContext(), R.drawable.descansa);

                if (cronometroValorActualDescansoRepeticion > 0) {
                    //Pasar el tiempo
                    cronometroValorActualDescansoRepeticion--;
                    if (pendienteDescansar == true && cronometroValorActualDescansoRepeticion == valorMaxcronometroValorActualDescansoRepeticion - 2) {
                        descansa.setVolume(15.0f, 15.0f);
                        descansa.start();
                        pendienteDescansar = false;
                    }

                    if (pendienteSerieTerminada == true && cronometroValorActualDescansoRepeticion == valorMaxcronometroValorActualDescansoRepeticion - 5) {
                        final MediaPlayer serieTerminada = MediaPlayer.create(getBaseContext(), R.drawable.serie_terminada);
                        serieTerminada.setVolume(15.0f, 15.0f);
                        serieTerminada.start();
                        pendienteSerieTerminada = false;
                    }

                    if (cronometroValorActualDescansoRepeticion == 0) {
                        cronometroRepeticion = 0;
                        cronometroCordenadas = 0;
                        if (serieActual > 1 && inicioSerie == true) {
                            final MediaPlayer repeticionIniciada = MediaPlayer.create(getBaseContext(), R.drawable.serie_iniciada);
                            repeticionIniciada.setVolume(15.0f, 15.0f);
                            repeticionIniciada.start();
                            inicioSerie = false;
                        } else {
                            final MediaPlayer repeticionIniciada = MediaPlayer.create(getBaseContext(), R.drawable.repeticion_iniciada);
                            repeticionIniciada.setVolume(15.0f, 15.0f);
                            repeticionIniciada.start();
                        }
                    }

                } else {
                    if (cronometroRepeticion >= tiempoLimiteRepeticiones) {

                        if (numeroRepeticion < repeticionesActualesTotal.size()) {
                            cronometroRepeticion = 0;
                            actualizarDatosRepeticion(distanciasSerie, repeticionesActualesTotal.get(numeroRepeticion));
                            numeroRepeticion++;

                            cronometroValorActualDescansoRepeticion = cronometroDescansoRepeticion;
                            valorMaxcronometroValorActualDescansoRepeticion = cronometroValorActualDescansoRepeticion;

                            alertaTermino = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            alertaTermino.vibrate(1000);

                            repActual++;
                            repeticionTerminada.setVolume(15.0f, 15.0f);
                            repeticionTerminada.start();
                            pendienteDescansar = true;

                            if (repActual > repeticiones) {
                                if (serieActual < series) {
                                    repActual = 1;
                                    serieActual++;

                                    pendienteSerieTerminada = true;
                                    inicioSerie = true;
                                    cronometroValorActualDescansoRepeticion = cronometroDescansoRepeticion + cronometroDescansoSerie;
                                    valorMaxcronometroValorActualDescansoRepeticion = cronometroValorActualDescansoRepeticion;
                                }
                                if ((repActual > repeticiones) && (serieActual == series)) {
                                    repActual = repeticiones;
                                    serieActual = series;
                                }
                            }
                            distanceSerie = 0;
                        } else {
                        }

                        distanciasSerie.clear();
                        guardarEstadoSerieRepeticion();
                    }
                }

                if (UPDATE_LISTENER != null) {
                    handler.sendEmptyMessage(0);
                } else {
                    if (cronometro >= tiempoLimiteTotal) {
                        crearNotificacion();
                        alertaTermino = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        alertaTermino.vibrate(1000);
                        stopSelf();
                    }
                }
            }
        }, 0, INTERVALO_ACTUALIZACION);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (cronometroValorActualDescansoRepeticion > 0) {
            //nada esta en descanso
        } else {
            if (cronometroCordenadas >= 10) {

                if (location != null) {
                    latitud = location.getLatitude();
                    longitud = location.getLongitude();
                    LatLng coordinate = new LatLng(latitud, longitud);

                    cordenadas.add(coordinate);
                    guardarCordenadas(cordenadas);

                    float distanciaBuffer = location.distanceTo(locationAntigua);

                    distance = distance + distanciaBuffer;
                    distance = Float.parseFloat(df.format(distance));
                    guardarDistanciaTotal(distance);

                    distanciasSerie.add(Float.parseFloat(df.format(distanciaBuffer)));
                    guardarDistanciaSerie(distanciasSerie);
                    distanceSerie = distanceSerie + distanciaBuffer;
                    distanceSerie = Float.parseFloat(df.format(distanceSerie));

                    locationAntigua.setLatitude(latitud);
                    locationAntigua.setLongitude(longitud);

                    if (UPDATE_LISTENER != null) {
                        UPDATE_LISTENER.mostrarCordenadas(latitud, longitud);
                        UPDATE_LISTENER.actualizarValorDesafio(distance);
                    }
                } else {

                }

                cronometroCordenadas = 0;
            }
        }
    }

    private void guardarCordenadas(List<LatLng> guardarCordenadas) {
        String guardar = "";
        for (int i = 0; i < guardarCordenadas.size(); i++) {
            if (guardar.equals("")) {
                guardar = guardarCordenadas.get(i).latitude + "=" + guardarCordenadas.get(i).longitude;
            } else {
                guardar = guardar + "X" + guardarCordenadas.get(i).latitude + "=" + guardarCordenadas.get(i).longitude;
            }
        }
        SharedPreferences prefs = getSharedPreferences("cordenadas", Context.MODE_PRIVATE);
        prefs.edit().putString("misCordenadas", guardar).commit();
    }

    private void actualizarDatosRepeticion(List<Float> distanceSerie, Repeticiones repeticiones) {
        float distanciaTotalSerie = 0;
        if (distanceSerie.isEmpty()) {
        } else {
            for (int i = 0; i < distanciasSerie.size(); i++) {
                distanciaTotalSerie = distanciaTotalSerie + distanceSerie.get(i);
            }
        }
        distanciaTotalSerie = Float.parseFloat(df.format(distanciaTotalSerie));

        repeticiones.setValor(distanciaTotalSerie);
        Repeticiones buscadoRepeticion = new Repeticiones();
        try {
            buscadoRepeticion = repeticionesCRUD.actualizarDatosRepeticion(repeticiones);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public String segundosToHorasCronometro(int totalSegundos) {
        int horas = totalSegundos / 3600;
        String horasString = String.valueOf(horas);
        if (horasString.length() == 1) {
            horasString = "0" + horasString;
        }

        int minutos = (totalSegundos - (3600 * horas)) / 60;
        String minutosString = String.valueOf(minutos);
        if (minutosString.length() == 1) {
            minutosString = "0" + minutosString;
        }

        int segundos = totalSegundos - ((horas * 3600) + (minutos * 60));
        String segundosString = String.valueOf(segundos);
        if (segundosString.length() == 1) {
            segundosString = "0" + segundosString;
        }

        return horasString + ":" + minutosString + ":" + segundosString;
    }

    private void crearNotificacion() {
        resultIntent = new Intent(getBaseContext(), MainActivity.class);
        resultIntent.putExtra("cronometroFinal", segundosToHorasCronometro(cronometro));
        resultPendingIntent = PendingIntent.getActivity(getBaseContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(getBaseContext())
                .setSmallIcon(R.drawable.ic_directions_bike_white_24dp)
                .setContentTitle("Desafio terminado")
                .setContentText("Presiona para ver tus resultados")
                .setContentIntent(resultPendingIntent)
                .setColor(getResources().getColor(R.color.colorPrimary));

        int mNotificationId2 = 001;
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId2, mBuilder.setAutoCancel(true).build());
    }

    private void pararCronometro() {
        if (temporizador != null)
            temporizador.cancel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void guardarEstadoSerieRepeticion() {
        SharedPreferences prefs = getSharedPreferences("serieRepeticion", Context.MODE_PRIVATE);
        prefs.edit().putInt("serieActual", serieActual).commit();
        prefs.edit().putInt("repeticionActual", repActual).commit();
        prefs.edit().putInt("numeroRepeticionActual", numeroRepeticion).commit();
    }

    private void guardarDistanciaTotal(float distance) {
        SharedPreferences prefs = getSharedPreferences("distanciaTotal", Context.MODE_PRIVATE);
        prefs.edit().putFloat("distanciaTotalService", distance).commit();
    }

    private void guardarDistanciaSerie(List<Float> listaDistanciasSerie) {
        String guardar = "";
        for (int i = 0; i < listaDistanciasSerie.size(); i++) {
            if (guardar.equals("")) {
                guardar = listaDistanciasSerie.get(i).toString();
            } else {
                guardar = guardar + "X" + listaDistanciasSerie.get(i).toString();
            }
        }
        SharedPreferences prefs = getSharedPreferences("distanciaTotal", Context.MODE_PRIVATE);
        prefs.edit().putString("distanciaTotalSerie", guardar).commit();
    }

    private void guardarRepeticionReinicio() {
        SharedPreferences prefs = getSharedPreferences("serieRepeticion", Context.MODE_PRIVATE);
        prefs.edit().putInt("numeroRepeticionActual", 0).commit();
    }
}
