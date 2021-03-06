package com.visionbook.sergi.visionbook.llista_llibres;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.visionbook.sergi.visionbook.capturar_llibre.CapturarFragment;
import com.visionbook.sergi.visionbook.Principal;
import com.visionbook.sergi.visionbook.R;
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
    private FloatingActionButton btnFlotantCapturar;
    private LinearLayout layoutInfoLlibre;

    public LlibresFragment(){

    }

    private void omplirLlistaLlibres(){
        List<Llibre> llistaLlibres = new ArrayList<>();

        //Es fa una select a la base de dades local de SQLite ordenat amb els mes recents primer
        Cursor cLlibre = db.rawQuery("SELECT * FROM llibres ORDER BY id DESC", null);

        if (cLlibre.moveToFirst()) {
            do {
                Llibre llibre = new Llibre();
                llibre.setId(cLlibre.getString(1));
                llibre.setTitol(cLlibre.getString(2));
                llibre.setAutors(Helper.convertirALlistaAutors(cLlibre.getString(3)));
                llibre.setDescripcio(cLlibre.getString(4));
                llibre.setEditorial(cLlibre.getString(5));
                llibre.setNumPag(Integer.parseInt(cLlibre.getString(6)));
                llibre.setDataPublicacio(cLlibre.getString(7));
                llibre.setUrlImatge(cLlibre.getString(8));
                byte[] blobPortada = cLlibre.getBlob(9);
                Bitmap portadaRaw = BitmapFactory.decodeByteArray(blobPortada, 0 ,blobPortada.length);
                llibre.setbPortada(portadaRaw);

                //Es fa una llista d'objectes Llibre per a pasar-li al adaptador del RecyclerView
                llistaLlibres.add(llibre);
            } while (cLlibre.moveToNext());
        }

        if (llistaLlibres.size() > 0) {
            //S'instancia l'adaptador pasant-li la llista per a que automaticament generi la llista
            //de vistes
            adaptadorLlibre = new AdaptadorLlibre(llistaLlibres, mRecyclerView);
            mRecyclerView.setAdapter(adaptadorLlibre);
            layoutInfoLlibre.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_llibres, container, false);

        btnFlotantCapturar = (FloatingActionButton) view.findViewById(R.id.btnFlotantCapturar);
        btnFlotantCapturar.setOnClickListener((View v)-> obrirCapturarLlibre());

        layoutInfoLlibre = (LinearLayout) view.findViewById(R.id.layoutInfoLlibre);

        sqLite = SQLite.getInstancia(getActivity());
        db = sqLite.getWritableDatabase();

        //Es prepara el RecyclerView per a poder començar a complir la llista de llibres
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerLlibres);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        omplirLlistaLlibres();

        return view;
    }

    private void obrirCapturarLlibre(){
        if (Helper.hiHaInternet(getActivity())) {
            Fragment capturarFragment = new CapturarFragment();
            getActivity().getFragmentManager().popBackStack();
            getFragmentManager().beginTransaction().replace(R.id.content_frame, capturarFragment).commit();
            getActivity().setTitle(getResources().getString(R.string.capturar_llibre));
            ((Principal) getActivity()).getNavigationView().setCheckedItem(R.id.nav_capturarllibre);
        }else {
            Snackbar.make(getView(), getResources().getString(R.string.error_internet), Snackbar.LENGTH_SHORT).show();
        }
    }

}
