package com.letchile.let.VehLiviano;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.InsPendientesActivity;
import com.letchile.let.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class ObsActivity extends AppCompatActivity {

    DBprovider db;

    EditText observacion1,observacion2,observacion3,observacion4;
    Spinner region,comuna;
    JSONObject llenado;

    public ObsActivity(){
        db = new DBprovider(ObsActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obs);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");



        //OBSERVACION 1
        observacion1 = (EditText)findViewById(R.id.observacion1);
        observacion1.setText(db.accesorio(Integer.parseInt(id_inspeccion),301).toString());

        //OBSERVACION 2
        observacion2 = (EditText)findViewById(R.id.observacion2);
        observacion2.setText(db.accesorio(Integer.parseInt(id_inspeccion),302).toString());

        //OBSERVACION 3
        observacion3 = (EditText)findViewById(R.id.observacion3);
        observacion3.setText(db.accesorio(Integer.parseInt(id_inspeccion),303).toString());

        //OBSERVACION 4
        observacion4 = (EditText)findViewById(R.id.observacion4);
        observacion4.setText(db.accesorio(Integer.parseInt(id_inspeccion),304).toString());


        //Botón volver de sección Observación
        final Button btnguardar = (Button)findViewById(R.id.btnSigObsJg);
        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    JSONObject valor101 = new JSONObject();
                    valor101.put("valor_id",301);
                    valor101.put("texto",observacion1.getText().toString());

                    JSONObject valor102 = new JSONObject();
                    valor102.put("valor_id",302);
                    valor102.put("texto",observacion2.getText().toString());

                    JSONObject valor103 = new JSONObject();
                    valor103.put("valor_id",303);
                    valor103.put("texto",observacion3.getText().toString());

                    JSONObject valor104 = new JSONObject();
                    valor104.put("valor_id",304);
                    valor104.put("texto",observacion4.getText().toString());

                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(valor101);
                    jsonArray.put(valor102);
                    jsonArray.put(valor103);
                    jsonArray.put(valor103);

                    if (!jsonArray.isNull(0)) {
                        for(int i=0;i<jsonArray.length();i++){
                            llenado = new JSONObject(jsonArray.getString(i));
                            db.insertarValor(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto"));
                        }
                    }

                }catch (Exception e)
                {

                    Toast.makeText(ObsActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                }

                Intent intent = new Intent( ObsActivity.this, SeccionActivity.class);//VOLVER A SECCIONES
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });




        //Botón volver a secciones
        final Button btnVolverObsJg = (Button)findViewById(R.id.btnVolverObsJg);
        btnVolverObsJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( ObsActivity.this, DatosInspActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });
    }
}
