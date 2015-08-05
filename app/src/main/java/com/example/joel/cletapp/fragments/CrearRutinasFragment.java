package com.example.joel.cletapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joel.cletapp.CRUDDatabase.DesafioCRUD;
import com.example.joel.cletapp.ClasesDataBase.Desafio;
import com.example.joel.cletapp.Mensaje;
import com.example.joel.cletapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Joel on 04/08/2015.
 */
public class CrearRutinasFragment extends Fragment {
    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    private String[] campos = {"Fecha de inicio", "Fecha final"};
    private String[] valores = {format.format(c.getTime()), format.format(c.getTime())};
    private AdapterCrearRutina adapterCrearRutina;

    private ListView ListViewDesafiosRutina;
    private String[] camposDesafios = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
    private String[] valoresDesafios = {"", "", "", "", "", "", ""};
    private int[] idDesafios = {-1,-1,-1,-1,-1,-1,-1};
    private AdapterDesafio adapterDesafio;

    private GridView GridViewDatosRutina;
    private Button ButtonCrearRutina;
    private EditText EditTextNombreRutina;
    private EditText EditTextNotaRutina;

    private DesafioCRUD desafioCRUD;
    private List<Desafio> listaDesafios;
    private ArrayList<String> listaDesafios2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rutina_crear, container, false);

        Mensaje asd = new Mensaje(getActivity().getApplicationContext(), "Inicializo todo");
        inicializarBaseDeDatos();
        inicializarComponentes(root);

        GridViewDatosRutina.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putStringArray("campos", campos);
                bundle.putStringArray("valores", valores);
                bundle.putStringArray("camposDesafios", camposDesafios);
                bundle.putStringArray("valoresDesafios", valoresDesafios);
                bundle.putIntArray("idDesafios", idDesafios);
                bundle.putInt("posicion", position);

                switch (position) {
                    case 0:
                        DialogFragment pickerInicio = new DialogoDatePickerRutinaFragment();
                        pickerInicio.setArguments(bundle);
                        pickerInicio.show(getFragmentManager(), "datePicker");
                        break;
                }
            }
        });

        ListViewDesafiosRutina.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putStringArray("camposDesafios", camposDesafios);
                bundle.putStringArray("valoresDesafios", valoresDesafios);
                bundle.putIntArray("idDesafios", idDesafios);
                bundle.putStringArrayList("desafios", listaDesafios2);
                bundle.putInt("posicion", position);

                Mensaje asds = new Mensaje(getActivity().getApplicationContext(), listaDesafios2.size()+"");
                Log.v("hola", idDesafios[0] + " " + idDesafios[1] + " " + idDesafios[2] + " " + idDesafios[3] + " " + idDesafios[4] + " " + idDesafios[5] + " " + idDesafios[6]);

                DialogoDesafioSelector dialogo = new DialogoDesafioSelector();
                dialogo.setArguments(bundle);
                dialogo.show(getFragmentManager(), "categoriaPicker");
            }
        });

        return root;
    }

    private void inicializarBaseDeDatos() {
        desafioCRUD = new DesafioCRUD(getActivity().getApplicationContext());
        listaDesafios = desafioCRUD.buscarTodosLosDesafiosPendientes();
        listaDesafios2 = new ArrayList<>();

        listaDesafios2.add("Descansar");
        for (int i = 0; i < listaDesafios.size(); i++) {
            listaDesafios2.add(listaDesafios.get(i).getDesafioId() + "-" + listaDesafios.get(i).getDesafioNombre());
        }
    }

    private void inicializarComponentes(View root) {
        actualizarFechas();
        valores[1] = format.format(c.getTime());

        GridViewDatosRutina = (GridView) root.findViewById(R.id.GridViewDatosRutina);
        ListViewDesafiosRutina = (ListView) root.findViewById(R.id.ListViewDesafiosRutina);
        ButtonCrearRutina = (Button) root.findViewById(R.id.ButtonCrearRutina);
        EditTextNombreRutina = (EditText) root.findViewById(R.id.EditTextNombreRutina);
        EditTextNotaRutina = (EditText) root.findViewById(R.id.EditTextNotaRutina);
        adapterCrearRutina = new AdapterCrearRutina(getActivity().getApplicationContext(), campos, valores);
        GridViewDatosRutina.setAdapter(adapterCrearRutina);

        adapterDesafio = new AdapterDesafio(getActivity().getApplicationContext(), camposDesafios, valoresDesafios, idDesafios);
        ListViewDesafiosRutina.setAdapter(adapterDesafio);
    }

    private void actualizarFechas() {
        Locale spanish = new Locale("es", "PE");
        for (int i = 0; i < camposDesafios.length; i++) {
            camposDesafios[i] = format.format(c.getTime()) + "     " + c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, spanish);

            if ((i + 1) < camposDesafios.length) {
                c.add(Calendar.DATE, +1);
            }
        }
    }
}

class AdapterCrearRutinaRow {

    TextView nombreCampo;
    TextView valorCampo;

    public AdapterCrearRutinaRow(View view) {
        this.nombreCampo = (TextView) view.findViewById(R.id.TextViewNombreCampo);
        this.valorCampo = (TextView) view.findViewById(R.id.TextViewValorCampo);
    }
}

class AdapterCrearRutina extends ArrayAdapter<String> {

    Context context;
    String[] campos;
    String[] valores;

    public AdapterCrearRutina(Context c, String[] listaCampos, String[] listaValores) {
        super(c, R.layout.single_crear_desafio_row, R.id.TextViewNombreCampo, listaCampos);
        this.context = c;
        this.campos = listaCampos;
        this.valores = listaValores;
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AdapterCrearRutinaRow holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_crear_desafio_row, parent, false);
            holder = new AdapterCrearRutinaRow(row);
            row.setTag(holder);
        } else {
            holder = (AdapterCrearRutinaRow) row.getTag();
        }

        holder.nombreCampo.setText(campos[position]);
        holder.valorCampo.setText(valores[position]);

        return row;
    }
}

class AdapterDesafioRow {

    TextView nombreCampo;
    TextView valorCampo;

    public AdapterDesafioRow(View view) {
        this.nombreCampo = (TextView) view.findViewById(R.id.TextViewNombreCampo);
        this.valorCampo = (TextView) view.findViewById(R.id.TextViewValorCampo);
    }
}

class AdapterDesafio extends ArrayAdapter<String> {

    Context context;
    String[] campos;
    String[] valores;
    int[] idDesafio;

    public AdapterDesafio(Context c, String[] listaCampos, String[] listaValores, int[] listaIdDesafios) {
        super(c, R.layout.single_perfil_row, R.id.TextViewNombreCampo, listaCampos);
        this.context = c;
        this.campos = listaCampos;
        this.valores = listaValores;
        this.idDesafio = listaIdDesafios;
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AdapterDesafioRow holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_perfil_row, parent, false);
            holder = new AdapterDesafioRow(row);
            row.setTag(holder);
        } else {
            holder = (AdapterDesafioRow) row.getTag();
        }

        holder.nombreCampo.setText(campos[position]);
        holder.valorCampo.setText(valores[position]);

        return row;
    }
}
