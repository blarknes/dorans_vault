package com.blarknes.doransvault.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blarknes.doransvault.R;
import com.blarknes.doransvault.model.Usuario;

import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter {

    private List<Usuario> usuarioList;
    private Context contexto;
    private OnRecycleListener onRecycleListener;

    public UsuarioAdapter(List<Usuario> usuarios, Context contexto, OnRecycleListener onRecycleListener) {
        this.usuarioList = usuarios;
        this.contexto = contexto;
        this.onRecycleListener = onRecycleListener;
    }

    public void atualizar(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(contexto)
                .inflate(R.layout.usuario, viewGroup, false);
        ViewHolder holder = new ViewHolder(view, onRecycleListener);
        return holder;
    }

    //CÓDIGO ADICIONADO PARA POSSIBILITAR A ATUALIZAÇÃO DO USUÁRIO
    public interface OnRecycleListener{
        void onRecycleClick(int position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder holder = (ViewHolder) viewHolder;

        Usuario usuario = usuarioList.get(i);

        holder.nome.setText(usuario.getNome());
        holder.cpf.setText(usuario.getCpf());
        holder.endereco.setText(usuario.getEndereco());
        holder.email.setText(usuario.getEmail());
        holder.telefone.setText(usuario.getTelefone());
        holder.sexo.setText(usuario.getSexo());
    }

    @Override
    public int getItemCount() {
        return usuarioList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView nome;
        final TextView cpf;
        final TextView endereco;
        final TextView email;
        final TextView telefone;
        final TextView sexo;

        OnRecycleListener onRecycleListener;

        public ViewHolder(View view, OnRecycleListener onRecycleListener) {
            super(view);
            nome = (TextView) view.findViewById(R.id.nome);
            cpf = (TextView) view.findViewById(R.id.cpf);
            endereco = (TextView) view.findViewById(R.id.endereco);
            email = (TextView) view.findViewById(R.id.email);
            telefone = (TextView) view.findViewById(R.id.telefone);
            sexo = (TextView) view.findViewById(R.id.sexo);

            this.onRecycleListener = onRecycleListener;
            view.setOnClickListener(this);
        }

        //CÓDIGO ADICIONADO PARA POSSIBILITAR A ATUALIZAÇÃO DO USUÁRIO
        @Override
        public void onClick(View view) {
            onRecycleListener.onRecycleClick(getAdapterPosition());
        }
    }
}
