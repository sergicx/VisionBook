package com.visionbook.sergi.visionbook.llista_llibres;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.visionbook.sergi.visionbook.mestre_llibre.LlibreDetall;
import com.visionbook.sergi.visionbook.Principal;
import com.visionbook.sergi.visionbook.R;
import com.visionbook.sergi.visionbook.entitats.Llibre;
import com.visionbook.sergi.visionbook.helper.Helper;
import com.visionbook.sergi.visionbook.helper.SQLite;

import java.util.List;

public class AdaptadorLlibre extends RecyclerView.Adapter<AdaptadorLlibre.ViewHolder> {
    List<Llibre> llistaLlibres;
    SQLiteDatabase db;
    private RecyclerView mRecyclerView;

    public AdaptadorLlibre(List<Llibre> llistaLlibres, RecyclerView recyclerView) {
        this.llistaLlibres = llistaLlibres;
        this.mRecyclerView = recyclerView;

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
        public ImageView ivRowPortada, btnRowMenu;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            tvRowTitol = (TextView) v.findViewById(R.id.tvRowTitol);
            tvRowAutor = (TextView) v.findViewById(R.id.tvRowAutor);
            ivRowPortada = (ImageView) v.findViewById(R.id.ivRowPortada);
            btnRowMenu = (ImageView) v.findViewById(R.id.btnRowMenu);
            db = SQLite.getInstancia(v.getContext()).getWritableDatabase();
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
            if (Helper.hiHaInternet(holder.itemView.getContext())) {
                Intent iResultat = new Intent(holder.itemView.getContext(), LlibreDetall.class);
                iResultat.putExtra("resultat", llistaLlibres.get(position));
                iResultat.putExtra("capturat", false);
                holder.itemView.getContext().startActivity(iResultat);
            }else
                Snackbar.make(((Principal) holder.itemView.getContext()).getWindow().getDecorView().getRootView(), holder.itemView.getResources().getString(R.string.error_internet), Snackbar.LENGTH_SHORT).show();
        });

        holder.btnRowMenu.setOnClickListener(view -> onPopupMenuClick(view, position));
    }

    @Override
    public int getItemCount() {
        return llistaLlibres.size();
    }

    public void onPopupMenuClick(View view, int pos) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_cardview, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menuBtnEliminar:
                    Helper.eliminarLlibre(llistaLlibres.get(pos).getId(), db);
                    llistaLlibres.remove(pos);
                    notifyDataSetChanged();
                    Toast.makeText(view.getContext(), view.getResources().getString(R.string.book_deleted), Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return false;
            }
        });
        popup.show();
    }

}
