package com.visionbook.sergi.visionbook.helper;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLite extends SQLiteOpenHelper{

    private static SQLite sqLite = null;

    public SQLite(Context context) {
        super(context, "Llibres.db", null, 1);
    }

    public static SQLite getInstancia(Context context){
        if (sqLite == null){
            sqLite = new SQLite(context.getApplicationContext());
        }

        return sqLite;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("Base de dades creada!");
        db.execSQL("CREATE TABLE llibres (id integer PRIMARY KEY AUTOINCREMENT NOT NULL , idllibre text, titol text, autor text, descripcio text, portada BLOB)");
}

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS llibres");
        onCreate(db);

    }
}
