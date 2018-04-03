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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class interior extends AppCompatActivity {

    DBprovider db;
    Boolean connec = false;
    private final int PHOTO_PANEL = 200;
    private final int PHOTO_PANEL_DENTRO = 300;
    private final int PHOTO_RADIO = 400;
    private final int PHOTO_KILOMETRAJE = 500;
    private final int PHOTO_ADICIONAL = 600;
    private final int PHOTO_CHECK_ENGINE = 700;
    private final int PHOTO_LUZ_TESTIGO = 800;
    private final int PHOTO_CONTROL = 900;
    private final int PHOTO_BLUETOOH = 1000;
    private final int PHOTO_TAPIZ = 1100;
    private final int PHOTO_BUTACA = 1200;
    private final int PHOTO_CORTA_CORRIENTE = 1300;
    private final int PHOTO_ALZA_VIDRIO_DE = 1400;
    private final int PHOTO_ALZA_VIDRIO_TR = 1500;
    private final int PHOTO_RETROVISOR = 1600;
    private final int PHOTO_PARLANTES = 1700;
    private final int PHOTO_TWEETER = 1800;
    private final int PHOTO_AMPL1 = 1900;
    private final int PHOTO_AMPL2 = 2000;
    private final int PHOTO_WOOFER = 2100;
    private final int PHOTO_PANTALLA = 2200;
    private final int PHOTO_GPS = 2300;
    private Button btnVolverInteriorE,btnVolverSecInteriorE,btnSiguienteInteriorE,btnPanelFueraInteE,btnPanelDentroInteE,btnRadioInteriorE,btnKilometrajeE,btnAdicionalInteriorE,btnSeccionUnoInterior, btnSeccionTresInterior, btnSeccionTresMQ;
    private ImageView imagePanelFueraInteE,imagePanelDentroInteE,imageRadioInteriorE,imageKilometrajeE,imageAdicionalInteriorE,imageLuzCheckEngineE,imageluzTestigoAirE,
            imageControlCruceE,imageBluetoothE,imageTapizCueroE,imageButacaElectE,imageCortaCorriE,imageAlzavidrioDeE,imageAlzavidrioTrE,imageRetroElectE,imageParlantesE,
            imageTweeterE,imageAmplifiUnoE,imageAmplifiDosE,imageWooferE,imagePantallaDvdE,imageGpsE;
    private CheckBox luzCheckEngineE,luzTestigoAirE,controlCruceE,bluetoothE,tapizCueroE,butacaElectE,cortaCorriE,alzavidrioDeE,alzavidrioTrE,retroElectE,parlantesE,tweeterE,
            amplifiUnoE,amplifiDosE,wooferE,pantallaDvdE,gpsE;
    private TextView txtAlzavidrioE;
    private String mPath;
    private File ruta_sd;
    private String ruta = "";
    String nombreimagen = "";
    Validaciones validaciones;
    int correlativo = 0;

    PropiedadesFoto foto;

    public interior(){db = new DBprovider(this);foto=new PropiedadesFoto(this); validaciones = new Validaciones(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interior);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion = bundle.getString("id_inspeccion");

        connec = new ConexionInternet(this).isConnectingToInternet();

        btnPanelFueraInteE = findViewById(R.id.btnPanelFueraInteE);
        imagePanelFueraInteE = findViewById(R.id.imagePanelFueraInteE);
        btnPanelDentroInteE = findViewById(R.id.btnPanelDentroInteE);
        imagePanelDentroInteE = findViewById(R.id.imagePanelDentroInteE);
        btnRadioInteriorE = findViewById(R.id.btnRadioInteriorE);
        imageRadioInteriorE = findViewById(R.id.imageRadioInteriorE);
        btnKilometrajeE = findViewById(R.id.btnKilometrajeE);
        imageKilometrajeE = findViewById(R.id.imageKilometrajeE);
        btnAdicionalInteriorE = findViewById(R.id.btnAdicionalInteriorE);
        imageAdicionalInteriorE  = findViewById(R.id.imageAdicionalInteriorE);
        luzCheckEngineE = findViewById(R.id.luzCheckEngineE);
        imageLuzCheckEngineE = findViewById(R.id.imageLuzCheckEngineE);
        luzTestigoAirE = findViewById(R.id.luzTestigoAirE);
        imageluzTestigoAirE = findViewById(R.id.imageluzTestigoAirE);
        controlCruceE = findViewById(R.id.controlCruceE);
        imageControlCruceE = findViewById(R.id.imageControlCruceE);
        bluetoothE = findViewById(R.id.bluetoothE);
        imageBluetoothE = findViewById(R.id.imageBluetoothE);
        tapizCueroE = findViewById(R.id.tapizCueroE);
        imageTapizCueroE = findViewById(R.id.imageTapizCueroE);
        butacaElectE = findViewById(R.id.butacaElectE);
        imageButacaElectE = findViewById(R.id.imageButacaElectE);
        cortaCorriE = findViewById(R.id.cortaCorriE);
        imageCortaCorriE = findViewById(R.id.imageCortaCorriE);
        alzavidrioDeE = findViewById(R.id.alzavidrioDeE);
        imageAlzavidrioDeE = findViewById(R.id.imageAlzavidrioDeE);
        alzavidrioTrE = findViewById(R.id.alzavidrioTrE);
        imageAlzavidrioTrE = findViewById(R.id.imageAlzavidrioTrE);
        retroElectE = findViewById(R.id.retroElectE);
        imageRetroElectE = findViewById(R.id.imageRetroElectE);
        parlantesE = findViewById(R.id.parlantesE);
        imageParlantesE = findViewById(R.id.imageParlantesE);
        tweeterE = findViewById(R.id.tweeterE);
        imageTweeterE = findViewById(R.id.imageTweeterE);
        amplifiUnoE = findViewById(R.id.amplifiUnoE);
        imageAmplifiUnoE = findViewById(R.id.imageAmplifiUnoE);
        amplifiDosE  = findViewById(R.id.amplifiDosE);
        imageAmplifiDosE = findViewById(R.id.imageAmplifiDosE);
        wooferE = findViewById(R.id.wooferE);
        imageWooferE = findViewById(R.id.imageWooferE);
        pantallaDvdE = findViewById(R.id.pantallaDvdE);
        imagePantallaDvdE = findViewById(R.id.imagePantallaDvdE);
        gpsE = findViewById(R.id.gpsE);
        imageGpsE = findViewById(R.id.imageGpsE);
        btnSeccionUnoInterior = findViewById(R.id.btnSeccionUnoInterior);
        btnSeccionTresInterior = findViewById(R.id.btnSeccionTresInterior);
        txtAlzavidrioE = findViewById(R.id.txtAlzavidrioE);
        btnSeccionTresMQ = findViewById(R.id.btnSeccionTresInteriorMQ);


        btnPanelFueraInteE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamaraPanelFuera(id_inspeccion);
            }
        });
        btnPanelDentroInteE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamaraPanelDentro(id_inspeccion);
            }
        });
        btnRadioInteriorE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamaraRadioInterior(id_inspeccion);
            }
        });
        btnKilometrajeE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamaraKilometraje(id_inspeccion);
            }
        });
        btnAdicionalInteriorE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   openCamaraAdicionalInterior(id_inspeccion);            }
        });
        btnSeccionUnoInterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {DesplegarCamposSeccionUno(id_inspeccion);  }
        });
        btnSeccionTresInterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {desplegarCamposSeccionTres(id_inspeccion);  }
        });

        btnSeccionTresMQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desplegarCamposSeccionTresMQ(id_inspeccion);
            }
        });



        luzCheckEngineE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),791));
        luzCheckEngineE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 791).toString().equals("Ok")) {
                        openCamaraLuzCheckEngine(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),791,"");
                    imageLuzCheckEngineE.setImageBitmap(null);
                }
            }
        });

        luzTestigoAirE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),792));
        luzTestigoAirE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 792).toString().equals("Ok")) {
                        openCamaraLuzTestigo(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),792,"");
                    imageluzTestigoAirE.setImageBitmap(null);
                }
            }
        });

        controlCruceE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),291));
        controlCruceE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 291).toString().equals("Ok")) {
                        openCamaraControlCrucero(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),291,"");
                    imageControlCruceE.setImageBitmap(null);
                }
            }
        });

        bluetoothE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),339));
        bluetoothE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 339).toString().equals("Ok")) {
                        openCamaraBluetooh(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),339,"");
                    imageBluetoothE.setImageBitmap(null);
                }
            }
        });

        tapizCueroE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),333));
        tapizCueroE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 333).toString().equals("Ok")) {
                        openCamaraTapizCuero(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),333,"");
                    imageTapizCueroE.setImageBitmap(null);
                }

            }
        });

        butacaElectE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),336));
        butacaElectE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 336).toString().equals("Ok")) {
                        openCamaraButaca(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),336,"");
                    imageButacaElectE.setImageBitmap(null);
                }
            }
        });

        cortaCorriE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),344));
        cortaCorriE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 344).toString().equals("Ok")) {
                        openCamaraCortaCorriente(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),344,"");
                    imageCortaCorriE.setImageBitmap(null);
                }
            }
        });

        alzavidrioDeE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),266));
        alzavidrioDeE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 266).toString().equals("Ok")) {
                        openCamaraAlzaVidrioDelantero(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),266,"");
                    imageAlzavidrioDeE.setImageBitmap(null);
                }
            }
        });

        alzavidrioTrE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),267));
        alzavidrioTrE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 267).toString().equals("Ok")) {
                        openCamaraAlzaVidrioTracero(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),267,"");
                    imageAlzavidrioTrE.setImageBitmap(null);
                }
            }
        });

        retroElectE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),256));
        retroElectE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 256).toString().equals("Ok")) {
                        openCamaraRetrovisorElectrico(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),256,"");
                    imageRetroElectE.setImageBitmap(null);
                }
            }
        });

        parlantesE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),271));
        parlantesE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 271).toString().equals("Ok")) {
                        openCamaraParlantesInterior(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),271,"");
                    imageParlantesE.setImageBitmap(null);
                }
            }
        });

        tweeterE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),239));
        tweeterE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 239).toString().equals("Ok")) {
                        openCamaraTweeter(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),239,"");
                    imageTweeterE.setImageBitmap(null);
                }


            }
        });

        amplifiUnoE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),221));
        amplifiUnoE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 221).toString().equals("Ok")) {
                        openCamaraAmplificadorUno(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),221,"");
                    imageAmplifiUnoE.setImageBitmap(null);
                }


            }
        });

        amplifiDosE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),227));
        amplifiDosE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 227).toString().equals("Ok")) {
                        openCamaraAmplificadorDos(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),227,"");
                    imageAmplifiDosE.setImageBitmap(null);
                }
            }
        });

        wooferE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),245));
        wooferE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 245).toString().equals("Ok")) {
                        openCamaraWoofer(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),245,"");
                    imageWooferE.setImageBitmap(null);
                }
            }
        });

        pantallaDvdE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),251));
        pantallaDvdE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 251).toString().equals("Ok")) {
                        openCamaraPantallaDvd(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),251,"");
                    imagePantallaDvdE.setImageBitmap(null);
                }
            }
        });

        gpsE.setChecked(validaciones.estadoCheck(Integer.parseInt(id_inspeccion),299));
        gpsE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!db.accesorio(Integer.parseInt(id_inspeccion), 299).toString().equals("Ok")) {
                        openCamaraGps(id_inspeccion);
                    }
                }else{
                    db.insertarValor(Integer.parseInt(id_inspeccion),299,"");
                    imageGpsE.setImageBitmap(null);
                }
            }
        });


        btnVolverInteriorE = (Button)findViewById(R.id.btnVolverInteriorE);
        btnVolverInteriorE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(interior.this, vl_techo.class);
                intent.putExtra("id_inspeccion", id_inspeccion);
                startActivity(intent);
                finish();
            }
        });
        btnSiguienteInteriorE = (Button)findViewById(R.id.btnSiguienteInteriorE);
        btnSiguienteInteriorE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String imagePanelFueraInte = db.foto(Integer.parseInt(id_inspeccion), "Foto Panel desde Afuera Interior");
                String imagePanelDentroInte = db.foto(Integer.parseInt(id_inspeccion), "Foto Panel desde Dentro Interior");
                String imageRadioInterior = db.foto(Integer.parseInt(id_inspeccion), "Foto Radio Interior");
                String imageKilometraje = db.foto(Integer.parseInt(id_inspeccion), "Foto Kilometraje Interior");

                if (imagePanelFueraInte.length()<=3 || imagePanelDentroInte.length()<=3 || imageRadioInterior.length()<=3 || imageKilometraje.length()<=3  ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(interior.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Panel desde Afuera</li><p><li>- Foto Panel desde Adent.</li></p>" +
                            "<p><li>- Foto Radio</li></p><p><li>- Foto Kilomentraje</li></p></ul></p>"));
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
                    Intent intent = new Intent(interior.this, motor.class);
                    intent.putExtra("id_inspeccion", id_inspeccion);
                    startActivity(intent);
                    finish();
                }

            }
        });

    }

    private void openCamaraPanelFuera(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {


            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Panel_desde_afuera_Interior.jpg";

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;
            File newFile = new File(mPath);


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(interior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_PANEL);
        }
    }
    private void openCamaraPanelDentro(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {


            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Panel_desde_dentro_Interior.jpg";

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;
            File newFile = new File(mPath);


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(interior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_PANEL_DENTRO);
        }
    }
    private void openCamaraRadioInterior(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {


            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Radio_Interior.jpg";

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;
            File newFile = new File(mPath);


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(interior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_RADIO);
        }
    }
    private void openCamaraKilometraje(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {


            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Kilometraje_Interior.jpg";

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;
            File newFile = new File(mPath);


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(interior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_KILOMETRAJE);
        }
    }
    private void openCamaraAdicionalInterior(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {


            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Adicional_Interior.jpg";

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;
            File newFile = new File(mPath);


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(interior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_ADICIONAL);
        }
    }
    private void openCamaraLuzCheckEngine(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Luz_Check_Engine_Interior.jpg";

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;
            File newFile = new File(mPath);


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(interior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_CHECK_ENGINE);
        }
    }
    private void openCamaraLuzTestigo(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {


            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Luz_Testigo_Airbags_Interior.jpg";

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;
            File newFile = new File(mPath);


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(interior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_LUZ_TESTIGO);
        }
    }
    private void openCamaraControlCrucero(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {


            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Control_Crucero_Interior.jpg";

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;
            File newFile = new File(mPath);


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(interior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_CONTROL);
        }
    }
    private void openCamaraBluetooh(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {


            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Bluetooh_Interior.jpg";

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;
            File newFile = new File(mPath);


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(interior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_BLUETOOH);
        }
    }
    private void openCamaraTapizCuero(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {



            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Tapiz_Cuero_Interior.jpg";

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;
            File newFile = new File(mPath);


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(interior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_TAPIZ);
        }
    }
    private void openCamaraButaca(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {


            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Butaca_Interior.jpg";

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;
            File newFile = new File(mPath);


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(interior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_BUTACA);
        }
    }
    private void openCamaraCortaCorriente(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {


            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Corta_Corriente_Interior.jpg";

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;
            File newFile = new File(mPath);


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(interior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_CORTA_CORRIENTE);
        }
    }
    private void openCamaraAlzaVidrioDelantero(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {


            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Alza_Vidrio_Delantero_Interior.jpg";

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;
            File newFile = new File(mPath);


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(interior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_ALZA_VIDRIO_DE);
        }
    }
    private void openCamaraAlzaVidrioTracero(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {


            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Alza_Vidrio_Tracero_Interior.jpg";

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;
            File newFile = new File(mPath);


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(interior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_ALZA_VIDRIO_TR);
        }
    }
    private void openCamaraRetrovisorElectrico(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {


            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Retrovisor_Electrico_Interior.jpg";

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;
            File newFile = new File(mPath);


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(interior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_RETROVISOR);
        }
    }
    private void openCamaraParlantesInterior(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {


            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Parlantes_Interior.jpg";

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;
            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(interior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_PARLANTES);
        }
    }
    private void openCamaraTweeter(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {


            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Tweeter_Interior.jpg";

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;
            File newFile = new File(mPath);


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(interior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_TWEETER);
        }
    }
    private void openCamaraAmplificadorUno(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Amplificador_Uno_Interior.jpg";

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;
            File newFile = new File(mPath);


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(interior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_AMPL1);
        }
    }
    private void openCamaraAmplificadorDos(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Amplificador_Dos_Interior.jpg";

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;
            File newFile = new File(mPath);


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(interior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_AMPL2);
        }
    }
    private void openCamaraWoofer(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Woofer_Interior.jpg";

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;
            File newFile = new File(mPath);


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(interior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_WOOFER);
        }
    }
    private void openCamaraPantallaDvd(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Pantalla_Dvd_Interior.jpg";

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;
            File newFile = new File(mPath);


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(interior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_PANTALLA);
        }
    }
    private void openCamaraGps(String id) {

        String id_inspeccion = id;
        ruta_sd = Environment.getExternalStorageDirectory();
        File file = new File(ruta_sd.toString() + '/' + id_inspeccion);//(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Gps_Interior.jpg";

            ruta = file.toString() + "/" + nombreimagen;
            mPath = ruta;
            File newFile = new File(mPath);


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(interior.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_GPS);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");
try {
    if (resultCode == RESULT_OK) {
        switch (requestCode) {
            case PHOTO_PANEL:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapPanel = BitmapFactory.decodeFile(mPath);
                bitmapPanel = foto.redimensiomarImagen(bitmapPanel);

                String imagenPanel = foto.convertirImagenDano(bitmapPanel);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Panel desde Afuera Interior", 0, imagenPanel);
                imagenPanel = "data:image/jpg;base64,"+imagenPanel;
                String base64Image1 = imagenPanel.split(",")[1];
                byte[] decodedString1 = Base64.decode(base64Image1, Base64.DEFAULT);
                Bitmap decodedByte1 = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                imagePanelFueraInteE.setImageBitmap(decodedByte1);


                Intent servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Panel desde Afuera Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_PANEL_DENTRO:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapPanelDentro = BitmapFactory.decodeFile(mPath);
                bitmapPanelDentro = foto.redimensiomarImagen(bitmapPanelDentro);

                String imagenPanelDentro = foto.convertirImagenDano(bitmapPanelDentro);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Panel desde Dentro Interior", 0, imagenPanelDentro);
                imagenPanelDentro = "data:image/jpg;base64,"+imagenPanelDentro;
                String base64Image2 = imagenPanelDentro.split(",")[1];
                byte[] decodedString2 = Base64.decode(base64Image2, Base64.DEFAULT);
                Bitmap decodedByte2 = BitmapFactory.decodeByteArray(decodedString2, 0, decodedString2.length);
                imagePanelDentroInteE.setImageBitmap(decodedByte2);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Panel desde Dentro Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_RADIO:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapRadio = BitmapFactory.decodeFile(mPath);
                bitmapRadio = foto.redimensiomarImagen(bitmapRadio);

                String imagenRadio = foto.convertirImagenDano(bitmapRadio);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Radio Interior", 0, imagenRadio);
                imagenRadio = "data:image/jpg;base64,"+imagenRadio;
                String base64Image3 = imagenRadio.split(",")[1];
                byte[] decodedString3 = Base64.decode(base64Image3, Base64.DEFAULT);
                Bitmap decodedByte3 = BitmapFactory.decodeByteArray(decodedString3, 0, decodedString3.length);
                imageRadioInteriorE.setImageBitmap(decodedByte3);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Radio Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_KILOMETRAJE:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapKilometraje = BitmapFactory.decodeFile(mPath);
                bitmapKilometraje = foto.redimensiomarImagen(bitmapKilometraje);

                String imagenKilometraje = foto.convertirImagenDano(bitmapKilometraje);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Kilometraje Interior", 0, imagenKilometraje);
                imagenKilometraje = "data:image/jpg;base64,"+imagenKilometraje;
                String base64Image4 = imagenKilometraje.split(",")[1];
                byte[] decodedString4 = Base64.decode(base64Image4, Base64.DEFAULT);
                Bitmap decodedByte4 = BitmapFactory.decodeByteArray(decodedString4, 0, decodedString4.length);
                imageKilometrajeE.setImageBitmap(decodedByte4);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Kilometraje Interior");
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

                Bitmap bitmapAdicional = BitmapFactory.decodeFile(mPath);
                bitmapAdicional = foto.redimensiomarImagen(bitmapAdicional);

                String imagenAdicional = foto.convertirImagenDano(bitmapAdicional);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Adicional Interior", 0, imagenAdicional);
                imagenAdicional = "data:image/jpg;base64,"+imagenAdicional;
                String base64Image5 = imagenAdicional.split(",")[1];
                byte[] decodedString5 = Base64.decode(base64Image5, Base64.DEFAULT);
                Bitmap decodedByte5 = BitmapFactory.decodeByteArray(decodedString5, 0, decodedString5.length);
                imageAdicionalInteriorE.setImageBitmap(decodedByte5);


                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Adicional Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_CHECK_ENGINE:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapCheck = BitmapFactory.decodeFile(mPath);
                bitmapCheck = foto.redimensiomarImagen(bitmapCheck);

                String imagenCheck = foto.convertirImagenDano(bitmapCheck);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Check Engine Interior", 0, imagenCheck);
                db.insertarValor(Integer.parseInt(id_inspeccion), 791, "Ok");
                imagenCheck = "data:image/jpg;base64,"+imagenCheck;
                String base64Image6 = imagenCheck.split(",")[1];
                byte[] decodedString6 = Base64.decode(base64Image6, Base64.DEFAULT);
                Bitmap decodedByte6 = BitmapFactory.decodeByteArray(decodedString6, 0, decodedString6.length);
                imageLuzCheckEngineE.setImageBitmap(decodedByte6);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Check Engine Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_LUZ_TESTIGO:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapLuzTestigo = BitmapFactory.decodeFile(mPath);
                bitmapLuzTestigo = foto.redimensiomarImagen(bitmapLuzTestigo);

                String imagenLuzTestigo = foto.convertirImagenDano(bitmapLuzTestigo);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Luz Testigo  Airbags Interior", 0, imagenLuzTestigo);
                db.insertarValor(Integer.parseInt(id_inspeccion), 792, "Ok");
                imagenLuzTestigo = "data:image/jpg;base64,"+imagenLuzTestigo;
                String base64Image7 = imagenLuzTestigo.split(",")[1];
                byte[] decodedString7 = Base64.decode(base64Image7, Base64.DEFAULT);
                Bitmap decodedByte7 = BitmapFactory.decodeByteArray(decodedString7, 0, decodedString7.length);
                imageluzTestigoAirE.setImageBitmap(decodedByte7);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Luz Testigo  Airbags Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_CONTROL:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapControl = BitmapFactory.decodeFile(mPath);
                bitmapControl = foto.redimensiomarImagen(bitmapControl);

                String imagenControl = foto.convertirImagenDano(bitmapControl);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Control Crucero Interior", 0, imagenControl);
                db.insertarValor(Integer.parseInt(id_inspeccion), 291, "Ok");

                imagenControl = "data:image/jpg;base64,"+imagenControl;
                String base64Image8 = imagenControl.split(",")[1];
                byte[] decodedString8 = Base64.decode(base64Image8, Base64.DEFAULT);
                Bitmap decodedByte8 = BitmapFactory.decodeByteArray(decodedString8, 0, decodedString8.length);
                imageControlCruceE.setImageBitmap(decodedByte8);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Control Crucero Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_BLUETOOH:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapBluetooh = BitmapFactory.decodeFile(mPath);
                bitmapBluetooh = foto.redimensiomarImagen(bitmapBluetooh);

                String imagenBluetooh = foto.convertirImagenDano(bitmapBluetooh);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Bluetooh Interior", 0, imagenBluetooh);
                db.insertarValor(Integer.parseInt(id_inspeccion), 339, "Ok");
                imagenBluetooh = "data:image/jpg;base64,"+imagenBluetooh;
                String base64Image9 = imagenBluetooh.split(",")[1];
                byte[] decodedString9 = Base64.decode(base64Image9, Base64.DEFAULT);
                Bitmap decodedByte9 = BitmapFactory.decodeByteArray(decodedString9, 0, decodedString9.length);
                imageBluetoothE.setImageBitmap(decodedByte9);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Bluetooh Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_TAPIZ:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapTapiz = BitmapFactory.decodeFile(mPath);
                bitmapTapiz = foto.redimensiomarImagen(bitmapTapiz);

                String imagenTapiz = foto.convertirImagenDano(bitmapTapiz);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Tapiz de Cuero Interior", 0, imagenTapiz);
                db.insertarValor(Integer.parseInt(id_inspeccion), 333, "Ok");
                imagenTapiz = "data:image/jpg;base64,"+imagenTapiz;
                String base64Image10 = imagenTapiz.split(",")[1];
                byte[] decodedString10 = Base64.decode(base64Image10, Base64.DEFAULT);
                Bitmap decodedByte10 = BitmapFactory.decodeByteArray(decodedString10, 0, decodedString10.length);
                imageTapizCueroE.setImageBitmap(decodedByte10);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Tapiz de Cuero Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_BUTACA:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapButaca = BitmapFactory.decodeFile(mPath);
                bitmapButaca = foto.redimensiomarImagen(bitmapButaca);

                String imagenButaca = foto.convertirImagenDano(bitmapButaca);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Butaca Eléctrica Interior", 0, imagenButaca);
                db.insertarValor(Integer.parseInt(id_inspeccion), 336, "Ok");
                imagenButaca = "data:image/jpg;base64,"+imagenButaca;
                String base64Image11 = imagenButaca.split(",")[1];
                byte[] decodedString11 = Base64.decode(base64Image11, Base64.DEFAULT);
                Bitmap decodedByte11 = BitmapFactory.decodeByteArray(decodedString11, 0, decodedString11.length);
                imageButacaElectE.setImageBitmap(decodedByte11);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Butaca Eléctrica Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_CORTA_CORRIENTE:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapCorta = BitmapFactory.decodeFile(mPath);
                bitmapCorta = foto.redimensiomarImagen(bitmapCorta);

                String imagenCorta = foto.convertirImagenDano(bitmapCorta);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Corta Corriente Interior", 0, imagenCorta);
                db.insertarValor(Integer.parseInt(id_inspeccion), 344, "Ok");
                imagenCorta = "data:image/jpg;base64,"+imagenCorta;
                String base64Image12 = imagenCorta.split(",")[1];
                byte[] decodedString12 = Base64.decode(base64Image12, Base64.DEFAULT);
                Bitmap decodedByte12 = BitmapFactory.decodeByteArray(decodedString12, 0, decodedString12.length);
                imageCortaCorriE.setImageBitmap(decodedByte12);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Corta Corriente Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_ALZA_VIDRIO_DE:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapAlzaVidrioDe = BitmapFactory.decodeFile(mPath);
                bitmapAlzaVidrioDe = foto.redimensiomarImagen(bitmapAlzaVidrioDe);

                String imagenAlzaVidrioDe = foto.convertirImagenDano(bitmapAlzaVidrioDe);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Alza Vidrio Delantero Interior", 0, imagenAlzaVidrioDe);
                db.insertarValor(Integer.parseInt(id_inspeccion), 266, "Ok");

                imagenAlzaVidrioDe = "data:image/jpg;base64,"+imagenAlzaVidrioDe;
                String base64Image13 = imagenAlzaVidrioDe.split(",")[1];
                byte[] decodedString13 = Base64.decode(base64Image13, Base64.DEFAULT);
                Bitmap decodedByte13 = BitmapFactory.decodeByteArray(decodedString13, 0, decodedString13.length);
                imageAlzavidrioDeE.setImageBitmap(decodedByte13);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Alza Vidrio Delantero Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_ALZA_VIDRIO_TR:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapAlzaVidrioTr = BitmapFactory.decodeFile(mPath);
                bitmapAlzaVidrioTr = foto.redimensiomarImagen(bitmapAlzaVidrioTr);

                String imagenAlzaVidrioTr = foto.convertirImagenDano(bitmapAlzaVidrioTr);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Alza Vidrio Trasero Interior", 0, imagenAlzaVidrioTr);
                db.insertarValor(Integer.parseInt(id_inspeccion), 267, "Ok");

                imagenAlzaVidrioTr = "data:image/jpg;base64,"+imagenAlzaVidrioTr;
                String base64Image14 = imagenAlzaVidrioTr.split(",")[1];
                byte[] decodedString14 = Base64.decode(base64Image14, Base64.DEFAULT);
                Bitmap decodedByte14 = BitmapFactory.decodeByteArray(decodedString14, 0, decodedString14.length);
                imageAlzavidrioTrE.setImageBitmap(decodedByte14);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Alza Vidrio Trasero Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_RETROVISOR:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapRetrovisor = BitmapFactory.decodeFile(mPath);
                bitmapRetrovisor = foto.redimensiomarImagen(bitmapRetrovisor);

                String imagenRetrovisor = foto.convertirImagenDano(bitmapRetrovisor);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Retrovisor Eléctrico Interior", 0, imagenRetrovisor);
                db.insertarValor(Integer.parseInt(id_inspeccion), 256, "Ok");

                imagenRetrovisor = "data:image/jpg;base64,"+imagenRetrovisor;
                String base64Image15 = imagenRetrovisor.split(",")[1];
                byte[] decodedString15 = Base64.decode(base64Image15, Base64.DEFAULT);
                Bitmap decodedByte15 = BitmapFactory.decodeByteArray(decodedString15, 0, decodedString15.length);
                imageRetroElectE.setImageBitmap(decodedByte15);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Retrovisor Eléctrico Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_PARLANTES:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapParlantes = BitmapFactory.decodeFile(mPath);
                bitmapParlantes = foto.redimensiomarImagen(bitmapParlantes);

                String imagenParlantes = foto.convertirImagenDano(bitmapParlantes);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Parlantes Interior", 0, imagenParlantes);
                db.insertarValor(Integer.parseInt(id_inspeccion), 271, "Ok");

                imagenParlantes = "data:image/jpg;base64,"+imagenParlantes;
                String base64Image16 = imagenParlantes.split(",")[1];
                byte[] decodedString16 = Base64.decode(base64Image16, Base64.DEFAULT);
                Bitmap decodedByte16 = BitmapFactory.decodeByteArray(decodedString16, 0, decodedString16.length);
                imageParlantesE.setImageBitmap(decodedByte16);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Parlantes Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_TWEETER:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapTweeter = BitmapFactory.decodeFile(mPath);
                bitmapTweeter = foto.redimensiomarImagen(bitmapTweeter);

                String ImagenTweeter = foto.convertirImagenDano(bitmapTweeter);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Tweeter Interior", 0, ImagenTweeter);
                db.insertarValor(Integer.parseInt(id_inspeccion), 239, "Ok");

                ImagenTweeter = "data:image/jpg;base64,"+ImagenTweeter;
                String base64Image17 = ImagenTweeter.split(",")[1];
                byte[] decodedString17 = Base64.decode(base64Image17, Base64.DEFAULT);
                Bitmap decodedByte17 = BitmapFactory.decodeByteArray(decodedString17, 0, decodedString17.length);
                imageTweeterE.setImageBitmap(decodedByte17);


                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Tweeter Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_AMPL1:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapAmp1 = BitmapFactory.decodeFile(mPath);
                bitmapAmp1 = foto.redimensiomarImagen(bitmapAmp1);

                String ImagenAmp1 = foto.convertirImagenDano(bitmapAmp1);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Amplificador Uno Interior", 0, ImagenAmp1);
                db.insertarValor(Integer.parseInt(id_inspeccion), 221, "Ok");

                ImagenAmp1 = "data:image/jpg;base64,"+ImagenAmp1;
                String base64Image18 = ImagenAmp1.split(",")[1];
                byte[] decodedString18 = Base64.decode(base64Image18, Base64.DEFAULT);
                Bitmap decodedByte18 = BitmapFactory.decodeByteArray(decodedString18, 0, decodedString18.length);
                imageAmplifiUnoE.setImageBitmap(decodedByte18);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Amplificador Uno Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_AMPL2:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapAmp2 = BitmapFactory.decodeFile(mPath);
                bitmapAmp2 = foto.redimensiomarImagen(bitmapAmp2);

                String ImagenAmp2 = foto.convertirImagenDano(bitmapAmp2);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Amplificador Dos Interior", 0, ImagenAmp2);
                db.insertarValor(Integer.parseInt(id_inspeccion), 227, "Ok");

                ImagenAmp2 = "data:image/jpg;base64,"+ImagenAmp2;
                String base64Image19 = ImagenAmp2.split(",")[1];
                byte[] decodedString19 = Base64.decode(base64Image19, Base64.DEFAULT);
                Bitmap decodedByte19 = BitmapFactory.decodeByteArray(decodedString19, 0, decodedString19.length);
                imageAmplifiDosE.setImageBitmap(decodedByte19);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Amplificador Dos Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_WOOFER:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapWoofer = BitmapFactory.decodeFile(mPath);
                bitmapWoofer = foto.redimensiomarImagen(bitmapWoofer);

                String ImagenWoofer = foto.convertirImagenDano(bitmapWoofer);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Woofer Interior", 0, ImagenWoofer);
                db.insertarValor(Integer.parseInt(id_inspeccion), 245, "Ok");

                ImagenWoofer = "data:image/jpg;base64,"+ImagenWoofer;
                String base64Image20 = ImagenWoofer.split(",")[1];
                byte[] decodedString20 = Base64.decode(base64Image20, Base64.DEFAULT);
                Bitmap decodedByte20 = BitmapFactory.decodeByteArray(decodedString20, 0, decodedString20.length);
                imageWooferE.setImageBitmap(decodedByte20);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Woofer Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_PANTALLA:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapPantalla = BitmapFactory.decodeFile(mPath);
                bitmapPantalla = foto.redimensiomarImagen(bitmapPantalla);

                String ImagenPantalla = foto.convertirImagenDano(bitmapPantalla);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Pantalla DVD Interior", 0, ImagenPantalla);
                db.insertarValor(Integer.parseInt(id_inspeccion), 251, "Ok");

                ImagenPantalla = "data:image/jpg;base64,"+ImagenPantalla;
                String base64Image21 = ImagenPantalla.split(",")[1];
                byte[] decodedString21 = Base64.decode(base64Image21, Base64.DEFAULT);
                Bitmap decodedByte21 = BitmapFactory.decodeByteArray(decodedString21, 0, decodedString21.length);
                imagePantallaDvdE.setImageBitmap(decodedByte21);

                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto Pantalla DVD Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
            case PHOTO_GPS:
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });

                Bitmap bitmapGps = BitmapFactory.decodeFile(mPath);
                bitmapGps = foto.redimensiomarImagen(bitmapGps);

                String ImagenGps = foto.convertirImagenDano(bitmapGps);
                db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto GPS Interior", 0, ImagenGps);
                db.insertarValor(Integer.parseInt(id_inspeccion), 299, "Ok");

                ImagenGps = "data:image/jpg;base64,"+ImagenGps;
                String base64Image22 = ImagenGps.split(",")[1];
                byte[] decodedString22 = Base64.decode(base64Image22, Base64.DEFAULT);
                Bitmap decodedByte22 = BitmapFactory.decodeByteArray(decodedString22, 0, decodedString22.length);
                imageGpsE.setImageBitmap(decodedByte22);


                servis = new Intent(interior.this, TransferirFoto.class);
                servis.putExtra("comentario", "Foto GPS Interior");
                servis.putExtra("id_inspeccion", id_inspeccion);
                startService(servis);

                break;
        }
    }
}catch(Exception e){
    Log.e("Error",e.getMessage());
    Toast.makeText(interior.this,"Porfavor vuelva a intentar tomar la foto",Toast.LENGTH_SHORT).show();
}
    }


    private  void DesplegarCamposSeccionUno(String id)    {

        if (btnPanelFueraInteE.getVisibility()==View.VISIBLE)
        {
            btnPanelFueraInteE.setVisibility(View.GONE);
            imagePanelFueraInteE.setVisibility(View.GONE);
            imagePanelFueraInteE.setImageBitmap(null);
            btnPanelDentroInteE.setVisibility(View.GONE);
            imagePanelDentroInteE.setVisibility(View.GONE);
            imagePanelDentroInteE.setImageBitmap(null);
            btnRadioInteriorE.setVisibility(View.GONE);
            imageRadioInteriorE.setVisibility(View.GONE);
            imageRadioInteriorE.setImageBitmap(null);
            btnKilometrajeE.setVisibility(View.GONE);
            imageKilometrajeE.setVisibility(View.GONE);
            imageKilometrajeE.setImageBitmap(null);
            btnAdicionalInteriorE.setVisibility(View.GONE);
            imageAdicionalInteriorE.setVisibility(View.GONE);
            imageAdicionalInteriorE.setImageBitmap(null);


        }
        else
        {

            //seccion tres
            luzCheckEngineE.setVisibility(View.GONE);
            imageLuzCheckEngineE.setVisibility(View.GONE);
            imageLuzCheckEngineE.setImageBitmap(null);
            luzTestigoAirE.setVisibility(View.GONE);
            imageluzTestigoAirE.setVisibility(View.GONE);
            imageluzTestigoAirE.setImageBitmap(null);
            controlCruceE.setVisibility(View.GONE);
            imageControlCruceE.setVisibility(View.GONE);
            imageControlCruceE.setImageBitmap(null);
            bluetoothE.setVisibility(View.GONE);
            imageBluetoothE.setVisibility(View.GONE);
            imageBluetoothE.setImageBitmap(null);
            tapizCueroE.setVisibility(View.GONE);
            imageTapizCueroE.setVisibility(View.GONE);
            imageTapizCueroE.setImageBitmap(null);
            butacaElectE.setVisibility(View.GONE);
            imageButacaElectE.setVisibility(View.GONE);
            imageButacaElectE.setImageBitmap(null);
            cortaCorriE.setVisibility(View.GONE);
            imageCortaCorriE.setVisibility(View.GONE);
            imageCortaCorriE.setImageBitmap(null);
            alzavidrioDeE.setVisibility(View.GONE);
            imageAlzavidrioDeE.setVisibility(View.GONE);
            imageAlzavidrioDeE.setImageBitmap(null);
            alzavidrioTrE.setVisibility(View.GONE);
            imageAlzavidrioTrE.setVisibility(View.GONE);
            imageAlzavidrioTrE.setImageBitmap(null);
            retroElectE.setVisibility(View.GONE);
            imageRetroElectE.setVisibility(View.GONE);
            imageRetroElectE.setImageBitmap(null);
            parlantesE.setVisibility(View.GONE);
            imageParlantesE.setVisibility(View.GONE);
            imageParlantesE.setImageBitmap(null);
            tweeterE.setVisibility(View.GONE);
            imageTweeterE.setVisibility(View.GONE);
            imageTweeterE.setImageBitmap(null);
            amplifiUnoE.setVisibility(View.GONE);
            imageAmplifiUnoE.setVisibility(View.GONE);
            imageAmplifiUnoE.setImageBitmap(null);
            amplifiDosE.setVisibility(View.GONE);
            imageAmplifiDosE.setVisibility(View.GONE);
            imageAmplifiDosE.setImageBitmap(null);
            wooferE.setVisibility(View.GONE);
            imageWooferE.setVisibility(View.GONE);
            imageWooferE.setImageBitmap(null);
            pantallaDvdE.setVisibility(View.GONE);
            imagePantallaDvdE.setVisibility(View.GONE);
            imagePantallaDvdE.setImageBitmap(null);
            gpsE.setVisibility(View.GONE);
            imageGpsE.setVisibility(View.GONE);
            imageGpsE.setImageBitmap(null);
            txtAlzavidrioE.setVisibility(View.GONE);



            String imagePanelFueraInte = db.foto(Integer.parseInt(id),"Foto Panel desde Afuera Interior");
            String imagePanelDentroInte = db.foto(Integer.parseInt(id),"Foto Panel desde Dentro Interior");
            String imageRadioInterior = db.foto(Integer.parseInt(id),"Foto Radio Interior");
            String imageKilometraje = db.foto(Integer.parseInt(id),"Foto Kilometraje Interior");
            String imageAdicionalInterior = db.foto(Integer.parseInt(id),"Foto Adicional Interior");

            if(imagePanelFueraInte.length()>3)
            {
                byte[] decodedString = Base64.decode(imagePanelFueraInte, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imagePanelFueraInteE.setImageBitmap(decodedByte);
            }
            if(imagePanelDentroInte.length()>3)
            {
                byte[] decodedString = Base64.decode(imagePanelDentroInte, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imagePanelDentroInteE.setImageBitmap(decodedByte);
            }
            if(imageRadioInterior.length()>3)
            {
                byte[] decodedString = Base64.decode(imageRadioInterior, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageRadioInteriorE.setImageBitmap(decodedByte);
            }
            if(imageKilometraje.length()>3)
            {
                byte[] decodedString = Base64.decode(imageKilometraje, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageKilometrajeE.setImageBitmap(decodedByte);
            }
            if(imageAdicionalInterior.length()>3)
            {
                byte[] decodedString = Base64.decode(imageAdicionalInterior, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageAdicionalInteriorE.setImageBitmap(decodedByte);
            }


            btnPanelFueraInteE.setVisibility(View.VISIBLE);
            imagePanelFueraInteE.setVisibility(View.VISIBLE);
            btnPanelDentroInteE.setVisibility(View.VISIBLE);
            imagePanelDentroInteE.setVisibility(View.VISIBLE);
            btnRadioInteriorE.setVisibility(View.VISIBLE);
            imageRadioInteriorE.setVisibility(View.VISIBLE);
            btnKilometrajeE.setVisibility(View.VISIBLE);
            imageKilometrajeE.setVisibility(View.VISIBLE);
            btnAdicionalInteriorE.setVisibility(View.VISIBLE);
            imageAdicionalInteriorE.setVisibility(View.VISIBLE);

        }
    }
    private void desplegarCamposSeccionTres(String id)    {
        if (luzCheckEngineE.getVisibility() == View.VISIBLE) {

            luzCheckEngineE.setVisibility(View.GONE);
            imageLuzCheckEngineE.setVisibility(View.GONE);
            imageLuzCheckEngineE.setImageBitmap(null);
            luzTestigoAirE.setVisibility(View.GONE);
            imageluzTestigoAirE.setVisibility(View.GONE);
            imageluzTestigoAirE.setImageBitmap(null);
            controlCruceE.setVisibility(View.GONE);
            imageControlCruceE.setVisibility(View.GONE);
            imageControlCruceE.setImageBitmap(null);
            bluetoothE.setVisibility(View.GONE);
            imageBluetoothE.setVisibility(View.GONE);
            imageBluetoothE.setImageBitmap(null);
            tapizCueroE.setVisibility(View.GONE);
            imageTapizCueroE.setVisibility(View.GONE);
            imageTapizCueroE.setImageBitmap(null);
            butacaElectE.setVisibility(View.GONE);
            imageButacaElectE.setVisibility(View.GONE);
            imageButacaElectE.setImageBitmap(null);
            cortaCorriE.setVisibility(View.GONE);
            imageCortaCorriE.setVisibility(View.GONE);
            imageCortaCorriE.setImageBitmap(null);
            alzavidrioDeE.setVisibility(View.GONE);
            imageAlzavidrioDeE.setVisibility(View.GONE);
            imageAlzavidrioDeE.setImageBitmap(null);
            alzavidrioTrE.setVisibility(View.GONE);
            imageAlzavidrioTrE.setVisibility(View.GONE);
            imageAlzavidrioTrE.setImageBitmap(null);
            txtAlzavidrioE.setVisibility(View.GONE);
            retroElectE.setVisibility(View.GONE);
            imageRetroElectE.setImageBitmap(null);
            imageRetroElectE.setVisibility(View.GONE);

        }
        else
        {
            //seccion uno
            btnPanelFueraInteE.setVisibility(View.GONE);
            imagePanelFueraInteE.setVisibility(View.GONE);
            imagePanelFueraInteE.setImageBitmap(null);
            btnPanelDentroInteE.setVisibility(View.GONE);
            imagePanelDentroInteE.setVisibility(View.GONE);
            imagePanelDentroInteE.setImageBitmap(null);
            btnRadioInteriorE.setVisibility(View.GONE);
            imageRadioInteriorE.setVisibility(View.GONE);
            imageRadioInteriorE.setImageBitmap(null);
            btnKilometrajeE.setVisibility(View.GONE);
            imageKilometrajeE.setVisibility(View.GONE);
            imageKilometrajeE.setImageBitmap(null);
            btnAdicionalInteriorE.setVisibility(View.GONE);
            imageAdicionalInteriorE.setVisibility(View.GONE);
            imageAdicionalInteriorE.setImageBitmap(null);

            imageRetroElectE.setImageBitmap(null);
            parlantesE.setVisibility(View.GONE);
            imageParlantesE.setVisibility(View.GONE);
            imageParlantesE.setImageBitmap(null);
            tweeterE.setVisibility(View.GONE);
            imageTweeterE.setVisibility(View.GONE);
            imageTweeterE.setImageBitmap(null);
            amplifiUnoE.setVisibility(View.GONE);
            imageAmplifiUnoE.setVisibility(View.GONE);
            imageAmplifiUnoE.setImageBitmap(null);
            amplifiDosE.setVisibility(View.GONE);
            imageAmplifiDosE.setVisibility(View.GONE);
            imageAmplifiDosE.setImageBitmap(null);
            wooferE.setVisibility(View.GONE);
            imageWooferE.setVisibility(View.GONE);
            imageWooferE.setImageBitmap(null);
            pantallaDvdE.setVisibility(View.GONE);
            imagePantallaDvdE.setVisibility(View.GONE);
            imagePantallaDvdE.setImageBitmap(null);
            gpsE.setVisibility(View.GONE);
            imageGpsE.setVisibility(View.GONE);
            imageGpsE.setImageBitmap(null);


            String imageRetroElect = db.foto(Integer.parseInt(id), "Foto Retrovisor Eléctrico Interior");
            String imageLuzCheckEngine = db.foto(Integer.parseInt(id),"Foto Check Engine Interior");
            String imageluzTestigoAir = db.foto(Integer.parseInt(id),"Foto Luz Testigo  Airbags Interior");
            String imageControlCruce = db.foto(Integer.parseInt(id),"Foto Control Crucero Interior");
            String imageBluetooth = db.foto(Integer.parseInt(id),"Foto Bluetooh Interior");
            String imageTapizCuero = db.foto(Integer.parseInt(id),"Foto Tapiz de Cuero Interior");
            String imageButacaElect = db.foto(Integer.parseInt(id),"Foto Butaca Eléctrica Interior");
            String imageCortaCorri = db.foto(Integer.parseInt(id),"Foto Corta Corriente Interior");
            String imageAlzavidrioDe = db.foto(Integer.parseInt(id),"Foto Alza Vidrio Delantero Interior");
            String imageAlzavidrioTr = db.foto(Integer.parseInt(id),"Foto Alza Vidrio Trasero Interior");


            if (imageRetroElect.length() > 3) {
                byte[] decodedString = Base64.decode(imageRetroElect, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageRetroElectE.setImageBitmap(decodedByte);
                retroElectE.setChecked(true);
            }

            if(imageLuzCheckEngine.length()>3)
            {
                byte[] decodedString = Base64.decode(imageLuzCheckEngine, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageLuzCheckEngineE.setImageBitmap(decodedByte);
                luzCheckEngineE.setChecked(true);
            }
            if(imageluzTestigoAir.length()>3)
            {
                byte[] decodedString = Base64.decode(imageluzTestigoAir, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageluzTestigoAirE.setImageBitmap(decodedByte);
                luzTestigoAirE.setChecked(true);
            }
            if(imageControlCruce.length()>3)
            {
                byte[] decodedString = Base64.decode(imageControlCruce, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageControlCruceE.setImageBitmap(decodedByte);
                controlCruceE.setChecked(true);
            }
            if(imageBluetooth.length()>3)
            {
                byte[] decodedString = Base64.decode(imageBluetooth, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageBluetoothE.setImageBitmap(decodedByte);
                bluetoothE.setChecked(true);
            }
            if(imageTapizCuero.length()>3)
            {
                byte[] decodedString = Base64.decode(imageTapizCuero, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageTapizCueroE.setImageBitmap(decodedByte);
                tapizCueroE.setChecked(true);
            }
            if(imageButacaElect.length()>3)
            {
                byte[] decodedString = Base64.decode(imageButacaElect, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageButacaElectE.setImageBitmap(decodedByte);
                butacaElectE.setChecked(true);
            }
            if(imageCortaCorri.length()>3)
            {
                byte[] decodedString = Base64.decode(imageCortaCorri, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageCortaCorriE.setImageBitmap(decodedByte);
                cortaCorriE.setChecked(true);
            }
            if(imageAlzavidrioDe.length()>3)
            {
                byte[] decodedString = Base64.decode(imageAlzavidrioDe, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageAlzavidrioDeE.setImageBitmap(decodedByte);
                alzavidrioDeE.setChecked(true);
            }
            if(imageAlzavidrioTr.length()>3)
            {
                byte[] decodedString = Base64.decode(imageAlzavidrioTr, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageAlzavidrioTrE.setImageBitmap(decodedByte);
                alzavidrioTrE.setChecked(true);
            }



            retroElectE.setVisibility(View.VISIBLE);
            imageRetroElectE.setVisibility(View.VISIBLE);
            luzCheckEngineE.setVisibility(View.VISIBLE);
            imageLuzCheckEngineE.setVisibility(View.VISIBLE);
            luzTestigoAirE.setVisibility(View.VISIBLE);
            imageluzTestigoAirE.setVisibility(View.VISIBLE);
            controlCruceE.setVisibility(View.VISIBLE);
            imageControlCruceE.setVisibility(View.VISIBLE);
            bluetoothE.setVisibility(View.VISIBLE);
            imageBluetoothE.setVisibility(View.VISIBLE);
            tapizCueroE.setVisibility(View.VISIBLE);
            imageTapizCueroE.setVisibility(View.VISIBLE);
            butacaElectE.setVisibility(View.VISIBLE);
            imageButacaElectE.setVisibility(View.VISIBLE);
            cortaCorriE.setVisibility(View.VISIBLE);
            imageCortaCorriE.setVisibility(View.VISIBLE);
            alzavidrioDeE.setVisibility(View.VISIBLE);
            imageAlzavidrioDeE.setVisibility(View.VISIBLE);
            alzavidrioTrE.setVisibility(View.VISIBLE);
            imageAlzavidrioTrE.setVisibility(View.VISIBLE);
            txtAlzavidrioE.setVisibility(View.VISIBLE);


        }

    }
    private void desplegarCamposSeccionTresMQ(String id){
        if (tweeterE.getVisibility() == View.VISIBLE) {

            imageRetroElectE.setImageBitmap(null);
            parlantesE.setVisibility(View.GONE);
            imageParlantesE.setVisibility(View.GONE);
            imageParlantesE.setImageBitmap(null);
            tweeterE.setVisibility(View.GONE);
            imageTweeterE.setVisibility(View.GONE);
            imageTweeterE.setImageBitmap(null);
            amplifiUnoE.setVisibility(View.GONE);
            imageAmplifiUnoE.setVisibility(View.GONE);
            imageAmplifiUnoE.setImageBitmap(null);
            amplifiDosE.setVisibility(View.GONE);
            imageAmplifiDosE.setVisibility(View.GONE);
            imageAmplifiDosE.setImageBitmap(null);
            wooferE.setVisibility(View.GONE);
            imageWooferE.setVisibility(View.GONE);
            imageWooferE.setImageBitmap(null);
            pantallaDvdE.setVisibility(View.GONE);
            imagePantallaDvdE.setVisibility(View.GONE);
            imagePantallaDvdE.setImageBitmap(null);
            gpsE.setVisibility(View.GONE);
            imageGpsE.setVisibility(View.GONE);
            imageGpsE.setImageBitmap(null);
            txtAlzavidrioE.setVisibility(View.GONE);
        }
        else {
            //seccion uno
            btnPanelFueraInteE.setVisibility(View.GONE);
            imagePanelFueraInteE.setVisibility(View.GONE);
            imagePanelFueraInteE.setImageBitmap(null);
            btnPanelDentroInteE.setVisibility(View.GONE);
            imagePanelDentroInteE.setVisibility(View.GONE);
            imagePanelDentroInteE.setImageBitmap(null);
            btnRadioInteriorE.setVisibility(View.GONE);
            imageRadioInteriorE.setVisibility(View.GONE);
            imageRadioInteriorE.setImageBitmap(null);
            btnKilometrajeE.setVisibility(View.GONE);
            imageKilometrajeE.setVisibility(View.GONE);
            imageKilometrajeE.setImageBitmap(null);
            btnAdicionalInteriorE.setVisibility(View.GONE);
            imageAdicionalInteriorE.setVisibility(View.GONE);
            imageAdicionalInteriorE.setImageBitmap(null);

            /*luzCheckEngineE.setVisibility(View.GONE);
            imageLuzCheckEngineE.setVisibility(View.GONE);
            imageLuzCheckEngineE.setImageBitmap(null);
            luzTestigoAirE.setVisibility(View.GONE);
            imageluzTestigoAirE.setVisibility(View.GONE);
            imageluzTestigoAirE.setImageBitmap(null);
            controlCruceE.setVisibility(View.GONE);
            imageControlCruceE.setVisibility(View.GONE);
            imageControlCruceE.setImageBitmap(null);
            bluetoothE.setVisibility(View.GONE);
            imageBluetoothE.setVisibility(View.GONE);
            imageBluetoothE.setImageBitmap(null);
            tapizCueroE.setVisibility(View.GONE);
            imageTapizCueroE.setVisibility(View.GONE);
            imageTapizCueroE.setImageBitmap(null);
            butacaElectE.setVisibility(View.GONE);
            imageButacaElectE.setVisibility(View.GONE);
            imageButacaElectE.setImageBitmap(null);
            cortaCorriE.setVisibility(View.GONE);
            imageCortaCorriE.setVisibility(View.GONE);
            imageCortaCorriE.setImageBitmap(null);
            alzavidrioDeE.setVisibility(View.GONE);
            imageAlzavidrioDeE.setVisibility(View.GONE);
            imageAlzavidrioDeE.setImageBitmap(null);
            alzavidrioTrE.setVisibility(View.GONE);
            imageAlzavidrioTrE.setVisibility(View.GONE);
            imageAlzavidrioTrE.setImageBitmap(null);
            retroElectE.setVisibility(View.GONE);
            imageRetroElectE.setImageBitmap(null);
            txtAlzavidrioE.setVisibility(View.GONE);*/
            luzCheckEngineE.setVisibility(View.GONE);
            imageLuzCheckEngineE.setVisibility(View.GONE);
            imageLuzCheckEngineE.setImageBitmap(null);
            luzTestigoAirE.setVisibility(View.GONE);
            imageluzTestigoAirE.setVisibility(View.GONE);
            imageluzTestigoAirE.setImageBitmap(null);
            controlCruceE.setVisibility(View.GONE);
            imageControlCruceE.setVisibility(View.GONE);
            imageControlCruceE.setImageBitmap(null);
            bluetoothE.setVisibility(View.GONE);
            imageBluetoothE.setVisibility(View.GONE);
            imageBluetoothE.setImageBitmap(null);
            tapizCueroE.setVisibility(View.GONE);
            imageTapizCueroE.setVisibility(View.GONE);
            imageTapizCueroE.setImageBitmap(null);
            butacaElectE.setVisibility(View.GONE);
            imageButacaElectE.setVisibility(View.GONE);
            imageButacaElectE.setImageBitmap(null);
            cortaCorriE.setVisibility(View.GONE);
            imageCortaCorriE.setVisibility(View.GONE);
            imageCortaCorriE.setImageBitmap(null);
            alzavidrioDeE.setVisibility(View.GONE);
            imageAlzavidrioDeE.setVisibility(View.GONE);
            imageAlzavidrioDeE.setImageBitmap(null);
            alzavidrioTrE.setVisibility(View.GONE);
            imageAlzavidrioTrE.setVisibility(View.GONE);
            imageAlzavidrioTrE.setImageBitmap(null);
            txtAlzavidrioE.setVisibility(View.GONE);
            retroElectE.setVisibility(View.GONE);
            imageRetroElectE.setImageBitmap(null);
            imageRetroElectE.setVisibility(View.GONE);


            String imageParlantes = db.foto(Integer.parseInt(id), "Foto Parlantes Interior");
            String imageTweeter = db.foto(Integer.parseInt(id), "Foto Tweeter Interior");
            String imageAmplifiUno = db.foto(Integer.parseInt(id), "Foto Amplificador Uno Interior");
            String imageAmplifiDos = db.foto(Integer.parseInt(id), "Foto Amplificador Dos Interior");
            String imageWoofer = db.foto(Integer.parseInt(id), "Foto Woofer Interior");
            String imagePantallaDvd = db.foto(Integer.parseInt(id), "Foto Pantalla DVD Interior");
            String imageGps = db.foto(Integer.parseInt(id), "Foto GPS Interior");




            if (imageParlantes.length() > 3) {
                byte[] decodedString = Base64.decode(imageParlantes, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageParlantesE.setImageBitmap(decodedByte);
                parlantesE.setChecked(true);
            }
            if (imageTweeter.length() > 3) {
                byte[] decodedString = Base64.decode(imageTweeter, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageTweeterE.setImageBitmap(decodedByte);
                tweeterE.setChecked(true);
            }
            if (imageAmplifiUno.length() > 3) {
                byte[] decodedString = Base64.decode(imageAmplifiUno, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageAmplifiUnoE.setImageBitmap(decodedByte);
                amplifiUnoE.setChecked(true);
            }
            if (imageAmplifiDos.length() > 3) {
                byte[] decodedString = Base64.decode(imageAmplifiDos, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageAmplifiDosE.setImageBitmap(decodedByte);
                amplifiDosE.setChecked(true);
            }
            if (imageWoofer.length() > 3) {
                byte[] decodedString = Base64.decode(imageWoofer, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageWooferE.setImageBitmap(decodedByte);
                wooferE.setChecked(true);
            }
            if (imagePantallaDvd.length() > 3) {
                byte[] decodedString = Base64.decode(imagePantallaDvd, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imagePantallaDvdE.setImageBitmap(decodedByte);
                pantallaDvdE.setChecked(true);
            }
            if (imageGps.length() > 3) {
                byte[] decodedString = Base64.decode(imageGps, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageGpsE.setImageBitmap(decodedByte);
                gpsE.setChecked(true);
            }



            parlantesE.setVisibility(View.VISIBLE);
            imageParlantesE.setVisibility(View.VISIBLE);
            tweeterE.setVisibility(View.VISIBLE);
            imageTweeterE.setVisibility(View.VISIBLE);
            amplifiUnoE.setVisibility(View.VISIBLE);
            imageAmplifiUnoE.setVisibility(View.VISIBLE);
            amplifiDosE.setVisibility(View.VISIBLE);
            imageAmplifiDosE.setVisibility(View.VISIBLE);
            wooferE.setVisibility(View.VISIBLE);
            imageWooferE.setVisibility(View.VISIBLE);
            pantallaDvdE.setVisibility(View.VISIBLE);
            imagePantallaDvdE.setVisibility(View.VISIBLE);
            gpsE.setVisibility(View.VISIBLE);
            imageGpsE.setVisibility(View.VISIBLE);

        }
    }

}
