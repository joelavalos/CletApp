package com.example.joel.cletapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joel.cletapp.ActivityRutinaOpciones;
import com.example.joel.cletapp.CRUDDatabase.DesafioCRUD;
import com.example.joel.cletapp.CRUDDatabase.DesafioObjetivoCRUD;
import com.example.joel.cletapp.CRUDDatabase.DesafioRutinaCRUD;
import com.example.joel.cletapp.CRUDDatabase.ResumenCRUD;
import com.example.joel.cletapp.CRUDDatabase.RutinaCRUD;
import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.ClasesDataBase.DesafioObjetivo;
import com.example.joel.cletapp.ClasesDataBase.DesafioRutina;
import com.example.joel.cletapp.ClasesDataBase.Rutina;
import com.example.joel.cletapp.HeartRateMonitor;
import com.example.joel.cletapp.Mensaje;
import com.example.joel.cletapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Joel on 21/07/2015.
 */
public class MainFragment extends Fragment {
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private boolean encontrado = false;
    private boolean nextDesafio = false;
    private String diff = "";
    private String fechaActual = "";

    private Button ButtonFrecuencioa;
    private Button ButtonIniciarRutina;
    private Button ButtonDetenerRutina;
    private ImageView ButtonIniciarDesafio;

    private ImageView ImageViewImagenDesafio2, ImageViewImagenDesafioActual;
    private TextView TextViewNombreDesafio, TextViewNombreDesafioActual;
    private TextView TextViewCategoriaDesafio, TextViewCategoriaDesafioActual;
    private TextView TextViewValorDesafio, TextViewValorDesafioActual;
    private TextView TextViewNotaDesafio, TextViewNotaDesafioActual;
    private TextView TextViewFechaDesafio, TextViewFechaDesafioActual;
    private TextView TextViewEstado, TextViewEstadoActual;

    private TextView TextViewElegirRutina;
    private TextView TextViewElegirDesafioActual;

    private RutinaCRUD rutinaCRUD;
    private DesafioRutinaCRUD desafioRutinaCRUD;
    private DesafioObjetivoCRUD desafioObjetivoCRUD;
    private DesafioObjetivo desafioObjetivo;
    private Desafio actualDesafio;
    private List<Rutina> listaRutinasIniciadas;
    private List<DesafioRutina> listaDesafiosRutina;
    private Rutina actualRutina;
    private DesafioCRUD desafioCRUD;
    private ResumenCRUD resumenCRUD;

    private List<Rutina> todasLasRutinas;
    private List<DesafioRutina> todosLosDesafiosRutinasTemporal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("CletApp");
        ((ActionBarActivity) getActivity()).getSupportActionBar().setIcon(R.drawable.ic_directions_bike_white_18dp);

        inicializarBaseDeDatos();
        inicializarComponentes(root);

        TextViewElegirRutina.setOnClickListener(new View.OnClickListener() {
            Bundle bundle = new Bundle();

            @Override
            public void onClick(View v) {

                DialogoRutinaSelector dialogo = new DialogoRutinaSelector();
                dialogo.setArguments(bundle);
                dialogo.show(getFragmentManager(), "categoriaPicker");
            }
        });

        ButtonFrecuencioa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getActivity().getApplicationContext(), HeartRateMonitor.class);
                startActivity(newIntent);
            }
        });

        ButtonIniciarRutina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ButtonIniciarRutina.isEnabled()) {

                    ButtonIniciarRutina.setEnabled(false);
                    ButtonDetenerRutina.setEnabled(true);
                    TextViewElegirRutina.setEnabled(false);
                    TextViewEstado.setText("Iniciada");
                    TextViewEstado.setTextColor(getResources().getColor(R.color.colorVerde));

                    actualRutina.setRutinaEstado('I');
                    for (int i = 0; i < listaDesafiosRutina.size(); i++) {
                        listaDesafiosRutina.get(i).getDesafio().setEstadoDesafio('I');

                        try {
                            desafioCRUD.actualizarDatosDesafio(listaDesafiosRutina.get(i).getDesafio());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        rutinaCRUD.actualizarRutina(actualRutina);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    todasLasRutinas = rutinaCRUD.buscarTodasLasRutinasPendientes();

                    for (int i = 0; i < todasLasRutinas.size(); i++) {
                        try {
                            todosLosDesafiosRutinasTemporal = desafioRutinaCRUD.buscarDesafioRutinaPorIdRutina(todasLasRutinas.get(i));

                            for (int j = 0; j < todosLosDesafiosRutinasTemporal.size(); j++) {
                                if (todosLosDesafiosRutinasTemporal.get(j).getDesafio().getEstadoDesafio() == 'I') {
                                    resumenCRUD.eliminarResumen(todasLasRutinas.get(i).getResumen());
                                    rutinaCRUD.eliminarRutina(todasLasRutinas.get(i));
                                    j = todosLosDesafiosRutinasTemporal.size();
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    if (!TextViewNombreDesafioActual.getText().toString().equals("Descansar")) {
                        ButtonIniciarDesafio.setEnabled(true);
                    }
                }
            }
        });

        ButtonDetenerRutina.setOnClickListener(new View.OnClickListener() {
            Bundle bundle = new Bundle();

            @Override
            public void onClick(View v) {
                bundle.putString("Accion", "DetenerRutina");
                bundle.putString("Mensaje", "Detener la rutina forzara la evaluacion con el progreso actual");
                bundle.putString("Titulo", "Detener rutina");

                DialogoConfirmacion dialogo = new DialogoConfirmacion();
                dialogo.setArguments(bundle);
                dialogo.show(getFragmentManager(), "categoriaPicker");
            }
        });

        ButtonIniciarDesafio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actualDesafio.getEstadoDesafio() == 'I') {
                    actualDesafio.setEstadoDesafio('T');
                    actualDesafio.setExitoDesafio(true);
                    try {
                        desafioCRUD.actualizarDatosDesafio(actualDesafio);
                        TextViewEstadoActual.setText("Terminado");
                        TextViewEstadoActual.setTextColor(getResources().getColor(R.color.colorVerde));
                        ButtonIniciarDesafio.setEnabled(false);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return root;
    }

    private void inicializarBaseDeDatos() {
        desafioCRUD = new DesafioCRUD(getActivity().getApplicationContext());
        resumenCRUD = new ResumenCRUD(getActivity().getApplicationContext());
        rutinaCRUD = new RutinaCRUD(getActivity().getApplicationContext());
        desafioRutinaCRUD = new DesafioRutinaCRUD(getActivity().getApplicationContext());
        desafioObjetivoCRUD = new DesafioObjetivoCRUD(getActivity().getApplicationContext());
        listaRutinasIniciadas = new ArrayList<>();
        listaDesafiosRutina = new ArrayList<>();
        todasLasRutinas = new ArrayList<>();
        todosLosDesafiosRutinasTemporal = new ArrayList<>();

        listaRutinasIniciadas = rutinaCRUD.buscarTodasLasRutinasIniciadas();
    }

    private void inicializarComponentes(View root) {
        ButtonFrecuencioa = (Button) root.findViewById(R.id.ButtonFrecuencioa);
        ButtonIniciarRutina = (Button) root.findViewById(R.id.ButtonIniciarRutina);
        ButtonDetenerRutina = (Button) root.findViewById(R.id.ButtonDetenerRutina);
        ButtonIniciarDesafio = (ImageView) root.findViewById(R.id.ButtonIniciarDesafio);

        ButtonIniciarDesafio.setEnabled(false);
        ButtonIniciarRutina.setEnabled(false);

        ImageViewImagenDesafio2 = (ImageView) root.findViewById(R.id.ImageViewImagenDesafio2);
        ImageViewImagenDesafioActual = (ImageView) root.findViewById(R.id.ImageViewImagenDesafioActual);

        TextViewNombreDesafio = (TextView) root.findViewById(R.id.TextViewNombreDesafio);
        TextViewNombreDesafioActual = (TextView) root.findViewById(R.id.TextViewNombreDesafioActual);

        TextViewCategoriaDesafio = (TextView) root.findViewById(R.id.TextViewCategoriaDesafio);
        TextViewCategoriaDesafioActual = (TextView) root.findViewById(R.id.TextViewCategoriaDesafioActual);

        TextViewValorDesafio = (TextView) root.findViewById(R.id.TextViewValorDesafio);
        TextViewValorDesafioActual = (TextView) root.findViewById(R.id.TextViewValorDesafioActual);

        TextViewNotaDesafio = (TextView) root.findViewById(R.id.TextViewNotaDesafio);
        TextViewNotaDesafioActual = (TextView) root.findViewById(R.id.TextViewNotaDesafioActual);

        TextViewFechaDesafio = (TextView) root.findViewById(R.id.TextViewFechaDesafio);
        TextViewFechaDesafioActual = (TextView) root.findViewById(R.id.TextViewFechaDesafioActual);

        TextViewEstado = (TextView) root.findViewById(R.id.TextViewEstado);
        TextViewEstadoActual = (TextView) root.findViewById(R.id.TextViewEstadoActual);

        TextViewElegirRutina = (TextView) root.findViewById(R.id.TextViewElegirRutina);
        TextViewElegirDesafioActual = (TextView) root.findViewById(R.id.TextViewElegirDesafioActual);

        cambiarVisibilidadRutina(View.INVISIBLE);
        cambiarVisibilidadDesafioActual(View.INVISIBLE);

        if (listaRutinasIniciadas.isEmpty()) {

        } else {
            Calendar cInicial = Calendar.getInstance();
            //cInicial.add(Calendar.DATE, +6);
            Date actual = cInicial.getTime();
            String fechaDesafioTermino = format.format(listaRutinasIniciadas.get(0).getRutinaTermino());
            fechaActual = format.format(actual);

            new Mensaje(getActivity().getApplicationContext(), "Rutina en curso");
            actualRutina = listaRutinasIniciadas.get(0);
            TextViewElegirRutina.setText("");
            TextViewElegirRutina.setEnabled(false);
            TextViewElegirDesafioActual.setText("");

            ButtonDetenerRutina.setEnabled(true);

            try {
                listaDesafiosRutina = desafioRutinaCRUD.buscarDesafioRutinaPorIdRutinaOrderByFecha(actualRutina);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            cargarDatosRutina(R.drawable.ic_directions_bike_black_48dp, actualRutina.getRutinaNombre(), "Desafios", String.valueOf(listaDesafiosRutina.size()), actualRutina.getRutinaDescripcion(),
                    "Desde: " + format.format(actualRutina.getRutinaInicio()) + " hasta: " + format.format(actualRutina.getRutinaTermino()),
                    "Iniciada");

            try {
                listaDesafiosRutina = desafioRutinaCRUD.buscarDesafioRutinaPorIdRutinaOrderByFecha(actualRutina);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            cargarDesafio(String.valueOf(R.drawable.ic_grade_black_48dp));

            if (actual.after(listaRutinasIniciadas.get(0).getRutinaTermino())) {
                new Mensaje(getActivity().getApplicationContext(), "Rutina terminada");

                Bundle bundle = new Bundle();
                bundle.putString("Accion", "RutinaTerminada");
                bundle.putString("Mensaje", "Desea ver las resultados?");
                bundle.putString("Titulo", "Rutina terminada");

                DialogoConfirmacion dialogo = new DialogoConfirmacion();
                dialogo.setArguments(bundle);
                dialogo.show(getFragmentManager(), "categoriaPicker");
            } else {

            }
        }
    }

    public void actualizarDatos(String data) {

        if (data.equals("")) {

        } else {
            TextViewElegirRutina.setText("");
            TextViewElegirDesafioActual.setText("");
            ButtonIniciarRutina.setEnabled(true);

            cambiarVisibilidadRutina(View.VISIBLE);
            cargarDatosRutina(Integer.parseInt(data.split("-")[1]), data.split("-")[2], data.split("-")[3], data.split("-")[4], data.split("-")[5], data.split("-")[6], data.split("-")[7]);

            try {
                actualRutina = rutinaCRUD.buscarRutinaPorId(Long.parseLong(data.split("-")[0]));
                listaDesafiosRutina = desafioRutinaCRUD.buscarDesafioRutinaPorIdRutinaOrderByFecha(actualRutina);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            cargarDesafio(data.split("-")[1]);
        }
    }

    private void cargarDesafio(String imagen) {
        Calendar cInicial = Calendar.getInstance();
        Calendar cInicialRev = Calendar.getInstance();

        cInicialRev.add(Calendar.DATE, -1);

        //cInicial.add(Calendar.DATE, +2);
        //cInicialRev.add(Calendar.DATE, +2);

        Date actual = cInicial.getTime();
        Date actualRev = cInicialRev.getTime();

        String fechaDesafio = format.format(listaDesafiosRutina.get(0).getFecha());
        String fechaActual = format.format(actual);

        for (int i = 0; i < listaDesafiosRutina.size(); i++) {
            String fechaDesafioTemporal = format.format(listaDesafiosRutina.get(i).getFecha());
            if (fechaActual.equals(fechaDesafioTemporal)) {
                actualDesafio = listaDesafiosRutina.get(i).getDesafio();
                encontrado = true;
            }
        }

        actualizarEstadoDesafios(actualRev);

        if (encontrado == false) {
            for (int i = 0; i < listaDesafiosRutina.size(); i++) {
                if (actual.before(listaDesafiosRutina.get(i).getFecha()) && nextDesafio == false) {
                    new Mensaje(getActivity().getApplicationContext(), "Aca toy");
                    fechaDesafio = format.format(listaDesafiosRutina.get(i).getFecha());
                    nextDesafio = true;
                }
            }
        }

        if (encontrado == false && nextDesafio == false) {
            diff = "No quedan desafios";
        }

        if (encontrado == false && nextDesafio == true) {
            try {
                if (TimeUnit.DAYS.convert(format.parse(fechaDesafio).getTime() - format.parse(fechaActual).getTime(), TimeUnit.MILLISECONDS) == 1) {
                    diff = "Proximo desafio en: " + String.valueOf(TimeUnit.DAYS.convert(format.parse(fechaDesafio).getTime() - format.parse(fechaActual).getTime(), TimeUnit.MILLISECONDS)) + " dia";
                } else {
                    diff = "Proximo desafio en: " + String.valueOf(TimeUnit.DAYS.convert(format.parse(fechaDesafio).getTime() - format.parse(fechaActual).getTime(), TimeUnit.MILLISECONDS)) + " dias";
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        nextDesafio = false;

        if (encontrado == true) {
            try {
                desafioObjetivo = desafioObjetivoCRUD.buscarDesafioObjetivoPorIdDesafio(actualDesafio);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String estadoDesafioCargado = "Pendiente";
            if (actualDesafio.getEstadoDesafio() == 'I') {
                estadoDesafioCargado = "Pendiente";
            } else if (actualDesafio.getEstadoDesafio() == 'T') {
                estadoDesafioCargado = "Terminado";
            }
            cambiarVisibilidadDesafioActual(View.VISIBLE);
            cargarDatosDesafioActual(Integer.parseInt(imagen), actualDesafio.getDesafioNombre(), desafioObjetivo.getObjetivo().getObjetivoNombre(), String.valueOf(Math.round(desafioObjetivo.getValor())),
                    actualDesafio.getDesafioDescripcion(),
                    format.format(actual),
                    estadoDesafioCargado);

            if (!listaRutinasIniciadas.isEmpty()) {
                ButtonIniciarDesafio.setEnabled(true);
            }

            if (estadoDesafioCargado.equals("Terminado")) {
                ButtonIniciarDesafio.setEnabled(false);
            }

            encontrado = false;

        } else {
            cambiarVisibilidadDesafioActual(View.VISIBLE);
            cargarDatosDesafioActual(Integer.parseInt(imagen), "Descansar", "", "", "", format.format(actual), diff);

            ButtonIniciarDesafio.setEnabled(false);
        }
    }

    private void actualizarEstadoDesafios(Date actualRev) {
        for (int i = 0; i < listaDesafiosRutina.size(); i++) {
            if (listaDesafiosRutina.get(i).getDesafio().getEstadoDesafio() == 'I') {
                if (listaDesafiosRutina.get(i).getFecha().before(actualRev)) {
                    listaDesafiosRutina.get(i).getDesafio().setEstadoDesafio('T');
                    listaDesafiosRutina.get(i).getDesafio().setExitoDesafio(false);
                    try {
                        desafioCRUD.actualizarDatosDesafio(listaDesafiosRutina.get(i).getDesafio());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void cambiarVisibilidadRutina(int visible) {
        ImageViewImagenDesafio2.setVisibility(visible);
        TextViewNombreDesafio.setVisibility(visible);
        TextViewCategoriaDesafio.setVisibility(visible);
        TextViewValorDesafio.setVisibility(visible);
        TextViewNotaDesafio.setVisibility(visible);
        TextViewFechaDesafio.setVisibility(visible);
        TextViewEstado.setVisibility(visible);
    }

    private void cambiarVisibilidadDesafioActual(int visible) {
        ImageViewImagenDesafioActual.setVisibility(visible);
        TextViewNombreDesafioActual.setVisibility(visible);
        TextViewCategoriaDesafioActual.setVisibility(visible);
        TextViewValorDesafioActual.setVisibility(visible);
        TextViewNotaDesafioActual.setVisibility(visible);
        TextViewFechaDesafioActual.setVisibility(visible);
        TextViewEstadoActual.setVisibility(visible);
    }

    private void cargarDatosDesafioActual(int ic_grade_black_48dp, String desafioNombre, String objetivoNombre, String valorDesafio, String desafioDescripcion, String fecha, String estado) {
        ImageViewImagenDesafioActual.setImageResource(ic_grade_black_48dp);
        TextViewNombreDesafioActual.setText(desafioNombre);
        TextViewCategoriaDesafioActual.setText(objetivoNombre);
        TextViewValorDesafioActual.setText(valorDesafio);
        TextViewNotaDesafioActual.setText(desafioDescripcion);
        TextViewFechaDesafioActual.setText(fecha);
        TextViewEstadoActual.setText(estado);

        if (estado.equals("Terminado")) {
            TextViewEstadoActual.setTextColor(getResources().getColor(R.color.colorVerde));
        }
    }

    private void cargarDatosRutina(int imagen, String rutinaNombre, String desafios, String valorDesafio, String rutinaDescripcion, String fecha, String estado) {
        cambiarVisibilidadRutina(View.VISIBLE);
        ImageViewImagenDesafio2.setImageResource(imagen);
        TextViewNombreDesafio.setText(rutinaNombre);
        TextViewCategoriaDesafio.setText(desafios);
        TextViewValorDesafio.setText(valorDesafio);
        TextViewNotaDesafio.setText(rutinaDescripcion);
        TextViewFechaDesafio.setText(fecha);

        if (estado.equals("Iniciada")) {
            TextViewEstado.setTextColor(getResources().getColor(R.color.colorVerde));
        } else {
            TextViewEstado.setTextColor(getResources().getColor(R.color.colorMorado));
        }
        TextViewEstado.setText(estado);
    }

    public void terminarRutina(String data) {
        actualRutina.setRutinaEstado('T');
        cambiarVisibilidadDesafioActual(View.INVISIBLE);
        cambiarVisibilidadRutina(View.INVISIBLE);
        TextViewElegirRutina.setText("Seleccionar rutina");
        TextViewElegirRutina.setEnabled(true);
        TextViewElegirDesafioActual.setText("Desafio actual");
        ButtonDetenerRutina.setEnabled(false);

        try {
            rutinaCRUD.actualizarRutina(actualRutina);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < listaDesafiosRutina.size(); i++) {
            listaDesafiosRutina.get(i).getDesafio().setEstadoDesafio('T');
            listaDesafiosRutina.get(i).getDesafio().setExitoDesafio(true);

            try {
                desafioCRUD.actualizarDatosDesafio(listaDesafiosRutina.get(i).getDesafio());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (data.equals("Aceptar")) {
            String idRutina = String.valueOf(listaRutinasIniciadas.get(0).getRutinaId());
            Intent newIntent = new Intent(getActivity().getApplicationContext(), ActivityRutinaOpciones.class);
            newIntent.putExtra("idRutina", idRutina);
            startActivity(newIntent);
        } else {

        }
    }

    public void detenerRutina(String data) {
        if (data.equals("Aceptar")) {
            actualRutina.setRutinaEstado('T');
            cambiarVisibilidadDesafioActual(View.INVISIBLE);
            cambiarVisibilidadRutina(View.INVISIBLE);
            TextViewElegirRutina.setText("Seleccionar rutina");
            TextViewElegirRutina.setEnabled(true);
            TextViewElegirDesafioActual.setText("Desafio actual");
            ButtonDetenerRutina.setEnabled(false);

            try {
                rutinaCRUD.actualizarRutina(actualRutina);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < listaDesafiosRutina.size(); i++) {
                listaDesafiosRutina.get(i).getDesafio().setEstadoDesafio('T');
                listaDesafiosRutina.get(i).getDesafio().setExitoDesafio(false);

                try {
                    desafioCRUD.actualizarDatosDesafio(listaDesafiosRutina.get(i).getDesafio());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            String idRutina = String.valueOf(actualRutina.getRutinaId());
            Intent newIntent = new Intent(getActivity().getApplicationContext(), ActivityRutinaOpciones.class);
            newIntent.putExtra("idRutina", idRutina);
            startActivity(newIntent);
        } else {
        }
    }
}
