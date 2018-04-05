package com.letchile.let.Remoto;

import com.letchile.let.Remoto.Data.Env_FotoEnviada;
import com.letchile.let.Remoto.Data.LoginResp;
import com.letchile.let.Remoto.Data.Resp_CambioRamo;
import com.letchile.let.Remoto.Data.Resp_FotoEnviada;
import com.letchile.let.Remoto.Data.oiRangoHorario;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by LET-CHILE on 14-03-2018.
 */

public interface InterfacePost {

    //LOGIN
    @FormUrlEncoded
    @POST("login/verificaLogeo")
    Call<LoginResp> getAcceso(@Field("usr") String usr, @Field("pwd") String pwd);


    //CAMBIAR RAMO
    @FormUrlEncoded
    @POST("cargamovil/cambiaRamo")
    Call<Resp_CambioRamo> getRamo(@Field("id_inspeccion") String id_inspeccion, @Field("comboRamo") String ramo);


    //TRAER RANGO HORARIO
    @FormUrlEncoded
    @POST("cargamovil/oiRangoHorario")
    Call<oiRangoHorario> getRango(@Field("id_inspeccion")String id_inspeccion, @Field("usr") String usr);


    //ENVIAR FOTOS
    //@Headers("Content-Type: application/json")
    @FormUrlEncoded
    @POST("cargamovil/descargafotos64")
    //Call<Resp_FotoEnviada> getFotoEnv(@Body Env_FotoEnviada body);
    Call<Resp_FotoEnviada> getFotoEnv(@Field("id_inspeccion")String id_inspeccion,@Field("nombre_foto") String nombre_foto, @Field("archivo") String archivo,@Field("comentario") String comentario);
    /*Call<Resp_FotoEnviada> getFotoEnv(
            @Part("id_inspeccion")RequestBody id_inspeccion,
            @Part("nombre_foto")RequestBody nombre_foto,
            @Part("comentario")RequestBody comentario,
            @Part("archivo")RequestBody archivo);*/

    //prueba final
}
