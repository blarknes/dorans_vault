package com.blarknes.doransvault.DAO;

import android.content.Context;

import com.blarknes.doransvault.model.Conta;

public class ContaDAO extends GenericDAO {

    public ContaDAO(Context context) {
        super(context, Conta.class);
    }
}
