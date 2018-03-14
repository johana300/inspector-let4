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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.BuildConfig;
import com.letchile.let.Clases.PropiedadesFoto;
import com.letchile.let.Clases.Validaciones;
import com.letchile.let.R;
import com.letchile.let.Servicios.ConexionInternet;
import com.letchile.let.Servicios.TransferirFoto;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class frontal extends AppCompatActivity {

    DBprovider db;
    Boolean connec = false;
    private final int PHOTO_FRONTAL = 200;
    private final int PHOTO_PARABRISAS = 300;
    private final int PHOTO_ADICIONAL = 400;
    private final int PHOTO_DANO = 500;
    private final int PHOTO_TURBO = 600;
    private final int PHOTO_HUENCHE = 700;
    private final int PHOTO_COCO = 800;
    private final int PHOTO_NEBLINERO = 900;
    private final int PHOTO_LOGO = 1000;
    private Button btnVolverFrontalE,btnVolerSecFrontalE,btnSiguienteFrontalE,btnFrontalE,btnLogoParaFrontalE,btnAdicionalFrontalE,btnFrontalDanoE,btnSeccionUnoFrontalE,btnSeccionDosFrontalE,brnSeccionFos3E;
    private ImageView imageFrontalE,imageLogoParaE,imageAdicionalFrontalE,imagenFrontalDanoE,imageTurboFrontalE,imageHuencheFrontalE,imageCocoFrE,imageNeblineroE,imageLogoE;
    private CheckBox sistemaTurboFrontalE,huincheFoE,CocoFoE,neblinerosFoE,logoFoE;
    private Spinner spinnerPiezaFrontalE,spinnerDanoFrontalE,spinnerDeducibleFrontalE;
    private TextView txtPieza,txtTipoDanoE,txtDeducibleE;
    private String mPath;
    private File ruta_sd;
    private String ruta = "";
    PropiedadesFoto foto;
    String nombreimagen = "",comentarioDañoImg="";
    Validaciones validaciones;
    int correlativo = 0;
    String dañosDedu[][];

    public frontal(){db = new DBprovider(this);foto=new PropiedadesFoto(this);validaciones = new Validaciones(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontal);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion = bundle.getString("id_inspeccion");

        connec = new ConexionInternet(this).isConnectingToInternet();


        btnFrontalE = findViewById(R.id.btnFrontalE);
        imageFrontalE = findViewById(R.id.imageFrontalE);
        btnLogoParaFrontalE = findViewById(R.id.btnLogoParaFrontalE);
        imageLogoParaE = findViewById(R.id.imageLogoParaE);
        btnAdicionalFrontalE = findViewById(R.id.btnAdicionalFrontalE);
        imageAdicionalFrontalE = findViewById(R.id.imageAdicionalFrontalE);
        btnFrontalDanoE = findViewById(R.id.btnFrontalDanoE);
        imagenFrontalDanoE  = findViewById(R.id.imagenFrontalDanoE);
        sistemaTurboFrontalE = findViewById(R.id.sistemaTurboFrontalE);
        imageTurboFrontalE = findViewById(R.id.imageTurboFrontalE);
        huincheFoE = findViewById(R.id.huincheFoE);
        imageHuencheFrontalE = findViewById(R.id.imageHuencheFrontalE);
        CocoFoE = findViewById(R.id.CocoFoE);
        imageCocoFrE = findViewById(R.id.imageCocoFrE);
        neblinerosFoE = findViewById(R.id.neblinerosFoE);
        imageNeblineroE = findViewById(R.id.imageNeblineroE);
        logoFoE = findViewById(R.id.logoFoE);
        imageLogoE = findViewById(R.id.imageLogoE);
        btnSeccionUnoFrontalE = findViewById(R.id.btnSeccionUnoFrontalE);
        btnSeccionDosFrontalE = findViewById(R.id.btnSeccionDosFrontalE);
        spinnerPiezaFrontalE = findViewById(R.id.spinnerPiezaFrontalE);
        spinnerDanoFrontalE = findViewById(R.id.spinnerDanoFrontalE);
        spinnerDeducibleFrontalE = findViewById(R.id.spinnerDeducibleFrontalE);
        txtPieza = findViewById(R.id.txtPieza);
        txtTipoDanoE = findViewById(R.id.txtTipoDanoE);
        txtDeducibleE = findViewById(R.id.txtDeducibleE);
        brnSeccionFos3E = findViewById(R.id.brnSeccionFos3E);


        btnSeccionUnoFrontalE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {DesplegarCamposSeccionUno(id_inspeccion); }
        });
        btnSeccionDosFrontalE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {desplegarCamposSeccionDos(id_inspeccion); }
        });
        brnSeccionFos3E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {desplegarCamposSeccionTres(id_inspeccion);
            }
        });


        btnFrontalE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openCamaraFrontal(id_inspeccion);   }
        });
        btnLogoParaFrontalE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openCamaraParabrisas(id_inspeccion);
            }
        });
        btnAdicionalFrontalE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  openCamaraAdioionalFrontal(id_inspeccion);}
        });
        btnFrontalDanoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openCamaraDanoFrontal(id_inspeccion);
            }
        });




        sistemaTurboFrontalE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),354));
        sistemaTurboFrontalE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 354).toString().equals("Ok")) {
                        openCamaraSistemaTurbo(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),354,"");
                    imageTurboFrontalE.setImageBitmap(null);
                }
            }
        });

        huincheFoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),288));
        huincheFoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 288).toString().equals("Ok")) {
                        openCamaraHuenche(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),288,"");
                    imageHuencheFrontalE.setImageBitmap(null);
                }
            }
        });

        CocoFoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),316));
        CocoFoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 316).toString().equals("Ok")) {
                        openCamaraCoco(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),316,"");
                    imageCocoFrE.setImageBitmap(null);
                }
            }
        });

        neblinerosFoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),286));
        neblinerosFoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 286).toString().equals("Ok")) {
                        openCamaraNeblinero(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),286,"");
                    imageNeblineroE.setImageBitmap(null);
                }
            }
        });

        logoFoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),260));
        logoFoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 260).toString().equals("Ok")) {
                        openCamaraLogo(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),260,"");
                    imageLogoE.setImageBitmap(null);
                }

            }
        });

        btnVolverFrontalE = (Button)findViewById(R.id.btnVolverFrontalE);
        btnVolverFrontalE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnSiguienteFrontalE = (Button)findViewById(R.id.btnSiguienteFrontalE);
        btnSiguienteFrontalE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String imageFrontal = db.foto(Integer.parseInt(id_inspeccion),"Foto Frontal");
                String imageLogoPara = db.foto(Integer.parseInt(id_inspeccion),"Logo Parabrisas Frontal");

                if(imageFrontal.length()<4 ||imageLogoPara.length()<4 )
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(frontal.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Frontal</li><p><li>- Foto Logo Parabrisas</li></p></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                    Intent intent = new Intent(frontal.this, lateralizquierdo.class);
                    intent.putExtra("id_inspeccion",id_inspeccion);
                    startActivity(intent);
                }
            }
        });

    }

    private void openCamaraFrontal(String id) {

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
            String imageName = fecha + "_Foto_Frontal.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Frontal.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(frontal.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));

            startActivityForResult(intent, PHOTO_FRONTAL);
        }
    }
    private void openCamaraParabrisas(String id) {

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
            String imageName = fecha + "_Foto_Parabrisas.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Parabrisas.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(frontal.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_PARABRISAS);
        }
    }
    private void openCamaraAdioionalFrontal(String id) {

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
            String imageName = fecha + "_Foto_Adicional_Frontal.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Adicional_Frontal.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(frontal.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_ADICIONAL);
        }
    }
    private void openCamaraDanoFrontal(String id) {

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
            String imageName = fecha + "_Foto_Dano_Frontal.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Dano_Frontal.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(frontal.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_DANO);
        }
    }
    private void openCamaraSistemaTurbo(String id) {

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
            String imageName = fecha + "_Foto_Sistema_Turbo.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Sistema_Turbo.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(frontal.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_TURBO);
        }
    }
    private void openCamaraHuenche(String id) {

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
            String imageName = fecha + "_Foto_Huenche_Frontal.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Huenche_Frontal.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(frontal.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_HUENCHE);
        }
    }
    private void openCamaraCoco(String id) {

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
            String imageName = fecha + "_Foto_Coco_Frontal.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Coco_Frontal.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(frontal.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_COCO);
        }
    }
    private void openCamaraNeblinero(String id) {

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
            String imageName = fecha + "_Foto_Neblinero_Frontal.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Neblinero_Frontal.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(frontal.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_NEBLINERO);
        }
    }
    private void openCamaraLogo(String id) {

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
            String imageName = fecha + "_Foto_Logo_Frontal.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Logo_Frontal.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(frontal.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_LOGO);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_FRONTAL:
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
                    imageFrontalE.setImageBitmap(bitmap);
                    String imagen = foto.convertirImagenDano(bitmap);
                    db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Frontal", 0, imagen);




                        Intent servis = new Intent(frontal.this, TransferirFoto.class);
                    servis.putExtra("comentario","Foto Frontal");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;

                case PHOTO_PARABRISAS:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapParabrisas = BitmapFactory.decodeFile(mPath);
                    bitmapParabrisas = foto.redimensiomarImagen(bitmapParabrisas);
                    imageLogoParaE.setImageBitmap(bitmapParabrisas);
                    String imagenParabrisas = foto.convertirImagenDano(bitmapParabrisas);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Logo Parabrisas Frontal",0,imagenParabrisas);

                        servis = new Intent(frontal.this, TransferirFoto.class);
                    servis.putExtra("comentario","Logo Parabrisas Frontal");
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


                    Bitmap bitmapAdcional = BitmapFactory.decodeFile(mPath);
                    bitmapAdcional = foto.redimensiomarImagen(bitmapAdcional);
                    imageAdicionalFrontalE.setImageBitmap(bitmapAdcional);
                    String imagenAdicional = foto.convertirImagenDano(bitmapAdcional);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Adicional Frontal",0,imagenAdicional);

                         servis = new Intent(frontal.this, TransferirFoto.class);
                    servis.putExtra("comentario","Adicional Frontal");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;
                case PHOTO_DANO:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapDano = BitmapFactory.decodeFile(mPath);
                    bitmapDano = foto.redimensiomarImagen(bitmapDano);
                    imagenFrontalDanoE.setImageBitmap(bitmapDano);
                    String imagendano = foto.convertirImagenDano(bitmapDano);




                    comentarioDañoImg = spinnerPiezaFrontalE.getSelectedItem().toString()+' '+spinnerDanoFrontalE.getSelectedItem().toString()+' '+spinnerDeducibleFrontalE.getSelectedItem().toString()+' ';
                    db.insertarComentarioFoto(Integer.parseInt(id_inspeccion),comentarioDañoImg,"frontal");
                    String comentarito = db.comentarioFoto(Integer.parseInt(id_inspeccion),"frontal");

                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, comentarito,0,imagendano);


                    dañosDedu = db.DeduciblePieza(spinnerPiezaFrontalE.getSelectedItem().toString(), "frontal");
                    //danioPo=db.Deducible(spinnerDeduciblePoE.getSelectedItem().toString());

                    //daño
                    db.insertarValor(Integer.parseInt(id_inspeccion),Integer.parseInt(dañosDedu[0][0]),String.valueOf(db.obtenerDanio(spinnerDanoFrontalE.getSelectedItem().toString())));

                    //deducible
                    db.insertarValor(Integer.parseInt(id_inspeccion),Integer.parseInt(dañosDedu[0][1]),db.obtenerDeducible(db.obtenerDanio(spinnerDanoFrontalE.getSelectedItem().toString()),spinnerDeducibleFrontalE.getSelectedItem().toString()));

                        servis = new Intent(frontal.this, TransferirFoto.class);
                    servis.putExtra("comentario",comentarito);
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;
                case PHOTO_TURBO:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmaoTurbo = BitmapFactory.decodeFile(mPath);
                    bitmaoTurbo = foto.redimensiomarImagen(bitmaoTurbo);
                    imageTurboFrontalE.setImageBitmap(bitmaoTurbo);
                    String imagenTurbo = foto.convertirImagenDano(bitmaoTurbo);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Turbo Frontal",0,imagenTurbo);
                    db.insertarValor(Integer.parseInt(id_inspeccion),354,"Ok");

                         servis = new Intent(frontal.this, TransferirFoto.class);
                    servis.putExtra("comentario","Turbo Frontal");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;
                case PHOTO_HUENCHE:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapHuenche = BitmapFactory.decodeFile(mPath);
                    bitmapHuenche = foto.redimensiomarImagen(bitmapHuenche);
                    imageHuencheFrontalE.setImageBitmap(bitmapHuenche);
                    String imagenHuenche = foto.convertirImagenDano(bitmapHuenche);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Huenche Frontal",0,imagenHuenche);
                    db.insertarValor(Integer.parseInt(id_inspeccion),288,"Ok");

                         servis = new Intent(frontal.this, TransferirFoto.class);
                    servis.putExtra("comentario","Huenche Frontal");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;
                case PHOTO_COCO:
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
                    imageCocoFrE.setImageBitmap(bitmapCoco);
                    String imagenCoco = foto.convertirImagenDano(bitmapCoco);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Coco Frontal",0,imagenCoco);
                    db.insertarValor(Integer.parseInt(id_inspeccion),316,"Ok");

                        servis = new Intent(frontal.this, TransferirFoto.class);
                    servis.putExtra("comentario","Coco Frontal");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;

                case PHOTO_NEBLINERO:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapNeblinero = BitmapFactory.decodeFile(mPath);
                    bitmapNeblinero = foto.redimensiomarImagen(bitmapNeblinero);
                    imageNeblineroE.setImageBitmap(bitmapNeblinero);
                    String imagenNeblinero = foto.convertirImagenDano(bitmapNeblinero);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Neblinero Frontal",0,imagenNeblinero);
                    db.insertarValor(Integer.parseInt(id_inspeccion),286,"Ok");

                         servis = new Intent(frontal.this, TransferirFoto.class);
                    servis.putExtra("comentario","Neblinero Frontal");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;
                case PHOTO_LOGO:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapLogo = BitmapFactory.decodeFile(mPath);
                    bitmapLogo = foto.redimensiomarImagen(bitmapLogo);
                    imageLogoE.setImageBitmap(bitmapLogo);
                    String imagenLogo = foto.convertirImagenDano(bitmapLogo);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Logo Frontal",0,imagenLogo);
                    db.insertarValor(Integer.parseInt(id_inspeccion),260,"Ok");

                         servis = new Intent(frontal.this, TransferirFoto.class);
                    servis.putExtra("comentario","Logo Frontal");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;
            }

        }
    }


    private  void DesplegarCamposSeccionUno(String id)    {

        if (btnFrontalE.getVisibility()==View.VISIBLE)
        {
            btnFrontalE.setVisibility(View.GONE);
            imageFrontalE.setVisibility(View.GONE);
            imageFrontalE.setImageBitmap(null);
            btnLogoParaFrontalE.setVisibility(View.GONE);
            imageLogoParaE.setVisibility(View.GONE);
            imageLogoParaE.setImageBitmap(null);
            btnAdicionalFrontalE.setVisibility(View.GONE);
            imageAdicionalFrontalE.setVisibility(View.GONE);
            imageAdicionalFrontalE.setImageBitmap(null);

        }
        else
        {

            //seccion dos

            txtPieza.setVisibility(View.GONE);
            txtTipoDanoE.setVisibility(View.GONE);
            txtDeducibleE.setVisibility(View.GONE);
            spinnerPiezaFrontalE.setVisibility(View.GONE);
            spinnerDanoFrontalE.setVisibility(View.GONE);
            spinnerDeducibleFrontalE.setVisibility(View.GONE);
            btnFrontalDanoE.setVisibility(View.GONE);
            imagenFrontalDanoE.setVisibility(View.GONE);
            imagenFrontalDanoE.setImageBitmap(null);

            //seccion tres
            sistemaTurboFrontalE.setVisibility(View.GONE);
            imageTurboFrontalE.setVisibility(View.GONE);
            imageTurboFrontalE.setImageBitmap(null);
            huincheFoE.setVisibility(View.GONE);
            imageHuencheFrontalE.setVisibility(View.GONE);
            imageHuencheFrontalE.setImageBitmap(null);
            CocoFoE.setVisibility(View.GONE);
            imageCocoFrE.setVisibility(View.GONE);
            imageCocoFrE.setImageBitmap(null);
            neblinerosFoE.setVisibility(View.GONE);
            imageNeblineroE.setVisibility(View.GONE);
            imageNeblineroE.setImageBitmap(null);
            logoFoE.setVisibility(View.GONE);
            imageLogoE.setVisibility(View.GONE);
            imageLogoE.setImageBitmap(null);


            String imageFrontal = db.foto(Integer.parseInt(id),"Foto Frontal");
            String imageLogoPara = db.foto(Integer.parseInt(id),"Logo Parabrisas Frontal");
            String imageAdicionalFrontal = db.foto(Integer.parseInt(id),"Adicional Frontal");

            if(imageFrontal.length()>=3 )
            {
                byte[] decodedString = Base64.decode(imageFrontal, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageFrontalE.setImageBitmap(decodedByte);
            }
            if(imageLogoPara.length()>=3 )
            {
                byte[] decodedString = Base64.decode(imageLogoPara, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageLogoParaE.setImageBitmap(decodedByte);
            }
            if(imageAdicionalFrontal.length()>=3 )
            {
                byte[] decodedString = Base64.decode(imageAdicionalFrontal, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageAdicionalFrontalE.setImageBitmap(decodedByte);
            }


            btnFrontalE.setVisibility(View.VISIBLE);
            imageFrontalE.setVisibility(View.VISIBLE);
            btnLogoParaFrontalE.setVisibility(View.VISIBLE);
            imageLogoParaE.setVisibility(View.VISIBLE);
            btnAdicionalFrontalE.setVisibility(View.VISIBLE);
            imageAdicionalFrontalE.setVisibility(View.VISIBLE);

        }
    }

    private  void desplegarCamposSeccionDos(String id)    {

        if (txtPieza.getVisibility() == View.VISIBLE) {

            txtPieza.setVisibility(View.GONE);
            txtTipoDanoE.setVisibility(View.GONE);
            txtDeducibleE.setVisibility(View.GONE);
            spinnerPiezaFrontalE.setVisibility(View.GONE);
            spinnerDanoFrontalE.setVisibility(View.GONE);
            spinnerDeducibleFrontalE.setVisibility(View.GONE);
            btnFrontalDanoE.setVisibility(View.GONE);
            imagenFrontalDanoE.setVisibility(View.GONE);
            imagenFrontalDanoE.setImageBitmap(null);
        }
        else
        {
            //seccion uno
            btnFrontalE.setVisibility(View.GONE);
            imageFrontalE.setVisibility(View.GONE);
            imageFrontalE.setImageBitmap(null);
            btnLogoParaFrontalE.setVisibility(View.GONE);
            imageLogoParaE.setVisibility(View.GONE);
            imageLogoParaE.setImageBitmap(null);
            btnAdicionalFrontalE.setVisibility(View.GONE);
            imageAdicionalFrontalE.setVisibility(View.GONE);
            imageAdicionalFrontalE.setImageBitmap(null);

            //seccion tres
            sistemaTurboFrontalE.setVisibility(View.GONE);
            imageTurboFrontalE.setVisibility(View.GONE);
            imageTurboFrontalE.setImageBitmap(null);
            huincheFoE.setVisibility(View.GONE);
            imageHuencheFrontalE.setVisibility(View.GONE);
            imageHuencheFrontalE.setImageBitmap(null);
            CocoFoE.setVisibility(View.GONE);
            imageCocoFrE.setVisibility(View.GONE);
            imageCocoFrE.setImageBitmap(null);
            neblinerosFoE.setVisibility(View.GONE);
            imageNeblineroE.setVisibility(View.GONE);
            imageNeblineroE.setImageBitmap(null);
            logoFoE.setVisibility(View.GONE);
            imageLogoE.setVisibility(View.GONE);
            imageLogoE.setImageBitmap(null);

            String imagenFrontalDano = db.foto(Integer.parseInt(id),db.comentarioFoto(Integer.parseInt(id),"frontal"));

            if(imagenFrontalDano.length()>=3 )
            {
                byte[] decodedString = Base64.decode(imagenFrontalDano, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imagenFrontalDanoE.setImageBitmap(decodedByte);
            }

            ///////SPINNERPIEZA POSTERIOR
            txtPieza.setVisibility(View.VISIBLE);
            spinnerPiezaFrontalE.setVisibility(View.VISIBLE);
            spinnerPiezaFrontalE = (Spinner)findViewById(R.id.spinnerPiezaFrontalE);
            String listapieza[][] =  db.listaPiezasFrontal();
            String[] listapiezaFrontal = new String[listapieza.length+1];
            listapiezaFrontal[0] = "Seleccionar Pieza";
            for(int i=0;i<listapieza.length;i++){
                listapiezaFrontal[i+1] = listapieza[i][0];
            }
            ArrayAdapter<String> adapterFrontal = new ArrayAdapter<String>(frontal.this,android.R.layout.simple_spinner_item,listapiezaFrontal);
            adapterFrontal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPiezaFrontalE.setAdapter(adapterFrontal);

            spinnerPiezaFrontalE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position !=0){
                        ///////SPINNERDAÑO POSTERIOR
                        txtTipoDanoE.setVisibility(View.VISIBLE);
                        spinnerDanoFrontalE.setVisibility(View.VISIBLE);
                        spinnerDanoFrontalE = (Spinner) findViewById(R.id.spinnerDanoFrontalE);
                        String listaDano[][] =  db.listaDanoPosterior();
                        final String[] listaDanoPosterior = new String[listaDano.length+1];
                        listaDanoPosterior[0] = "Seleccionar Daño";
                        for(int i=0;i<listaDano.length;i++){
                            listaDanoPosterior[i+1] = listaDano[i][0];
                        }
                        ArrayAdapter<String> adapterDanoPosterior = new ArrayAdapter<String>(frontal.this,android.R.layout.simple_spinner_item,listaDanoPosterior);
                        adapterDanoPosterior.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDanoFrontalE.setAdapter(adapterDanoPosterior);

                        spinnerDanoFrontalE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i!=0){
                                    txtDeducibleE.setVisibility(View.VISIBLE);
                                spinnerDeducibleFrontalE.setVisibility(View.VISIBLE);
                                String[][] listadedu=db.listaDeduciblesPosterior(spinnerDanoFrontalE.getSelectedItem().toString());
                                Spinner spinnerDeducibleFrontalE = (Spinner)findViewById(R.id.spinnerDeducibleFrontalE);
                                String[] spinnerDedu = new String[listadedu.length+1];
                                spinnerDedu[0]= "Seleccione";
                                for(int ii=0;ii<listadedu.length;ii++){
                                    spinnerDedu[ii+1] = listadedu[ii][0];
                                }
                                ArrayAdapter<String> adapterdedu = new ArrayAdapter<String>(frontal.this,android.R.layout.simple_spinner_item,spinnerDedu);
                                adapterdedu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerDeducibleFrontalE.setAdapter(adapterdedu);
                                spinnerDeducibleFrontalE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if(position!=0) {
                                            btnFrontalDanoE.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



            imagenFrontalDanoE.setVisibility(View.VISIBLE);


        }
    }

    private void desplegarCamposSeccionTres(String id)    {
        if (sistemaTurboFrontalE.getVisibility() == View.VISIBLE) {

            sistemaTurboFrontalE.setVisibility(View.GONE);
            imageTurboFrontalE.setVisibility(View.GONE);
            imageTurboFrontalE.setImageBitmap(null);
            huincheFoE.setVisibility(View.GONE);
            imageHuencheFrontalE.setVisibility(View.GONE);
            imageHuencheFrontalE.setImageBitmap(null);
            CocoFoE.setVisibility(View.GONE);
            imageCocoFrE.setVisibility(View.GONE);
            imageCocoFrE.setImageBitmap(null);
            neblinerosFoE.setVisibility(View.GONE);
            imageNeblineroE.setVisibility(View.GONE);
            imageNeblineroE.setImageBitmap(null);
            logoFoE.setVisibility(View.GONE);
            imageLogoE.setVisibility(View.GONE);
            imageLogoE.setImageBitmap(null);
        }
        else
        {
            //seccion uno
            btnFrontalE.setVisibility(View.GONE);
            imageFrontalE.setVisibility(View.GONE);
            imageFrontalE.setImageBitmap(null);
            btnLogoParaFrontalE.setVisibility(View.GONE);
            imageLogoParaE.setVisibility(View.GONE);
            imageLogoParaE.setImageBitmap(null);
            btnAdicionalFrontalE.setVisibility(View.GONE);
            imageAdicionalFrontalE.setVisibility(View.GONE);
            imageAdicionalFrontalE.setImageBitmap(null);

            //seccion dos

            txtPieza.setVisibility(View.GONE);
            txtTipoDanoE.setVisibility(View.GONE);
            txtDeducibleE.setVisibility(View.GONE);
            spinnerPiezaFrontalE.setVisibility(View.GONE);
            spinnerDanoFrontalE.setVisibility(View.GONE);
            spinnerDeducibleFrontalE.setVisibility(View.GONE);
            btnFrontalDanoE.setVisibility(View.GONE);
            imagenFrontalDanoE.setVisibility(View.GONE);
            imagenFrontalDanoE.setImageBitmap(null);


            String imageTurboFrontal = db.foto(Integer.parseInt(id),"Turbo Frontal");
            String imageHuencheFrontal = db.foto(Integer.parseInt(id),"Huenche Frontal");
            String imageCocoFr = db.foto(Integer.parseInt(id),"Coco Frontal");
            String imageNeblinero = db.foto(Integer.parseInt(id),"Neblinero Frontal");
            String imageLogo = db.foto(Integer.parseInt(id),"Logo Frontal");


            if(imageTurboFrontal.length()>=3 )
            {
                byte[] decodedString = Base64.decode(imageTurboFrontal, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageTurboFrontalE.setImageBitmap(decodedByte);
                sistemaTurboFrontalE.setChecked(true);
            }
            if(imageHuencheFrontal.length()>3)
            {
                byte[] decodedString = Base64.decode(imageHuencheFrontal, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageHuencheFrontalE.setImageBitmap(decodedByte);
                huincheFoE.setChecked(true);
            }
            if(imageCocoFr.length()>=3)
            {
                byte[] decodedString = Base64.decode(imageCocoFr, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageCocoFrE.setImageBitmap(decodedByte);
                CocoFoE.setChecked(true);
            }
            if(imageNeblinero.length()>=3)
            {
                byte[] decodedString = Base64.decode(imageNeblinero, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageNeblineroE.setImageBitmap(decodedByte);
                neblinerosFoE.setChecked(true);
            }
            if(imageLogo.length()>=3)
            {
                byte[] decodedString = Base64.decode(imageLogo, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageLogoE.setImageBitmap(decodedByte);
                logoFoE.setChecked(true);
            }

            sistemaTurboFrontalE.setVisibility(View.VISIBLE);
            imageTurboFrontalE.setVisibility(View.VISIBLE);
            huincheFoE.setVisibility(View.VISIBLE);
            imageHuencheFrontalE.setVisibility(View.VISIBLE);
            CocoFoE.setVisibility(View.VISIBLE);
            imageCocoFrE.setVisibility(View.VISIBLE);
            neblinerosFoE.setVisibility(View.VISIBLE);
            imageNeblineroE.setVisibility(View.VISIBLE);
            logoFoE.setVisibility(View.VISIBLE);
            imageLogoE.setVisibility(View.VISIBLE);


        }

    }
}
