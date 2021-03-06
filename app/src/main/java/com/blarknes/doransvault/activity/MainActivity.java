package com.blarknes.doransvault.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.blarknes.doransvault.DAO.ContaDAO;
import com.blarknes.doransvault.DAO.DatabaseManager;
import com.blarknes.doransvault.R;
import com.blarknes.doransvault.model.Conta;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);
    }

    private void configureActions() {
        FloatingActionButton btnAdicionar = findViewById(R.id.adicionar);
        btnAdicionar.setOnClickListener(v -> {
            Intent myIntent = new Intent(MainActivity.this, AdicionarContaActivity.class);
            startActivity(myIntent);
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

    //code for db update
    @Override
    public void onRecycleClick(int position) {
        this.setConta(contaList.get(position));
        Intent myIntent = new Intent(com.blarknes.doransvault.activity.MainActivity.this, VisualizarContaActivity.class);
        startActivity(myIntent);
    }

    //code for db update
    public static Conta getConta() {
        return conta;
    }

    //code for db update
    public static void setConta(Conta conta) {
        com.blarknes.doransvault.activity.MainActivity.conta = conta;
    }
}
