package com.visionbook.sergi.visionbook;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

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

    }

}