package com.anncode.notificacionfirebase.RestAPI.adapter;

import com.anncode.notificacionfirebase.RestAPI.ConsantesRestAPI;
import com.anncode.notificacionfirebase.RestAPI.Endponits;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anahisalgado on 09/06/16.
 */
public class RestApiAdapter {

    public Endponits establecerConexionRestAPI(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConsantesRestAPI.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                ;

        return retrofit.create(Endponits.class);

    }
}
