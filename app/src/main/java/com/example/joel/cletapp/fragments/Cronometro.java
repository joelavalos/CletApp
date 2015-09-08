package com.example.joel.cletapp.fragments;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.joel.cletapp.MainActivity;
import com.example.joel.cletapp.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Joel on 05/09/2015.
 */
public class Cronometro extends Service {
    private Timer temporizador = new Timer();
    private static final long INTERVALO_ACTUALIZACION = 1000; // En ms
    public static MainFragment UPDATE_LISTENER;
    private int cronometro;

    private Handler handler;

    private Intent resultIntent;
    private PendingIntent resultPendingIntent;

    private NotificationCompat.Builder mBuilder;
    private int tiempoLimite = 60;

    public static void setUpdateListener(MainFragment poiService) {
        UPDATE_LISTENER = poiService;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.v("asd", "Primero");
        resultIntent = new Intent(getBaseContext(), MainActivity.class);
        resultPendingIntent = PendingIntent.getActivity(getBaseContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder = new NotificationCompat.Builder(getBaseContext())
                .setSmallIcon(R.drawable.ic_directions_bike_black_48dp)
                .setContentTitle("Titulo")
                .setContentText("Texto")
                .setContentIntent(resultPendingIntent);

        cronometro = UPDATE_LISTENER.intValorCronometro;
        iniciarCronometro();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                UPDATE_LISTENER.actualizarCronometro(cronometro);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("asd", "Segundo: " + cronometro);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                UPDATE_LISTENER.actualizarCronometro(cronometro);
                if (cronometro == tiempoLimite) {
                    cronometro = tiempoLimite;
                    actualizarNotificacion();
                    UPDATE_LISTENER.completarDesafio();
                    stopSelf();
                }
            }
        };
        return super.onStartCommand(intent, flags, startId);
    }

    private void actualizarNotificacion() {
        if (cronometro == tiempoLimite) {
            mBuilder.setSmallIcon(R.drawable.ic_directions_bike_white_24dp)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setContentTitle("Desafio terminado")
                    .setContentText("Presiona para ver tus resultados")
                    .setContentIntent(resultPendingIntent);

            int mNotificationId = 002;
            NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, mBuilder.setAutoCancel(true).build());

            Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
        } else {
            mBuilder.setSmallIcon(R.drawable.ic_directions_bike_white_24dp)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setContentTitle("Desafio en curso")
                    .setContentText("Tiempo: " + UPDATE_LISTENER.segundosToHoras(cronometro))
                    .setContentIntent(resultPendingIntent);

            int mNotificationId = 002;
            NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, mBuilder.setAutoCancel(true).build());
        }
    }

    private void detenerNotificacion() {
        int mNotificationId = 002;
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(mNotificationId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pararCronometro();
    }

    private void iniciarCronometro() {
        temporizador.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                cronometro += 1;
                if (UPDATE_LISTENER != null) {
                    handler.sendEmptyMessage(0);
                }
                if (UPDATE_LISTENER != null && UPDATE_LISTENER.estado == false) {
                    actualizarNotificacion();

                } else if (UPDATE_LISTENER != null && UPDATE_LISTENER.estado == true) {
                    detenerNotificacion();
                }
            }
        }, 0, INTERVALO_ACTUALIZACION);
    }

    private void pararCronometro() {
        if (temporizador != null)
            temporizador.cancel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
