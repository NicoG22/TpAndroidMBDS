package org.unice.mbds.tp1.tpandroid.object;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas on 13/01/2016.
 */
public class User {
    private String nom;
    private String prenom;
    private String id;
    private static List<Product> order = new ArrayList<>();

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

    public List<Product> getOrder() {
        return order;
    }
}
