package com.example.joel.cletapp;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.joel.cletapp.fragments.DesafioFragment;
import com.example.joel.cletapp.fragments.GlosarioFragment;
import com.example.joel.cletapp.fragments.HistorialFragment;
import com.example.joel.cletapp.fragments.MainFragment;
import com.example.joel.cletapp.fragments.MisDesafiosFragment;
import com.example.joel.cletapp.fragments.MisRutinasFragment;
import com.example.joel.cletapp.fragments.PerfilFragment;
import com.example.joel.cletapp.fragments.RutinaFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {

    public static final String PREF_FILE_NAME = "testpref"; //Archivo en donde se guardan los datos del Navegation
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer"; //Nombre de la LLAVE en el sharedPreference

    private ActionBarDrawerToggle mDrawerToggle; //Variable para manejar el boton que despliega el NavegationDrawer
    private DrawerLayout mDrawerLayout; //Maneja el Navegation Drawer

    private boolean mUserLearnedDrawer; //Si el usuario ha visto el drawer TRUE si no FALSE
    private boolean mFromSavedInstanceState; //Si se ha guardado en los datos TRUE si no FALSE
    private View containerView; //Variable usada para obtener el recurso NavegationDrawer

    private GlosarioFragment glosarioFragment;
    private RutinaFragment rutinaFragment;
    private HistorialFragment historialFragment;
    private MainFragment mainFragment;
    private DesafioFragment desafioFragment;
    private PerfilFragment perfilFragment;
    private MisDesafiosFragment misDesafiosFragment;
    private MisRutinasFragment misRutinasFragment;
    View v, v2;

    String[] opciones;
    int[] imagenes = {R.drawable.ic_home_black_24dp, R.drawable.ic_person_black_24dp, R.drawable.ic_star_black_24dp, R.drawable.ic_directions_bike_black_24dp, R.drawable.ic_collections_bookmark_black_24dp, R.drawable.ic_school_black_24dp};
    ListView listOpciones;
    AdapterOpcion adapter;
    Bundle savedInstanceState;


    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));

        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        inicializarComponentes(root);
        //new Mensaje(getActivity().getApplicationContext(), "NavigationDrawer creado");

        listOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String opcionSeleccionada = adapter.options[position];

                v.setBackgroundResource(0);
                view.setBackgroundResource(R.color.colorSeleccionado);
                v = view;

                if (opcionSeleccionada.equals("Entrenamiento")) {
                    mDrawerLayout.closeDrawer(containerView);
                    if (!(v2 == view)) {
                        cargarFragmento(getMainFragment(), R.anim.activity_visible_salida_izquierda, R.anim.activity_nuevo_entrada_derecha);
                        v2 = view;
                    }
                } else if (opcionSeleccionada.equals("Perfil")) {
                    mDrawerLayout.closeDrawer(containerView);

                    if (!(v2 == view)) {
                        cargarFragmento(getPerfilFragment(), R.anim.activity_visible_salida_izquierda, R.anim.activity_nuevo_entrada_derecha);
                        v2 = view;
                    }

                } else if (opcionSeleccionada.equals(getResources().getString(R.string.desafios))) {
                    mDrawerLayout.closeDrawer(containerView);

                    if (!(v2 == view)) {
                        cargarFragmento(getMisDesafiosFragment(), R.anim.activity_visible_salida_izquierda, R.anim.activity_nuevo_entrada_derecha);
                        //cargarFragmento(getDesafioFragment(), R.anim.activity_visible_salida_izquierda, R.anim.activity_nuevo_entrada_derecha);
                        v2 = view;
                    }
                } else if (opcionSeleccionada.equals("Rutinas de entrenamiento")) {
                    mDrawerLayout.closeDrawer(containerView);

                    if (!(v2 == view)) {
                        //cargarFragmento(getRutinaFragment(), R.anim.activity_visible_salida_izquierda, R.anim.activity_nuevo_entrada_derecha);
                        cargarFragmento(getMisRutinasFragment(), R.anim.activity_visible_salida_izquierda, R.anim.activity_nuevo_entrada_derecha);
                        v2 = view;
                    }
                } else if (opcionSeleccionada.equals("Ayuda")) {
                    mDrawerLayout.closeDrawer(containerView);

                    if (!(v2 == view)) {
                        cargarFragmento(getGlosarioFragment(), R.anim.activity_visible_salida_izquierda, R.anim.activity_nuevo_entrada_derecha);
                        v2 = view;
                    }
                } else {
                    Mensaje msg = new Mensaje(getActivity().getApplicationContext(), "En construccion");
                }
            }
        });

        return root;
    }

    private void inicializarComponentes(View root) {
        listOpciones = (ListView) root.findViewById(R.id.ListViewOpcion);
        Resources res = getResources();
        opciones = res.getStringArray(R.array.opciones);

        glosarioFragment = null;
        rutinaFragment = null;
        historialFragment = null;
        mainFragment = null;
        desafioFragment = null;
        perfilFragment = null;

        adapter = new AdapterOpcion(getActivity().getApplicationContext(), opciones, imagenes);
        listOpciones.setAdapter(adapter);

        v = new View(this.getActivity());
        v2 = new View(this.getActivity());
    }

    public void cargarFragmento(Fragment fragment, int animacionSalida, int animacioEntrada) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(animacionSalida, animacioEntrada, animacionSalida, animacioEntrada);
        transaction.replace(R.id.contenedor, fragment, "TagPrincipal");
        transaction.commit();
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {

        containerView = getActivity().findViewById(fragmentId); //Se obtiene la vista del NagivationDrawer
        mDrawerLayout = drawerLayout; //Se obtiene la vista principal

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer + "");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };

        //En caso de nunca haber visto el Drawer
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            //new Mensaje(getActivity().getApplicationContext(), "nunca habia visto el drawer");
            //mDrawerLayout.openDrawer(containerView);
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState(); //Se muestra el boton para manejar el Navegation
            }
        });
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, preferenceValue);
    }

    public GlosarioFragment getGlosarioFragment() {
        if (glosarioFragment == null) {
            glosarioFragment = new GlosarioFragment();
        }
        return glosarioFragment;
    }

    public RutinaFragment getRutinaFragment() {
        if (rutinaFragment == null) {
            rutinaFragment = new RutinaFragment();
        }
        return rutinaFragment;
    }

    public HistorialFragment getHistorialFragment() {
        if (historialFragment == null) {
            historialFragment = new HistorialFragment();
        }
        return historialFragment;
    }

    public MainFragment getMainFragment() {
        if (mainFragment == null) {
            mainFragment = new MainFragment();
        }
        return mainFragment;
    }

    public DesafioFragment getDesafioFragment() {
        if (desafioFragment == null) {
            desafioFragment = new DesafioFragment();
        }
        return desafioFragment;
    }

    public PerfilFragment getPerfilFragment() {
        if (perfilFragment == null) {
            perfilFragment = new PerfilFragment();
        }
        return perfilFragment;
    }

    public MisDesafiosFragment getMisDesafiosFragment() {
        if (misDesafiosFragment == null) {
            misDesafiosFragment = new MisDesafiosFragment();
        }
        return misDesafiosFragment;
    }

    public MisRutinasFragment getMisRutinasFragment() {
        if (misRutinasFragment == null) {
            misRutinasFragment = new MisRutinasFragment();
        }
        return misRutinasFragment;
    }
}
