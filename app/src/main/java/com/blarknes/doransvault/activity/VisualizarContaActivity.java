package com.blarknes.doransvault.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.blarknes.doransvault.DAO.ContaDAO;
import com.blarknes.doransvault.R;
import com.blarknes.doransvault.model.Conta;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.InputStream;
import java.util.List;

public class VisualizarContaActivity extends AppCompatActivity {
    List<Conta> contaList;
    Conta conta;
    ProgressDialog progressDialog;
    String url, eloHolder, nickHolder, subtitleHolder, region;
    ImageView imgElo, imgIcon;
    TextView nick, login, senha, elo, subtitle;
    Bitmap bitmapElo, bitmapIcon;
    ImageButton showPassword;

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

        showPassword = findViewById(R.id.showPassword);
        showPassword.setOnClickListener(v -> {
            senha.setInputType(InputType.TYPE_CLASS_TEXT);
        });

        nick.setText(conta.getNick());
        login.setText(conta.getLogin());
        senha.setText(conta.getSenha());

        region = conta.getRegiao().toLowerCase();

        url = String.format("https://www.leagueofgraphs.com/pt/summoner/%s/%s",
                region, nick.getText().toString());

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

                    if (docElo.text().contains("Desafiante"))
                        eloHolder = "Desafiante - " + docPdl.text().substring(16) + " pdl";
                    else if (docElo.text().contains("GrandMaster"))
                        eloHolder = "Grão-Mestre - " + docPdl.text().substring(16) + " pdl";
                    else if (docElo.text().contains("Master"))
                        eloHolder = "Mestre - " + docPdl.text().substring(16) + " pdl";

                } catch (Exception e){
                    eloHolder = "Unranked";
                }
            } catch (Exception e) {
                e.printStackTrace();
                nickHolder = conta.getNick() + " ("+conta.getRegiao()+")";
                subtitleHolder = "no data";
                eloHolder = "no data";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            nick.setText(nickHolder);

            if (bitmapIcon != null)
                imgIcon.setImageBitmap(bitmapIcon);
            if (bitmapElo != null)
                imgElo.setImageBitmap(bitmapElo);

            subtitle.setText(subtitleHolder);
            elo.setText(eloHolder);
            progressDialog.dismiss();
        }
    }
}