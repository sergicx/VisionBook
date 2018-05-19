package com.visionbook.sergi.visionbook.peticions_api;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.visionbook.sergi.visionbook.mestre_llibre.LlibreDetall;
import com.visionbook.sergi.visionbook.R;
import com.visionbook.sergi.visionbook.entitats.Llibre;
import com.visionbook.sergi.visionbook.helper.HttpHandler;
import com.visionbook.sergi.visionbook.helper.SQLite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ObtenirDadesLlibre extends AsyncTask<String, Void, Llibre> {
    ProgressDialog dialog;
    Context context;
    private SQLite sqLite;
    private SQLiteDatabase db;

    public ObtenirDadesLlibre(Context context){
        this.context = context;
        sqLite = SQLite.getInstancia(context);
        //i obtinc la escritura de la base de dades per a poder modificar en tot el fragment
        db = sqLite.getWritableDatabase();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("Començant la cerca...");
    }

    @Override
    protected Llibre doInBackground(String... strings) {
        System.out.println("BUSCANT PER: "+ strings[0]);
        HttpHandler sh = new HttpHandler();
        //Concateno la url de la consulta a la API amb el text que hagi capturat
        String url = "https://www.googleapis.com/books/v1/volumes?q="+strings[0];
        //Faig una petició GET utilitzant la classe HttpHandler
        String jsonStr = sh.makeServiceCall(url);

        if(jsonStr != null){
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                System.out.println("trobats: "+jsonObj.getInt("totalItems"));
                if (jsonObj.getInt("totalItems") > 0){
                    JSONArray llibres = jsonObj.getJSONArray("items");
                    JSONObject primerLlibre = llibres.getJSONObject(0);
                    String id = primerLlibre.getString("id");
                    JSONObject volumeInfo = primerLlibre.getJSONObject("volumeInfo");
                    String titol = volumeInfo.getString("title");
                    String descripcio = "Sense descripció";
                    String editorial = "-";
                    String dataPublicacio = "-";
                    int numPag = 0;

                    if (volumeInfo.has("pageCount"))
                        numPag = volumeInfo.getInt("pageCount");

                    if(volumeInfo.has("publishedDate"))
                        dataPublicacio = volumeInfo.getString("publishedDate");

                    if (volumeInfo.has("description"))
                        descripcio = volumeInfo.getString("description");

                    if (volumeInfo.has("publisher"))
                        editorial = volumeInfo.getString("publisher");


                    String urlImg = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");

                    ArrayList<String> llistaAutors = new ArrayList<>();
                    JSONArray autorsJSON = volumeInfo.getJSONArray("authors");


                    for (int i=0; i< autorsJSON.length(); i++){
                        llistaAutors.add(autorsJSON.getString(i));
                    }

                    Llibre llibre = new Llibre(id, titol, llistaAutors, editorial, dataPublicacio, descripcio, numPag, urlImg);

                    llibre.setAutors(llistaAutors);
                    return llibre;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Llibre result) {
        super.onPostExecute(result);

        if (result != null) {
            //Si ha trobat el llibre a la api, s'obrirá l'activitat detall pasantli el objecte llibre

            Intent iResultat = new Intent(context, LlibreDetall.class);
            iResultat.putExtra("resultat", result);
            iResultat.putExtra("capturat", true);
            context.startActivity(iResultat);
        }
    }

}