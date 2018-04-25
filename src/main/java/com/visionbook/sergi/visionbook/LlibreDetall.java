package com.visionbook.sergi.visionbook;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
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
    private TextView tvResum, tvAutor;
    private ImageView ivPortada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llibre_detall);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDetall);
        setSupportActionBar(toolbar);

        tvResum = (TextView) findViewById(R.id.tvResum);
        tvAutor = (TextView) findViewById(R.id.tvAutor);
        ivPortada = (ImageView) findViewById(R.id.ivPortada);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

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
        try {
            Bitmap portada = new DescarregarPortada().execute(llibre.getUrlImatge()).get();
            ivPortada.setImageBitmap(portada);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private class DescarregarPortada extends AsyncTask<String, Void, Bitmap> {
        private String TAG = "DownloadImage";
        private Bitmap downloadImageBitmap(String sUrl) {
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(sUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            } catch (Exception e) {
                Log.d(TAG, "Exception 1, Something went wrong!");
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPreExecute(){
            ProgressDialog dialog = ProgressDialog.show(LlibreDetall.this, "Loading...", "Please wait...", true);
            dialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImageBitmap(params[0]);
        }

    }



}