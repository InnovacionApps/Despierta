package com.targetapps.despierta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class Login extends ActionBarActivity {


    //ViewPager pager;

    private Toolbar toolbar;

    Button btTel;
    Button btnLogin;
    EditText etTel;
    String tel;

    httpHandler handler = new httpHandler();

    private CallbackManager mCallbackManager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etTel = (EditText) findViewById(R.id.etTelefono);


        //pager = (ViewPager) findViewById(R.id.viewpager);

        //btnLogin = (Button)findViewById(R.id.login);
        //btnRegistro = (Button)findViewById(R.id.registro);
        //etUser = (EditText)findViewById(R.id.user);


        mCallbackManager = CallbackManager.Factory.create();
        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken old, AccessToken newToken) {

            }
        };

        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {

            }
        };

        mTokenTracker.startTracking();
        mProfileTracker.startTracking();

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(mCallbackManager, mCallBack);


        //consultaSesion();

    }

    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            //cargarPerfil();
            /*ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("http://www.targetphoneapps.com/"))
                    .build();

            ShareButton shareButton = (ShareButton) findViewById(R.id.share);
            shareButton.setShareContent(content);
            */
            Check();
        }


        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };


    /*
    public void ingreseTelefono(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Ingrese su Telefono");
        builder.setInverseBackgroundForced(true);
        builder.setNegativeButton(R.string.minimizar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "Despierta funcionara en segundo plano", Toast.LENGTH_LONG).show();

            }
        });
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void Registrar() {

        Profile profile = Profile.getCurrentProfile();
        String id = profile.getId();
        String usuario = profile.getFirstName().toString().trim();
        String telefono = etTel.getText().toString().trim();

        if (telefono.length()<8) {
            Toast.makeText(this, "Numero debe tener 8 digitos", Toast.LENGTH_LONG).show();
        } else {

            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                String registro = handler.get("http://52.88.17.215/Registro1.php?id="+ id +"&user=" + usuario + "&telefono=" + telefono);

                switch (registro) {
                    case "Usuario registrado":
                        startActivity(new Intent(this,MiPerfilActivity.class));
                        finish();
                        //Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                        break;
                    case "Usuario no registrado":
                        Toast.makeText(this, "Usuario no registrado, por favor inténtelo nuevamente", Toast.LENGTH_LONG).show();
                        break;
                    case "Telefono ya existe":
                        Toast.makeText(this, "El teléfono que está intentando de registrar ya existe.", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void Check() {

        Profile profile = Profile.getCurrentProfile();
        if (profile == null){
            findViewById(R.id.layout_telefono).setVisibility(View.VISIBLE);
            etTel.requestFocus();
            btTel = (Button) findViewById(R.id.btTelefono);
            btTel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Registrar();
                    findViewById(R.id.layout_telefono).setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(), MiPerfilActivity.class));
                    finish();
                }
            });
        }else {
            String id = profile.getId();


            if (id.equals("")) {
                Toast.makeText(this, "Ingrese sus datos de usuario", Toast.LENGTH_LONG).show();
            } else {

                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String check = handler.get("http://52.88.17.215/Checkuser.php?id="+ id);

                    switch (check) {
                        case "Usuario existente":
                            startActivity(new Intent(getApplicationContext(), MiPerfilActivity.class));
                            finish();
                            break;
                        case "Usuario no existe":
                            findViewById(R.id.layout_telefono).setVisibility(View.VISIBLE);
                            etTel.requestFocus();
                            btTel = (Button) findViewById(R.id.btTelefono);
                            btTel.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {
                                    Registrar();
                                    findViewById(R.id.layout_telefono).setVisibility(View.GONE);
                                    startActivity(new Intent(getApplicationContext(), MiPerfilActivity.class));
                                    finish();
                                }
                            });
                            break;
                        default:
                            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
                }
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_salir) {

            finish();

        }

        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onStop() {
        super.onStop();
        mTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
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
