package com.letchile.let.Servicios;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;

import org.json.JSONArray;
import org.json.JSONException;
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
 * Created by LETCHILE on 01/03/2018.
 */

public class TransferirInspeccion extends Service {

    DBprovider db;
    int id_inspeccion = 0;
    Boolean conn = false;
    private NotificationManager notificationManager;
    private static final int ID_NOTIFICACION = 1234;

    public TransferirInspeccion(){
        db = new DBprovider(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //INSTANCIAR LA NOTIFICACION
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId) {
        super.onStartCommand(intent, flags, startId);
try {
    conn = new ConexionInternet(this).isConnectingToInternet();

    NotificationCompat.Builder builder = new NotificationCompat.Builder(
            getBaseContext())
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("LET CHILE SPA")
            .setContentText("Servicio de transferencia iniciada")
            .setWhen(System.currentTimeMillis());

    //lanzar notificacion de servicio activo
    notificationManager.notify(ID_NOTIFICACION, builder.build());
    if (conn) {

        transferirInspeccionCompleta tranf = new transferirInspeccionCompleta();

        //Consultar las inspecciones en estado dos y tres
        int insp_e2 = db.estadoInspecciones(2);
        int insp_e3 = db.estadoInspecciones(3);

        //preguntar por la primera inspeccion en estado 2 (por transmitir)
        if (insp_e2 > 0 && insp_e3 == 0) {

            //cambiar a estado para transmitir
            db.cambiarEstadoInspeccion(insp_e2, 3);

            JSONArray jsonArray = new JSONArray();
            String accesorios[][] = db.listaAccesoriosParaEnviar(insp_e2);
            for (int i = 0; i < accesorios.length; i++) {
                try {
                    JSONObject valores = new JSONObject();
                    valores.put("idcampo", accesorios[i][0]);
                    valores.put("valor", accesorios[i][1]);
                    jsonArray.put(valores);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //paso los datos a variables
            String variable1 = String.valueOf(insp_e2);
            String variable2 = jsonArray.toString();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                tranf.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, variable1, variable2);
            else
                tranf.execute(variable1, variable2);

        } else if (insp_e2 == 0 && insp_e3 == 0) {
            //si no hay transmisión faltante o en desarrollo cerrar servicio
            onDestroy();
        }
    } else {
        Toast.makeText(this, "No cuenta con internet para transmitir", Toast.LENGTH_SHORT).show();
        onDestroy();
    }
}catch (Exception e){
    Log.e("Error",e.getMessage());
}
        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.cancel(ID_NOTIFICACION);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //en la tarea asincronica se cambia la inspección a transmitida en caso de
    public class transferirInspeccionCompleta extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            //ENVIA DATOS DE DIGITACIÓN
            try{
                URL url = new URL("https://www.autoagenda.cl/movil/cargamovil/datosValor"); // here is your URL path
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("id_inspeccion", strings[0]);
                postDataParams.put("dataV", strings[1]);
                Log.e("Parametos a pasar", postDataParams.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

                Log.e("Status servicio", String.valueOf(responseCode));

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

                }else{
                    onDestroy(); // SIN INTERNET Y/O OTRO ERROR
                }

            }catch (Exception e){
                Log.e("Error",e.getMessage());
            }


            //ENVIA FOTOS
           String fotos[][] = db.ListaDatosFotos(Integer.parseInt(strings[0]));
            for(int i = 0;i<fotos.length;){
                try{
                    URL url = new URL("https://www.autoagenda.cl/movil/cargamovil/descargafotos64"); // here is your URL path

                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("id_inspeccion", fotos[i][0]);
                    postDataParams.put("nombre_foto", fotos[i][1]);
                    postDataParams.put("archivo", fotos[i][3]);
                    postDataParams.put("comentario", fotos[i][2]);
                    Log.e("Parametos a pasar", postDataParams.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

                    Log.e("Status servicio", String.valueOf(responseCode));

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


                        try {
                            if (sb.toString().equals("Ok")){
                                i++;
                                //eliminar
                                db.cambiarEstadoFoto(Integer.parseInt(strings[0]),fotos[i][1].toString(),fotos[i][2].toString(),2);
                            }
                        }catch (Exception e){
                            //sacar si esque se cae
                            //i++;
                            Log.e("Errror transmision",e.getMessage());
                        }
                    }else{
                        onDestroy(); // sin internet o otro error
                    }

                }catch (Exception e){
                    Log.e("Error",e.getMessage());
                }
            }




            //ingresa fecha inspeccion
            String[][] datosInspeccion=db.BuscaDatosInspeccion(strings[0]);
                try{
                    URL url = new URL("https://www.autoagenda.cl/movil/cargamovil/ingresoFechaInspeccion"); // here is your URL path

                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("id_inspeccion","");
                    postDataParams.put("fecha_inspeccion", "");
                    postDataParams.put("usr", "");
                    Log.e("Parametos a pasar", postDataParams.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

                    Log.e("Status servicio", String.valueOf(responseCode));

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



                    }else{
                        onDestroy(); // sin internet o otro error
                    }

                }catch (Exception e){
                    Log.e("Error",e.getMessage());
                }





            return strings[0];
        }

        @Override
        protected void onPostExecute(String result) {

            //rescata la inspeccion que se estaba transmitiendo
            id_inspeccion = Integer.parseInt(result);
            //la cambia a estado 4
            db.cambiarEstadoInspeccion(id_inspeccion,4);
            //la borra de la base de datos porsiacaso
            db.deleteInspeccion(id_inspeccion);

            new Handler().postDelayed(new Runnable() {
                    public void run() {

                        transferirInspeccionCompleta tranf = new transferirInspeccionCompleta();

                        //Consultar las inspecciones en estado dos y tres
                        int insp_e2 = db.estadoInspecciones(2);
                        int insp_e3 = db.estadoInspecciones(3);

                        //preguntar por la primera inspeccion en estado 2 (por transmitir)
                        if (insp_e2 > 0 && insp_e3 == 0) {

                            //cambiar a estado para transmitir
                            db.cambiarEstadoInspeccion(insp_e2, 3);

                            JSONArray jsonArray = new JSONArray();
                            String accesorios[][] = db.listaAccesoriosParaEnviar(insp_e2);
                            for (int i = 0; i < accesorios.length; i++) {
                                try {
                                    JSONObject valores = new JSONObject();
                                    valores.put("idcampo", accesorios[i][0]);
                                    valores.put("valor", accesorios[i][1]);
                                    jsonArray.put(valores);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            //paso los datos a variables
                            String variable1 = String.valueOf(insp_e2);
                            String variable2 = jsonArray.toString();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                tranf.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, variable1, variable2);
                            else
                                tranf.execute(variable1, variable2);

                        } else if (insp_e2 == 0 && insp_e3 == 0) {
                            //si no hay transmisión faltante o en desarrollo cerrar servicio
                            onDestroy();
                        }
                    }
                }, 30000);
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
}
