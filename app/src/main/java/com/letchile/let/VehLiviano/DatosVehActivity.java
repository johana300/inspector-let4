package com.letchile.let.VehLiviano;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.letchile.let.R;

public class DatosVehActivity extends AppCompatActivity {

    Spinner tipo_veh,uso_veh,transmision;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_veh);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");

        tipo_veh = (Spinner)findViewById(R.id.spinner_tipo_veh);


        //botón guardar y siguiente de datos de vehículos
        final Button btnGuardar1JG = (Button)findViewById(R.id.btnSigVehJg);
        btnGuardar1JG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent intent = new Intent( DatosVehActivity.this, AccActivity.class);
                startActivity(intent);*/
            }
        });

        final Button btnVolverVehJg = (Button)findViewById(R.id.btnVolverVehJg);
        btnVolverVehJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent2 = new Intent( DatosVehActivity.this, SeccionActivity.class);
                intent2.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent2);
            }
        });


        final Button btnPenVehJg = (Button)findViewById(R.id.btnPenVehJg);
        btnPenVehJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent3 = new Intent( DatosVehActivity.this, InsPendientesActivity.class);//cambiar a oi pendientes
                intent3.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent3);
            }
        });
    }
}
