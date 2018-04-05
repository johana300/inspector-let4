package com.letchile.let.VehLiviano.Fotos;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.BuildConfig;
import com.letchile.let.Clases.PropiedadesFoto;
import com.letchile.let.R;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Servicios.TransferirFoto;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class llantasneumaticos extends AppCompatActivity {

    DBprovider db;
    Boolean connec = false;

    private final int PHOTO_LLANTAS = 200;
    private final int PHOTO_RUEDA = 300;
    private final int PHOTO_ADICIONAL = 400;
    private Button btnLlantasVolverE,btnVolverSecLlaE,btnSiguienteLlantaE,btnLlantasNE,btnRuedaRespuestoLlantasE,btnAdicionalLlantasE,btnSeccionUnoLlantasE;
    private ImageView imageLlantasNE,imageRuedaRespuestoLlantasE,imageAdicionalLlantasE;
    private String mPath;
    private File ruta_sd;
    private String ruta = "";
    PropiedadesFoto foto;
    String nombreimagen = "";
    int correlativo = 0;

    public llantasneumaticos(){db = new DBprovider(this);foto=new PropiedadesFoto(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llantasneumaticos);


        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion = bundle.getString("id_inspeccion");

        connec = new ConexionInternet(this).isConnectingToInternet();


        btnLlantasNE = findViewById(R.id.btnLlantasNE);
        imageLlantasNE = findViewById(R.id.imageLlantasNE);
        btnRuedaRespuestoLlantasE = findViewById(R.id.btnRuedaRespuestoLlantasE);
        imageRuedaRespuestoLlantasE = findViewById(R.id.imageRuedaRespuestoLlantasE);
        btnAdicionalLlantasE = findViewById(R.id.btnAdicionalLlantasE);
        imageAdicionalLlantasE = findViewById(R.id.imageAdicionalLlantasE);
        btnSeccionUnoLlantasE = findViewById(R.id.btnSeccionUnoLlantasE);

        btnSeccionUnoLlantasE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {desplegarCamposSeccionUno(id_inspeccion);
            }
        });

        btnLlantasNE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {funcionCamara(id_inspeccion,"_Foto_LLANTAS.jpg",PHOTO_LLANTAS);
            }
        });
        btnRuedaRespuestoLlantasE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcionCamara(id_inspeccion,"_Foto_Rueda_Respuesto.jpg",PHOTO_RUEDA); }
        });
        btnAdicionalLlantasE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcionCamara(id_inspeccion,"_Foto_Adicional_Llantas.jpg",PHOTO_ADICIONAL);
            }
        });

        btnLlantasVolverE = findViewById(R.id.btnLlantasVolverE);
        btnLlantasVolverE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent   = new Intent(llantasneumaticos.this,lateralizquierdo.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
                finish();
            }
        });

        btnSiguienteLlantaE = findViewById(R.id.btnSiguienteLlantaE);
        btnSiguienteLlantaE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageLlantasN = db.foto(Integer.parseInt(id_inspeccion),"Foto Llantas y Neumaticos");
                String imageRuedaRespuestoLlantas = db.foto(Integer.parseInt(id_inspeccion),"Foto Rueda de Respuesto Llantas y Neumaticos");
                if(imageLlantasN.length()<=3 || imageRuedaRespuestoLlantas.length()<=3)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(llantasneumaticos.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Llantas y Neumáticos</li><p><li>- Foto Rueda de Respuesto</li></p></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else{
                    Intent intent   = new Intent(llantasneumaticos.this,vl_techo.class);
                    intent.putExtra("id_inspeccion",id_inspeccion);
                    startActivity(intent);
                    finish();
                }

            }
        });

    }

    ///FUNCIÓN ABRE LA CAMARA Y TOMA LAS FOTOGRAFIAS
    public void funcionCamara(String id,String nombre_foto,int CodigoFoto){
        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString()+'/'+id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+nombre_foto;

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;

            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,  FileProvider.getUriForFile(llantasneumaticos.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, CodigoFoto);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");

        try {

            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case PHOTO_LLANTAS:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapLlanatas = BitmapFactory.decodeFile(mPath);
                        bitmapLlanatas = foto.redimensiomarImagen(bitmapLlanatas);

                        String imagenLlantas = foto.convertirImagenDano(bitmapLlanatas);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Llantas y Neumaticos", 0, imagenLlantas);
                        imagenLlantas = "data:image/jpg;base64,"+imagenLlantas;
                        String base64Image1 = imagenLlantas.split(",")[1];
                        byte[] decodedString1 = Base64.decode(base64Image1, Base64.DEFAULT);
                        Bitmap decodedByte1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                        imageLlantasNE.setImageBitmap(decodedByte1);

                        Intent servis = new Intent(llantasneumaticos.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Foto Llantas y Neumaticos");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        break;
                    case PHOTO_RUEDA:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapRueda = BitmapFactory.decodeFile(mPath);
                        bitmapRueda = foto.redimensiomarImagen(bitmapRueda);

                        String imagenRueda = foto.convertirImagenDano(bitmapRueda);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Rueda de Respuesto Llantas y Neumaticos", 0, imagenRueda);
                        db.insertarValor(Integer.parseInt(id_inspeccion), 289, "Ok");
                        imagenRueda = "data:image/jpg;base64,"+imagenRueda;
                        String base64Image2 = imagenRueda.split(",")[1];
                        byte[] decodedString2 = Base64.decode(base64Image2, Base64.DEFAULT);
                        Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
                        imageRuedaRespuestoLlantasE.setImageBitmap(decodedByte2);

                        servis = new Intent(llantasneumaticos.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Foto Rueda de Respuesto Llantas y Neumaticos");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        break;
                    case PHOTO_ADICIONAL:
                        MediaScannerConnection.scanFile(this,
                                new String[]{mPath}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        Log.i("ExternalStorage", "Scanned " + path + ":");
                                        Log.i("ExternalStorage", "-> Uri = " + uri);
                                    }
                                });

                        Bitmap bitmapAdicionalLlantas = BitmapFactory.decodeFile(mPath);
                        bitmapAdicionalLlantas = foto.redimensiomarImagen(bitmapAdicionalLlantas);
                        imageAdicionalLlantasE.setImageBitmap(bitmapAdicionalLlantas);
                        String imagenAdicionalLlantas = foto.convertirImagenDano(bitmapAdicionalLlantas);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Adicional Llantas y Neumaticos", 0, imagenAdicionalLlantas);

                        imagenAdicionalLlantas = "data:image/jpg;base64,"+imagenAdicionalLlantas;
                        String base64Image3 = imagenAdicionalLlantas.split(",")[1];
                        byte[] decodedString3 = Base64.decode(base64Image3, Base64.DEFAULT);
                        Bitmap decodedByte3 = BitmapFactory.decodeByteArray(decodedString3, 0, decodedString3.length);


                        servis = new Intent(llantasneumaticos.this, TransferirFoto.class);
                        servis.putExtra("comentario", "Foto Adicional Llantas y Neumaticos");
                        servis.putExtra("id_inspeccion", id_inspeccion);
                        startService(servis);

                        break;
                }
            }
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
    }


    public void desplegarCamposSeccionUno(String id)    {
        if (btnLlantasNE.getVisibility()==View.VISIBLE)
        {
            btnLlantasNE.setVisibility(View.GONE);
            imageLlantasNE.setVisibility(View.GONE);
            imageLlantasNE.setImageBitmap(null);
            btnRuedaRespuestoLlantasE.setVisibility(View.GONE);
            imageRuedaRespuestoLlantasE.setVisibility(View.GONE);
            imageRuedaRespuestoLlantasE.setImageBitmap(null);
            btnAdicionalLlantasE.setVisibility(View.GONE);
            imageAdicionalLlantasE.setVisibility(View.GONE);
            imageAdicionalLlantasE.setImageBitmap(null);

        }
        else
        {
            String imageLlantasN = db.foto(Integer.parseInt(id),"Foto Llantas y Neumaticos");
            String imageRuedaRespuestoLlantas = db.foto(Integer.parseInt(id),"Foto Rueda de Respuesto Llantas y Neumaticos");
            String imageAdicionalLlantas = db.foto(Integer.parseInt(id),"Foto Adicional Llantas y Neumaticos");

            if(imageLlantasN.length()>3)
            {
                byte[] decodedString = Base64.decode(imageLlantasN, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageLlantasNE.setImageBitmap(decodedByte);
            }

            if(imageRuedaRespuestoLlantas.length()>3)
            {
                byte[] decodedString = Base64.decode(imageRuedaRespuestoLlantas, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageRuedaRespuestoLlantasE.setImageBitmap(decodedByte);
            }
            if(imageAdicionalLlantas.length()>3)
            {
                byte[] decodedString = Base64.decode(imageAdicionalLlantas, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageAdicionalLlantasE.setImageBitmap(decodedByte);
            }


            btnLlantasNE.setVisibility(View.VISIBLE);
            imageLlantasNE.setVisibility(View.VISIBLE);
            btnRuedaRespuestoLlantasE.setVisibility(View.VISIBLE);
            imageRuedaRespuestoLlantasE.setVisibility(View.VISIBLE);
            btnAdicionalLlantasE.setVisibility(View.VISIBLE);
            imageAdicionalLlantasE.setVisibility(View.VISIBLE);

        }

    }
}
