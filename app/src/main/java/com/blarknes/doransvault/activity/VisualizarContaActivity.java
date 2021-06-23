package com.blarknes.doransvault.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.blarknes.doransvault.DAO.ContaDAO;
import com.blarknes.doransvault.R;
import com.blarknes.doransvault.model.Conta;

import java.io.InputStream;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class VisualizarContaActivity extends AppCompatActivity {
    List<Conta> contaList;
    Conta conta;
    ProgressDialog progressDialog;
    String url, eloHolder, nickHolder, subtitleHolder;
    ImageView imgElo, imgIcon;
    TextView nick, login, senha, elo, subtitle;
    Integer c = 0;
    Bitmap bitmapElo, bitmapIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_conta);

        ConfigureActions();
        new InternetTask().execute();
    }

    private void ConfigureActions(){
        ContaDAO dao = new ContaDAO(this);
        contaList = dao.getAll();
        conta = MainActivity.getConta();

        nick = findViewById(R.id.nick);
        login = findViewById(R.id.login);
        senha = findViewById(R.id.senha);
        subtitle = findViewById(R.id.subtitle);
        elo = findViewById(R.id.elo);
        imgElo = findViewById(R.id.imageElo);
        imgIcon = findViewById(R.id.imageIcon);

        nick.setText(conta.getNick());
        login.setText(conta.getLogin());
        senha.setText(conta.getSenha());

        //TODO INSERT SERVER FURTHER
        url = "https://www.leagueofgraphs.com/pt/summoner/br/" + nick.getText().toString();

        final int id = conta.getId();
    }

    private class InternetTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(VisualizarContaActivity.this);
            progressDialog.setMessage("Carregando Perfil...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(url).get();

                //Code to get the Profile Icon Image
                Element imgIconDoc = doc.select("div.img-align-block div img").first();
                String imgIconSrc = imgIconDoc.absUrl("src");
                InputStream inputIcon = new java.net.URL(imgIconSrc).openStream();
                bitmapIcon = BitmapFactory.decodeStream(inputIcon);

                //Code to get the Elo Image
                Element imgEloDoc = doc.select("div.best-league div.img-align-block div img").first();
                String imgEloSrc = imgEloDoc.absUrl("src");
                InputStream inputElo = new java.net.URL(imgEloSrc).openStream();
                bitmapElo = BitmapFactory.decodeStream(inputElo);

                Element docNick = doc.select("div.txt h2").first();
                nickHolder = docNick.text();

                Element docSubtitle = doc.select("div.bannerSubtitle").first();
                //TODO Element docSubtitle2 = doc.select("div.bannerSubtitle a").first();
                subtitleHolder = docSubtitle.text();

                try{
                    Element docElo = doc.select("div.leagueTier").first();
                    Element docPdl = doc.select("div.league-points").first();
                    eloHolder = docElo.text() + " - " + docPdl.text().substring(16) + " pdl";
                } catch (Exception e){
                    eloHolder = "Unranked";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            nick.setText(nickHolder);
            subtitle.setText(subtitleHolder);
            imgIcon.setImageBitmap(bitmapIcon);
            imgElo.setImageBitmap(bitmapElo);
            elo.setText(eloHolder);
            progressDialog.dismiss();
        }
    }
}