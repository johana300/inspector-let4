package com.letchile.let.VehLiviano;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.R;

public class detalleActivity extends AppCompatActivity {

    DBprovider db;
    TextView n_oi,pantete,asegurado,direccion,comentario,fono,ramo;
    Button btnInspeccion, btnAddhito, btnVolver;

    public detalleActivity(){
        db = new DBprovider(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        final AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");

        String[][] datosInspeccion=db.BuscaDatosInspeccion(id_inspeccion);

        n_oi = (TextView)findViewById(R.id.n_oim);
        n_oi.setText(id_inspeccion);

        asegurado = (TextView)findViewById(R.id.aseguradoM);
        asegurado.setText(datosInspeccion[0][1]);

        pantete = (TextView)findViewById(R.id.patenteM);
        pantete.setText(datosInspeccion[0][6]);

        direccion = (TextView)findViewById(R.id.direccionM);
        direccion.setText(datosInspeccion[0][4]);

        comentario = (TextView)findViewById(R.id.comentarioM);
        comentario.setText(datosInspeccion[0][3]);

        fono = (TextView)findViewById(R.id.telefonoM);
        fono.setText(datosInspeccion[0][2]);

        ramo = (TextView)findViewById(R.id.tipoVehiculoM);
        if(datosInspeccion[0][7].toString().equals("vl1")) {
            ramo.setText("Vehículo liviano");
        }else if(datosInspeccion[0][7].toString().equals("vp1")){
            ramo.setText("Vehículo pesado");
        }else{
            ramo.setText("");
        }



        //Inspeccionar
        btnInspeccion = (Button)findViewById(R.id.btnInspeccionarM);
        btnInspeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(detalleActivity.this);
                builder.setCancelable(false);
                builder.setMessage("¿Desea realizar la inspeccion N°OI: "+id_inspeccion+"?, recuerde que una vez comenzada la inspección" +
                        "no podrá volver atrás.");

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(ramo.getText().toString().equals("Vehículo liviano")) {
                            Intent inn = new Intent(detalleActivity.this,SeccionActivity.class);
                            inn.putExtra("id_inspeccion",n_oi.getText().toString());
                            startActivity(inn);
                        }else if(ramo.getText().toString().equals("Vehículo pesado")){
                            Toast.makeText(detalleActivity.this, "Pantalla vehículo pesado", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                builder.setNegativeButton("Rechazar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(detalleActivity.this, "Inspección rechazada", Toast.LENGTH_LONG).show();
                    }
                });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        });

        //Agregar hito

        //Volver al layout anterior
        btnVolver = (Button)findViewById(R.id.btnVolverM);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(detalleActivity.this,InsPendientesActivity.class);
                startActivity(in);
            }
        });


    }
}
