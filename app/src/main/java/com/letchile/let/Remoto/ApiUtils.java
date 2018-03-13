package com.letchile.let.Remoto;

/**
 * Created by LET-CHILE on 13-03-2018.
 */

public class ApiUtils {

    private ApiUtils(){}

    private static final String BASE_URL = "https://www.autoagenda.cl/movil/cargamovil/";

    private static APIService getApiService(){
        return RetrofitCliente.getClient(BASE_URL).create(APIService.class);
    }

}
