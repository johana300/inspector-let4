package com.letchile.let.Remoto;

import com.letchile.let.Remoto.Data.LoginResp;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by LET-CHILE on 14-03-2018.
 */

public interface InterfacePost {

    @FormUrlEncoded
    @POST("login/verificaLogeo")
    Call<LoginResp> getAcceso(@Field("usr") String usr, @Field("pwd") String pwd);

}
