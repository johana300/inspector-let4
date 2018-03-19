package com.letchile.let.VehLiviano;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.Clases.Validaciones;
import com.letchile.let.InsPendientesActivity;
import com.letchile.let.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class AccActivity extends AppCompatActivity {

    DBprovider db;

    EditText kilometraje,alarma,cAirb,cupula;


    Spinner tipoRueda,ubiRueda,ubicAnti;


    JSONObject llenado;
    Validaciones validaciones;


    public AccActivity(){
        db = new DBprovider(this);
        validaciones = new Validaciones(this);
    }

     JSONObject obj = new JSONObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");

        //kilometraje
        kilometraje = (EditText)findViewById(R.id.Ekilometraje);
        kilometraje.setText(db.accesorio(Integer.parseInt(id_inspeccion),296).toString());

        //Marca Alarma
        alarma = (EditText)findViewById(R.id.EAlarma);
        alarma.setText(db.accesorio(Integer.parseInt(id_inspeccion),552).toString());

        //Cantidad de airbag  282--cantAirb
        cAirb = (EditText)findViewById(R.id.cantAirb);
        cAirb.setText(db.accesorio(Integer.parseInt(id_inspeccion),282).toString());

        //marca cúpula
        cupula = (EditText)findViewById(R.id.mCupula);
        cupula.setText(db.accesorio(Integer.parseInt(id_inspeccion),762).toString());


        // cargar un combo tipo rueda repuesto
        tipoRueda = (Spinner)findViewById(R.id.tipoRueda);
        String[] arraytipo = getResources().getStringArray(R.array.tipo_rueda);
        final List<String> arraytipolist = Arrays.asList(arraytipo);
        ArrayAdapter<String> spinner_adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arraytipolist);
        spinner_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoRueda.setAdapter(spinner_adapter1);
        tipoRueda.setSelection(arraytipolist.lastIndexOf(db.accesorio(Integer.parseInt(id_inspeccion),753).toString()));

        // cargar un combo ubicacion rueda repuesto
        ubiRueda = (Spinner)findViewById(R.id.ubiRueda);
        String[] arraytipo2 = getResources().getStringArray(R.array.ubicacion_rueda);
        final List<String> arraytipolist2 = Arrays.asList(arraytipo2);
        ArrayAdapter<String> spinner_adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arraytipolist2);
        spinner_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ubiRueda.setAdapter(spinner_adapter2);
        ubiRueda.setSelection(arraytipolist2.lastIndexOf(db.accesorio(Integer.parseInt(id_inspeccion),290).toString()));

        // cargar un combo ubicacion barra antivuelco
        ubicAnti = (Spinner)findViewById(R.id.ubicAnti);
        String[] arraytipo3 = getResources().getStringArray(R.array.ubic_anti);
        final List<String> arraytipolist3 = Arrays.asList(arraytipo3);
        ArrayAdapter<String> spinner_adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arraytipolist3);
        spinner_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ubicAnti.setAdapter(spinner_adapter3);
        ubicAnti.setSelection(arraytipolist3.lastIndexOf(db.accesorio(Integer.parseInt(id_inspeccion),312).toString()));


        //Botón guardar siguiente de sección accesorio
        final Button btnSigAccJg = (Button)findViewById(R.id.btnSigAccJg);
        btnSigAccJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{

                    //edit text
                    //kilometraje
                    JSONObject valor63 = new JSONObject();
                    valor63.put("valor_id",296);
                    valor63.put("texto",kilometraje.getText().toString());

                    //Marca alarma
                    JSONObject valor64 = new JSONObject();
                    valor64.put("valor_id",552);
                    valor64.put("texto",alarma.getText().toString());

                    //cantidad de airbag
                    JSONObject valor65 = new JSONObject();
                    valor65.put("valor_id",282);
                    valor65.put("texto",cAirb.getText().toString());

                    //marca cupula
                    JSONObject valor66 = new JSONObject();
                    valor66.put("valor_id",762);
                    valor66.put("texto",cupula.getText().toString());

                    JSONObject valor75 = new JSONObject();
                    valor75.put("valor_id",753);
                    valor75.put("texto",tipoRueda.getSelectedItem().toString());

                    JSONObject valor76 = new JSONObject();
                    valor76.put("valor_id",290);
                    valor76.put("texto",ubiRueda.getSelectedItem().toString());

                    JSONObject valor77 = new JSONObject();
                    valor77.put("valor_id",312);
                    valor77.put("texto",ubicAnti.getSelectedItem().toString());


                    //UNIR TODOS LOS ACCESORIOS EN UN JSONARRAY
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(valor63);
                    jsonArray.put(valor64);
                    jsonArray.put(valor65);
                    jsonArray.put(valor66);
                    jsonArray.put(valor75);
                    jsonArray.put(valor76);
                    jsonArray.put(valor77);


                    if (!jsonArray.isNull(0)) {
                        for(int i=0;i<jsonArray.length();i++){
                            llenado = new JSONObject(jsonArray.getString(i));
                            db.insertarValor(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto"));
                            //validaciones.insertarDatos(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto"));
                        }
                    }

                }catch (Exception e)
                {
                    Toast.makeText(AccActivity.this,e.getMessage(),Toast.LENGTH_SHORT);
                }


                Intent intent = new Intent( AccActivity.this, AudioActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });

        //Botón volver a Secciones
        final Button btnVolverAccJg = (Button)findViewById(R.id.btnVolverAccJg);
        btnVolverAccJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent( AccActivity.this, DatosVehActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });

        //Botón volver a pendiente
        final Button btnPenAccJg = (Button)findViewById(R.id.btnPenAccJg);
        btnPenAccJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( AccActivity.this, InsPendientesActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });



    }
}
