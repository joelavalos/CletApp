package com.example.joel.cletapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.joel.cletapp.ActivityDesafioTerminado;
import com.example.joel.cletapp.ActivityRutinaOpciones;
import com.example.joel.cletapp.CRUDDatabase.DesafioCRUD;
import com.example.joel.cletapp.CRUDDatabase.DesafioObjetivoCRUD;
import com.example.joel.cletapp.CRUDDatabase.DesafioRutinaCRUD;
import com.example.joel.cletapp.CRUDDatabase.ObjetivoCRUD;
import com.example.joel.cletapp.CRUDDatabase.ResumenCRUD;
import com.example.joel.cletapp.CRUDDatabase.RutinaCRUD;
import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.ClasesDataBase.DesafioObjetivo;
import com.example.joel.cletapp.ClasesDataBase.DesafioRutina;
import com.example.joel.cletapp.ClasesDataBase.Objetivo;
import com.example.joel.cletapp.ClasesDataBase.Resumen;
import com.example.joel.cletapp.ClasesDataBase.Rutina;
import com.example.joel.cletapp.HeartRateMonitor;
import com.example.joel.cletapp.Mensaje;
import com.example.joel.cletapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Joel on 21/07/2015.
 */
public class MainFragment extends Fragment {

    GoogleMap googleMap;
    MapView mapView;
    private LatLng nuevaCordenada;
    private PolylineOptions options;
    private Polyline line;

    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private boolean encontrado = false;
    private boolean nextDesafio = false;
    private String diff = "";
    private String fechaActual = "";

    private Button ButtonFrecuencioa;
    private ImageButton ButtonIniciarRutina;
    private ImageButton ButtonDetenerRutina;
    private ImageButton ButtonIniciarDesafio;
    private ImageButton ButtonCrearRutinaFlash;
    private ImageButton ButtonDetenerDesafio;

    private TextView textoCronometro;
    private int totalSegundos = 0;
    private int segundos = 0;
    private int minutos = 0;
    private int horas = 0;
    private String segundosString;
    private String minutosString;
    private String horasString;
    public boolean estado = true;

    //Datos para el servicio
    public boolean pauseDesafioActivado = false;
    public int intValorCronometro = 0;

    private ImageView ImageViewImagenDesafio2, ImageViewImagenDesafioActual;
    private TextView TextViewNombreDesafio, TextViewNombreDesafioActual;
    private TextView TextViewCategoriaDesafio, TextViewCategoriaDesafioActual;
    private TextView TextViewValorDesafio, TextViewValorDesafioActual;
    private TextView TextViewNotaDesafio, TextViewNotaDesafioActual;
    private TextView TextViewFechaDesafio, TextViewFechaDesafioActual;
    private TextView TextViewEstado, TextViewEstadoActual, TextViewEstadoTerminado;
    private TextView TextViewSeries, TextViewRepeticiones;

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

    //Prueba
    public List<LatLng> cordenadasRuta;
    //Prueba

    //Pruebas para crear rutinas
    private ObjetivoCRUD objetivoCRUD;

    private String[] valores = {"", ""};
    private Date parsedInicio;
    private Date parsedFinal;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private String[] camposDesafios = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
    private String[] valoresDesafios = {"", "", "", "", "", "", ""};
    private Resumen newResumen;
    private Rutina newRutina;
    private List<Desafio> listaDesafios;
    private Desafio newDesafio;
    private List<String> fechasDesafios;
    private Date parsedDesafio;
    private DesafioRutina newDesafioRutina;
    private Calendar c;
    private ArrayList<Integer> diasSelecionados;
    private List<String> nuevosDesafios;
    //Fin de pruebas

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        mapView = (MapView) root.findViewById(R.id.mi_mapa);

        mapView.onCreate(savedInstanceState);
        googleMap = mapView.getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);

        View btnMyLocation = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(80,80); // size of button in dp
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.setMargins(0, 0, 20, 20);
        btnMyLocation.setLayoutParams(params);

        Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        LatLng coordinate = new LatLng(lat, lng);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 20));
        options = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);

        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("CletApp");
        ((ActionBarActivity) getActivity()).getSupportActionBar().setIcon(R.drawable.ic_directions_bike_white_18dp);

        inicializarBaseDeDatos();
        inicializarComponentes(root);

        Intent newIntent = getActivity().getIntent();
        if (newIntent.getStringExtra("cronometroFinal") == null) {
            //Si el desafio sigue en curso
        } else {
            textoCronometro.setText(newIntent.getStringExtra("cronometroFinal"));
            ButtonIniciarDesafio.setEnabled(false);
            guardarEstadoDesafioNoPause();
            guardarEstadoDesafioDetenido();
            completarDesafio();
        }

        if (cargarEstadoDesafio().equals("iniciado")) {
            ButtonIniciarDesafio.setEnabled(true);
            ButtonIniciarDesafio.setImageResource(R.drawable.xhdpi_ic_pause_white_24dp);
            ButtonIniciarDesafio.setTag(R.drawable.xhdpi_ic_pause_white_24dp);
            ButtonDetenerDesafio.setVisibility(View.VISIBLE);
            ButtonDetenerDesafio.setVisibility(View.VISIBLE);
            ButtonDetenerRutina.setEnabled(false);
            ButtonDetenerRutina.setVisibility(View.INVISIBLE);
            new Mensaje(getActivity().getApplicationContext(), "Esyo iniciado");

            cargarRuta();
        } else {
            intValorCronometro = 0;
        }

        if (cargarEstadoDesafioPause().equals("pause")) {
            ButtonIniciarDesafio.setImageResource(R.drawable.xhdpi_ic_play_arrow_white_24dp);
            ButtonIniciarDesafio.setTag(R.drawable.xhdpi_ic_play_arrow_white_24dp);
            ButtonDetenerDesafio.setVisibility(View.VISIBLE);
            ButtonDetenerRutina.setVisibility(View.VISIBLE);
            intValorCronometro = cargarValorEstadoDesafioPause();
            textoCronometro.setText(segundosToHoras(intValorCronometro));
            new Mensaje(getActivity().getApplicationContext(), "Esyo en pause");

            cargarRuta();
            //guardarEstadoDesafioNoPause();
        }

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
                    ButtonIniciarRutina.setVisibility(View.INVISIBLE);

                    ButtonDetenerRutina.setEnabled(true);
                    ButtonDetenerRutina.setVisibility(View.VISIBLE);

                    TextViewElegirRutina.setEnabled(false);
                    ButtonCrearRutinaFlash.setEnabled(false);
                    ButtonCrearRutinaFlash.setVisibility(View.INVISIBLE);
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

        Cronometro.setUpdateListener(this);
        //cordenadasRuta = Cronometro.getCordenadas();

        ButtonIniciarDesafio.setOnClickListener(new View.OnClickListener() {
            int play = R.drawable.xhdpi_ic_play_arrow_white_24dp;
            int pause = R.drawable.xhdpi_ic_pause_white_24dp;

            @Override
            public void onClick(View v) {
                if ((Integer) ButtonIniciarDesafio.getTag() == play) {
                    guardarEstadoDesafioNoPause();
                    pauseDesafioActivado = false;
                    ButtonIniciarDesafio.setImageResource(R.drawable.xhdpi_ic_pause_white_24dp);
                    ButtonIniciarDesafio.setTag(R.drawable.xhdpi_ic_pause_white_24dp);
                    ButtonDetenerDesafio.setVisibility(View.VISIBLE);
                    ButtonDetenerRutina.setEnabled(false);

                    iniciarCronometro();
                    new Mensaje(getActivity().getApplicationContext(), "Iniciado cronometro");
                    guardarEstadoDesafioIniciado();

                } else {
                    ButtonIniciarDesafio.setImageResource(R.drawable.xhdpi_ic_play_arrow_white_24dp);
                    ButtonIniciarDesafio.setTag(R.drawable.xhdpi_ic_play_arrow_white_24dp);
                    pauseDesafioActivado = true;
                    guardarEstadoDesafioPause();
                }
            }
        });

        ButtonCrearRutinaFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogoMultiSelect dialogo = new DialogoMultiSelect();
                dialogo.show(getFragmentManager(), "multiSelect");
            }
        });

        ButtonDetenerDesafio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonIniciarDesafio.setImageResource(R.drawable.xhdpi_ic_play_arrow_white_24dp);
                ButtonIniciarDesafio.setTag(R.drawable.xhdpi_ic_play_arrow_white_24dp);
                ButtonDetenerDesafio.setVisibility(View.INVISIBLE);
                ButtonDetenerRutina.setEnabled(true);
                pararCronometro();
                guardarEstadoDesafioDetenido();
                guardarEstadoDesafioNoPause();
                actualizarCronometro(0);
            }
        });

        TextViewElegirDesafioActual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idDesafio = String.valueOf(actualDesafio.getDesafioId());
                Intent newIntent = new Intent(getActivity().getApplicationContext(), ActivityDesafioTerminado.class);
                newIntent.putExtra("Desafio", idDesafio);
                newIntent.putExtra("Duracion", segundosToHoras(35));
                newIntent.putExtra("Distancia", TextViewValorDesafioActual.getText().toString());
                newIntent.putExtra("Calorias", 0);
                newIntent.putExtra("Pulso", 0);
                newIntent.putExtra("Nombre", actualDesafio.getDesafioNombre());
                newIntent.putExtra("Nota", actualDesafio.getDesafioDescripcion());
                newIntent.putExtra("Fecha", format.format(actualDesafio.getTerminoDesafio()));
                startActivity(newIntent);
            }
        });

        return root;
    }

    private void crearRutinaFlash() {

        for (int i = 0; i < diasSelecionados.size(); i++) {
            crearDesafios();
        }

        inicializarBaseDeDatos();
        listaDesafios = new ArrayList<>();
        fechasDesafios = new ArrayList<>();
        fechaFinal(c);

        try {
            parsedInicio = format.parse(valores[0]);
            parsedFinal = format.parse(valores[1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        newResumen = new Resumen(0, "Rutina en curso", new java.sql.Date(parsedInicio.getTime()));
        newResumen = resumenCRUD.insertarResumen(newResumen);

        newRutina = new Rutina(0,
                "AutoRutina",
                "Nota Rutina",
                new java.sql.Date(parsedInicio.getTime()),
                new java.sql.Date(parsedFinal.getTime()),
                'P',
                newResumen);
        newRutina = rutinaCRUD.insertarRutina(newRutina);

        listaDesafios.clear();

        String[] diasOrdenados = {"", "", "", "", "", "", ""};

        for (int i = 0; i < camposDesafios.length; i++) {
            if (camposDesafios[i].split(" ")[1].contains("lunes")) {
                diasOrdenados[i] = "0";
            } else if (camposDesafios[i].split(" ")[1].contains("martes")) {
                diasOrdenados[i] = "1";

            } else if (camposDesafios[i].split(" ")[1].contains("coles")) {
                diasOrdenados[i] = "2";

            } else if (camposDesafios[i].split(" ")[1].contains("jueves")) {
                diasOrdenados[i] = "3";

            } else if (camposDesafios[i].split(" ")[1].contains("viernes")) {
                diasOrdenados[i] = "4";

            } else if (camposDesafios[i].split(" ")[1].contains("bado")) {
                diasOrdenados[i] = "5";

            } else if (camposDesafios[i].split(" ")[1].contains("mingo")) {
                diasOrdenados[i] = "6";

            }
        }

        for (int i = 0; i < diasOrdenados.length; i++) {
            for (int j = 0; j < diasSelecionados.size(); j++) {
                if (diasSelecionados.get(j) == Integer.parseInt(diasOrdenados[i])) {
                    valoresDesafios[i] = nuevosDesafios.get(j);
                }
            }
        }

        for (int i = 0; i < valoresDesafios.length; i++) {
            if (valoresDesafios[i].equals("")) {
                valoresDesafios[i] = "Descansar";
            }
        }

        for (int i = 0; i < valoresDesafios.length; i++) {
            if (!valoresDesafios[i].equals("Descansar")) {
                try {
                    newDesafio = desafioCRUD.buscarDesafioPorId(Long.parseLong(valoresDesafios[i].split("-")[0]));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                listaDesafios.add(newDesafio);
                fechasDesafios.add(camposDesafios[i].split(" ")[0]);
            }
        }

        for (int i = 0; i < listaDesafios.size(); i++) {

            try {
                parsedDesafio = format.parse(fechasDesafios.get(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            newDesafioRutina = new DesafioRutina(0, newRutina, listaDesafios.get(i), new java.sql.Date(parsedDesafio.getTime()));
            newDesafioRutina = desafioRutinaCRUD.insertarDesafioRutina(newDesafioRutina);
        }

        new Mensaje(getActivity().getApplicationContext(), "Rutina creada");
        reiniciarDatos();
    }

    private void crearDesafios() {
        Objetivo objetivo = null;

        try {
            objetivo = objetivoCRUD.buscarObjetivoPorNombre("Distancia");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            parsedInicio = format.parse("10/10/2015");
            parsedFinal = format.parse("10/10/2015");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Desafio desafio = new Desafio(0,
                "Desafio Autogenerado",
                "Nota Autogenerada",
                new java.sql.Date(parsedInicio.getTime()),
                new java.sql.Date(parsedFinal.getTime()),
                'P',
                false,
                2,
                3);
        desafio = desafioCRUD.insertarDesafio(desafio);
        nuevosDesafios.add(desafio.getDesafioId() + "-" + desafio.getDesafioNombre());

        DesafioObjetivo desafioObjetivo = new DesafioObjetivo(0, desafio, objetivo, Float.parseFloat("1800"));
        desafioObjetivoCRUD.insertarDesafioObjetivo(desafioObjetivo);
    }

    private void fechaFinal(Calendar c) {
        c = Calendar.getInstance();
        String formattedDate = sdf.format(c.getTime());
        valores[0] = formattedDate;
        actualizarFechas(c);
        formattedDate = sdf.format(c.getTime());
        valores[1] = formattedDate;
    }

    private void actualizarFechas(Calendar c) {
        Locale spanish = new Locale("es", "PE");
        for (int i = 0; i < camposDesafios.length; i++) {
            camposDesafios[i] = sdf.format(c.getTime()) + " " + c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, spanish);
            if ((i + 1) < camposDesafios.length) {
                c.add(Calendar.DATE, +1);
            }
        }
    }

    private void reiniciarDatos() {

        c = Calendar.getInstance();
        valores[0] = format.format(c.getTime());
        valores[1] = format.format(c.getTime());

        for (int i = 0; i < valoresDesafios.length; i++) {
            valoresDesafios[i] = "";
        }
    }

    /*private String validarCreacion() {
        String validar = "";
        int diasDescanso = 0;

        for (int i = 0; i < valoresDesafios.length; i++) {
            if (valoresDesafios[i].equals("")) {
                validar = "Agenda incompleta";
            } else if (valoresDesafios[i].equals("Descansar")) {
                diasDescanso++;
            }
        }

        if (diasDescanso == 7) {
            validar = "Seleccione al menos 1 desafio";
        }

        try {
            parsedInicio = format.parse(valores[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cInicial = Calendar.getInstance();
        cInicial.add(Calendar.DATE, -1);
        Date dateInicio = new java.sql.Date(parsedInicio.getTime());
        Date actual = cInicial.getTime();

        if (dateInicio.getTime() < actual.getTime()) {
            validar = "La fecha inicial debe ser despues de: " + format.format(actual.getTime());
        }

        if (EditTextNombreRutina.getText().toString().equals("")) {
            validar = "Ingrese nombre";
        }

        return validar;
    }*/

    private void cargarRuta() {
        googleMap.clear();
        options = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);

        SharedPreferences prefs = getActivity().getSharedPreferences("cordenadas", Context.MODE_PRIVATE);
        String misCordenadas = prefs.getString("misCordenadas", "nada");

        if (misCordenadas.equals("nada")){
        }
        else{
            String cordenadas[] = misCordenadas.split("X");
            new Mensaje(getActivity().getApplicationContext(), "porte: " + cordenadas.length);

            for (int i = 0; i < cordenadas.length; i++){
                String stringLatLong = cordenadas[i];
                double lat = Double.parseDouble(cordenadas[i].split("=")[0]);
                double longi = Double.parseDouble(cordenadas[i].split("=")[1]);
                nuevaCordenada = new LatLng(lat, longi);
                options.add(nuevaCordenada);
                //new Mensaje(getActivity().getApplicationContext(), "Lat.equals(" + lat+")" + " Long.equals("+longi+")");
            }
        }
        line = googleMap.addPolyline(options);
    }

    private String cargarEstadoDesafio() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "nada";
        String estadoDesafioRecuperado = sharedPref.getString("estadoDesafio", defaultValue);

        return estadoDesafioRecuperado;
    }

    private void guardarEstadoDesafioIniciado() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("estadoDesafio", "iniciado");
        editor.commit();
    }

    private void guardarEstadoDesafioDetenido() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("estadoDesafio", "detenido");
        editor.commit();
    }

    private void guardarEstadoDesafioPause() {
        pararCronometro();
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("estadoDesafioPause", "pause");
        editor.putInt("valorDesafioPause", intValorCronometro);
        editor.commit();
    }

    private void guardarEstadoDesafioNoPause() {
        pararCronometro();
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("estadoDesafioPause", "nopause");
        editor.putInt("valorDesafioPause", 0);
        editor.commit();
    }

    private String cargarEstadoDesafioPause() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "nada";
        String estadoDesafioRecuperado = sharedPref.getString("estadoDesafioPause", defaultValue);

        return estadoDesafioRecuperado;
    }

    private int cargarValorEstadoDesafioPause() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int defaultValue = 0;
        int estadoDesafioRecuperado = sharedPref.getInt("valorDesafioPause", defaultValue);

        return estadoDesafioRecuperado;
    }

    public void completarDesafio() {
        actualDesafio.setEstadoDesafio('T');
        actualDesafio.setExitoDesafio(true);
        try {
            desafioCRUD.actualizarDatosDesafio(actualDesafio);
            TextViewEstadoActual.setText("Terminado");
            TextViewEstadoActual.setTextColor(getResources().getColor(R.color.colorVerde));
            ButtonIniciarDesafio.setImageResource(R.drawable.xhdpi_ic_play_arrow_white_24dp);
            ButtonIniciarDesafio.setTag(R.drawable.xhdpi_ic_play_arrow_white_24dp);
            ButtonIniciarDesafio.setEnabled(false);
            ButtonDetenerRutina.setEnabled(true);
            ButtonDetenerRutina.setVisibility(View.VISIBLE);
            ButtonDetenerDesafio.setVisibility(View.INVISIBLE);
            TextViewElegirDesafioActual.setEnabled(true);
            TextViewEstadoTerminado.setVisibility(View.VISIBLE);
            cambiarVisibilidadDesafioActual(View.INVISIBLE);
            guardarEstadoDesafioNoPause();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        new Mensaje(getActivity().getApplicationContext(), "Desafio terminado");
        guardarEstadoDesafioDetenido();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        Cronometro.setUpdateListener(null);
        //guardarEstadoDesafio("detenido");
    }

    private void iniciarCronometro() {
        Intent service = new Intent(getActivity().getBaseContext(), Cronometro.class);
        Log.v("CletApp", "Servicio iniciado");
        getActivity().startService(service);
    }

    private void pararCronometro() {
        Intent service = new Intent(getActivity().getBaseContext(), Cronometro.class);
        getActivity().stopService(service);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        estado = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        //Cronometro.setUpdateListener(this);
        estado = true;
    }

    public void actualizarCronometro(int tiempo) {
        intValorCronometro = tiempo;
        Log.v("CletApp", tiempo + "");
        textoCronometro.setText(segundosToHoras(tiempo));
    }

    public void mostrarCordenadas(double latitud, double longitud){
        //new Mensaje(getActivity().getApplicationContext(), "Cordenadas: " + latitud + ", " + longitud);
        nuevaCordenada = new LatLng(latitud, longitud);
        options.add(nuevaCordenada);
        line = googleMap.addPolyline(options);
    }

    public String segundosToHoras(int totalSegundos) {
        horas = totalSegundos / 3600;
        horasString = String.valueOf(horas);
        if (horasString.length() == 1) {
            horasString = "0" + horasString;
        }

        minutos = (totalSegundos - (3600 * horas)) / 60;
        minutosString = String.valueOf(minutos);
        if (minutosString.length() == 1) {
            minutosString = "0" + minutosString;
        }

        segundos = totalSegundos - ((horas * 3600) + (minutos * 60));
        segundosString = String.valueOf(segundos);
        if (segundosString.length() == 1) {
            segundosString = "0" + segundosString;
        }

        return horasString + ":" + minutosString + ":" + segundosString;
    }

    private void inicializarBaseDeDatos() {
        //Prueba para crear desafios
        desafioCRUD = new DesafioCRUD(getActivity().getApplicationContext());
        desafioObjetivoCRUD = new DesafioObjetivoCRUD(getActivity().getApplicationContext());
        objetivoCRUD = new ObjetivoCRUD(getActivity().getApplicationContext());
        //Fin de las pruebas

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
        diasSelecionados = new ArrayList<>();
        nuevosDesafios = new ArrayList<>();
        ButtonFrecuencioa = (Button) root.findViewById(R.id.ButtonFrecuencioa);
        ButtonIniciarRutina = (ImageButton) root.findViewById(R.id.ButtonIniciarRutina);
        ButtonDetenerRutina = (ImageButton) root.findViewById(R.id.ButtonDetenerRutina);
        ButtonIniciarDesafio = (ImageButton) root.findViewById(R.id.ButtonIniciarDesafio);
        ButtonCrearRutinaFlash = (ImageButton) root.findViewById(R.id.ButtonCrearRutinaFlash);
        ButtonDetenerDesafio = (ImageButton) root.findViewById(R.id.ButtonDetenerDesafio);

        textoCronometro = (TextView) root.findViewById(R.id.cronometro);
        textoCronometro.setText("00:00:00");

        ButtonIniciarDesafio.setEnabled(false);
        ButtonIniciarDesafio.setTag(R.drawable.xhdpi_ic_play_arrow_white_24dp);

        ButtonDetenerRutina.setEnabled(false);
        ButtonDetenerRutina.setVisibility(View.INVISIBLE);

        ButtonIniciarRutina.setEnabled(false);
        ButtonIniciarRutina.setVisibility(View.INVISIBLE);

        ButtonDetenerDesafio.setVisibility(View.INVISIBLE);

        ImageViewImagenDesafio2 = (ImageView) root.findViewById(R.id.ImageViewImagenDesafio2);
        ImageViewImagenDesafioActual = (ImageView) root.findViewById(R.id.ImageViewImagenDesafioActual);

        TextViewNombreDesafio = (TextView) root.findViewById(R.id.TextViewNombreDesafio);
        TextViewNombreDesafioActual = (TextView) root.findViewById(R.id.TextViewNombreDesafioActual);

        TextViewCategoriaDesafio = (TextView) root.findViewById(R.id.TextViewCategoriaDesafio);
        //TextViewCategoriaDesafioActual = (TextView) root.findViewById(R.id.TextViewCategoriaDesafioActual);

        TextViewValorDesafio = (TextView) root.findViewById(R.id.TextViewValorDesafio);
        TextViewValorDesafioActual = (TextView) root.findViewById(R.id.TextViewValorDesafioActual);

        TextViewNotaDesafio = (TextView) root.findViewById(R.id.TextViewNotaDesafio);
        //TextViewNotaDesafioActual = (TextView) root.findViewById(R.id.TextViewNotaDesafioActual);

        TextViewFechaDesafio = (TextView) root.findViewById(R.id.TextViewFechaDesafio);
        //TextViewFechaDesafioActual = (TextView) root.findViewById(R.id.TextViewFechaDesafioActual);

        TextViewEstado = (TextView) root.findViewById(R.id.TextViewEstado);
        TextViewEstadoActual = (TextView) root.findViewById(R.id.TextViewEstadoActual);
        TextViewEstadoTerminado = (TextView) root.findViewById(R.id.TextViewEstadoTerminado);

        TextViewElegirRutina = (TextView) root.findViewById(R.id.TextViewElegirRutina);
        TextViewElegirDesafioActual = (TextView) root.findViewById(R.id.TextViewElegirDesafioActual);

        TextViewSeries = (TextView) root.findViewById(R.id.TextViewSeries);
        TextViewRepeticiones = (TextView) root.findViewById(R.id.TextViewRepeticiones);

        cambiarVisibilidadRutina(View.INVISIBLE);
        cambiarVisibilidadDesafioActual(View.INVISIBLE);

        TextViewElegirDesafioActual.setEnabled(false);
        TextViewEstadoTerminado.setVisibility(View.INVISIBLE);

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
            ButtonCrearRutinaFlash.setEnabled(false);
            ButtonCrearRutinaFlash.setVisibility(View.INVISIBLE);

            TextViewElegirDesafioActual.setText("");

            ButtonDetenerRutina.setEnabled(true);
            ButtonDetenerRutina.setVisibility(View.VISIBLE);

            try {
                listaDesafiosRutina = desafioRutinaCRUD.buscarDesafioRutinaPorIdRutinaOrderByFecha(actualRutina);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            cargarDatosRutina(R.drawable.mdpi_ic_directions_bike_black_24dp, actualRutina.getRutinaNombre(), "Desafios", String.valueOf(listaDesafiosRutina.size()), actualRutina.getRutinaDescripcion(),
                    "Desde: " + format.format(actualRutina.getRutinaInicio()) + " hasta: " + format.format(actualRutina.getRutinaTermino()),
                    "Iniciada");

            try {
                listaDesafiosRutina = desafioRutinaCRUD.buscarDesafioRutinaPorIdRutinaOrderByFecha(actualRutina);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            cargarDesafio(String.valueOf(R.drawable.mdpi_ic_star_black_24dp));

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
            ButtonIniciarRutina.setVisibility(View.VISIBLE);

            cambiarVisibilidadRutina(View.VISIBLE);
            cargarDatosRutina(/*Integer.parseInt(data.split("-")[1])*/ R.drawable.mdpi_ic_directions_bike_black_24dp, data.split("-")[2], data.split("-")[3], data.split("-")[4], data.split("-")[5], data.split("-")[6], data.split("-")[7]);

            try {
                actualRutina = rutinaCRUD.buscarRutinaPorId(Long.parseLong(data.split("-")[0]));
                listaDesafiosRutina = desafioRutinaCRUD.buscarDesafioRutinaPorIdRutinaOrderByFecha(actualRutina);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //cargarDesafio(data.split("-")[1]);
            cargarDesafio(String.valueOf(R.drawable.mdpi_ic_star_black_24dp));
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
                    estadoDesafioCargado,
                    actualDesafio.getSeries(),
                    actualDesafio.getRepeticiones());

            if (!listaRutinasIniciadas.isEmpty()) {
                ButtonIniciarDesafio.setEnabled(true);
            }

            if (estadoDesafioCargado.equals("Terminado")) {
                ButtonIniciarDesafio.setEnabled(false);
                TextViewElegirDesafioActual.setEnabled(true);
                TextViewEstadoTerminado.setVisibility(View.VISIBLE);
                cambiarVisibilidadDesafioActual(View.INVISIBLE);
            }

            encontrado = false;

        } else {
            cambiarVisibilidadDesafioActual(View.VISIBLE);
            cargarDatosDesafioActual(Integer.parseInt(imagen), "Descansar", "", "", "", format.format(actual), diff, 0, 0);

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

        //TextViewRepeticiones.setVisibility(visible);
        //TextViewSeries.setVisibility(visible);
        //TextViewCategoriaDesafio.setVisibility(visible);
        //TextViewValorDesafio.setVisibility(visible);
        //TextViewNotaDesafio.setVisibility(visible);
        //TextViewFechaDesafio.setVisibility(visible);
        //TextViewEstado.setVisibility(visible);
    }

    private void cambiarVisibilidadDesafioActual(int visible) {
        ImageViewImagenDesafioActual.setVisibility(visible);
        TextViewNombreDesafioActual.setVisibility(visible);
        //TextViewCategoriaDesafioActual.setVisibility(visible);
        TextViewValorDesafioActual.setVisibility(visible);

        TextViewRepeticiones.setVisibility(visible);
        TextViewSeries.setVisibility(visible);
        //.setVisibility(visible);
        //TextViewFechaDesafioActual.setVisibility(visible);
        //TextViewEstadoActual.setVisibility(visible);
    }

    private void cargarDatosDesafioActual(int ic_grade_black_48dp, String desafioNombre, String objetivoNombre, String valorDesafio, String desafioDescripcion, String fecha, String estado, int series, int repeticiones) {
        ImageViewImagenDesafioActual.setImageResource(ic_grade_black_48dp);
        TextViewNombreDesafioActual.setText(desafioNombre);
        //TextViewCategoriaDesafioActual.setText(objetivoNombre);
        TextViewValorDesafioActual.setText(valorDesafio);
        //TextViewNotaDesafioActual.setText(desafioDescripcion);
        //TextViewFechaDesafioActual.setText(fecha);
        TextViewEstadoActual.setText(estado);
        TextViewSeries.setText("Series: 0/" + series);
        TextViewRepeticiones.setText("Repeticiones: 0/" + repeticiones);

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
        ButtonCrearRutinaFlash.setEnabled(true);
        ButtonCrearRutinaFlash.setVisibility(View.VISIBLE);
        TextViewElegirDesafioActual.setText("Desafio actual");
        ButtonDetenerRutina.setEnabled(false);
        ButtonDetenerRutina.setVisibility(View.INVISIBLE);
        TextViewEstadoTerminado.setVisibility(View.INVISIBLE);

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
            TextViewEstadoTerminado.setVisibility(View.INVISIBLE);
            TextViewElegirDesafioActual.setTextColor(getResources().getColor(R.color.colorGris25));
            TextViewElegirDesafioActual.setText("Sin desafio actual");
            TextViewElegirDesafioActual.setEnabled(false);
            TextViewElegirRutina.setText("Seleccionar rutina");
            TextViewElegirRutina.setEnabled(true);
            ButtonCrearRutinaFlash.setEnabled(true);
            ButtonCrearRutinaFlash.setVisibility(View.VISIBLE);
            TextViewElegirDesafioActual.setText("Desafio actual");
            ButtonDetenerRutina.setEnabled(false);
            ButtonDetenerRutina.setVisibility(View.INVISIBLE);
            ButtonIniciarDesafio.setEnabled(false);

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

    public void diasSeleccionados(ArrayList<Integer> data) {
        diasSelecionados = data;
        crearRutinaFlash();
    }
}
