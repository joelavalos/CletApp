package com.example.joel.cletapp.ClasesDataBase;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Joel on 04/08/2015.
 */
public class Rutina implements Serializable {
    private long rutinaId;
    private String rutinaNombre;
    private String rutinaDescripcion;
    private Date rutinaInicio;
    private Date rutinaTermino;
    private char rutinaEstado;
    private Resumen resumen;

    public Rutina() {
        this.rutinaId = -1;
    }

    public Rutina(long rutinaId, String rutinaNombre, String rutinaDescripcion, Date rutinaInicio, Date rutinaTermino, char rutinaEstado, Resumen resumen) {
        this.rutinaId = rutinaId;
        this.rutinaNombre = rutinaNombre;
        this.rutinaDescripcion = rutinaDescripcion;
        this.rutinaInicio = rutinaInicio;
        this.rutinaTermino = rutinaTermino;
        this.rutinaEstado = rutinaEstado;
        this.resumen = resumen;
    }

    public long getRutinaId() {
        return rutinaId;
    }

    public void setRutinaId(long rutinaId) {
        this.rutinaId = rutinaId;
    }

    public String getRutinaNombre() {
        return rutinaNombre;
    }

    public void setRutinaNombre(String rutinaNombre) {
        this.rutinaNombre = rutinaNombre;
    }

    public String getRutinaDescripcion() {
        return rutinaDescripcion;
    }

    public void setRutinaDescripcion(String rutinaDescripcion) {
        this.rutinaDescripcion = rutinaDescripcion;
    }

    public Date getRutinaInicio() {
        return rutinaInicio;
    }

    public void setRutinaInicio(Date rutinaInicio) {
        this.rutinaInicio = rutinaInicio;
    }

    public Date getRutinaTermino() {
        return rutinaTermino;
    }

    public void setRutinaTermino(Date rutinaTermino) {
        this.rutinaTermino = rutinaTermino;
    }

    public char getRutinaEstado() {
        return rutinaEstado;
    }

    public void setRutinaEstado(char rutinaEstado) {
        this.rutinaEstado = rutinaEstado;
    }

    public Resumen getResumen() {
        return resumen;
    }

    public void setResumen(Resumen resumen) {
        this.resumen = resumen;
    }
}
