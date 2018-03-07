package com.letchile.let;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.Servicios.ConexionInternet;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class LoginActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    DBprovider db;
    Boolean conexion = false;
    Button btnLogin;
    private final int MY_PERMISSIONS = 100;

    public LoginActivity(){
        db = new DBprovider(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        conexion = new ConexionInternet(this).isConnectingToInternet();

        btnLogin = (Button) findViewById(R.id.btnlogin);

        if(mayRequestStoragePermission())
            btnLogin.setEnabled(true);
        else
            btnLogin.setEnabled(false);


        //llamar al usuario insertado de la base de datos para omitir el login
        String usuario = db.obtenerUsuario();
        if(usuario.equals("")) {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText usuario = (EditText) findViewById(R.id.usuarioM);
                    EditText password = (EditText) findViewById(R.id.contrasenaM);

                    new SendPostRequest().execute(usuario.getText().toString(), password.getText().toString());
                }
            });
        }else{
            Intent i = new Intent(LoginActivity.this, InsPendientesActivity.class);
            startActivity(i);
        }

    }

    private boolean mayRequestStoragePermission() {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        // }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(LoginActivity.this, "Permisos aceptados", Toast.LENGTH_SHORT).show();
                btnLogin.setEnabled(true);
            }
        }else{
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }


    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){
            pDialog= new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Autenticando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        protected String doInBackground(String... parametros) {

            //*********************************************LOGIN*****************************************//
            try {
                if(conexion){
                    URL url = new URL("https://www.autoagenda.cl/movil/login/verificaLogeo"); // here is your URL path

                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("usr", parametros[0]);
                    postDataParams.put("pwd", parametros[1]);
                    Log.e("params", postDataParams.toString());

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));

                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {

                        BufferedReader in = new BufferedReader(new
                                InputStreamReader(
                                conn.getInputStream()));

                        StringBuffer sb = new StringBuffer("");
                        String line = "";

                        while ((line = in.readLine()) != null) {

                            sb.append(line);
                            break;
                        }

                        in.close();

                        //agregar el usuario del telefono
                        String json = sb.toString();
                        try {
                            JSONObject obj = new JSONObject(json);
                            if (obj.getString("MSJ").equals("3") || obj.getString("MSJ").equals("6")) {
                                db.inserUsuario(parametros[0], parametros[1],Integer.parseInt(obj.getString("MSJ")));
                            }
                        } catch (Throwable t) {
                            Log.e("LET", "Could not parse malformed JSON: \"" + json + "\"");
                        }

                        //String con el json de respuesta
                        return sb.toString();

                    } else {
                        return new String("false : " + responseCode);
                    }


                }else{
                    return "No hay conexión";
                }



            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }



        }

        @Override
        protected void onPostExecute(String result) {

            String json = result;
            try {
                //crear json para leer viñeta
                JSONObject obj = new JSONObject(json);
                if (obj.getString("MSJ").equals("3") || obj.getString("MSJ").equals("6")) {
                    pDialog.dismiss();
                    Intent i = new Intent(LoginActivity.this, InsPendientesActivity.class);
                    startActivity(i);
                } else {
                    pDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Usuario y/o contraseña incorrectas", Toast.LENGTH_SHORT).show();
                }
            }catch (Throwable t){
                Log.e("My App", "No se pudo convertir el json: \"" + json + "\"");
            }
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }
}





















