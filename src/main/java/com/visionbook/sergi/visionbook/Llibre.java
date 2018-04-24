package com.visionbook.sergi.visionbook;


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
    private String isbn;
    private int numPag;
    private String categoria;
    private String urlImatge;

    public Llibre() {

    }

    public Llibre(String id, String titol, ArrayList<String> autors, String editorial, String dataPublicacio, String descripcio, String isbn, int numPag, String categoria, String urlImatge) {
        this.id = id;
        this.titol = titol;
        this.autors = autors;
        this.editorial = editorial;
        this.dataPublicacio = dataPublicacio;
        this.descripcio = descripcio;
        this.isbn = isbn;
        this.numPag = numPag;
        this.categoria = categoria;
        this.urlImatge = urlImatge;
    }

    protected Llibre(Parcel in) {
        id = in.readString();
        titol = in.readString();
        autors = in.createStringArrayList();
        editorial = in.readString();
        dataPublicacio = in.readString();
        descripcio = in.readString();
        isbn = in.readString();
        numPag = in.readInt();
        categoria = in.readString();
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getNumPag() {
        return numPag;
    }

    public void setNumPag(int numPag) {
        this.numPag = numPag;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUrlImatge() {
        return urlImatge;
    }

    public void setUrlImatge(String urlImatge) {
        this.urlImatge = urlImatge;
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
        parcel.writeString(isbn);
        parcel.writeInt(numPag);
        parcel.writeString(categoria);
        parcel.writeString(urlImatge);
    }
}