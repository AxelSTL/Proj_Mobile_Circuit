package com.example.book2run.model;

public class Commentary {
    String nom;
    int etoiles;
    String message;

    public Commentary(String nom, int etoiles, String message) {
        this.nom = nom;
        this.etoiles = etoiles;
        this.message = message;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
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
