package com.blarknes.doransvault.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blarknes.doransvault.DAO.UsuarioDAO;
import com.blarknes.doransvault.R;
import com.blarknes.doransvault.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class AdicionarUsuarioActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_usuario);

        configureActions();
    }

    private void configureActions() {
        Button btnGravar = findViewById(R.id.btnGravar) ;
        final Spinner sexoSpinner = (Spinner) findViewById(R.id.inptSexo);

        final List<String> sexoArray = new ArrayList<String>();
        sexoArray.add("Masculino");
        sexoArray.add("Feminino");

        ArrayAdapter<String> sexoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sexoArray);
        sexoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexoSpinner.setAdapter(sexoAdapter);

        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText inptNome = findViewById(R.id.inptNome);
                EditText inptCpf = findViewById(R.id.inptCpf);
                EditText inptEndereco = findViewById(R.id.inptEndereco);
                EditText inptEmail = findViewById(R.id.inptEmail);
                EditText inptTelefone = findViewById(R.id.inptTelefone);


                String nome = inptNome.getText().toString();
                String cpf = inptCpf.getText().toString();
                String endereco = inptEndereco.getText().toString();
                String email = inptEmail.getText().toString();
                String telefone = inptTelefone.getText().toString();
                String sexo = sexoSpinner.getSelectedItem().toString();

                int aux = 0;
                //Condições para adicionar o cadastro ao banco de dados;
                //Verificação do Nome
                if (nome.equalsIgnoreCase("")) {
                    Toast.makeText(com.blarknes.doransvault.activity.AdicionarUsuarioActivity.this, "Nome Inválido", Toast.LENGTH_SHORT).show();
                } else {
                    aux++;
                }

                //Verificação do CPF
                int cpfNumbers = 0;
                for (int i = 0; i < cpf.length(); i++){
                    cpfNumbers += Integer.parseInt(String.valueOf(cpf.charAt(i)));
                }
                if (cpf.length() >= 9){
                    if (cpfNumbers % 11 != 0){
                        Toast.makeText(com.blarknes.doransvault.activity.AdicionarUsuarioActivity.this, "CPF Inválido", Toast.LENGTH_SHORT).show();
                    } else {
                        aux++;
                    }
                }else{
                    Toast.makeText(com.blarknes.doransvault.activity.AdicionarUsuarioActivity.this, "CPF Inválido", Toast.LENGTH_SHORT).show();
                }

                //Verificação do Endereço
                if (endereco.equalsIgnoreCase("")) {
                    Toast.makeText(com.blarknes.doransvault.activity.AdicionarUsuarioActivity.this, "Endereço Inválido", Toast.LENGTH_SHORT).show();
                } else {
                    aux++;
                }

                //Verificação do Email
                for (int i = 0; i < email.length(); i++){
                    if (email.indexOf("@") == -1 && email.indexOf(".") == -1){
                        Toast.makeText(com.blarknes.doransvault.activity.AdicionarUsuarioActivity.this, "E-mail Inválido", Toast.LENGTH_SHORT).show();
                    }else{
                        aux++;
                        break;
                    }
                }

                //Verificando o Número de Telefone
                if (telefone.length() != 11){
                    Toast.makeText(com.blarknes.doransvault.activity.AdicionarUsuarioActivity.this, "Telefone Inválido", Toast.LENGTH_SHORT).show();
                }else{
                    aux++;
                }

                Log.v("teste", ""+aux);
                if (aux == 5) {
                    Usuario usuario = new Usuario();
                    usuario.setNome(nome);
                    usuario.setCpf(cpf);
                    usuario.setEndereco(endereco);
                    usuario.setEmail(email);
                    usuario.setTelefone(telefone);
                    usuario.setSexo(sexoSpinner.getSelectedItem().toString());

                    UsuarioDAO dao = new UsuarioDAO(com.blarknes.doransvault.activity.AdicionarUsuarioActivity.this);
                    dao.insert(usuario);

                    //AdicionarUsuarioActivity.this.finish();
                    startActivity(new Intent(com.blarknes.doransvault.activity.AdicionarUsuarioActivity.this, MainActivity.class));
                }
            }
        });
    }

}
