package com.visionbook.sergi.visionbook.comentaris;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.visionbook.sergi.visionbook.R;
import com.visionbook.sergi.visionbook.entitats.Comentari;
import com.visionbook.sergi.visionbook.entitats.Llibre;
import com.visionbook.sergi.visionbook.helper.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LlistaComentaris extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private AdaptadorComentari adaptadorComentari;
    private List<Comentari> llistaComentaris = new ArrayList<>();
    private Llibre llibre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llista_comentaris);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        llibre = getIntent().getParcelableExtra("llibre");

        setTitle(getResources().getString(R.string.titol_comentaris, llibre.getTitol()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener((View) -> onBackPressed());

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerComentatis);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        new ObtenirComentaris().execute(llibre.getId());

    }

    private class ObtenirComentaris extends AsyncTask <String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            HttpHandler sh = new HttpHandler();
            String url = "http://visionbook.ml/getComentaris.php?id_llibre=" + strings[0];
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    if (jsonObj.getInt("trobats") > 0) {
                        JSONArray comentaris = jsonObj.getJSONArray("comentaris");
                        for (int i = 0; i < comentaris.length(); i++) {
                            llistaComentaris.add(new Comentari(comentaris.getJSONObject(i).getString("nom"), comentaris.getJSONObject(i).getString("comentari")));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            adaptadorComentari = new AdaptadorComentari(llistaComentaris, mRecyclerView);
            mRecyclerView.setAdapter(adaptadorComentari);
        }

    }


}
