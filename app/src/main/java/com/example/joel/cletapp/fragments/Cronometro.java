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
import com.example.joel.cletapp.Mensaje;
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
    private int cronometroDescansoSerie = 10;
    private int cronometroDescansoRepeticion = 15;
    private int cronometroCordenadas = 0;
    private int series = 0;
    private int repeticiones = 0;

    private Handler handler;
    private Vibrator alertaTermino;

    private Intent resultIntent;
    private PendingIntent resultPendingIntent;

    private Notification n;
    private NotificationManager notifManager; // notifManager IS GLOBAL
    private NotificationCompat.Builder mBuilder;
    private NotificationCompat.Builder mBuilderForeground;
    private int tiempoLimite = 10800;

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

    public static void setUpdateListener(MainFragment poiService) {
        UPDATE_LISTENER = poiService;
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
            locationAntigua = new Location("puntoViejo");
            locationAntigua.setLatitude(location.getLatitude());
            locationAntigua.setLongitude(location.getLongitude());

            latitud = location.getLatitude();
            longitud = location.getLongitude();
        }

        startForeground(mNotificationId, n);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                UPDATE_LISTENER.actualizarCronometro(cronometro);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        iniciarCronometro();
        iniciarBaseDeDatos();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                UPDATE_LISTENER.actualizarCronometro(cronometro);

                if (cronometro == tiempoLimite) {
                    crearNotificacion();
                    UPDATE_LISTENER.completarDesafio();
                    alertaTermino = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    alertaTermino.vibrate(1000);
                    stopSelf();
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
        //numeroRepeticion = repeticionesActualesTotal.size();
        numeroRepeticion = 0;
    }

    private void iniciarCronometro() {
        cronometro = UPDATE_LISTENER.intValorCronometro;
        series = UPDATE_LISTENER.seriesTotal;
        repeticiones = UPDATE_LISTENER.repeticionesTotal;

        temporizador.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                cronometro += 1;
                cronometroCordenadas += 1;
                cronometroRepeticion += 1;

                int mNotificationId = 9;
                mBuilderForeground.setContentText(segundosToHorasCronometro(cronometro));
                n = mBuilderForeground.build();
                notifManager.notify(mNotificationId, n);

                if (UPDATE_LISTENER != null) {
                    handler.sendEmptyMessage(0);
                } else {
                    if (cronometro == tiempoLimite) {
                        crearNotificacion();
                        alertaTermino = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        alertaTermino.vibrate(1000);
                        stopSelf();
                    }
                }
            }
        }, 0, INTERVALO_ACTUALIZACION);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        pararCronometro();
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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (cronometroCordenadas >= 10) {

            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 1000, 1, this);
            location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);

            latitud = location.getLatitude();
            longitud = location.getLongitude();
            LatLng coordinate = new LatLng(latitud, longitud);

            cordenadas.add(coordinate);
            guardarCordenadas(cordenadas);

            if (UPDATE_LISTENER != null) {
                UPDATE_LISTENER.mostrarCordenadas(latitud, longitud);

                distance = distance + location.distanceTo(locationAntigua);
                distance = Float.parseFloat(df.format(distance));

                distanceSerie = distanceSerie + location.distanceTo(locationAntigua);
                distanceSerie = Float.parseFloat(df.format(distanceSerie));


                new Mensaje(getBaseContext(), "Totales: " + repeticionesActualesTotal.size());


                UPDATE_LISTENER.actualizarValorDesafio(distance);

                locationAntigua.setLatitude(latitud);
                locationAntigua.setLongitude(longitud);

                if (cronometroRepeticion >= 60) {
                    if (numeroRepeticion < repeticionesActuales.size()) {
                        cronometroRepeticion = 0;
                        actualizarDatosRepeticion(distanceSerie, repeticionesActualesTotal.get(numeroRepeticion));
                        numeroRepeticion++;
                        distanceSerie = 0;
                    } else {
                        new Mensaje(getBaseContext(), "Todo terminado");
                    }
                }
            }

            cronometroCordenadas = 0;
        }
    }

    private void actualizarDatosRepeticion(float distanceSerie, Repeticiones repeticiones) {
        repeticiones.setValor(distanceSerie);
        Repeticiones buscadoRepeticion = new Repeticiones();
        try {
            buscadoRepeticion = repeticionesCRUD.actualizarDatosRepeticion(repeticiones);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        new Mensaje(getBaseContext(), "Repeticion actualizada con: " + buscadoRepeticion.getValor());
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
}
