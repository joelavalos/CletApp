package com.example.joel.cletapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joel.cletapp.CRUDDatabase.DesafioRutinaCRUD;
import com.example.joel.cletapp.CRUDDatabase.ResumenCRUD;
import com.example.joel.cletapp.CRUDDatabase.RutinaCRUD;
import com.example.joel.cletapp.ClasesDataBase.DesafioRutina;
import com.example.joel.cletapp.ClasesDataBase.Rutina;
import com.example.joel.cletapp.Communicator;
import com.example.joel.cletapp.Mensaje;
import com.example.joel.cletapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 18/08/2015.
 */
public class DialogoRutinaSelector extends DialogFragment {
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private View v, v2;

    private RutinaCRUD rutinaCRUD;
    private DesafioRutinaCRUD desafioRutinaCRUD;
    private ResumenCRUD resumenCRUD;
    private List<Rutina> listaRutinasPendientes;
    private List<DesafioRutina> listaDesafioRutinas;

    private Communicator comm;
    private String Datos = "";

    private ListView ListViewRutinasCreadas;
    private AdapterListaRutinas adapterListaRutinas;

    private ArrayList<Integer> imagenes = new ArrayList<>();
    private ArrayList<String> nombreRutinas = new ArrayList<>();
    private ArrayList<String> categoriaRutinas = new ArrayList<>();
    private ArrayList<String> valorRutinas = new ArrayList<>();
    private ArrayList<String> notaRutinas = new ArrayList<>();
    private ArrayList<String> fechaRutinas = new ArrayList<>();
    private ArrayList<String> estadoRutinas = new ArrayList<>();
    private ArrayList<String> idRutinas = new ArrayList<>();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapterListaRutinas = new AdapterListaRutinas(getActivity().getApplicationContext(), imagenes, nombreRutinas, categoriaRutinas, valorRutinas, notaRutinas, fechaRutinas, estadoRutinas, idRutinas);
        ListViewRutinasCreadas.setAdapter(adapterListaRutinas);

        v = new View(this.getActivity());
        v2 = new View(this.getActivity());

        ListViewRutinasCreadas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                v.setBackgroundResource(0);
                view.setBackgroundResource(R.color.colorSeleccionado);
                v = view;

                Datos = (String) parent.getAdapter().getItem(position);
            }
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        comm = (Communicator) getActivity();
        rutinaCRUD = new RutinaCRUD(getActivity().getApplicationContext());
        desafioRutinaCRUD = new DesafioRutinaCRUD(getActivity().getApplicationContext());
        resumenCRUD = new ResumenCRUD(getActivity().getApplicationContext());
        listaRutinasPendientes = new ArrayList<>();
        listaDesafioRutinas = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View viewDialogo = inflater.inflate(R.layout.activity_rutinas_dialogo, null);

        listaRutinasPendientes = new ArrayList<>();
        idRutinas = new ArrayList<>();
        imagenes = new ArrayList<>();
        nombreRutinas = new ArrayList<>();
        notaRutinas = new ArrayList<>();
        fechaRutinas = new ArrayList<>();
        estadoRutinas = new ArrayList<>();
        valorRutinas = new ArrayList<>();
        categoriaRutinas = new ArrayList<>();

        listaRutinasPendientes = rutinaCRUD.buscarTodasLasRutinasPendientes();

        for (int i = 0; i < listaRutinasPendientes.size(); i++) {
            try {
                listaDesafioRutinas = desafioRutinaCRUD.buscarDesafioRutinaPorIdRutina(listaRutinasPendientes.get(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (listaDesafioRutinas.isEmpty()) {
                resumenCRUD.eliminarResumen(listaRutinasPendientes.get(i).getResumen());
                rutinaCRUD.eliminarRutina(listaRutinasPendientes.get(i));
            } else {
                idRutinas.add(String.valueOf(listaRutinasPendientes.get(i).getRutinaId()));
                imagenes.add(R.drawable.ic_directions_bike_black_48dp);
                nombreRutinas.add(listaRutinasPendientes.get(i).getRutinaNombre());
                notaRutinas.add(listaRutinasPendientes.get(i).getRutinaDescripcion());
                fechaRutinas.add("Desde: " + format.format(listaRutinasPendientes.get(i).getRutinaInicio()) + " hasta: " + format.format(listaRutinasPendientes.get(i).getRutinaTermino()));

                if (listaRutinasPendientes.get(i).getRutinaEstado() == 'P') {
                    estadoRutinas.add("Pendiente");
                } else {
                    estadoRutinas.add("Terminada");
                }

                valorRutinas.add(String.valueOf(Math.round(listaDesafioRutinas.size())));
                categoriaRutinas.add("Desafios");
            }
        }
        ListViewRutinasCreadas = (ListView) viewDialogo.findViewById(R.id.ListViewRutinasCreadas);

        builder.setTitle("Rutinas")
                .setView(viewDialogo)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        comm.Actualizar(Datos);

                        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("detalleRutina");
                        if (prev != null) {
                            DialogFragment df = (DialogFragment) prev;
                            df.dismiss();
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

class AdapterRutinaRow {

    ImageView ImageViewImagenRutina;
    TextView TextViewNombreRutina;
    TextView TextViewCategoriaRutina;
    TextView TextViewValorRutina;
    TextView TextViewNotaRutina;
    TextView TextViewFechaRutina;
    TextView TextViewEstadoRutina;

    public AdapterRutinaRow(View view) {
        ImageViewImagenRutina = (ImageView) view.findViewById(R.id.ImageViewImagenRutina);
        TextViewNombreRutina = (TextView) view.findViewById(R.id.TextViewNombreRutina);
        TextViewCategoriaRutina = (TextView) view.findViewById(R.id.TextViewCategoriaRutina);
        TextViewValorRutina = (TextView) view.findViewById(R.id.TextViewValorRutina);
        TextViewNotaRutina = (TextView) view.findViewById(R.id.TextViewNotaRutina);
        TextViewFechaRutina = (TextView) view.findViewById(R.id.TextViewFechaRutina);
        TextViewEstadoRutina = (TextView) view.findViewById(R.id.TextViewEstadoRutina);
    }
}

class AdapterListaRutinas extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> idRutinas;
    ArrayList<Integer> images = new ArrayList<>();
    ArrayList<String> nombreRutina = new ArrayList<>();
    ArrayList<String> categoriaRutina = new ArrayList<>();
    ArrayList<String> valorRutina = new ArrayList<>();
    ArrayList<String> notaRutina = new ArrayList<>();
    ArrayList<String> fechaRutina = new ArrayList<>();
    ArrayList<String> estadoRutina = new ArrayList<>();

    public AdapterListaRutinas(Context c, ArrayList<Integer> ima, ArrayList<String> nombres, ArrayList<String> categorias, ArrayList<String> valores, ArrayList<String> notas, ArrayList<String> fechas, ArrayList<String> estados, ArrayList<String> IDs) {
        super(c, R.layout.single_desafio_row, R.id.TextViewNombreDesafio, nombres);
        this.context = c;
        images = ima;
        nombreRutina = nombres;
        categoriaRutina = categorias;
        valorRutina = valores;
        notaRutina = notas;
        fechaRutina = fechas;
        estadoRutina = estados;
        idRutinas = IDs;
    }

    @Override
    public String getItem(int position) {
        String returnData = "";
        returnData = idRutinas.get(position);
        returnData = returnData + "-" + images.get(position);
        returnData = returnData + "-" + nombreRutina.get(position);
        returnData = returnData + "-" + categoriaRutina.get(position);
        returnData = returnData + "-" + valorRutina.get(position);
        returnData = returnData + "-" + notaRutina.get(position);
        returnData = returnData + "-" + fechaRutina.get(position);
        returnData = returnData + "-" + estadoRutina.get(position);

        return returnData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        AdapterRutinaRow holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_rutina_row, parent, false);
            holder = new AdapterRutinaRow(row);
            row.setTag(holder);
        } else {
            holder = (AdapterRutinaRow) row.getTag();
        }

        holder.ImageViewImagenRutina.setImageResource(R.drawable.ic_directions_bike_black_48dp);
        holder.TextViewNombreRutina.setText(nombreRutina.get(position));
        holder.TextViewCategoriaRutina.setText(categoriaRutina.get(position));
        holder.TextViewValorRutina.setText(valorRutina.get(position));
        holder.TextViewNotaRutina.setText(notaRutina.get(position));
        holder.TextViewFechaRutina.setText(fechaRutina.get(position));
        holder.TextViewEstadoRutina.setText(estadoRutina.get(position));

        if (estadoRutina.get(position).equals("Pendiente")) {
            holder.TextViewEstadoRutina.setTextColor(context.getResources().getColor(R.color.colorMorado));
        } else if (estadoRutina.get(position).equals("Terminada")) {
            holder.TextViewEstadoRutina.setTextColor(context.getResources().getColor(R.color.colorVerde));
        } else {
            holder.TextViewEstadoRutina.setTextColor(context.getResources().getColor(R.color.colorRojo));
        }

        return row;
    }
}
