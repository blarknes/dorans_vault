package com.blarknes.doransvault.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.blarknes.doransvault.DAO.ContaDAO;
import com.blarknes.doransvault.R;
import com.blarknes.doransvault.model.Conta;

public class AdicionarContaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_conta);

        configureActions();
    }

    private void configureActions() {
        Button btnGravar = findViewById(R.id.btnGravar) ;

        btnGravar.setOnClickListener(v -> {
            EditText inptNick = findViewById(R.id.inptNick);
            EditText inptLogin = findViewById(R.id.inptLogin);
            EditText inptSenha = findViewById(R.id.inptSenha);
            Spinner inptRegiao = findViewById(R.id.inptRegiao);

            String nick = inptNick.getText().toString();
            String login = inptLogin.getText().toString();
            String senha = inptSenha.getText().toString();

            String regiao = inptRegiao.getSelectedItem().toString();

            int aux = 0;

            if (nick.equalsIgnoreCase("") || nick.length() > 16)
                Toast.makeText(AdicionarContaActivity.this, "Nick Inválido", Toast.LENGTH_SHORT).show();
            else
                aux++;

            if (aux == 1) {
                Conta conta = new Conta();
                conta.setNick(nick);
                conta.setLogin(login);
                conta.setSenha(senha);
                conta.setRegiao(regiao);
                conta.setLowPriority(false);

                ContaDAO dao = new ContaDAO(AdicionarContaActivity.this);
                dao.insert(conta);

                startActivity(new Intent(AdicionarContaActivity.this, MainActivity.class));
            }
        });
    }
}
