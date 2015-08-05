package com.example.joel.cletapp.ClasesDataBase;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Joel on 05/08/2015.
 */
public class DesafioRutina implements Serializable {
    private long desafioRutinaId;
    private Rutina rutina;
    private Desafio desafio;
    private Date fecha;

    public DesafioRutina() {
        this.desafioRutinaId = -1;
    }

    public DesafioRutina(long desafioRutinaId, Rutina rutina, Desafio desafio, Date fecha) {
        this.desafioRutinaId = desafioRutinaId;
        this.rutina = rutina;
        this.desafio = desafio;
        this.fecha = fecha;
    }

    public long getDesafioRutinaId() {
        return desafioRutinaId;
    }

    public void setDesafioRutinaId(long desafioRutinaId) {
        this.desafioRutinaId = desafioRutinaId;
    }

    public Rutina getRutina() {
        return rutina;
    }

    public void setRutina(Rutina rutina) {
        this.rutina = rutina;
    }

    public Desafio getDesafio() {
        return desafio;
    }

    public void setDesafio(Desafio desafio) {
        this.desafio = desafio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
