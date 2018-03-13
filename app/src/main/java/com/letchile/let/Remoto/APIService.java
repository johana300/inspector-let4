package com.letchile.let.Remoto;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by LET-CHILE on 13-03-2018.
 */

public interface APIService {

    @POST("/posts")
    @FormUrlEncoded
    Call<POST> savePostCall(@Field("id_inspeccion") int id_inspeccion,
                            @Field("nombre_foto") String nombre_foto,
                            @Field("archivo") String archivo,
                            @Field("comentario") String comentario);


}
