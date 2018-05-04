package com.visionbook.sergi.visionbook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.visionbook.sergi.visionbook.entitats.Llibre;
import com.visionbook.sergi.visionbook.helper.Helper;

import java.util.List;

public class AdaptadorLlibre extends RecyclerView.Adapter<AdaptadorLlibre.ViewHolder> {
    List<Llibre> llistaLlibres;

    public AdaptadorLlibre(List<Llibre> llistaLlibres, RecyclerView recyclerView) {
        this.llistaLlibres = llistaLlibres;

//        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == getItemCount()-1){
//                    System.out.println("CARREGAR MES ITEMS!");
//                }
//            }
//        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRowTitol;
        public TextView tvRowAutor;
        public ImageView ivRowPortada;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            tvRowTitol = (TextView) v.findViewById(R.id.tvRowTitol);
            tvRowAutor = (TextView) v.findViewById(R.id.tvRowAutor);
            ivRowPortada = (ImageView) v.findViewById(R.id.ivRowPortada);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.row_llibre, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvRowTitol.setText(llistaLlibres.get(position).getTitol());
        holder.tvRowAutor.setText(Helper.getLlistaAutors(llistaLlibres.get(position).getAutors()));
        holder.ivRowPortada.setImageBitmap(llistaLlibres.get(position).getbPortada());

        holder.itemView.setOnClickListener((View e) ->{
            Intent iResultat = new Intent(holder.itemView.getContext(), LlibreDetall.class);
            iResultat.putExtra("resultat", llistaLlibres.get(position));
            iResultat.putExtra("capturat", false);
            holder.itemView.getContext().startActivity(iResultat);
        });
    }

    @Override
    public int getItemCount() {
        return llistaLlibres.size();
    }

}
