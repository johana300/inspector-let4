package com.letchile.let.VehLiviano;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.LoginActivity;
import com.letchile.let.R;
import com.letchile.let.Servicios.ConexionInternet;

import org.json.JSONArray;
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

/**
 * Created by Mauro on 08/01/2018.
 */

public class InsPendientesActivity extends AppCompatActivity{

    DBprovider db;
    SwipeRefreshLayout refres;
    String usr = "";
    ProgressDialog pDialog = null;
    Boolean connec = false;
    JSONArray jsonar;
    JSONObject jsonbb, jsonEstado;
    TableLayout tl;
    TableRow tr;


    public InsPendientesActivity(){

        db=new DBprovider(this);
    }

    private int obtenerAnchoPixelesTexto(String texto)
    {
        Paint p = new Paint();
        Rect bounds = new Rect();
        p.setTextSize(50);

        p.getTextBounds(texto, 0, texto.length(), bounds);
        return bounds.width();
    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspecciones_pendientes);


        connec = new ConexionInternet(this).isConnectingToInternet();
        //Comprobar la conexión a internet para cargar las fotos
            usr = db.obtenerUsuario();
            new SendPostRequest().execute(usr.toString());


        //refresca el layout
        refres = (SwipeRefreshLayout)findViewById(R.id.swipeM);
        refres.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                startActivity(getIntent());

                refres.setRefreshing(false);
            }
        });

    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("¿Desea volver al inicio de sesión?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent in = new Intent(InsPendientesActivity.this,LoginActivity.class);
                startActivity(in);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }




    public class SendPostRequest extends AsyncTask<String, Integer, String> {

        protected void onPreExecute(){
            pDialog= new ProgressDialog(InsPendientesActivity.this);
            pDialog.setTitle("Datos LET");
            pDialog.setMessage("Descargando...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        protected String doInBackground(String... parametros) {
            publishProgress(0);
            try{
                if(connec)
                {
                    URL urll = new URL("https://www.autoagenda.cl/movil/cargamovil/cantidadComunasMovil");

                    HttpsURLConnection conns = (HttpsURLConnection)urll.openConnection();
                    conns.setReadTimeout(15000);
                    conns.setConnectTimeout(15000);
                    conns.setRequestMethod("POST");
                    conns.setDoInput(true);
                    conns.setDoOutput(true);

                    OutputStream osc = conns.getOutputStream();
                    BufferedWriter writerr = new BufferedWriter(new OutputStreamWriter(osc,"UTF-8"));
                    writerr.flush();
                    writerr.close();
                    osc.close();

                    int respon = conns.getResponseCode();
                    if(respon==HttpsURLConnection.HTTP_OK){
                        BufferedReader inne=new BufferedReader(new InputStreamReader(conns.getInputStream()));

                        StringBuffer sbb = new StringBuffer("");
                        String lnn = "";
                        while ((lnn=inne.readLine())!=null){
                            sbb.append(lnn);
                            break;
                        }
                        inne.close();
                        Integer total = db.listatotalComunasRegiones();
                        String json = sbb.toString();
                        try{
                            JSONObject obj = new JSONObject(json);
                            Integer numero = Integer.parseInt(obj.getString("MSJ"));
                            if(total.equals(numero)){
                                Log.e("Prueba","No se actualiza");
                            }else{
                                db.borrarComunas();
                                try{
                                    URL url2 = new URL("https://www.autoagenda.cl/movil/cargamovil/cargaComuna");
                                    HttpsURLConnection coni = (HttpsURLConnection) url2.openConnection();
                                    coni.setReadTimeout(15000 /* milliseconds */);
                                    coni.setConnectTimeout(15000 /* milliseconds */);
                                    coni.setRequestMethod("POST");
                                    coni.setDoInput(true);
                                    coni.setDoOutput(true);

                                    OutputStream osi = coni.getOutputStream();
                                    BufferedWriter writerrr = new BufferedWriter(new OutputStreamWriter(osi,"UTF-8"));

                                    writerrr.flush();
                                    writerrr.close();
                                    osi.close();

                                    int respone = coni.getResponseCode();
                                    if(respone == HttpsURLConnection.HTTP_OK){
                                        BufferedReader innn = new BufferedReader(new InputStreamReader(coni.getInputStream()));

                                        StringBuffer sbbb = new StringBuffer("");
                                        String linn = "";

                                        while ((linn=innn.readLine())!=null){
                                            sbbb.append(linn);
                                            break;
                                        }
                                        innn.close();

                                        String jsonn = sbbb.toString();
                                        try{
                                            JSONArray jsonjoha = new JSONArray(jsonn);
                                            if(!jsonjoha.isNull(0))
                                            {
                                                String result="";
                                                for(int i=0;i<jsonjoha.length();i++){
                                                    publishProgress(i+1);
                                                    JSONObject jsonnn = new JSONObject(jsonjoha.getString(i));
                                                    result = db.insertarComuna(jsonnn.getInt("id_region"),jsonnn.getString("region"),jsonnn.getString("comuna"));
                                                }
                                            }
                                        }catch (Exception e)
                                        {
                                            return e.getMessage();
                                        }
                                    }

                                }catch (Exception e){
                                    return  e.getMessage();
                                }
                            }
                        }catch (Exception e){
                            return e.getMessage();
                        }
                    }
                }
            }catch (Exception e){
                return e.getMessage();
            }



            String respuestaEstado = "";

            try {
                //pregunto sobre la conexión
                if (connec) {
                    String inspecciones[][] = db.listaInspecciones();
                    if (inspecciones.length > 0) {
                        for (int i = 0; i < inspecciones.length; i++) {
                            publishProgress(i+1);
                            URL url = new URL("https://www.autoagenda.cl/movil/cargamovil/estadoOI");
                            JSONObject postDataParams = new JSONObject();
                            postDataParams.put("usr", parametros[0]);
                            postDataParams.put("id_inspeccion", inspecciones[i][0]);
                            Log.e("Parametros", postDataParams.toString());

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setReadTimeout(15000);
                            conn.setConnectTimeout(15000);
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
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                                StringBuffer sb = new StringBuffer("");
                                String line = "";

                                while ((line = in.readLine()) != null) {
                                    sb.append(line);
                                    break;
                                }

                                in.close();

                                respuestaEstado = sb.toString();

                                jsonEstado = new JSONObject(respuestaEstado);
                                if(jsonEstado.getString("MSJ").equals("No"))
                                {
                                    db.borrarInspeccion(Integer.parseInt(inspecciones[i][0]));
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                return respuestaEstado = "Mensaje de error: " + e.getMessage();
            }



            try {
                if(connec) {
                    URL url = new URL("https://www.autoagenda.cl/movil/cargamovil/cargaInspecciones");

                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("usr_inspector", parametros[0]);
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
                        JSONArray jsonar = new JSONArray(json);
                        try {

                            //descarga inspecciones
                            if (!jsonar.isNull(0)) {
                                String result = "";
                                for (int i = 0; i < jsonar.length(); i++) {
                                    jsonbb = new JSONObject(jsonar.getString(i));
                                    db.borrarInspeccion(jsonbb.getInt("id_inspeccion"));
                                    result = db.insertaInspecciones(jsonbb.getInt("id_inspeccion"),jsonbb.getString("asegurado"),jsonbb.getString("paterno_asegurado"),
                                            jsonbb.getString("materno_asegurado"),jsonbb.getString("rut"),jsonbb.getString("comuna_asegurado"),jsonbb.getString("direccion_asegurado"),
                                            jsonbb.getInt("fono"),jsonbb.getString("marca"),jsonbb.getString("modelo"),jsonbb.getString("direccion_cita"),jsonbb.getString("fecha_cita"),
                                            jsonbb.getString("hora_cita"),jsonbb.getString("comuna_cita"),jsonbb.getString("comentario_cita"),jsonbb.getString("id_ramo"),
                                            jsonbb.getString("patente"),jsonbb.getString("cia"),jsonbb.getString("corredor"),jsonbb.getString("pac"));

                                    //falta hacer filtro para descargar inspeccion = poner estado
                                    if(result.equals("Ok"))
                                    {

                                        URL url2 = new URL("https://www.autoagenda.cl/movil/cargamovil/descargaInspeccionMovil");

                                        JSONObject postData = new JSONObject();
                                        postData.put("usr_inspector",parametros[0]);
                                        postData.put("id_inspeccion",Integer.toString(jsonbb.getInt("id_inspeccion")));
                                        Log.e("Parametros de descarga",postData.toString());

                                        HttpURLConnection cone = (HttpURLConnection)url2.openConnection();
                                        cone.setReadTimeout(15000);
                                        cone.setConnectTimeout(15000);
                                        cone.setRequestMethod("POST");
                                        cone.setDoInput(true);
                                        cone.setDoOutput(true);

                                        OutputStream oss = cone.getOutputStream();
                                        BufferedWriter writerr = new BufferedWriter(new OutputStreamWriter(oss,"UTF-8"));
                                        writerr.write(getPostDataString(postData));

                                        writerr.flush();
                                        writerr.close();
                                        oss.close();

                                        int respon = cone.getResponseCode();
                                        if(respon == HttpURLConnection.HTTP_OK){
                                            BufferedReader ini = new BufferedReader(new InputStreamReader(cone.getInputStream()));

                                            StringBuffer sbb = new StringBuffer("");
                                            String linee = "";

                                            while ((linee = ini.readLine())!=null){
                                                sbb.append(linee);
                                                break;
                                            }

                                            ini.close();

                                            String jsonDesc = sbb.toString();
                                            try{
                                                JSONObject jsonDesca = new JSONObject(jsonDesc);
                                                if(jsonDesca.getString("MSJ").equals("Ok")){
                                                    Log.e("LET", "No se borra de la lista");
                                                }else{
                                                    db.borrarInspeccion(jsonbb.getInt("id_inspeccion"));
                                                }
                                            }catch (Exception e) {
                                                Log.e("LET", "No se pudo convertir: \"" + json + "\"");
                                            }


                                        }
                                    }
                                }
                            } else {
                                return "No hay inspecciones";
                            }

                        } catch (Throwable t) {
                            Log.e("LET", "Could not parse malformed JSON: \"" + json + "\"");
                        }

                        return sb.toString();

                    } else {
                        return new String("false : " + responseCode);
                    }
                }
            } catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
            return respuestaEstado;


        }


        @Override
        protected void onProgressUpdate(Integer... progreso)
        {
            pDialog.setProgress(progreso[0]);
        }


        @Override
        protected void onPostExecute(String result) {

            pDialog.dismiss();

            tl=(TableLayout)InsPendientesActivity.this.findViewById(R.id.tableM);

            String inspecciones[][]=db.listaInspecciones();

            if(inspecciones.length<1)
            {
                Toast.makeText(InsPendientesActivity.this, "No tiene Inspecciones pendientes", Toast.LENGTH_LONG).show();
            }
            else{
                for(int i=0;i<inspecciones.length;i++)
                {
                    tr = new TableRow(InsPendientesActivity.this);


                    TableLayout.LayoutParams parametros =  new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
                    parametros.setMargins(60,21,21,21);
                    tr.setLayoutParams(parametros);

                    final TextView id_inspeccion = new TextView(InsPendientesActivity.this);
                    id_inspeccion.setText(inspecciones[i][0]);
                    id_inspeccion.setTextSize(15);
                    id_inspeccion.setHeight(100);

                    TextView fecha_cita = new TextView(InsPendientesActivity.this);
                    fecha_cita.setText(inspecciones[i][1]);
                    fecha_cita.setTextSize(12);


                    TextView hora = new TextView(InsPendientesActivity.this);
                    hora.setText(inspecciones[i][2]);
                    hora.setTextSize(12);


                    TextView patente = new TextView(InsPendientesActivity.this);
                    patente.setText(inspecciones[i][4]);
                    patente.setTextSize(13);

                    /*TextView Detalle = new TextView(InsPendientesActivity.this);
                    Detalle.setText("detalle");
                    Detalle.setTextSize(10);*/

                    Button Det = new Button(InsPendientesActivity.this);
                    Det.setText("Detalle");
                    Det.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(InsPendientesActivity.this,id_inspeccion.getText().toString(), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(InsPendientesActivity.this,detalleActivity.class);
                            i.putExtra("id_inspeccion",id_inspeccion.getText().toString());
                            startActivity(i);
                            /*Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            startActivityForResult(intent, 1);*/
                        }
                    });

                    tr.addView(id_inspeccion);
                    tr.addView(patente);
                    tr.addView(fecha_cita);
                    tr.addView(hora);
                    tr.addView(Det);
                    tl.addView(tr);
                }
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
