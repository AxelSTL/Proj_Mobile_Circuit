package com.example.book2run.model;

public class Circuit {
    int code;
    String nom;
    String adresse;
    String description;
    String ville;
    int codePostal;
    int price;
    String mainImg;
    String dateDebut;
    String dateFin;
    int codeResa;
    boolean fav;

/*String image2;
    String image3;
    String image4;*/

    public Circuit(int code, String nom, String adresse, String description, String ville, int codePostal, String mainImg, int price, boolean fav) {
        this.code = code;
        this.nom = nom;
        this.adresse = adresse;
        this.description = description;
        this.ville = ville;
        this.codePostal = codePostal;
        this.mainImg = mainImg;
        this.price = price;
        this.fav = fav;
    }

    public Circuit(int code, String nom, String adresse, String description, String mainImg, int price, String dateDebut, String dateFin, int codeResa) {
        this.code = code;
        this.nom = nom;
        this.adresse = adresse;
        this.description = description;
        this.mainImg = mainImg;
        this.price = price;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.codeResa = codeResa;
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

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public int getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }

    public String getMainImg() {
        return mainImg;
    }

    public void setMainImg(String mainImg) {
        this.mainImg = mainImg;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public int getCodeResa() {
        return codeResa;
    }

    public void setCodeResa(int codeResa) {
        this.codeResa = codeResa;
    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }
}
