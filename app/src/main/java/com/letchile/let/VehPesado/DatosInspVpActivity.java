package com.letchile.let.VehPesado;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.BuildConfig;
import com.letchile.let.Clases.PropiedadesFoto;
import com.letchile.let.InsPendientesActivity;
import com.letchile.let.R;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Servicios.TransferirFoto;
import com.letchile.let.VehLiviano.DatosInspActivity;
import com.letchile.let.VehLiviano.SeccionActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatosInspVpActivity extends AppCompatActivity {

    DBprovider db;

    EditText dirIns;
    EditText fechaInsp;
    EditText horaInsp;

    private File ruta_sdV;
    private String rutaV = "";
    Spinner tipoVehVp,region,comuna;;
    PropiedadesFoto fotoV;
    Boolean connec = false;
    ImageView imagenComproV;
    Button btnFotocomprobanteV,btbVolver,BtnPendientes,BtnGuardar,mOptionButton;
    EditText usrInspector,direccionIns,fechaIns,horaIns,entrevistado;
    private String mPathV;
    String nombreimagenV = "";
    private final int PHOTO_COMPROBANTEV = 250;
    Context contextoV = this;
    /*FALTA CARGAR COMBOS DE REGION Y COMUNA*/

    public DatosInspVpActivity(){db = new DBprovider(this);fotoV = new PropiedadesFoto(this);};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_insp_vp);


        connec = new ConexionInternet(this).isConnectingToInternet();

        //Traspaso de datos
        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");

        imagenComproV = findViewById(R.id.imagenComproV);

        //direccion inspeccion
        dirIns = (EditText)findViewById(R.id.dirIns);
        dirIns.getText().toString();

        //fecha inspeccion
        fechaInsp = (EditText)findViewById(R.id.fechaInsp);
        fechaInsp.getText().toString();

        //hora inspeccion
        horaInsp = (EditText)findViewById(R.id.horaInsp);
        horaInsp.getText().toString();

        //entrevistado
        entrevistado = (EditText)findViewById(R.id.entrevistado);
        entrevistado.getText().toString();

        // cargar un combo tipo vehiculo
        /*tipoVehVp = (Spinner)findViewById(R.id.tipo_veh_vp);
        String[] arraytipo = getResources().getStringArray(R.array.tipoVehVp);
        final List<String> arraytipolist = Arrays.asList(arraytipo);
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource( this, R.array.tipoVehVp , android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoVehVp.setAdapter(spinner_adapter);
        tipoVehVp.setSelection(arraytipolist.lastIndexOf(db.accesorio(92,364).toString()));*/

        //region

        String regionInicial[][]=db.obtenerRegion(db.accesorio(Integer.parseInt(id_inspeccion),7).toString());
        String listaRegiones[][]=db.listaRegiones();
        region = (Spinner)findViewById(R.id.regionSpinnerMQ);
        String[] arraySpinner = new String[listaRegiones.length];
        arraySpinner[0]=regionInicial[0][0];
        for(int i=1;i<listaRegiones.length;i++)        {
            arraySpinner[i]=listaRegiones[i][0];
        }
        ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        region.setAdapter(adapterRegion);


        comuna = (Spinner)findViewById(R.id.comunaSpinner);
        region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                comuna.setAdapter(null);
                String regionSelected = region.getSelectedItem().toString();
                String listaComuna[][] = db.listaComunas(regionSelected);
                String[] spinnercomuna = new String[listaComuna.length];
                spinnercomuna[0] = "Seleccione";
                for(int i=1;i<listaComuna.length;i++){
                    spinnercomuna[i] = listaComuna[i][0];
                }
                ArrayAdapter<String> adapterComuna = new ArrayAdapter<String>(DatosInspVpActivity.this,android.R.layout.simple_spinner_item,spinnercomuna);
                adapterComuna.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                comuna.setAdapter(adapterComuna);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //FOOTER
        btnFotocomprobanteV = (Button)findViewById(R.id.fotoComprobanteV);
        btnFotocomprobanteV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openCamaraComprobanteV(Integer.parseInt(id_inspeccion));}
        });

        //image view
        String imagenComprobante = db.foto(Integer.parseInt(id_inspeccion),"Foto Comprobante");

        if(imagenComprobante.length()>=3 )
        {
            byte[] decodedString = Base64.decode(imagenComprobante, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imagenComproV.setImageBitmap(decodedByte);
            imagenComproV.setVisibility(View.VISIBLE);
        }



        //Botón volver de sección
        final Button btnSigInspJg = (Button)findViewById(R.id.btnSigInspJg);
        btnSigInspJg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                try{


                    JSONObject valor93 = new JSONObject();
                    valor93.put("valor_id",733);
                    valor93.put("texto",dirIns.getText().toString());

                    JSONObject valor94 = new JSONObject();
                    valor94.put("valor_id",737);
                    valor94.put("texto",fechaInsp.getText().toString());

                    JSONObject valor95 = new JSONObject();
                    valor95.put("valor_id",738);
                    valor95.put("texto",horaInsp.getText().toString());

                    JSONObject valor96 = new JSONObject();
                    valor96.put("valor_id",755);
                    valor96.put("texto",entrevistado.getText().toString());

                    JSONObject valor97 = new JSONObject();
                    valor97.put("valor_id",364);
                    valor97.put("texto",tipoVehVp.getSelectedItem().toString());






                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(valor93);
                    jsonArray.put(valor94);
                    jsonArray.put(valor95);
                    jsonArray.put(valor96);
                    jsonArray.put(valor97);






                    JSONObject llenado;
                    if (!jsonArray.isNull(0)) {
                        for(int i=0;i<jsonArray.length();i++){
                            llenado = new JSONObject(jsonArray.getString(i));
                            db.insertarValor(92,llenado.getInt("valor_id"),llenado.getString("texto"));


                        }
                    }

                }catch (Exception e)
                {

                    Toast.makeText(DatosInspVpActivity.this, "", Toast.LENGTH_SHORT);
                }

                Intent intent = new Intent( DatosInspVpActivity.this, ObsVpActivity.class);
                startActivity(intent);
            }
        });

        //Botón volver pendiente
        final Button btnVolverInspVpJg = (Button)findViewById(R.id.btnVolverInspVpJg);
        btnVolverInspVpJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( DatosInspVpActivity.this, InsPendientesActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });

        //Botón volver de secciones
        final Button btnVolverInspJg = (Button)findViewById(R.id.btnVolverInspJg);
        btnVolverInspJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( DatosInspVpActivity.this, SeccionActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });


    }


    private void openCamaraComprobanteV(int id_inspeccion){
        ruta_sdV = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sdV.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {
            //Long timestamp = System.currentTimeMillis() / 1000;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date();

            String fecha = dateFormat.format(date);
            String imageName = fecha + "Comprobante.jpg";
            rutaV = file.toString() + "/" + imageName;
            mPathV = rutaV;

            Calendar c = Calendar.getInstance();
            nombreimagenV = String.valueOf(id_inspeccion)+"_"+ String.valueOf(c.get(Calendar.SECOND))+"_Comprobante.jpg";

            File newFile = new File(mPathV);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("nombreImg",imageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(DatosInspVpActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, PHOTO_COMPROBANTEV);

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_COMPROBANTEV:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPathV}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmap = BitmapFactory.decodeFile(mPathV);
                    bitmap = fotoV.redimensiomarImagen(bitmap);
                    imagenComproV.setImageBitmap(bitmap);
                    imagenComproV.setVisibility(View.VISIBLE);
                    String imagen = fotoV.convertirImagenDano(bitmap);

                    db.insertaFoto(Integer.parseInt(id_inspeccion), 72, nombreimagenV, "Foto Comprobante", 0, imagen);
                    break;
            }

            //TRANSFERIR FOTO

            Intent servis = new Intent(contextoV, TransferirFoto.class);
            servis.putExtra("id_foto","72");
            servis.putExtra("id_inspeccion",id_inspeccion);
            startService(servis);

        }
    }


}
