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

import com.example.joel.cletapp.CRUDDatabase.CiclistaCRUD;
import com.example.joel.cletapp.ClasesDataBase.Ciclista;
import com.example.joel.cletapp.Communicator;
import com.example.joel.cletapp.Mensaje;
import com.example.joel.cletapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//import com.example.joel.cletapp.DataBaseAdapter;

/**
 * Created by Joel on 21/07/2015.
 */
public class PerfilFragment extends Fragment implements Communicator {

    private Button ButtonGuardarPerfil;
    private ListView ListViewDatosPerfil;

    private String[] campos = {"Nombre", "Apellido", "Fecha de nacimiento", "Peso", "Altura", "Sexo"};
    private String[] valores = {"", "", "", "0", "0", "Indefinido"};
    private String[] valoresActualizados;
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private Date parsed = null;

    private CiclistaCRUD ciclistaCRUD;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Perfil");
        ((ActionBarActivity) getActivity()).getSupportActionBar().setIcon(null);

        inicializarComponentes(root);
        inicializarBaseDeDatos(root);

        ButtonGuardarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validarCreacion().equals("")) {
                    new Mensaje(getActivity().getApplicationContext(), validarCreacion());
                } else {
                    AdapterPerfil adapterPerfilActualizado = (AdapterPerfil) ListViewDatosPerfil.getAdapter();
                    valoresActualizados = adapterPerfilActualizado.getValores();
                    int id = updateData("0", valoresActualizados[0], valoresActualizados[1], valoresActualizados[2], valoresActualizados[3], valoresActualizados[4], valoresActualizados[5]);
                    new Mensaje(getActivity().getApplicationContext(), "Perfil actualizado");
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
                        dialogo.show(getFragmentManager(), "nombreDialog");
                        break;
                    case 1:
                        dialogo = new DialogoInputText();
                        dialogo.setArguments(bundle);
                        dialogo.show(getFragmentManager(), "apellidoDialog");
                        break;
                    case 2:
                        DialogFragment picker = new DialogoDatePickerFragment();
                        picker.setArguments(bundle);
                        picker.show(getFragmentManager(), "datePicker");
                        break;
                    case 3:
                        dialogoPesoEstatura = new DialogoPesoEstatura();
                        dialogoPesoEstatura.setArguments(bundle);
                        dialogoPesoEstatura.show(getFragmentManager(), "pesoDialog");
                        break;
                    case 4:
                        dialogoPesoEstatura = new DialogoPesoEstatura();
                        dialogoPesoEstatura.setArguments(bundle);
                        dialogoPesoEstatura.show(getFragmentManager(), "alturaDialog");
                        break;
                    case 5:
                        DialogoListSelector dialogo2 = new DialogoListSelector();
                        dialogo2.setArguments(bundle);
                        dialogo2.show(getFragmentManager(), "sexPicker");
                        break;
                }
            }
        });

        return root;
    }

    private String validarCreacion() {
        String validar = "";

        if (valores[5].equals("Indefinido")) {
            validar = "Seleccione sexo";
        }
        if (valores[4].equals("0.0 m")) {
            validar = "Ingrese altura";
        }
        if (valores[3].equals("0.0 Kg")) {
            validar = "Ingrese peso";
        }

        return validar;
    }

    private void inicializarComponentes(View root) {
        ButtonGuardarPerfil = (Button) root.findViewById(R.id.ButtonGuardarPerfil);
        ListViewDatosPerfil = (ListView) root.findViewById(R.id.ListViewDatosPerfil);

        AdapterPerfil adapterPerfil = new AdapterPerfil(getActivity().getApplicationContext(), campos, valores);
        ListViewDatosPerfil.setAdapter(adapterPerfil);
    }

    public long addCiclista(String rut, String nombre, String apellido_pat, String fecha, String peso, String altura, String sexo) {
        String[] valorAltura = altura.split(" ");
        String[] valorPeso = peso.split(" ");
        Character sexoChar = sexoToLetra(sexo);

        try {
            parsed = format.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Ciclista ciclista = new Ciclista(rut, nombre, apellido_pat, new java.sql.Date(parsed.getTime()), Float.parseFloat(valorPeso[0]), Float.parseFloat(valorAltura[0]), sexoChar);
        //return dataBaseAdapter.insertarPerfil(ciclista);
        return ciclistaCRUD.insertarPerfil(ciclista);
    }

    public int updateData(String rut, String nombre, String apellido_pat, String fecha, String peso, String altura, String sexo) {
        String[] valorAltura = altura.split(" ");
        String[] valorPeso = peso.split(" ");
        Character sexoChar = sexoToLetra(sexo);

        try {
            parsed = format.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Ciclista ciclista = new Ciclista(rut, nombre, apellido_pat, new java.sql.Date(parsed.getTime()), Float.parseFloat(valorPeso[0]), Float.parseFloat(valorAltura[0]), sexoChar);
        //return dataBaseAdapter.actualizarDatosCiclista(ciclista);
        return ciclistaCRUD.actualizarDatosCiclista(ciclista);
    }

    public void inicializarBaseDeDatos(View view) {
        ciclistaCRUD = new CiclistaCRUD(getActivity().getApplicationContext());
        Ciclista ciclista = new Ciclista();

        try {
            ciclista = ciclistaCRUD.buscarCiclistaPorRut("0");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (ciclista.getCiclistRut().equals("null")) {
            Mensaje qwe = new Mensaje(getActivity().getApplicationContext(), "No hay datos");
            addCiclista("0", "", "", "01/01/1990", "0", "0", "Indefinido");

            try {
                ciclista = ciclistaCRUD.buscarCiclistaPorRut("0");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valores[0] = ciclista.getCiclistaNombre();
            valores[1] = ciclista.getCiclistaApellido();
            valores[2] = format.format(ciclista.getCiclistaFechaNacimiento());
            valores[3] = ciclista.getCiclistaPeso() + " Kg";
            valores[4] = ciclista.getCiclistaAltura() + " m";
            valores[5] = letraToSexo(ciclista.getCiclistaSexo());

        } else {
            Mensaje qwe = new Mensaje(getActivity().getApplicationContext(), "Si hay datos");

            valores[0] = ciclista.getCiclistaNombre();
            valores[1] = ciclista.getCiclistaApellido();
            valores[2] = format.format(ciclista.getCiclistaFechaNacimiento());
            valores[3] = ciclista.getCiclistaPeso() + " Kg";
            valores[4] = ciclista.getCiclistaAltura() + " m";
            valores[5] = letraToSexo(ciclista.getCiclistaSexo());
        }
    }

    /**
     * @param stringSexo recibe la variable sexo como string
     * @return devuelve al vasriable sexo como caracter
     */
    public Character sexoToLetra(String stringSexo) {
        Character letraSexo;

        if (stringSexo.equals("Hombre")) {
            letraSexo = "H".charAt(0);
        } else if (stringSexo.equals("Mujer")) {
            letraSexo = "M".charAt(0);
        } else {
            letraSexo = "I".charAt(0);
        }

        return letraSexo;
    }

    /**
     * @param letraSexo recibe la variable sexo como caracter
     * @return devuelve al variable sexo como string
     */
    public String letraToSexo(Character letraSexo) {
        String stringSexo;

        if (String.valueOf(letraSexo).equals("H")) {
            stringSexo = "Hombre";
        } else if (String.valueOf(letraSexo).equals("M")) {
            stringSexo = "Mujer";
        } else {
            stringSexo = "Indefinido";
        }

        return stringSexo;
    }

    @Override
    public void Actualizar(String data) {
        if (data.equals("Aceptar")) {
            new Mensaje(getActivity().getApplicationContext(), "Perfil actualizado");
        } else {

        }
    }

    @Override
    public void Eliminar(String data) {

    }

    @Override
    public void Reiniciar(String data) {

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
