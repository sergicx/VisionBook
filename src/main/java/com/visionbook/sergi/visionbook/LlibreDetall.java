package com.visionbook.sergi.visionbook;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class LlibreDetall extends AppCompatActivity {

    private Llibre llibre;
    private TextView tvResum, tvAutor, tvEditorial, tvNumPag, tvData;
    private ImageView ivPortada;
    private ProgressDialog dialog;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llibre_detall);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDetall);
        setSupportActionBar(toolbar);

        tvResum = (TextView) findViewById(R.id.tvResum);
        tvAutor = (TextView) findViewById(R.id.tvAutor);
        ivPortada = (ImageView) findViewById(R.id.ivPortada);
        tvEditorial = (TextView) findViewById(R.id.tvEditorial);
        tvNumPag = (TextView) findViewById(R.id.tvNumPag);
        tvData = (TextView) findViewById(R.id.tvData);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        llibre = (Llibre) getIntent().getParcelableExtra("resultat");

        collapsingToolbarLayout.setTitle(llibre.getTitol());



        //Separo els autors per comes si hi ha mes d'un
        ArrayList<String> llistaAutors = llibre.getAutors();
        StringBuilder autorsBuilder = new StringBuilder();
        for (int i=0; i<llistaAutors.size(); i++){
            autorsBuilder.append(llistaAutors.get(i));
            if (i != llistaAutors.size()-1){
                autorsBuilder.append(", ");
            }
        }
        tvAutor.setText(autorsBuilder.toString());
        tvResum.setText(llibre.getDescripcio());
        tvEditorial.setText(llibre.getEditorial());
        if (llibre.getNumPag() > 0)
            tvNumPag.setText(Integer.toString(llibre.getNumPag()));
        else
            tvNumPag.setText("-");
        tvData.setText(llibre.getDataPublicacio());

        String urlPortada = obtenirPortadaGran(llibre.getUrlImatge());

         new DescarregarPortada().execute(urlPortada);

    }

    private class DescarregarPortada extends AsyncTask<String, Void, Bitmap> {


        private Bitmap descarregarBitmap(String sUrl) {
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(sUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPreExecute(){
           dialog = ProgressDialog.show(LlibreDetall.this, "Carregant", "Carregant el llibre...", true);
           dialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return descarregarBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap imatge){
            ivPortada.setImageBitmap(imatge);
            dialog.dismiss();
        }

    }

    private String obtenirPortadaGran(String url){
        return url.replaceAll("zoom=1", "zoom=0");
    }

}