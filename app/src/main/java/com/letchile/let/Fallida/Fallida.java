package com.letchile.let.Fallida;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.BuildConfig;
import com.letchile.let.Clases.PropiedadesFoto;
import com.letchile.let.InsPendientesActivity;
import com.letchile.let.R;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Servicios.TransferirFoto;
import com.letchile.let.Servicios.TransferirFotoFallida;
import com.letchile.let.Servicios.TransferirInspeccionFallida;
import com.letchile.let.detalleActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Johana Godoy on 22-02-2018.
 */

public class Fallida extends AppCompatActivity{

    DBprovider db;
    private static String APP_DIRECTORY = "";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY ;
    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_FALLIDA = 250;
    private File ruta_fa;
    private String ruta = "";
    private String mPath;
    PropiedadesFoto foto;
    ImageView imagenFallida;
    Boolean connec = false;
    Button btnFotoFallida,btnEnviarFallida;
    Context contexto = this;
    int correlativo = 0;
    String nombreimagen = "",fecha_cita,hora_cita,fechaHoraFallida,perfil;
    EditText edtComentarioRegiones;

    public Fallida(){db = new DBprovider(this);foto = new PropiedadesFoto(this);}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fallida);

        connec = new ConexionInternet(this).isConnectingToInternet();

        perfil = db.obtenerPerfil();

        edtComentarioRegiones = findViewById(R.id.edtComentarioFallida);
        if(perfil.equals("6")) {
            edtComentarioRegiones.setVisibility(View.VISIBLE);
        }

        //Traspaso de datos
        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");
        fecha_cita=bundle.getString("fecha_cita");
        hora_cita=bundle.getString("hora_cita");

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            fechaHoraFallida = sdf.format(new Date());

        } catch (Exception exception) {
            Log.e("DIDN'T WORK", "exception " + exception);
        }


        //FOTO
        imagenFallida = findViewById(R.id.imagenFallida);

        //Botón volver al detalle de la OI
        final Button btnVolverFaJg = findViewById(R.id.btnVolverFaJg);
        btnVolverFaJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( Fallida.this, detalleActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                intent.putExtra("fecha_cita",fecha_cita);
                intent.putExtra("hora_cita",hora_cita);
                startActivity(intent);

            }
        });


        //FOOTER
        btnFotoFallida = findViewById(R.id.btnFotoFallida);
        btnFotoFallida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openCamaraFallida(Integer.parseInt(id_inspeccion));}
        });


        btnEnviarFallida = findViewById(R.id.btnSigObsJg);
        btnEnviarFallida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                connec = new ConexionInternet(Fallida.this).isConnectingToInternet();
                if(connec) {
                    Intent ine = new Intent(Fallida.this, TransferirInspeccionFallida.class);
                    ine.putExtra("id_inspeccion", id_inspeccion);
                    startService(ine);

                    Intent in = new Intent(Fallida.this,InsPendientesActivity.class);
                    startActivity(in);
                }else{
                    Intent in = new Intent(Fallida.this,InsPendientesActivity.class);
                    startActivity(in);
                }
            }
        });


        //image view
        String imagenFa = db.fotoFallida(Integer.parseInt(id_inspeccion));

        if(imagenFa.length()>=3 )
        {
            byte[] decodedString = Base64.decode(imagenFa, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imagenFallida.setImageBitmap(decodedByte);
            imagenFallida.setVisibility(View.VISIBLE);
        }


    }




    private void openCamaraFallida(int id_inspeccion){
        ruta_fa = Environment.getExternalStorageDirectory();
        File file = new File(ruta_fa.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {

            correlativo = db.correlativoFotosFallida(id_inspeccion);
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Fallida.jpg";

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;

            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(Fallida.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, PHOTO_FALLIDA);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_FALLIDA:
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
                    imagenFallida.setImageBitmap(bitmap);
                    imagenFallida.setVisibility(View.VISIBLE);
                    String imagen = foto.convertirImagenDano(bitmap);
                    //el id se trae de la base de datos
                    if(perfil.equals("6")) {
                        db.insertartFotoFallida(Integer.parseInt(id_inspeccion), nombreimagen, fecha_cita, db.idFotoFallida(Integer.parseInt(id_inspeccion)), fechaHoraFallida, 0, imagen, edtComentarioRegiones.getText().toString());
                        Intent servis = new Intent(contexto, TransferirFotoFallida.class);
                        servis.putExtra("nombreFoto",nombreimagen);
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);
                        Toast.makeText(Fallida.this,"Fotografía enviada",Toast.LENGTH_SHORT).show();
                        edtComentarioRegiones.setText("");
                    }else{
                        db.insertartFotoFallida(Integer.parseInt(id_inspeccion), nombreimagen, fecha_cita, db.idFotoFallida(Integer.parseInt(id_inspeccion)), fechaHoraFallida, 0, imagen, "Foto Fallida");
                        Intent servis = new Intent(contexto, TransferirFotoFallida.class);
                        servis.putExtra("nombreFoto",nombreimagen);
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);
                    }
                    break;
            }
        }
    }




}