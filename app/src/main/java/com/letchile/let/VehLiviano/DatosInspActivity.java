package com.letchile.let.VehLiviano;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
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
import com.letchile.let.Clases.PropiedadesTexto;
import com.letchile.let.Clases.Validaciones;
import com.letchile.let.InsPendientesActivity;
import com.letchile.let.R;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Servicios.TransferirFoto;
import com.letchile.let.VehPesado.DatosInspVpActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class DatosInspActivity extends AppCompatActivity {

    private static String APP_DIRECTORY = "";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY ;
    private final int MY_PERMISSIONS = 100;
    DBprovider db;
    Button btnFotocomprobante,btbVolver,BtnPendientes,BtnGuardar,mOptionButton;
    EditText usrInspector,direccionIns,fechaIns,horaIns,entrevistado;
    Spinner region,comuna,spinnerInsp;
    private String mPath;
    private File ruta_sd;
    private String ruta = "";
    private final int PHOTO_COMPROBANTE = 250;
    ImageView imagenCompro;
    Boolean connec = false;
    PropiedadesFoto foto;
    Context contexto = this;
    String nombreimagen = "", imagenComprobante;
    JSONObject llenado;
    int correlativo = 0;
    String id_inspeccion;


    public DatosInspActivity(){db = new DBprovider(this);foto = new PropiedadesFoto(this);};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_insp);

        connec = new ConexionInternet(this).isConnectingToInternet();

        //Traspaso de datos
        Bundle bundle = getIntent().getExtras();
        id_inspeccion=bundle.getString("id_inspeccion");


        //FOTO
        imagenCompro = findViewById(R.id.imagenCompro);

        direccionIns =  findViewById(R.id.direccionInspe);
        direccionIns.setOnEditorActionListener(new PropiedadesTexto());
        direccionIns.setText(db.accesorio(Integer.parseInt(id_inspeccion),358));

        fechaIns =  findViewById(R.id.fechaInsp);
        fechaIns.setOnEditorActionListener(new PropiedadesTexto());
        fechaIns.setText(db.accesorio(Integer.parseInt(id_inspeccion),360));

        horaIns =  findViewById(R.id.horaInsp);
        horaIns.setOnEditorActionListener(new PropiedadesTexto());
        horaIns.setText(db.accesorio(Integer.parseInt(id_inspeccion),361));

        entrevistado =  findViewById(R.id.entrevistadoInsp);
        entrevistado.setOnEditorActionListener(new PropiedadesTexto());
        entrevistado.setText(db.accesorio(Integer.parseInt(id_inspeccion),755));

        // cargar un combo inspeccion por
        spinnerInsp = findViewById(R.id.spinnerInsp);
        String[] arraytipo = getResources().getStringArray(R.array.insp);
        final List<String> arraytipolist = Arrays.asList(arraytipo);
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource( this, R.array.insp , android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInsp.setAdapter(spinner_adapter);
        spinnerInsp.setSelection(arraytipolist.lastIndexOf("Nueva"));


        //region

        String regionInicial=db.obtenerRegion(db.accesorio(Integer.parseInt(id_inspeccion),359).toString());
        String listaRegiones[][]=db.listaRegiones();
        region = findViewById(R.id.regionSpinnerMQ);
        String[] arraySpinner = new String[listaRegiones.length+1];
        arraySpinner[0]=regionInicial;
        for(int i=0;i<listaRegiones.length;i++)        {
            arraySpinner[i+1]=listaRegiones[i][0];
        }
        ArrayAdapter<String> adapterRegion = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapterRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        region.setAdapter(adapterRegion);


        comuna = findViewById(R.id.comunaSpinner);
        region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                comuna.setAdapter(null);
                String regionSelected = region.getSelectedItem().toString();
                String listaComuna[][] = db.listaComunas(regionSelected);
                String[] spinnercomuna = new String[listaComuna.length+1];
                spinnercomuna[0] = db.accesorio(Integer.parseInt(id_inspeccion),359).toString();
                for(int i=0;i<listaComuna.length;i++){
                    spinnercomuna[i+1] = listaComuna[i][0];
                }
                ArrayAdapter<String> adapterComuna = new ArrayAdapter<String>(DatosInspActivity.this,android.R.layout.simple_spinner_item,spinnercomuna);
                adapterComuna.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                comuna.setAdapter(adapterComuna);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //FOOTER
        btnFotocomprobante = findViewById(R.id.fotoComprobante);
        btnFotocomprobante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openCamaraComprobante(Integer.parseInt(id_inspeccion));}
        });

        //image view
        imagenComprobante = db.foto(Integer.parseInt(id_inspeccion),"Foto Comprobante");

        if(imagenComprobante.length()>=3 )
        {
            byte[] decodedString = Base64.decode(imagenComprobante, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imagenCompro.setImageBitmap(decodedByte);
            imagenCompro.setVisibility(View.VISIBLE);
        }


        //Botón volver de sección datos de inspeccion
        final Button btnSigInspJg = findViewById(R.id.btnSigInspJg);
        btnSigInspJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(imagenCompro.getVisibility()==View.GONE){
                    Toast.makeText(DatosInspActivity.this,"Falta la foto de comprobante",Toast.LENGTH_SHORT).show();
                }else{
                    guardarDatos();
                    Intent intent = new Intent(DatosInspActivity.this, ObsActivity.class);
                    intent.putExtra("id_inspeccion", id_inspeccion);
                    startActivity(intent);
                }

            }
        });

        //Botón volver pendiente
        final Button btnPenInspJg = findViewById(R.id.btnPenInspJg);
        btnPenInspJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatos();
                Intent intent = new Intent( DatosInspActivity.this, InsPendientesActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });

        //Botón volver de secciones
        final Button btnVolverInspJg = findViewById(R.id.btnVolverInspJg);
        btnVolverInspJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatos();
                Intent intent = new Intent( DatosInspActivity.this, TechoActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
            }
        });



    }

    public void guardarDatos(){
        try {
            //LLENO EL JSON

            JSONObject datosValorIns = new JSONObject();
            datosValorIns.put("valor_id",1);
            datosValorIns.put("texto",spinnerInsp.getSelectedItem().toString());

            JSONObject datosValorA = new JSONObject();
            datosValorA.put("valor_id",358);
            datosValorA.put("texto",direccionIns.getText().toString());

            JSONObject datosValorB = new JSONObject();
            datosValorB.put("valor_id",360);
            datosValorB.put("texto",fechaIns.getText().toString());

            JSONObject datosValorC = new JSONObject();
            datosValorC.put("valor_id",361);
            datosValorC.put("texto",horaIns.getText().toString());

            JSONObject datosValorD = new JSONObject();
            datosValorD.put("valor_id",755);
            datosValorD.put("texto",entrevistado.getText().toString());

            JSONObject datosValorCo = new JSONObject();
            datosValorCo.put("valor_id",359);
            datosValorCo.put("texto",comuna.getSelectedItem().toString());

            JSONArray jsonArray = new JSONArray();

            jsonArray.put(datosValorIns);
            jsonArray.put(datosValorA);
            jsonArray.put(datosValorB);
            jsonArray.put(datosValorC);
            jsonArray.put(datosValorD);
            jsonArray.put(datosValorCo);


            //PREGUNTO SI ES NULO PARA INSERTAR LOS DATOS

            if (!jsonArray.isNull(0)) {
                for(int i=0;i<jsonArray.length();i++){
                    llenado= new JSONObject(jsonArray.getString(i));
                    db.insertarValor(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto"));
                    //validaciones.insertarDatos(Integer.parseInt(id_inspeccion),llenado.getInt("valor_id"),llenado.getString("texto"));
                }
            }

        }catch (Exception e)
        {
            Toast.makeText(DatosInspActivity.this,e.getMessage(),Toast.LENGTH_SHORT);
        }
    }

    private void openCamaraComprobante(int id_inspeccion){
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {
            correlativo = db.correlativoFotos(id_inspeccion);
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Comprobante.jpg";
            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;
            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("nombreImg",nombreimagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(DatosInspActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, PHOTO_COMPROBANTE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_COMPROBANTE:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                    bitmap = foto.redimensiomarImagen(bitmap);
                    imagenCompro.setImageBitmap(bitmap);
                    imagenCompro.setVisibility(View.VISIBLE);
                    String imagen = foto.convertirImagenDano(bitmap);

                    db.insertaFoto(Integer.parseInt(id_inspeccion), correlativo, nombreimagen, "Foto Comprobante", 0, imagen);
                    break;
            }
            //TRANSFERIR FOTO
            Intent servis = new Intent(contexto, TransferirFoto.class);
            servis.putExtra("comentario","Foto Comprobante");
            servis.putExtra("id_inspeccion",id_inspeccion);
            startService(servis);
        }
    }







}
