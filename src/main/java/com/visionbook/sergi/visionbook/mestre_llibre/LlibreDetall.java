package com.visionbook.sergi.visionbook.mestre_llibre;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.visionbook.sergi.visionbook.R;
import com.visionbook.sergi.visionbook.comentaris.LlistaComentaris;
import com.visionbook.sergi.visionbook.entitats.Llibre;
import com.visionbook.sergi.visionbook.helper.Helper;
import com.visionbook.sergi.visionbook.helper.SQLite;
import com.visionbook.sergi.visionbook.peticions_api.NumeroComentaris;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

public class LlibreDetall extends AppCompatActivity{

    private Llibre llibre;
    private TextView tvResum, tvAutor, tvEditorial, tvNumPag, tvData;
    private ImageView ivPortada;
    private ProgressDialog dialog;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private SQLiteDatabase db;
    private boolean capturat;
    private Button btnComentaris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llibre_detall);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDetall);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener((View) -> onBackPressed());

        db = SQLite.getInstancia(this).getWritableDatabase();

        tvResum = (TextView) findViewById(R.id.tvResum);
        tvAutor = (TextView) findViewById(R.id.tvAutor);
        ivPortada = (ImageView) findViewById(R.id.ivPortada);
        tvEditorial = (TextView) findViewById(R.id.tvEditorial);
        tvNumPag = (TextView) findViewById(R.id.tvNumPag);
        tvData = (TextView) findViewById(R.id.tvData);
        btnComentaris = (Button) findViewById(R.id.btnObrirComentaris);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        llibre = (Llibre) getIntent().getParcelableExtra("resultat");
        capturat = getIntent().getBooleanExtra("capturat", true);

        collapsingToolbarLayout.setTitle(llibre.getTitol());

        //Obtinc els autors separats per comes utilitzant el metode helper de getLlistaAutors
        tvAutor.setText(Helper.getLlistaAutors(llibre.getAutors()));
        tvResum.setText(llibre.getDescripcio());
        tvEditorial.setText(llibre.getEditorial());
        if (llibre.getNumPag() > 0)
            tvNumPag.setText(Integer.toString(llibre.getNumPag()));
        else
            tvNumPag.setText("-");
        tvData.setText(llibre.getDataPublicacio());

        String urlPortadaGran = obtenirPortadaGran(llibre.getUrlImatge());
        String urlPortadaPetita = obtenirPortadaPetita(llibre.getUrlImatge());

        new DescarregarPortada(true).execute(urlPortadaGran);
        new DescarregarPortada(false).execute(urlPortadaPetita);

        new NumeroComentaris(this).execute(llibre.getId());

        btnComentaris.setOnClickListener((View v)->{
            Intent i = new Intent(this, LlistaComentaris.class);
            i.putExtra("llibre", llibre);
            startActivity(i);
        });

        getWindow().getDecorView().findViewById(R.id.toolbarDetall).setFocusable(false);
    }

    private class DescarregarPortada extends AsyncTask<String, Void, Bitmap> {

        private boolean isPortadaGran;

        public DescarregarPortada(boolean portadaGran) {
            this.isPortadaGran = portadaGran;
        }

        private Bitmap descarregarBitmap(String sUrl) {
            Bitmap bitmap = null;

            //S'obté el bitmap a partir de una url que porti a una imatge
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
            if (isPortadaGran) {
                dialog = ProgressDialog.show(LlibreDetall.this, getResources().getString(R.string.carregant1), getResources().getString(R.string.carregant2), true);
                dialog.show();
            }
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return descarregarBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap imatge){
            if (isPortadaGran){
                ivPortada.setImageBitmap(imatge);
            }else{
                if (!Helper.existeixLlibre(llibre.getId(), db))
                    afegirLlibreSql(imatge);
                else if (capturat){
                    Helper.eliminarLlibre(llibre.getId(), db);
                    afegirLlibreSql(imatge);
                }
            }

            if (isPortadaGran) dialog.dismiss();
        }
    }

    private String obtenirPortadaGran(String url){
        return url.replaceAll("zoom=1", "zoom=0");
    }

    private String obtenirPortadaPetita(String url){
        return url.replaceAll("zoom=1", "zoom=2");
    }

    private byte[] convertirPortadaBlob(Bitmap b){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] img = bos.toByteArray();

        return img;
    }

    private void afegirLlibreSql(Bitmap imatge){
        ContentValues registreLlibre = new ContentValues();
        registreLlibre.put("idllibre", llibre.getId());
        registreLlibre.put("titol", llibre.getTitol());
        registreLlibre.put("autor", Helper.getLlistaAutors(llibre.getAutors()));
        registreLlibre.put("descripcio", llibre.getDescripcio());
        registreLlibre.put("portada", convertirPortadaBlob(imatge));
        registreLlibre.put("editorial", llibre.getEditorial());
        registreLlibre.put("numpag", llibre.getNumPag());
        registreLlibre.put("data", llibre.getDataPublicacio());
        registreLlibre.put("urlportada", llibre.getUrlImatge());
        db.insert("llibres", null, registreLlibre);
    }

    public void onRestart() {
        super.onRestart();
        new NumeroComentaris(this).execute(llibre.getId());
    }




}