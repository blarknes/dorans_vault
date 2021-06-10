package com.blarknes.doransvault.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blarknes.doransvault.DAO.DatabaseManager;
import com.blarknes.doransvault.DAO.UsuarioDAO;
import com.blarknes.doransvault.R;
import com.blarknes.doransvault.model.Usuario;

import java.util.List;

public class MainActivity extends AppCompatActivity implements UsuarioAdapter.OnRecycleListener {

    List<Usuario> usuarioList;
    private static Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseManager.init(this);

        initGUI();
        configureActions();
    }

    private void initGUI() {
        UsuarioDAO dao = new UsuarioDAO(this);
        usuarioList = dao.getAll();

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setAdapter(new UsuarioAdapter(usuarioList, this, this));
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);
    }

    private void configureActions() {
        Button btnAdicionar = findViewById(R.id.adicionar);
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(com.blarknes.doransvault.activity.MainActivity.this, AdicionarUsuarioActivity.class);
                startActivity(myIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        UsuarioDAO dao = new UsuarioDAO(this);
        List<Usuario> usuarioList = dao.getAll();
        RecyclerView recyclerView = findViewById(R.id.recycler);
        UsuarioAdapter adapter = (UsuarioAdapter) recyclerView.getAdapter();
        adapter.atualizar(usuarioList);
    }

    //CÓDIGO ADICIONADO PARA POSSIBILITAR A ATUALIZAÇÃO DO USUÁRIO
    @Override
    public void onRecycleClick(int position) {
        this.setUsuario(usuarioList.get(position));
        Intent myIntent = new Intent(com.blarknes.doransvault.activity.MainActivity.this, AtualizarUsuarioActivity.class);
        startActivity(myIntent);
    }

    //CÓDIGO ADICIONADO PARA POSSIBILITAR A ATUALIZAÇÃO DO USUÁRIO
    public static Usuario getUsuario() {
        return usuario;
    }

    //CÓDIGO ADICIONADO PARA POSSIBILITAR A ATUALIZAÇÃO DO USUÁRIO
    public static void setUsuario(Usuario usuario) {
        com.blarknes.doransvault.activity.MainActivity.usuario = usuario;
    }
}
