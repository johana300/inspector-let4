package com.letchile.let.VehPesado;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.letchile.let.R;

public class SeccionVpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seccion_vp);


        //Botón foto


        //Botón Sección datos asegurado
        Button btnAsegVpJg = (Button) findViewById(R.id.btnAsegVpJg);
        btnAsegVpJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SeccionVpActivity.this, DatosAsegVpActivity.class);
                startActivity(intent);

            }
        });

        //Boton datos de inspección
        Button btnVehVpJg = (Button)findViewById(R.id.btnVehVpJg);
        btnVehVpJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeccionVpActivity.this, DatosInspVpActivity.class);
                startActivity(intent);
            }
        });

        //Boton Observación
        Button btnObsVpJg = (Button)findViewById(R.id.btnObsVpJg);
        btnObsVpJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeccionVpActivity.this, ObsVpActivity.class);
                startActivity(intent);
            }
        });
    }
}
