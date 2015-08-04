package com.example.joel.cletapp.ClasesDataBase;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Joel on 31/07/2015.
 */
public class Resumen implements Serializable {
    private long resumenId;
    private String resumenAnalisis;
    private Date resumenFecha;

    public Resumen() {
        this.resumenId = -1;
    }

    public Resumen(long resumenId, String resumenAnalisis, Date resumenFecha) {
        this.resumenId = resumenId;
        this.resumenAnalisis = resumenAnalisis;
        this.resumenFecha = resumenFecha;
    }

    public long getResumenId() {
        return resumenId;
    }

    public void setResumenId(long resumenId) {
        this.resumenId = resumenId;
    }

    public String getResumenAnalisis() {
        return resumenAnalisis;
    }

    public void setResumenAnalisis(String resumenAnalisis) {
        this.resumenAnalisis = resumenAnalisis;
    }

    public Date getResumenFecha() {
        return resumenFecha;
    }

    public void setResumenFecha(Date resumenFecha) {
        this.resumenFecha = resumenFecha;
    }
}
