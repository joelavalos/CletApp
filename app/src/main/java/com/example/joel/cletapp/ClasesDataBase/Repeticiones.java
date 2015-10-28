package com.example.joel.cletapp.ClasesDataBase;

import java.io.Serializable;

/**
 * Created by Joel on 28/10/2015.
 */
public class Repeticiones implements Serializable {
    private long repeticionId;
    private Serie serie;
    private float valor;

    public Repeticiones() {
    }

    public Repeticiones(long repeticionId, Serie serie, float valor) {
        this.repeticionId = repeticionId;
        this.serie = serie;
        this.valor = valor;
    }

    public long getRepeticionId() {
        return repeticionId;
    }

    public void setRepeticionId(long repeticionId) {
        this.repeticionId = repeticionId;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }
}
