package com.blarknes.doransvault.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.blarknes.doransvault.DAO.ContaDAO;
import com.blarknes.doransvault.R;
import com.blarknes.doransvault.model.Conta;
import java.util.List;

public class ContaAdapter extends RecyclerView.Adapter {
    private List<Conta> contaList;
    private Context contexto;
    private OnRecycleListener onRecycleListener;

    public ContaAdapter(List<Conta> contas, Context contexto, OnRecycleListener onRecycleListener) {
        this.contaList = contas;
        this.contexto = contexto;
        this.onRecycleListener = onRecycleListener;
    }

    public void atualizar(List<Conta> contaList) {
        this.contaList = contaList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(contexto).inflate(R.layout.conta, viewGroup, false);
        ViewHolder holder = new ViewHolder(view, onRecycleListener);
        return holder;
    }

    //code for db update
    public interface OnRecycleListener{
        void onRecycleClick(int position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder holder = (ViewHolder) viewHolder;

        Conta conta = contaList.get(i);

        holder.nick.setText(conta.getNick() + " ("+conta.getRegiao()+")");
        try{
            holder.lowPrio.setChecked(conta.getLowPriority());
        } catch (Exception e) {
            holder.lowPrio.setChecked(false);
        }

        holder.lowPrio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                conta.setLowPriority(true);
            else
                conta.setLowPriority(false);

            ContaDAO dao = new ContaDAO(contexto);
            dao.update(conta);
        });

        /*holder.login.setText(conta.getLogin());*/
    }

    @Override
    public int getItemCount() {
        return contaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView nick;
        final Switch lowPrio;
        /*final TextView login;*/

        OnRecycleListener onRecycleListener;

        public ViewHolder(View view, OnRecycleListener onRecycleListener) {
            super(view);
            nick = (TextView) view.findViewById(R.id.nick);
            lowPrio = (Switch) view.findViewById(R.id.switch1);
            /*login = (TextView) view.findViewById(R.id.login);*/

            this.onRecycleListener = onRecycleListener;
            view.setOnClickListener(this);
        }

        //code for db update
        @Override
        public void onClick(View view) {
            onRecycleListener.onRecycleClick(getAdapterPosition());
        }
    }
}
