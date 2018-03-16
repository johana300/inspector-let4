package com.letchile.let.VehPesado;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class ObsVpActivity extends AppCompatActivity {

    DBprovider db;

    EditText obs1;
    EditText obs2;
    EditText obs3;
    EditText obs4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obs_vp);

        //OBSERVACION 1
        obs1 = (EditText)findViewById(R.id.obs1);
        obs1.getText().toString();

        //OBSERVACION 2
        obs2 = (EditText)findViewById(R.id.obs2);
        obs2.getText().toString();

        //OBSERVACION 3
        obs3 = (EditText)findViewById(R.id.obs3);
        obs3.getText().toString();

        //BOTON GUARDAR Y SIGUIENTE
        final Button btnSigObsVpJg = (Button)findViewById(R.id.btnSigObsVpJg);
        btnSigObsVpJg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                try{


                    JSONObject valor98 = new JSONObject();
                    valor98.put("valor_id",730);
                    valor98.put("texto",obs1.getText().toString());

                    JSONObject valor99 = new JSONObject();
                    valor99.put("valor_id",731);
                    valor99.put("texto",obs2.getText().toString());

                    JSONObject valor100 = new JSONObject();
                    valor100.put("valor_id",732);
                    valor100.put("texto",obs3.getText().toString());



                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(valor98);
                    jsonArray.put(valor99);
                    jsonArray.put(valor100);







                    JSONObject llenado;
                    if (!jsonArray.isNull(0)) {
                        for(int i=0;i<jsonArray.length();i++){
                            llenado = new JSONObject(jsonArray.getString(i));
                            db.insertarValor(92,llenado.getInt("valor_id"),llenado.getString("texto"));


                        }
                    }

                }catch (Exception e)
                {

                    Toast.makeText(ObsVpActivity.this, "", Toast.LENGTH_SHORT);
                }

                Intent intent = new Intent( ObsVpActivity.this, ObsVpActivity.class);//IR A PAGINA SECCIONES
                startActivity(intent);
            }
        });



    }
}
