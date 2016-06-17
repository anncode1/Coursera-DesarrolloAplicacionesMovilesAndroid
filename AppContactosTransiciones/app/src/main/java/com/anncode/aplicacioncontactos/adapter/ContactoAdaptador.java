package com.anncode.aplicacioncontactos.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anncode.aplicacioncontactos.db.ConstructorContactos;
import com.anncode.aplicacioncontactos.pojo.Contacto;
import com.anncode.aplicacioncontactos.DetalleContacto;
import com.anncode.recyclerviewfragments.R;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

/**
 * Created by anahisalgado on 19/04/16.
 */
public class ContactoAdaptador extends RecyclerView.Adapter<ContactoAdaptador.ContactoViewHolder>{

    ArrayList<Contacto> contactos;
    Activity activity;

    public ContactoAdaptador(ArrayList<Contacto> contactos, Activity activity) {
        this.contactos = contactos;
        this.activity = activity;
    }

    @Override
    public ContactoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_grid_contacto, parent, false);
        return new ContactoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ContactoViewHolder contactoViewHolder, int position) {
        final Contacto contacto = contactos.get(position);
        //contactoViewHolder.imgFoto.setImageResource(contacto.getUrlFoto());
        Picasso.with(activity)
                .load(contacto.getUrlFoto())
                .placeholder(R.drawable.shock_rave_bonus_icon)
                .into(contactoViewHolder.imgFoto);
        //contactoViewHolder.tvNombre.setText(contacto.getNombre());
        //contactoViewHolder.tvTelefono.setText(contacto.getTelefono());
        contactoViewHolder.tvLikes.setText(String.valueOf(contacto.getLikes()));
        
        contactoViewHolder.imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(activity, contacto.getNombre(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(activity, DetalleContacto.class);
                intent.putExtra("url", contacto.getUrlFoto());
                intent.putExtra("like", contacto.getLikes());
                //intent.putExtra("email", contacto.getEmail());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    Explode explode = new Explode();
                    explode.setDuration(1000);
                    activity.getWindow().setExitTransition(explode);
                    activity.startActivity(intent,
                            ActivityOptionsCompat.makeSceneTransitionAnimation(activity, v, activity.getString(R.string.transicion_foto)).toBundle());
                }else {
                    activity.startActivity(intent);
                }



            }
        });

        /*contactoViewHolder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Diste like a " + contacto.getNombre(),
                        Toast.LENGTH_SHORT).show();


                ConstructorContactos constructorContactos = new ConstructorContactos(activity);
                constructorContactos.darLikeCotnacto(contacto);
                contactoViewHolder.tvLikes.setText(constructorContactos.obtenerLikesContacto(contacto) + " Likes");


            }
        });*/


    }

    @Override
    public int getItemCount() {
        return contactos.size();
    }

    public static class ContactoViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgFoto;
        //private TextView tvNombre;
        //private TextView tvTelefono;
        //private ImageButton btnLike;
        private TextView tvLikes;

        public ContactoViewHolder(View itemView) {
            super(itemView);

            imgFoto     = (ImageView) itemView.findViewById(R.id.imgFoto);
            /*tvNombre    = (TextView) itemView.findViewById(R.id.tvNombreCV);
            tvTelefono  = (TextView) itemView.findViewById(R.id.tvTelefonoCV);
            btnLike     = (ImageButton) itemView.findViewById(R.id.btnLike);
            */
            tvLikes     = (TextView) itemView.findViewById(R.id.tvLikes);

        }
    }
}
