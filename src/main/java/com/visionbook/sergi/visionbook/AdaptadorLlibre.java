package com.visionbook.sergi.visionbook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorLlibre extends RecyclerView.Adapter<AdaptadorLlibre.ViewHolder> {
    ArrayList<Llibre> llistaLlibres;
    private Context mContext;
    private RecyclerView mRecyclerV;

    public AdaptadorLlibre(ArrayList<Llibre> llistaLlibres, Context mContext, RecyclerView mRecyclerV) {
        this.llistaLlibres = llistaLlibres;
        this.mContext = mContext;
        this.mRecyclerV = mRecyclerV;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRowTitol;
        public TextView tvRowAutor;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            tvRowTitol = (TextView) v.findViewById(R.id.tvRowTitol);
            tvRowAutor = (TextView) v.findViewById(R.id.tvRowAutor);
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
    }

    @Override
    public int getItemCount() {
        return llistaLlibres.size();
    }

}
