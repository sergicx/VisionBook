package com.visionbook.sergi.visionbook.entitats;


public class Comentari {
    private String nomUsuari;
    private String comentari;
    private String timeStamp;

    public Comentari(String nomUsuari, String comentari, String timeStamp) {
        this.nomUsuari = nomUsuari;
        this.comentari = comentari;
        this.timeStamp = timeStamp;
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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
