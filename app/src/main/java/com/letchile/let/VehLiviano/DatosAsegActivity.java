package com.letchile.let.VehLiviano;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.Clases.PropiedadesTexto;
import com.letchile.let.Clases.Validaciones;
import com.letchile.let.InsPendientesActivity;
import com.letchile.let.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class DatosAsegActivity extends AppCompatActivity {

    DBprovider db;
    ProgressDialog pDialog;
    EditText asegurado, paternoAsegurado, maternoAsegurado,rut,direccion,fono,email,celular;
    String [][] datosInspeccion;
    JSONObject llenado;
    JSONArray arrayValor;
    Validaciones validaciones;
    String id_inspeccion;
    Spinner comboComuna;

    public DatosAsegActivity() {
        db = new DBprovider(this);validaciones=new Validaciones(this);    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_aseg);

        Bundle bundle = getIntent().getExtras();
        id_inspeccion=bundle.getString("id_inspeccion");


        //Setear datos
        asegurado = findViewById(R.id.nomJg);
        asegurado.setOnEditorActionListener(new PropiedadesTexto());
        asegurado.setText(db.accesorio(Integer.parseInt(id_inspeccion),2).toString());

        paternoAsegurado = findViewById(R.id.apellidoPaternoM);
        paternoAsegurado.setOnEditorActionListener(new PropiedadesTexto());
        paternoAsegurado.setText(db.accesorio(Integer.parseInt(id_inspeccion),3).toString());

        maternoAsegurado = findViewById(R.id.maternoAseguradoM);
        maternoAsegurado.setOnEditorActionListener(new PropiedadesTexto());
        maternoAsegurado.setText(db.accesorio(Integer.parseInt(id_inspeccion),4).toString());

        rut = findViewById(R.id.rutJg);
        rut.setOnEditorActionListener(new PropiedadesTexto());
        rut.setText(db.accesorio(Integer.parseInt(id_inspeccion),5).toString());

        direccion = findViewById(R.id.direccionM);
        direccion.setOnEditorActionListener(new PropiedadesTexto());
        direccion.setText(db.accesorio(Integer.parseInt(id_inspeccion),8).toString());


        fono = findViewById(R.id.fonoJg);
        fono.setOnEditorActionListener(new PropiedadesTexto());
        fono.setText(db.accesorio(Integer.parseInt(id_inspeccion),6).toString());

        celular = findViewById(R.id.celuJg);
        celular.setOnEditorActionListener(new PropiedadesTexto());
        celular.setText(db.accesorio(Integer.parseInt(id_inspeccion),533).toString());


        email = findViewById(R.id.mailJg);
        email.setOnEditorActionListener(new PropiedadesTexto());
        email.setText(db.accesorio(Integer.parseInt(id_inspeccion),532).toString());


        //db.accesorio(Integer.parseInt(id_inspeccion),7);
        //llenar regiones
        String regionInicial=db.obtenerRegion(db.accesorio(Integer.parseInt(id_inspeccion),7).toString());
        String listaRegiones[][]=db.listaRegiones();
        final Spinner comboRegion = findViewById(R.id.comboRegJg);
        String[] arraySpinner = new String[listaRegiones.length+1];
        arraySpinner[0]=regionInicial;
        for(int i=0;i<listaRegiones.length;i++)        {
            arraySpinner[i+1]=listaRegiones[i][0];
        }
        ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboRegion.setAdapter(adapterRegion);


        comboComuna = findViewById(R.id.comboComJr);
        comboRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Rescata el nombre del item elegido
                String regionSelected = comboRegion.getSelectedItem().toString();
                //Rescata la lista según el item elegido
                String listaComunas[][] = db.listaComunas(regionSelected);
                //Inicia el nuevo combo a llenar

                //Se crea una variable array para ser llenado
                String[] spinnerComuna = new String[listaComunas.length+1];
                spinnerComuna[0] = db.accesorio(Integer.parseInt(id_inspeccion),7).toString();
                for(int i=0;i<listaComunas.length;i++){
                    spinnerComuna[i+1] = listaComunas[i][0];
                }
                ArrayAdapter<String> adapterComuna = new ArrayAdapter<String>(DatosAsegActivity.this,android.R.layout.simple_spinner_item,spinnerComuna);
                adapterComuna.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                comboComuna.setAdapter(adapterComuna);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        Button btnSigAsegJG = findViewById(R.id.btnSigAsegJg);
        btnSigAsegJG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatos();
                Intent intent = new Intent(DatosAsegActivity.this, DatosVehActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
                finish();
            }
        });

        final Button btnVolverAsegJg = findViewById(R.id.btnVolverAsegJg);
        btnVolverAsegJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatos();
                Intent intent = new Intent(DatosAsegActivity.this, seccion2.class);//cambiar por volver a fotos
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
                finish();
            }
        });


    }

    public void guardarDatos(){
        try {
            //LLENO EL JSON
            JSONObject datosValor1 = new JSONObject();
            datosValor1.put("valor_id",2);
            datosValor1.put("texto",asegurado.getText().toString());

            JSONObject datosValor2 = new JSONObject();
            datosValor2.put("valor_id",3);
            datosValor2.put("texto",paternoAsegurado.getText().toString());

            JSONObject datosValor3 = new JSONObject();
            datosValor3.put("valor_id",4);
            datosValor3.put("texto",maternoAsegurado.getText().toString());

            JSONObject datosValor4 = new JSONObject();
            datosValor4.put("valor_id",5);
            datosValor4.put("texto",rut.getText().toString());

            JSONObject datosValor5 = new JSONObject();
            datosValor5.put("valor_id",8);
            datosValor5.put("texto",direccion.getText().toString());

            JSONObject datosValor6 = new JSONObject();
            datosValor6.put("valor_id",6);
            datosValor6.put("texto",fono.getText().toString());

            JSONObject datosValorx = new JSONObject();
            datosValorx.put("valor_id",533);
            datosValorx.put("texto",celular.getText().toString());

            JSONObject datosValor7 = new JSONObject();
            datosValor7.put("valor_id",532);
            datosValor7.put("texto",email.getText().toString());

            JSONObject datosValor8 = new JSONObject();
            datosValor8.put("valor_id",7);
            datosValor8.put("texto",comboComuna.getSelectedItem().toString());

            JSONArray jsonArray = new JSONArray();

            jsonArray.put(datosValor1);
            jsonArray.put(datosValor2);
            jsonArray.put(datosValor3);
            jsonArray.put(datosValor4);
            jsonArray.put(datosValor5);
            jsonArray.put(datosValor6);
            jsonArray.put(datosValorx);
            jsonArray.put(datosValor7);
            jsonArray.put(datosValor8);



            //PREGUNTO SI ES NULO PARA INSERTAR LOS DATOS
            if (!jsonArray.isNull(0)) {
                for(int i=0;i<jsonArray.length();i++){
                    llenado = new JSONObject(jsonArray.getString(i));
                    db.insertarValor(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto"));
                    //validaciones.insertarDatos(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto"));
                }
            }


        }catch (Exception e)
        {
            Toast.makeText(DatosAsegActivity.this,e.getMessage(),Toast.LENGTH_SHORT);
        }
    }

}



