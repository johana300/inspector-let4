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
    private final int PHOTO_CODE = 200;
    private final int TAKE_POSTERIOR = 300;
    private final int TAKE_LUNETA = 400;
    private final int TAKE_ADICIONAL = 500;
    private final int TAKE_SENSORES = 600;
    private final int TAKE_CAMARA = 700;
    private final int TAKE_COCO = 800;
    private final int TAKE_MUELA = 900;
    private final int TAKE_REMOLQUE = 1000;

    private final int TAKE_EQFRIO = 1100;
    private final int TAKE_CREFRI = 1200;
    private final int TAKE_CUBREPICK = 1300;
    private final int TAKE_TAPARIGIDA = 1400;
    private final int TAKE_LONACUBRE = 1500;
    private final int TAKE_HERRAMIENTA = 1600;

    private ImageView mSetImage,imageViewFotoPoE,imageLogoLunetaE,imageFotoAdicionalE,imageSensores,imageCameraPoE,imageCocoPoE,imageMuelaE,imageenChufeRemolque,imageCamRePoE,imageCubrePickPoE,imageEquipoE,imageTapaRPoE,imageLonaCPoE,imageCajaHerrPoE;
    private RelativeLayout mRlView;
    private String mPath;
    private Button btnVolverPoE,btnVolerSecPoE,btnSiguientePoE,btnPosteriorE,btnLogoLunetaE,btnFotoAdiocionalE,btnFotoDanoE,btnSeccionPos1E,seccionPos2E,seccionPos3E,seccionPos3EMQ;
    private TextView txtPieza,txtTipoDanoE,txtDeducibleE;
    private Spinner spinnerPiezaPoE,spinnerDanoPoE,spinnerDeduciblePoE;
    private String sd;
    private File ruta_sd;
    private String nombre_foto = "";
    private String ruta = "";
    private CheckBox sensoresPoE,camaraPoE,cocoPoE,muelaPoE,enchufeRemolque,camaraRefriPoE,cubrePickPoE,equipoPoE,tapaRiPoE,LonaCubrePoE,cajaHerrPoE ;
    PropiedadesFoto foto;
    String nombreimagen = "", comentarioDañoImg="";
    Validaciones validaciones;
    int correlativo = 0;
    String dañosDedu[][];


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
        seccionPos3EMQ = findViewById(R.id.seccionPos3MQ);
        enchufeRemolque = findViewById(R.id.enchufeRemolque);
        imageenChufeRemolque = findViewById(R.id.imageenChufeRemolque);
        camaraRefriPoE = findViewById(R.id.camaraRefriPoE);

        //accesorios faltantes
        imageCamRePoE = findViewById(R.id.imageCamRePoE);
        cubrePickPoE = findViewById(R.id.cubrePickPoE);
        imageCubrePickPoE = findViewById(R.id.imageCubrePickPoE);
        equipoPoE = findViewById(R.id.equipoPoE);
        imageEquipoE = findViewById(R.id.imageEquipoE);
        tapaRiPoE = findViewById(R.id.tapaRiPoE);
        imageTapaRPoE = findViewById(R.id.imageTapaRPoE);
        LonaCubrePoE = findViewById(R.id.LonaCubrePoE);
        imageLonaCPoE = findViewById(R.id.imageLonaCPoE);
        cajaHerrPoE = findViewById(R.id.cajaHerrPoE);
        imageCajaHerrPoE = findViewById(R.id.imageCajaHerrPoE);




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
        seccionPos3EMQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desplegarCamposSeccionTresMQ(id_inspeccion);
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


        //accesorios faltantes

        camaraRefriPoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),331));
        camaraRefriPoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 331).toString().equals("Ok")) {
                        showOptionRefrigerado(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),331,"");
                    imageCamRePoE.setImageBitmap(null);
                }
            }
        });


        equipoPoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),346));
        equipoPoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 346).toString().equals("Ok")) {
                        showOptionsEquipoFrio(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),346,"");
                    imageEquipoE.setImageBitmap(null);
                }
            }
        });

        cubrePickPoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),305));
        cubrePickPoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 305).toString().equals("Ok")) {
                        showOptionCubrePickup(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),305,"");
                    imageCubrePickPoE.setImageBitmap(null);
                }
            }
        });

        tapaRiPoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),307));
        tapaRiPoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 307).toString().equals("Ok")) {
                        showOptionTapaRigida(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),307,"");
                    imageTapaRPoE.setImageBitmap(null);
                }
            }
        });

        LonaCubrePoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),306));
        LonaCubrePoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 306).toString().equals("Ok")) {
                        showOptionLonaCubrePickup(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),306,"");
                    imageLonaCPoE.setImageBitmap(null);
                }
            }
        });

        cajaHerrPoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),310));
        cajaHerrPoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 310).toString().equals("Ok")) {
                        showOptionCajaHerramientas(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),310,"");
                    imageCajaHerrPoE.setImageBitmap(null);
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

                String imagenPosterior = db.foto(Integer.parseInt(id_inspeccion),"Posterior");
                String imagenLogoLuneta = db.foto(Integer.parseInt(id_inspeccion),"Logo Luneta Posterior");
                if(imagenLogoLuneta.length()>4 || imagenPosterior.length()>4){

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
                String imagenPosterior = db.foto(Integer.parseInt(id_inspeccion),"Posterior");
                String imagenLogoLuneta = db.foto(Integer.parseInt(id_inspeccion),"Logo Luneta Posterior");


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
                    finish();

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

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Dano_Posterior.jpg";


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

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Posterior.jpg";

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

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Logo_Luneta_Posterior.jpg";

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

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Adicional_Posterior.jpg";

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

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Sensores_Posterior.jpg";


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

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Camara_Posterior.jpg";

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

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Coco_Posterior.jpg";


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

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_muela.jpg";


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

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Enchufe_Remolque_Posterior.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,  FileProvider.getUriForFile(Posterior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, TAKE_REMOLQUE);
        }
    }


    //accesorios faltantes
    private void showOptionsEquipoFrio(String id_inspeccion){openCameraEquipoFrio(id_inspeccion);}
    private void openCameraEquipoFrio(String id){
        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString()+'/'+id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated) {
            //Long timestamp = System.currentTimeMillis() / 1000;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date();

            String fecha = dateFormat.format(date);
            String imageName = fecha + "_Foto_Equipo_Frio.jpg";
            ruta = file.toString() +"/" +imageName;
            mPath =  ruta ;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Equipo_Frio.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,  FileProvider.getUriForFile(Posterior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, TAKE_EQFRIO);
        }
    }

    private void showOptionRefrigerado(String id_inspeccion){openCameraRefrigerada(id_inspeccion);}
    private void openCameraRefrigerada(String id){
        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString()+'/'+id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated) {
            //Long timestamp = System.currentTimeMillis() / 1000;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date();

            String fecha = dateFormat.format(date);
            String imageName = fecha + "_Foto_Refrigerada.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion) + "_" + String.valueOf(correlativo) + "_Foto_Refrigerada.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(Posterior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, TAKE_CREFRI);
        }
    }

    private void showOptionCubrePickup(String id_inspeccion){openCameraCubrePickup(id_inspeccion);}
    private void openCameraCubrePickup(String id){
        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString()+'/'+id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated) {
            //Long timestamp = System.currentTimeMillis() / 1000;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date();

            String fecha = dateFormat.format(date);
            String imageName = fecha + "_Foto_Cubre_Pick_up.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion) + "_" + String.valueOf(correlativo) + "_Foto_Cubre_Pick_up.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(Posterior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, TAKE_CUBREPICK);
        }
    }

    private void showOptionTapaRigida(String id_inspeccion){openCameraTapaRigida(id_inspeccion);}
    private void openCameraTapaRigida(String id){
        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString()+'/'+id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated) {
            //Long timestamp = System.currentTimeMillis() / 1000;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date();

            String fecha = dateFormat.format(date);
            String imageName = fecha + "_Foto_Tapa_Rigida.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion) + "_" + String.valueOf(correlativo) + "_Foto_Tapa_Rigida.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(Posterior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, TAKE_TAPARIGIDA);
        }
    }

    private void showOptionLonaCubrePickup(String id_inspeccion){openCameraLonaCubrepickup(id_inspeccion);}
    private void openCameraLonaCubrepickup(String id){
        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString()+'/'+id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated) {
            //Long timestamp = System.currentTimeMillis() / 1000;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date();

            String fecha = dateFormat.format(date);
            String imageName = fecha + "_Foto_Lona_cubre.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion) + "_" + String.valueOf(correlativo) + "_Foto_Lona_cubre.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(Posterior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, TAKE_LONACUBRE);
        }
    }

    private void showOptionCajaHerramientas(String id_inspeccion){openCameraCajaHerramientas(id_inspeccion);}
    private void openCameraCajaHerramientas(String id){
        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString()+'/'+id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated) {
            //Long timestamp = System.currentTimeMillis() / 1000;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date();

            String fecha = dateFormat.format(date);
            String imageName = fecha + "_Foto_caja_herramientas.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion) + "_" + String.valueOf(correlativo) + "_Foto_caja_herramientas.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(Posterior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            startActivityForResult(intent, TAKE_HERRAMIENTA);
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


                    dañosDedu = db.DeduciblePieza(spinnerPiezaPoE.getSelectedItem().toString(), "posterior");
                    //daño
                    db.insertarValor(Integer.parseInt(id_inspeccion),Integer.parseInt(dañosDedu[0][0]),String.valueOf(db.obtenerDanio(spinnerDanoPoE.getSelectedItem().toString())));
                    //deducible
                    db.insertarValor(Integer.parseInt(id_inspeccion),Integer.parseInt(dañosDedu[0][1]),db.obtenerDeducible(db.obtenerDanio(spinnerDanoPoE.getSelectedItem().toString()),spinnerDeduciblePoE.getSelectedItem().toString()));

                    comentarioDañoImg = spinnerPiezaPoE.getSelectedItem().toString()+' '+spinnerDanoPoE.getSelectedItem().toString()+' '+spinnerDeduciblePoE.getSelectedItem().toString()+' ';
                    db.insertarComentarioFoto(Integer.parseInt(id_inspeccion),comentarioDañoImg,"posterior");
                    String comentarito = db.comentarioFoto(Integer.parseInt(id_inspeccion),"posterior");

                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, comentarito,0,imagenDano);

                    Intent servis = new Intent(Posterior.this, TransferirFoto.class);
                    servis.putExtra("comentario",comentarito);
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
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Posterior",0,imagenPosterio);

                        servis = new Intent(Posterior.this, TransferirFoto.class);
                        servis.putExtra("comentario","Posterior");
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
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Logo Luneta Posterior",0,imagenLuneta);

                        servis = new Intent(Posterior.this, TransferirFoto.class);
                    servis.putExtra("comentario","Logo Luneta Posterior");
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
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Adicional Posterior",0,imagenAdicional);

                        servis = new Intent(Posterior.this, TransferirFoto.class);
                    servis.putExtra("comentario","Adicional Posterior");
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
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Sensores Posteriores",0,imagenSensores);
                    db.insertarValor(Integer.parseInt(id_inspeccion),317,"Ok");

                        servis = new Intent(Posterior.this, TransferirFoto.class);
                    servis.putExtra("comentario","Sensores Posteriores");
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
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Camara Posterior",0,imagenCamara);
                    db.insertarValor(Integer.parseInt(id_inspeccion),314,"Ok");

                        servis = new Intent(Posterior.this, TransferirFoto.class);
                    servis.putExtra("comentario","Camara Posterior");
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
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Coco Posterior",0,imagenCoco);
                    db.insertarValor(Integer.parseInt(id_inspeccion),273,"Ok");

                       servis = new Intent(Posterior.this, TransferirFoto.class);
                    servis.putExtra("comentario","Coco Posterior");
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
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Muela Posterior",0,imagenMuela);
                    db.insertarValor(Integer.parseInt(id_inspeccion),275,"Ok");

                        servis = new Intent(Posterior.this, TransferirFoto.class);
                    servis.putExtra("comentario","Muela Posterior");
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
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Enchufe Remolque Posterior",0,imagenRemolque);
                    db.insertarValor(Integer.parseInt(id_inspeccion),274,"Ok");

                        servis = new Intent(Posterior.this, TransferirFoto.class);
                    servis.putExtra("comentario","Enchufe Remolque Posterior");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;

                    //accesorios faltantes

                case TAKE_CREFRI:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapCrefrigerada = BitmapFactory.decodeFile(mPath);
                    bitmapCrefrigerada = foto.redimensiomarImagen(bitmapCrefrigerada);
                    imageCamRePoE.setImageBitmap(bitmapCrefrigerada);
                    String imagenCrefrigerada = foto.convertirImagenDano(bitmapCrefrigerada);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Camara refrigerada",0,imagenCrefrigerada);
                    db.insertarValor(Integer.parseInt(id_inspeccion),331,"Ok");

                    servis = new Intent(Posterior.this, TransferirFoto.class);
                    servis.putExtra("comentario","Camara refrigerada");
                    servis.putExtra("id_inspeccion",id_inspeccion);
                    startService(servis);

                    break;

                case TAKE_EQFRIO:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapEquipoFrio = BitmapFactory.decodeFile(mPath);
                    bitmapEquipoFrio = foto.redimensiomarImagen(bitmapEquipoFrio);
                    imageEquipoE.setImageBitmap(bitmapEquipoFrio);
                    String imagenEQuipoFrio = foto.convertirImagenDano(bitmapEquipoFrio);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Equipo frio",0,imagenEQuipoFrio);
                    db.insertarValor(Integer.parseInt(id_inspeccion),346,"Ok");

                    servis = new Intent(Posterior.this, TransferirFoto.class);
                    servis.putExtra("comentario","Equipo frio");
                    servis.putExtra("id_inspeccion",id_inspeccion);
                    startService(servis);

                    break;



                case TAKE_CUBREPICK:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapCubrePick = BitmapFactory.decodeFile(mPath);
                    bitmapCubrePick = foto.redimensiomarImagen(bitmapCubrePick);
                    imageCubrePickPoE.setImageBitmap(bitmapCubrePick);
                    String imagenCubrepic = foto.convertirImagenDano(bitmapCubrePick);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Cubre pick up",0,imagenCubrepic);
                    db.insertarValor(Integer.parseInt(id_inspeccion),305,"Ok");

                    servis = new Intent(Posterior.this, TransferirFoto.class);
                    servis.putExtra("comentario","Cubre pick up");
                    servis.putExtra("id_inspeccion",id_inspeccion);
                    startService(servis);

                    break;


                case TAKE_TAPARIGIDA:

                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapTaparig = BitmapFactory.decodeFile(mPath);
                    bitmapTaparig = foto.redimensiomarImagen(bitmapTaparig);
                    imageTapaRPoE.setImageBitmap(bitmapTaparig);
                    String imagenTaparig = foto.convertirImagenDano(bitmapTaparig);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Tapa rigida",0,imagenTaparig);
                    db.insertarValor(Integer.parseInt(id_inspeccion),307,"Ok");

                    servis = new Intent(Posterior.this, TransferirFoto.class);
                    servis.putExtra("comentario","Tapa rigida");
                    servis.putExtra("id_inspeccion",id_inspeccion);
                    startService(servis);

                    break;

                case TAKE_LONACUBRE:

                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapLonacubre = BitmapFactory.decodeFile(mPath);
                    bitmapLonacubre = foto.redimensiomarImagen(bitmapLonacubre);
                    imageLonaCPoE.setImageBitmap(bitmapLonacubre);
                    String imagenLonacubre = foto.convertirImagenDano(bitmapLonacubre);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Lona cubre",0,imagenLonacubre);
                    db.insertarValor(Integer.parseInt(id_inspeccion),306,"Ok");

                    servis = new Intent(Posterior.this, TransferirFoto.class);
                    servis.putExtra("comentario","Lona cubre");
                    servis.putExtra("id_inspeccion",id_inspeccion);
                    startService(servis);

                    break;

                case TAKE_HERRAMIENTA:

                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapHarramienta = BitmapFactory.decodeFile(mPath);
                    bitmapHarramienta = foto.redimensiomarImagen(bitmapHarramienta);
                    imageCajaHerrPoE.setImageBitmap(bitmapHarramienta);
                    String imagenHerramienta = foto.convertirImagenDano(bitmapHarramienta);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "caja herramienta",0,imagenHerramienta);
                    db.insertarValor(Integer.parseInt(id_inspeccion),310,"Ok");

                    servis = new Intent(Posterior.this, TransferirFoto.class);
                    servis.putExtra("comentario","caja herramienta");
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

            //Seccion tres mq
            equipoPoE.setVisibility(View.GONE);
            imageEquipoE.setImageBitmap(null);
            camaraRefriPoE.setVisibility(View.GONE);
            imageCamRePoE.setImageBitmap(null);
            cubrePickPoE.setVisibility(View.GONE);
            imageCubrePickPoE.setImageBitmap(null);
            tapaRiPoE.setVisibility(View.GONE);
            imageTapaRPoE.setImageBitmap(null);
            LonaCubrePoE.setVisibility(View.GONE);
            imageLonaCPoE.setImageBitmap(null);
            cajaHerrPoE.setVisibility(View.GONE);
            imageCajaHerrPoE.setImageBitmap(null);

            String imagenPosterior = db.foto(Integer.parseInt(id),"Posterior");
            String imagenLogoLuneta = db.foto(Integer.parseInt(id),"Logo Luneta Posterior");
            String imagenAdicional = db.foto(Integer.parseInt(id),"Adicional Posterior");

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

            //Seccion tres mq
            equipoPoE.setVisibility(View.GONE);
            imageEquipoE.setImageBitmap(null);
            camaraRefriPoE.setVisibility(View.GONE);
            imageCamRePoE.setImageBitmap(null);
            cubrePickPoE.setVisibility(View.GONE);
            imageCubrePickPoE.setImageBitmap(null);
            tapaRiPoE.setVisibility(View.GONE);
            imageTapaRPoE.setImageBitmap(null);
            LonaCubrePoE.setVisibility(View.GONE);
            imageLonaCPoE.setImageBitmap(null);
            cajaHerrPoE.setVisibility(View.GONE);
            imageCajaHerrPoE.setImageBitmap(null);

            String imagenDanoPosterior = db.foto(Integer.parseInt(id),db.comentarioFoto(Integer.parseInt(id),"posterior"));

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
                                    final Spinner spinnerDeduciblePoE = (Spinner) findViewById(R.id.spinnerDeduciblePoE);
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

            //Seccion tres mq
            equipoPoE.setVisibility(View.GONE);
            imageEquipoE.setImageBitmap(null);
            camaraRefriPoE.setVisibility(View.GONE);
            imageCamRePoE.setImageBitmap(null);
            cubrePickPoE.setVisibility(View.GONE);
            imageCubrePickPoE.setImageBitmap(null);
            tapaRiPoE.setVisibility(View.GONE);
            imageTapaRPoE.setImageBitmap(null);
            LonaCubrePoE.setVisibility(View.GONE);
            imageLonaCPoE.setImageBitmap(null);
            cajaHerrPoE.setVisibility(View.GONE);
            imageCajaHerrPoE.setImageBitmap(null);


            String imagenSensores = db.foto(Integer.parseInt(id),"Sensores Posteriores");
            String imagenCamara = db.foto(Integer.parseInt(id),"Camara Posterior");
            String imagenCoco = db.foto(Integer.parseInt(id),"Coco Posterior");
            String imagenMuela = db.foto(Integer.parseInt(id),"Muela Posterior");
            String imagenenChufeRemolqu = db.foto(Integer.parseInt(id),"Enchufe Remolque Posterior");

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

    private void desplegarCamposSeccionTresMQ(String id){


        if (equipoPoE.getVisibility()==View.VISIBLE){

            equipoPoE.setVisibility(View.GONE);
            imageEquipoE.setImageBitmap(null);
            camaraRefriPoE.setVisibility(View.GONE);
            imageCamRePoE.setImageBitmap(null);
            cubrePickPoE.setVisibility(View.GONE);
            imageCubrePickPoE.setImageBitmap(null);
            tapaRiPoE.setVisibility(View.GONE);
            imageTapaRPoE.setImageBitmap(null);
            LonaCubrePoE.setVisibility(View.GONE);
            imageLonaCPoE.setImageBitmap(null);
            cajaHerrPoE.setVisibility(View.GONE);
            imageCajaHerrPoE.setImageBitmap(null);

        }else {


            String imagenEquipoFrio = db.foto(Integer.parseInt(id),"Equipo frio");
            String imagenCamaraRefrigerada = db.foto(Integer.parseInt(id),"Camara refrigerada");
            String imagenCubrepickup = db.foto(Integer.parseInt(id),"Cubre pick up");
            String imagenTapaRigida = db.foto(Integer.parseInt(id),"Tapa rigida");
            String imagenLonaCubre = db.foto(Integer.parseInt(id),"Lona cubre");
            String imagenCajaHerramientas = db.foto(Integer.parseInt(id),"caja herramienta");


            if(imagenCajaHerramientas.length()>=3)
            {
                byte[] decodedString = Base64.decode(imagenCajaHerramientas, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageCajaHerrPoE.setImageBitmap(decodedByte);
                cajaHerrPoE.setChecked(true);
            }
            if(imagenLonaCubre.length()>=3)
            {
                byte[] decodedString = Base64.decode(imagenLonaCubre, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageLonaCPoE.setImageBitmap(decodedByte);
                LonaCubrePoE.setChecked(true);
            }
            if(imagenTapaRigida.length()>=3)
            {
                byte[] decodedString = Base64.decode(imagenTapaRigida, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageTapaRPoE.setImageBitmap(decodedByte);
                tapaRiPoE.setChecked(true);
            }
            if(imagenCubrepickup.length()>=3)
            {
                byte[] decodedString = Base64.decode(imagenCubrepickup, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageCubrePickPoE.setImageBitmap(decodedByte);
                cubrePickPoE.setChecked(true);
            }
            if(imagenCamaraRefrigerada.length()>=3)
            {
                byte[] decodedString = Base64.decode(imagenCamaraRefrigerada, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageCamRePoE.setImageBitmap(decodedByte);
                camaraRefriPoE.setChecked(true);
            }
            if(imagenEquipoFrio.length()>=3)
            {
                byte[] decodedString = Base64.decode(imagenEquipoFrio, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageEquipoE.setImageBitmap(decodedByte);
                equipoPoE.setChecked(true);
            }


            equipoPoE.setVisibility(View.VISIBLE);
            imageEquipoE.setVisibility(View.VISIBLE);
            camaraRefriPoE.setVisibility(View.VISIBLE);
            imageCamRePoE.setVisibility(View.VISIBLE);
            cubrePickPoE.setVisibility(View.VISIBLE);
            imageCubrePickPoE.setVisibility(View.VISIBLE);
            tapaRiPoE.setVisibility(View.VISIBLE);
            imageTapaRPoE.setVisibility(View.VISIBLE);
            LonaCubrePoE.setVisibility(View.VISIBLE);
            imageLonaCPoE.setVisibility(View.VISIBLE);
            cajaHerrPoE.setVisibility(View.VISIBLE);
            imageCajaHerrPoE.setVisibility(View.VISIBLE);


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
        }
    }




}
