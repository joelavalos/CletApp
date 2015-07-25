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

    private String[] campos = {"Nombre", "Apellido", "Fecha de nacimiento", "Peso", "Altura", "Sexo"};
    private String[] valores = {"Joel", "Avalos", "17/09/1990", "64 Kg", "1.95 m", "Hombre"};
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
                int id = updateData("0", valoresActualizados[0], valoresActualizados[1], valoresActualizados[2], valoresActualizados[3], valoresActualizados[4], valoresActualizados[5]);
                //Long id = addUser(valoresActualizados[0], valoresActualizados[1], valoresActualizados[2], valoresActualizados[3], valoresActualizados[4], valoresActualizados[5], valoresActualizados[6]);

                if (id < 0) {
                    Mensaje qwe = new Mensaje(getActivity().getApplicationContext(), "Error al actualizar " + id);
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
                    case 0:
                        dialogo = new DialogoInputText();
                        dialogo.setArguments(bundle);
                        dialogo.show(getFragmentManager(), "datePicker");
                        break;
                    case 1:
                        dialogo = new DialogoInputText();
                        dialogo.setArguments(bundle);
                        dialogo.show(getFragmentManager(), "datePicker");
                        break;
                    case 2:
                        DialogFragment picker = new DialogoDatePickerFragment();
                        picker.setArguments(bundle);
                        picker.show(getFragmentManager(), "datePicker");
                        break;
                    case 3:
                        dialogoPesoEstatura = new DialogoPesoEstatura();
                        dialogoPesoEstatura.setArguments(bundle);
                        dialogoPesoEstatura.show(getFragmentManager(), "datePicker");
                        break;
                    case 4:
                        dialogoPesoEstatura = new DialogoPesoEstatura();
                        dialogoPesoEstatura.setArguments(bundle);
                        dialogoPesoEstatura.show(getFragmentManager(), "datePicker");
                        break;
                    case 5:
                        DialogoListSelector dialogo2 = new DialogoListSelector();
                        dialogo2.setArguments(bundle);
                        dialogo2.show(getFragmentManager(), "datePicker");
                        break;
                }
            }
        });

        return root;
    }

    public long addUser(String rut, String nombre, String apellido_pat, String fecha, String peso, String altura, String sexo) {
        String[] valorAltura = altura.split(" ");
        String[] valorPeso = peso.split(" ");

        sexo = sexoToLetra(sexo);

        return dataBaseAdapter.insertarPerfil(rut, nombre, apellido_pat, fecha, valorPeso[0], valorAltura[0], sexo);
    }

    public int updateData(String rut, String nombre, String apellido_pat, String fecha, String peso, String altura, String sexo) {
        String[] valorAltura = altura.split(" ");
        String[] valorPeso = peso.split(" ");

        sexo = sexoToLetra(sexo);

        return dataBaseAdapter.actualizarPerfil(rut, nombre, apellido_pat, fecha, valorPeso[0], valorAltura[0], sexo);
    }

    public void viewDetails(View view) {
        String data = dataBaseAdapter.obtenerTodosPerfiles();
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
        data = dataBaseAdapter.buscarPerfil("0");

        if (data.isEmpty()) {
            Mensaje qwe = new Mensaje(getActivity().getApplicationContext(), "No hay datos");
            addUser("0", "", "", "", "0", "0", "Indefinido");
            data = dataBaseAdapter.buscarPerfil("0");

            for (int i = 0; i < valores.length; i++) {
                valores[i] = data.get(i);
            }
            valores[3] = valores[3] + " Kg";
            valores[4] = valores[4] + " m";
            valores[5] = letraToSexo(valores[5]);

        } else {
            for (int i = 0; i < valores.length; i++) {
                valores[i] = data.get(i);
            }
            valores[3] = valores[3] + " Kg";
            valores[4] = valores[4] + " m";
            valores[5] = letraToSexo(valores[5]);
            Mensaje qwe = new Mensaje(getActivity().getApplicationContext(), "Si hay datos");
        }
    }

    /**
     *
     * @param stringSexo recibe la variable sexo como string
     * @return devuelve al vasriable sexo como caracter
     */
    public String sexoToLetra(String stringSexo) {
        String letraSexo;

        if (stringSexo.equals("Hombre")) {
            letraSexo = "H";
        } else if (stringSexo.equals("Mujer")) {
            letraSexo = "M";
        } else {
            letraSexo = "I";
        }

        return letraSexo;
    }

    /**
     *
     * @param letraSexo recibe la variable sexo como caracter
     * @return devuelve al variable sexo como string
     */
    public String letraToSexo(String letraSexo) {
        String stringSexo;

        if (letraSexo.equals("H")) {
            stringSexo = "Hombre";
        } else if (letraSexo.equals("M")) {
            stringSexo = "Mujer";
        } else {
            stringSexo = "Indefinido";
        }

        return stringSexo;
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
