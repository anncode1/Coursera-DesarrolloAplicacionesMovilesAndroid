package com.anncode.aplicacioncontactos.restApi.model;

import com.anncode.aplicacioncontactos.pojo.Contacto;

import java.util.ArrayList;

/**
 * Created by anahisalgado on 25/05/16.
 */
public class ContactoResponse {

    ArrayList<Contacto> contactos;

    public ArrayList<Contacto> getContactos() {
        return contactos;
    }

    public void setContactos(ArrayList<Contacto> contactos) {
        this.contactos = contactos;
    }
}
