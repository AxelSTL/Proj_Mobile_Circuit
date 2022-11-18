package com.example.book2run.model;

public class Circuit {
    int code;
    String nom;
    String adresse;
    String description;

    public Circuit(int code, String nom, String adresse, String description) {
        this.code = code;
        this.nom = nom;
        this.adresse = adresse;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
