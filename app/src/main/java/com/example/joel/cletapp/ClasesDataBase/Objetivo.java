package com.example.joel.cletapp.ClasesDataBase;

import java.io.Serializable;

/**
 * Created by Joel on 28/07/2015.
 */
public class Objetivo implements Serializable {
    private long objetivoId;
    private String objetivoNombre;
    private String objetivoDescripcion;

    public Objetivo() {
    }

    public Objetivo(long objetivoId, String objetivoNombre, String objetivoDescripcion) {
        this.objetivoId = objetivoId;
        this.objetivoNombre = objetivoNombre;
        this.objetivoDescripcion = objetivoDescripcion;
    }

    public long getObjetivoId() {
        return objetivoId;
    }

    public void setObjetivoId(long objetivoId) {
        this.objetivoId = objetivoId;
    }

    public String getObjetivoNombre() {
        return objetivoNombre;
    }

    public void setObjetivoNombre(String objetivoNombre) {
        this.objetivoNombre = objetivoNombre;
    }

    public String getObjetivoDescripcion() {
        return objetivoDescripcion;
    }

    public void setObjetivoDescripcion(String objetivoDescripcion) {
        this.objetivoDescripcion = objetivoDescripcion;
    }
}
