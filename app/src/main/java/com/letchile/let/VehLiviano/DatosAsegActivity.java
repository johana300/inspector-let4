package com.letchile.let.VehLiviano;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.R;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Iterator;

public class DatosAsegActivity extends AppCompatActivity {

    DBprovider db;
    ProgressDialog pDialog;
    EditText asegurado, paternoAsegurado, maternoAsegurado,rut,direccion,fono,email;
    String [][] datosInspeccion;

    public DatosAsegActivity() {
        db = new DBprovider(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_aseg);

        new SendPostRequest().execute();

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");

        datosInspeccion = db.BuscaDatosInspeccion(id_inspeccion);

        //Setear datos
        asegurado = (EditText)findViewById(R.id.nomJg);
        asegurado.setText(datosInspeccion[0][1]);

        paternoAsegurado = (EditText)findViewById(R.id.apellidoPaternoM);
        paternoAsegurado.setText(datosInspeccion[0][8]);

        maternoAsegurado = (EditText)findViewById(R.id.maternoAseguradoM);
        maternoAsegurado.setText(datosInspeccion[0][9]);

        rut = (EditText)findViewById(R.id.rutJg);
        rut.setText(datosInspeccion[0][10]);

        fono = (EditText)findViewById(R.id.fonoJg);
        fono.setText(datosInspeccion[0][2]);

        direccion = (EditText)findViewById(R.id.direccionM);
        direccion.setText(datosInspeccion[0][4]);

        email = (EditText)findViewById(R.id.mailJg);
        email.setText(datosInspeccion[0][11]);



        //llenar spinner

        String primeraComuna = datosInspeccion[0][5];
        Spinner comboComuna = (Spinner)findViewById(R.id.comboComJr);
        ArrayAdapter<String> adapterComuna = new ArrayAdapter<String>(DatosAsegActivity.this,android.R.layout.simple_spinner_item);
        adapterComuna.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboComuna.setAdapter(adapterComuna);
        if(primeraComuna!=null)
        {
            int posicion = adapterComuna.getPosition(primeraComuna);
            comboComuna.setSelection(posicion);
        }

        //llenar regiones
        /*String listaRegiones[][]=db.listaRegiones();
        final Spinner comboRegion = (Spinner)findViewById(R.id.comboRegJg);
        String[] arraySpinner = new String[listaRegiones.length];
        arraySpinner[0]="Seleccione...";
        for(int i=1;i<listaRegiones.length;i++)        {
            arraySpinner[i]=listaRegiones[i][0];
        }
        ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboRegion.setAdapter(adapterRegion);


        comboRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Rescata el nombre del item elegido
                String regionSelected = comboRegion.getSelectedItem().toString();
                //Rescata la lista según el item elegido
                String listaComunas[][] = db.listaComunas(regionSelected);
                //Inicia el nuevo combo a llenar
                Spinner comboComuna = (Spinner)findViewById(R.id.comboComJr);
                //Se crea una variable array para ser llenado
                String[] spinnerComuna = new String[listaComunas.length];
                for(int i=0;i<listaComunas.length;i++){
                    spinnerComuna[i] = listaComunas[i][0];
                }
                ArrayAdapter<String> adapterComuna = new ArrayAdapter<String>(DatosAsegActivity.this,android.R.layout.simple_spinner_item,spinnerComuna);
                adapterComuna.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                comboComuna.setAdapter(adapterComuna);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/






        final Button btnSigAsegJG = (Button) findViewById(R.id.btnSigAsegJg);
        btnSigAsegJG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DatosAsegActivity.this, DatosVehActivity.class);
                startActivity(intent);
            }
        });

        final Button btnVolverAsegJg = (Button) findViewById(R.id.btnVolverAsegJg);
        btnVolverAsegJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DatosAsegActivity.this, SeccionActivity.class);//cambiar por volver a fotos
                startActivity(intent);
            }
        });

        final Button btnPenAsegJg = (Button) findViewById(R.id.btnPenAsegJg);
        btnPenAsegJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DatosAsegActivity.this, SeccionActivity.class);
                startActivity(intent);
            }
        });
    }


    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            /*pDialog = new ProgressDialog(DatosAsegActivity.this);
            pDialog.setMessage("Autenticando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();*/

        }

        protected String doInBackground(String... parametros) {

           return "nada";

        }

        @Override
        protected void onPostExecute(String result) {


        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
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



