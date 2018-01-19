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
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });

    }




}
