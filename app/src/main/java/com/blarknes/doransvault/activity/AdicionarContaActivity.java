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

public class AdicionarContaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_conta);

        configureActions();
    }

    private void configureActions() {
        Button btnGravar = findViewById(R.id.btnGravar) ;

        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText inptNick = findViewById(R.id.inptNick);
                EditText inptLogin = findViewById(R.id.inptLogin);
                EditText inptSenha = findViewById(R.id.inptSenha);

                String nick = inptNick.getText().toString();
                String login = inptLogin.getText().toString();
                String senha = inptSenha.getText().toString();

                int aux = 0;
                //Condições para adicionar o cadastro ao banco de dados;
                //Verificação do Nome
                if (nick.equalsIgnoreCase("")) {
                    Toast.makeText(com.blarknes.doransvault.activity.AdicionarContaActivity.this, "Nick Inválido", Toast.LENGTH_SHORT).show();
                } else {
                    aux++;
                }

                /*//Verificação do CPF
                int cpfNumbers = 0;
                for (int i = 0; i < cpf.length(); i++){
                    cpfNumbers += Integer.parseInt(String.valueOf(cpf.charAt(i)));
                }
                if (cpf.length() >= 9){
                    if (cpfNumbers % 11 != 0){
                        Toast.makeText(com.blarknes.doransvault.activity.AdicionarContaActivity.this, "CPF Inválido", Toast.LENGTH_SHORT).show();
                    } else {
                        aux++;
                    }
                }else{
                    Toast.makeText(com.blarknes.doransvault.activity.AdicionarContaActivity.this, "CPF Inválido", Toast.LENGTH_SHORT).show();
                }*/

                if (aux == 1) {
                    Conta conta = new Conta();
                    conta.setNick(nick);
                    conta.setLogin(login);
                    conta.setSenha(senha);

                    ContaDAO dao = new ContaDAO(com.blarknes.doransvault.activity.AdicionarContaActivity.this);
                    dao.insert(conta);

                    //AdicionarContaActivity.this.finish();
                    startActivity(new Intent(com.blarknes.doransvault.activity.AdicionarContaActivity.this, MainActivity.class));
                }
            }
        });
    }

}
