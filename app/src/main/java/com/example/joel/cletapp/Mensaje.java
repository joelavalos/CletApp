package com.example.joel.cletapp;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Joel on 17/07/2015.
 */
public class Mensaje {
    public Mensaje(Context context, String mensaje) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
    }
}
