package com.example.joel.cletapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.joel.cletapp.ActivityProgresoDesafio;
import com.example.joel.cletapp.ActivityProgresoRutina;
import com.example.joel.cletapp.CRUDDatabase.DesafioCRUD;
import com.example.joel.cletapp.CRUDDatabase.DesafioObjetivoCRUD;
import com.example.joel.cletapp.CRUDDatabase.DesafioRutinaCRUD;
import com.example.joel.cletapp.CRUDDatabase.ObjetivoCRUD;
import com.example.joel.cletapp.CRUDDatabase.RepeticionesCRUD;
import com.example.joel.cletapp.CRUDDatabase.ResumenCRUD;
import com.example.joel.cletapp.CRUDDatabase.RutaCRUD;
import com.example.joel.cletapp.CRUDDatabase.RutinaCRUD;
import com.example.joel.cletapp.CRUDDatabase.SerieCRUD;
import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.ClasesDataBase.DesafioObjetivo;
import com.example.joel.cletapp.ClasesDataBase.DesafioRutina;
import com.example.joel.cletapp.ClasesDataBase.Objetivo;
import com.example.joel.cletapp.ClasesDataBase.Repeticiones;
import com.example.joel.cletapp.ClasesDataBase.Resumen;
import com.example.joel.cletapp.ClasesDataBase.Ruta;
import com.example.joel.cletapp.ClasesDataBase.Rutina;
import com.example.joel.cletapp.ClasesDataBase.Serie;
import com.example.joel.cletapp.HeartRateMonitor;
import com.example.joel.cletapp.Mensaje;
import com.example.joel.cletapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
    private PolylineOptions optionsSeleccionada;
    private Polyline line;
    private Polyline lineSeleccionada;

    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private boolean encontrado = false;
    private boolean nextDesafio = false;
    private String diff = "";
    private String fechaActual = "";

    private Button ButtonFrecuencioa;
    //private ImageButton ButtonSeleccionarRuta;
    private ImageButton ButtonIniciarRutina;
    //private ImageButton ButtonDetenerRutina;
    private ImageButton ButtonIniciarDesafio;
    //private Button ButtonCrearRutinaFlash;
    private ImageButton ButtonDetenerDesafio;

    private TextView textoCronometro;
    private int totalSegundos = 0;
    private int segundos = 0;
    private int minutos = 0;
    private int horas = 0;
    private String segundosString;
    private String minutosString;
    private String horasString;
    private String rutaBaseDeDatos = "";
    public boolean estado = true;

    //Datos para el servicio
    public boolean pauseDesafioActivado = false;
    public int intValorCronometro = 0;
    public int cronoServiceValorActualDescansoRepeticion = 0;
    public int cronometroRepeticion = 0;

    //Valores serie y repeticion en recuperacion
    public int cronoServiceSerie = 1;
    public int cronoServiceRepeticion = 1;
    public int cronoServiceNumeroRepeticion = 0;

    //Para guardar la distanciaTotal y de la serie
    public float cronoServiceDistanciaTotal = 0;
    public List<Float> cronoServiceDistanciaSerie = new ArrayList<>();

    //private ImageView ImageViewImagenDesafio2, ImageViewImagenDesafioActual;
    private ImageView ImageViewImagenDesafioActual;
    //private TextView TextViewNombreDesafio, TextViewNombreDesafioActual;
    private TextView TextViewNombreDesafioActual;
    //private TextView TextViewCategoriaDesafio, TextViewCategoriaDesafioActual;
    private TextView TextViewCategoriaDesafioActual;
    //private TextView TextViewValorDesafio;
    private TextView TextViewValorDesafioActual;
    //private TextView TextViewNotaDesafio, TextViewNotaDesafioActual;
    private TextView TextViewNotaDesafioActual;
    //private TextView TextViewFechaDesafio, TextViewFechaDesafioActual;
    private TextView TextViewFechaDesafioActual;
    //private TextView TextViewEstado, TextViewEstadoActual, TextViewEstadoTerminado;
    private TextView TextViewEstadoActual, TextViewEstadoTerminado;
    //private TextView TextViewSeries, TextViewRepeticiones;
    //private TextView TextViewRepeticiones;

    //private TextView TextViewElegirRutina;
    private boolean estadoElegirRutina = true;
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
    private SerieCRUD serieCRUD;
    private RepeticionesCRUD repeticionesCRUD;

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
    public int seriesTotal, repeticionesTotal = 0;
    public int seriesActual, repeticionesActual = 1;

    private DetalleFragment detalleFragment;
    //private Button ButtonPrueba;
    private boolean mostrado = false;
    private Button ButtonDetalleRutina;
    //Fin de pruebas

    private int tiempoLimiteTotal = 1870;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem itemOculto = menu.findItem(R.id.action_search);
        itemOculto.setVisible(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        mapView = (MapView) root.findViewById(R.id.mi_mapa);

        mapView.onCreate(savedInstanceState);
        googleMap = mapView.getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);

        View btnMyLocation = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(80, 80); // size of button in dp
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.setMargins(0, 0, 20, 20);
        btnMyLocation.setLayoutParams(params);

        Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        double lat;
        double lng;

        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            LatLng latLngTest = new LatLng(lat, lng);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngTest, 16));
            googleMap.addMarker(new MarkerOptions().position(latLngTest).title("Start"));
        }

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
            //ButtonDetenerRutina.setEnabled(false);
            //ButtonDetenerRutina.setVisibility(View.INVISIBLE);
            //ButtonSeleccionarRuta.setVisibility(View.INVISIBLE);

            cargarRuta();

        } else {
            //ButtonSeleccionarRuta.setVisibility(View.VISIBLE);
            intValorCronometro = 0;
            cronometroRepeticion = 0;
            cronoServiceValorActualDescansoRepeticion = 0;
            //cronometroRepeticion = 1;
            cronoServiceSerie = 1;
            cronoServiceDistanciaSerie = new ArrayList<>();
        }

        if (cargarEstadoDesafioPause().equals("pause")) {
            //new Mensaje(getActivity().getApplicationContext(), "Estoy en pause");
            ButtonIniciarDesafio.setImageResource(R.drawable.xhdpi_ic_play_arrow_white_24dp);
            ButtonIniciarDesafio.setTag(R.drawable.xhdpi_ic_play_arrow_white_24dp);
            ButtonDetenerDesafio.setVisibility(View.VISIBLE);
            //ButtonDetenerRutina.setVisibility(View.VISIBLE);

            intValorCronometro = cargarValorEstadoDesafioPause();
            cronoServiceValorActualDescansoRepeticion = cargarValorEstadoDesafioPauseDescanso();

            cronoServiceDistanciaSerie = cargarValorDistanciaSerie(); //Tiene algo que hace crashear
            cronoServiceDistanciaTotal = cargarValorDistanciaTotal();

            cronometroRepeticion = cargarValorEstadoDesafioPauseCronometroSerie();
            cronoServiceSerie = cargarValorCronometroSerie();
            cronoServiceRepeticion = cargarValorCronometroRepeticion();
            cronoServiceNumeroRepeticion = cargarValorCronometroNumeroRepeticion();
            actualizarSeriesRepeticiones(cronoServiceSerie, cronoServiceRepeticion, cronoServiceNumeroRepeticion);
            textoCronometro.setText(segundosToHoras(intValorCronometro));

            cargarRuta();
            //guardarEstadoDesafioNoPause();
        }

        /*
        TextViewElegirRutina.setOnClickListener(new View.OnClickListener() {
            Bundle bundle = new Bundle();

            @Override
            public void onClick(View v) {
                if (estadoElegirRutina == true) {
                    DialogoRutinaSelector dialogo = new DialogoRutinaSelector();
                    dialogo.setArguments(bundle);
                    dialogo.show(getFragmentManager(), "categoriaPicker");
                } else {
                    //new Mensaje(getActivity().getApplicationContext(), "Hola mundo");
                    //Mostrar el progreso actual de la rutina
                    String idRutina = String.valueOf(actualRutina.getRutinaId());
                    Intent newIntent = new Intent(getActivity().getApplicationContext(), ActivityProgresoRutina.class);
                    newIntent.putExtra("idRutina", idRutina);
                    startActivity(newIntent);
                }
            }
        });
        */

        ButtonFrecuencioa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getActivity().getApplicationContext(), HeartRateMonitor.class);
                startActivity(newIntent);
            }
        });

        ButtonDetalleRutina.setOnClickListener(new View.OnClickListener() {
            Bundle bundle = new Bundle();

            @Override
            public void onClick(View v) {
                String nombreRutina = "";
                String nombreDesafio = "";
                String seriesTotalString = "";
                String repeticionesTotalString = "";
                String seriesString = "";
                String repeticionesString = "";

                bundle.putBoolean("estado", estadoElegirRutina);
                if (!estadoElegirRutina) {
                    nombreRutina = actualRutina.getRutinaNombre();
                    nombreDesafio = actualDesafio.getDesafioNombre();
                    seriesTotalString = String.valueOf(seriesTotal);
                    repeticionesTotalString = String.valueOf(repeticionesTotal);

                    seriesString = String.valueOf(seriesActual);
                    if (seriesActual == 0){
                        seriesString = String.valueOf(1);
                    }
                    repeticionesString = String.valueOf(repeticionesActual);
                    if (repeticionesActual == 0){
                        repeticionesString = String.valueOf(1);
                    }

                    bundle.putString("nombreRutina", nombreRutina);
                    bundle.putString("nombreDesafio", nombreDesafio);
                    bundle.putString("seriesDesafioTotal", seriesTotalString);
                    bundle.putString("repeticionesDesafioTotal", repeticionesTotalString);
                    bundle.putString("seriesDesafio", seriesString);
                    bundle.putString("repeticionesDesafio", repeticionesString);
                }
                DialogoDetalleRutina dialogoDetalleRutina = new DialogoDetalleRutina();
                dialogoDetalleRutina.setArguments(bundle);
                dialogoDetalleRutina.show(getFragmentManager(), "detalleRutina");
            }
        });

        /*
        ButtonCrearRutinaFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogoMultiSelect dialogo = new DialogoMultiSelect();
                dialogo.show(getFragmentManager(), "multiSelect");
            }
        });
        */


        ButtonIniciarRutina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarRutina();
            }
        });

        /*
        ButtonDetenerRutina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detenerRutinaEnCurso();
            }
        });
        */

        Cronometro.setUpdateListener(this, tiempoLimiteTotal);
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
                    //ButtonDetenerRutina.setEnabled(false);
                    //ButtonSeleccionarRuta.setVisibility(View.INVISIBLE);

                    if (!rutaBaseDeDatos.equals("")) {
                        SharedPreferences prefs = getActivity().getSharedPreferences("rutaBaseDeDatos", Context.MODE_PRIVATE);
                        prefs.edit().putString("coordenadasBaseDeDatos", rutaBaseDeDatos).commit();
                    } else {
                        SharedPreferences prefs = getActivity().getSharedPreferences("rutaBaseDeDatos", Context.MODE_PRIVATE);
                        prefs.edit().putString("coordenadasBaseDeDatos", "nada").commit();
                    }

                    //intValorCronometro = cargarValorEstadoDesafioPause();
                    //cronoServiceDistanciaSerie = cargarValorDistanciaSerie();
                    //cronoServiceDistanciaTotal = cargarValorDistanciaTotal();
                    //cronometroRepeticion = cargarValorEstadoDesafioPauseCronometroSerie();
                    //cronoServiceSerie = cargarValorCronometroSerie();
                    //cronoServiceRepeticion = cargarValorCronometroRepeticion();
                    //actualizarSeriesRepeticiones(cronoServiceSerie, cronoServiceRepeticion);
                    cronoServiceNumeroRepeticion = cargarValorCronometroNumeroRepeticion();
                    cronoServiceDistanciaTotal = cargarValorDistanciaTotal();
                    //new Mensaje(getActivity().getApplicationContext(), "Serie: " + cronoServiceSerie + " Rep: " + cronoServiceRepeticion);
                    //textoCronometro.setText(segundosToHoras(intValorCronometro));
                    guardarEstadoDesafioIniciado();
                    iniciarCronometro();
                } else {
                    final MediaPlayer desafioPausado = MediaPlayer.create(getActivity().getApplicationContext(), R.drawable.desafio_pausado);
                    desafioPausado.setVolume(15.0f, 15.0f);
                    desafioPausado.start();

                    ButtonIniciarDesafio.setImageResource(R.drawable.xhdpi_ic_play_arrow_white_24dp);
                    ButtonIniciarDesafio.setTag(R.drawable.xhdpi_ic_play_arrow_white_24dp);
                    pauseDesafioActivado = true;
                    guardarEstadoDesafioPause();
                    //guardarDistanciaTotalService(cronoServiceDistanciaTotal);
                }
            }
        });

        ButtonDetenerDesafio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MediaPlayer desafioTerminado = MediaPlayer.create(getActivity().getApplicationContext(), R.drawable.desafio_terminado);
                desafioTerminado.setVolume(15.0f, 15.0f);
                desafioTerminado.start();
                //ButtonSeleccionarRuta.setVisibility(View.VISIBLE);

                SharedPreferences prefs = getActivity().getSharedPreferences("rutaBaseDeDatos", Context.MODE_PRIVATE);
                prefs.edit().putString("coordenadasBaseDeDatos", "nada").commit();

                ButtonIniciarDesafio.setImageResource(R.drawable.xhdpi_ic_play_arrow_white_24dp);
                ButtonIniciarDesafio.setTag(R.drawable.xhdpi_ic_play_arrow_white_24dp);
                ButtonDetenerDesafio.setVisibility(View.INVISIBLE);
                //ButtonDetenerRutina.setEnabled(true);
                pararCronometro();
                guardarEstadoDesafioDetenido();
                guardarEstadoDesafioNoPause();
                guardarRepeticionReinicio();
                actualizarCronometro(0, 0, 0);
                cronoServiceDistanciaSerie = new ArrayList<Float>();
                cronoServiceDistanciaTotal = 0;
                guardarDistanciaTotalService(cronoServiceDistanciaTotal);
                cronometroRepeticion = 0;
                cronoServiceSerie = 1;
                cronoServiceRepeticion = 1;
                //TextViewValorDesafioActual
                //TextViewValorDesafioActual.setText(String.valueOf(convertirMetrosToKilometros(desafioObjetivo.getValor())) + " Km");
                TextViewValorDesafioActual.setText(String.valueOf("0.0") + " Km");
                actualizarSeriesRepeticionesDefault(0, 0);
            }
        });

        TextViewElegirDesafioActual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String idDesafio = String.valueOf(actualDesafio.getDesafioId());
                Intent newIntent = new Intent(getActivity().getApplicationContext(), ActivityDesafioTerminado.class);
                newIntent.putExtra("Desafio", idDesafio);
                newIntent.putExtra("Duracion", segundosToHoras(35));
                newIntent.putExtra("Distancia", TextViewValorDesafioActual.getText().toString());
                newIntent.putExtra("Calorias", 0);
                newIntent.putExtra("Pulso", 0);
                newIntent.putExtra("Nombre", actualDesafio.getDesafioNombre());
                newIntent.putExtra("Nota", actualDesafio.getDesafioDescripcion());
                newIntent.putExtra("Fecha", format.format(actualDesafio.getTerminoDesafio()));
                startActivity(newIntent);*/

                /*
                if (getDetalleFragment().isAdded()){
                    ocultarFragmento(getDetalleFragment(), R.anim.activity_visible_salida_abajo, R.anim.activity_nuevo_entrada_arriba);
                }
                */

                String idDesafio = String.valueOf(actualDesafio.getDesafioId());
                String valorDesafio = String.valueOf(Math.round(desafioObjetivo.getValor()));
                Intent newIntent = new Intent(getActivity().getApplicationContext(), ActivityProgresoDesafio.class);
                newIntent.putExtra("Desafio", idDesafio);
                newIntent.putExtra("valorDesafio", valorDesafio);
                startActivity(newIntent);
            }
        });

        /*ButtonSeleccionarRuta.setOnClickListener(new View.OnClickListener() {
            Bundle bundle = new Bundle();

            @Override
            public void onClick(View v) {
                bundle.putString("Accion", "SeleccionarRuta");
                bundle.putString("Mensaje", "Descripccion");
                bundle.putString("Titulo", "Seleccione ruta");

                DialogoRutaSelector dialogo = new DialogoRutaSelector();
                dialogo.setArguments(bundle);
                dialogo.show(getFragmentManager(), "rutaPicker");
            }
        });*/

        return root;
    }

    private void detenerRutinaEnCurso() {
        Bundle bundle = new Bundle();
        bundle.putString("Accion", "DetenerRutina");
        bundle.putString("Mensaje", "Detener la rutina forzara la evaluacion con el progreso actual");
        bundle.putString("Titulo", "Detener rutina");

        DialogoConfirmacion dialogo = new DialogoConfirmacion();
        dialogo.setArguments(bundle);
        dialogo.show(getFragmentManager(), "categoriaPicker");
    }

    private void iniciarRutina() {
        if (ButtonIniciarRutina.isEnabled()) {

            ButtonIniciarRutina.setEnabled(false);
            ButtonIniciarRutina.setVisibility(View.INVISIBLE);

            //ButtonDetenerRutina.setEnabled(true);
            //ButtonDetenerRutina.setVisibility(View.VISIBLE);

            //TextViewElegirRutina.setEnabled(false);
            estadoElegirRutina = false;
            //ButtonCrearRutinaFlash.setEnabled(false);
            //ButtonCrearRutinaFlash.setVisibility(View.INVISIBLE);
            //TextViewEstado.setText("Iniciada");
            //TextViewEstado.setTextColor(getResources().getColor(R.color.colorVerde));

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

    public void caminoDeRuta() {
        Bundle bundle = new Bundle();
        bundle.putString("Accion", "SeleccionarRuta");
        bundle.putString("Mensaje", "Descripccion");
        bundle.putString("Titulo", "Seleccione ruta");

        DialogoRutaSelector dialogo = new DialogoRutaSelector();
        dialogo.setArguments(bundle);
        dialogo.show(getFragmentManager(), "rutaPicker");
    }

    private void guardarRepeticionReinicio() {
        SharedPreferences prefs = getActivity().getSharedPreferences("serieRepeticion", Context.MODE_PRIVATE);
        prefs.edit().putInt("numeroRepeticionActual", 0).commit();
    }


    private int cargarValorCronometroNumeroRepeticion() {
        SharedPreferences prefs = getActivity().getSharedPreferences("serieRepeticion", Context.MODE_PRIVATE);
        return prefs.getInt("numeroRepeticionActual", 0);
    }

    private List<Float> cargarValorDistanciaSerie() {
        List<Float> distanciaSerieLista;
        SharedPreferences prefs = getActivity().getSharedPreferences("distanciaTotal", Context.MODE_PRIVATE);
        String distanciaSeries = prefs.getString("distanciaTotalSerie", "nada");

        if (distanciaSeries.equals("nada")) {
            distanciaSerieLista = new ArrayList<>();
        } else {
            distanciaSerieLista = new ArrayList<>();
            String valoresDistanciaSerie[] = distanciaSeries.split("X");

            for (int i = 0; i < valoresDistanciaSerie.length; i++) {
                distanciaSerieLista.add(Float.parseFloat(valoresDistanciaSerie[i]));
            }
        }

        return distanciaSerieLista;
    }

    private float cargarValorDistanciaTotal() {
        SharedPreferences prefs = getActivity().getSharedPreferences("distanciaTotal", Context.MODE_PRIVATE);
        return prefs.getFloat("distanciaTotalService", 0);
    }

    private void guardarDistanciaTotalService(float distance) {
        SharedPreferences prefs = getActivity().getSharedPreferences("distanciaTotal", Context.MODE_PRIVATE);
        prefs.edit().putFloat("distanciaTotalService", distance).commit();
    }

    private int cargarValorCronometroSerie() {
        SharedPreferences prefs = getActivity().getSharedPreferences("serieRepeticion", Context.MODE_PRIVATE);
        return prefs.getInt("serieActual", 1);
    }

    private int cargarValorCronometroRepeticion() {
        SharedPreferences prefs = getActivity().getSharedPreferences("serieRepeticion", Context.MODE_PRIVATE);
        return prefs.getInt("repeticionActual", 1);
    }

    public Desafio pasarDesafioActual() {
        return actualDesafio;
    }

    private void crearRutinaFlash() {

        int valorObjetivoPersonalizado = determinarValorObjetivo();
        //new Mensaje(getActivity().getApplicationContext(), "Resultado: " + valorObjetivoPersonalizado);

        for (int i = 0; i < diasSelecionados.size(); i++) {
            crearDesafios(valorObjetivoPersonalizado);
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
        newRutina.setRutinaNombre("Rutina " + newRutina.getRutinaId());
        try {
            newRutina = rutinaCRUD.actualizarRutina(newRutina);
        } catch (ParseException e) {
            e.printStackTrace();
        }

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
        actualizarDatos(generarDatosRutina());
        reiniciarDatos();
    }

    private String generarDatosRutina() {
        String returnData = "";
        returnData = String.valueOf(newRutina.getRutinaId());
        returnData = returnData + "-" + "imagen";
        returnData = returnData + "-" + newRutina.getRutinaNombre();
        returnData = returnData + "-" + "Desafios";
        returnData = returnData + "-" + "0";
        returnData = returnData + "-" + newRutina.getRutinaDescripcion();
        returnData = returnData + "-" + newRutina.getRutinaInicio();
        returnData = returnData + "-" + newRutina.getRutinaEstado();

        return returnData;
    }

    private void crearDesafios(int valorObjetivoPersonalizado) {
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
                0,
                2,
                3,
                0);
        desafio = desafioCRUD.insertarDesafio(desafio);
        desafio.setDesafioNombre("Desafio " + desafio.getDesafioId());
        try {
            desafio = desafioCRUD.actualizarDatosDesafio(desafio);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        nuevosDesafios.add(desafio.getDesafioId() + "-" + desafio.getDesafioNombre());

        int valorObjetivo = valorObjetivoPersonalizado;

        DesafioObjetivo desafioObjetivo = new DesafioObjetivo(0, desafio, objetivo, Float.parseFloat(String.valueOf(valorObjetivo)));
        desafioObjetivoCRUD.insertarDesafioObjetivo(desafioObjetivo);

        //Se crean las series y repeticiones para el desafio, primer argunmento el desafio, segundo las series y tercero las repeticiones
        crearSeriesRepeticiones(desafio, 2, 3);

    }

    /**
     * Funciona que calcula el valor de los desafios.
     *
     * @return el valor del desafio.
     */
    private int determinarValorObjetivo() {
        List<Rutina> ultimasRutinas = new ArrayList<>();
        List<Desafio> ultimosDesafios = new ArrayList<>();
        List<DesafioObjetivo> valoresDesafios = new ArrayList<>();
        List<DesafioRutina> todosLosDesafiosRutinas = new ArrayList<>();
        List<Float> promediosFinales = new ArrayList<>();

        try {
            ultimasRutinas = rutinaCRUD.buscarUltimaRutina();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!ultimasRutinas.isEmpty()) {
            Rutina ultimaRutina = ultimasRutinas.get(ultimasRutinas.size() - 1);

            try {
                todosLosDesafiosRutinas = desafioRutinaCRUD.buscarDesafioRutinaPorIdRutinaOrderByFecha(ultimaRutina);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            DesafioObjetivo desafioObjetivoValor = null;
            for (int i = 0; i < todosLosDesafiosRutinas.size(); i++) {
                try {
                    desafioObjetivoValor = desafioObjetivoCRUD.buscarDesafioObjetivoPorIdDesafio(todosLosDesafiosRutinas.get(i).getDesafio());
                    valoresDesafios.add(desafioObjetivoValor);
                    ultimosDesafios.add(todosLosDesafiosRutinas.get(i).getDesafio());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < ultimosDesafios.size(); i++) {
                List<Repeticiones> todasLasRepeticiones = new ArrayList<>();

                List<Repeticiones> todasLasUltimasRepeticionesDeLaSerie = new ArrayList<>();
                List<Serie> todasLasUltimasSeries = new ArrayList<>();
                try {
                    Desafio actualUltimoDesafio = desafioCRUD.buscarDesafioPorId(ultimosDesafios.get(i).getDesafioId());
                    todasLasUltimasSeries = serieCRUD.buscarSeriePorIdDesafio(actualUltimoDesafio);

                    List<Float> diferenciasFinales = new ArrayList<>();
                    for (int j = 0; j < todasLasUltimasSeries.size(); j++) {
                        todasLasUltimasRepeticionesDeLaSerie = repeticionesCRUD.buscarRepeticionesPorIdSerie(todasLasUltimasSeries.get(j));

                        for (int k = 0; k < todasLasUltimasRepeticionesDeLaSerie.size(); k++) {
                            todasLasRepeticiones.add(todasLasUltimasRepeticionesDeLaSerie.get(k));
                            diferenciasFinales.add(todasLasUltimasRepeticionesDeLaSerie.get(k).getValor());
                            //new Mensaje(getActivity().getApplicationContext(), "Repeticion: " + todasLasUltimasRepeticionesDeLaSerie.get(k).getRepeticionId());
                        }
                    }

                    Object maximoValor = Collections.max(diferenciasFinales);
                    promediosFinales.add(Float.parseFloat(String.valueOf(maximoValor)));
                    diferenciasFinales = new ArrayList<>();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        } else {
            promediosFinales.add(Float.parseFloat("2000"));
        }

        Object nuevoValorDesafio = Collections.max(promediosFinales);
        return (int) Math.round(Float.parseFloat(String.valueOf(nuevoValorDesafio)));
    }

    private void crearSeriesRepeticiones(Desafio desafio, int series, int repeticiones) {
        for (int h = 0; h < series; h++) {
            Serie serie = new Serie(0, desafio);
            serie = serieCRUD.insertarSerie(serie);

            for (int j = 0; j < repeticiones; j++) {
                Repeticiones addRepeticiones = new Repeticiones(0, serie, 0);
                repeticionesCRUD.insertarRepeticion(addRepeticiones);
            }
        }
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

    private void cargarRuta() {
        googleMap.clear();
        options = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);

        SharedPreferences prefs = getActivity().getSharedPreferences("cordenadas", Context.MODE_PRIVATE);
        String misCordenadas = prefs.getString("misCordenadas", "nada");

        if (misCordenadas.equals("nada")) {
        } else {
            String cordenadas[] = misCordenadas.split("X");
            //new Mensaje(getActivity().getApplicationContext(), "porte: " + cordenadas.length);

            for (int i = 0; i < cordenadas.length; i++) {
                String stringLatLong = cordenadas[i];
                double lat = Double.parseDouble(cordenadas[i].split("=")[0]);
                double longi = Double.parseDouble(cordenadas[i].split("=")[1]);
                nuevaCordenada = new LatLng(lat, longi);
                options.add(nuevaCordenada);
                //new Mensaje(getActivity().getApplicationContext(), "Lat.equals(" + lat+")" + " Long.equals("+longi+")");
            }
        }
        line = googleMap.addPolyline(options);

        String prueba = cargarRutaBaseDeDatos();
        if (!prueba.equals("nada")) {
            cargarRutaSeleccionadaSinLimpiar(prueba);
        }

    }

    private void cargarRutaSeleccionada(String stringCoordenadas) {
        googleMap.clear();
        optionsSeleccionada = new PolylineOptions().width(10).color(Color.RED).geodesic(true);

        if (stringCoordenadas.equals("")) {
            new Mensaje(getActivity().getApplicationContext(), "Seleccione una ruta");
        } else {
            String cordenadas[] = stringCoordenadas.split("X");
            //new Mensaje(getActivity().getApplicationContext(), "porte: " + cordenadas.length);

            for (int i = 0; i < cordenadas.length; i++) {
                String stringLatLong = cordenadas[i];
                double lat = Double.parseDouble(cordenadas[i].split("=")[0]);
                double longi = Double.parseDouble(cordenadas[i].split("=")[1]);
                nuevaCordenada = new LatLng(lat, longi);
                optionsSeleccionada.add(nuevaCordenada);
                //new Mensaje(getActivity().getApplicationContext(), "Lat.equals(" + lat+")" + " Long.equals("+longi+")");
            }
        }
        lineSeleccionada = googleMap.addPolyline(optionsSeleccionada);
    }

    private void cargarRutaSeleccionadaSinLimpiar(String stringCoordenadas) {
        optionsSeleccionada = new PolylineOptions().width(10).color(Color.RED).geodesic(true);

        if (stringCoordenadas.equals("")) {
        } else {
            String cordenadas[] = stringCoordenadas.split("X");
            //new Mensaje(getActivity().getApplicationContext(), "porte: " + cordenadas.length);

            for (int i = 0; i < cordenadas.length; i++) {
                String stringLatLong = cordenadas[i];
                double lat = Double.parseDouble(cordenadas[i].split("=")[0]);
                double longi = Double.parseDouble(cordenadas[i].split("=")[1]);
                nuevaCordenada = new LatLng(lat, longi);
                optionsSeleccionada.add(nuevaCordenada);
                //new Mensaje(getActivity().getApplicationContext(), "Lat.equals(" + lat+")" + " Long.equals("+longi+")");
            }
        }
        lineSeleccionada = googleMap.addPolyline(optionsSeleccionada);
    }

    private String cargarEstadoDesafio() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "nada";
        String estadoDesafioRecuperado = sharedPref.getString("estadoDesafio", defaultValue);

        return estadoDesafioRecuperado;
    }

    private String cargarRutaBaseDeDatos() {
        SharedPreferences prefs = getActivity().getSharedPreferences("rutaBaseDeDatos", Context.MODE_PRIVATE);
        String defaultValue = "nada";
        String rutaBaseDeDatos = prefs.getString("coordenadasBaseDeDatos", defaultValue);

        return rutaBaseDeDatos;
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
        editor.putInt("valorDesafioPauseDescanso", cronoServiceValorActualDescansoRepeticion);
        editor.putInt("valorDesafioPauseCronometroSerie", cronometroRepeticion);
        //editor.putInt("valorDesafioPauseCronometroSerie", cronometroRepeticion);
        editor.commit();

        /*SharedPreferences prefs = getActivity().getSharedPreferences("serieRepeticion", Context.MODE_PRIVATE);
        prefs.edit().putInt("serieActual", cronoServiceSerie).commit();
        prefs.edit().putInt("repeticionActual", cronoServiceRepeticion).commit();*/
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

    private int cargarValorEstadoDesafioPauseDescanso() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int defaultValue = 0;
        int estadoDesafioRecuperado = sharedPref.getInt("valorDesafioPauseDescanso", defaultValue);

        return estadoDesafioRecuperado;
    }

    private int cargarValorEstadoDesafioPauseCronometroSerie() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int defaultValue = 0;
        int estadoDesafioRecuperado = sharedPref.getInt("valorDesafioPauseCronometroSerie", defaultValue);

        return estadoDesafioRecuperado;
    }

    public void completarDesafio() {
        actualDesafio.setEstadoDesafio('T');
        actualDesafio.setExitoDesafio(1);
        Date d = Calendar.getInstance().getTime();
        actualDesafio.setTerminoDesafio(new java.sql.Date(d.getTime()));

        try {
            desafioCRUD.actualizarDatosDesafio(actualDesafio);
            //TextViewEstadoActual.setText("Terminado");
            //TextViewEstadoActual.setTextColor(getResources().getColor(R.color.colorVerde));
            ButtonIniciarDesafio.setImageResource(R.drawable.xhdpi_ic_play_arrow_white_24dp);
            ButtonIniciarDesafio.setTag(R.drawable.xhdpi_ic_play_arrow_white_24dp);
            ButtonIniciarDesafio.setEnabled(false);
            //ButtonDetenerRutina.setEnabled(true);
            //ButtonDetenerRutina.setVisibility(View.VISIBLE);
            ButtonDetenerDesafio.setVisibility(View.INVISIBLE);
            TextViewElegirDesafioActual.setEnabled(true);
            TextViewEstadoTerminado.setVisibility(View.VISIBLE);
            cambiarVisibilidadDesafioActual(View.INVISIBLE);
            guardarEstadoDesafioNoPause();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SharedPreferences prefs = getActivity().getSharedPreferences("rutaBaseDeDatos", Context.MODE_PRIVATE);
        prefs.edit().putString("coordenadasBaseDeDatos", "nada").commit();

        new Mensaje(getActivity().getApplicationContext(), "Desafio terminado");
        guardarEstadoDesafioDetenido();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        Cronometro.setUpdateListener(null, tiempoLimiteTotal);
        //guardarEstadoDesafio("detenido");
    }

    private void iniciarCronometro() {
        Intent service = new Intent(getActivity().getBaseContext(), Cronometro.class);
        getActivity().startService(service);
        //new Mensaje(getActivity().getApplicationContext(), "No dramas al iniciar");
    }

    private void pararCronometro() {
        Intent service = new Intent(getActivity().getBaseContext(), Cronometro.class);
        getActivity().stopService(service);
       /* final MediaPlayer testSound = MediaPlayer.create(getActivity().getApplicationContext(), R.drawable.desafio_terminado);
        testSound.setVolume(15.0f, 15.0f);
        testSound.start();*/
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
        if (getDetalleFragment().isAdded()) {
            detalleFragment = null;
        }
        //Cronometro.setUpdateListener(this);
        estado = true;

        //Esta parte es nueva
        SharedPreferences prefs = getActivity().getSharedPreferences("tiempoFinalTerminado", Context.MODE_PRIVATE);
        int cronometroFinalDesafio = prefs.getInt("tiempoFinalCronometro", -1);

        if (!(cronometroFinalDesafio == -1)) {
            completarDesafio();
            prefs = getActivity().getSharedPreferences("tiempoFinalTerminado", Context.MODE_PRIVATE);
            prefs.edit().putInt("tiempoFinalCronometro", -1).commit();

            DialogoNombreRuta dialogo = new DialogoNombreRuta();
            dialogo.show(getFragmentManager(), "nombreRutaPicker");
        }

        //Esta parte es nueva
    }

    public void actualizarCronometro(int tiempo, int tiempoSerie, int tiempoCronometroDescanso) {
        intValorCronometro = tiempo;
        cronometroRepeticion = tiempoSerie;
        cronoServiceValorActualDescansoRepeticion = tiempoCronometroDescanso;
        textoCronometro.setText(segundosToHoras(tiempo));
    }

    public void actualizarColorCronometro(int valorColor) {
        if (valorColor == 1) {
            textoCronometro.setTextColor(getResources().getColor(R.color.colorVerde));
        } else if (valorColor == 2) {
            textoCronometro.setTextColor(getResources().getColor(R.color.colorRojo));
        } else {
            textoCronometro.setTextColor(getResources().getColor(R.color.colorAzul));
        }
    }

    public void actualizarSeriesRepeticiones(int seriesCrono, int repeticionesCrono, int numeroRepeticionCrono) {
        cronoServiceSerie = seriesCrono;
        cronoServiceNumeroRepeticion = numeroRepeticionCrono;
        cronoServiceRepeticion = repeticionesCrono;
        //TextViewSeries.setText("Series: " + String.valueOf(seriesCrono) + "/" + String.valueOf(seriesTotal));
        //TextViewRepeticiones.setText("Repeticiones: " + String.valueOf(repeticionesCrono) + "/" + String.valueOf(repeticionesTotal));
        seriesActual = seriesCrono;
        repeticionesActual = repeticionesCrono;
    }

    public void actualizarSeriesRepeticionesDefault(int seriesCrono, int repeticionesCrono) {
        //TextViewSeries.setText("Series: " + String.valueOf(seriesCrono) + "/" + String.valueOf(seriesTotal));
        //TextViewRepeticiones.setText("Repeticiones: " + String.valueOf(repeticionesCrono) + "/" + String.valueOf(repeticionesTotal));
        seriesActual = seriesCrono;
        repeticionesActual = repeticionesCrono;
    }

    public void mostrarCordenadas(double latitud, double longitud) {
        //new Mensaje(getActivity().getApplicationContext(), "Cordenadas: " + latitud + ", " + longitud);
        nuevaCordenada = new LatLng(latitud, longitud);
        options.add(nuevaCordenada);
        line = googleMap.addPolyline(options);
    }

    public void actualizarValorDesafio(float distancia) {
        DecimalFormat df = new DecimalFormat("#.#");
        if (distancia >= 1000) {
            TextViewValorDesafioActual.setText(String.valueOf(convertirMetrosToKilometros(distancia)) + " Km" /*+ " / " + String.valueOf(convertirMetrosToKilometros(desafioObjetivo.getValor())) + " Km"*/);
        } else {
            TextViewValorDesafioActual.setText(String.valueOf(df.format(distancia)) + " m" /*+ " / " + String.valueOf(convertirMetrosToKilometros(desafioObjetivo.getValor())) + " Km"*/);
        }

    }

    public float convertirMetrosToKilometros(float metros) {
        DecimalFormat df = new DecimalFormat("#.#");
        return Float.parseFloat(df.format(metros / 1000));
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
        serieCRUD = new SerieCRUD(getActivity().getApplicationContext());
        repeticionesCRUD = new RepeticionesCRUD(getActivity().getApplicationContext());
        desafioCRUD = new DesafioCRUD(getActivity().getApplicationContext());
        desafioObjetivoCRUD = new DesafioObjetivoCRUD(getActivity().getApplicationContext());
        objetivoCRUD = new ObjetivoCRUD(getActivity().getApplicationContext());
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
        //ButtonSeleccionarRuta = (ImageButton) root.findViewById(R.id.ButtonSeleccionarRuta);
        ButtonIniciarRutina = (ImageButton) root.findViewById(R.id.ButtonIniciarRutina);
        //ButtonDetenerRutina = (ImageButton) root.findViewById(R.id.ButtonDetenerRutina);
        ButtonIniciarDesafio = (ImageButton) root.findViewById(R.id.ButtonIniciarDesafio);
        //ButtonCrearRutinaFlash = (Button) root.findViewById(R.id.ButtonCrearRutinaFlash);
        ButtonDetenerDesafio = (ImageButton) root.findViewById(R.id.ButtonDetenerDesafio);
        //ButtonPrueba = (Button) root.findViewById(R.id.ButtonPrueba);
        ButtonDetalleRutina = (Button) root.findViewById(R.id.ButtonDetalleRutina);

        textoCronometro = (TextView) root.findViewById(R.id.cronometro);
        textoCronometro.setText("00:00:00");

        ButtonIniciarDesafio.setEnabled(false);
        ButtonIniciarDesafio.setTag(R.drawable.xhdpi_ic_play_arrow_white_24dp);

        //ButtonDetenerRutina.setEnabled(false);
        //ButtonDetenerRutina.setVisibility(View.INVISIBLE);

        ButtonIniciarRutina.setEnabled(false);
        ButtonIniciarRutina.setVisibility(View.INVISIBLE);

        ButtonDetenerDesafio.setVisibility(View.INVISIBLE);

        //ImageViewImagenDesafio2 = (ImageView) root.findViewById(R.id.ImageViewImagenDesafio2);
        ImageViewImagenDesafioActual = (ImageView) root.findViewById(R.id.ImageViewImagenDesafioActual);

        //TextViewNombreDesafio = (TextView) root.findViewById(R.id.TextViewNombreDesafio);
        TextViewNombreDesafioActual = (TextView) root.findViewById(R.id.TextViewNombreDesafioActual);

        //TextViewCategoriaDesafio = (TextView) root.findViewById(R.id.TextViewCategoriaDesafio);
        //TextViewCategoriaDesafioActual = (TextView) root.findViewById(R.id.TextViewCategoriaDesafioActual);

        //TextViewValorDesafio = (TextView) root.findViewById(R.id.TextViewValorDesafio);
        TextViewValorDesafioActual = (TextView) root.findViewById(R.id.TextViewValorDesafioActual);

        //TextViewNotaDesafio = (TextView) root.findViewById(R.id.TextViewNotaDesafio);
        //TextViewNotaDesafioActual = (TextView) root.findViewById(R.id.TextViewNotaDesafioActual);

        //TextViewFechaDesafio = (TextView) root.findViewById(R.id.TextViewFechaDesafio);
        //TextViewFechaDesafioActual = (TextView) root.findViewById(R.id.TextViewFechaDesafioActual);

        //TextViewEstado = (TextView) root.findViewById(R.id.TextViewEstado);
        //TextViewEstadoActual = (TextView) root.findViewById(R.id.TextViewEstadoActual);
        TextViewEstadoTerminado = (TextView) root.findViewById(R.id.TextViewEstadoTerminado);

        //TextViewElegirRutina = (TextView) root.findViewById(R.id.TextViewElegirRutina);
        TextViewElegirDesafioActual = (TextView) root.findViewById(R.id.TextViewElegirDesafioActual);

        //TextViewSeries = (TextView) root.findViewById(R.id.TextViewSeries);
        //TextViewRepeticiones = (TextView) root.findViewById(R.id.TextViewRepeticiones);

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

            //new Mensaje(getActivity().getApplicationContext(), "Rutina en curso");
            actualRutina = listaRutinasIniciadas.get(0);
            //TextViewElegirRutina.setText("");
            //TextViewElegirRutina.setEnabled(false);
            estadoElegirRutina = false;
            //ButtonCrearRutinaFlash.setEnabled(false);
            //ButtonCrearRutinaFlash.setVisibility(View.INVISIBLE);

            TextViewElegirDesafioActual.setText("");

            //ButtonDetenerRutina.setEnabled(true);
            //ButtonDetenerRutina.setVisibility(View.VISIBLE);

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

            cargarDesafio(String.valueOf(R.drawable.mdpi_ic_place_black_24dp));

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
            //TextViewElegirRutina.setText("");
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
            cargarDesafio(String.valueOf(R.drawable.mdpi_ic_place_black_24dp));
            iniciarRutina();
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
                    //new Mensaje(getActivity().getApplicationContext(), "Aca toy");
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

            List<Serie> seriesDesafio = new ArrayList<>();
            List<Repeticiones> repeticionesDesafio = new ArrayList<>();

            try {
                seriesDesafio = serieCRUD.buscarSeriePorIdDesafio(desafioObjetivo.getDesafio());
                if (!seriesDesafio.isEmpty()) {
                    repeticionesDesafio = repeticionesCRUD.buscarRepeticionesPorIdSerie(seriesDesafio.get(0));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            seriesTotal = seriesDesafio.size();
            repeticionesTotal = repeticionesDesafio.size();

            cambiarVisibilidadDesafioActual(View.VISIBLE);
            cargarDatosDesafioActual(Integer.parseInt(imagen), actualDesafio.getDesafioNombre(), desafioObjetivo.getObjetivo().getObjetivoNombre(), String.valueOf(Math.round(desafioObjetivo.getValor())),
                    actualDesafio.getDesafioDescripcion(),
                    format.format(actual),
                    estadoDesafioCargado,
                    seriesTotal,
                    repeticionesTotal);

            if (!listaRutinasIniciadas.isEmpty()) {
                ButtonIniciarDesafio.setEnabled(true);
            }

            if (estadoDesafioCargado.equals("Terminado")) {
                ButtonIniciarDesafio.setEnabled(false);
                TextViewElegirDesafioActual.setEnabled(true);
                TextViewEstadoTerminado.setVisibility(View.VISIBLE);
                cambiarVisibilidadDesafioActual(View.INVISIBLE);
            }
            TextViewElegirDesafioActual.setEnabled(true);
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
                    listaDesafiosRutina.get(i).getDesafio().setExitoDesafio(0);
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
        //ImageViewImagenDesafio2.setVisibility(visible);
        //TextViewNombreDesafio.setVisibility(visible);

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
        //TextViewNombreDesafioActual.setVisibility(visible);
        //TextViewCategoriaDesafioActual.setVisibility(visible);
        TextViewValorDesafioActual.setVisibility(visible);

        //TextViewRepeticiones.setVisibility(visible);
        //TextViewSeries.setVisibility(visible);
        //.setVisibility(visible);
        //TextViewFechaDesafioActual.setVisibility(visible);
        //TextViewEstadoActual.setVisibility(visible);
    }

    private void cargarDatosDesafioActual(int ic_grade_black_48dp, String desafioNombre, String objetivoNombre, String valorDesafio, String desafioDescripcion, String fecha, String estado, int series, int repeticiones) {
        ImageViewImagenDesafioActual.setImageResource(ic_grade_black_48dp);
        TextViewNombreDesafioActual.setText(desafioNombre);
        //TextViewCategoriaDesafioActual.setText(objetivoNombre);
        if (valorDesafio.equals("")) {
            //TextViewValorDesafioActual.setText(String.valueOf(valorDesafio));
            TextViewValorDesafioActual.setText(String.valueOf("0.0 Km"));
        } else {
            //TextViewValorDesafioActual.setText(String.valueOf(convertirMetrosToKilometros(Float.parseFloat(valorDesafio))) + " Km");
            TextViewValorDesafioActual.setText(String.valueOf("0.0") + " Km");
        }

        //TextViewNotaDesafioActual.setText(desafioDescripcion);
        //TextViewFechaDesafioActual.setText(fecha);
        //TextViewEstadoActual.setText(estado);

        if (series != 0) {
            //TextViewSeries.setText("Series: 0/" + series);
            //TextViewRepeticiones.setText("Repeticiones: 0/" + repeticiones);
        } else {
            //TextViewSeries.setText(estado);
            //TextViewRepeticiones.setText("");
        }

        if (estado.equals("Terminado")) {
            //TextViewEstadoActual.setTextColor(getResources().getColor(R.color.colorVerde));
        }
    }

    private void cargarDatosRutina(int imagen, String rutinaNombre, String desafios, String valorDesafio, String rutinaDescripcion, String fecha, String estado) {
        cambiarVisibilidadRutina(View.VISIBLE);
        //ImageViewImagenDesafio2.setImageResource(imagen);
        //TextViewNombreDesafio.setText(rutinaNombre);
        //TextViewCategoriaDesafio.setText(desafios);
        //TextViewValorDesafio.setText(valorDesafio);
        //TextViewNotaDesafio.setText(rutinaDescripcion);
        //TextViewFechaDesafio.setText(fecha);

        if (estado.equals("Iniciada")) {
            //TextViewEstado.setTextColor(getResources().getColor(R.color.colorVerde));
        } else {
            //TextViewEstado.setTextColor(getResources().getColor(R.color.colorMorado));
        }
        //TextViewEstado.setText(estado);
    }

    public void terminarRutina(String data) {
        actualRutina.setRutinaEstado('T');
        cambiarVisibilidadDesafioActual(View.INVISIBLE);
        cambiarVisibilidadRutina(View.INVISIBLE);
        //TextViewElegirRutina.setText("Seleccionar rutina");
        //TextViewElegirRutina.setEnabled(true);
        estadoElegirRutina = true;
        //ButtonCrearRutinaFlash.setEnabled(true);
        //ButtonCrearRutinaFlash.setVisibility(View.VISIBLE);
        TextViewElegirDesafioActual.setText("Desafio actual");
        //ButtonDetenerRutina.setEnabled(false);
        //ButtonDetenerRutina.setVisibility(View.INVISIBLE);
        TextViewEstadoTerminado.setVisibility(View.INVISIBLE);

        try {
            rutinaCRUD.actualizarRutina(actualRutina);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < listaDesafiosRutina.size(); i++) {
            listaDesafiosRutina.get(i).getDesafio().setEstadoDesafio('T');
            listaDesafiosRutina.get(i).getDesafio().setExitoDesafio(1);

            try {
                desafioCRUD.actualizarDatosDesafio(listaDesafiosRutina.get(i).getDesafio());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (data.equals("Aceptar")) {
            /*String idRutina = String.valueOf(listaRutinasIniciadas.get(0).getRutinaId());
            Intent newIntent = new Intent(getActivity().getApplicationContext(), ActivityRutinaOpciones.class);
            newIntent.putExtra("idRutina", idRutina);
            startActivity(newIntent);*/

            String idRutina = String.valueOf(actualRutina.getRutinaId());
            Intent newIntent = new Intent(getActivity().getApplicationContext(), ActivityProgresoRutina.class);
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
            //TextViewElegirDesafioActual.setText("Sin desafio actual");
            TextViewElegirDesafioActual.setText("");
            TextViewElegirDesafioActual.setEnabled(false);
            //TextViewElegirRutina.setText("Seleccionar rutina");
            //TextViewElegirRutina.setEnabled(true);
            estadoElegirRutina = true;
            //ButtonCrearRutinaFlash.setEnabled(true);
            //ButtonCrearRutinaFlash.setVisibility(View.VISIBLE);
            TextViewElegirDesafioActual.setText("");
            //ButtonDetenerRutina.setEnabled(false);
            //ButtonDetenerRutina.setVisibility(View.INVISIBLE);
            ButtonIniciarDesafio.setEnabled(false);

            try {
                rutinaCRUD.actualizarRutina(actualRutina);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < listaDesafiosRutina.size(); i++) {
                listaDesafiosRutina.get(i).getDesafio().setEstadoDesafio('T');
                listaDesafiosRutina.get(i).getDesafio().setExitoDesafio(0);

                try {
                    desafioCRUD.actualizarDatosDesafio(listaDesafiosRutina.get(i).getDesafio());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            String idRutina = String.valueOf(actualRutina.getRutinaId());
            Intent newIntent = new Intent(getActivity().getApplicationContext(), ActivityProgresoRutina.class);
            newIntent.putExtra("idRutina", idRutina);
            startActivity(newIntent);
        } else {
        }
    }

    public void diasSeleccionados(ArrayList<Integer> data) {
        diasSelecionados = data;
        if (!diasSelecionados.isEmpty()) {
            crearRutinaFlash();
            iniciarRutina();
        } else {
            new Mensaje(getActivity().getApplicationContext(), "Selecciones al menos 1 dia");
        }
    }

    public void actualizarRuta(String data) {
        cargarRutaSeleccionada(data);
        rutaBaseDeDatos = data;
    }

    public void guardarRuta(String data) {
        new Mensaje(getActivity().getApplicationContext(), data);
        guardarCordenadasBaseDatos(data);
    }

    private void guardarCordenadasBaseDatos(String nombreRuta) {
        SharedPreferences prefs = getActivity().getSharedPreferences("cordenadasFinales", Context.MODE_PRIVATE);
        String cordenadasFinales = prefs.getString("misCordenadasFinales", "nada");

        if (!cordenadasFinales.equals("nada")) {
            RutaCRUD rutaCRUD = new RutaCRUD(getActivity().getApplicationContext());
            Ruta nuevaRuta = new Ruta();
            nuevaRuta.setRutaNombre(nombreRuta);
            nuevaRuta.setRutaCordenadas(cordenadasFinales);
            nuevaRuta = rutaCRUD.insertarRuta(nuevaRuta);
        }

        prefs = getActivity().getSharedPreferences("cordenadasFinales", Context.MODE_PRIVATE);
        prefs.edit().putString("misCordenadasFinales", "nada").commit();
    }

    public DetalleFragment getDetalleFragment() {
        if (detalleFragment == null) {
            detalleFragment = new DetalleFragment();
        }
        return detalleFragment;
    }

    /*
    public void cargarFragmento(Fragment fragment, int animacionSalida, int animacioEntrada) {
        if (!fragment.isAdded()){
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setCustomAnimations(animacionSalida, animacioEntrada, animacionSalida, animacioEntrada);
            transaction.add(R.id.contenedorDetalle, fragment, "otroTag");
            transaction.commit();
            mostrado = true;
        }
    }

    public void ocultar(){
        ocultarFragmento(getDetalleFragment(), R.anim.activity_visible_salida_abajo, R.anim.activity_nuevo_entrada_arriba);
    }

    public void ocultarFragmento(Fragment fragment, int animacionSalida, int animacioEntrada) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(animacionSalida, animacioEntrada, animacionSalida, animacioEntrada);
        transaction.remove(fragment);
        transaction.commit();
        mostrado = false;
    }
    */
}
