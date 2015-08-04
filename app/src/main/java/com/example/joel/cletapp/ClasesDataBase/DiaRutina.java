package com.example.joel.cletapp.ClasesDataBase;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Joel on 04/08/2015.
 */
public class DiaRutina implements Serializable {
    private long diaRutinaId;
    private Rutina rutina;
    private Dia dia;
    private Date fecha;

    public DiaRutina() {
        this.diaRutinaId = -1;
    }

    public DiaRutina(long diaRutinaId, Rutina rutina, Dia dia, Date fecha) {
        this.diaRutinaId = diaRutinaId;
        this.rutina = rutina;
        this.dia = dia;
        this.fecha = fecha;
    }

    public long getDiaRutinaId() {
        return diaRutinaId;
    }

    public void setDiaRutinaId(long diaRutinaId) {
        this.diaRutinaId = diaRutinaId;
    }

    public Rutina getRutina() {
        return rutina;
    }

    public void setRutina(Rutina rutina) {
        this.rutina = rutina;
    }

    public Dia getDia() {
        return dia;
    }

    public void setDia(Dia dia) {
        this.dia = dia;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
