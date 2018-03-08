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
import android.view.Gravity;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class motor extends AppCompatActivity {

    DBprovider db;
    Boolean connec = false;
    private final int PHOTO_CUNA = 200;
    private final int PHOTO_MOTOR = 300;
    private final int PHOTO_CHASIS = 400;
    private final int PHOTO_ADICIONAL = 500;

    private Button btnVolverMotorE,btnVolverSecMotorE,btnSiguienteMotorE,cunaMotorE,motorE,chasisVinE,adicionalE,btnSeccionUnoMotor;
    private ImageView imageCunaMotorE,imageMotorE,imageChasisVinE,imageAdicionalE;
    private String mPath;
    private File ruta_sd;
    private String ruta = "";
    String nombreimagen = "";

    PropiedadesFoto foto;

    public motor(){db = new DBprovider(this);foto=new PropiedadesFoto(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motor);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion = bundle.getString("id_inspeccion");

        connec = new ConexionInternet(this).isConnectingToInternet();

        cunaMotorE = findViewById(R.id.cunaMotorE);
        imageCunaMotorE = findViewById(R.id.imageCunaMotorE);
        motorE = findViewById(R.id.motorE);
        imageMotorE = findViewById(R.id.imageMotorE);
        chasisVinE = findViewById(R.id.chasisVinE);
        imageChasisVinE = findViewById(R.id.imageChasisVinE);
        adicionalE = findViewById(R.id.adicionalE);
        imageAdicionalE = findViewById(R.id.imageAdicionalE);
        btnSeccionUnoMotor = findViewById(R.id.btnSeccionUnoMotor);

        cunaMotorE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openCamaraCunaMotor(id_inspeccion);
            }
        });
        motorE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamaraMotor(id_inspeccion);
            }
        });
        chasisVinE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openCamaraChasisVin(id_inspeccion);
            }
        });
        adicionalE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openCamaraAdicional(id_inspeccion);
            }
        });
        btnSeccionUnoMotor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {desplegarSeccionUnoMotor(id_inspeccion);
            }
        });

        btnVolverMotorE = (Button)findViewById(R.id.btnVolverMotorE);
        btnVolverMotorE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        btnSiguienteMotorE = (Button)findViewById(R.id.btnSiguienteMotorE);
        btnSiguienteMotorE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageMotor = db.foto(Integer.parseInt(id_inspeccion),"Foto Motor");
                String imageChasisVin = db.foto(Integer.parseInt(id_inspeccion),"Foto Chasis(VIN)");
                String imageCunaMotor = db.foto(Integer.parseInt(id_inspeccion),"Foto Cuna Motor");

                if(imageCunaMotor.length()<=3 || imageChasisVin.length()<=3 || imageMotor.length()<=3 )  {
                    AlertDialog.Builder builder = new AlertDialog.Builder(motor.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Cuna Motor</li><p><li>- Foto Motor</li></p>" +
                            "<p><li>- Foto Chavis(VIN)</li></p></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

                else
                {
                    Intent intent = new Intent(motor.this, documento.class);
                    intent.putExtra("id_inspeccion", id_inspeccion);
                    startActivity(intent);
                }
            }
        });

    }

    private void openCamaraCunaMotor(String id) {

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
            String imageName = fecha + "_Foto_Cuna_Motor.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            Calendar c = Calendar.getInstance();
            nombreimagen = String.valueOf(id_inspeccion)+"_"+ String.valueOf(c.get(Calendar.SECOND))+"_Foto_Cuna_Motor.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(motor.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_CUNA);
        }
    }
    private void openCamaraMotor(String id) {

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
            String imageName = fecha + "_Foto_Motor.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            Calendar c = Calendar.getInstance();
            nombreimagen = String.valueOf(id_inspeccion)+"_"+ String.valueOf(c.get(Calendar.SECOND))+"_Foto_Motor.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(motor.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_MOTOR);
        }
    }
    private void openCamaraChasisVin(String id) {

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
            String imageName = fecha + "_Foto_Chasis_Motor.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            Calendar c = Calendar.getInstance();
            nombreimagen = String.valueOf(id_inspeccion)+"_"+ String.valueOf(c.get(Calendar.SECOND))+"_Foto_Chasis_Motor.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(motor.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_CHASIS);
        }
    }
    private void openCamaraAdicional(String id) {

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
            String imageName = fecha + "_Foto_Adicional_Motor.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            Calendar c = Calendar.getInstance();
            nombreimagen = String.valueOf(id_inspeccion)+"_"+ String.valueOf(c.get(Calendar.SECOND))+"_Foto_Adicional_Motor.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(motor.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_ADICIONAL);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_CUNA:
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
                    imageCunaMotorE.setImageBitmap(bitmap);
                    String imagen = foto.convertirImagenDano(bitmap);
                    db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Cuna Motor", 0, imagen);

                        Intent servis = new Intent(motor.this, TransferirFoto.class);
                    servis.putExtra("comentario","Foto Cuna Motor");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;
                case PHOTO_MOTOR:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapMotor = BitmapFactory.decodeFile(mPath);
                    bitmapMotor = foto.redimensiomarImagen(bitmapMotor);
                    imageMotorE.setImageBitmap(bitmapMotor);
                    String imagenMotor = foto.convertirImagenDano(bitmapMotor);
                    db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Motor", 0, imagenMotor);

                     servis = new Intent(motor.this, TransferirFoto.class);
                    servis.putExtra("comentario","Foto Motor");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;
                case PHOTO_CHASIS:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapChasis = BitmapFactory.decodeFile(mPath);
                    bitmapChasis = foto.redimensiomarImagen(bitmapChasis);
                    imageChasisVinE.setImageBitmap(bitmapChasis);
                    String imagenChasis = foto.convertirImagenDano(bitmapChasis);
                    db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Chasis(VIN)", 0, imagenChasis);

                        servis = new Intent(motor.this, TransferirFoto.class);
                    servis.putExtra("comentario","Foto Chasis(VIN)");
                        servis.putExtra("id_inspeccion",id_inspeccion);
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

                    Bitmap bitmapAdicional = BitmapFactory.decodeFile(mPath);
                    bitmapAdicional = foto.redimensiomarImagen(bitmapAdicional);
                    imageAdicionalE.setImageBitmap(bitmapAdicional);
                    String imagenAdicional = foto.convertirImagenDano(bitmapAdicional);
                    db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Adicional Motor", 0, imagenAdicional);

                        servis = new Intent(motor.this, TransferirFoto.class);
                    servis.putExtra("comentario","Foto Adicional Motor");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;
            }
        }
    }

    private void desplegarSeccionUnoMotor(String id)    {

        if (cunaMotorE.getVisibility() == View.VISIBLE) {

            cunaMotorE.setVisibility(View.GONE);
            imageCunaMotorE.setVisibility(View.GONE);
            imageCunaMotorE.setImageBitmap(null);
            motorE.setVisibility(View.GONE);
            imageMotorE.setVisibility(View.GONE);
            imageMotorE.setImageBitmap(null);
            chasisVinE.setVisibility(View.GONE);
            imageChasisVinE.setVisibility(View.GONE);
            imageChasisVinE.setImageBitmap(null);
            adicionalE.setVisibility(View.GONE);
            imageAdicionalE.setVisibility(View.GONE);
            imageAdicionalE.setImageBitmap(null);

        }
        else
        {

            String imageCunaMotor = db.foto(Integer.parseInt(id),"Foto Cuna Motor");
            String imageMotor = db.foto(Integer.parseInt(id),"Foto Motor");
            String imageChasisVin = db.foto(Integer.parseInt(id),"Foto Chasis(VIN)");
            String imageAdicional = db.foto(Integer.parseInt(id),"Foto Adicional Motor");

            if(imageCunaMotor.length()>3)
            {
                byte[] decodedString = Base64.decode(imageCunaMotor, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageCunaMotorE.setImageBitmap(decodedByte);

            }
            if(imageMotor.length()>3)
            {
                byte[] decodedString = Base64.decode(imageMotor, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageMotorE.setImageBitmap(decodedByte);
            }
            if(imageChasisVin.length()>3)
            {
                byte[] decodedString = Base64.decode(imageChasisVin, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageChasisVinE.setImageBitmap(decodedByte);
            }
            if(imageAdicional.length()>3)
            {
                byte[] decodedString = Base64.decode(imageAdicional, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageAdicionalE.setImageBitmap(decodedByte);
            }


            cunaMotorE.setVisibility(View.VISIBLE);
            imageCunaMotorE.setVisibility(View.VISIBLE);
            motorE.setVisibility(View.VISIBLE);
            imageMotorE.setVisibility(View.VISIBLE);
            chasisVinE.setVisibility(View.VISIBLE);
            imageChasisVinE.setVisibility(View.VISIBLE);
            adicionalE.setVisibility(View.VISIBLE);
            imageAdicionalE.setVisibility(View.VISIBLE);




        }

    }
}
