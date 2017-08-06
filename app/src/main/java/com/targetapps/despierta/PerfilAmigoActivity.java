package com.targetapps.despierta;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.targetapps.despierta.widget.PullScrollView;


import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;


public class PerfilAmigoActivity extends ActionBarActivity implements PullScrollView.OnTurnListener {


    private Toolbar toolbar;

    private GoogleMap mMap;
    String user_pide ="o";
    String user_entrega ="o";
    double lat;
    double lon;
    String nombre;
    TextView tv;
    Button btnAgregarAmigo;
    ImageView ivPerfil;

    httpHandler handler = new httpHandler();

    String usuario;

    private PullScrollView mScrollView;
    private ImageView mHeadImg;

    String nom;



    MediaPlayer alerta; //variable para que suene al recibir una solicitud


    Profile profile = Profile.getCurrentProfile();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_perfil_amigo);

        setUpMapIfNeeded();

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); // tipo de mapa
        mMap.setMyLocationEnabled(true);
        mMap.getMyLocation();


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv = (TextView) findViewById(R.id.textView2);
        btnAgregarAmigo = (Button) findViewById(R.id.button);
        ivPerfil = (ImageView) findViewById(R.id.imageView);


        Bundle _bundle = getIntent().getExtras();
        usuario = _bundle.getString("amigo");

        //Toast.makeText(getApplicationContext(),"Amigo nombre"+usuario,Toast.LENGTH_SHORT).show();

        CargarImagenAmigo();

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String cargarPerfil = handler.get("http://52.88.17.215/CargarPerfil.php?user=" + usuario);
            nom=cargarPerfil;


            if (!cargarPerfil.equals("Usuario no encontrado")) {
                tv.setText(cargarPerfil);
                consultarAmigo();
            } else {
                tv.setText("Usuario No Encontrado");
                btnAgregarAmigo.setVisibility(View.INVISIBLE);
                ivPerfil.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
        }

        Button btnEnvio = (Button) findViewById(R.id.btnEnviar);
        try{
            //saber de donde viene para ocultar el boton "Enviar"
            Profile profile = Profile.getCurrentProfile();

            //para saber si viene de MainActivity2
            user_pide =profile.getFirstName();
            user_entrega =usuario;

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "FALLO: "+e, Toast.LENGTH_SHORT).show();
        }
        btnEnvio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
                boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (!enabled) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Debe activar su GPS", Toast.LENGTH_SHORT).show();
                } else {
                    lat = mMap.getMyLocation().getLatitude();
                    lon = mMap.getMyLocation().getLongitude();
                    enviarDatos(); // envia la ubicacion a la bdd
                }

            }
        });


        Button btnUbica = (Button) findViewById(R.id .btnUbicar);
        btnUbica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre = user_entrega;
                AlertDialog.Builder builder = new AlertDialog.Builder(PerfilAmigoActivity.this);


                    builder.setMessage("¿Enviar solicitud de Ubicación a " + nom + "?")
                            .setTitle("Ubicar")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (usuario.length() != 0) {
                                        enviarSolicitud();
                                        //Toast.makeText(MainActivity.this, "solicitud enviada" , Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(PerfilAmigoActivity.this, "Escribe tu nombre primero", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    //Toast.makeText(MainActivity.this, "CANCEL", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });

                builder.create();
                builder.show();
            }
        });

        initView();


    }

    public void CargarImagenAmigo(){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String cargarPerfil = handler.get("http://52.88.17.215/CargarImagen.php?user=" + usuario);

            ProfilePictureView profilePictureView;
            profilePictureView = (ProfilePictureView) findViewById(R.id.imagen_perfil_amigo);
            //TextView mUserId = (TextView) findViewById(R.id.mUserId);

            if (profile != null) {
                //mUserId.setText("User Id: " + profile.getId());
                profilePictureView.setProfileId(cargarPerfil);

            }
        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
        }
    }

    //envia la solicitud de ubicacion a la bdd
    public void enviarSolicitud() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://52.88.17.215/PutGps.php?";
        String parametros = "user1=" + nombre + "&user2="+profile.getId();

        client.post(url + parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    //String resultado = new String(responseBody) ;
                    Toast.makeText(PerfilAmigoActivity.this, "Enviado correctamente a "+nom, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(PerfilAmigoActivity.this, "Error, inténtalo nuevamente", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {

            }
        }
    }



    protected void initView() {
        mScrollView = (PullScrollView) findViewById(R.id.scroll_view);
        mHeadImg = (ImageView) findViewById(R.id.background_img);

        mScrollView.setHeader(mHeadImg);
        mScrollView.setOnTurnListener(this);
    }


    public void onTurn() {

    }

    public void AgregarAmigo(View view) {
        try {

            String prefUsuario = profile.getId();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String agregarAmigo = handler.get("http://52.88.17.215/AgregarAmigo.php?user=" + prefUsuario + "&user2=" + usuario);
            //Tu-La otra persona
            switch (agregarAmigo) {
                case "Registro ingresado":
                    Toast.makeText(this, "Solicitud de amistad enviada", Toast.LENGTH_SHORT).show();
                    break;
                case "Error":
                    Toast.makeText(this, "Error en el sistema", Toast.LENGTH_LONG).show();
                    break;
                default:
                    //

            }

        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
        }
    }


    void consultarAmigo() {

        String prefUsuario = profile.getId().toString();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String consultarAmigo = handler.get("http://52.88.17.215/ConsultarAmigo.php?user=" + prefUsuario + "&user2=" + usuario);

        switch (consultarAmigo) {
            case "No son amigos":
                findViewById(R.id.btnUbicar).setVisibility(View.GONE);
                break;
            case "Si son amigos":
                //btnAgregarAmigo.setVisibility(View.INVISIBLE);
                btnAgregarAmigo.setText("Amigos");
                btnAgregarAmigo.setClickable(false);
                btnAgregarAmigo.setTextSize(10);
                btnAgregarAmigo.setBackgroundColor(Color.parseColor("#2196F3"));
                break;
            case "Solicitud de amistad enviada":
                btnAgregarAmigo.setText("Solicitud enviada");
                btnAgregarAmigo.setClickable(false);
                btnAgregarAmigo.setTextSize(10);
                btnAgregarAmigo.setBackgroundColor(Color.parseColor("#2196F3"));
                break;
            case "Solicitud de amistad pendiente":
                btnAgregarAmigo.setText("Solicitud pendiente");
                btnAgregarAmigo.setClickable(false);
                btnAgregarAmigo.setTextSize(10);
                btnAgregarAmigo.setBackgroundColor(Color.parseColor("#2196F3"));
                break;
            default:
                //

        }
    }


    //Enviar ubicacion
    public void enviarDatos() {
        final String user_pide2;
        user_pide2=profile.getId();
        final String user_entrega2;
        user_entrega2 = usuario;
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://52.88.17.215/PutPermitir.php?";
        String parametros = "user1=" + user_pide2 + "&user2="+user_entrega2+"&lat="+lat+"&lon="+lon;

        client.post(url + parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    //String resultado = new String(responseBody) ;
                    Toast.makeText(PerfilAmigoActivity.this, "Ubicación enviada a "+nom, Toast.LENGTH_SHORT).show();
                    //finish(); // se cierra el mapa
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(PerfilAmigoActivity.this, "Error, inténtalo nuevamente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_salir) {

            finish();
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }


}
