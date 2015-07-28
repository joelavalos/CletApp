package com.example.joel.cletapp.ClasesDataBase;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Joel on 27/07/2015.
 */
public class Desafio implements Serializable {
    private long desafioId;
    private String desafioNombre;
    private String desafioDescripcion;
    private Date inicioDesafio;
    private Date terminoDesafio;
    private char estadoDesafio;
    private boolean exitoDesafio;

    public Desafio() {
        this.desafioId = -1;
    }

    public Desafio(long desafioId, String desafioNombre, String desafioDescripcion, Date inicioDesafio, Date terminoDesafio, char estadoDesafio, boolean exitoDesafio) {
        this.desafioId = desafioId;
        this.desafioNombre = desafioNombre;
        this.desafioDescripcion = desafioDescripcion;
        this.inicioDesafio = inicioDesafio;
        this.terminoDesafio = terminoDesafio;
        this.estadoDesafio = estadoDesafio;
        this.exitoDesafio = exitoDesafio;
    }

    public long getDesafioId() {
        return desafioId;
    }

    public void setDesafioId(long desafioId) {
        this.desafioId = desafioId;
    }

    public String getDesafioNombre() {
        return desafioNombre;
    }

    public void setDesafioNombre(String desafioNombre) {
        this.desafioNombre = desafioNombre;
    }

    public String getDesafioDescripcion() {
        return desafioDescripcion;
    }

    public void setDesafioDescripcion(String desafioDescripcion) {
        this.desafioDescripcion = desafioDescripcion;
    }

    public Date getInicioDesafio() {
        return inicioDesafio;
    }

    public void setInicioDesafio(Date inicioDesafio) {
        this.inicioDesafio = inicioDesafio;
    }

    public Date getTerminoDesafio() {
        return terminoDesafio;
    }

    public void setTerminoDesafio(Date terminoDesafio) {
        this.terminoDesafio = terminoDesafio;
    }

    public char getEstadoDesafio() {
        return estadoDesafio;
    }

    public void setEstadoDesafio(char estadoDesafio) {
        this.estadoDesafio = estadoDesafio;
    }

    public boolean getExitoDesafio() {
        return exitoDesafio;
    }

    public void setExitoDesafio(boolean exitoDesafio) {
        this.exitoDesafio = exitoDesafio;
    }
}
