package com.example.joel.cletapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by Joel on 20/07/2015.
 */
public class AdapterHistorial extends ArrayAdapter<String> {

    Context context;
    int[] images;
    String[] nombreDesafios;
    String[] fechasTermino;
    String[] exitoDesafios;

    public AdapterHistorial(Context c, int[] imgs, String[] nombres, String[] fechasTer, String[] exitos) {
        super(c, R.layout.single_historial_desafios_row, R.id.TextViewNombreDesafio, nombres);
        this.context = c;
        images = imgs;
        nombreDesafios = nombres;
        fechasTermino = fechasTer;
        exitoDesafios = exitos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        AdapterHistorialRow holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_historial_desafios_row, parent, false);
            holder = new AdapterHistorialRow(row);
            row.setTag(holder);
        } else {
            holder = (AdapterHistorialRow) row.getTag();
        }

        holder.ImageViewImagenDesafio.setImageResource(images[position]);
        holder.TextViewNombreDesafio.setText(nombreDesafios[position]);
        holder.TextViewFechaTerminoDesafio.setText(fechasTermino[position]);
        holder.TextViewExitoDesafio.setText(exitoDesafios[position]);

        if(exitoDesafios[position].contains("Logrado")){
            holder.TextViewExitoDesafio.setTextColor(context.getResources().getColor(R.color.colorVerde));
        }
        else {
            holder.TextViewExitoDesafio.setTextColor(context.getResources().getColor(R.color.colorRojo));
        }

        return row;
    }
}
