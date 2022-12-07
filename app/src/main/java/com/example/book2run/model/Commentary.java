package com.example.book2run.model;

public class Commentary {
    String pseudo;
    int etoiles;
    String message;

    public Commentary(String pseudo, int etoiles, String message) {
        this.pseudo = pseudo;
        this.etoiles = etoiles;
        this.message = message;
    }

    public String getNom() {
        return pseudo;
    }

    public void setNom(String nom) {
        this.pseudo = nom;
    }

    public int getEtoiles() {
        return etoiles;
    }

    public void setEtoiles(int etoiles) {
        this.etoiles = etoiles;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
