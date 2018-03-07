package com.letchile.let.VehPesado;

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
import com.letchile.let.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class DatosInspVpActivity extends AppCompatActivity {

    DBprovider db;

    EditText dirIns;
    EditText fechaInsp;
    EditText horaInsp;
    EditText entrevistado;
    Spinner tipoVehVp;
    /*FALTA CARGAR COMBOS DE REGION Y COMUNA*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_insp_vp);

        //direccion inspeccion
        dirIns = (EditText)findViewById(R.id.dirIns);
        dirIns.getText().toString();

        //fecha inspeccion
        fechaInsp = (EditText)findViewById(R.id.fechaInsp);
        fechaInsp.getText().toString();

        //hora inspeccion
        horaInsp = (EditText)findViewById(R.id.horaInsp);
        horaInsp.getText().toString();

        //entrevistado
        entrevistado = (EditText)findViewById(R.id.entrevistado);
        entrevistado.getText().toString();

        // cargar un combo tipo vehiculo
        /*tipoVehVp = (Spinner)findViewById(R.id.tipo_veh_vp);
        String[] arraytipo = getResources().getStringArray(R.array.tipoVehVp);
        final List<String> arraytipolist = Arrays.asList(arraytipo);
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource( this, R.array.tipoVehVp , android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoVehVp.setAdapter(spinner_adapter);
        tipoVehVp.setSelection(arraytipolist.lastIndexOf(db.accesorio(92,364).toString()));*/



        //Botón volver de sección
        final Button btnSigInspJg = (Button)findViewById(R.id.btnSigInspJg);
        btnSigInspJg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                try{


                    JSONObject valor93 = new JSONObject();
                    valor93.put("valor_id",733);
                    valor93.put("texto",dirIns.getText().toString());

                    JSONObject valor94 = new JSONObject();
                    valor94.put("valor_id",737);
                    valor94.put("texto",fechaInsp.getText().toString());

                    JSONObject valor95 = new JSONObject();
                    valor95.put("valor_id",738);
                    valor95.put("texto",horaInsp.getText().toString());

                    JSONObject valor96 = new JSONObject();
                    valor96.put("valor_id",755);
                    valor96.put("texto",entrevistado.getText().toString());

                    JSONObject valor97 = new JSONObject();
                    valor97.put("valor_id",364);
                    valor97.put("texto",tipoVehVp.getSelectedItem().toString());






                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(valor93);
                    jsonArray.put(valor94);
                    jsonArray.put(valor95);
                    jsonArray.put(valor96);
                    jsonArray.put(valor97);






                    JSONObject llenado;
                    if (!jsonArray.isNull(0)) {
                        for(int i=0;i<jsonArray.length();i++){
                            llenado = new JSONObject(jsonArray.getString(i));
                            db.insertarValor(92,llenado.getInt("valor_id"),llenado.getString("texto"));


                        }
                    }

                }catch (Exception e)
                {

                    Toast.makeText(DatosInspVpActivity.this, "", Toast.LENGTH_SHORT);
                }

                Intent intent = new Intent( DatosInspVpActivity.this, ObsVpActivity.class);
                startActivity(intent);
            }
        });


        //BOTON VOLVER A OI PENDIENTES

        //BOTON VOLVER A SECCIONES


    }
}
