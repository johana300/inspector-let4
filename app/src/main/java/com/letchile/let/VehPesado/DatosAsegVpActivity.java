/**
 * Created by Johana on 01/2018.
 */

package com.letchile.let.VehPesado;

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
import com.letchile.let.Clases.Validaciones;
import com.letchile.let.R;


import org.json.JSONArray;
import org.json.JSONObject;

public class DatosAsegVpActivity extends AppCompatActivity {

    DBprovider db;

    EditText nomVpJg,patVpJg,matVpJg,rutVpJg,dirJg,mailVpJg,fonoJg,celular;
    ProgressDialog pDialog;
    String [][] datosInspeccion;
    JSONObject llenadoV;
    JSONArray arrayValor;
    Validaciones validaciones;


    public DatosAsegVpActivity() {
        db = new DBprovider(this);validaciones=new Validaciones(this);    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_aseg_vp);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");

        //nombre
        nomVpJg = (EditText)findViewById(R.id.nomVpJg);
        nomVpJg.setText(db.accesorio(Integer.parseInt(id_inspeccion),365).toString());

        //Apellido paterno
        patVpJg = (EditText)findViewById(R.id.patVpJg);
        patVpJg.setText(db.accesorio(Integer.parseInt(id_inspeccion),366).toString());

        //Apellido materno
        matVpJg = (EditText)findViewById(R.id.matVpJg);
        matVpJg.setText(db.accesorio(Integer.parseInt(id_inspeccion),367).toString());

        //rut
        rutVpJg = (EditText)findViewById(R.id.rutVpJg);
        rutVpJg.setText(db.accesorio(Integer.parseInt(id_inspeccion),368).toString());

        //direccion
        dirJg = (EditText)findViewById(R.id.dirJg);
        dirJg.setText(db.accesorio(Integer.parseInt(id_inspeccion),371).toString());

        //fono
        fonoJg = (EditText)findViewById(R.id.fonoJg);
        fonoJg.setText(db.accesorio(Integer.parseInt(id_inspeccion),369).toString());

        //celular
        celular = (EditText)findViewById(R.id.celular);
        celular.setText(db.accesorio(Integer.parseInt(id_inspeccion),533).toString());

        //mail
        mailVpJg = (EditText)findViewById(R.id.mailVpJg);
        mailVpJg.setText(db.accesorio(Integer.parseInt(id_inspeccion),532).toString());


        String regionInicial[][]=db.obtenerRegion(db.accesorio(Integer.parseInt(id_inspeccion),370).toString());
        String listaRegiones[][]=db.listaRegiones();
        final Spinner comboRegion = (Spinner)findViewById(R.id.comboRegVpJg);
        String[] arraySpinner = new String[listaRegiones.length+1];
        arraySpinner[0]="Seleccione...";
        for(int i=0;i<listaRegiones.length;i++)        {
            arraySpinner[i+1]=listaRegiones[i][0];
        }
        ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboRegion.setAdapter(adapterRegion);


        final Spinner comboComuna = (Spinner)findViewById(R.id.comboComVpJg);
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
                spinnerComuna[0] = "Seleccione...";
                for(int i=0;i<listaComunas.length;i++){
                    spinnerComuna[i+1] = listaComunas[i][0];
                }
                ArrayAdapter<String> adapterComuna = new ArrayAdapter<String>(DatosAsegVpActivity.this,android.R.layout.simple_spinner_item,spinnerComuna);
                adapterComuna.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                comboComuna.setAdapter(adapterComuna);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //boton siguiente
        final Button btnSigAsegVpJg = (Button)findViewById(R.id.btnSigAsegVpJg);
        btnSigAsegVpJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try{


                    JSONObject valor85 = new JSONObject();
                    valor85.put("valor_id",365);
                    valor85.put("texto",nomVpJg.getText().toString());

                    JSONObject valor86 = new JSONObject();
                    valor86.put("valor_id",366);
                    valor86.put("texto",patVpJg.getText().toString());

                    JSONObject valor87 = new JSONObject();
                    valor87.put("valor_id",367);
                    valor87.put("texto",matVpJg.getText().toString());

                    JSONObject valor88 = new JSONObject();
                    valor88.put("valor_id",368);
                    valor88.put("texto",rutVpJg.getText().toString());

                    JSONObject valor89 = new JSONObject();
                    valor89.put("valor_id",371);
                    valor89.put("texto",dirJg.getText().toString());

                    JSONObject valor90 = new JSONObject();
                    valor90.put("valor_id",369);
                    valor90.put("texto",fonoJg.getText().toString());

                    JSONObject valor91 = new JSONObject();
                    valor91.put("valor_id",533);
                    valor91.put("texto",celular.getText().toString());


                    JSONObject valor92 = new JSONObject();
                    valor92.put("valor_id",532);
                    valor92.put("texto",mailVpJg.getText().toString());

                    JSONObject datosValorVpCo = new JSONObject();
                    datosValorVpCo.put("valor_id",370);
                    datosValorVpCo.put("texto",comboComuna.getSelectedItem().toString());




                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(valor85);
                    jsonArray.put(valor86);
                    jsonArray.put(valor87);
                    jsonArray.put(valor88);
                    jsonArray.put(valor89);
                    jsonArray.put(valor90);
                    jsonArray.put(valor91);
                    jsonArray.put(valor92);
                    jsonArray.put(datosValorVpCo);




                    JSONObject llenadoV;
                    if (!jsonArray.isNull(0)) {
                        for(int i=0;i<jsonArray.length();i++){
                            llenadoV = new JSONObject(jsonArray.getString(i));
                            db.insertarValor(Integer.parseInt(id_inspeccion),llenadoV.getInt("valor_id"),llenadoV.getString("texto"));


                        }
                    }

                }catch (Exception e)
                {

                    Toast.makeText(DatosAsegVpActivity.this, "", Toast.LENGTH_SHORT).show();
                }


                if(comboComuna.getSelectedItem().toString().equals("Seleccione...")){

                    Toast.makeText(DatosAsegVpActivity.this, "Debe seleccionar región y comuna.",Toast.LENGTH_SHORT).show();

                }
                else
                {

                    Intent intent = new Intent( DatosAsegVpActivity.this, DatosInspVpActivity.class);
                    intent.putExtra("id_inspeccion",id_inspeccion);
                    startActivity(intent);
                }


            }
        });


        //Botón volver de sección pesado
        final Button btnVolverAsegVpJg = (Button)findViewById(R.id.btnVolverAsegVpJg);
        btnVolverAsegVpJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( DatosAsegVpActivity.this, SeccionVpActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });




    }


}
