package com.example.joel.cletapp.ClasesDataBase;

import java.io.Serializable;

/**
 * Created by Joel on 28/10/2015.
 */
public class Serie implements Serializable {
    private long serieId;
    private Desafio desafio;

    public Serie() {
    }

    public Serie(long serieId, Desafio desafio) {
        this.serieId = serieId;
        this.desafio = desafio;
    }

    public long getSerieId() {
        return serieId;
    }

    public void setSerieId(long serieId) {
        this.serieId = serieId;
    }

    public Desafio getDesafio() {
        return desafio;
    }

    public void setDesafio(Desafio desafio) {
        this.desafio = desafio;
    }
}
