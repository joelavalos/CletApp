package com.example.joel.cletapp.ClasesDataBase;

/**
 * Created by Joel on 24/11/2015.
 */
public class Ruta {
    private long rutaId;
    private String rutaNombre;
    private String rutaCordenadas;

    public Ruta() {
        this.rutaId = -1;
    }

    public Ruta(long rutaId, String rutaNombre, String rutaCordenadas) {
        this.rutaId = rutaId;
        this.rutaNombre = rutaNombre;
        this.rutaCordenadas = rutaCordenadas;
    }

    public long getRutaId() {
        return rutaId;
    }

    public void setRutaId(long rutaId) {
        this.rutaId = rutaId;
    }

    public String getRutaNombre() {
        return rutaNombre;
    }

    public void setRutaNombre(String rutaNombre) {
        this.rutaNombre = rutaNombre;
    }

    public String getRutaCordenadas() {
        return rutaCordenadas;
    }

    public void setRutaCordenadas(String rutaCordenadas) {
        this.rutaCordenadas = rutaCordenadas;
    }
}
