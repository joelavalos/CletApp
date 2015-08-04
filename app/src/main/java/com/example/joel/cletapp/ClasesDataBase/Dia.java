package com.example.joel.cletapp.ClasesDataBase;

import java.io.Serializable;

/**
 * Created by Joel on 04/08/2015.
 */
public class Dia implements Serializable {
    String diaNombre;

    public Dia() {
        this.diaNombre = "";
    }

    public Dia(String diaNombre) {
        this.diaNombre = diaNombre;
    }

    public String getDiaNombre() {
        return diaNombre;
    }

    public void setDiaNombre(String diaNombre) {
        this.diaNombre = diaNombre;
    }
}
