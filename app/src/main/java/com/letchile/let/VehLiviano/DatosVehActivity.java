package com.letchile.let.VehLiviano;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatosVehActivity extends AppCompatActivity {

    Spinner tipo_veh,uso_veh,transmision;
    EditText patente,marca,modelo,subModelo,color,anio,nPuertas,nMotor,nChasis;
    CheckBox bencina,Diesel,gasLicuado,electrico,placaAdulterada,cuatroxcuatro,iimportDirec;
    JSONObject llenado;
    DBprovider db;

    //variables con una s alfinal para diferenciar de los checkbox
    String bencinas="",Diesels="",gasLicuados="",electricos="",placaAdulteradas="",cuatroxcuatros="",iimportDirecs="";

    public DatosVehActivity(){
        db = new DBprovider(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_veh);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");


        //Spinner
        //inicializo el spinner
        tipo_veh = (Spinner)findViewById(R.id.spinner_tipo_veh);
        //traigo el array de datos
        String[] arraytipo = getResources().getStringArray(R.array.tipo_veh);
        //creo una lista para insertar todos los datos
        final List<String> arraytipolist = Arrays.asList(arraytipo);
        //creo el adapter para llenar el spinner con la lista
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arraytipolist);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //se inserta el adapter
        tipo_veh.setAdapter(spinner_adapter);
        //se selecciona el que está guardado en la base de datos
        tipo_veh.setSelection(arraytipolist.indexOf(db.accesorio(Integer.parseInt(id_inspeccion),18).toString()));



        uso_veh = (Spinner)findViewById(R.id.spinner_uso_veh);
        String[] arrayuso = getResources().getStringArray(R.array.uso_veh);
        final List<String> arrayusolist = Arrays.asList(arrayuso);
        ArrayAdapter<String> spinner_adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayuso);
        spinner_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        uso_veh.setAdapter(spinner_adapter2);
        uso_veh.setSelection(arrayusolist.indexOf(db.accesorio(Integer.parseInt(id_inspeccion),20).toString()));

        transmision = (Spinner)findViewById(R.id.spinner_trans);
        String[] arrayTrans = getResources().getStringArray(R.array.trans);
        final  List<String> arrayTransList = Arrays.asList(arrayTrans);
        ArrayAdapter<String> spinner_adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayTransList);
        spinner_adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transmision.setAdapter(spinner_adapter3);
        transmision.setSelection(arrayTransList.indexOf(db.accesorio(Integer.parseInt(id_inspeccion),550).toString()));

        //Edit Text
        patente = (EditText)findViewById(R.id.patenteM);
        patente.setText(db.accesorio(Integer.parseInt(id_inspeccion),363).toString());
        marca = (EditText)findViewById(R.id.marcaM);
        marca.setText(db.accesorio(Integer.parseInt(id_inspeccion),10).toString());
        modelo =(EditText)findViewById(R.id.modeloM);
        modelo.setText(db.accesorio(Integer.parseInt(id_inspeccion),11).toString());
        subModelo = (EditText)findViewById(R.id.subModelo);
        subModelo.setText(db.accesorio(Integer.parseInt(id_inspeccion),12).toString());
        color = (EditText)findViewById(R.id.colorM);
        color.setText(db.accesorio(Integer.parseInt(id_inspeccion),14).toString());
        anio = (EditText)findViewById(R.id.AnioM);
        anio.setText(db.accesorio(Integer.parseInt(id_inspeccion),13).toString());
        nPuertas = (EditText)findViewById(R.id.nPuertasM);
        nPuertas.setText(db.accesorio(Integer.parseInt(id_inspeccion),15).toString());
        nMotor = (EditText)findViewById(R.id.nMotorM);
        nMotor.setText(db.accesorio(Integer.parseInt(id_inspeccion),16).toString());
        nChasis = (EditText)findViewById(R.id.nChasisM);
        nChasis.setText(db.accesorio(Integer.parseInt(id_inspeccion),17).toString());




        //Checkbox 1
        bencina = (CheckBox)findViewById(R.id.checkBencina);
        //db.accesorio(Integer.parseInt(id_inspeccion),385).toString();//(()).equals("Ok");
        if((db.accesorio(Integer.parseInt(id_inspeccion),385).toString()).equals("Ok"))
        {
            bencina.setChecked(true);
            bencinas = "Ok";
        }else{
            bencina.setChecked(false);
            bencinas = "";
        }
        bencina.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean estaCheck) {
                if(estaCheck){

                    bencinas = "Ok";

                    Diesel.setChecked(false);
                    gasLicuado.setChecked(false);
                    electrico.setChecked(false);
                }else{
                    bencinas = "";
                }
            }
        });


        Diesel = (CheckBox)findViewById(R.id.checkDiesel);
        if((db.accesorio(Integer.parseInt(id_inspeccion),21)).equals("Ok"))
        {
            Diesel.setChecked(true);
            Diesels = "Ok";
        }else{
            Diesel.setChecked(false);
            Diesels = "";
        }
        Diesel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean estaCheck) {
                if(estaCheck){

                    Diesels = "Ok";

                    bencina.setChecked(false);
                    gasLicuado.setChecked(false);
                    electrico.setChecked(false);
                }else{
                    Diesels = "";
                }
            }
        });

        gasLicuado = (CheckBox)findViewById(R.id.checkGasL);
        if((db.accesorio(Integer.parseInt(id_inspeccion),551)).equals("Ok"))
        {
            gasLicuado.setChecked(true);
            gasLicuados = "Ok";
        }else{
            gasLicuado.setChecked(false);
            gasLicuados = "";
        }
        gasLicuado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean estaCheck) {
                if(estaCheck){

                    gasLicuados = "Ok";

                    bencina.setChecked(false);
                    Diesel.setChecked(false);
                    electrico.setChecked(false);
                }else{

                    gasLicuados = "";

                }
            }
        });

        electrico = (CheckBox)findViewById(R.id.checkEl);
        if((db.accesorio(Integer.parseInt(id_inspeccion),549)).equals("Ok"))
        {
            electrico.setChecked(true);
            electricos = "Ok";
        }else{
            electrico.setChecked(false);
            electricos = "";
        }
        electrico.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean estaCheck) {
                if(estaCheck){

                    electricos = "Ok";

                    bencina.setChecked(false);
                    Diesel.setChecked(false);
                    gasLicuado.setChecked(false);
                }else{
                    electricos = "";
                }
            }
        });


        //Checkbox 2
        placaAdulterada = (CheckBox)findViewById(R.id.checkPlaca);
        if((db.accesorio(Integer.parseInt(id_inspeccion),790)).equals("Ok"))
        {
            placaAdulterada.setChecked(true);
            placaAdulteradas="Ok";
        }else{
            placaAdulterada.setChecked(false);
            placaAdulteradas="";
        }
        placaAdulterada.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean estaCheck) {
                if(estaCheck){
                    placaAdulteradas="Ok";
                }else{
                    placaAdulteradas="";
                }
            }
        });

        cuatroxcuatro = (CheckBox)findViewById(R.id.check4x4);
        if((db.accesorio(Integer.parseInt(id_inspeccion),19)).equals("Ok"))
        {
            cuatroxcuatro.setChecked(true);
            cuatroxcuatros = "Ok";
        }else{
            cuatroxcuatro.setChecked(false);
            cuatroxcuatros = "";
        }
        cuatroxcuatro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean estaCheck) {
                if(estaCheck){
                    cuatroxcuatros = "Ok";
                }else{
                    cuatroxcuatros = "";
                }
            }
        });

        iimportDirec = (CheckBox)findViewById(R.id.checkImpDir);
        if((db.accesorio(Integer.parseInt(id_inspeccion),357)).equals("Ok"))
        {
            iimportDirec.setChecked(true);
            iimportDirecs = "Ok";
        }else{
            iimportDirec.setChecked(false);
            iimportDirecs = "";
        }
        iimportDirec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean estaCheck) {
                if(estaCheck){
                    iimportDirecs = "Ok";
                }else{
                    iimportDirecs = "";
                }
            }
        });



        //botón guardar y siguiente de datos de vehículos
        final Button btnGuardar1JG = (Button)findViewById(R.id.btnSigVehJg);
        btnGuardar1JG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    JSONObject datosvalor1 = new JSONObject();
                    datosvalor1.put("valor_id",18);
                    datosvalor1.put("texto",tipo_veh.getSelectedItem().toString());

                    JSONObject datosvalor2 = new JSONObject();
                    datosvalor2.put("valor_id",20);
                    datosvalor2.put("texto",uso_veh.getSelectedItem().toString());

                    JSONObject datosvalor3 = new JSONObject();
                    datosvalor3.put("valor_id",550);
                    datosvalor3.put("texto",transmision.getSelectedItem().toString());

                    JSONObject datosvalor4 = new JSONObject();
                    datosvalor4.put("valor_id",363);
                    datosvalor4.put("texto",patente.getText().toString());

                    JSONObject datosvalor5 = new JSONObject();
                    datosvalor5.put("valor_id",10);
                    datosvalor5.put("texto",marca.getText().toString());

                    JSONObject datosvalor6 = new JSONObject();
                    datosvalor6.put("valor_id",11);
                    datosvalor6.put("texto",modelo.getText().toString());

                    JSONObject datosvalor7 = new JSONObject();
                    datosvalor7.put("valor_id",12);
                    datosvalor7.put("texto",subModelo.getText().toString());

                    JSONObject datosvalor8 = new JSONObject();
                    datosvalor8.put("valor_id",14);
                    datosvalor8.put("texto",color.getText().toString());

                    JSONObject datosvalor9 = new JSONObject();
                    datosvalor9.put("valor_id",13);
                    datosvalor9.put("texto",anio.getText().toString());

                    JSONObject datosvalor10 = new JSONObject();
                    datosvalor10.put("valor_id",15);
                    datosvalor10.put("texto",nPuertas.getText().toString());

                    JSONObject datosvalor11 = new JSONObject();
                    datosvalor11.put("valor_id",16);
                    datosvalor11.put("texto",nMotor.getText().toString());

                    JSONObject datosvalor12 = new JSONObject();
                    datosvalor12.put("valor_id",17);
                    datosvalor12.put("texto",nChasis.getText().toString());

                    JSONObject datosvalor13 = new JSONObject();
                    datosvalor13.put("valor_id",385);
                    datosvalor13.put("texto",bencinas);

                    JSONObject datosvalor14 = new JSONObject();
                    datosvalor14.put("valor_id",21);
                    datosvalor14.put("texto",Diesels);

                    JSONObject datosvalor15 = new JSONObject();
                    datosvalor15.put("valor_id",551);
                    datosvalor15.put("texto",gasLicuados);

                    JSONObject datosvalor16 = new JSONObject();
                    datosvalor16.put("valor_id",549);
                    datosvalor16.put("texto",electricos);

                    JSONObject datosvalor17 = new JSONObject();
                    datosvalor17.put("valor_id",790);
                    datosvalor17.put("texto",placaAdulteradas);

                    JSONObject datosvalor18 = new JSONObject();
                    datosvalor18.put("valor_id",19);
                    datosvalor18.put("texto",cuatroxcuatros);

                    JSONObject datosvalor19 = new JSONObject();
                    datosvalor19.put("valor_id",357);
                    datosvalor19.put("texto",iimportDirecs);


                    JSONArray datosvalores = new JSONArray();
                    datosvalores.put(datosvalor1);
                    datosvalores.put(datosvalor2);
                    //datosvalores.put(datosvalor3);
                    datosvalores.put(datosvalor4);
                    datosvalores.put(datosvalor5);
                    datosvalores.put(datosvalor6);
                    datosvalores.put(datosvalor7);
                    datosvalores.put(datosvalor8);
                    datosvalores.put(datosvalor9);
                    datosvalores.put(datosvalor10);
                    datosvalores.put(datosvalor11);
                    datosvalores.put(datosvalor12);
                    datosvalores.put(datosvalor13);
                    datosvalores.put(datosvalor14);
                    datosvalores.put(datosvalor15);
                    datosvalores.put(datosvalor16);
                    datosvalores.put(datosvalor17);
                    datosvalores.put(datosvalor18);
                    datosvalores.put(datosvalor19);

                    if(!datosvalores.isNull(0)){
                        for(int i=0;i<datosvalores.length();i++){
                            llenado = new JSONObject(datosvalores.getString(i));
                            db.insertarValor(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto"));
                        }
                    }

                }catch (Exception e){
                    Toast.makeText(DatosVehActivity.this,"Error en los datos",Toast.LENGTH_SHORT);
                }

                /*codigo de validacion de datos obligatorios*/
                if (nChasis.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(DatosVehActivity.this, "Debe digitar chasis de vehículo.",
                            Toast.LENGTH_SHORT).show();

                }
                else if (anio.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(DatosVehActivity.this, "Debe digitar año de vehículo.",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent( DatosVehActivity.this, AccActivity.class);
                    intent.putExtra("id_inspeccion",id_inspeccion);
                    startActivity(intent);

                }
                /*fin*/
            }
        });


        final Button btnVolverSecc = (Button)findViewById(R.id.btnVolverVehJg);
        btnVolverSecc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( DatosVehActivity.this, SeccionActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });

    }
}
