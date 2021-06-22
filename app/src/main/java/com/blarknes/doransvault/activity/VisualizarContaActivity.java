package com.blarknes.doransvault.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blarknes.doransvault.DAO.ContaDAO;
import com.blarknes.doransvault.R;
import com.blarknes.doransvault.model.Conta;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class VisualizarContaActivity extends AppCompatActivity {
    List<Conta> contaList;
    Conta conta;
    ProgressDialog progressDialog;
    String html, url, eloHolder, pdlHolder;
    TextView nick, login, senha, elo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_conta);

        ContaDAO dao = new ContaDAO(this);
        contaList = dao.getAll();
        conta = MainActivity.getConta();

        nick = findViewById(R.id.nick);
        login = findViewById(R.id.login);
        senha = findViewById(R.id.senha);
        elo = findViewById(R.id.elo);

        nick.setText(conta.getNick());
        login.setText(conta.getLogin());
        senha.setText(conta.getSenha());

        url = "https://www.leagueofgraphs.com/summoner/br/" + nick.getText().toString();

        final int id = conta.getId();

        new InternetTask().execute();
    }

    private class InternetTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(VisualizarContaActivity.this);
            progressDialog.setMessage("parsing...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(url).get();
                //html = doc.toString();

                Element htmlNick = doc.select("div.txt > h2").first();
                Element htmlLevel = doc.select("div.bannerSubtitle").first();
                Element htmlElo = doc.select("div.leagueTier").first();
                Element htmlPDL = doc.select("span.leaguePoints ").first();

                eloHolder = htmlElo.text();
                pdlHolder = htmlPDL.text();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            elo.setText(eloHolder + " - " + pdlHolder + " pdl");

            progressDialog.dismiss();
        }
    }
}