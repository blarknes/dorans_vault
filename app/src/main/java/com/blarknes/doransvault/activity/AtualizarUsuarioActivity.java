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

public class AtualizarUsuarioActivity extends AppCompatActivity {

    List<Usuario> usuarioList;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_usuario);

        configureActions();
    }

    private void configureActions(){
        UsuarioDAO dao = new UsuarioDAO(this);
        usuarioList = dao.getAll();
        usuario = MainActivity.getUsuario();

        final EditText inptNome = findViewById(R.id.inptNome);
        final EditText inptCpf = findViewById(R.id.inptCpf);
        final EditText inptEndereco = findViewById(R.id.inptEndereco);
        final EditText inptEmail = findViewById(R.id.inptEmail);
        final EditText inptTelefone = findViewById(R.id.inptTelefone);

        final Spinner sexoSpinner = (Spinner) findViewById(R.id.inptSexo);
        List<String> sexoArray = new ArrayList<String>();
        sexoArray.add("Masculino");
        sexoArray.add("Feminino");
        ArrayAdapter<String> sexoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sexoArray);
        sexoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexoSpinner.setAdapter(sexoAdapter);
        if (usuario.getSexo().equals("Feminino")) {
            sexoSpinner.setSelection(1);
        }

        inptNome.setText(usuario.getNome());
        inptCpf.setText(usuario.getCpf());
        inptEndereco.setText(usuario.getEndereco());
        inptEmail.setText(usuario.getEmail());
        inptTelefone.setText(usuario.getTelefone());

        final int id = usuario.getId();

        Button salvar = findViewById(R.id.btnSalvar);
        Button excluir = findViewById(R.id.btnExcluir);

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = inptNome.getText().toString();
                String cpf = inptCpf.getText().toString();
                String endereco = inptEndereco.getText().toString();
                String email = inptEmail.getText().toString();
                String telefone = inptTelefone.getText().toString();
                String sexo = sexoSpinner.getSelectedItem().toString();

                int aux = 0;
                //Condições para atualizar o cadastro ao banco de dados;
                //Verificação do Nome
                if (nome.equalsIgnoreCase("")) {
                    Toast.makeText(com.blarknes.doransvault.activity.AtualizarUsuarioActivity.this, "Nome Inválido", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(com.blarknes.doransvault.activity.AtualizarUsuarioActivity.this, "CPF Inválido", Toast.LENGTH_SHORT).show();
                    } else {
                        aux++;
                    }
                }else{
                    Toast.makeText(com.blarknes.doransvault.activity.AtualizarUsuarioActivity.this, "CPF Inválido", Toast.LENGTH_SHORT).show();
                }

                //Verificação do Endereço
                if (endereco.equalsIgnoreCase("")) {
                    Toast.makeText(com.blarknes.doransvault.activity.AtualizarUsuarioActivity.this, "Endereço Inválido", Toast.LENGTH_SHORT).show();
                } else {
                    aux++;
                }

                //Verificação do Email
                for (int i = 0; i < email.length(); i++){
                    if (email.indexOf("@") == -1 && email.indexOf(".") == -1){
                        Toast.makeText(com.blarknes.doransvault.activity.AtualizarUsuarioActivity.this, "E-mail Inválido", Toast.LENGTH_SHORT).show();
                    }else{
                        aux++;
                        break;
                    }
                }

                //Verificando o Número de Telefone
                if (telefone.length() != 11){
                    Toast.makeText(com.blarknes.doransvault.activity.AtualizarUsuarioActivity.this, "Telefone Inválido", Toast.LENGTH_SHORT).show();
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
                    usuario.setSexo(sexo);
                    usuario.setId(id);

                    UsuarioDAO dao = new UsuarioDAO(com.blarknes.doransvault.activity.AtualizarUsuarioActivity.this);
                    dao.update(usuario);

                    //AtualizarUsuarioActivity.this.finish();
                    startActivity(new Intent(com.blarknes.doransvault.activity.AtualizarUsuarioActivity.this, MainActivity.class));
                }
            }
        });

        excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsuarioDAO dao = new UsuarioDAO(com.blarknes.doransvault.activity.AtualizarUsuarioActivity.this);
                dao.delete(usuario);
                //AtualizarUsuarioActivity.this.finish();
                startActivity(new Intent(com.blarknes.doransvault.activity.AtualizarUsuarioActivity.this, MainActivity.class));
            }
        });
    }
}
