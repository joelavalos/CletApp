package com.example.joel.cletapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joel.cletapp.DataBaseAdapter;
import com.example.joel.cletapp.Mensaje;
import com.example.joel.cletapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 21/07/2015.
 */
public class PerfilFragment extends Fragment {

    private Button ButtonGuardarPerfil;
    private ListView ListViewDatosPerfil;

    private String[] campos = {"Rut", "Nombre", "Apellido", "Fecha de nacimiento", "Peso", "Altura", "Sexo"};
    private String[] valores = {"17.409.487-k", "Joel", "Avalos", "17/09/1990", "64 Kg", "1.95 m", "Hombre"};
    private String[] valoresActualizados;

    DataBaseAdapter dataBaseAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Perfil");
        ((ActionBarActivity) getActivity()).getSupportActionBar().setIcon(null);

        ButtonGuardarPerfil = (Button) root.findViewById(R.id.ButtonGuardarPerfil);
        ListViewDatosPerfil = (ListView) root.findViewById(R.id.ListViewDatosPerfil);

        AdapterPerfil adapterPerfil = new AdapterPerfil(getActivity().getApplicationContext(), campos, valores);
        ListViewDatosPerfil.setAdapter(adapterPerfil);

        dataBaseAdapter = new DataBaseAdapter(getActivity().getApplicationContext());
        getSinglePerson(root);

        Mensaje qwe = new Mensaje(getActivity().getApplicationContext(), "FragmentPerfil creado");

        ButtonGuardarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdapterPerfil adapterPerfilActualizado = (AdapterPerfil) ListViewDatosPerfil.getAdapter();
                valoresActualizados = adapterPerfilActualizado.getValores();
                Long id = addUser(valoresActualizados[0], valoresActualizados[1], valoresActualizados[2], valoresActualizados[3], valoresActualizados[4], valoresActualizados[5], valoresActualizados[6]);

                if (id < 0) {
                    Mensaje qwe = new Mensaje(getActivity().getApplicationContext(), "Error al insertar " + id);
                } else {
                    Mensaje qwe = new Mensaje(getActivity().getApplicationContext(), "Perfil actualizado");
                }
            }
        });

        ListViewDatosPerfil.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle = new Bundle();
                bundle.putStringArray("campos", campos);
                bundle.putStringArray("valores", valores);
                bundle.putInt("posicion", position);
                DialogoInputText dialogo = new DialogoInputText();
                DialogoPesoEstatura dialogoPesoEstatura = new DialogoPesoEstatura();

                switch (position) {
                    case 1:
                        dialogo = new DialogoInputText();
                        dialogo.setArguments(bundle);
                        dialogo.show(getFragmentManager(), "datePicker");
                        break;
                    case 2:
                        dialogo = new DialogoInputText();
                        dialogo.setArguments(bundle);
                        dialogo.show(getFragmentManager(), "datePicker");
                        break;
                    case 3:
                        DialogFragment picker = new DialogoDatePickerFragment();
                        picker.setArguments(bundle);
                        picker.show(getFragmentManager(), "datePicker");
                        break;
                    case 4:
                        dialogoPesoEstatura = new DialogoPesoEstatura();
                        dialogoPesoEstatura.setArguments(bundle);
                        dialogoPesoEstatura.show(getFragmentManager(), "datePicker");
                        break;
                    case 5:
                        dialogoPesoEstatura = new DialogoPesoEstatura();
                        dialogoPesoEstatura.setArguments(bundle);
                        dialogoPesoEstatura.show(getFragmentManager(), "datePicker");
                        break;
                    case 6:
                        DialogoListSelector dialogo2 = new DialogoListSelector();
                        dialogo2.setArguments(bundle);
                        dialogo2.show(getFragmentManager(), "datePicker");
                        break;
                }
            }
        });

        return root;
    }

    public long addUser(String rut, String nombre, String apellido_pat, String fecha, String altura, String peso, String sexo) {
        String[] valorAltura = altura.split(" ");
        String[] valorPeso = peso.split(" ");
        return dataBaseAdapter.insertData(rut, nombre, apellido_pat, fecha, valorAltura[0], valorPeso[0], sexo);
    }

    public void viewDetails(View view) {
        String data = dataBaseAdapter.getAllData();
        if (data.equals("")) {
            Mensaje qwe = new Mensaje(getActivity().getApplicationContext(), "No hay datos");
        } else {
            String[] datosGuardados = data.split(" ");
            for (int i = 0; i < valores.length; i++) {
                valores[i] = datosGuardados[i];
            }
            valores[4] = valores[4] + " Kg";
            valores[5] = valores[5] + " m";
            datosGuardados = valores[6].split("\n");
            valores[6] = datosGuardados[0];
            Mensaje qwe = new Mensaje(getActivity().getApplicationContext(), data);
        }
    }

    public void getSinglePerson(View view) {
        List<String> data = new ArrayList<String>();
        data = dataBaseAdapter.getData("0");

        if (data.isEmpty()) {
            Mensaje qwe = new Mensaje(getActivity().getApplicationContext(), "No hay datos");
            addUser("0", "", "", "", "", "", "");
            data = dataBaseAdapter.getData("0");

            for (int i = 0; i < valores.length; i++) {
                valores[i] = data.get(i);
            }
            valores[4] = valores[4] + " Kg";
            valores[5] = valores[5] + " m";

        } else {
            for (int i = 0; i < valores.length; i++) {
                valores[i] = data.get(i);
            }
            valores[4] = valores[4] + " Kg";
            valores[5] = valores[5] + " m";
            Mensaje qwe = new Mensaje(getActivity().getApplicationContext(), "Si hay datos");
        }
    }
}


class AdapterPerfilRow {

    TextView nombreCampo;
    TextView valorCampo;

    public AdapterPerfilRow(View view) {
        this.nombreCampo = (TextView) view.findViewById(R.id.TextViewNombreCampo);
        this.valorCampo = (TextView) view.findViewById(R.id.TextViewValorCampo);
    }
}

class AdapterPerfil extends ArrayAdapter<String> {

    Context context;
    String[] campos;
    String[] valores;

    public AdapterPerfil(Context c, String[] listaCampos, String[] listaValores) {
        super(c, R.layout.single_glosario_row, R.id.TextViewNombreCampo, listaCampos);
        this.context = c;
        this.campos = listaCampos;
        this.valores = listaValores;
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    public String[] getCampos() {
        return campos;
    }

    public String[] getValores() {
        return valores;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        AdapterPerfilRow holder = null;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_perfil_row, parent, false);
            holder = new AdapterPerfilRow(row);
            row.setTag(holder);
        } else {
            holder = (AdapterPerfilRow) row.getTag();
        }

        holder.nombreCampo.setText(campos[position]);
        holder.valorCampo.setText(valores[position]);

        return row;
    }
}
