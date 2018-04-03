package com.letchile.let.VehLiviano;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.InsPendientesActivity;
import com.letchile.let.R;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Servicios.TransferirInspeccion;
import com.letchile.let.VehLiviano.Fotos.Posterior;
import com.letchile.let.detalleActivity;

public class seccion2 extends AppCompatActivity {

    DBprovider db;
    Boolean connec = false;

    public seccion2(){
        db = new DBprovider(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seccion2);

        connec = new ConexionInternet(this).isConnectingToInternet();


        //Traspaso de datos
        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion = bundle.getString("id_inspeccion");


        //Botón Sección fotos
        Button btnFotos = (Button) findViewById(R.id.btnFotoJg);
        btnFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(seccion2.this, Posterior.class);
                intent.putExtra("id_inspeccion", id_inspeccion);
                startActivity(intent);
            }
        });


        //Botón Sección datos inspeccion
        Button btnDatosInspeccion = (Button) findViewById(R.id.btnInspJg);
        btnDatosInspeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(seccion2.this, DatosInspActivity.class);
                intent.putExtra("id_inspeccion", id_inspeccion);
                startActivity(intent);
            }
        });

        //Botón Sección datos asegurado
        Button btnAsegJG = (Button) findViewById(R.id.btnAsegJg);
        btnAsegJG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(seccion2.this, DatosAsegActivity.class);
                intent.putExtra("id_inspeccion", id_inspeccion);
                startActivity(intent);
            }
        });

        //Botón Sección datos vehículo
        Button btnVehJG = (Button) findViewById(R.id.btnVehJg);
        btnVehJG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(seccion2.this, DatosVehActivity.class);
                intent.putExtra("id_inspeccion", id_inspeccion);
                startActivity(intent);
            }
        });

        //Botón Sección accesorio vehículo
        Button btnaccesorios = (Button) findViewById(R.id.btnAcc);
        btnaccesorios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(seccion2.this, AccActivity.class);
                intent.putExtra("id_inspeccion", id_inspeccion);
                startActivity(intent);
            }
        });

        //Botón Sección audio vehículo
        Button btnAudio = (Button) findViewById(R.id.btnAudioJg);
        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(seccion2.this, AudioActivity.class);
                intent.putExtra("id_inspeccion", id_inspeccion);
                startActivity(intent);
            }
        });

        Button btnNeumaticos = (Button) findViewById(R.id.btnNeumJg);
        btnNeumaticos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(seccion2.this, NeuActivity.class);
                intent.putExtra("id_inspeccion", id_inspeccion);
                startActivity(intent);
            }
        });

        Button btnTecho = (Button) findViewById(R.id.btnTechoJg);
        btnTecho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(seccion2.this, TechoActivity.class);
                intent.putExtra("id_inspeccion", id_inspeccion);
                startActivity(intent);
            }
        });


        Button btnObs = (Button) findViewById(R.id.btnObsJg);
        btnObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(seccion2.this, ObsActivity.class);
                intent.putExtra("id_inspeccion", id_inspeccion);
                startActivity(intent);
            }
        });

        //Botón volver a Detalle
        Button btnVolverSecJg = (Button) findViewById(R.id.btnVolverSecJg);
        btnVolverSecJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(seccion2.this, detalleActivity.class);
                intent.putExtra("id_inspeccion", id_inspeccion);
                startActivity(intent);
            }
        });


        //VALIDAR QUE TODO LO OBLIGATORIO ESTÉ LISTO
        Button btnTransmitir = findViewById(R.id.btnTranJg);
        btnTransmitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //verificar nuevamente la conexión
                connec = new ConexionInternet(seccion2.this).isConnectingToInternet();
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(seccion2.this);
                builder.setCancelable(false);
                builder.setMessage(Html.fromHtml("¿Seguro que desea transmitir la inspeccion <b>N°OI: "+id_inspeccion+"</b>?."));

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int fotosTomadas = db.fotosObligatoriasTomadas(Integer.parseInt(id_inspeccion));

                        if(fotosTomadas>=18){

                            //cambiar inspeccion a estado para transmitir
                            db.cambiarEstadoInspeccion(Integer.parseInt(id_inspeccion),2);

                            if(connec) {
                                //comprobar que el servicio esté activo
                                if (!compruebaServicio(TransferirInspeccion.class)) {
                                    Intent servis = new Intent(seccion2.this, TransferirInspeccion.class);
                                    startService(servis);
                                }
                            }
                            Intent seccion = new Intent(seccion2.this, InsPendientesActivity.class);
                            startActivity(seccion);
                            finish();

                        }else{
                            Toast.makeText(seccion2.this,"Faltan fotos obligatorias por tomar",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Rechazar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(seccion2.this, "Inspección no transmitida", Toast.LENGTH_LONG).show();
                    }
                });
                android.app.AlertDialog alert = builder.create();
                alert.show();
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
