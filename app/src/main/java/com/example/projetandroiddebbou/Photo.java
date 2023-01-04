package com.example.projetandroiddebbou;

import java.io.Serializable;

public class Photo implements Serializable {

    private String cheminPhoto;
    private double latitude;
    private double longitude;
    private String groupe;
    private String nom;


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCheminPhoto() {
        return cheminPhoto;
    }

    public void setCheminPhoto(String cheminPhoto) {
        this.cheminPhoto = cheminPhoto;
    }

    public String getGroupe() {
        return groupe;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


    public Photo()  {

    }

    public Photo(String cheminPhoto, double latitude,double longitude,String groupe,String nom) {
        this.cheminPhoto = cheminPhoto;
        this.latitude= latitude;
        this.longitude= longitude;
        this.groupe=groupe;
        this.nom=nom;
    }



    @Override
    public String toString()  {
        return this.cheminPhoto;
    }


}
