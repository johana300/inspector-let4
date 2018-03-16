package com.letchile.let.Clases;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by LETCHILE on 16/02/2018.
 */

public class PropiedadesFoto {

    public PropiedadesFoto(Context context){
    }

    //CONVERTIR A BASE 64
    public String convertirImagenDano(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArrayImageMuela = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteArrayImageMuela, Base64.DEFAULT);

        return encodedImage;
    }

    //CAMBIAR LAS DIMENSIONES DE LAS FOTOS
    float anchoNuevo = 800;
    float altoNuevo = 1000;
    public Bitmap redimensiomarImagen(Bitmap bitmap) {

        int ancho = bitmap.getWidth();
        int alto = bitmap.getHeight();

        if(ancho>anchoNuevo || alto>altoNuevo)
        {
            float escalaAncho = anchoNuevo/ancho;
            float escalaAlto = altoNuevo/alto;

            Matrix matrix = new Matrix();
            matrix.postScale(escalaAncho,escalaAlto);

            return Bitmap.createBitmap(bitmap,0,0,ancho,alto,matrix,false);
        }
        else
        {
            return bitmap;
        }
    }

}
