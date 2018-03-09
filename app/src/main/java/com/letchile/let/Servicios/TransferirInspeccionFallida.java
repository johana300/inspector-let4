package com.letchile.let.Servicios;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;

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
 * Created by LET-CHILE on 09-03-2018.
 */

public class TransferirInspeccionFallida extends Service {

    DBprovider db;
    int id_inspeccion = 0;
    boolean conn = false;
    private NotificationManager notificationManager;
    private static final int ID_NOTIFICACION = 4321;

    public TransferirInspeccionFallida(){
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

        conn = new ConexionInternet(this).isConnectingToInternet();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getBaseContext())
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("LET CHILE SPA")
                .setContentText("Servicio de transferencia fallida iniciada")
                .setWhen(System.currentTimeMillis());

        String id_inspeccion = (String) intent.getExtras().get("id_inspeccion");

        notificationManager.notify(ID_NOTIFICACION,builder.build());

        if(conn){

            transferirInsFallida transfer = new transferirInsFallida();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                transfer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,id_inspeccion);
            else
                transfer.execute(id_inspeccion);

        }else{
            Toast.makeText(this,"No cuenta con internet para transmitir",Toast.LENGTH_SHORT).show();
            onDestroy();
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

    public class transferirInsFallida extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            //ENVIA FOTOS
            String fotos[][] = db.ListaDatosFotosFallida(Integer.parseInt(strings[0]));
            for(int i = 0;i<fotos.length;){
                try{
                    URL url = new URL("https://www.autoagenda.cl/movil/cargamovil/descargaFotosFallida64"); // here is your URL path

                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("id_inspeccion", fotos[i][0]);
                    postDataParams.put("nombre_foto", fotos[i][1]);
                    postDataParams.put("archivo", fotos[i][3]);
                    postDataParams.put("comentario", fotos[i][2]);
                    postDataParams.put("fechaHoraFallida", fotos[i][4]);
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
                                db.cambiarEstadoFotoFallida(Integer.parseInt(strings[0]),strings[1],2);
                            }
                        }catch (Exception e){
                            Log.e("Errror transmision",e.getMessage());
                        }
                    }else{
                        onDestroy(); // sin internet o otro error
                    }

                }catch (Exception e){
                    Log.e("Error",e.getMessage());
                }
            }
            return strings[0];
        }

        @Override
        protected void onPostExecute(String result) {

            int id_inspeccion = Integer.parseInt(result);

            db.deleteInspeccionFallida(id_inspeccion);

            onDestroy();

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
