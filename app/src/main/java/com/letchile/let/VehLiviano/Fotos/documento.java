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
import com.letchile.let.VehLiviano.DatosAsegActivity;
import com.letchile.let.VehLiviano.SeccionActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class documento extends AppCompatActivity {

    DBprovider db;
    Boolean connec = false;
    PropiedadesFoto foto;
    private final int PHOTO_DOCUMENTO = 200;
    private final int PHOTO_PAC = 300;
    private final int PHOTO_CARNET_AN = 400;
    private final int PHOTO_CARNET_RE = 500;
    private final int PHOTO_CONVERTIBLE = 600;
    private final int PHOTO_ADICIONAL = 700;
    private Button btnVolverDocuE,btnVolverSecDocuE,btnDocumentoE,btnPACE,btnCarnetAnversoE,btnCarnetReversoE,btnConvertibleE,btnAdicionalDocumentoE,btnSeccionUnoDocumento;
    private ImageView imageDocumentoE,imagePACE,imageCarnetAnversoE,imageCarnetReversoE,imageConvertibleE,imageAdicionalDocumentoE;
    private String mPath;
    private File ruta_sd;
    private String ruta = "";
    String nombreimagen = "";
    int correlativo=0;


    public documento(){db = new DBprovider(this);foto=new PropiedadesFoto(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documento);

        Bundle bundle = getIntent().getExtras();
        final String id_inspeccion = bundle.getString("id_inspeccion");

        connec = new ConexionInternet(this).isConnectingToInternet();

        btnDocumentoE = findViewById(R.id.btnDocumentoE);
        imageDocumentoE = findViewById(R.id.imageDocumentoE);
        btnPACE = findViewById(R.id.btnPACE);
        imagePACE = findViewById(R.id.imagePACE);
        btnCarnetAnversoE = findViewById(R.id.btnCarnetAnversoE);
        imageCarnetAnversoE = findViewById(R.id.imageCarnetAnversoE);
        btnCarnetReversoE = findViewById(R.id.btnCarnetReversoE);
        imageCarnetReversoE = findViewById(R.id.imageCarnetReversoE);
        btnConvertibleE = findViewById(R.id.btnConvertibleE);
        imageConvertibleE = findViewById(R.id.imageConvertibleE);
        btnAdicionalDocumentoE = findViewById(R.id.btnAdicionalDocumentoE);
        imageAdicionalDocumentoE = findViewById(R.id.imageAdicionalDocumentoE);
        btnSeccionUnoDocumento = findViewById(R.id.btnSeccionUnoDocumento);

        btnDocumentoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamaraDocumento(id_inspeccion);
            }
        });
        btnPACE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamaraPAC(id_inspeccion);
            }
        });
        btnCarnetAnversoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamaraCarnetAn(id_inspeccion);
            }
        });
        btnCarnetReversoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamaraCarnetRe(id_inspeccion);
            }
        });
        btnConvertibleE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamaraConvertible(id_inspeccion);
            }
        });
        btnAdicionalDocumentoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openCamaraAdicionalDocumento(id_inspeccion);
            }
        });
        btnSeccionUnoDocumento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desplegarSeccionUnoDocumento(id_inspeccion);
            }
        });

        btnVolverDocuE = (Button)findViewById(R.id.btnVolverDocuE);
        btnVolverDocuE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(documento.this,motor.class);
                intent.putExtra("id_inspeccion",id_inspeccion);
                startActivity(intent);
                finish();
            }
        });
        btnVolverSecDocuE = (Button)findViewById(R.id.btnVolverSecDocuE);
        btnVolverSecDocuE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageDocumento = db.foto(Integer.parseInt(id_inspeccion),"Foto Documento");
                String imagePAC = db.foto(Integer.parseInt(id_inspeccion),"Foto PAC");
                String imageCarnetAnverso = db.foto(Integer.parseInt(id_inspeccion),"Foto Carnet Anverso");
                String imageCarnetReverso = db.foto(Integer.parseInt(id_inspeccion),"Foto Carnet Reversa");
                //String imageConvertible = db.foto(Integer.parseInt(id_inspeccion),"Foto Convertible");

                try {
                    if (db.consultaPAC(Integer.parseInt(id_inspeccion)).toString().equals("S")) {

                        if(imageDocumento.length()<=3 || imagePAC.length()<=3 || imageCarnetAnverso.length()<=3 || imageCarnetReverso.length()<=3 )
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(documento.this);
                            builder.setCancelable(false);
                            builder.setTitle("LET Chile");
                            builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Documento</li><p><li>- Foto PAC</li></p><p><li>- Foto Carnet Anverso</li></p>" +
                                    "<p><li>- Foto Reverso</li></p></ul></p>"));
                            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        else{

                            Intent intent = new Intent(documento.this,SeccionActivity.class);
                            intent.putExtra("id_inspeccion",id_inspeccion);
                            startActivity(intent);
                        }
                    }
                    else
                    {
                        if(imageDocumento.length()<=3 )
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(documento.this);
                            builder.setCancelable(false);
                            builder.setTitle("LET Chile");
                            builder.setMessage(Html.fromHtml("<b>Debe Tomar Fotos Obligatorias</b><p><ul><li>- Foto Documento</li></ul></p>"));
                            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        else{

                            Intent intent = new Intent(documento.this,DatosAsegActivity.class);
                            intent.putExtra("id_inspeccion",id_inspeccion);
                            startActivity(intent);
                            finish();
                        }

                    }
                }catch(Exception e){
                    Log.e("Error pac",e.getMessage()+'-'+db.consultaPAC(Integer.parseInt(id_inspeccion)));
                }



            }
        });

    }


    private void openCamaraDocumento(String id) {

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
            String imageName = fecha + "_Foto_Documento.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Documento.jpg";

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(documento.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_DOCUMENTO);
        }
    }
    private void openCamaraPAC(String id) {

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
            String imageName = fecha + "_Foto_PAC.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_PAC.jpg";

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(documento.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_PAC);
        }
    }
    private void openCamaraCarnetAn(String id) {

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
            String imageName = fecha + "_Foto_Carnet_Anverso.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Carnet_Anverso.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(documento.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_CARNET_AN);
        }
    }
    private void openCamaraCarnetRe(String id) {

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
            String imageName = fecha + "_Foto_Carnet_Reverso.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Carnet_Reverso.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(documento.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_CARNET_RE);
        }
    }
    private void openCamaraConvertible(String id) {

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
            String imageName = fecha + "_Foto_Convertible.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Convertible.jpg";

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(documento.this,
                    BuildConfig.APPLICATION_ID + ".provider", newFile));
            //db.insertaFoto(Integer.parseInt(id_inspeccion),1,imageName, "Daño Posterior",0,newFile);
            startActivityForResult(intent, PHOTO_CONVERTIBLE);
        }
    }
    private void openCamaraAdicionalDocumento(String id) {

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
            String imageName = fecha + "_Foto_Adicional_Documento.jpg";
            ruta = file.toString() + "/" + imageName;
            mPath = ruta;

            File newFile = new File(mPath);

            correlativo = db.correlativoFotos(Integer.parseInt(id_inspeccion));
            nombreimagen = String.valueOf(id_inspeccion)+"_"+String.valueOf(correlativo)+"_Foto_Adicional_Documento.jpg";


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(documento.this,
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
                case PHOTO_DOCUMENTO:
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
                    imageDocumentoE.setImageBitmap(bitmap);
                    String imagen = foto.convertirImagenDano(bitmap);
                    db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Documento", 0, imagen);

                        Intent servis1 = new Intent(documento.this, TransferirFoto.class);
                    servis1.putExtra("comentario","Foto Documento");
                    servis1.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis1);

                    break;
                case PHOTO_PAC:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapPAC = BitmapFactory.decodeFile(mPath);
                    bitmapPAC = foto.redimensiomarImagen(bitmapPAC);
                    imagePACE.setImageBitmap(bitmapPAC);
                    String imagenPAC = foto.convertirImagenDano(bitmapPAC);
                    db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto PAC", 0, imagenPAC);

                        Intent servis2 = new Intent(documento.this, TransferirFoto.class);
                        servis2.putExtra("comentario","Foto PAC");
                        servis2.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis2);

                    break;
                case PHOTO_CARNET_AN:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapCarnetAn = BitmapFactory.decodeFile(mPath);
                    bitmapCarnetAn = foto.redimensiomarImagen(bitmapCarnetAn);
                    imageCarnetAnversoE.setImageBitmap(bitmapCarnetAn);
                    String imagenCarnetAn = foto.convertirImagenDano(bitmapCarnetAn);
                    db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Carnet Anverso", 0, imagenCarnetAn);

                        Intent servis3 = new Intent(documento.this, TransferirFoto.class);
                        servis3.putExtra("comentario","Foto Carnet Anverso");
                        servis3.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis3);

                    break;
                case PHOTO_CARNET_RE:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapCarnetRe = BitmapFactory.decodeFile(mPath);
                    bitmapCarnetRe = foto.redimensiomarImagen(bitmapCarnetRe);
                    imageCarnetReversoE.setImageBitmap(bitmapCarnetRe);
                    String imagenCarnetRe = foto.convertirImagenDano(bitmapCarnetRe);
                    db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Carnet Reversa", 0, imagenCarnetRe);

                        Intent servis4 = new Intent(documento.this, TransferirFoto.class);
                        servis4.putExtra("comentario","Foto Carnet Reversa");
                        servis4.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis4);

                    break;
                case  PHOTO_CONVERTIBLE:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    Bitmap bitmapConvertible = BitmapFactory.decodeFile(mPath);
                    bitmapConvertible = foto.redimensiomarImagen(bitmapConvertible);
                    imageConvertibleE.setImageBitmap(bitmapConvertible);
                    String imagenConvetible = foto.convertirImagenDano(bitmapConvertible);
                    db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Convertible", 0, imagenConvetible);

                        Intent servis5 = new Intent(documento.this, TransferirFoto.class);
                        servis5.putExtra("comentario","Foto Convertible");
                        servis5.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis5);

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
                    imageAdicionalDocumentoE.setImageBitmap(bitmapAdicional);
                    String imagenAdicional = foto.convertirImagenDano(bitmapAdicional);
                    db.insertaFoto(Integer.parseInt(id_inspeccion), db.correlativoFotos(Integer.parseInt(id_inspeccion)), nombreimagen, "Foto Adicional Documento", 0, imagenAdicional);

                        Intent servis6 = new Intent(documento.this, TransferirFoto.class);
                        servis6.putExtra("comentario","Foto Adicional Documento");
                        servis6.putExtra("id_inspeccion",id_inspeccion);
                        startService(servis6);

                    break;

            }
        }
    }

    private void desplegarSeccionUnoDocumento(String id)
    {
        if (btnDocumentoE.getVisibility() == View.VISIBLE) {

            btnDocumentoE.setVisibility(View.GONE);
            imageDocumentoE.setVisibility(View.GONE);
            imageDocumentoE.setImageBitmap(null);
            btnPACE.setVisibility(View.GONE);
            imagePACE.setVisibility(View.GONE);
            imagePACE.setImageBitmap(null);
            btnCarnetAnversoE.setVisibility(View.GONE);
            imageCarnetAnversoE.setVisibility(View.GONE);
            imageCarnetAnversoE.setImageBitmap(null);
            btnCarnetReversoE.setVisibility(View.GONE);
            imageCarnetReversoE.setVisibility(View.GONE);
            imageCarnetReversoE.setImageBitmap(null);
            btnConvertibleE.setVisibility(View.GONE);
            imageConvertibleE.setVisibility(View.GONE);
            imageConvertibleE.setImageBitmap(null);
            btnAdicionalDocumentoE.setVisibility(View.GONE);
            imageAdicionalDocumentoE.setVisibility(View.GONE);
            imageAdicionalDocumentoE.setImageBitmap(null);

        }
        else
        {

            String imageDocumento = db.foto(Integer.parseInt(id),"Foto Documento");
            String imagePAC = db.foto(Integer.parseInt(id),"Foto PAC");
            String imageCarnetAnverso = db.foto(Integer.parseInt(id),"Foto Carnet Anverso");
            String imageCarnetReverso = db.foto(Integer.parseInt(id),"Foto Carnet Reversa");
            String imageConvertible = db.foto(Integer.parseInt(id),"Foto Convertible");
            String imageAdicionalDocumento = db.foto(Integer.parseInt(id),"Foto Adicional Documento");

            if(imageDocumento.length()>3)
            {
                byte[] decodedString = Base64.decode(imageDocumento, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageDocumentoE.setImageBitmap(decodedByte);
            }
            if(imagePAC.length()>3)
            {
                byte[] decodedString = Base64.decode(imagePAC, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imagePACE.setImageBitmap(decodedByte);
            }
            if(imageCarnetAnverso.length()>3)
            {
                byte[] decodedString = Base64.decode(imageCarnetAnverso, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageCarnetAnversoE.setImageBitmap(decodedByte);
            }
            if(imageCarnetReverso.length()>3)
            {
                byte[] decodedString = Base64.decode(imageCarnetReverso, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageCarnetReversoE.setImageBitmap(decodedByte);
            }
            if(imageConvertible.length()>3)
            {
                byte[] decodedString = Base64.decode(imageConvertible, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageConvertibleE.setImageBitmap(decodedByte);
            }
            if(imageAdicionalDocumento.length()>3)
            {
                byte[] decodedString = Base64.decode(imageAdicionalDocumento, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageAdicionalDocumentoE.setImageBitmap(decodedByte);
            }


            btnDocumentoE.setVisibility(View.VISIBLE);
            imageDocumentoE.setVisibility(View.VISIBLE);

            try {
                if (db.consultaPAC(Integer.parseInt(id)).toString().equals("S")) {
                    btnPACE.setVisibility(View.VISIBLE);
                    imagePACE.setVisibility(View.VISIBLE);
                    btnCarnetAnversoE.setVisibility(View.VISIBLE);
                    imageCarnetAnversoE.setVisibility(View.VISIBLE);
                    btnCarnetReversoE.setVisibility(View.VISIBLE);
                    imageCarnetReversoE.setVisibility(View.VISIBLE);
                }
            }catch(Exception e){
                Log.e("Error pac",e.getMessage()+'-'+db.consultaPAC(Integer.parseInt(id)));
            }

            btnConvertibleE.setVisibility(View.VISIBLE);
            imageConvertibleE.setVisibility(View.VISIBLE);
            btnAdicionalDocumentoE.setVisibility(View.VISIBLE);
            imageAdicionalDocumentoE.setVisibility(View.VISIBLE);





        }

    }

}
