package com.anncode.notificacionfirebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anncode.notificacionfirebase.RestAPI.Endponits;
import com.anncode.notificacionfirebase.RestAPI.adapter.RestApiAdapter;
import com.anncode.notificacionfirebase.RestAPI.model.UsuarioResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anahisalgado on 15/06/16.
 */
public class ToqueAnimal extends BroadcastReceiver {

    private static final String ANIMAL_RECEPTOR = "Perro";
    private static final String ANIMAL_EMISOR = "Unicornio";
    private static final String PERRO = "-KKI-JIngAMiEh3zOogs";
    private static final String UNICORNIO = "-KKG6wZdReyp7F0fkODI";
    private static final String ID_RECEPTOR = PERRO;


    @Override
    public void onReceive(Context context, Intent intent) {
        String ACTION_KEY = "TOQUE_ANIMAL";
        String accion = intent.getAction();

        if (ACTION_KEY.equals(accion)){
            toqueAnimal();
            Toast.makeText(context, "Diste un toque a " + ANIMAL_RECEPTOR, Toast.LENGTH_SHORT).show();
        }
    }

    public void toqueAnimal(){
        Log.d("TOQUE_ANIMAL", "true");
        final UsuarioResponse usuarioResponse = new UsuarioResponse(ID_RECEPTOR, "123", ANIMAL_RECEPTOR);
        RestApiAdapter restApiAdapter =  new RestApiAdapter();
        Endponits endponits = restApiAdapter.establecerConexionRestAPI();
        Call<UsuarioResponse> usuarioResponseCall = endponits.toqueAnimal(usuarioResponse.getId(), ANIMAL_EMISOR);
        usuarioResponseCall.enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                UsuarioResponse usuarioResponse1 =response.body();
                Log.d("ID_FIREBASE", usuarioResponse1.getId());
                Log.d("TOKEN_FIREBASE", usuarioResponse1.getToken());
                Log.d("ANIMAL_FIREBASE", usuarioResponse1.getAnimal());
            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {

            }
        });
    }
}
