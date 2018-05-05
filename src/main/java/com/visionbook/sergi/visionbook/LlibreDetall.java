package com.visionbook.sergi.visionbook;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.visionbook.sergi.visionbook.entitats.Llibre;
import com.visionbook.sergi.visionbook.helper.Helper;
import com.visionbook.sergi.visionbook.helper.SQLite;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

public class LlibreDetall extends AppCompatActivity {

    private Llibre llibre;
    private TextView tvResum, tvAutor, tvEditorial, tvNumPag, tvData;
    private ImageView ivPortada;
    private ProgressDialog dialog;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private SQLiteDatabase db;
    private boolean capturat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llibre_detall);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDetall);
        setSupportActionBar(toolbar);

        db = SQLite.getInstancia(this).getWritableDatabase();

        tvResum = (TextView) findViewById(R.id.tvResum);
        tvAutor = (TextView) findViewById(R.id.tvAutor);
        ivPortada = (ImageView) findViewById(R.id.ivPortada);
        tvEditorial = (TextView) findViewById(R.id.tvEditorial);
        tvNumPag = (TextView) findViewById(R.id.tvNumPag);
        tvData = (TextView) findViewById(R.id.tvData);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        llibre = (Llibre) getIntent().getParcelableExtra("resultat");
        capturat = getIntent().getBooleanExtra("capturat", true);

        collapsingToolbarLayout.setTitle(llibre.getTitol());

        tvAutor.setText(Helper.getLlistaAutors(llibre.getAutors()));
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
           dialog = ProgressDialog.show(LlibreDetall.this, getResources().getString(R.string.carregant1), getResources().getString(R.string.carregant2), true);
           dialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return descarregarBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap imatge){
            ivPortada.setImageBitmap(imatge);
            if (!existeixLlibre(llibre.getId()))
                afegirLlibreSql(imatge);
            else if (capturat){
                eliminarLlibre(llibre.getId());
                afegirLlibreSql(imatge);
            }

            dialog.dismiss();
        }

    }

    private String obtenirPortadaGran(String url){
        return url.replaceAll("zoom=1", "zoom=0");
    }

    private byte[] convertirPortadaBlob(Bitmap b){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 20, bos);
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

    private boolean existeixLlibre(String idLlibre){
        Cursor cLlibre = db.rawQuery("SELECT * FROM llibres WHERE idllibre = " + "'" + idLlibre + "'", null);

        if (cLlibre.moveToFirst()) return true;

        return false;
    }

    private void eliminarLlibre(String idLlibre){
        db.delete("llibres", "idllibre" + "= '" + idLlibre + "'", null);
    }



}