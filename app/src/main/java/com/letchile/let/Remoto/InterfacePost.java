package com.letchile.let.Remoto;

import com.letchile.let.Remoto.Data.LoginResp;
import com.letchile.let.Remoto.Data.Resp_CambioRamo;
import com.letchile.let.Remoto.Data.Resp_FotoEnviada;
import com.letchile.let.Remoto.Data.oiRangoHorario;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

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
    @FormUrlEncoded
    @POST("cargamovil/descargafotos64")
    Call<Resp_FotoEnviada> getFotoEnv(@Field("id_inspeccion")String id_inspeccion,@Field("nombre_foto") String nombre_foto,@Field("comentario") String comentario, @Field("archivo") String archivo);


    //prueba final
}
