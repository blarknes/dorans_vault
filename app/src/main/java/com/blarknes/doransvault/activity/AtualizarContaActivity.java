package com.blarknes.doransvault.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.blarknes.doransvault.DAO.ContaDAO;
import com.blarknes.doransvault.R;
import com.blarknes.doransvault.model.Conta;
import java.util.List;

public class AtualizarContaActivity extends AppCompatActivity {
    List<Conta> contaList;
    Conta conta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_conta);

        configureActions();
    }

    private void configureActions(){
        ContaDAO dao = new ContaDAO(this);
        contaList = dao.getAll();
        conta = MainActivity.getConta();

        final EditText inptNick = findViewById(R.id.inptNick);
        final EditText inptLogin = findViewById(R.id.inptLogin);
        final EditText inptSenha = findViewById(R.id.inptSenha);

        inptNick.setText(conta.getNick());
        inptLogin.setText(conta.getLogin());
        inptSenha.setText(conta.getSenha());

        final int id = conta.getId();

        Button salvar = findViewById(R.id.btnSalvar);
        Button excluir = findViewById(R.id.btnExcluir);

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nick = inptNick.getText().toString();
                String login = inptLogin.getText().toString();
                String senha = inptSenha.getText().toString();

                int aux = 0;
                //Condições para atualizar o cadastro ao banco de dados;
                //Verificação do Nome
                if (nick.equalsIgnoreCase("")) {
                    Toast.makeText(com.blarknes.doransvault.activity.AtualizarContaActivity.this, "Nome Inválido", Toast.LENGTH_SHORT).show();
                } else {
                    aux++;
                }

                if (aux == 1) {
                    Conta conta = new Conta();

                    conta.setNick(nick);
                    conta.setLogin(login);
                    conta.setSenha(senha);
                    conta.setId(id);

                    ContaDAO dao = new ContaDAO(com.blarknes.doransvault.activity.AtualizarContaActivity.this);
                    dao.update(conta);

                    //AtualizarUsuarioActivity.this.finish();
                    startActivity(new Intent(com.blarknes.doransvault.activity.AtualizarContaActivity.this, MainActivity.class));
                }
            }
        });

        excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContaDAO dao = new ContaDAO(com.blarknes.doransvault.activity.AtualizarContaActivity.this);
                dao.delete(conta);
                //AtualizarUsuarioActivity.this.finish();
                startActivity(new Intent(com.blarknes.doransvault.activity.AtualizarContaActivity.this, MainActivity.class));
            }
        });
    }
}
