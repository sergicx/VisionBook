package com.visionbook.sergi.visionbook;

import android.graphics.Bitmap;
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

    public AdaptadorLlibre(List<Llibre> llistaLlibres) {
        this.llistaLlibres = llistaLlibres;
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
    }

    @Override
    public int getItemCount() {
        return llistaLlibres.size();
    }

}
