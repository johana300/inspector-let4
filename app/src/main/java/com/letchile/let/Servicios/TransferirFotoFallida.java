package com.letchile.let.Servicios;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.letchile.let.BD.DBprovider;

/**
 * Created by LETCHILE on 07/03/2018.
 */

public class TransferirFotoFallida extends Service{
    DBprovider db;

    Boolean connect = false;

    public TransferirFotoFallida(){
        db = new DBprovider(this);
    }

    public int onStartCommand(Intent intent,int flags,int startId){

        String id_inspeccion = (String) intent.getExtras().get("id_inspeccion");
        String id_foto = (String) intent.getExtras().get("id_foto");


        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //asdasdasd


}
