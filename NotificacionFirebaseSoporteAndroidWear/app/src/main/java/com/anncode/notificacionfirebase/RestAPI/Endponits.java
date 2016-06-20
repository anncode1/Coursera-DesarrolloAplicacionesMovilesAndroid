package com.anncode.notificacionfirebase.RestAPI;

import com.anncode.notificacionfirebase.RestAPI.model.UsuarioResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by anahisalgado on 09/06/16.
 */
public interface Endponits {

    @FormUrlEncoded
    @POST(ConsantesRestAPI.KEY_POST_ID_TOKEN)
    Call<UsuarioResponse> registrarTokenID(@Field("token") String token, @Field("animal") String animal);

    @GET(ConsantesRestAPI.KEY_TOQUE_ANIMAL)
    Call<UsuarioResponse> toqueAnimal(@Path("id") String id, @Path("animal") String animal);

}
