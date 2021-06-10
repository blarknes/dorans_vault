package com.blarknes.doransvault.DAO;

import android.content.Context;

import com.blarknes.doransvault.model.Usuario;

public class UsuarioDAO extends GenericDAO {

    public UsuarioDAO(Context context) {
        super(context, Usuario.class);
    }
}
