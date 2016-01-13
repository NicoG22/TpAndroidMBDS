package org.unice.mbds.tp1.tpandroid.object;

/**
 * Created by Nicolas on 13/01/2016.
 */
public class User {
    private String nom;
    private String prenom;
    private String id;

    public User(String nom, String prenom, String id) {
        this.nom = nom;
        this.id = id;
        this.prenom = prenom;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }
}
