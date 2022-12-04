package com.example.book2run.model;

public class Circuit {
    int code;
    String nom;
    String adresse;
    String description;
    int price;
    String mainImg;
    String dateDebut;
    String dateFin;

/*String image2;
    String image3;
    String image4;*/

    public Circuit(int code, String nom, String adresse, String description, String mainImg, int price) {
        this.code = code;
        this.nom = nom;
        this.adresse = adresse;
        this.description = description;
        this.mainImg = mainImg;
        this.price = price;
    }

    public Circuit(int code, String nom, String adresse, String description, String mainImg, int price, String dateDebut, String dateFin) {
        this.code = code;
        this.nom = nom;
        this.adresse = adresse;
        this.description = description;
        this.mainImg = mainImg;
        this.price = price;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
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

    /*public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }*/

}
