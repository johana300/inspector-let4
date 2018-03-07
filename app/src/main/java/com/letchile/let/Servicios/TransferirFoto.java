package com.letchile.let.Servicios;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.R;

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
import java.util.Timer;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by LETCHILE on 20/02/2018.
 */

public class TransferirFoto extends Service {

    DBprovider db;
    String para1,para2,para3,para4;
    Boolean connec = false;
    public TransferirFoto(){
        db = new DBprovider(this);
    }


    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        super.onStartCommand(intent,flags,startId);

        String id_inspeccion = (String) intent.getExtras().get("id_inspeccion");
        String id_foto = (String) intent.getExtras().get("id_foto");
        //Trae datos de la foto
        String datosFotos[][] = db.DatosFotos(Integer.parseInt(id_inspeccion), Integer.parseInt(id_foto));
        para1 = datosFotos[0][0].toString();
        para2 = datosFotos[0][1].toString();
        para3 = datosFotos[0][2].toString();
        para4 = datosFotos[0][3].toString();

        connec = new ConexionInternet(this).isConnectingToInternet();
        if(connec) {
            Toast.makeText(this, "Transferencia iniciada", Toast.LENGTH_SHORT).show();

            transferirbackground transfer = new transferirbackground();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                transfer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, para1.toString(), para2.toString(), para3.toString(), para4.toString());
            else
                transfer.execute(para1.toString(), para2.toString(), para3.toString(), para4.toString());
        }else{

            //CAMBIAR A ESTADO POR TRANSMITIR
            db.cambiarEstadoFoto(Integer.parseInt(id_inspeccion),para2.toString(),para3.toString(),1);
            onDestroy();
        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /*String id_inspeccion = (String) inten.getExtras().get("id_inspeccion");
        String id_foto = (String) inten.getExtras().get("id_foto");*/
        //id_foto = (String) intent.getExtras().get("id_foto");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"Tranmisión finalizada",Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public class transferirbackground extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL("https://www.autoagenda.cl/movil/cargamovil/descargafotos64"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("id_inspeccion", strings[0]);
                postDataParams.put("nombre_foto", strings[1]);
                postDataParams.put("archivo", strings[3]);
                postDataParams.put("comentario", strings[2]);
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


                    //CAMBIAR A ESTADO TRANSMITIDA
                    try {
                        if (sb.toString().equals("Ok")){
                            db.cambiarEstadoFoto(Integer.parseInt(strings[0]),strings[1],strings[2],2);
                        }
                    }catch (Exception e){
                        Log.e("Error cambio de estado",e.getMessage());
                    }
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {

            Log.e("Resultado Transmisión",result);
            onDestroy();
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
