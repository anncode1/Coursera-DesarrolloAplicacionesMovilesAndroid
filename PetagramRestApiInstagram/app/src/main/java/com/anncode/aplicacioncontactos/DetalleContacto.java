package com.anncode.aplicacioncontactos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anncode.recyclerviewfragments.R;
import com.squareup.picasso.Picasso;

/**
 * Created by anahisalgado on 19/04/16.
 */
public class DetalleContacto extends AppCompatActivity {
    private static final String KEY_EXTRA_URL = "url";
    private static final String KEY_EXTRA_LIKES = "like";
    //private TextView tvNombre;
    //private TextView tvTelefono;
    //private TextView tvEmail;
    private ImageView imgFotoDetalle;
    private TextView tvLikesDetalle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_contacto_foto);

        Bundle extras = getIntent().getExtras();
        String url   = extras.getString(KEY_EXTRA_URL);
        int likes    = extras.getInt(KEY_EXTRA_LIKES);


        tvLikesDetalle     = (TextView) findViewById(R.id.tvLikesDetalle);
        tvLikesDetalle.setText(String.valueOf(likes));

        imgFotoDetalle = (ImageView) findViewById(R.id.imgFotoDetalle);
        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.shock_rave_bonus_icon)
                .into(imgFotoDetalle);

    }

    /*
    public void llamar(View v) {
        String telefono = tvTelefono.getText().toString();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telefono)));

    }

    public void enviarMail(View v){
        String email = tvEmail.getText().toString();
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Email "));
    }
    */
}
