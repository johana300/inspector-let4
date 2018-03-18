package com.letchile.let.VehPesado.Fotos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.letchile.let.BD.DBprovider;
import com.letchile.let.BuildConfig;
import com.letchile.let.Clases.PropiedadesFoto;
import com.letchile.let.R;
import com.letchile.let.Servicios.TransferirFoto;
import com.letchile.let.VehPesado.DatosAsegVpActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class documentos_vp extends AppCompatActivity {


    @BindView(R.id.btnPosteriorVP)Button btnFotoPosterior;
    @BindView(R.id.imageViewFotoPvpMQ)ImageView imagenPosteriorVp;
    @BindView(R.id.btnPosteriorVP2)Button btnFotoPosterior2;
    @BindView(R.id.imageViewFotoPvpMQ2)ImageView imagenPosteriorVp2;
    @BindView(R.id.btnPosteriorVP3)Button btnFotoPosterior3;
    @BindView(R.id.imageViewFotoPvpMQ3)ImageView imagenPosteriorVp3;

    @BindView(R.id.btnFotoAdiocionalPvpMQ)Button btnFotoAdicionalPosterior;
    @BindView(R.id.imageFotoAdicionalPvpMQ)ImageView imagenAdicionalPosteriorVp;

    private final int TAKE_POSTERIOR = 100,TAKE_POSTERIOR2=400,TAKE_POSTERIOR3=500, TAKE_ADDPOVP=200,TAKE_ADDPOVP2=600,TAKE_DANOPVP=300;
    String id_inspeccion,ruta,mPath,nombreimagen,tipoVeh;
    private File ruta_sd;
    int correlativo = 0;
    DBprovider db;
    PropiedadesFoto foto;
    Intent servis;

    public documentos_vp(){
        db = new DBprovider(this);
        foto = new PropiedadesFoto(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentos_vp);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        id_inspeccion = bundle.getString("id_inspeccion");
        tipoVeh = bundle.getString("tipoVeh");

        //region Sacar foto a posterior
        btnFotoPosterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    String imageName = fecha + "_Foto_Documento_VP.jpg";
                    ruta = file.toString() +"/" +imageName;
                    mPath =  ruta ;

                    File newFile = new File(mPath);

                    correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
                    nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Documento_VP.jpg";

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(documentos_vp.this, BuildConfig.APPLICATION_ID+".provider",newFile));
                    startActivityForResult(intent,TAKE_POSTERIOR);
                }
            }
        });
        //endregion

        //region Sacar foto a posterior 2
        btnFotoPosterior2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    String imageName = fecha + "_Foto_Informe_Anverso_VP.jpg";
                    ruta = file.toString() +"/" +imageName;
                    mPath =  ruta ;

                    File newFile = new File(mPath);

                    correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
                    nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Informe_Anverso_VP.jpg";

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(documentos_vp.this, BuildConfig.APPLICATION_ID+".provider",newFile));
                    startActivityForResult(intent,TAKE_POSTERIOR2);
                }
            }
        });
        //endregion

        //region Sacar foto a posterior 3
        btnFotoPosterior3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    String imageName = fecha + "_Foto_Informe_Reverso_VP.jpg";
                    ruta = file.toString() +"/" +imageName;
                    mPath =  ruta ;

                    File newFile = new File(mPath);

                    correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
                    nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Informe_Reverso_VP.jpg";

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(documentos_vp.this, BuildConfig.APPLICATION_ID+".provider",newFile));
                    startActivityForResult(intent,TAKE_POSTERIOR3);
                }
            }
        });
        //endregion

        //region Sacar foto adicional
        btnFotoAdicionalPosterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    String imageName = fecha + "_Foto_Documento_Adicional_VP.jpg";
                    ruta = file.toString() +"/" +imageName;
                    mPath =  ruta ;

                    File newFile = new File(mPath);

                    correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
                    nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Documento_Adicional_VP.jpg";

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(documentos_vp.this, BuildConfig.APPLICATION_ID+".provider",newFile));
                    startActivityForResult(intent,TAKE_ADDPOVP);
                }
            }
        });
        //endregion
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        db.cambiarEstadoInspeccion(Integer.parseInt(id_inspeccion),1);
        if(resultCode== RESULT_OK){
            switch(requestCode){
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

                    Bitmap bitPosterior = BitmapFactory.decodeFile(mPath);
                    bitPosterior = foto.redimensiomarImagen(bitPosterior);
                    imagenPosteriorVp.setImageBitmap(bitPosterior);
                    String imagenPosterior = foto.convertirImagenDano(bitPosterior);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Documento",0,imagenPosterior);

                    servis = new Intent(documentos_vp.this, TransferirFoto.class);
                    servis.putExtra("comentario","Documento");
                    servis.putExtra("id_inspeccion",id_inspeccion);
                    startService(servis);

                    break;

                case TAKE_POSTERIOR2:

                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitPosterior2 = BitmapFactory.decodeFile(mPath);
                    bitPosterior2 = foto.redimensiomarImagen(bitPosterior2);
                    imagenPosteriorVp2.setImageBitmap(bitPosterior2);
                    String imagenPosterior2 = foto.convertirImagenDano(bitPosterior2);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Informe_Anverso",0,imagenPosterior2);

                    servis = new Intent(documentos_vp.this, TransferirFoto.class);
                    servis.putExtra("comentario","Informe_Anverso");
                    servis.putExtra("id_inspeccion",id_inspeccion);
                    startService(servis);

                    break;

                case TAKE_POSTERIOR3:

                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitPosterior3 = BitmapFactory.decodeFile(mPath);
                    bitPosterior3 = foto.redimensiomarImagen(bitPosterior3);
                    imagenPosteriorVp3.setImageBitmap(bitPosterior3);
                    String imagenPosterior3 = foto.convertirImagenDano(bitPosterior3);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Informe_Reverso",0,imagenPosterior3);

                    servis = new Intent(documentos_vp.this, TransferirFoto.class);
                    servis.putExtra("comentario","Informe_Reverso");
                    servis.putExtra("id_inspeccion",id_inspeccion);
                    startService(servis);

                    break;

                case TAKE_ADDPOVP:

                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitAddPosterior = BitmapFactory.decodeFile(mPath);
                    bitAddPosterior = foto.redimensiomarImagen(bitAddPosterior);
                    imagenAdicionalPosteriorVp.setImageBitmap(bitAddPosterior);
                    String imagenAddPosterior = foto.convertirImagenDano(bitAddPosterior);
                    db.insertaFoto(Integer.parseInt(id_inspeccion),db.correlativoFotos(Integer.parseInt(id_inspeccion)),nombreimagen, "Adicional Documento",0,imagenAddPosterior);

                    servis = new Intent(documentos_vp.this, TransferirFoto.class);
                    servis.putExtra("comentario","Adicional Documento");
                    servis.putExtra("id_inspeccion",id_inspeccion);
                    startService(servis);


                    break;

            }
        }
    }


    @OnClick(R.id.btnSeccionUnoPosteriorvpMQ) //Mostrar seccion 1
    public void Seccion1(View view){
        if(btnFotoPosterior.getVisibility()==View.VISIBLE){
            btnFotoPosterior.setVisibility(View.GONE);
            imagenPosteriorVp.setVisibility(View.GONE);
            imagenPosteriorVp.setImageBitmap(null);
            btnFotoPosterior2.setVisibility(View.GONE);
            imagenPosteriorVp2.setVisibility(View.GONE);
            imagenPosteriorVp2.setImageBitmap(null);
            btnFotoPosterior3.setVisibility(View.GONE);
            imagenPosteriorVp3.setVisibility(View.GONE);
            imagenPosteriorVp3.setImageBitmap(null);
            btnFotoAdicionalPosterior.setVisibility(View.GONE);
            imagenAdicionalPosteriorVp.setVisibility(View.GONE);
            imagenAdicionalPosteriorVp.setImageBitmap(null);


        }else{
            btnFotoPosterior.setVisibility(View.VISIBLE);
            imagenPosteriorVp.setVisibility(View.VISIBLE);
            btnFotoPosterior2.setVisibility(View.VISIBLE);
            imagenPosteriorVp2.setVisibility(View.VISIBLE);
            btnFotoPosterior3.setVisibility(View.VISIBLE);
            imagenPosteriorVp3.setVisibility(View.VISIBLE);
            btnFotoAdicionalPosterior.setVisibility(View.VISIBLE);
            imagenAdicionalPosteriorVp.setVisibility(View.VISIBLE);


            String imagenPosterior = db.foto(Integer.parseInt(id_inspeccion),"Documento");
            String imagenPosterior2 = db.foto(Integer.parseInt(id_inspeccion),"Informe_Anverso");
            String imagenPosterior3 = db.foto(Integer.parseInt(id_inspeccion),"Informe_Reverso");

            String imagenAdicional = db.foto(Integer.parseInt(id_inspeccion),"Adicional Documento");



            if(imagenPosterior.length()>=3){
                byte[] decodeString = Base64.decode(imagenPosterior,Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
                imagenPosteriorVp.setImageBitmap(decodedByte);
            }
            if(imagenPosterior2.length()>=3){
                byte[] decodeString = Base64.decode(imagenPosterior2,Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
                imagenPosteriorVp2.setImageBitmap(decodedByte);
            }
            if(imagenPosterior3.length()>=3){
                byte[] decodeString = Base64.decode(imagenPosterior3,Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
                imagenPosteriorVp3.setImageBitmap(decodedByte);
            }
            if(imagenAdicional.length()>=3){
                byte[] decodeString = Base64.decode(imagenAdicional,Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodeString,0,decodeString.length);
                imagenAdicionalPosteriorVp.setImageBitmap(decodedByte);
            }


        }
    }

    @OnClick(R.id.btnSiguientePvpMQ)//Seguir a la siguiente seccion de camara
    public void seguir(View view){
        Intent in = new Intent(documentos_vp.this, DatosAsegVpActivity.class);
        in.putExtra("id_inspeccion",id_inspeccion);
        in.putExtra("tipoVeh",tipoVeh);
        startActivity(in);
        finish();
    }

    @OnClick(R.id.btnVolverPvpMQ)//Volver a las secciones
    public void volver(View view){

        if(tipoVeh.equals("4")) {
            Intent in = new Intent(documentos_vp.this, interior_vp.class);
            in.putExtra("id_inspeccion", id_inspeccion);
            in.putExtra("tipoVeh", tipoVeh);
            startActivity(in);
            finish();
        }else{
            Intent in = new Intent(documentos_vp.this, llanaNeuma_vp.class);
            in.putExtra("id_inspeccion", id_inspeccion);
            in.putExtra("tipoVeh", tipoVeh);
            startActivity(in);
            finish();
        }

    }
}
