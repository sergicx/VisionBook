package com.visionbook.sergi.visionbook.helper;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.visionbook.sergi.visionbook.R;

import java.util.ArrayList;

public class Helper {


    public static String getLlistaAutors(ArrayList<String> autors){
        StringBuilder autorsBuilder = new StringBuilder();
        for (int i=0; i<autors.size(); i++){
            autorsBuilder.append(autors.get(i));
            if (i != autors.size()-1){
                autorsBuilder.append(", ");
            }
        }
        return autorsBuilder.toString();
    }

    public static ArrayList<String> convertirALlistaAutors(String autors){
        ArrayList<String> llistaAutors = new ArrayList<>();
        String[] arrayAutors = autors.split(", ");

        for (int i = 0; i<arrayAutors.length; i++){
            llistaAutors.add(arrayAutors[i]);
        }

        return  llistaAutors;
    }

    public static boolean existeixLlibre(String idLlibre, SQLiteDatabase db){
        Cursor cLlibre = db.rawQuery("SELECT * FROM llibres WHERE idllibre = " + "'" + idLlibre + "'", null);

        if (cLlibre.moveToFirst()) return true;

        return false;
    }

    public static void eliminarLlibre(String idLlibre, SQLiteDatabase db){
        db.delete("llibres", "idllibre" + "= '" + idLlibre + "'", null);
    }

    public static void confirmacioTancarSesio(Context context, DialogInterface.OnClickListener dialogClickListener, String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(text).setPositiveButton(context.getResources().getString(R.string.si), dialogClickListener)
                .setNegativeButton(context.getResources().getString(R.string.no), dialogClickListener).show();
    }

    public static boolean hiHaInternet(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void caixaMisatge(Context context, String titol, String misatge) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setMessage(misatge);
        dlgAlert.setTitle(titol);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
        dlgAlert.setPositiveButton("OK",
                (dialog, which) -> {

                });
    }
}
