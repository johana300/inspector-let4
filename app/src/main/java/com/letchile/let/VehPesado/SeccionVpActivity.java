package com.letchile.let.VehPesado;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.InsPendientesActivity;
import com.letchile.let.R;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Servicios.TransferirInspeccion;

import java.util.Arrays;
import java.util.List;

public class SeccionVpActivity extends AppCompatActivity {

    String vehPesado;
    Spinner tipoVehPesado;
    DBprovider db;
    Boolean connec = false;


    public SeccionVpActivity() {
        db = new DBprovider(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seccion_vp);

        connec = new ConexionInternet(this).isConnectingToInternet();

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion = bundle.getString("id_inspeccion");

        tipoVehPesado=(Spinner) findViewById(R.id.tipo_veh_vp);
        String[] arraytipo= getResources().getStringArray(R.array.veh_pesado);
        final List<String> arraytipolist = Arrays.asList(arraytipo);
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arraytipolist);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoVehPesado.setAdapter(spinner_adapter);


        //Botón foto
        // vehPesado=tipoVehPesado.getSelectedItem().toString();


        //Botón Datos Asegurado
        Button btnAsegVpJg =(Button) findViewById(R.id.btnAsegVpJg);
        btnAsegVpJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeccionVpActivity.this, DatosAsegVpActivity.class);
                intent.putExtra("id_inspeccion", id_inspeccion);
                startActivity(intent);
            }
        });


        //Botón Datos inspección
        Button btnVehVpJg =(Button) findViewById(R.id.btnVehVpJg);
        btnVehVpJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeccionVpActivity.this, DatosInspVpActivity.class);
                intent.putExtra("id_inspeccion", id_inspeccion);
                startActivity(intent);
            }
        });


        //Botón Observaciones//
        Button btnObsVpJg =(Button) findViewById(R.id.btnObsVpJg);
        btnObsVpJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeccionVpActivity.this, ObsVpActivity.class);
                intent.putExtra("id_inspeccion", id_inspeccion);
                startActivity(intent);
            }
        });


        //Botón Sección datos asegurado
        Button btnVolverSecVpJg = (Button) findViewById(R.id.btnVolverSecVpJg);
        btnVolverSecVpJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SeccionVpActivity.this, InsPendientesActivity.class);
                startActivity(intent);

            }
        });


        //Boton Transmitir OI
        Button btnTranVpJg = findViewById(R.id.btnTranVpJg);
        btnTranVpJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //verificar nuevamente la conexión
                connec = new ConexionInternet(SeccionVpActivity.this).isConnectingToInternet();

                //CUANDO SE TOMA LA PRIMERA FOTO EN LA SECCION POSTERIOR LA INSPECCIÓN ESTÁ EN ESTADO INICIADA

                //2,3,62,63,61,30,31,22,9,39,40,41,42,13,14,65,69,72

                int fotosTomadas = db.fotosObligatoriasTomadas(Integer.parseInt(id_inspeccion));

                if(fotosTomadas==18){

                    //cambiar inspeccion a estado para transmitir
                    db.cambiarEstadoInspeccion(Integer.parseInt(id_inspeccion),2);

                    if(connec) {
                        //comprobar que el servicio esté activo
                        if (!compruebaServicio(TransferirInspeccion.class)) {
                            Intent servis = new Intent(SeccionVpActivity.this, TransferirInspeccion.class);
                            startService(servis);
                        }
                    }


                    Intent seccion = new Intent(SeccionVpActivity.this, InsPendientesActivity.class);
                    startActivity(seccion);

                }else{
                    Toast.makeText(SeccionVpActivity.this,"Faltan fotos obligatorias por tomar",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean compruebaServicio(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
