package com.letchile.let.Clases;

/**
 * Created by LET-CHILE on 13-03-2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RespuestaServicioFoto {

    @SerializedName("MSJ")
    @Expose
    private String mSJ;

    public String getMSJ() {
        return mSJ;
    }

    public void setMSJ(String mSJ) {
        this.mSJ = mSJ;
    }



}