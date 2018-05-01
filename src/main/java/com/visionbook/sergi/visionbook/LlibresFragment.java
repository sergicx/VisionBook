package com.visionbook.sergi.visionbook;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class LlibresFragment extends Fragment{

    private SQLite sqLite;
    private SQLiteDatabase db;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AdaptadorLlibre adaptadorLlibre;

    public LlibresFragment(){

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void omplirLlistaLlibres(){
        ArrayList<Llibre> llistaLlibres = new ArrayList<>();

        Cursor cLlibre = db.rawQuery("SELECT * FROM llibres ORDER BY id DESC", null);

        if (cLlibre.moveToFirst()) {
            do {
                Llibre llibre = new Llibre();
                llibre.setId(cLlibre.getString(1));
                llibre.setTitol(cLlibre.getString(2));
                llibre.setAutors(Helper.convertirALlistaAutors(cLlibre.getString(3)));

                llistaLlibres.add(llibre);
            } while (cLlibre.moveToNext());
        }

        if (llistaLlibres.size() > 0) {
            adaptadorLlibre = new AdaptadorLlibre(llistaLlibres, getContext(), mRecyclerView);
            mRecyclerView.setAdapter(adaptadorLlibre);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_llibres, container, false);
        sqLite = SQLite.getInstancia(getContext());
        db = sqLite.getWritableDatabase();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerLlibres);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        omplirLlistaLlibres();

        return view;
    }

}
