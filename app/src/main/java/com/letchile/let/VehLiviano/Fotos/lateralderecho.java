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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

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


public class lateralderecho extends AppCompatActivity {
    DBprovider db;
    Boolean connec = false;

    private final int PHOTO_CODE = 200;
    private final int PHOTO_ADICIONAL = 300;
    private final int PHOTO_ADICIONAL_DOS = 400;
    private final int PHOTO_DANO = 500;
    private Button btnVolverLdE, btnVolverSecldE, btnSiguienteLdE, btnLateDerechoE, btnAdicionalUnoE,btnAdicionalDosE,btnFotoDanoLateE,btnSeccionLateralDerechoE,btnSeccionDosLateralDerechoE;
    private Spinner spinnerDanoDeE, spinnerPiezaDeE,spinnerDeducibleDeE;
    private TextView txtPieza,txtTipoDanoE,txtDeducibleE;
    private ImageView imagenLateDerechoE,imageAdicionalUnoLateE,imageAdicionalDosLateE,imagenLaDeDanoE;
    private String mPath;
    private File ruta_sd;
    private String ruta = "";
    String nombreimagen = "",comentarioDañoImg="";
    PropiedadesFoto foto;
    int correlativo = 0;
    String dañosDedu[][];


    public lateralderecho(){db = new DBprovider(this);foto=new PropiedadesFoto(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lateralderecho);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion = bundle.getString("id_inspeccion");

        connec = new ConexionInternet(this).isConnectingToInternet();


        btnLateDerechoE = findViewById(R.id.btnLateDerechoE);
        imagenLateDerechoE = findViewById(R.id.imagenLateDerechoE);
        btnAdicionalUnoE = findViewById(R.id.btnAdicionalUnoE);
        imageAdicionalUnoLateE = findViewById(R.id.imageAdicionalUnoLateE);
        btnAdicionalDosE = findViewById(R.id.btnAdicionalDosE);
        imageAdicionalDosLateE = findViewById(R.id.imageAdicionalDosLateE);
        btnFotoDanoLateE = findViewById(R.id.btnFotoDanoLateE);
        imagenLaDeDanoE = findViewById(R.id.imagenLaDeDanoE);
        btnSeccionLateralDerechoE = findViewById(R.id.btnSeccionLateralDerechoE);
        btnSeccionDosLateralDerechoE = findViewById(R.id.btnSeccionDosLateralDerechoE);
        txtPieza = findViewById(R.id.txtPieza);
        txtTipoDanoE = findViewById(R.id.txtTipoDanoE);
        txtDeducibleE = findViewById(R.id.txtDeducibleE);
        spinnerDeducibleDeE = findViewById(R.id.spinnerDeducibleDeEe);
        spinnerPiezaDeE = findViewById(R.id.spinnerPiezaDeE);
        spinnerDanoDeE  = findViewById(R.id.spinnerDanoDeE);

        btnSeccionLateralDerechoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {desplegarCamposSeccionUno(id_inspeccion); }
        });
        btnSeccionDosLateralDerechoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {desplegarCamposSeccionDos(id_inspeccion); }
        });

        //botones Abren Camara
        btnLateDerechoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openCamaraLateralDerecho(id_inspeccion);
            }
        });
        btnAdicionalUnoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openCamaraLateAdicional(id_inspeccion);
            }
        });
        btnAdicionalDosE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openCamaraLateAdicionalDos(id_inspeccion);
            }
        });
        btnFotoDanoLateE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openCamaraDanoLateDe(id_inspeccion);
            }
        });


        btnVolverLdE = findViewById(R.id.btnVolverLdE);
        btnVolverLdE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {      onBackPressed();

            }
        });

        btnSiguienteLdE =  findViewById(R.id.btnSiguienteLdE);
        btnSiguienteLdE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String imagenLateDerecho = db.foto(Integer.parseInt(id_inspeccion),"Foto Lateral Derecho");
                //String imageAdicionalUnoLate = db.foto(Integer.parseInt(id_inspeccion),10);


                if(imagenLateDerecho.length()<4 )
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(lateralderecho.this);
                    builder.setCancelable(false);
                    builder.setTitle("LET Chile");
                    builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Lateral Derecho</li></ul></p>"));
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    //Toast toast =  Toast.makeText(lateralderecho.this, "Debe Tomar Fotos Obligatorias", Toast.LENGTH_SHORT);
                    //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    //toast.show();
                }
                else {
                    Intent intent = new Intent(lateralderecho.this, frontal.class);
                    intent.putExtra("id_inspeccion",id_inspeccion);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void openCamaraLateralDerecho(String id) {

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
            String imageName = fecha + "_Foto_Lateral_Derecho.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Lateral_Derecho.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(lateralderecho.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_CODE);
        }
    }
    private void openCamaraLateAdicional(String id) {

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
            String imageName = fecha + "_Foto_Lateral_Derecho_Adicional.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Lateral_Derecho_Adicional.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(lateralderecho.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_ADICIONAL);
        }
    }
    private void openCamaraLateAdicionalDos(String id) {

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
            String imageName = fecha + "_Foto_Lateral_Derecho_Adicional_Dos.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Lateral_Derecho_Adicional_Dos.jpg";

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(lateralderecho.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_ADICIONAL_DOS);
        }
    }
    private void openCamaraDanoLateDe(String id) {

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
            String imageName = fecha + "_Foto_Lateral_Derecho_Dano.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Lateral_Derecho_Dano.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(lateralderecho.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_DANO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion=bundle.getString("id_inspeccion");

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
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
                    imagenLateDerechoE.setImageBitmap(bitmap);
                    String imagen = foto.convertirImagenDano(bitmap);
                    db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Lateral Derecho", 0, imagen);


                    Intent servis = new Intent(lateralderecho.this, TransferirFoto.class);
                    servis.putExtra("comentario","Foto Lateral Derecho");
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
                    imageAdicionalUnoLateE.setImageBitmap(bitmapAdcional);
                    String imagenAdcional = foto.convertirImagenDano(bitmapAdcional);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Adicional Uno Lateral Derecho",0,imagenAdcional);

                     servis = new Intent(lateralderecho.this, TransferirFoto.class);
                    servis.putExtra("comentario","Adicional Uno Lateral Derecho");
                        servis.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis);

                    break;
                case PHOTO_ADICIONAL_DOS:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });


                    Bitmap bitmapAdcionalDos= BitmapFactory.decodeFile(mPath);
                    bitmapAdcionalDos = foto.redimensiomarImagen(bitmapAdcionalDos);
                    imageAdicionalDosLateE.setImageBitmap(bitmapAdcionalDos);
                    String imagenAdicionalDos = foto.convertirImagenDano(bitmapAdcionalDos);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Adicional Dos Lateral Derecho",0,imagenAdicionalDos);

                     servis = new Intent(lateralderecho.this, TransferirFoto.class);
                    servis.putExtra("comentario","Adicional Dos Lateral Derecho");
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
                    imagenLaDeDanoE.setImageBitmap(bitmapDano);
                    String imagenDano = foto.convertirImagenDano(bitmapDano);

                    comentarioDañoImg = spinnerPiezaDeE.getSelectedItem().toString()+' '+spinnerDanoDeE.getSelectedItem().toString()+' '+spinnerDeducibleDeE.getSelectedItem().toString()+' ';
                    db.insertarComentarioFoto(Integer.parseInt(id_inspeccion),comentarioDañoImg,"lateral_derecho");
                    String comentarito = db.comentarioFoto(Integer.parseInt(id_inspeccion),"lateral_derecho");

                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, comentarito,0,imagenDano);
                    dañosDedu = db.DeduciblePieza(spinnerPiezaDeE.getSelectedItem().toString(), "lateral_derecho");
                    //daño
                    db.insertarValor(Integer.parseInt(id_inspeccion),Integer.parseInt(dañosDedu[0][0]),String.valueOf(db.obtenerDanio(spinnerDanoDeE.getSelectedItem().toString())));

                    //deducible
                    db.insertarValor(Integer.parseInt(id_inspeccion),Integer.parseInt(dañosDedu[0][1]),db.obtenerDeducible(db.obtenerDanio(spinnerDanoDeE.getSelectedItem().toString()),spinnerDeducibleDeE.getSelectedItem().toString()));

                    servis = new Intent(lateralderecho.this, TransferirFoto.class);
                    servis.putExtra("comentario",comentarito);
                    servis.putExtra("id_inspeccion",id_inspeccion);
                    startService(servis);
                    //danioPo=db.Deducible(spinnerDeduciblePoE.getSelectedItem().toString());

                    break;


            }

        }
    }

    public void desplegarCamposSeccionUno(String id)    {
        if (btnLateDerechoE.getVisibility()==View.VISIBLE)
        {
            btnLateDerechoE.setVisibility(View.GONE);
            imagenLateDerechoE.setVisibility(View.GONE);
            btnAdicionalUnoE.setVisibility(View.GONE);
            imageAdicionalUnoLateE.setVisibility(View.GONE);
            btnAdicionalDosE.setVisibility(View.GONE);
            imageAdicionalDosLateE.setVisibility(View.GONE);

        }
        else
        {
            txtPieza.setVisibility(View.GONE);
            spinnerPiezaDeE.setVisibility(View.GONE);
            txtTipoDanoE.setVisibility(View.GONE);
            spinnerDanoDeE.setVisibility(View.GONE);
            txtDeducibleE.setVisibility(View.GONE);
            spinnerDeducibleDeE.setVisibility(View.GONE);
            btnFotoDanoLateE.setVisibility(View.GONE);
            imagenLaDeDanoE.setVisibility(View.GONE);
            imagenLaDeDanoE.setImageBitmap(null);


            String imagenLateDerecho = db.foto(Integer.parseInt(id),"Foto Lateral Derecho");
            String imageAdicionalUnoLate = db.foto(Integer.parseInt(id),"Adicional Uno Lateral Derecho");
            String imageAdicionalDosLate = db.foto(Integer.parseInt(id),"Adicional Dos Lateral Derecho");

            if(imagenLateDerecho.length()>=3 )
            {
                // Bitmap bitmap = BitmapFactory.decodeFile(imagenDanoPosterior);
                //bitmap = redimensiomarImagen(bitmap,600,800);
                byte[] decodedString = Base64.decode(imagenLateDerecho, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imagenLateDerechoE.setImageBitmap(decodedByte);
            }

            if(imageAdicionalUnoLate.length()>=3 )
            {
                // Bitmap bitmap = BitmapFactory.decodeFile(imagenDanoPosterior);
                //bitmap = redimensiomarImagen(bitmap,600,800);
                byte[] decodedString = Base64.decode(imageAdicionalUnoLate, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageAdicionalUnoLateE.setImageBitmap(decodedByte);
            }

            if(imageAdicionalDosLate.length()>=3)
            {
                byte[] decodedString = Base64.decode(imageAdicionalDosLate, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageAdicionalDosLateE.setImageBitmap(decodedByte);
            }

            btnLateDerechoE.setVisibility(View.VISIBLE);
            imagenLateDerechoE.setVisibility(View.VISIBLE);
            btnAdicionalUnoE.setVisibility(View.VISIBLE);
            imageAdicionalUnoLateE.setVisibility(View.VISIBLE);
            btnAdicionalDosE.setVisibility(View.VISIBLE);
            imageAdicionalDosLateE.setVisibility(View.VISIBLE);

        }

    }
    public void desplegarCamposSeccionDos(String id)    {
        if (txtPieza.getVisibility()==View.VISIBLE)
        {
            txtPieza.setVisibility(View.GONE);
            spinnerPiezaDeE.setVisibility(View.GONE);
            txtTipoDanoE.setVisibility(View.GONE);
            spinnerDanoDeE.setVisibility(View.GONE);
            txtDeducibleE.setVisibility(View.GONE);
            spinnerDeducibleDeE.setVisibility(View.GONE);
            btnFotoDanoLateE.setVisibility(View.GONE);
            imagenLaDeDanoE.setVisibility(View.GONE);
            imagenLaDeDanoE.setImageBitmap(null);
        }
        else
        {

            btnLateDerechoE.setVisibility(View.GONE);
            imagenLateDerechoE.setVisibility(View.GONE);
            imagenLateDerechoE.setImageBitmap(null);
            btnAdicionalUnoE.setVisibility(View.GONE);
            imageAdicionalUnoLateE.setVisibility(View.GONE);
            imageAdicionalUnoLateE.setImageBitmap(null);
            btnAdicionalDosE.setVisibility(View.GONE);
            imageAdicionalDosLateE.setVisibility(View.GONE);
            imageAdicionalDosLateE.setImageBitmap(null);



            String imagenLaDeDano = db.foto(Integer.parseInt(id),db.comentarioFoto(Integer.parseInt(id),"lateral_derecho"));

            if(imagenLaDeDano.length()>=3)
            {
                byte[] decodedString = Base64.decode(imagenLaDeDano, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imagenLaDeDanoE.setImageBitmap(decodedByte);
            }

            ///////SPINNERPIEZA LATERAL DERECHO
            spinnerPiezaDeE = (Spinner) findViewById(R.id.spinnerPiezaDeE);
            String listapieza[][] = db.listaPiezasLateralDerecho();
            String[] listapiezaLateralDerecho = new String[listapieza.length+1];
            listapiezaLateralDerecho[0] = "Seleccione";
            for (int i = 0; i < listapieza.length; i++) {
                listapiezaLateralDerecho[i+1] = listapieza[i][0];
            }
            ArrayAdapter<String> adapterLateralDerecho = new ArrayAdapter<String>(lateralderecho.this, android.R.layout.simple_spinner_item, listapiezaLateralDerecho);
            adapterLateralDerecho.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPiezaDeE.setAdapter(adapterLateralDerecho);

            ///////SPINNERDAÑO POSTERIOR
            spinnerPiezaDeE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position != 0) {
                        ///////SPINNERDAÑO POSTERIOR
                        spinnerDanoDeE = (Spinner) findViewById(R.id.spinnerDanoDeE);
                        txtTipoDanoE.setVisibility(View.VISIBLE);
                        spinnerDanoDeE.setVisibility(View.VISIBLE);
                        String listaDano[][] = db.listaDanoPosterior();
                        String[] listaDanoPosterior = new String[listaDano.length+1];
                        listaDanoPosterior[0]="Seleccione";
                        for (int i = 0; i < listaDano.length; i++) {
                            listaDanoPosterior[i+1] = listaDano[i][0];
                        }
                        ArrayAdapter<String> adapterDanoPosterior = new ArrayAdapter<String>(lateralderecho.this, android.R.layout.simple_spinner_item, listaDanoPosterior);
                        adapterDanoPosterior.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDanoDeE.setAdapter(adapterDanoPosterior);

                        spinnerDanoDeE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i !=0) { // 6 -> faltante
                                    String[][] listadedu = db.listaDeduciblesPosterior(spinnerDanoDeE.getSelectedItem().toString());
                                    Spinner spinnerDeducibleDeE = (Spinner) findViewById(R.id.spinnerDeducibleDeEe);
                                    spinnerDeducibleDeE.setVisibility(View.VISIBLE);
                                    String[] spinnerDedu = new String[listadedu.length+1];
                                    spinnerDedu[0]="Seleccione";
                                    for (int ii = 0; ii < listadedu.length; ii++) {
                                        spinnerDedu[ii+1] = listadedu[ii][0];
                                    }
                                    ArrayAdapter<String> adapterdedu = new ArrayAdapter<String>(lateralderecho.this, android.R.layout.simple_spinner_item, spinnerDedu);
                                    adapterdedu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerDeducibleDeE.setAdapter(adapterdedu);
                                    txtDeducibleE.setVisibility(View.VISIBLE);
                                    spinnerDeducibleDeE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            if (position != 0) {
                                                btnFotoDanoLateE.setVisibility(View.VISIBLE);
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
                public void onNothingSelected(AdapterView<?> parent) {}
            });




           /* spinnerDanoDeE = (Spinner) findViewById(R.id.spinnerDanoDeE);
                String listaDano[][] = db.listaDanoPosterior();

                final String[] listaDanoPosterior = new String[listaDano.length];
                listaDanoPosterior[0] = "Seleccionar Daño";
                for (int i = 1; i < listaDano.length; i++) {
                    listaDanoPosterior[i] = listaDano[i][0];
                }
                ArrayAdapter<String> adapterDanoPosterior = new ArrayAdapter<String>(lateralderecho.this, android.R.layout.simple_spinner_item, listaDanoPosterior);

                adapterDanoPosterior.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDanoDeE.setAdapter(adapterDanoPosterior);

                spinnerDanoDeE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String[][] listadedu = db.listaDeduciblesPosterior(spinnerDanoDeE.getSelectedItem().toString());
                        Spinner spinnerDeducibleDeE = (Spinner) findViewById(R.id.spinnerDeducibleDeE);
                        String[] spinnerDedu = new String[listadedu.length];
                        spinnerDedu[0]="Seleccionar Deducible";
                        for (int ii = 0; ii < listadedu.length; ii++) {
                            spinnerDedu[ii] = listadedu[ii][0];
                        }
                        ArrayAdapter<String> adapterdedu = new ArrayAdapter<String>(lateralderecho.this, android.R.layout.simple_spinner_item, spinnerDedu);

                        adapterdedu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDeducibleDeE.setAdapter(adapterdedu);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });*/
            txtPieza.setVisibility(View.VISIBLE);
            spinnerPiezaDeE.setVisibility(View.VISIBLE);
            imagenLaDeDanoE.setVisibility(View.VISIBLE);
        }

    }


}
