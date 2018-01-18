package com.letchile.let.VehLiviano;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.letchile.let.R;

public class DatosVehActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_veh);

        //botón guardar y siguiente de datos de vehículos
        final Button btnGuardar1JG = (Button)findViewById(R.id.btnSigVehJg);
        btnGuardar1JG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent intent = new Intent( DatosVehActivity.this, AccActivity.class);
                startActivity(intent);*/

            }
        });
        //Botón volver de sección datos de vehículos
        final Button btnVolverVehJg = (Button)findViewById(R.id.btnVolverVehJg);
        btnVolverVehJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent2 = new Intent( DatosVehActivity.this, DatosAsegActivity.class);
                startActivity(intent2);
            }
        });
        //botón volver a pendientes

        final Button btnPenVehJg = (Button)findViewById(R.id.btnPenVehJg);
        btnPenVehJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent3 = new Intent( DatosVehActivity.this, SeccionActivity.class);//cambiar a oi pendientes
                startActivity(intent3);
            }
        });
    }
}
