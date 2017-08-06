package com.targetapps.despierta;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.targetapps.despierta.widget.PullScrollView;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import dmax.dialog.SpotsDialog;


public class MiPerfilActivity extends ActionBarActivity implements PullScrollView.OnTurnListener {


    TextView tvNombre;
    ImageView ivPerfil;

    String num_user,num_user2;

    httpHandler handler = new httpHandler();

    private PullScrollView mScrollView;
    private ImageView mHeadImg;
    private TableLayout mMainLayout;

    private Toolbar toolbar;

    FloatingActionButton btnAgregar, btnPerfil;
    EditText etbuscarAmigos;
    Button btnBuscarAmigos;
    TextView tvNombreUsuario;
    ImageView imgPerfil;


    private ListView lvSolicitudes;

    private String jsonResult;
    private ListView lvAmigos;

    String usuario;

    String nombre1="";
    String nombre2="";
    String [] items;
    String [] item;
    String [] items2;
    String user_envia;
    AlertDialog.Builder builderListaUbicacion;



    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_mi_perfil);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        etbuscarAmigos = (EditText) findViewById(R.id.buscarAmigos);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.button5);
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.button6);


        btnAgregar = fab;
        btnPerfil = fab2;


        btnBuscarAmigos = (android.widget.Button) findViewById(R.id.button4);
        tvNombreUsuario = (TextView) findViewById(R.id.textView3);
        imgPerfil = (ImageView) findViewById(R.id.imageView);

        tvNombreUsuario.setVisibility(View.INVISIBLE);



        lvSolicitudes = (ListView) findViewById(R.id.Lsolicitudes);

        String url = "http://52.88.17.215/ListadoAmigos.php?user=" +usuario;

        lvAmigos = (ListView) findViewById(R.id.listView1);
        accessWebService();



        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            usuario = profile.getId();
            JsonReadTask task = new JsonReadTask();
            task.execute(url);
            cargarPerfil();
            initView();
            showTable();
            nombre1 = profile.getId();
        } else {
            Toast.makeText(this, "Debe loguearse", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Login.class));
            finish();
        }
        obtAmigosRecibidos();


    }



    public void obtAmigosRecibidos(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://52.88.17.215/GetData.php?usuario="+nombre1;

        RequestParams parametros = new RequestParams();
        parametros.put("edad", 18);

        client.post(url, parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    obtUbiRec(new String(responseBody));
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Toast.makeText(MiPerfilActivity.this, "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public ArrayList<String> obtUbiRec(String response){
        ArrayList<String> listaado = new ArrayList<String>();
        try{
            JSONArray jsonArray = new JSONArray(response);
            String texto;


            items = new String[jsonArray.length()];
            items2 = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++){
                texto = jsonArray.getJSONObject(i).getString("usuarios_usuario");
                num_user2 = jsonArray.getJSONObject(i).getString("usuarios_telefono");


                items[i] = texto.replaceAll("-recibido-","");
                items2[i]= num_user2;

            }

            if(jsonArray.length()>0){
                builderListaUbicacion = new AlertDialog.Builder(MiPerfilActivity.this);
                builderListaUbicacion.setTitle("Ubicaciones recibidas")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                nombre2 = items2[item];
                                devolver();
                                Intent intent = new Intent(MiPerfilActivity.this, MainActivity.class);
                                intent.putExtra("nombre1", "" + nombre1);
                                intent.putExtra("nombre2", "" + nombre2.replaceAll("-recibido-",""));
                                user_envia=items[item];
                                intent.putExtra("usr",user_envia);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                dialog.cancel();
                                finish();
                            }
                        });

                builderListaUbicacion.create();
                builderListaUbicacion.show();


            }



        }catch (Exception e){
            e.printStackTrace();
        }

        return listaado;
    }



    public void devolver() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://52.88.17.215/PutDevolver.php?";
        String parametros = "user1=" + nombre2 + "&user2="+nombre1;

        client.post(url + parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    Toast.makeText(MiPerfilActivity.this, "Cargando ubicacion de "+user_envia, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Toast.makeText(MiPerfilActivity.this, "Error, inténtalo nuevamente", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void BuscarAmigos(View view) {

        final String numero = etbuscarAmigos.getText().toString().trim();

        if (numero.equals("")) {
            Toast.makeText(this, "Ingrese un número telefónico", Toast.LENGTH_LONG).show();
        } else {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                final String buscarAmigos = handler.get("http://52.88.17.215/CargarPersonas.php?numero=" + numero);
                switch (buscarAmigos) {
                    case "Usuario no encontrado":
                        Toast.makeText(this, "Usuario no existe", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        tvNombreUsuario.setVisibility(View.VISIBLE);
                        btnAgregar.setVisibility(View.VISIBLE);
                        btnPerfil.setVisibility(View.VISIBLE);
                        tvNombreUsuario.setText(buscarAmigos);


                }





                btnAgregar.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        try {

                            Profile profile = Profile.getCurrentProfile();
                            String prefUsuario = profile.getId().toString();

                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            String agregarAmigo = handler.get("http://52.88.17.215/AgregarAmigo.php?user=" + prefUsuario + "&user2=" +numero);
                            //Tu-La otra persona
                            switch (agregarAmigo) {
                                case "Registro ingresado":
                                    Toast.makeText(getApplicationContext(), "Solicitud de amistad enviada", Toast.LENGTH_SHORT).show();
                                    findViewById(R.id.buscador).setVisibility(View.GONE);
                                    break;
                                case "Solicitud ya enviada":
                                    Toast.makeText(getApplicationContext(), "Solicitud ya enviada", Toast.LENGTH_LONG).show();
                                    break;
                                case "Error":
                                    Toast.makeText(getApplicationContext(), "Error en el sistema", Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(getApplicationContext(), "No se completo la accion", Toast.LENGTH_LONG).show();

                                    //

                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
                        }
                    }
                });


                btnPerfil.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), PerfilAmigoActivity.class);
                        intent.putExtra("amigo", numero);
                        startActivity(intent);
                    }
                });


                Profile profile = Profile.getCurrentProfile();
                String prefUsuario = profile.getFirstName().toString();
                String consultarAmigo = handler.get("http://52.88.17.215/ConsultarAmigo.php?user=" + prefUsuario + "&user2=" + buscarAmigos);

                switch (consultarAmigo) {
                    case "No son amigos":
                        break;
                    case "Si son amigos":
                        btnAgregar.setClickable(false);
                        break;
                    case "Solicitud de amistad enviada":
                        btnAgregar.setClickable(false);
                        break;
                    case "Solicitud de amistad pendiente":
                        btnAgregar.setClickable(false);
                        break;
                    default:
                        //

                }

            } catch (Exception e) {
                //
                Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onClickSelectContact(View btnSelectContact) {

        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();

            retrieveContactNumber();


        }
    }


    private void retrieveContactNumber() {

        String contactNumber = null;


        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);


        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();


        etbuscarAmigos.setText(contactNumber);

    }

    public void BuscarA(View view) {
        findViewById(R.id.buscador).setVisibility(View.VISIBLE);

    }

    public void mSolicitudes(View view) {
        findViewById(R.id.solicitudes).setVisibility(View.VISIBLE);
        String url2 = "http://52.88.17.215/CargarSolicitudes.php?user=" +usuario;

        JsonReadTask2 task2 = new JsonReadTask2();
        task2.execute(url2);
    }


    public void cargarPerfil() {

        Profile profile = Profile.getCurrentProfile();
        tvNombre = (TextView) findViewById(R.id.nombre_usuario);
        ProfilePictureView profilePictureView;
        profilePictureView = (ProfilePictureView) findViewById(R.id.imagen_perfil);
        //TextView mUserId = (TextView) findViewById(R.id.mUserId);

        if (profile != null) {
            tvNombre.setText(profile.getFirstName());
            //mUserId.setText("User Id: " + profile.getId());
            profilePictureView.setProfileId(profile.getId());

        }
    }

    protected void initView() {

        mScrollView = (PullScrollView) findViewById(R.id.scroll_view);
        mHeadImg = (ImageView) findViewById(R.id.background_img);
        mMainLayout = (TableLayout) findViewById(R.id.table_layout);
        mScrollView.setHeader(mHeadImg);
        mScrollView.setOnTurnListener(this);

    }

    public void showTable() {
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.leftMargin = 30;
        layoutParams.bottomMargin = 10;
        layoutParams.topMargin = 10;

    }

    public void onTurn() {

    }

    //inicio lista amigos

    private class JsonReadTask extends AsyncTask<String, Void, String> {

        AlertDialog dialog = new SpotsDialog(MiPerfilActivity.this, R.style.Custom);

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                // e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "No se pudieron cargar sus amigos", Toast.LENGTH_LONG).show();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String result) {
            ListDrwaer();
            dialog.dismiss();
        }
    }// end async task

    private class JsonReadTask2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                // e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "Error..." + e.toString(), Toast.LENGTH_LONG).show();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String result) {
            ListDrawer();
        }
    }// end async task

    public void accessWebService() {

    }

    // build hash set for list view
    public void ListDrwaer() {
        List<Map<String, String>> employeeList = new ArrayList<Map<String, String>>();

        try {

                JSONObject jsonResponse = new JSONObject(jsonResult);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("amigos");

                    item = new String[jsonMainNode.length()];
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    String name = jsonChildNode.optString("usuarios_usuario");
                    String number = jsonChildNode.optString("usuarios_telefono");
                    item[i]=number;
                    String outPut = name;
                    employeeList.add(createEmployee("amistad", outPut));
                }




        } catch (JSONException e) {
            Log.v(null, "Error:" + e);
            //Toast.makeText(getApplicationContext(), "Ningun "+e, Toast.LENGTH_SHORT).show();
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, employeeList,
                R.layout.amigos_lista,
                new String[]{"amistad"}, new int[]{R.id.textView});


        lvAmigos.setAdapter(simpleAdapter);


        lvAmigos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String valor = lvAmigos.getItemAtPosition(position).toString();

                String valorReemplace = valor.replace("{amistad=", "");
                final String valorReemplace2 = valorReemplace.replace("}", "");

                //Toast.makeText(ListadoAmigosActivity.this, valorReemplace2, Toast.LENGTH_LONG).show();

                Intent i = new Intent(MiPerfilActivity.this, PerfilAmigoActivity.class);
                i.putExtra("amigo", item[position]);
                startActivity(i);

            }
        });
    }

    public void ListDrawer() {
        List<Map<String, String>> employeeList = new ArrayList<Map<String, String>>();

        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("solicitudes");


            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name = jsonChildNode.optString("usuarios_usuario");
                String number = jsonChildNode.optString("usuarios_telefono");
                num_user=number;
                String outPut = name;
                employeeList.add(createEmployee("solicitud", outPut));


            }

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "No tiene solicitudes pendientes",
                    Toast.LENGTH_SHORT).show();
            findViewById(R.id.solicitudes).setVisibility(View.GONE);

        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, employeeList,
                R.layout.city_row_item,
                new String[]{"solicitud"}, new int[]{R.id.cityName});


        lvSolicitudes.setAdapter(simpleAdapter);


        lvSolicitudes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position,
                                    long id) {

                String valor = lvSolicitudes.getItemAtPosition(position).toString();

                String valorReemplace = valor.replace("{solicitud=", "");
                final String valorReemplace2 = valorReemplace.replace("}", "");

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(MiPerfilActivity.this);
                dialogo1.setTitle("Solicitud de amistad");
                dialogo1.setMessage(valorReemplace2 + " quiere ser su amigo.");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        aceptar(valorReemplace2);
                        String url2 = "http://52.88.17.215/CargarSolicitudes.php?user=" + usuario;

                        JsonReadTask2 task2 = new JsonReadTask2();
                        task2.execute(url2);
                        String url = "http://52.88.17.215/ListadoAmigos.php?user=" +usuario;
                        JsonReadTask task = new JsonReadTask();
                        task.execute(url);
                        findViewById(R.id.solicitudes).setVisibility(View.GONE);
                    }
                });
                dialogo1.setNegativeButton("Rechazar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        cancelar(valorReemplace2);
                        String url2 = "http://52.88.17.215/CargarSolicitudes.php?user=" + usuario;

                        JsonReadTask2 task2 = new JsonReadTask2();
                        task2.execute(url2);
                        String url = "http://52.88.17.215/ListadoAmigos.php?user=" +usuario;
                        JsonReadTask task = new JsonReadTask();
                        task.execute(url);
                        findViewById(R.id.solicitudes).setVisibility(View.GONE);
                    }
                });
                dialogo1.show();
            }
        });
    }

    public void aceptar(String Solicitante) {
        try {
            Profile profile = Profile.getCurrentProfile();
            usuario = profile.getId().toString();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String registro = handler.get("http://52.88.17.215/AceptarSolicitud.php?user=" + num_user  + "&user2=" + usuario);


        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();

        }
    }

    public void cancelar(String Solicitante) {
        try {
            Profile profile = Profile.getCurrentProfile();
            usuario = profile.getId().toString();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String registro = handler.get("http://52.88.17.215/RechazarSolicitud.php?user=" + num_user + "&user2=" + usuario);


        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();

        }
    }

    private HashMap<String, String> createEmployee(String name, String number) {
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
    }



    //fin lista amigos


    @Override
    public void onBackPressed() {

        View layout_buscador = findViewById(R.id.buscador);

        if (layout_buscador.getVisibility() == View.VISIBLE){
            findViewById(R.id.buscador).setVisibility(View.GONE);
        }else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mi_perfil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

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

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.solicitudes).setVisibility(View.GONE);
        String url = "http://52.88.17.215/ListadoAmigos.php?user=" +usuario;
        JsonReadTask task = new JsonReadTask();
        task.execute(url);
        cargarPerfil();
    }


}
