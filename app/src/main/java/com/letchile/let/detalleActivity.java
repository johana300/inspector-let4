package com.letchile.let;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.Fallida.Fallida;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.VehLiviano.SeccionActivity;
import com.letchile.let.VehPesado.SeccionVpActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

public class detalleActivity extends AppCompatActivity {

    DBprovider db;
    TextView n_oi,pantete,asegurado,direccion,comentario,fono,ramo,pac,marca,modelo;
    Button btnInspeccion, btnAddhito, btnVolver,btnFallida;
    Boolean conexion1 = false;
    ProgressDialog pDialog;
    long minutosDiferencia;
    JSONObject jsonInspe;
    String fecha_cita,hora_cita;
    Spinner comboRamo;

    public detalleActivity(){
        db = new DBprovider(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        conexion1 = new ConexionInternet(this).isConnectingToInternet();

        final String perfil = db.obtenerPerfil();

        final AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);

        //variable id_inspeccion
        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");
        fecha_cita=bundle.getString("fecha_cita");
        hora_cita=bundle.getString("hora_cita");
        final String fecha_total = fecha_cita+'T'+hora_cita;

        final Calendar c= Calendar.getInstance();
        //DateFormat df = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        //String date = df.format(Calendar.getInstance().getTime());


        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm");
        try {

            Date fechaCita = format.parse(fecha_total);
            Calendar Datecita = Calendar.getInstance();
            Datecita.setTime(fechaCita);

            long msDiff = Calendar.getInstance().getTimeInMillis()-Datecita.getTimeInMillis() ;
            minutosDiferencia = TimeUnit.MILLISECONDS.toMinutes(msDiff);

            //Toast.makeText(this,"diferencia: "+minutosDiferencia,Toast.LENGTH_SHORT).show();

        } catch (Exception exception) {
            Log.e("DIDN'T WORK", "exception " + exception);
        }


        String[][] datosInspeccion=db.BuscaDatosInspeccion(id_inspeccion);

        n_oi = (TextView)findViewById(R.id.n_oim);
        n_oi.setText(id_inspeccion);

        pac = (TextView)findViewById(R.id.pac);
        if(datosInspeccion[0][12].toString().equals("S")) {
            pac.setText("Si");
        }else if(datosInspeccion[0][12].toString().equals("") || datosInspeccion[0][12].isEmpty()){
            pac.setText("No");
        }else{
            pac.setText("");
        }

        asegurado = (TextView)findViewById(R.id.aseguradoM);
        asegurado.setText(datosInspeccion[0][1]);

        pantete = (TextView)findViewById(R.id.patenteM);
        pantete.setText(db.accesorio(Integer.parseInt(id_inspeccion),363).toString());

        direccion = (TextView)findViewById(R.id.direccionM);
        direccion.setText(datosInspeccion[0][4]);

        comentario = (TextView)findViewById(R.id.comentarioM);
        comentario.setText(datosInspeccion[0][3]);

        fono = (TextView)findViewById(R.id.telefonoM);
        fono.setText(datosInspeccion[0][2]);

        marca = (TextView)findViewById(R.id.MarcaMQ);
        marca.setText(datosInspeccion[0][13]);

        modelo = (TextView)findViewById(R.id.modeloMQ);
        modelo.setText(datosInspeccion[0][14]);

        ramo = (TextView)findViewById(R.id.tipoVehiculoM);
        if(datosInspeccion[0][7].toString().equals("vl1")) {
            ramo.setText("Vehículo liviano");
        }else if(datosInspeccion[0][7].toString().equals("vp1")){
            ramo.setText("Vehículo pesado");
        }else{
            ramo.setText("");
        }

        // cargar ramo
        comboRamo = (Spinner)findViewById(R.id.comboRamo);
        String[] arraytipo = getResources().getStringArray(R.array.tipo_ramo);
        final List<String> arraytipolist = Arrays.asList(arraytipo);
        ArrayAdapter<String> spinner_adapter1 = new ArrayAdapter<String>(detalleActivity.this,android.R.layout.simple_list_item_1,arraytipolist);
        spinner_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboRamo.setAdapter(spinner_adapter1);


        //Inspeccionar
        btnInspeccion = (Button)findViewById(R.id.btnInspeccionarM);
        btnInspeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(detalleActivity.this);
                builder.setCancelable(false);
                builder.setMessage(Html.fromHtml("¿Seguro que desea realizar la inspeccion <b>N°OI: "+id_inspeccion+"</b>?. <b>Recuerde que una vez comenzada la inspección</b>" +
                        " <b><font color='#FF0000'>NO</font> podrá volver atrás.</b>"));

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(ramo.getText().toString().equals("Vehículo liviano")) {
                            Intent inn = new Intent(detalleActivity.this,SeccionActivity.class);
                            inn.putExtra("id_inspeccion",n_oi.getText().toString());
                            startActivity(inn);
                        }else if(ramo.getText().toString().equals("Vehículo pesado")){
                           // Toast.makeText(detalleActivity.this, "Pantalla vehículo pesado", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent( detalleActivity.this, SeccionVpActivity.class);
                            intent.putExtra("id_inspeccion",n_oi.getText().toString());
                            startActivity(intent);
                        }
                    }
                });

                builder.setNegativeButton("Rechazar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(detalleActivity.this, "Inspección rechazada", Toast.LENGTH_LONG).show();
                    }
                });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        });

        //Agregar hito

        //Volver al layout anterior
        btnVolver = (Button)findViewById(R.id.btnVolverM);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent in = new Intent(detalleActivity.this,InsPendientesActivity.class);
                //startActivity(in);
                onBackPressed();
            }
        });

        //Botón declarar Fallida  btnFallida
        btnFallida = (Button)findViewById(R.id.btnFallida);
        btnFallida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //pregunto perfil, si es 3 validar horario de fallida, 6 sin validaciones
                if(perfil.equals("3"))
                {
                    if(minutosDiferencia<=30 && minutosDiferencia>=-30){
                        /*Intent intent = new Intent(detalleActivity.this,Fallida.class);
                        intent.putExtra("id_inspeccion",n_oi.getText().toString());
                        startActivity(intent);*/
                        new notificarFallida().execute(n_oi.getText().toString(),db.obtenerUsuario());
                    }else{
                        Toast.makeText(detalleActivity.this, "Fuera de rango de horario" , Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    new notificarFallida().execute(n_oi.getText().toString(),db.obtenerUsuario());
                    /*Intent intent = new Intent(detalleActivity.this,Fallida.class);
                    intent.putExtra("id_inspeccion",n_oi.getText().toString());
                    new notificarFallida().execute(n_oi.getText().toString(),db.obtenerUsuario());*/

                    //startActivity(intent);
                }
            }
        });


    }



    public void cambioRamo(View view)    {
        String cRamo = comboRamo.getSelectedItem().toString();
        String comboRamoo = "";
        if(cRamo.equals("Vehículo liviano"))
        {
            comboRamoo = "vl1";
            //Toast.makeText(detalleActivity.this, "Ha cambiado a vehículo liviano", Toast.LENGTH_LONG).show();
        }
        else{
            comboRamoo = "vp1";
            // Toast.makeText(detalleActivity.this, "Ha cambiado a vehículo pesado", Toast.LENGTH_LONG).show();
        }
        new envioRamo().execute(n_oi.getText().toString(),comboRamoo);
    }




    public class envioRamo extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){
            pDialog= new ProgressDialog(detalleActivity.this);
            //pDialog.setMessage("Autenticando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... param) {

            //*********************************************ramo*****************************************//
            try {
                if (conexion1) {
                    URL url = new URL("https://www.autoagenda.cl/movil/cargamovil/cambiaRamo"); // here is your URL path

                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("id_inspeccion", param[0]);
                    postDataParams.put("comboRamo", param[1]);
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

                        String json = sb.toString();

                        try {
                            JSONObject obj = new JSONObject(json);

                            obj.getString("MSJ");
                            if (obj.getString("MSJ").equals("Ok") ) {
                                db.actualizaRamo(Integer.parseInt(param[0]), param[1]);

                                if( json.indexOf("Ok")>0) {
                                    String[][] datosInspeccion = db.BuscaDatosInspeccion(param[0]);
                                    if (datosInspeccion[0][7].toString().equals("vl1")) {
                                        ramo.setText("Vehículo liviano");
                                    } else if (datosInspeccion[0][7].toString().equals("vp1")) {
                                        ramo.setText("Vehículo pesado");
                                    } else {
                                        ramo.setText("");
                                    }
                                }
                            }
                        } catch (Throwable t) {
                            Log.e("LET", "Could not parse malformed JSON: \"" + json + "\"");
                        }




                        //String con el json de respuesta
                        return sb.toString();

                    } else {
                        return new String("false : " + responseCode);
                    }


                } else {
                    return "No hay conexión";
                }


            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }


        }

        @Override
        protected void onPostExecute(String result) {

            pDialog.dismiss();
            Toast.makeText(detalleActivity.this, "Se ha actualizado ramo", Toast.LENGTH_SHORT).show();
        }
    }




    public class notificarFallida extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){
            pDialog= new ProgressDialog(detalleActivity.this);
            //pDialog.setMessage("Autenticando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... param) {

            //fallida $.post("https://www.autoagenda.cl/movil/cargamovil/fotoFallida",{id_inspeccion:oi,usr:user},function(subefotofallida){
            conexion1 = new ConexionInternet(detalleActivity.this).isConnectingToInternet();
            try {
                if (conexion1) {
                    URL url = new URL("https://www.autoagenda.cl/movil/cargamovil/fotoFallida"); // here is your URL path

                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("id_inspeccion", param[0]);
                    postDataParams.put("usr", param[1]);
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

                        //se llena la inspeccion fallida
                        String json = sb.toString();
                        JSONArray jsonar = new JSONArray(json);
                        try{
                            if(!jsonar.isNull(0)){
                                String result = "";
                                for (int i = 0; i < jsonar.length(); i++) {
                                    jsonInspe = new JSONObject(jsonar.getString(i));
                                    //borrar inspeccion anterior
                                    //db.borrarInspeccionFallida(Integer.parseInt(param[0]));
                                    /*result = db.insertaInspeccionesFallida(jsonInspe.getInt("id_inspeccion"),jsonInspe.getString("fecha"),jsonInspe.getString("comentario"),
                                            jsonInspe.getInt("idFallida"),jsonInspe.getString("fecha_cita"),jsonInspe.getString("hora_cita"),jsonInspe.getInt("activo"));*/

                                    db.borrarInspeccionFallida(jsonInspe.getInt("id_inspeccion"));
                                    result = db.insertaInspeccionesFallida(jsonInspe.getInt("id_inspeccion"),jsonInspe.getString("fechaFallida"),jsonInspe.getString("comentarioFallida"),
                                            jsonInspe.getInt("idFallida"),jsonInspe.getString("fechaCita"),jsonInspe.getString("horaCita"),jsonInspe.getInt("activo"));
                                }
                            }
                        }catch (Exception e){
                            Log.e("Error al convertir json", e.getMessage());
                        }
                        //String con el json de respuesta
                        return sb.toString();

                    } else {
                        return new String("false : " + responseCode);
                    }
                } else {
                    return "No hay conexión";
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {

            pDialog.dismiss();

            Intent in = new Intent(detalleActivity.this,Fallida.class);
            in.putExtra("id_inspeccion",n_oi.getText().toString());
            in.putExtra("fecha_cita",fecha_cita);
            in.putExtra("hora_cita",fecha_cita);

            startActivity(in);
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
