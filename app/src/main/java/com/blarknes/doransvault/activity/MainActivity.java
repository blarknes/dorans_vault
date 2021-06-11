package com.blarknes.doransvault.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blarknes.doransvault.DAO.ContaDAO;
import com.blarknes.doransvault.DAO.DatabaseManager;
import com.blarknes.doransvault.R;
import com.blarknes.doransvault.model.Conta;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ContaAdapter.OnRecycleListener {

    List<Conta> contaList;
    private static Conta conta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseManager.init(this);

        initGUI();
        configureActions();
    }

    private void initGUI() {
        ContaDAO dao = new ContaDAO(this);
        contaList = dao.getAll();

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setAdapter(new ContaAdapter(contaList, this, this));
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);
    }

    private void configureActions() {
        Button btnAdicionar = findViewById(R.id.adicionar);
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(com.blarknes.doransvault.activity.MainActivity.this, AdicionarContaActivity.class);
                startActivity(myIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ContaDAO dao = new ContaDAO(this);
        List<Conta> contaList = dao.getAll();
        RecyclerView recyclerView = findViewById(R.id.recycler);
        ContaAdapter adapter = (ContaAdapter) recyclerView.getAdapter();
        adapter.atualizar(contaList);
    }

    //CÓDIGO ADICIONADO PARA POSSIBILITAR A ATUALIZAÇÃO DO USUÁRIO
    @Override
    public void onRecycleClick(int position) {
        this.setConta(contaList.get(position));
        Intent myIntent = new Intent(com.blarknes.doransvault.activity.MainActivity.this, AtualizarContaActivity.class);
        startActivity(myIntent);
    }

    //CÓDIGO ADICIONADO PARA POSSIBILITAR A ATUALIZAÇÃO DO USUÁRIO
    public static Conta getConta() {
        return conta;
    }

    //CÓDIGO ADICIONADO PARA POSSIBILITAR A ATUALIZAÇÃO DO USUÁRIO
    public static void setConta(Conta conta) {
        com.blarknes.doransvault.activity.MainActivity.conta = conta;
    }
}
