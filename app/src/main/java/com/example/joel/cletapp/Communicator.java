package com.example.joel.cletapp;

import java.util.ArrayList;

/**
 * Created by Joel on 07/08/2015.
 */
public interface Communicator {
    public void Actualizar(String data);
    public void Eliminar(String data);
    public void Reiniciar(String data);
    public void TerminarRutina(String data);
    public void DiasSeleccionados(ArrayList<Integer> data);
    public void SeleccionarRuta(String data);
    public void GuardarRuta(String data);
    public void OcultarDetalle(String data);
    public void pruebaDialogToDialog(String[] desafios, String[] valores, String[] nombres, String[] objetivos);
}
