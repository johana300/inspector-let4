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
            public void onClick(View v) {
                openCamaraLlantasNE(id_inspeccion);
            }
        });
        btnRuedaRespuestoLlantasE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamaraRuedaRespuesto(id_inspeccion); }
        });
        btnAdicionalLlantasE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamaraAdicionalLlantas(id_inspeccion);
            }
        });

        btnLlantasVolverE = (Button)findViewById(R.id.btnLlantasVolverE);
        btnLlantasVolverE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent   = new Intent(llantasneumaticos.this,lateralizquierdo.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
                finish();
            }
        });

        btnSiguienteLlantaE = (Button)findViewById(R.id.btnSiguienteLlantaE);
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
    private void openCamaraLlantasNE(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {
            //Long timestamp = System.currentTimeMillis() / 1000;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date();

            String fecha = dateFormat.format(date);
            String imageName = fecha + "_Foto_LLANTAS.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_LLANTAS.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(llantasneumaticos.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_LLANTAS);
        }
    }
    private void openCamaraRuedaRespuesto(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {
            //Long timestamp = System.currentTimeMillis() / 1000;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date();

            String fecha = dateFormat.format(date);
            String imageName = fecha + "_Foto_Rueda_Respuesto.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Rueda_Respuesto.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(llantasneumaticos.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_RUEDA);
        }
    }
    private void openCamaraAdicionalLlantas(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {
            //Long timestamp = System.currentTimeMillis() / 1000;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date();

            String fecha = dateFormat.format(date);
            String imageName = fecha + "_Foto_Adicional_Llantas.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Adicional_Llantas.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(llantasneumaticos.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_ADICIONAL);
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
                        imageLlantasNE.setImageBitmap(bitmapLlanatas);
                        String imagenLlantas = foto.convertirImagenDano(bitmapLlanatas);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Llantas y Neumaticos", 0, imagenLlantas);

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
                        imageRuedaRespuestoLlantasE.setImageBitmap(bitmapRueda);
                        String imagenRueda = foto.convertirImagenDano(bitmapRueda);
                        db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Rueda de Respuesto Llantas y Neumaticos", 0, imagenRueda);

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
