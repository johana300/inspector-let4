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
import android.widget.ImageView;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.BuildConfig;
import com.letchile.let.Clases.PropiedadesFoto;
import com.letchile.let.R;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Servicios.TransferirFoto;
import com.letchile.let.detalleActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    Button btnFotoFallida;
    Context contexto = this;


    public Fallida(){db = new DBprovider(this);foto = new PropiedadesFoto(this);}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fallida);

        connec = new ConexionInternet(this).isConnectingToInternet();
        //Traspaso de datos
        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");
        final String fecha_cita=bundle.getString("fecha_cita");
        final String perfil=bundle.getString("perfil");




        //FOTO
        imagenFallida = findViewById(R.id.imagenFallida);

        //Botón volver al detalle de la OI
        final Button btnVolverFaJg = (Button)findViewById(R.id.btnVolverFaJg);
        btnVolverFaJg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( Fallida.this, detalleActivity.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                //startActivity(intent);

                //Intent intent2 = new Intent(Fallida.this, detalleActivity.class);
                intent.putExtra("fecha_cita",fecha_cita);
                intent.putExtra("perfil",perfil);
                startActivity(intent);

            }
        });


        //FOOTER
        btnFotoFallida = (Button)findViewById(R.id.btnFotoFallida);
        if(mayRequestStoragePermission())
            btnFotoFallida.setEnabled(true);
        else
            btnFotoFallida.setEnabled(false);

        btnFotoFallida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openCamaraFallida(Integer.parseInt(id_inspeccion));}
        });

        //image view
        String imagenFa = db.foto(Integer.parseInt(id_inspeccion),"Foto Fallida");

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
            //Long timestamp = System.currentTimeMillis() / 1000;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date();

            String fecha = dateFormat.format(date);
            String imageName = fecha + "Fallida.jpg";
            ruta = file.toString() + "/" + imageName;
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
                    db.insertaFoto(Integer.parseInt(id_inspeccion), 70, mPath, "Foto Fallida", 0, imagen);
                    break;
            }

            //TRANSFERIR FOTO
            if(connec) {
                /*Intent servis = new Intent(DatosInspActivity.this,Transferir.class);
                servis.putExtra("id_foto","70");
                servis.putExtra("id_inspeccion",id_inspeccion);
                //Bundle extra = new Bundle();
                //extra.putString("id_inspeccion",id_inspeccion);
                //extra.putString("id_foto",String.valueOf(70));*/
                Intent servis = new Intent(contexto, TransferirFoto.class);
                servis.putExtra("id_foto","70");
                servis.putExtra("id_inspeccion",id_inspeccion);
                startService(servis);
            }
        }
    }

    private boolean mayRequestStoragePermission() {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        // }

        return false;
    }

    //foto fallida  fotofallida
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(Fallida.this, "Permisos aceptados", Toast.LENGTH_SHORT).show();
                btnFotoFallida.setEnabled(true);
            }
        }else{
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Fallida.this);
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }


}

