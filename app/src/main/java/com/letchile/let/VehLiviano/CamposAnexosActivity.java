package com.letchile.let.VehLiviano;

import com.letchile.let.BD.DBprovider;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.letchile.let.InsPendientesActivity;
import com.letchile.let.R;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Clases.PropiedadesTexto;
import com.letchile.let.Clases.Validaciones;
import com.letchile.let.Servicios.TransferirInspeccion;
import com.letchile.let.VehLiviano.Fotos.Posterior;
import com.letchile.let.detalleActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CamposAnexosActivity extends AppCompatActivity {

    DBprovider db;
    Boolean connec = false;
    String id_inspeccion;
    RadioButton rs1, rs2, rs3, rs4, rs5, rs6, rs7, rs8, rn1, rn2, rn3, rn4, rn5, rn6, rn7, rn8, rn9;
    RadioGroup grupo1,grupo2,grupo3,grupo4,grupo5,grupo6,grupo7,grupo8,grupo9;
    JSONObject llenado;



    public CamposAnexosActivity() { db = new DBprovider(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campos_anexos);
        ButterKnife.bind(this);
        connec = new ConexionInternet(this).isConnectingToInternet();

        grupo1=(RadioGroup) findViewById(R.id.grupo1);
        grupo2=(RadioGroup) findViewById(R.id.grupo2);
        grupo3=(RadioGroup) findViewById(R.id.grupo3);
        grupo4=(RadioGroup) findViewById(R.id.grupo4);
        grupo5=(RadioGroup) findViewById(R.id.grupo5);
        grupo6=(RadioGroup) findViewById(R.id.grupo6);
        grupo7=(RadioGroup) findViewById(R.id.grupo7);
        grupo8=(RadioGroup) findViewById(R.id.grupo8);
        grupo9=(RadioGroup) findViewById(R.id.grupo9);


        rs2=(RadioButton)findViewById(R.id.rs2);
        rs3=(RadioButton)findViewById(R.id.rs3);
        rs4=(RadioButton)findViewById(R.id.rs4);
        rs5=(RadioButton)findViewById(R.id.rs5);
        rs6=(RadioButton)findViewById(R.id.rs6);
        rs7=(RadioButton)findViewById(R.id.rs7);
        rs8=(RadioButton)findViewById(R.id.rs8);
        rs8=(RadioButton)findViewById(R.id.rs9);
        rn1=(RadioButton)findViewById(R.id.rn1);
        rn2=(RadioButton)findViewById(R.id.rn2);
        rn3=(RadioButton)findViewById(R.id.rn3);
        rn4=(RadioButton)findViewById(R.id.rn4);
        rn5=(RadioButton)findViewById(R.id.rn5);
        rn6=(RadioButton)findViewById(R.id.rn6);
        rn7=(RadioButton)findViewById(R.id.rn7);
        rn8=(RadioButton)findViewById(R.id.rn8);
        rn9=(RadioButton)findViewById(R.id.rn9);





        Bundle bundle = getIntent().getExtras();
        id_inspeccion = bundle.getString("id_inspeccion");


        //VALIDAR QUETODO LO OBLIGATORIO ESTÉ LISTO
        //ir a clase
        Button btnTransmitir = findViewById(R.id.btnFinAnexos);
        btnTransmitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //verificar nuevamente la conexión
                connec = new ConexionInternet(CamposAnexosActivity.this).isConnectingToInternet();
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CamposAnexosActivity.this);
                builder.setCancelable(false);
                builder.setMessage(Html.fromHtml("¿Seguro que desea transmitir la inspeccion <b>N°OI: " + id_inspeccion + "</b>?."));

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                        if (grupo1.getCheckedRadioButtonId() == -1) {

                            Toast.makeText(CamposAnexosActivity.this, "Debe seleccionar una opción en todos los anexos. " , Toast.LENGTH_LONG).show();
                        } else if (grupo2.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(CamposAnexosActivity.this, "Debe seleccionar una opción en todos los anexos. ", Toast.LENGTH_LONG).show();

                        } else if (grupo3.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(CamposAnexosActivity.this, "Debe seleccionar una opción en todos los anexos. ", Toast.LENGTH_LONG).show();

                        } else if (grupo4.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(CamposAnexosActivity.this, "Debe seleccionar una opción en todos los anexos. ", Toast.LENGTH_LONG).show();

                        } else if (grupo5.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(CamposAnexosActivity.this, "Debe seleccionar una opción en todos los anexos. ", Toast.LENGTH_LONG).show();

                        } else if (grupo6.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(CamposAnexosActivity.this, "Debe seleccionar una opción en todos los anexos. ", Toast.LENGTH_LONG).show();

                        } else if (grupo7.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(CamposAnexosActivity.this, "Debe seleccionar una opción en todos los anexos. ", Toast.LENGTH_LONG).show();

                        } else if (grupo8.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(CamposAnexosActivity.this, "Debe seleccionar una opción en todos los anexos. ", Toast.LENGTH_LONG).show();

                        } else if (grupo9.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(CamposAnexosActivity.this, "Debe seleccionar una opción en todos los anexos. ", Toast.LENGTH_LONG).show();

                        } else {

                            guardarDatos();
                            //cambiar inspeccion a estado para transmitir
                            db.cambiarEstadoInspeccion(Integer.parseInt(id_inspeccion), 2);

                            if (connec) {
                                Intent servis = new Intent(CamposAnexosActivity.this, TransferirInspeccion.class);
                                startService(servis);

                                Toast.makeText(CamposAnexosActivity.this, "Inspección " + id_inspeccion , Toast.LENGTH_LONG).show();
                            }
                            Intent seccion = new Intent(CamposAnexosActivity.this, InsPendientesActivity.class);
                            seccion.putExtra("id_inspeccion",id_inspeccion);
                            startActivity(seccion);
                            finish();

                        }

                    }
                }).setNegativeButton("Rechazar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(CamposAnexosActivity.this, "Inspección no transmitida", Toast.LENGTH_LONG).show();
                    }
                });

                android.app.AlertDialog alert = builder.create();
                alert.show();

            }

        });


    }

    //guarda datos
    public void guardarDatos() {
        try {

            RadioGroup contenedor1 = (RadioGroup) findViewById(R.id.grupo1);
            int radioButtonID1 = contenedor1.getCheckedRadioButtonId();
            RadioButton radioButton1 = (RadioButton) contenedor1.findViewById(radioButtonID1);
            String texto1 = (String) radioButton1.getText();


            RadioGroup contenedor2 = (RadioGroup) findViewById(R.id.grupo2);
            int radioButtonID2 = contenedor2.getCheckedRadioButtonId();
            RadioButton radioButton2 = (RadioButton) contenedor2.findViewById(radioButtonID2);
            String texto2 = (String) radioButton2.getText();

            RadioGroup contenedor3 = (RadioGroup) findViewById(R.id.grupo3);
            int radioButtonID3 = contenedor3.getCheckedRadioButtonId();
            RadioButton radioButton3 = (RadioButton) contenedor3.findViewById(radioButtonID3);
            String texto3 = (String) radioButton3.getText();

            RadioGroup contenedor4 = (RadioGroup) findViewById(R.id.grupo4);
            int radioButtonID4 = contenedor4.getCheckedRadioButtonId();
            RadioButton radioButton4 = (RadioButton) contenedor4.findViewById(radioButtonID4);
            String texto4 = (String) radioButton4.getText();

            RadioGroup contenedor5 = (RadioGroup) findViewById(R.id.grupo5);
            int radioButtonID5 = contenedor5.getCheckedRadioButtonId();
            RadioButton radioButton5 = (RadioButton) contenedor5.findViewById(radioButtonID5);
            String texto5 = (String) radioButton5.getText();

            RadioGroup contenedor6 = (RadioGroup) findViewById(R.id.grupo6);
            int radioButtonID6 = contenedor6.getCheckedRadioButtonId();
            RadioButton radioButton6 = (RadioButton) contenedor6.findViewById(radioButtonID6);
            String texto6 = (String) radioButton6.getText();

            RadioGroup contenedor7 = (RadioGroup) findViewById(R.id.grupo7);
            int radioButtonID7 = contenedor7.getCheckedRadioButtonId();
            RadioButton radioButton7 = (RadioButton) contenedor7.findViewById(radioButtonID7);
            String texto7 = (String) radioButton7.getText();

            RadioGroup contenedor8 = (RadioGroup) findViewById(R.id.grupo8);
            int radioButtonID8 = contenedor8.getCheckedRadioButtonId();
            RadioButton radioButton8 = (RadioButton) contenedor8.findViewById(radioButtonID8);
            String texto8 = (String) radioButton8.getText();

            RadioGroup contenedor9 = (RadioGroup) findViewById(R.id.grupo9);
            int radioButtonID9 = contenedor9.getCheckedRadioButtonId();
            RadioButton radioButton9 = (RadioButton) contenedor9.findViewById(radioButtonID9);
            String texto9 = (String) radioButton9.getText();


            if(texto1.equals("Si"))
            {
                texto1="Ok";
            }
            else
            {
                texto1="";
            }

            if(texto2.equals("Si"))
            {
                texto2="Ok";
            }
            else
            {
                texto2="";
            }

            if(texto3.equals("Si"))
            {
                texto3="Ok";
            }
            else
            {
                texto3="";
            }

            if(texto4.equals("Si"))
            {
                texto4="Ok";
            }
            else
            {
                texto4="";
            }


            if(texto5.equals("Si"))
            {
                texto5="Ok";
            }
            else
            {
                texto5="";
            }


            if(texto6.equals("Si"))
            {
                texto6="Ok";
            }
            else
            {
                texto6="";
            }


            if(texto7.equals("Si"))
            {
                texto7="Ok";
            }
            else
            {
                texto7="";
            }

            if(texto8.equals("Si"))
            {
                texto8="Ok";
            }
            else
            {
                texto8="";
            }




            JSONObject valoraa = new JSONObject();
            valoraa.put("valor_id",815);
            valoraa.put("texto",texto1);

            JSONObject valorab = new JSONObject();
            valorab.put("valor_id",816);
            valorab.put("texto",texto2);

            JSONObject valorac = new JSONObject();
            valorac.put("valor_id",817);
            valorac.put("texto",texto3);

            JSONObject valorad = new JSONObject();
            valorad.put("valor_id",818);
            valorad.put("texto",texto4);

            JSONObject valorae = new JSONObject();
            valorae.put("valor_id",819);
            valorae.put("texto",texto5);

            JSONObject valoraf = new JSONObject();
            valoraf.put("valor_id",820);
            valoraf.put("texto",texto6);

            JSONObject valorag = new JSONObject();
            valorag.put("valor_id",821);
            valorag.put("texto",texto7);

            JSONObject valorah = new JSONObject();
            valorah.put("valor_id",823);
            valorah.put("texto",texto8);

            JSONObject valorai = new JSONObject();
            valorai.put("valor_id",824);
            valorai.put("texto",texto9);

            JSONArray datosvalores = new JSONArray();
            datosvalores.put(valoraa);
            datosvalores.put(valorab);
            datosvalores.put(valorac);
            datosvalores.put(valorad);
            datosvalores.put(valorae);
            datosvalores.put(valoraf);
            datosvalores.put(valorag);
            datosvalores.put(valorah);
            datosvalores.put(valorai);




            //Toast.makeText(CamposAnexosActivity.this, "Inspecciónnnnnn " + texto1 , Toast.LENGTH_LONG).show();

            if(!datosvalores.isNull(0)){
                for(int i=0;i<datosvalores.length();i++){
                    llenado = new JSONObject(datosvalores.getString(i));
                    db.insertarValor(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto"));


                }
            }

        }catch (Exception e){
            Toast.makeText(CamposAnexosActivity.this,"Error conversión json",Toast.LENGTH_SHORT);
        }

    }

    @OnClick(R.id.btnVolverAnex)
    public void DatosVolver(View view)
    {
        Intent intent = new Intent(CamposAnexosActivity.this, seccion2.class);
        intent.putExtra("id_inspeccion", id_inspeccion);
        startActivity(intent);
    }

}




