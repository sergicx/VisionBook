package com.visionbook.sergi.visionbook.entitats;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Llibre implements Parcelable{
    private String id;
    private String titol;
    private ArrayList<String> autors;
    private String editorial;
    private String dataPublicacio;
    private String descripcio;
    private int numPag;
    private String urlImatge;
    private Bitmap bPortada;

    public Llibre() {

    }

    public Llibre(String id, String titol, ArrayList<String> autors, String editorial, String dataPublicacio, String descripcio, int numPag, String urlImatge) {
        this.id = id;
        this.titol = titol;
        this.autors = autors;
        this.editorial = editorial;
        this.dataPublicacio = dataPublicacio;
        this.descripcio = descripcio;
        this.numPag = numPag;
        this.urlImatge = urlImatge;
    }

    protected Llibre(Parcel in) {
        id = in.readString();
        titol = in.readString();
        autors = in.createStringArrayList();
        editorial = in.readString();
        dataPublicacio = in.readString();
        descripcio = in.readString();
        numPag = in.readInt();
        urlImatge = in.readString();
    }

    public static final Creator<Llibre> CREATOR = new Creator<Llibre>() {
        @Override
        public Llibre createFromParcel(Parcel in) {
            return new Llibre(in);
        }

        @Override
        public Llibre[] newArray(int size) {
            return new Llibre[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitol() {
        return titol;
    }

    public void setTitol(String titol) {
        this.titol = titol;
    }

    public ArrayList<String> getAutors() {
        return autors;
    }

    public void setAutors(ArrayList<String> autors) {
        this.autors = autors;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getDataPublicacio() {
        return dataPublicacio;
    }

    public void setDataPublicacio(String dataPublicacio) {
        this.dataPublicacio = dataPublicacio;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public int getNumPag() {
        return numPag;
    }

    public void setNumPag(int numPag) {
        this.numPag = numPag;
    }

    public String getUrlImatge() {
        return urlImatge;
    }

    public void setUrlImatge(String urlImatge) {
        this.urlImatge = urlImatge;
    }

    public Bitmap getbPortada() {
        return bPortada;
    }

    public void setbPortada(Bitmap bPortada) {
        this.bPortada = bPortada;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(titol);
        parcel.writeStringList(autors);
        parcel.writeString(editorial);
        parcel.writeString(dataPublicacio);
        parcel.writeString(descripcio);
        parcel.writeInt(numPag);
        parcel.writeString(urlImatge);
    }
}