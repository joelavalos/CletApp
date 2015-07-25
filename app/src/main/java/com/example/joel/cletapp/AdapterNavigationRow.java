package com.example.joel.cletapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Joel on 18/07/2015.
 */
public class AdapterNavigationRow {

    TextView myTextOption;
    ImageView myImageOption;

    AdapterNavigationRow(View v){
        myImageOption = (ImageView) v.findViewById(R.id.ImageViewImagenOpcion);
        myTextOption = (TextView) v.findViewById(R.id.TextViewTextoOpcion);
    }
}
