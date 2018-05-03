package com.visionbook.sergi.visionbook;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.visionbook.sergi.visionbook.entitats.Llibre;
import com.visionbook.sergi.visionbook.helper.Helper;
import com.visionbook.sergi.visionbook.helper.SQLite;

import java.util.ArrayList;
import java.util.List;


public class LlibresFragment extends Fragment{

    private SQLite sqLite;
    private SQLiteDatabase db;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private AdaptadorLlibre adaptadorLlibre;

    public LlibresFragment(){

    }

    private void omplirLlistaLlibres(){
        List<Llibre> llistaLlibres = new ArrayList<>();

        Cursor cLlibre = db.rawQuery("SELECT * FROM llibres ORDER BY id DESC", null);

        if (cLlibre.moveToFirst()) {
            do {
                Llibre llibre = new Llibre();
                llibre.setId(cLlibre.getString(1));
                llibre.setTitol(cLlibre.getString(2));
                llibre.setAutors(Helper.convertirALlistaAutors(cLlibre.getString(3)));
                byte[] blobPortada = cLlibre.getBlob(5);
                Bitmap portadaRaw = BitmapFactory.decodeByteArray(blobPortada, 0 ,blobPortada.length);
                llibre.setbPortada(portadaRaw);

                llistaLlibres.add(llibre);
            } while (cLlibre.moveToNext());
        }

        if (llistaLlibres.size() > 0) {
            adaptadorLlibre = new AdaptadorLlibre(llistaLlibres, mRecyclerView);
            mRecyclerView.setAdapter(adaptadorLlibre);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_llibres, container, false);
        sqLite = SQLite.getInstancia(getActivity());
        db = sqLite.getWritableDatabase();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerLlibres);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        omplirLlistaLlibres();


        return view;
    }

}
