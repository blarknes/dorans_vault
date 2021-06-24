package com.blarknes.doransvault.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Conta {
    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    private int id;

    @DatabaseField
    private String nick;

    @DatabaseField
    private String login;

    @DatabaseField
    private String senha;

    @DatabaseField
    private String regiao;

    @DatabaseField
    private Boolean lowPriority;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public Boolean getLowPriority() {
        return lowPriority;
    }

    public void setLowPriority(Boolean lowPriority) {
        this.lowPriority = lowPriority;
    }
}
