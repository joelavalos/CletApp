package com.example.joel.cletapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Joel on 20/07/2015.
 */
public class AdapterHistorialRow {

    ImageView ImageViewImagenDesafio;
    TextView TextViewNombreDesafio;
    TextView TextViewFechaTerminoDesafio;
    TextView TextViewExitoDesafio;

    public AdapterHistorialRow(View v) {
        this.ImageViewImagenDesafio = (ImageView) v.findViewById(R.id.ImageViewImagenDesafio);
        this.TextViewNombreDesafio = (TextView) v.findViewById(R.id.TextViewNombreDesafio);
        this.TextViewFechaTerminoDesafio = (TextView) v.findViewById(R.id.TextViewFechaTerminoDesafio);
        this.TextViewExitoDesafio = (TextView) v.findViewById(R.id.TextViewExitoDesafio);
    }
}
