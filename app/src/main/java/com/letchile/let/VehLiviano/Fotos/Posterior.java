package com.letchile.let.VehLiviano.Fotos;

import android.Manifest;
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
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.BuildConfig;
import com.letchile.let.Clases.PropiedadesFoto;
import com.letchile.let.Clases.Validaciones;
import com.letchile.let.R;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Servicios.TransferirFoto;
import com.letchile.let.VehLiviano.SeccionActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LETCHILE on 20/02/2018.
 */

public class Posterior extends AppCompatActivity {

    DBprovider db;
    Boolean connec = false;

    private static String APP_DIRECTORY = "";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY ;
    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int TAKE_POSTERIOR = 300;
    private final int TAKE_LUNETA = 400;
    private final int TAKE_ADICIONAL = 500;
    private final int TAKE_SENSORES = 600;
    private final int TAKE_CAMARA = 700;
    private final int TAKE_COCO = 800;
    private final int TAKE_MUELA = 900;
    private final int TAKE_REMOLQUE = 1000;
    private ImageView mSetImage,imageViewFotoPoE,imageLogoLunetaE,imageFotoAdicionalE,imageSensores,imageCameraPoE,imageCocoPoE,imageMuelaE,imageenChufeRemolque;
    private RelativeLayout mRlView;
    private String mPath;
    private Button btnVolverPoE,btnVolerSecPoE,btnSiguientePoE,btnPosteriorE,btnLogoLunetaE,btnFotoAdiocionalE,btnFotoDanoE,btnSeccionPos1E,seccionPos2E,seccionPos3E;
    private TextView txtPieza,txtTipoDanoE,txtDeducibleE;
    private Spinner spinnerPiezaPoE,spinnerDanoPoE,spinnerDeduciblePoE;
    private String sd;
    private File ruta_sd;
    private String nombre_foto = "";
    private String ruta = "";
    private CheckBox sensoresPoE,camaraPoE,cocoPoE,muelaPoE,enchufeRemolque;
    PropiedadesFoto foto;
    String nombreimagen = "";
    Validaciones validaciones;

    public Posterior(){db = new DBprovider(this);foto=new PropiedadesFoto(this);validaciones = new Validaciones(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vl_posterior);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion = bundle.getString("id_inspeccion");

        connec = new ConexionInternet(this).isConnectingToInternet();

        mRlView = findViewById(R.id.relativeLayout2);
        btnPosteriorE = findViewById((R.id.btnPosteriorE));
        imageViewFotoPoE = findViewById(R.id.imageViewFotoPoE);
        btnLogoLunetaE = findViewById(R.id.btnLogoLunetaE);
        imageLogoLunetaE = findViewById(R.id.imageLogoLunetaE);
        btnFotoAdiocionalE = findViewById(R.id.btnFotoAdiocionalE);
        imageFotoAdicionalE = findViewById(R.id.imageFotoAdicionalE);
        mSetImage = findViewById(R.id.imagenPoDanoE);
        btnFotoDanoE = findViewById((R.id.btnFotoDanoE));
        sensoresPoE = findViewById(R.id.sensoresPoE);
        imageSensores = findViewById(R.id.imageSensores);
        camaraPoE = findViewById(R.id.camaraPoE);
        imageCameraPoE = findViewById(R.id.imageCameraPoE);
        cocoPoE = findViewById(R.id.cocoPoE);
        imageCocoPoE = findViewById(R.id.imageCocoPoE);
        muelaPoE = findViewById(R.id.muelaPoE);
        imageMuelaE = findViewById(R.id.imageMuelaE);
        spinnerPiezaPoE = findViewById(R.id.spinnerPiezaPoE);
        spinnerDanoPoE = findViewById(R.id.spinnerDanoPoE);
        spinnerDeduciblePoE = findViewById(R.id.spinnerDeduciblePoE);
        btnSeccionPos1E = findViewById(R.id.btnSeccionPos1E);
        txtPieza = findViewById(R.id.txtPieza);
        txtTipoDanoE = findViewById(R.id.txtTipoDanoE);
        txtDeducibleE = findViewById(R.id.txtDeducibleE);
        seccionPos2E = findViewById(R.id.seccionPos2E);
        seccionPos3E = findViewById(R.id.seccionPos3E);
        enchufeRemolque = findViewById(R.id.enchufeRemolque);
        imageenChufeRemolque = findViewById(R.id.imageenChufeRemolque);



        /////CUANDO SE SACA LA PRIMERA FOTO SE TIENE QUE GRABAR LA FECHA Y LA HORA

        btnSeccionPos1E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DesplegarCamposSeccionUno(id_inspeccion);
            }
        });
        seccionPos2E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desplegarCamposSeccionDos(id_inspeccion);
            }
        });
        seccionPos3E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desplegarCamposSeccionTres(id_inspeccion);
            }
        });

        btnFotoDanoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptions(id_inspeccion);
            }
        });
        btnPosteriorE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showOptionsbtnPosteriorE(id_inspeccion);
            }
        });
        btnLogoLunetaE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {showOptionsBtnLogoLunetaE(id_inspeccion);
            }
        });

        // CHECKBOX
        sensoresPoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),317));
        sensoresPoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 317).toString().equals("Ok")) {
                        showOptionsCheckSensores(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),317,"");
                    imageSensores.setImageBitmap(null);
                }
            }
        });

        camaraPoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),314));
        camaraPoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 314).toString().equals("Ok")) {
                        showOptionsCheckCamara(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),314,"");
                    imageCameraPoE.setImageBitmap(null);
                }
            }
        });

        cocoPoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),273));
        cocoPoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 273).toString().equals("Ok")) {
                        showOptionsCheckCocoE(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),273,"");
                    imageCocoPoE.setImageBitmap(null);
                }
            }
        });

        muelaPoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),275));
        muelaPoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 275).toString().equals("Ok")) {
                        showOptionsCheckMuelaE(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),275,"");
                    imageMuelaE.setImageBitmap(null);
                }
            }
        });

        enchufeRemolque.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),274));
        enchufeRemolque.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 274).toString().equals("Ok")) {
                        showOptionsCheckEnchufeRemolque(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),274,"");
                    imageenChufeRemolque.setImageBitmap(null);
                }
            }
        });
        btnFotoAdiocionalE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsBtnAdicional(id_inspeccion);
            }
        });


        btnVolverPoE = (Button)findViewById(R.id.btnVolverPoE);
        btnVolverPoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String imagenPosterior = db.foto(Integer.parseInt(id_inspeccion),2);
                String imagenLogoLuneta = db.foto(Integer.parseInt(id_inspeccion),3);
                if(imagenLogoLuneta.length()>4 || imagenPosterior.length()>4){

                    //Toast toast =  Toast.makeText(prueba.this, "Debe continuar realizando la inspección", Toast.LENGTH_SHORT);
                    //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    //toast.show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Posterior.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe continuar realizando la inspección</b>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();


                }
                else{
                    //Intent intent =  new Intent(prueba.this, seccion.class);
                    //startActivity(intent);
                    onBackPressed();

                }

                //Intent intent =  new Intent(prueba.this, seccion.class);
                //startActivity(intent);
            }
        });


        btnSiguientePoE = (Button)findViewById(R.id.btnSiguientePoE);
        btnSiguientePoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imagenPosterior = db.foto(Integer.parseInt(id_inspeccion),2);
                String imagenLogoLuneta = db.foto(Integer.parseInt(id_inspeccion),3);


                if(imagenLogoLuneta.length()<=3){
                    //Toast toast =  Toast.makeText(prueba.this, "Debe Tomar Fotos Obligatorias", Toast.LENGTH_SHORT);
                    //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    //toast.show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Posterior.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Posterior</li><p><li>- Foto Logo Luneta</li></p></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
                else if(imagenPosterior.length()<=3){
                    //Toast toast =  Toast.makeText(prueba.this, "Debe Tomar Fotos Obligatorias", Toast.LENGTH_SHORT);
                    //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    //toast.show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Posterior.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Posterior</li><p><li>- Foto Logo Luneta</li></p></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else{
                    Intent intent   = new Intent(Posterior.this,lateralderecho.class);
                    intent.putExtra("id_inspeccion",id_inspeccion);
                    startActivity(intent);

                }
            }
        });
    }


    private void showOptions(String id_inspeccion){openCamera(id_inspeccion);}
    private void showOptionsbtnPosteriorE(String id_inspeccion){openCamerabtnPosteriorE(id_inspeccion);}
    private void showOptionsBtnLogoLunetaE(String id_inspeccion){openCameraBtnLogoLunetaE(id_inspeccion);}
    private void showOptionsBtnAdicional(String id_inspeccion){openCameraBtnAdicional(id_inspeccion);}
    private void showOptionsCheckSensores(String id_inspeccion){openCameraSensores(id_inspeccion);}
    private void showOptionsCheckCamara(String id_inspeccion){openCameraCamaraPo(id_inspeccion);}
    private void showOptionsCheckCocoE(String id_inspeccion){openCameraCocoE(id_inspeccion);}
    private void showOptionsCheckMuelaE(String id_inspeccion){openCameraMuelaE(id_inspeccion);}
    private void showOptionsCheckEnchufeRemolque(String id_inspeccion){openCameraEnchufeRemolque(id_inspeccion);}

    private void openCamera(String id) {
        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString()+'/'+id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){
            //Long timestamp = System.currentTimeMillis() / 1000;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date();

            String fecha = dateFormat.format(date);
            String imageName = fecha + "_Foto_Dano_Posterior.jpg";
            ruta = file.toString() +"/" +imageName;
            mPath =  ruta ;

            File newFile = new File(mPath);

            Calendar c = Calendar.getInstance();
            nombreimagen = String.valueOf(id_inspeccion)+"_"+ String.valueOf(c.get(Calendar.SECOND))+"_Foto_Dano_Posterior.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,  FileProvider.getUriForFile(Posterior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, PHOTO_CODE);
        }
    }
    private void openCamerabtnPosteriorE(String id) {

        String id_inspeccion = id;
        ruta_sd =Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString()+'/'+id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date();

            String fecha = dateFormat.format(date);
            String imageName = fecha + "_Foto_Posterior.jpg";
            ruta = file.toString() +"/" +imageName;
            mPath =  ruta ;

            File newFile = new File(mPath);

            Calendar c = Calendar.getInstance();
            nombreimagen = String.valueOf(id_inspeccion)+"_"+ String.valueOf(c.get(Calendar.SECOND))+"_Foto_Posterior.jpg";

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(Posterior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, TAKE_POSTERIOR);
        }
    }
    private void openCameraBtnLogoLunetaE(String id) {
        String id_inspeccion = id;
        ruta_sd =Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString()+'/'+id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = new Date();

            String fecha = dateFormat.format(date);
            String imageName = fecha + "_Logo_Luneta_Posterior.jpg";
            ruta = file.toString() +"/" +imageName;
            mPath =  ruta ;

            File newFile = new File(mPath);

            Calendar c = Calendar.getInstance();
            nombreimagen = String.valueOf(id_inspeccion)+"_"+ String.valueOf(c.get(Calendar.SECOND))+"_Logo_Luneta_Posterior.jpg";

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(Posterior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, TAKE_LUNETA);
        }
    }
    private void openCameraBtnAdicional(String id)  {

        String id_inspeccion = id;
        ruta_sd =Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString()+'/'+id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = new Date();

            String fecha = dateFormat.format(date);
            String imageName = fecha+ "_Foto_Adicional_Posterior.jpg";
            ruta = file.toString() +"/" +imageName;
            mPath =  ruta ;

            File newFile = new File(mPath);

            Calendar c = Calendar.getInstance();
            nombreimagen = String.valueOf(id_inspeccion)+"_"+ String.valueOf(c.get(Calendar.SECOND))+"_Foto_Adicional_Posterior.jpg";

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(Posterior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, TAKE_ADICIONAL);
        }
    }
    private void openCameraSensores(String id) {

        String id_inspeccion = id;
        ruta_sd =Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString()+'/'+id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){
            //Long timestamp = System.currentTimeMillis() / 1000;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = new Date();

            String fecha = dateFormat.format(date);
            String imageName = fecha + "_Foto_Sensores_Posterior.jpg";
            ruta = file.toString() +"/" +imageName;
            mPath =  ruta ;

            File newFile = new File(mPath);

            Calendar c = Calendar.getInstance();
            nombreimagen = String.valueOf(id_inspeccion)+"_"+ String.valueOf(c.get(Calendar.SECOND))+"_Foto_Sensores_Posterior.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,  FileProvider.getUriForFile(Posterior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, TAKE_SENSORES);
        }
    }
    private void openCameraCamaraPo(String id) {

        String id_inspeccion = id;
        ruta_sd =Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString()+'/'+id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){
            //Long timestamp = System.currentTimeMillis() / 1000;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = new Date();

            String fecha = dateFormat.format(date);
            String imageName = fecha + "_Foto_Camara_Posterior.jpg";
            ruta = file.toString() +"/" +imageName;
            mPath =  ruta ;

            File newFile = new File(mPath);

            Calendar c = Calendar.getInstance();
            nombreimagen = String.valueOf(id_inspeccion)+"_"+ String.valueOf(c.get(Calendar.SECOND))+"_Foto_Camara_Posterior.jpg";

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,  FileProvider.getUriForFile(Posterior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, TAKE_CAMARA);
        }
    }
    private void openCameraCocoE(String id) {

        String id_inspeccion = id;
        ruta_sd =Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString()+'/'+id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){
            //Long timestamp = System.currentTimeMillis() / 1000;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = new Date();

            String fecha = dateFormat.format(date);
            String imageName = fecha + "_Foto_Coco_Posterior.jpg";
            ruta = file.toString() +"/" +imageName;
            mPath =  ruta ;

            File newFile = new File(mPath);

            Calendar c = Calendar.getInstance();
            nombreimagen = String.valueOf(id_inspeccion)+"_"+ String.valueOf(c.get(Calendar.SECOND))+"_Foto_Coco_Posterior.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,  FileProvider.getUriForFile(Posterior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, TAKE_COCO);
        }
    }
    private void openCameraMuelaE(String id) {

        String id_inspeccion = id;
        ruta_sd =Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString()+'/'+id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){
            //Long timestamp = System.currentTimeMillis() / 1000;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = new Date();

            String fecha = dateFormat.format(date);
            String imageName = fecha + "_Foto_muela.jpg";
            ruta = file.toString() +"/" +imageName;
            mPath =  ruta ;

            File newFile = new File(mPath);

            Calendar c = Calendar.getInstance();
            nombreimagen = String.valueOf(id_inspeccion)+"_"+ String.valueOf(c.get(Calendar.SECOND))+"_Foto_muela.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,  FileProvider.getUriForFile(Posterior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, TAKE_MUELA);
        }
    }
    private void openCameraEnchufeRemolque(String  id)    {
        String id_inspeccion = id;
        ruta_sd =Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString()+'/'+id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){
            //Long timestamp = System.currentTimeMillis() / 1000;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = new Date();

            String fecha = dateFormat.format(date);
            String imageName = fecha + "_Foto_Enchufe_Remolque_Posterior.jpg";
            ruta = file.toString() +"/" +imageName;
            mPath =  ruta ;

            File newFile = new File(mPath);

            Calendar c = Calendar.getInstance();
            nombreimagen = String.valueOf(id_inspeccion)+"_"+ String.valueOf(c.get(Calendar.SECOND))+"_Foto_Enchufe_Remolque_Posterior.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,  FileProvider.getUriForFile(Posterior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, TAKE_REMOLQUE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");

        //CAMBIAR EL ESTADO DE LA INSPECCIÓN A INICIADA PARA PODER VALIDAR DESPUÉS
        db.cambiarEstadoInspeccion(Integer.parseInt(id_inspeccion),1);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PHOTO_CODE:
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
                    mSetImage.setImageBitmap(bitmap);
                    String imagenDano = foto.convertirImagenDano(bitmap);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),1,nombreimagen, "Daño Posterior",0,imagenDano);

                        Intent servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("id_foto","1");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;

                case TAKE_POSTERIOR:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapPosterio = BitmapFactory.decodeFile(mPath);
                    bitmapPosterio = foto.redimensiomarImagen(bitmapPosterio);
                    imageViewFotoPoE.setImageBitmap(bitmapPosterio);
                    String imagenPosterio = foto.convertirImagenDano(bitmapPosterio);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),2,nombreimagen, "Posterior",0,imagenPosterio);

                        servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("id_foto","2");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;
                case TAKE_LUNETA:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });


                    Bitmap bitmapLuneta = BitmapFactory.decodeFile(mPath);
                    bitmapLuneta = foto.redimensiomarImagen(bitmapLuneta);
                    imageLogoLunetaE.setImageBitmap(bitmapLuneta);
                    String imagenLuneta = foto.convertirImagenDano(bitmapLuneta);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),3,nombreimagen, "Logo Luneta Posterior",0,imagenLuneta);

                        servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("id_foto","3");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;
                case TAKE_ADICIONAL:
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
                    imageFotoAdicionalE.setImageBitmap(bitmapAdicional);
                    String imagenAdicional = foto.convertirImagenDano(bitmapAdicional);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),4,nombreimagen, "Adicional Posterior",0,imagenAdicional);

                        servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("id_foto","4");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;
                case TAKE_SENSORES:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapSensores = BitmapFactory.decodeFile(mPath);
                    bitmapSensores = foto.redimensiomarImagen(bitmapSensores);
                    imageSensores.setImageBitmap(bitmapSensores);
                    String imagenSensores = foto.convertirImagenDano(bitmapSensores);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),5,nombreimagen, "Sensores Posteriores",0,imagenSensores);
                    db.insertarValor(Integer.parseInt(id_inspeccion),317,"Ok");

                        servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("id_foto","5");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;
                case TAKE_CAMARA:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });


                    Bitmap bitmapCamara = BitmapFactory.decodeFile(mPath);
                    bitmapCamara = foto.redimensiomarImagen(bitmapCamara);
                    imageCameraPoE.setImageBitmap(bitmapCamara);
                    String imagenCamara = foto.convertirImagenDano(bitmapCamara);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),6,nombreimagen, "Camara Posterior",0,imagenCamara);
                    db.insertarValor(Integer.parseInt(id_inspeccion),314,"Ok");

                        servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("id_foto","6");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;
                case TAKE_COCO:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });


                    Bitmap bitmapCoco = BitmapFactory.decodeFile(mPath);
                    bitmapCoco = foto.redimensiomarImagen(bitmapCoco);
                    imageCocoPoE.setImageBitmap(bitmapCoco);
                    String imagenCoco = foto.convertirImagenDano(bitmapCoco);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),7,nombreimagen, "Coco Posterior",0,imagenCoco);
                    db.insertarValor(Integer.parseInt(id_inspeccion),273,"Ok");

                       servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("id_foto","7");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;

                case TAKE_MUELA:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapMuela = BitmapFactory.decodeFile(mPath);
                    bitmapMuela = foto.redimensiomarImagen(bitmapMuela);
                    imageMuelaE.setImageBitmap(bitmapMuela);
                    String imagenMuela = foto.convertirImagenDano(bitmapMuela);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),8,nombreimagen, "Muela Posterior",0,imagenMuela);
                    db.insertarValor(Integer.parseInt(id_inspeccion),275,"Ok");

                        servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("id_foto","8");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;
                case TAKE_REMOLQUE:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapRemolque = BitmapFactory.decodeFile(mPath);
                    bitmapRemolque = foto.redimensiomarImagen(bitmapRemolque);
                    imageenChufeRemolque.setImageBitmap(bitmapRemolque);
                    String imagenRemolque = foto.convertirImagenDano(bitmapRemolque);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),71,nombreimagen, "Enchufe Remolque Posterior",0,imagenRemolque);
                    db.insertarValor(Integer.parseInt(id_inspeccion),274,"Ok");

                        servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("id_foto","71");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;

            }
        }
    }




    private  void DesplegarCamposSeccionUno(String id)    {

        if (btnPosteriorE.getVisibility()==View.VISIBLE)
        {
            btnPosteriorE.setVisibility(View.GONE);
            imageViewFotoPoE.setVisibility(View.GONE);
            imageViewFotoPoE.setImageBitmap(null);
            btnLogoLunetaE.setVisibility(View.GONE);
            imageLogoLunetaE.setVisibility(View.GONE);
            imageLogoLunetaE.setImageBitmap(null);
            btnFotoAdiocionalE.setVisibility(View.GONE);
            imageFotoAdicionalE.setVisibility(View.GONE);
            imageFotoAdicionalE.setImageBitmap(null);

        }
        else
        {

            //seccion dos

            txtPieza.setVisibility(View.GONE);
            txtTipoDanoE.setVisibility(View.GONE);
            txtDeducibleE.setVisibility(View.GONE);
            spinnerPiezaPoE.setVisibility(View.GONE);
            spinnerDanoPoE.setVisibility(View.GONE);
            spinnerDeduciblePoE.setVisibility(View.GONE);
            btnFotoDanoE.setVisibility(View.GONE);
            mSetImage.setVisibility(View.GONE);
            mSetImage.setImageBitmap(null);

            //seccion tres
            sensoresPoE.setVisibility(View.GONE);
            imageSensores.setVisibility(View.GONE);
            imageSensores.setImageBitmap(null);
            camaraPoE.setVisibility(View.GONE);
            imageCameraPoE.setVisibility(View.GONE);
            imageCameraPoE.setImageBitmap(null);
            cocoPoE.setVisibility(View.GONE);
            imageCocoPoE.setVisibility(View.GONE);
            imageCocoPoE.setImageBitmap(null);
            muelaPoE.setVisibility(View.GONE);
            imageMuelaE.setVisibility(View.GONE);
            imageMuelaE.setImageBitmap(null);
            enchufeRemolque.setVisibility(View.GONE);
            imageenChufeRemolque.setVisibility(View.GONE);
            imageenChufeRemolque.setImageBitmap(null);

            String imagenPosterior = db.foto(Integer.parseInt(id),2);
            String imagenLogoLuneta = db.foto(Integer.parseInt(id),3);
            String imagenAdicional = db.foto(Integer.parseInt(id),4);

            if(imagenPosterior.length()>=3 )
            {
                byte[] decodedString = Base64.decode(imagenPosterior, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageViewFotoPoE.setImageBitmap(decodedByte);
            }
            if(imagenLogoLuneta.length()>=3 ) {

                byte[] decodedString = Base64.decode(imagenLogoLuneta, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageLogoLunetaE.setImageBitmap(decodedByte);
            }
            if(imagenAdicional.length()>=3 )
            {
                byte[] decodedString = Base64.decode(imagenAdicional, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageFotoAdicionalE.setImageBitmap(decodedByte);
            }


            btnPosteriorE.setVisibility(View.VISIBLE);
            imageViewFotoPoE.setVisibility(View.VISIBLE);

            btnLogoLunetaE.setVisibility(View.VISIBLE);
            imageLogoLunetaE.setVisibility(View.VISIBLE);

            btnFotoAdiocionalE.setVisibility(View.VISIBLE);
            imageFotoAdicionalE.setVisibility(View.VISIBLE);

        }
    }

    private  void desplegarCamposSeccionDos(String id)    {

        if (txtPieza.getVisibility() == View.VISIBLE) {

            txtPieza.setVisibility(View.GONE);
            txtTipoDanoE.setVisibility(View.GONE);
            txtDeducibleE.setVisibility(View.GONE);
            spinnerPiezaPoE.setVisibility(View.GONE);
            spinnerDanoPoE.setVisibility(View.GONE);
            spinnerDeduciblePoE.setVisibility(View.GONE);
            btnFotoDanoE.setVisibility(View.GONE);
            mSetImage.setVisibility(View.GONE);
            mSetImage.setImageBitmap(null);
        }
        else
        {
            //seccion uno
            btnPosteriorE.setVisibility(View.GONE);
            imageViewFotoPoE.setVisibility(View.GONE);
            imageViewFotoPoE.setImageBitmap(null);
            btnLogoLunetaE.setVisibility(View.GONE);
            imageLogoLunetaE.setVisibility(View.GONE);
            imageLogoLunetaE.setImageBitmap(null);
            btnFotoAdiocionalE.setVisibility(View.GONE);
            imageFotoAdicionalE.setVisibility(View.GONE);
            imageFotoAdicionalE.setImageBitmap(null);

            //seccion tres
            sensoresPoE.setVisibility(View.GONE);
            imageSensores.setVisibility(View.GONE);
            imageSensores.setImageBitmap(null);
            camaraPoE.setVisibility(View.GONE);
            imageCameraPoE.setVisibility(View.GONE);
            imageCameraPoE.setImageBitmap(null);
            cocoPoE.setVisibility(View.GONE);
            imageCocoPoE.setVisibility(View.GONE);
            imageCocoPoE.setImageBitmap(null);
            muelaPoE.setVisibility(View.GONE);
            imageMuelaE.setVisibility(View.GONE);
            imageMuelaE.setImageBitmap(null);
            enchufeRemolque.setVisibility(View.GONE);
            imageenChufeRemolque.setVisibility(View.GONE);
            imageenChufeRemolque.setImageBitmap(null);

            String imagenDanoPosterior = db.foto(Integer.parseInt(id),1);

            if(imagenDanoPosterior.length()>=3 )
            {
                byte[] decodedString = Base64.decode(imagenDanoPosterior, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                mSetImage.setImageBitmap(decodedByte);
            }

            ///////SPINNERPIEZA POSTERIOR
            spinnerPiezaPoE = (Spinner) findViewById(R.id.spinnerPiezaPoE);
            String listapieza[][] =db.listaPiezasPosterior();
            String[] listapiezaPosterior = new String[listapieza.length +1 ];
            listapiezaPosterior[0]="Seleccione";
            for (int i = 0; i < listapieza.length; i++) {
                listapiezaPosterior[i+1] = listapieza[i][0];
            }
            ArrayAdapter<String> adapterPosteior = new ArrayAdapter<String>(Posterior.this, android.R.layout.simple_spinner_item, listapiezaPosterior);
            adapterPosteior.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPiezaPoE.setAdapter(adapterPosteior);
            spinnerPiezaPoE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position != 0) {
                        ///////SPINNERDAÑO POSTERIOR
                        spinnerDanoPoE = (Spinner) findViewById(R.id.spinnerDanoPoE);
                        txtTipoDanoE.setVisibility(View.VISIBLE);
                        spinnerDanoPoE.setVisibility(View.VISIBLE);

                        String listaDano[][] = db.listaDanoPosterior();
                        String[] listaDanoPosterior = new String[listaDano.length+1];
                        listaDanoPosterior[0]="Seleccione";
                        for (int i = 0; i < listaDano.length; i++) {
                            listaDanoPosterior[i+1] = listaDano[i][0];
                        }
                        ArrayAdapter<String> adapterDanoPosterior = new ArrayAdapter<String>(Posterior.this, android.R.layout.simple_spinner_item, listaDanoPosterior);
                        adapterDanoPosterior.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDanoPoE.setAdapter(adapterDanoPosterior);
                        spinnerDanoPoE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i !=0) { // 6 -> faltante
                                    String[][] listadedu = db.listaDeduciblesPosterior(spinnerDanoPoE.getSelectedItem().toString());
                                    Spinner spinnerDeduciblePoE = (Spinner) findViewById(R.id.spinnerDeduciblePoE);
                                    txtDeducibleE.setVisibility(View.VISIBLE);
                                    spinnerDeduciblePoE.setVisibility(View.VISIBLE);
                                    String[] spinnerDedu = new String[listadedu.length];
                                    for (int ii = 0; ii < listadedu.length; ii++) {
                                        spinnerDedu[ii] = listadedu[ii][0];
                                    }
                                    ArrayAdapter<String> adapterdedu = new ArrayAdapter<String>(Posterior.this, android.R.layout.simple_spinner_item, spinnerDedu);
                                    adapterdedu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerDeduciblePoE.setAdapter(adapterdedu);
                                    spinnerDeduciblePoE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                            btnFotoDanoE.setVisibility(View.VISIBLE);

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {
                                        }
                                    });
                                }
                                /*}else if(i == 6){
                                    btnFotoDanoE.setVisibility(View.VISIBLE);
                                }*/
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });



            txtPieza.setVisibility(View.VISIBLE);
            spinnerPiezaPoE.setVisibility(View.VISIBLE);
            mSetImage.setVisibility(View.VISIBLE);

        }
    }

    private void desplegarCamposSeccionTres(String id)    {
        if (sensoresPoE.getVisibility() == View.VISIBLE) {

            sensoresPoE.setVisibility(View.GONE);
            imageSensores.setVisibility(View.GONE);
            imageSensores.setImageBitmap(null);
            camaraPoE.setVisibility(View.GONE);
            imageCameraPoE.setVisibility(View.GONE);
            imageCameraPoE.setImageBitmap(null);
            cocoPoE.setVisibility(View.GONE);
            imageCocoPoE.setVisibility(View.GONE);
            imageCocoPoE.setImageBitmap(null);
            muelaPoE.setVisibility(View.GONE);
            imageMuelaE.setVisibility(View.GONE);
            imageMuelaE.setImageBitmap(null);
            enchufeRemolque.setVisibility(View.GONE);
            imageenChufeRemolque.setVisibility(View.GONE);
            imageenChufeRemolque.setImageBitmap(null);
        }
        else
        {
            //seccion uno
            btnPosteriorE.setVisibility(View.GONE);
            imageViewFotoPoE.setVisibility(View.GONE);
            imageViewFotoPoE.setImageBitmap(null);
            btnLogoLunetaE.setVisibility(View.GONE);
            imageLogoLunetaE.setVisibility(View.GONE);
            imageLogoLunetaE.setImageBitmap(null);
            btnFotoAdiocionalE.setVisibility(View.GONE);
            imageFotoAdicionalE.setVisibility(View.GONE);
            imageFotoAdicionalE.setImageBitmap(null);

            //seccion dos

            txtPieza.setVisibility(View.GONE);
            txtTipoDanoE.setVisibility(View.GONE);
            txtDeducibleE.setVisibility(View.GONE);
            spinnerPiezaPoE.setVisibility(View.GONE);
            spinnerDanoPoE.setVisibility(View.GONE);
            spinnerDeduciblePoE.setVisibility(View.GONE);
            btnFotoDanoE.setVisibility(View.GONE);
            mSetImage.setVisibility(View.GONE);
            mSetImage.setImageBitmap(null);


            String imagenSensores = db.foto(Integer.parseInt(id),5);
            String imagenCamara = db.foto(Integer.parseInt(id),6);
            String imagenCoco = db.foto(Integer.parseInt(id),7);
            String imagenMuela = db.foto(Integer.parseInt(id),8);
            String imagenenChufeRemolqu = db.foto(Integer.parseInt(id),71);

            if(imagenSensores.length()>=3 )
            {
                byte[] decodedString = Base64.decode(imagenSensores, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageSensores.setImageBitmap(decodedByte);
                sensoresPoE.setChecked(true);
            }
            if(imagenCamara.length()>=3 )
            {
                byte[] decodedString = Base64.decode(imagenCamara, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageCameraPoE.setImageBitmap(decodedByte);
                camaraPoE.setChecked(true);
            }

            if(imagenCoco.length()>=3 )
            {
                byte[] decodedString = Base64.decode(imagenCoco, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageCocoPoE.setImageBitmap(decodedByte);
                cocoPoE.setChecked(true);
            }

            if(imagenMuela.length()>=3)
            {
                byte[] decodedString = Base64.decode(imagenMuela, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageMuelaE.setImageBitmap(decodedByte);
                muelaPoE.setChecked(true);
            }
            if(imagenenChufeRemolqu.length()>=3)
            {
                byte[] decodedString = Base64.decode(imagenenChufeRemolqu, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageenChufeRemolque.setImageBitmap(decodedByte);
                enchufeRemolque.setChecked(true);
            }

            sensoresPoE.setVisibility(View.VISIBLE);
            imageSensores.setVisibility(View.VISIBLE);
            camaraPoE.setVisibility(View.VISIBLE);
            imageCameraPoE.setVisibility(View.VISIBLE);
            cocoPoE.setVisibility(View.VISIBLE);
            imageCocoPoE.setVisibility(View.VISIBLE);
            muelaPoE.setVisibility(View.VISIBLE);
            imageMuelaE.setVisibility(View.VISIBLE);
            enchufeRemolque.setVisibility(View.VISIBLE);
            imageenChufeRemolque.setVisibility(View.VISIBLE);

        }

    }



}
