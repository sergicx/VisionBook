package com.visionbook.sergi.visionbook.comentaris;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.visionbook.sergi.visionbook.R;
import com.visionbook.sergi.visionbook.entitats.Comentari;

import java.util.List;

public class AdaptadorComentari extends RecyclerView.Adapter<AdaptadorComentari.ViewHolder>{

    List<Comentari> llistaComentaris;
    private RecyclerView mRecyclerView;

    public AdaptadorComentari(List<Comentari> llistaComentaris, RecyclerView mRecyclerView) {
        this.llistaComentaris = llistaComentaris;
        this.mRecyclerView = mRecyclerView;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRowNomUsuari;
        public TextView tvRowComentari;
        public TextView tvTimeStampCom;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            tvRowNomUsuari = (TextView) v.findViewById(R.id.tvRowNomUsuari);
            tvRowComentari = (TextView) v.findViewById(R.id.tvRowComentari);
            tvTimeStampCom = (TextView) v.findViewById(R.id.tvTimeStampCom);
        }
    }

    @NonNull
    @Override
    public AdaptadorComentari.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.row_comentari, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorComentari.ViewHolder holder, int position) {
        holder.tvRowNomUsuari.setText(llistaComentaris.get(position).getNomUsuari());
        holder.tvRowComentari.setText(llistaComentaris.get(position).getComentari());
        holder.tvTimeStampCom.setText(llistaComentaris.get(position).getTimeStamp());

    }

    @Override
    public int getItemCount() {
        return llistaComentaris.size();
    }
}
