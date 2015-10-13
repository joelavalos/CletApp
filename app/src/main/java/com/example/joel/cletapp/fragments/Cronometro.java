package com.example.joel.cletapp.fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.example.joel.cletapp.MainActivity;
import com.example.joel.cletapp.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Joel on 05/09/2015.
 */
public class Cronometro extends Service {
    private Timer temporizador = new Timer();
    private static final long INTERVALO_ACTUALIZACION = 1000; // En ms
    public static MainFragment UPDATE_LISTENER;
    private int cronometro = 0;
    private int cronometroCordenadas = 0;

    private Handler handler;
    private Vibrator alertaTermino;
    //private long[] pattern = { 0, 1000*3, 1000*3};

    private Intent resultIntent;
    private PendingIntent resultPendingIntent;

    private Notification n;
    private NotificationManager notifManager; // notifManager IS GLOBAL
    private NotificationCompat.Builder mBuilder;
    private NotificationCompat.Builder mBuilderForeground;
    private int tiempoLimite = 300;

    //Prueba
    private double longitud, latitud;
    private LocationManager locationManager;
    private Location location;
    private boolean gpsActivo;
    //Fin prueba

    public static void setUpdateListener(MainFragment poiService) {
        UPDATE_LISTENER = poiService;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //cronometro = UPDATE_LISTENER.intValorCronometro;
        notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        resultIntent = new Intent(getBaseContext(), MainActivity.class);
        resultPendingIntent = PendingIntent.getActivity(getBaseContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilderForeground = new NotificationCompat.Builder(getBaseContext())
                .setSmallIcon(R.drawable.ic_directions_bike_white_24dp)
                .setContentTitle("Titulo")
                .setContentText("Texto")
                .setContentIntent(resultPendingIntent)
                .setColor(getResources().getColor(R.color.colorPrimary));

        int mNotificationId = 9;
        n = mBuilderForeground.build();
        notifManager.notify(mNotificationId, n);

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
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                UPDATE_LISTENER.actualizarCronometro(cronometro);
                if (cronometroCordenadas == 5){
                    UPDATE_LISTENER.mostrarCordenadas(latitud, longitud);
                    cronometroCordenadas = 0;
                }
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

    private void iniciarCronometro() {
        cronometro = UPDATE_LISTENER.intValorCronometro;
        temporizador.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                cronometro += 1;
                cronometroCordenadas += 1;

                int mNotificationId = 9;
                mBuilderForeground.setContentText(segundosToHorasCronometro(cronometro));
                n = mBuilderForeground.build();
                notifManager.notify(mNotificationId, n);

                //Prueba
                Criteria criteria = new Criteria();
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                String provider = locationManager.getBestProvider(criteria, false);
                Location location = locationManager.getLastKnownLocation(provider);
                latitud = location.getLatitude();
                longitud = location.getLongitude();
                LatLng coordinate = new LatLng(latitud, longitud);
                //Fin prueba

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
}
