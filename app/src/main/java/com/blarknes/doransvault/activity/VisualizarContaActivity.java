package com.blarknes.doransvault.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blarknes.doransvault.DAO.ContaDAO;
import com.blarknes.doransvault.R;
import com.blarknes.doransvault.model.Conta;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

public class VisualizarContaActivity extends AppCompatActivity {
    List<Conta> contaList;
    Conta conta;
    ProgressDialog progressDialog;
    String url, urlChampion,  region,
           eloHolder, eloFlexHolder,
           nickHolder, subtitleHolder,
           decayHolder;
    ImageView imgElo, imgEloFlex, imgIcon, imgBanner;
    TextView nick, login, senha, elo, eloFlex, subtitle, decay;
    Bitmap bitmapElo, bitmapEloFlex, bitmapIcon, bitmapBanner;
    ImageButton showPassword;
    int[] games = new int[10];
    public static String result, urlRanked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_conta);

        ConfigureActions();
        new InternetTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.ellipsis_account_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch(menuItem.getItemId()){
            case R.id.edit_account:
                AccountActivityChange(0);
                return true;
            case R.id.delete_account:
                AccountActivityChange(1);
                return true;
            case R.id.refresh_account:
                new InternetTask().execute();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void AccountActivityChange(int activityType){
        switch(activityType){
            case 0:
                //this.setConta(contaList.get(position));
                Intent myIntent = new Intent(com.blarknes.doransvault.activity.VisualizarContaActivity.this, AtualizarContaActivity.class);
                startActivity(myIntent);
                break;
            case 1:
                ContaDAO dao = new ContaDAO(com.blarknes.doransvault.activity.VisualizarContaActivity.this);
                dao.delete(conta);
                startActivity(new Intent(com.blarknes.doransvault.activity.VisualizarContaActivity.this, MainActivity.class));
                break;
            default:
                break;
        }
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
        eloFlex = findViewById(R.id.eloFlex);
        decay = findViewById(R.id.decayLabel);
        imgElo = findViewById(R.id.imageElo);
        imgEloFlex = findViewById(R.id.imageEloFlex);
        imgIcon = findViewById(R.id.imageIcon);
        imgBanner = findViewById(R.id.imageBanner);

        showPassword = findViewById(R.id.showPassword);
        showPassword.setOnClickListener(v -> {
            if (senha.getTransformationMethod() == null)
                senha.setTransformationMethod(new PasswordTransformationMethod());
            else
                senha.setTransformationMethod(null);
        });

        nick.setText(conta.getNick());
        login.setText(conta.getLogin());
        senha.setText(conta.getSenha());

        region = conta.getRegiao().toLowerCase();

        url = String.format("https://www.leagueofgraphs.com/pt/summoner/%s/%s",
                region, nick.getText().toString());
        urlChampion = String.format("https://championmastery.gg/summoner?summoner=%s&region=%s",
                nick.getText().toString(), region);

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
                Document docChampion = Jsoup.connect(urlChampion).get();

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

                //Code to get Background Banner
                Element imgBannerDoc = docChampion.select("#tbody tr td a").first();

                String championNameAux = imgBannerDoc.text(), championName;

                if (championNameAux.indexOf("'") > 0)
                    championName = championNameAux.substring(0, championNameAux.indexOf("'")) + championNameAux.substring(championNameAux.indexOf("'")+1).toLowerCase();
                else
                    championName = championNameAux.replace(" ", "");

                championName = String.format("https://ddragon.leagueoflegends.com/cdn/img/champion/splash/%s_0.jpg", championName);
                InputStream inputBanner = new java.net.URL(championName).openStream();
                bitmapBanner = BitmapFactory.decodeStream(inputBanner);

                Element docNick = doc.select("div.txt h2").first();
                nickHolder = docNick.text();
                Element docSubtitle = doc.select("div.bannerSubtitle").first();
                subtitleHolder = docSubtitle.text().substring(docSubtitle.text().indexOf("Nível"));

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

                try{
                    Element imgEloFlex = doc.select("div.other-league-content-row img").first();

                    String eloFlexAux = imgEloFlex.attr("class");
                    eloFlexAux = eloFlexAux.substring(eloFlexAux.indexOf("-")+1);
                    eloFlexAux = eloFlexAux.substring(eloFlexAux.indexOf("-")+1).replace(" ", "");

                    Element docEloFlex = doc.select("div.other-league-content-row div.txt div.row > div").first();
                    Element docPdlFlex = doc.select("div.other-league-content-row span.leaguepoints").first();
                    eloFlexHolder = docEloFlex.text() + " - " + docPdlFlex.text() + " pdl";

                    if (docEloFlex.text().contains("Desafiante"))
                        eloFlexHolder = "Desafiante - " + docPdlFlex.text().substring(16) + " pdl";
                    else if (docEloFlex.text().contains("GrandMaster"))
                        eloFlexHolder = "Grão-Mestre - " + docPdlFlex.text().substring(16) + " pdl";
                    else if (docEloFlex.text().contains("Master"))
                        eloFlexHolder = "Mestre - " + docPdlFlex.text().substring(16) + " pdl";

                    //Code to get the Flex Elo Image
                    String imgEloFlexSrc = String.format("https://lolg-cdn.porofessor.gg/img/league-icons-v2/160/%s.png", eloFlexAux);
                    InputStream inputEloFlex = new java.net.URL(imgEloFlexSrc).openStream();
                    bitmapEloFlex = BitmapFactory.decodeStream(inputEloFlex);

                } catch (Exception e){
                    eloFlexHolder = "Unranked";
                }


                if (eloHolder.contains("Mestre") || eloHolder.contains("Desafiante")) {
                    try {
                        Element requestCodeDoc = doc.select("li.current.allChampions > a").last();
                        String requestCode = requestCodeDoc.absUrl("data-additional-url-base");
                        requestCode = requestCode.substring(requestCode.indexOf(region.toLowerCase()));
                        requestCode = requestCode.substring(requestCode.indexOf("/") + 1);
                        requestCode = requestCode.substring(0, requestCode.indexOf("/"));
                        String requestCodeAux = region.toLowerCase() + "/" + requestCode;
                        urlRanked = String.format("https://www.leagueofgraphs.com/partial/summoner/recent/%s/0/queueId-420", requestCodeAux);

                    } catch (Exception e) {
                        decayHolder = "Você está decaindo";
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
                nickHolder = conta.getNick() + " ("+conta.getRegiao()+")";
                subtitleHolder = "no data";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            grayScale();

            nick.setText(nickHolder);

            if (bitmapIcon != null)
                imgIcon.setImageBitmap(bitmapIcon);
            if (bitmapElo != null)
                imgElo.setImageBitmap(bitmapElo);
            if (bitmapEloFlex != null)
                imgEloFlex.setImageBitmap(bitmapEloFlex);
            if (bitmapBanner != null)
                imgBanner.setImageBitmap(bitmapBanner);

            subtitle.setText(subtitleHolder);
            elo.setText(eloHolder);
            eloFlex.setText(eloFlexHolder);

            decayCalculator(urlRanked);

            progressDialog.dismiss();
        }
    }

    public void grayScale(){
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(.55f);
        imgBanner.setColorFilter(new ColorMatrixColorFilter(matrix));
    }

    public void decayCalculator(String urlRanked) {
        RequestQueue queue = Volley.newRequestQueue(VisualizarContaActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlRanked,
            response -> {
                Boolean hasMoreGames = true;

                while (hasMoreGames) {
                    String responseAux = "";
                    if (response.contains("gameDate")) {
                        responseAux = response.substring(response.indexOf("gameDate")+8);
                        responseAux = responseAux.substring(responseAux.indexOf("\">")+2);
                        responseAux = responseAux.substring(0, responseAux.indexOf("</div>")).trim();

                        int timeNumber;
                        if (responseAux.contains("A day ago"))
                            games[1]++;
                        else if (responseAux.contains("min") || responseAux.contains("hour")) {
                            Calendar rightNow = Calendar.getInstance();
                            timeNumber = responseAux.contains("An") ? 1 : Integer.parseInt(responseAux.substring(0, responseAux.indexOf(" ")));
                            if (rightNow.get(Calendar.HOUR_OF_DAY) - timeNumber < 0)
                                games[1]++;
                            else
                                games[0]++;
                        } else {
                            timeNumber = Integer.parseInt(responseAux.substring(0, responseAux.indexOf(" ")));
                            if (timeNumber < 10)
                                games[timeNumber]++;
                        }

                        response = response.substring(response.indexOf("gameDate")+8);
                    } else
                        hasMoreGames = false;
                }

                int countDays = 10;
                for (int i = 9; i >= 0;  i--) {
                    countDays += games[i]-1;
                    if (countDays > 10)
                        countDays = 10;
                }

                if (countDays == 0)
                    result = "Você decairá hoje";
                else if (countDays == 1)
                    result = "Você decairá em 1 dia";
                else
                    result = "Você decairá em "+countDays+" dias";

                Log.v("ranked", result);

                decayHolder = result;
                decay.setText(decayHolder);
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    result = "erro";
                }
            });
        queue.add(stringRequest);

    }

}