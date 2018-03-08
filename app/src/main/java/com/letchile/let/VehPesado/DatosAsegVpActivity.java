/**
 * Created by Johana on 01/2018.
 */

package com.letchile.let.VehPesado;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

    EditText nomVpJg;
    EditText patVpJg;
    EditText matVpJg;
    EditText rutVpJg;
    EditText dirJg;
    EditText fonoJg;
    EditText celular;
    EditText mailVpJg;
    /*FALTA CARGAR LOS COMBO DE REGION Y COMUNA*/
    Spinner comboRegVpJg;
    Spinner comboComVpJg;
    Validaciones validaciones;




    public DatosAsegVpActivity() {
        db = new DBprovider(this);validaciones=new Validaciones(this);    }

    JSONObject obj = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_aseg_vp);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");

        //nombre
        nomVpJg = (EditText)findViewById(R.id.nomVpJg);
        nomVpJg.getText().toString();

        //Apellido paterno
        patVpJg = (EditText)findViewById(R.id.patVpJg);
        patVpJg.getText().toString();

        //Apellido materno
        matVpJg = (EditText)findViewById(R.id.matVpJg);
        matVpJg.getText().toString();

        //rut
        rutVpJg = (EditText)findViewById(R.id.rutVpJg);
        rutVpJg.getText().toString();

        //direccion
        dirJg = (EditText)findViewById(R.id.dirJg);
        dirJg.getText().toString();

        //fono
        fonoJg = (EditText)findViewById(R.id.fonoJg);
        fonoJg.getText().toString();

        //celular
        celular = (EditText)findViewById(R.id.celular);
        celular.getText().toString();

        //mail
        mailVpJg = (EditText)findViewById(R.id.mailVpJg);
        mailVpJg.getText().toString();


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




                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(valor85);
                    jsonArray.put(valor86);
                    jsonArray.put(valor87);
                    jsonArray.put(valor88);
                    jsonArray.put(valor89);
                    jsonArray.put(valor90);
                    jsonArray.put(valor91);
                    jsonArray.put(valor92);




                    JSONObject llenado;
                    if (!jsonArray.isNull(0)) {
                        for(int i=0;i<jsonArray.length();i++){
                            llenado = new JSONObject(jsonArray.getString(i));
                            db.insertarValor(92,llenado.getInt("valor_id"),llenado.getString("texto"));


                        }
                    }

                }catch (Exception e)
                {

                    Toast.makeText(DatosAsegVpActivity.this, "", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent( DatosAsegVpActivity.this, DatosInspVpActivity.class);
                startActivity(intent);
            }
        });


        //Botón volver de sección pesado
        final Button btnVolverAsegVpJg = (Button)findViewById(R.id.btnVolverAsegVpJg);
        btnVolverAsegVpJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( DatosAsegVpActivity.this, SeccionVpActivity.class);
                startActivity(intent);
            }
        });


        /*BOTON VOLVER A OI PENDIENTES*/




    }


}
