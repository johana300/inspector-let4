package com.letchile.let.Remoto;

import com.letchile.let.Clases.LoginEnv;
import com.letchile.let.Clases.LoginResp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by LET-CHILE on 14-03-2018.
 */

public interface InterfacePost {


    @POST("login Usuario")
    Call<LoginResp> getAcceso(@Body LoginEnv loginEnv);

}
