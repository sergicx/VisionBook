package com.visionbook.sergi.visionbook.helper;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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


}
