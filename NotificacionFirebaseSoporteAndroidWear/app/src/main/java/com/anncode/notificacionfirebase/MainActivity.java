package com.anncode.notificacionfirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.anncode.notificacionfirebase.RestAPI.Endponits;
import com.anncode.notificacionfirebase.RestAPI.adapter.RestApiAdapter;
import com.anncode.notificacionfirebase.RestAPI.model.UsuarioResponse;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String ANIMAL_EMISOR = "Unicornio";
    private static final String ANIMAL_RECEPTOR = "Perro";
    private static final String PERRO = "-KKI-JIngAMiEh3zOogs";
    private static final String UNICORNIO = "-KKG6wZdReyp7F0fkODI";
    private static final String ID_RECEPTOR = PERRO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }

    }

    public void enviarToken(View v){
        String token = FirebaseInstanceId.getInstance().getToken();
        enviarTokenRegistro(token);
    }

    private void enviarTokenRegistro(String token){
        Log.d("TOKEN", token);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endponits endponits = restApiAdapter.establecerConexionRestAPI();
        Call<UsuarioResponse> usuarioResponseCall = endponits.registrarTokenID(token, ANIMAL_EMISOR);

        usuarioResponseCall.enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                UsuarioResponse usuarioResponse = response.body();
                Log.d("ID_FIREBASE", usuarioResponse.getId());
                Log.d("TOKEN_FIREBASE", usuarioResponse.getToken());

            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {

            }
        });
    }

    public void toqueAnimal(View v){
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
