package com.letchile.let.VehPesado;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
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
import com.letchile.let.VehPesado.Fotos.posterior_vp;
import com.letchile.let.VehLiviano.SeccionActivity;
import com.letchile.let.VehPesado.Fotos.posterior_vp;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SeccionVpActivity extends AppCompatActivity {

    @BindView(R.id.tipo_veh_vp)Spinner tipoVehPesado;

    String id_inspeccion;
    String vehPesado;
    DBprovider db;
    Boolean connec = false;


    public SeccionVpActivity() {
        db = new DBprovider(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seccion_vp);
        ButterKnife.bind(this);

        connec = new ConexionInternet(this).isConnectingToInternet();

        Bundle bundle = getIntent().getExtras();
        id_inspeccion = bundle.getString("id_inspeccion");

        String[] arraytipo= getResources().getStringArray(R.array.veh_pesado);
        final List<String> arraytipolist = Arrays.asList(arraytipo);
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arraytipolist);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoVehPesado.setAdapter(spinner_adapter);

    }

    @OnClick(R.id.btnFotoVpJg) //Sección fotos
    public void Fotos(View view){

        if(tipoVehPesado.getSelectedItemPosition()!=0){
            Intent intent = new Intent(SeccionVpActivity.this, posterior_vp.class);
            intent.putExtra("id_inspeccion", id_inspeccion);
            intent.putExtra("tipoVeh",String.valueOf(tipoVehPesado.getSelectedItemPosition()));
            startActivity(intent);
        }else{
            Toast.makeText(SeccionVpActivity.this,"Debe seleccionar el tipo de vehículo",Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btnAsegVpJg) //Sección datos asegurado
    public void DatosAsegurVP(View view){
        Intent intent = new Intent(SeccionVpActivity.this, DatosAsegVpActivity.class);
        intent.putExtra("id_inspeccion", id_inspeccion);
        startActivity(intent);
    }

    @OnClick(R.id.btnInspVpJg) // Sección datos inspección
    public void DatosInsVp(View view){
        Intent intent = new Intent(SeccionVpActivity.this, DatosInspVpActivity.class);
        intent.putExtra("id_inspeccion", id_inspeccion);
        startActivity(intent);
    }

    @OnClick(R.id.btnObsVpJg) //Sección observación
    public void ObsVp(View view){
        Intent intent = new Intent(SeccionVpActivity.this, ObsVpActivity.class);
        intent.putExtra("id_inspeccion", id_inspeccion);
        startActivity(intent);
    }

    @OnClick(R.id.btnVolverSecVpJg) //Volver
    public void Volver(View view){
        Intent intent = new Intent(SeccionVpActivity.this, InsPendientesActivity.class);
        intent.putExtra("id_inspeccion", id_inspeccion);
        startActivity(intent);
    }

    @OnClick(R.id.btnTranVpJg) // Transmitir
    public void Transmitir(View view){
        //verificar nuevamente la conexión
        connec = new ConexionInternet(SeccionVpActivity.this).isConnectingToInternet();

        //CUANDO SE TOMA LA PRIMERA FOTO EN LA SECCION POSTERIOR LA INSPECCIÓN ESTÁ EN ESTADO INICIADA

        //2,3,62,63,61,30,31,22,9,39,40,41,42,13,14,65,69,72

        int fotosTomadas = db.fotosObligatoriasTomadas(Integer.parseInt(id_inspeccion));

        if(fotosTomadas>=10){

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
