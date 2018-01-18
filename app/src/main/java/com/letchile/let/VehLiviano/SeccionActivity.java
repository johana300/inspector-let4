package com.letchile.let.VehLiviano;

import android.app.Activity;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.letchile.let.R;


public class SeccionActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seccion);

        //Traspaso de datos
        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");

        //Botón Sección fotos


        //Botón Sección datos asegurado
        Button btnAsegJG = (Button)findViewById(R.id.btnAsegJg);
        btnAsegJG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeccionActivity.this, DatosAsegActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });

        //Botón Sección datos vehículo
        Button btnVehJG = (Button)findViewById(R.id.btnVehJg);
        btnVehJG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeccionActivity.this, DatosVehActivity.class);
                startActivity(intent);
            }
        });

        //Botón Sección Accesorios
        /*Button btnAccJG = (Button)findViewById(R.id.btnAcc);
        btnAccJG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeccionActivity.this, AccActivity.class);
                startActivity(intent);
            }
        });

        //Botón Sección Audio
        Button btnAudioJG = (Button)findViewById(R.id.btnAudioJg);
        btnAudioJG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeccionActivity.this, AudioActivity.class);
                startActivity(intent);
            }
        });

        //Botón Sección neumático
        Button btnNeumJG = (Button)findViewById(R.id.btnNeumJg);
        btnNeumJG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeccionActivity.this, NeuActivity.class);
                startActivity(intent);
            }
        });

        //Botón Sección Techo
        Button btnTechoJG = (Button)findViewById(R.id.btnTechoJg);
        btnTechoJG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeccionActivity.this, TechoActivity.class);
                startActivity(intent);
            }
        });

        //Botón Sección datos inspección
        Button btnInspJG = (Button)findViewById(R.id.btnInspJg);
        btnInspJG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeccionActivity.this, DatosInspActivity.class);
                startActivity(intent);
            }
        });

        //Botón Observaciones
        Button btnObsJG = (Button)findViewById(R.id.btnObsJg);
        btnObsJG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeccionActivity.this, ObsActivity.class);
                startActivity(intent);
            }
        });

        //Botón volver a pendientes OI
        Button btnTranJg = (Button)findViewById(R.id.btnTranJg);
        btnTranJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeccionActivity.this, ObsActivity.class); //ir a pagina despues de transmitir
                startActivity(intent);
            }
        });*/

        //Botón transmitir

    }




}
