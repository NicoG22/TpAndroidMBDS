package org.unice.mbds.tp1.tpandroid.object;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nicolas on 30/10/2015.
 */
public class Person {
    private String id;
    private String nom;
    private String prenom;
    private String sexe;
    private String email;
    private String telephone;
    private boolean connected;

    // Constructor to convert JSON object into a Java class instance
    public Person(JSONObject object){

        Log.w("Object json : ", object.toString());

        try {
            this.id = object.getString("id");
            this.nom = object.getString("nom");
            this.prenom = object.getString("prenom");
            this.sexe = object.getString("sexe");
            this.email = object.getString("email");
            this.telephone = object.getString("telephone");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<Person> fromJson(JSONArray jsonObjects) {
        ArrayList<Person> persons = new ArrayList<>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                persons.add(new Person(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return persons;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String etat() {
        if(connected) {
            return "Connecte";
        } else {
            return "Deconnecte";
        }
    }
}
