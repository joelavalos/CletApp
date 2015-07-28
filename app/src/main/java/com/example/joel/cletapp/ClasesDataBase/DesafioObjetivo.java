package com.example.joel.cletapp.ClasesDataBase;

import java.io.Serializable;

/**
 * Created by Joel on 28/07/2015.
 */
public class DesafioObjetivo implements Serializable {
    private long desObjId;
    private Desafio desafio;
    private Objetivo objetivo;
    private float valor;

    public DesafioObjetivo() {
    }

    public DesafioObjetivo(long desObjId, float valor) {
        this.desObjId = desObjId;
        this.valor = valor;
    }

    public DesafioObjetivo(long desObjId, Desafio desafio, Objetivo objetivo, float valor) {
        this.desObjId = desObjId;
        this.desafio = desafio;
        this.objetivo = objetivo;
        this.valor = valor;
    }

    public long getDesObjId() {
        return desObjId;
    }

    public void setDesObjId(long desObjId) {
        this.desObjId = desObjId;
    }

    public Desafio getDesafio() {
        return desafio;
    }

    public void setDesafio(Desafio desafio) {
        this.desafio = desafio;
    }

    public Objetivo getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(Objetivo objetivo) {
        this.objetivo = objetivo;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }
}
