package com.visionbook.sergi.visionbook.comentaris;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private List<Comentari> llistaComentaris;
    private Llibre llibre;
    private FirebaseUser usuari;
    private Button btnAfegirComentari;
    private EditText etComentari;
    private TextView tvNoComentaris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llista_comentaris);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        llibre = getIntent().getParcelableExtra("llibre");
        usuari = FirebaseAuth.getInstance().getCurrentUser();
        btnAfegirComentari = (Button) findViewById(R.id.btnAfegirComentari);
        etComentari = (EditText) findViewById(R.id.etComentari);
        tvNoComentaris = (TextView) findViewById(R.id.tvNoComentaris);

        setTitle(getResources().getString(R.string.titol_comentaris, llibre.getTitol()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener((View) -> onBackPressed());

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerComentaris);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        new ObtenirComentaris().execute(llibre.getId());

        btnAfegirComentari.setOnClickListener((View v)->{
            if (etComentari.getText().length() > 0)
                new AfegirComentari().execute(etComentari.getText().toString());
        });


    }

    private class ObtenirComentaris extends AsyncTask <String, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            llistaComentaris = new ArrayList<>();
        }

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
                            llistaComentaris.add(new Comentari(comentaris.getJSONObject(i).getString("nom"), comentaris.getJSONObject(i).getString("comentari"), comentaris.getJSONObject(i).getString("data")));
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

            if (llistaComentaris.size() == 0)
                tvNoComentaris.setVisibility(View.VISIBLE);
            else
                tvNoComentaris.setVisibility(View.GONE);
        }

    }

    private class AfegirComentari extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            HttpHandler sh = new HttpHandler();
            String url = "http://visionbook.ml/creaComentari.php?id_usuari="+usuari.getUid()+"&id_llibre="+llibre.getId()+"&comentari="+strings[0];
            sh.makeServiceCall(url);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            new ObtenirComentaris().execute(llibre.getId());
            adaptadorComentari.notifyDataSetChanged();
            etComentari.setText("");
            etComentari.clearFocus();
            etComentari.setFocusable(false);
            etComentari.setFocusableInTouchMode(true);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }
    }


}
