package com.visionbook.sergi.visionbook.entitats;


public class Comentari {
    private String nomUsuari;
    private String comentari;

    public Comentari(String nomUsuari, String comentari) {
        this.nomUsuari = nomUsuari;
        this.comentari = comentari;
    }

    public String getNomUsuari() {
        return nomUsuari;
    }

    public void setNomUsuari(String nomUsuari) {
        this.nomUsuari = nomUsuari;
    }

    public String getComentari() {
        return comentari;
    }

    public void setComentari(String comentari) {
        this.comentari = comentari;
    }
}
