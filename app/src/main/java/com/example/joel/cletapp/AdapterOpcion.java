package com.example.joel.cletapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by Joel on 18/07/2015.
 */
public class AdapterOpcion extends ArrayAdapter<String> {

    Context context;
    int[] images;
    String[] options;

    AdapterOpcion(Context c, String[] opciones, int imgs[]) {
        super(c, R.layout.single_navegation_row, R.id.TextViewTextoOpcion, opciones);
        this.context = c;
        images = imgs;
        options = opciones;
    }

    @Override
    public boolean isEnabled(int position) {
        return super.isEnabled(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        AdapterNavigationRow holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_navegation_row, parent, false);
            holder = new AdapterNavigationRow(row);
            row.setTag(holder);
        } else {
            holder = (AdapterNavigationRow) row.getTag();
        }

        holder.myImageOption.setImageResource(images[position]);
        holder.myTextOption.setText(options[position]);

        return row;
    }
}
