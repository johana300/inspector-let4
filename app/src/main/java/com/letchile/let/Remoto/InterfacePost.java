package com.letchile.let.Remoto;

import com.letchile.let.Clases.LoginEnv;
import com.letchile.let.Clases.LoginResp;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;


/**
 * Created by LET-CHILE on 14-03-2018.
 */

public interface InterfacePost {


    /*@FormUrlEncoded
    @POST("login/verificaLogeo")
    public void logeoUser(@Field("usr") String usr, @Field("pwd") String pwd, Callback<LoginResp> callback);
    //Call<LoginResp> getAcceso(@Path("usr") String usr, @Path("pwd") String pwd,CallB);
    //Call<LoginResp> getAcceso(@Body LoginEnv loginEnv);*/

    @FormUrlEncoded
    @POST("/login/verificaLogeo")
    public void logeoUser(@Field("usr") String usr, @Field("pwd") String pwd, Callback<Response> callback);


}
