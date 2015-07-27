package com.example.joel.cletapp.ClasesDataBase;

import java.io.Serializable;
import java.sql.Date;


/**
 * Created by Joel on 27/07/2015.
 */
public class Ciclista implements Serializable {
    private String ciclistRut;
    private String ciclistaNombre;
    private String ciclistaApellido;
    private Date ciclistaFechaNacimiento;
    private float ciclistaPeso;
    private float ciclistaAltura;
    private char ciclistaSexo;

    public Ciclista() {
        this.ciclistRut = "null";
    }

    public Ciclista(String ciclistRut, String ciclistaNombre, String ciclistaApellido, Date ciclistaFechaNacimiento, float ciclistaPeso, float ciclistaAltura, char ciclistaSexo) {
        this.ciclistRut = ciclistRut;
        this.ciclistaNombre = ciclistaNombre;
        this.ciclistaApellido = ciclistaApellido;
        this.ciclistaFechaNacimiento = ciclistaFechaNacimiento;
        this.ciclistaPeso = ciclistaPeso;
        this.ciclistaAltura = ciclistaAltura;
        this.ciclistaSexo = ciclistaSexo;
    }

    public String getCiclistRut() {
        return ciclistRut;
    }

    public void setCiclistRut(String ciclistRut) {
        this.ciclistRut = ciclistRut;
    }

    public String getCiclistaNombre() {
        return ciclistaNombre;
    }

    public void setCiclistaNombre(String ciclistaNombre) {
        this.ciclistaNombre = ciclistaNombre;
    }

    public String getCiclistaApellido() {
        return ciclistaApellido;
    }

    public void setCiclistaApellido(String ciclistaApellido) {
        this.ciclistaApellido = ciclistaApellido;
    }

    public Date getCiclistaFechaNacimiento() {
        return ciclistaFechaNacimiento;
    }

    public void setCiclistaFechaNacimiento(Date ciclistaFechaNacimiento) {
        this.ciclistaFechaNacimiento = ciclistaFechaNacimiento;
    }

    public float getCiclistaAltura() {
        return ciclistaAltura;
    }

    public void setCiclistaAltura(float ciclistaAltura) {
        this.ciclistaAltura = ciclistaAltura;
    }

    public float getCiclistaPeso() {
        return ciclistaPeso;
    }

    public void setCiclistaPeso(float ciclistaPeso) {
        this.ciclistaPeso = ciclistaPeso;
    }

    public char getCiclistaSexo() {
        return ciclistaSexo;
    }

    public void setCiclistaSexo(char ciclistaSexo) {
        this.ciclistaSexo = ciclistaSexo;
    }
}
