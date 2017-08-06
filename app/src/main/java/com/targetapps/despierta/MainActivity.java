package com.targetapps.despierta;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity implements GoogleMap.OnMapClickListener, View.OnClickListener, AdapterView.OnItemSelectedListener, TextToSpeech.OnInitListener, LocationListener {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;

    MaterialSearchView searchView;

    httpHandler handler = new httpHandler();

    //int currentapiVersion = android.os.Build.VERSION.SDK_INT;

    Marker mPositionMarker;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private final LatLng Inicio = new LatLng(-36.829203, -73.050706);
    EditText Busqueda;
    Spinner Resultado;
    android.widget.Button btBuscar;
    int nmax = 20;
    int resourse = android.R.layout.simple_spinner_item;
    ArrayAdapter<String> adapter;
    String[] spinnerArray;
    String[] result = new String[nmax];
    double[] latitud = new double[nmax];
    double[] longitud = new double[nmax];

    private TextToSpeech textToSpeech;
    
    Double mLatitud,mLongitud;

    private int count;

    String user_pide2;
    String micro;
    String letra;
    String user_pide = "o";
    String user_entrega = "o";
    String nombre1 = "";
    String nombre2 = "";
    String usr_nom = "";
    //Button btnUbicacion;
    LatLng geo;

    //solicitud ubicacion
    String nombre_pide = new String(); // nombre del amigo que pidiÃ³ tu ubicaciÃ³n
    ListView solicitud;

    String ip="192.168.1.102";
    //fin
    ListView horarios;
    String Smicro,sLetra,weekDay;
    Double source;
    String [] horas;
    String [] start;
    String [] end;
    int [] secs;
    ArrayList<String> completo;


    ListView listView_micros, listView_letras;
    Button btCargar, btVolver, btnCerrar;

    android.support.design.widget.FloatingActionButton floatingActionButton;

    private static final long POINT_RADIUS = 200; // in Meters
    private static final long PROX_ALERT_EXPIRATION = -1; // It will never expire
    private static final String PROX_ALERT_INTENT = "com.targetapps.despierta";
    private LocationManager locationManager;
    private Double lat = null;
    private Double lon = null;

    String user_tel;

    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 200;

    Profile profile2;

    Spinner spMicro,spLetra;
    Double radio = 0.2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        final Profile profile = Profile.getCurrentProfile();

        if (profile != null) {
            profile2 = profile;
        }


        setContentView(R.layout.activity_main);

        final SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE",Locale.US);
        Calendar calendar = Calendar.getInstance();
        weekDay = dayFormat.format(calendar.getTime());
        /*floatingActionButton = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fab_micros);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                findViewById(R.id.layout_micros).setVisibility(View.VISIBLE);

            }
        });*/

        Button btCerrar = (Button) findViewById(R.id.btCerrar);
        btCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                findViewById(R.id.Layout1).setVisibility(View.GONE);
                findViewById(R.id.Resultado).setVisibility(View.GONE);

            }
        });

        btnCerrar = (Button) findViewById(R.id.button2);
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.layout_solicitudes).setVisibility(View.GONE);
                btnCerrar.setVisibility(View.GONE);
            }
        });


        setUpMapIfNeeded();

        Busqueda = (EditText) findViewById(R.id.Busqueda);
        btBuscar = (android.widget.Button) findViewById(R.id.btBuscar);
        Resultado = (Spinner) findViewById(R.id.Resultado);
        btBuscar.setOnClickListener(this);

        //Resultado = (Spinner) findViewById(R.id.Resultado);
        //Resultado.setPrompt("Mostrar mapa");
        //spinnerArray = new String[1];
        //spinnerArray[0] = "Resultados para tu busqueda :";
        //adapter = new ArrayAdapter<String>(this, resourse, spinnerArray);
        //Resultado.setAdapter(adapter);
        Resultado.setOnItemSelectedListener(this);


        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Inicio, 15));
        mMap.setMyLocationEnabled(true);
        mMap.getMyLocation();
        mMap.getUiSettings().setCompassEnabled(true);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        /*if(currentapiVersion>22){
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }*/
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);


        textToSpeech = new TextToSpeech(this, this);

        String[] micros = getResources().getStringArray(R.array.micro);


        listView_micros = (ListView) findViewById(R.id.listView);
        listView_letras = (ListView) findViewById(R.id.listView2);

        ArrayAdapter<String> Amicros;
        Amicros = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, micros);


        listView_micros.setAdapter(Amicros);

        final Micros micros1 = new Micros();
        btCargar = (Button) findViewById(R.id.btCargarPar);


        listView_micros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                switch (position) {


                    case 0:
                        ArrayAdapter<String> Lhualpensan;
                        Lhualpensan = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.buses_hualpensan);
                        listView_letras.setAdapter(Lhualpensan);
                        String selectedFromList = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList;
                        enviarDatos();
                        break;
                    case 1:
                        ArrayAdapter<String> Lvialactea;
                        Lvialactea = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.via_lactea);
                        listView_letras.setAdapter(Lvialactea);
                        String selectedFromList2 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList2;
                        enviarDatos();
                        break;
                    case 2:
                        ArrayAdapter<String> Lviafuturo;
                        Lviafuturo = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.via_futuro);
                        listView_letras.setAdapter(Lviafuturo);
                        String selectedFromList3 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList3;
                        enviarDatos();
                        break;
                    case 3:
                        ArrayAdapter<String> Lnuevasotrapel;
                        Lnuevasotrapel = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.nueva_sotrapel);
                        listView_letras.setAdapter(Lnuevasotrapel);
                        String selectedFromList4 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList4;
                        enviarDatos();
                        break;
                    case 4:
                        ArrayAdapter<String> Lviasigloxxi;
                        Lviasigloxxi = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.via_siglo_xxi);
                        listView_letras.setAdapter(Lviasigloxxi);
                        String selectedFromList5 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList5;
                        enviarDatos();
                        break;
                    case 5:
                        ArrayAdapter<String> Lchiguayantesur;
                        Lchiguayantesur = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.chiguayante_sur);
                        listView_letras.setAdapter(Lchiguayantesur);
                        String selectedFromList6 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList6;
                        enviarDatos();
                        break;
                    case 6:
                        ArrayAdapter<String> Lviauniverso;
                        Lviauniverso = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.via_universo);
                        listView_letras.setAdapter(Lviauniverso);
                        String selectedFromList7 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList7;
                        enviarDatos();
                        break;
                    case 7:
                        ArrayAdapter<String> Lexpresoschiguayante;
                        Lexpresoschiguayante = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.expresos_chiguayante);
                        listView_letras.setAdapter(Lexpresoschiguayante);
                        String selectedFromList8 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList8;
                        enviarDatos();
                        break;
                    case 8:
                        ArrayAdapter<String> Lnuevallacolen;
                        Lnuevallacolen = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.nueva_llacolen);
                        listView_letras.setAdapter(Lnuevallacolen);
                        String selectedFromList9 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList9;
                        enviarDatos();
                        break;
                    case 9:
                        ArrayAdapter<String> Lrivierabiobio;
                        Lrivierabiobio = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.riviera_biobio);
                        listView_letras.setAdapter(Lrivierabiobio);
                        String selectedFromList10 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList10;
                        enviarDatos();
                        break;
                    case 10:
                        ArrayAdapter<String> Lsanpedro;
                        Lsanpedro = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.san_pedro);
                        listView_letras.setAdapter(Lsanpedro);
                        String selectedFromList11 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList11;
                        enviarDatos();
                        break;
                    case 11:
                        ArrayAdapter<String> Lbusessanpedrodelmar;
                        Lbusessanpedrodelmar = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.buses_san_pedro_del_mar);
                        listView_letras.setAdapter(Lbusessanpedrodelmar);
                        String selectedFromList12 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList12;
                        enviarDatos();
                        break;
                    case 12:
                        ArrayAdapter<String> Lsanremo;
                        Lsanremo = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.san_remo);
                        listView_letras.setAdapter(Lsanremo);
                        String selectedFromList13 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList13;
                        enviarDatos();
                        break;
                    case 13:
                        ArrayAdapter<String> Lrutalasplayas;
                        Lrutalasplayas = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.ruta_las_playas);
                        listView_letras.setAdapter(Lrutalasplayas);
                        String selectedFromList14 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList14;
                        enviarDatos();
                        break;
                    case 14:
                        ArrayAdapter<String> Lrutadelmar;
                        Lrutadelmar = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.ruta_del_mar);
                        listView_letras.setAdapter(Lrutadelmar);
                        String selectedFromList15 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList15;
                        enviarDatos();
                        break;
                    case 15:
                        ArrayAdapter<String> Lbusesrutadelmar;
                        Lbusesrutadelmar = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.buses_ruta_del_mar);
                        listView_letras.setAdapter(Lbusesrutadelmar);
                        String selectedFromList16 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList16;
                        enviarDatos();
                        break;
                    case 16:
                        ArrayAdapter<String> Llasgolondrinas;
                        Llasgolondrinas = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.las_golondrinas);
                        listView_letras.setAdapter(Llasgolondrinas);
                        String selectedFromList17 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList17;
                        enviarDatos();
                        break;
                    case 17:
                        ArrayAdapter<String> Lbusesminiverde;
                        Lbusesminiverde = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.buses_mini_verde);
                        listView_letras.setAdapter(Lbusesminiverde);
                        String selectedFromList18 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList18;
                        enviarDatos();
                        break;
                    case 18:
                        ArrayAdapter<String> Lminibuseshualpencillo;
                        Lminibuseshualpencillo = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.mini_buses_hualpencillo);
                        listView_letras.setAdapter(Lminibuseshualpencillo);
                        String selectedFromList19 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList19;
                        enviarDatos();
                        break;
                    case 19:
                        ArrayAdapter<String> Lflotalaslilas;
                        Lflotalaslilas = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.flota_las_lilas);
                        listView_letras.setAdapter(Lflotalaslilas);
                        String selectedFromList20 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList20;
                        enviarDatos();
                        break;
                    case 20:
                        ArrayAdapter<String> Lflotacentauro;
                        Lflotacentauro = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.flota_centauro);
                        listView_letras.setAdapter(Lflotacentauro);
                        String selectedFromList21 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList21;
                        enviarDatos();
                        break;
                    case 21:
                        ArrayAdapter<String> Lbusescampanil;
                        Lbusescampanil = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.buses_campanil);
                        listView_letras.setAdapter(Lbusescampanil);
                        String selectedFromList22 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList22;
                        enviarDatos();
                        break;
                    case 22:
                        ArrayAdapter<String> Lgeminisdelsur;
                        Lgeminisdelsur = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.geminis_del_sur);
                        listView_letras.setAdapter(Lgeminisdelsur);
                        String selectedFromList23 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList23;
                        enviarDatos();
                        break;
                    case 23:
                        ArrayAdapter<String> Lbusesbasenaval;
                        Lbusesbasenaval = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.buses_base_naval);
                        listView_letras.setAdapter(Lbusesbasenaval);
                        String selectedFromList24 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList24;
                        enviarDatos();
                        break;
                    case 24:
                        ArrayAdapter<String> Lbusestucapel;
                        Lbusestucapel = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.buses_tucapel);
                        listView_letras.setAdapter(Lbusestucapel);
                        String selectedFromList25 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList25;
                        enviarDatos();
                        break;
                    case 25:
                        ArrayAdapter<String> Lmiexpreso;
                        Lmiexpreso = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.mi_expreso);
                        listView_letras.setAdapter(Lmiexpreso);
                        String selectedFromList26 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList26;
                        enviarDatos();
                        break;
                    case 26:
                        ArrayAdapter<String> Lrengolientur;
                        Lrengolientur = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.rengo_lientur);
                        listView_letras.setAdapter(Lrengolientur);
                        String selectedFromList27 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList27;
                        enviarDatos();
                        break;
                    case 27:
                        ArrayAdapter<String> Lbusescondor;
                        Lbusescondor = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.buses_condor);
                        listView_letras.setAdapter(Lbusescondor);
                        String selectedFromList28 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList28;
                        enviarDatos();
                        break;
                    case 28:
                        ArrayAdapter<String> Llasbahias;
                        Llasbahias = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.las_bahias);
                        listView_letras.setAdapter(Llasbahias);
                        String selectedFromList29 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList29;
                        enviarDatos();
                        break;
                    case 29:
                        ArrayAdapter<String> Lbusespuchacay;
                        Lbusespuchacay = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.buses_puchacay);
                        listView_letras.setAdapter(Lbusespuchacay);
                        String selectedFromList30 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList30;
                        enviarDatos();
                        break;
                    case 30:
                        ArrayAdapter<String> Lpedrodevaldivia;
                        Lpedrodevaldivia = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.pedro_de_valdivia);
                        listView_letras.setAdapter(Lpedrodevaldivia);
                        String selectedFromList31 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList31;
                        enviarDatos();
                        break;
                    case 31:
                        ArrayAdapter<String> Llasgalaxias;
                        Llasgalaxias = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.las_galaxias);
                        listView_letras.setAdapter(Llasgalaxias);
                        String selectedFromList32 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList32;
                        enviarDatos();
                        break;
                    case 32:
                        ArrayAdapter<String> Lviadelsol;
                        Lviadelsol = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.via_del_sol);
                        listView_letras.setAdapter(Lviadelsol);
                        String selectedFromList33 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList33;
                        enviarDatos();
                        break;
                    case 33:
                        ArrayAdapter<String> Lnuevasolyet;
                        Lnuevasolyet = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.nueva_sol_yet);
                        listView_letras.setAdapter(Lnuevasolyet);
                        String selectedFromList34 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList34;
                        enviarDatos();
                        break;
                    case 34:
                        ArrayAdapter<String> Lbiotren;
                        Lbiotren = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.biotren);
                        listView_letras.setAdapter(Lbiotren);
                        String selectedFromList35 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList35;
                        enviarDatos();
                        break;
                    case 35:
                        ArrayAdapter<String> Lintermodalbiobus;
                        Lintermodalbiobus = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, micros1.intermodal_bio_bus);
                        listView_letras.setAdapter(Lintermodalbiobus);
                        String selectedFromList36 = (listView_micros.getItemAtPosition(position).toString());
                        micro = selectedFromList36;
                        enviarDatos();
                        break;
                    default:
                        break;


                }


            }
        });
        btCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
                findViewById(R.id.layout_micros).setVisibility(View.GONE);
                obtDatos();
            }
        });
        btVolver = (Button) findViewById(R.id.btVolver);
        btVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.layout_micros).setVisibility(View.GONE);
            }
        });


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                source =marker.getPosition().latitude;
                fadeIn(findViewById(R.id.layout_hora));
                
                mLatitud = marker.getPosition().latitude;
                mLongitud = marker.getPosition().longitude;

                SolicitarMicro();
                metodoLetras();
                metodoHorarios();


            }
        });
        Inclinacion();
        Biotren();


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.mipmap.ic_ab_drawer_azul);
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(drawerToggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);

        }


        try {
            nombre1 = getIntent().getExtras().getString("nombre1");
            nombre2 = getIntent().getExtras().getString("nombre2");
            usr_nom = getIntent().getExtras().getString("usr");

        } catch (Exception e) {

        }

        if (nombre1 != "" && nombre2 != "") { // si va a ver la ubicacion de un amigo y los datos contiene los nombres

            obtUbicacion();
        } else {

        }

        solicitud = (ListView) findViewById(R.id.lvSolicitud);

        MyTimerTask myTask = new MyTimerTask();
        Timer timer = new Timer();
        timer.schedule(myTask, 50, 5000);

        solicitud.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = ((TextView) view).getText().toString();
                nombre_pide = item.toString();
                final String usuario = profile.getFirstName();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setMessage("¿Enviar ubicacion a " + nombre_pide + "?")
                        .setTitle("Ubicar")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (usuario.length() != 0) {
                                    user_pide = user_tel;
                                    user_pide2 = nombre_pide;
                                    user_entrega = profile.getId();
                                    Ubicacion();
                                } else {
                                    //Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //Toast.makeText(MainActivity2.this, "CANCEL", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });
                builder.create();
                builder.show();
            }
        });


        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mMap.clear();
                try {
                    String busqueda = query;
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    List<Address> list = geocoder.getFromLocationName(busqueda, nmax);

                    Iterator<Address> iterator = list.iterator();
                    int n = 0;
                    String Pais;
                    Address Direccion;

                    while (iterator.hasNext()) {
                        Direccion = iterator.next();
                        latitud[n] = Direccion.getLatitude();
                        longitud[n] = Direccion.getLongitude();
                        Pais = Direccion.getCountryName();
                        result[n] = Direccion.getAddressLine(0) + ", " + Direccion.getAddressLine(1) + ", " + Pais;

                        n++;
                    }

                    spinnerArray = new String[n];
                    System.arraycopy(result, 0, spinnerArray, 0, n);
                    adapter = new ArrayAdapter<String>(getApplicationContext(), resourse, spinnerArray);
                    Resultado.setAdapter(adapter);
                    Resultado.setVisibility(View.INVISIBLE);

                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Error en la busqueda, verifica \n tu conexión a internet", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
        final FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.action_a);
        actionA.setIcon(R.mipmap.ic_report_problem_white_24dp);
        final FloatingActionButton actionB = (FloatingActionButton) findViewById(R.id.action_b);
        actionB.setIcon(R.mipmap.bus);
        final FloatingActionButton actionC = (FloatingActionButton) findViewById(R.id.action_c);
        actionC.setIcon(R.mipmap.ic_report_problem_white_24dp);
        actionC.setVisibility(View.GONE);
        final FloatingActionButton actionE = (FloatingActionButton) findViewById(R.id.action_e);
        actionE.setIcon(R.mipmap.ic_tren);
        actionE.setVisibility(View.GONE);
        final FloatingActionButton actionD = (FloatingActionButton) findViewById(R.id.action_d);
        actionD.setIcon(R.mipmap.ic_open_with_white_24dp_trans);
        actionD.setVisibility(View.GONE);
        final FloatingActionButton actionF = (FloatingActionButton) findViewById(R.id.action_f);
        actionF.setIcon(R.mipmap.bus_azul);
        actionF.setVisibility(View.GONE);
        final FloatingActionButton actionG = (FloatingActionButton) findViewById(R.id.action_g);
        actionG.setIcon(R.mipmap.ic_metro_santiago_cl);
        actionG.setVisibility(View.GONE);
        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);

        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                actionC.setVisibility(actionC.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                actionD.setVisibility(actionD.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });


        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionA.setVisibility(actionA.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                actionE.setVisibility(actionE.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                actionF.setVisibility(actionF.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                actionG.setVisibility(actionG.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);


            }
        });

        actionF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.layout_micros).setVisibility(View.VISIBLE);

                View layout_micros = findViewById(R.id.layout_micros);

                if (layout_micros.getVisibility() == View.VISIBLE) {
                    menuMultipleActions.toggle();
                }
            }
        });


        actionG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //findViewById(R.id.layout_hora).setVisibility(View.VISIBLE);
                //fadeIn(findViewById(R.id.layout_hora));
            }
        });


        /*FloatingActionButton actionC = new FloatingActionButton(getBaseContext());
        actionC.setTitle("Hide/Show Action above");
        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });*/

        //menuMultipleActions.addButton(actionC);

        //actionA.setBackground();

        ImageView btn_vista = (ImageView) findViewById(R.id.btn_view);
        btn_vista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.layout_hora).getVisibility() == View.VISIBLE) {
                    fadeOut(findViewById(R.id.layout_hora));
                }
            }
        });

        spMicro = (Spinner) findViewById(R.id.spMicro);
        spLetra = (Spinner) findViewById(R.id.spLetra);
        horarios = (ListView) findViewById(R.id.lvHorarios);



    }

    //horarios paraderos inicio

    public void SolicitarMicro(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://"+ip+"/horarios/micros.php";
        RequestParams parametros = new RequestParams();
        parametros.put("lat", mLatitud);
        parametros.put("lon", mLongitud);

        client.post(url, parametros, new AsyncHttpResponseHandler() { //manejador de respuestas http
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    cargaListaMicro(obtDatosJsonMicro(new String(responseBody)));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //carga el spinner con micros
    public void cargaListaMicro(ArrayList<String> datos){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,datos);
        spMicro.setAdapter(adapter);
    }
    //llena el array de micros
    public ArrayList<String> obtDatosJsonMicro(String response){
        ArrayList<String> listado = new ArrayList<String>();
        try{
            JSONArray jsonArray = new JSONArray(response);
            String texto;

            //aca se reciben los datos :)
            for (int i = 0; i < jsonArray.length(); i++){
                texto = jsonArray.getJSONObject(i).getString("agency_name");/*+", "+
                        jsonArray.getJSONObject(i).getString("id")+", "+
                        jsonArray.getJSONObject(i).getString("email")+" ";*/

                listado.add(texto);
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        return listado;
    }


    public void metodoHorarios(){
        spMicro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Smicro = parent.getSelectedItem().toString();
                SolicitarLetra();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void SolicitarLetra(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://"+ip+"/horarios/letras.php";
        RequestParams parametros = new RequestParams();
        parametros.put("micro", Smicro);
        parametros.put("lat", mLatitud);
        parametros.put("lon", mLongitud);



        client.post(url, parametros, new AsyncHttpResponseHandler() { //manejador de respuestas http
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    cargaListaLetra(obtDatosJsonLetra(new String(responseBody)));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //carga el spinner con letras
    public void cargaListaLetra(ArrayList<String> datos){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,datos);
        spLetra.setAdapter(adapter);
    }
    //llena el array de letras
    public ArrayList<String> obtDatosJsonLetra(String response){
        ArrayList<String> listado = new ArrayList<String>();
        try{
            JSONArray jsonArray = new JSONArray(response);
            String texto;


            for (int i = 0; i < jsonArray.length(); i++){
                texto = jsonArray.getJSONObject(i).getString("route_short_name");
                listado.add(texto);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listado;
    }

    public void metodoLetras(){

        spLetra.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sLetra = parent.getSelectedItem().toString();
                SolicitarHoras();
                SolicitarFrec();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void SolicitarHoras(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://"+ip+"/horarios/horarios.php";
        RequestParams parametros = new RequestParams();
        parametros.put("letra", sLetra);
        parametros.put("lat", source);
        parametros.put("dia", weekDay);


        client.post(url, parametros, new AsyncHttpResponseHandler() { //manejador de respuestas http
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    obtDatosJsonHoras(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ArrayList<String> obtDatosJsonHoras(String response){
        ArrayList<String> listado = new ArrayList<String>();
        try{
            JSONArray jsonArray = new JSONArray(response);
            String texto;
            horas = new String[jsonArray.length()];


            for (int a = 0; a < jsonArray.length(); a++){
                texto = jsonArray.getJSONObject(a).getString("arrival_time");
                horas[a] = jsonArray.getJSONObject(a).getString("arrival_time");

                listado.add(texto);
            }

            calHoras();
            //horaCercana(completo);

        }catch (Exception e){
            e.printStackTrace();
        }
        return listado;
    }


    public void SolicitarFrec(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://"+ip+"/horarios/frecuencias.php";
        RequestParams parametros = new RequestParams();
        parametros.put("letra", sLetra);
        parametros.put("dia", weekDay);


        client.post(url, parametros, new AsyncHttpResponseHandler() { //manejador de respuestas http
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    obtDatosJsonFrec(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public ArrayList<String> obtDatosJsonFrec(String response){
        ArrayList<String> listado = new ArrayList<String>();
        try{
            JSONArray jsonArray = new JSONArray(response);
            String com;
            start = new String[jsonArray.length()];
            end = new String[jsonArray.length()];
            secs = new int[jsonArray.length()];


            for (int i = 0; i < jsonArray.length(); i++){
                com = jsonArray.getJSONObject(i).getString("start_time");
                start[i] = jsonArray.getJSONObject(i).getString("start_time");
                end[i] = jsonArray.getJSONObject(i).getString("end_time");
                secs[i] = jsonArray.getJSONObject(i).getInt("headway_secs");

                listado.add(com);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return listado;
    }

    public void calHoras(){
        completo = new ArrayList<String>();
        Date timeS = new Date();
        Date timeE = new Date();
        Date timeH = new Date();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Calendar calendarDate = Calendar.getInstance();
        int cont =0;

        for(int i=0; i < start.length; i++){

            try {

                timeE = timeFormat.parse(end[i]);
            } catch (ParseException exc) {
                exc.printStackTrace();
            }


            while(cont < horas.length){
                try {
                    timeH = timeFormat.parse(horas[cont]);

                    completo.add(timeFormat.format(timeH.getTime()));
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                while(timeH.getTime() < timeE.getTime()){

                    calendarDate.setTime(timeH);
                    calendarDate.add(Calendar.SECOND, secs[i]);
                    timeH.setTime(calendarDate.getTimeInMillis());

                    if (timeH.getTime() < timeE.getTime()){
                        completo.add(timeFormat.format(calendarDate.getTime())+" + "+secs[i]/60 + "min");
                    }
                }
                cont++;
                break;
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,completo);
        horarios.setAdapter(adapter);

    }


    //horarios paraderos final

    private void fadeIn(final View view){
        final AlphaAnimation fadeInAnimator = new AlphaAnimation(0.0f, 1.0f);
        fadeInAnimator.setDuration(800);
        fadeInAnimator.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(fadeInAnimator);
    }

    private void fadeOut(final View view){
        final AlphaAnimation fadeOutAnimator = new AlphaAnimation(1.0f, 0.0f);
        fadeOutAnimator.setDuration(800);
        fadeOutAnimator.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(fadeOutAnimator);
    }


    public void Ubicacion() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Debe activar su GPS", Toast.LENGTH_SHORT).show();
        } else {
            lat = mMap.getMyLocation().getLatitude();
            lon = mMap.getMyLocation().getLongitude();
            enviarUbicacion(); // envia la ubicacion a la bdd
        }
    }

    public class AlertasBdd extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            guardarAlerta();
            return null;
        }
    }

    public class MyTimerTask extends TimerTask {
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (profile2 != null) {
                        obtSolicitudes();
                    }

                }
            });

        }
    }


    public void obtSolicitudes() {


        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://52.88.17.215/GetGps.php?usuario=" + profile2.getId();
        RequestParams parametros = new RequestParams();
        parametros.put("edad", 18);


        client.post(url, parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    cargaLista(obtDatosSolicitud(new String(responseBody)));
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Toast.makeText(MainActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    int minimo;

    public void cargaLista(ArrayList<String> datos) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datos);
        solicitud.setAdapter(adapter);
        int tamano = adapter.getCount();

        final MediaPlayer alerta;
        alerta = MediaPlayer.create(MainActivity.this, R.raw.alerta);
        Vibrator v = (Vibrator) this.getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);

        if (tamano == 0) {

        } else if (minimo < tamano) {
            v.vibrate(1000);
            alerta.start();
            alerta.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    alerta.stop();
                    alerta.release();
                }
            });
        }

        minimo = tamano;
    }


    public ArrayList<String> obtDatosSolicitud(String response) {
        ArrayList<String> listado = new ArrayList<String>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            String texto;
            //Toast.makeText(MainActivity.this,jsonArray.length()+" personas",Toast.LENGTH_SHORT).show();

            //aca se reciben los datos :)
            for (int i = 0; i < jsonArray.length(); i++) {
                texto = jsonArray.getJSONObject(i).getString("usuarios_usuario");//+" "+
                user_tel = jsonArray.getJSONObject(i).getString("usuarios_telefono");/*+" "+
                        jsonArray.getJSONObject(i).getString("edad")+" ";*/
                listado.add(texto);
            }
            doIncrease(jsonArray.length());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listado;
    }

    public void enviarUbicacion() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://52.88.17.215/PutPermitir2.php?";
        String parametros = "user1=" + user_pide + "&user2=" + user_entrega + "&lat=" + lat + "&lon=" + lon;

        client.post(url + parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    //String resultado = new String(responseBody) ;
                    Toast.makeText(MainActivity.this, "Ubicación enviada a " + user_pide2, Toast.LENGTH_SHORT).show();
                    nombre1 = null;
                    nombre2 = null;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Toast.makeText(MainActivity.this, "Error, intentalo nuevamente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void obtUbicacion() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://52.88.17.215/GetUbicacion.php?nombre1=" + nombre1 + "&nombre2=" + nombre2;
        RequestParams parametros = new RequestParams();
        parametros.put("edad", 18);

        client.post(url, parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    obtLatLon(new String(responseBody));
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Toast.makeText(MainActivity.this, "Error de conexiòn", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public ArrayList<String> obtLatLon(String response) {
        ArrayList<String> listado = new ArrayList<String>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            String latitud;
            String longitud;
            String nombre = "Aqui esta ";

            Double Dlatitud = new Double(0);
            Double Dlongitud = new Double(0);


            for (int i = 0; i < jsonArray.length(); i++) {
                latitud = jsonArray.getJSONObject(i).getString("latitud");
                longitud = jsonArray.getJSONObject(i).getString("longitud");

                Dlatitud = new Double(latitud);
                Dlongitud = new Double(longitud);


                mMap.addMarker(new MarkerOptions().position(new LatLng(Dlatitud, Dlongitud)).title(nombre + usr_nom));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Dlatitud, Dlongitud), 15));

                listado.add(latitud);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return listado;
    }


    public void locUp() {
        /*if(currentapiVersion>22) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }*/
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 60000, 0,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

                        mMap.moveCamera(center);
                        mMap.animateCamera(zoom);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                60000, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

                        mMap.moveCamera(center);
                        mMap.animateCamera(zoom);

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
    }


    private void guardarAlerta() {

        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            String usuario = profile.getFirstName();

            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                final String buscarAmigos = handler.get("http://52.88.17.215/GuardarAlertas.php?user=" + usuario);
                switch (buscarAmigos) {
                    case "Alerta Guardada":

                        break;
                    case "Error":

                        break;
                    default:


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    //navigation drawer

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        switch (menuItem.getItemId()) {
                            case R.id.navigation_sub_item_1:
                                startActivity(new Intent(getApplicationContext(), Login.class));
                                break;
                            case R.id.navigation_sub_item_2:
                                startActivity(new Intent(getApplicationContext(), MiPerfilActivity.class));
                                break;
                            case R.id.navigation_sub_item_3:
                                Toast.makeText(getApplicationContext(), "Proximamente...", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.navigation_sub_item_4:
                                startActivity(new Intent(getApplicationContext(), About.class));
                                break;
                            default:
                                return true;
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });

    }


    public void enviarDatos() {
        listView_letras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedFromList2 = (listView_letras.getItemAtPosition(i).toString());
                letra = selectedFromList2;

            }
        });
    }


    public void obtDatos() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://buycheck.hol.es/GetParadero.php";
        RequestParams parametros = new RequestParams();
        parametros.put("micro", micro);
        parametros.put("letra", letra);

        client.post(url, parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    obtDatosJson(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Toast.makeText(MainActivity.this, "Error de conexiòn", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //llena el array de datos con Json
    public ArrayList<String> obtDatosJson(String response) {
        ArrayList<String> listado = new ArrayList<String>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            String latitud;
            String longitud;
            String nombre;


            //aca se reciben los datos :)
            for (int i = 0; i < jsonArray.length(); i++) {
                latitud = jsonArray.getJSONObject(i).getString("stop_lat");
                longitud = jsonArray.getJSONObject(i).getString("stop_lon");
                nombre = jsonArray.getJSONObject(i).getString("stop_name");

                Double Dlatitud = new Double(latitud);
                Double Dlongitud = new Double(longitud);


                mMap.addMarker(new MarkerOptions().position(new LatLng(Dlatitud, Dlongitud)).title(nombre).icon(BitmapDescriptorFactory.fromResource(R.mipmap.paradero)));


                listado.add(latitud);
            }

            mMap.setInfoWindowAdapter(new UserInfoWindowAdapter2((getLayoutInflater())));
            Toast.makeText(MainActivity.this, "Paraderos cargados", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "ERROR: " + e, Toast.LENGTH_SHORT).show();
        }
        return listado;
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        
    }

    private void Inclinacion() {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(Inicio)
                .zoom(15)
                .tilt(50)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    @Override
    public void onMapClick(LatLng puntoPulsado) {
    }


    private void addProximityAlert() {


        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Debe activar su GPS", Toast.LENGTH_SHORT).show();
        } else {
            double latitude = lat;
            double longitude = lon;
            Intent intent = new Intent(PROX_ALERT_INTENT);
            PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            /*if(currentapiVersion>22) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
            }*/
            locationManager.addProximityAlert(
                    latitude, // the latitude of the central point of the alert region
                    longitude, // the longitude of the central point of the alert region
                    POINT_RADIUS, // the radius of the central point of the alert region, in meters
                    PROX_ALERT_EXPIRATION, // time for this proximity alert, in milliseconds, or -1 to indicate no                           expiration
                    proximityIntent // will be used to generate an Intent to fire when entry to or exit from the alert region is detected
            );

            IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
            registerReceiver(new ProximityIntentReceiver(), filter);
            Toast.makeText(getApplicationContext(), "Alerta añadida", Toast.LENGTH_SHORT).show();

        }
    }

    //biotren

    public void Biotren() {

        int despierta = R.mipmap.ic_launcher;


        LatLng chiguayante = new LatLng(-36.915222, -73.029014);
        LatLng obisposanmiguel = new LatLng(-36.925723, -73.021514);
        LatLng pedromedina = new LatLng(-36.938470, -73.011438);
        LatLng manquimavida = new LatLng(-36.951219, -73.013202);
        LatLng leonera = new LatLng(-36.838530, -73.097467);
        LatLng juanpablo = new LatLng(-36.837393, -73.118099);
        LatLng costamar = new LatLng(-36.882724, -73.139604);
        LatLng lomascoloradas = new LatLng(-36.830014, -73.061185);
        LatLng concethno = new LatLng(-36.739528, -73.102695);
        LatLng NN = new LatLng(-36.756709, -73.091676);
        LatLng lorenzoarenas = new LatLng(-36.756713, -73.091665);
        LatLng UTFSM = new LatLng(-36.783872, -73.085632);
        LatLng loscondores = new LatLng(-36.739495, -73.102717);
        LatLng hualqui = new LatLng(-36.980259, -72.940960);


        //marker estaciones
        mMap.addMarker(new MarkerOptions()
                .position(chiguayante)  //guarda la posicion
                .title("E S T A C I Ó N") //titulo de la posicion 1Â°
                .snippet("Chiguayante")  // titulo posicion 2Â°
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_tren))); //icono
        mMap.addMarker(new MarkerOptions()
                .position(obisposanmiguel)  //guarda la posicion
                .title("E S T A C I Ó N") //titulo de la posicion 1Â°
                .snippet("Obispo san Miguel, Chiguayante")  // titulo posicion 2Â°
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_tren))); //icono
        mMap.addMarker(new MarkerOptions()
                .position(pedromedina)  //guarda la posicion
                .title("E S T A C I Ó N") //titulo de la posicion 1Â°
                .snippet("Pedro Medina, Chiguayante")  // titulo posicion 2Â°
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_tren))); //icono
        mMap.addMarker(new MarkerOptions()
                .position(manquimavida)  //guarda la posicion
                .title("E S T A C I Ó N") //titulo de la posicion 1Â°
                .snippet("Manquimavida, Chiguayante")  // titulo posicion 2Â°
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_tren))); //icono
        mMap.addMarker(new MarkerOptions()
                .position(leonera)  //guarda la posicion
                .title("E S T A C I Ó N") //titulo de la posicion 1Â°
                .snippet("Leonera")  // titulo posicion 2Â°
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_tren))); //icono
        mMap.addMarker(new MarkerOptions()
                .position(juanpablo)  //guarda la posicion
                .title("E S T A C I Ó N") //titulo de la posicion 1Â°
                .snippet("Juan Pablo II, San pedro")  // titulo posicion 2Â°
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_tren))); //icono
        mMap.addMarker(new MarkerOptions()
                .position(costamar)  //guarda la posicion
                .title("E S T A C I Ó N") //titulo de la posicion 1Â°
                .snippet("Costa mar")  // titulo posicion 2Â°
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_tren))); //icono
        mMap.addMarker(new MarkerOptions()
                .position(lomascoloradas)  //guarda la posicion
                .title("E S T A C I Ó N") //titulo de la posicion 1Â°
                .snippet("Concepción")  // titulo posicion 2Â°
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_tren))); //icono
        mMap.addMarker(new MarkerOptions()
                .position(concethno)  //guarda la posicion
                .title("E S T A C I Ó N") //titulo de la posicion 1Â°
                .snippet("Concepción - Talcahuano")  // titulo posicion 2Â°
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_tren))); //icono
        mMap.addMarker(new MarkerOptions()
                .position(NN)  //guarda la posicion
                .title("E S T A C I Ó N") //titulo de la posicion 1Â°
                .snippet("estación biotrÃ©n")  // titulo posicion 2Â°
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_tren))); //icono
        mMap.addMarker(new MarkerOptions()
                .position(lorenzoarenas)  //guarda la posicion
                .title("E S T A C I Ó N") //titulo de la posicion 1Â°
                .snippet("Lorenzo arenas, Talcahuano")  // titulo posicion 2Â°
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_tren))); //icono
        mMap.addMarker(new MarkerOptions()
                .position(UTFSM)  //guarda la posicion
                .title("E S T A C I Ó N") //titulo de la posicion 1Â°
                .snippet("U. T. F. S. M, Talcahuano\n")  // titulo posicion 2Â°
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_tren))); //icono
        mMap.addMarker(new MarkerOptions()
                .position(loscondores)  //guarda la posicion
                .title("E S T A C I Ó N") //titulo de la posicion 1Â°
                .snippet("Los condores, Talcahuano")  // titulo posicion 2Â°
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_tren))); //icono
        mMap.addMarker(new MarkerOptions()
                .position(hualqui)  //guarda la posicion
                .title("E S T A C I Ó N") //titulo de la posicion 1Â°
                .snippet("Hualqui")  // titulo posicion 2Â°
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_tren))); //icono


        mMap.setInfoWindowAdapter(new UserInfoWindowAdapter((getLayoutInflater())));


    }

    @Override
    public void onClick(View view) {
        try {
            String busqueda = Busqueda.getText().toString();
            Geocoder geocoder = new Geocoder(this);
            List<Address> list = geocoder.getFromLocationName(busqueda, nmax);

            Iterator<Address> iterator = list.iterator();
            int n = 0;
            String Pais;
            Address Direccion;

            while (iterator.hasNext()) {
                Direccion = iterator.next();
                latitud[n] = Direccion.getLatitude();
                longitud[n] = Direccion.getLongitude();
                Pais = Direccion.getCountryName();
                result[n] = Direccion.getAddressLine(0) + ", " + Direccion.getAddressLine(1) + ", " + Pais;

                n++;
            }

            spinnerArray = new String[n];
            System.arraycopy(result, 0, spinnerArray, 0, n);
            adapter = new ArrayAdapter<String>(this, resourse, spinnerArray);
            Resultado.setAdapter(adapter);
            Resultado.setVisibility(View.VISIBLE);

        } catch (IOException e) {
            Toast.makeText(this, "Error en la busqueda, verifica \n tu conexión a internet", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        Double mult = 0.000001;
        Double lat = (latitud[arg2] * 1e6) * mult;
        Double lon = (longitud[arg2] * 1e6) * mult;
        geo = new LatLng(lat, lon);

        SolicitarRadio(lat, lon, radio);


        if (lat != 0 && lon != 0) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(geo, 16));
            mMap.addMarker(new MarkerOptions()
                    .position(geo)
                    .title("Busqueda")
                    .snippet("" + arg0.getSelectedItem())
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_accessibility_white_24dp)));
        }


    }

    public void SolicitarRadio(Double lat,Double lon, Double radio ){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://"+ip+"/horarios/radio.php";
        RequestParams parametros = new RequestParams();
        parametros.put("lat", lat);
        parametros.put("lon", lon);
        parametros.put("radio",radio);

        //Toast.makeText(MainActivity.this, "lat: "+lat + "Lon "+lon, Toast.LENGTH_SHORT).show();

        client.post(url, parametros, new AsyncHttpResponseHandler() { //manejador de respuestas http
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    obtDatosJsonRadio(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //llena el array de radio
    public ArrayList<String> obtDatosJsonRadio(String response){
        ArrayList<String> listado = new ArrayList<String>();
        try{
            JSONArray jsonArray = new JSONArray(response);

            String nombre;
            Double lati;
            Double loni;

            if (jsonArray.length()!=0 && radio<=1){

                //aca se reciben los datos Emoticón smile
                for (int i = 0; i < jsonArray.length(); i++){
                    nombre = jsonArray.getJSONObject(i).getString("stop_name");
                    lati = jsonArray.getJSONObject(i).getDouble("stop_lat");
                    loni = jsonArray.getJSONObject(i).getDouble("stop_lon");

                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lati,loni))
                            .title(nombre)
                            .snippet("Precione aqui para ver micros y horarios"));

                    listado.add(nombre);
                }
                radio=0.2;
            }else {
                if (radio<1){
                    radio = radio+0.2;
                    SolicitarRadio(geo.latitude,geo.longitude,radio);
                }else {
                    radio=0.2;
                    Toast.makeText(this,"No se encontraron paraderos cercanos",Toast.LENGTH_LONG).show();
                }

            }




        }catch (Exception e){
            e.printStackTrace();
        }
        return listado;
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem menuItem = menu.findItem(R.id.notificacion);
        menuItem.setIcon(buildCounterDrawable(count, R.mipmap.ic_perfil_24dp_azul));

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.toolbar_notificacion, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK && event.getRepeatCount() == 0) {

            // funcion para medir distancia

            try {


                Location punto1;
                Location punto2;

                punto1 = new Location("");
                punto1.setLatitude(mMap.getMyLocation().getLatitude());
                punto1.setLongitude(mMap.getMyLocation().getLongitude());

                punto2 = new Location("");
                punto2.setLatitude(lat);
                punto2.setLongitude(lon);

                textToSpeech.setLanguage(new Locale("spa", "CL"));
                speak("Debes agregar tu destino primero");

                float distancia = punto1.distanceTo(punto2);
                int dist = (int) distancia;
                if (dist < 1000) {

                    textToSpeech.setLanguage(new Locale("spa", "CL"));
                    speak("Faltan " + dist + " metros para llegar a tu destino");
                    Toast.makeText(this, "Faltan " + dist + " metros para llegar a tu destino", Toast.LENGTH_LONG).show();

                } else {
                    if (dist < 2000) {
                        dist = dist / 1000;
                        textToSpeech.setLanguage(new Locale("spa", "CL"));
                        speak("Faltan " + dist + " kilometro para llegar a tu destino");

                    } else {
                        dist = dist / 1000;
                        textToSpeech.setLanguage(new Locale("spa", "CL"));
                        speak("Faltan " + dist + " kilometros para llegar a tu destino");
                    }
                }


            } catch (Exception p) {
                p.getMessage();
                textToSpeech.setLanguage(new Locale("spa", "ESP"));
                speak("Debes agregar tu destino primero");

            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.LANG_MISSING_DATA | status == TextToSpeech.LANG_NOT_SUPPORTED) {
            Toast.makeText(this, "ERROR LANG_MISSING_DATA | LANG_NOT_SUPPORTED", Toast.LENGTH_SHORT).show();
        }
    }

    private void speak(String str) {
        textToSpeech.speak(str, TextToSpeech.QUEUE_FLUSH, null);
        textToSpeech.setSpeechRate(0.0f);
        textToSpeech.setPitch(0.0f);
    }

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        super.onDestroy();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.notificacion) {
            findViewById(R.id.layout_solicitudes).setVisibility(View.VISIBLE);
            btnCerrar.setVisibility(View.VISIBLE);
        }


        /*if (id == R.id.search) {
            findViewById(R.id.Layout1).setVisibility(View.VISIBLE);
            Busqueda.requestFocus();
        }*/


        if (id == R.id.action_salir) {

            finish();
        }


        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onBackPressed() {

        View layout_micros = findViewById(R.id.layout_micros);

        if (layout_micros.getVisibility() == View.VISIBLE) {

            layout_micros.setVisibility(View.GONE);

        }
        if (searchView.isSearchOpen()) {

            searchView.closeSearch();

        } else {
            Dialogo dialogo = new Dialogo();
            dialogo.show(getFragmentManager(), "Mi Dialogo");
        }


    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void doIncrease(int count2) {
        count = count2;
        invalidateOptionsMenu();
    }

    

}

