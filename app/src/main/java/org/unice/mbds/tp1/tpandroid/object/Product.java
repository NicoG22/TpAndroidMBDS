package org.unice.mbds.tp1.tpandroid.object;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Gael on 10/12/2015.
 */
public class Product {
    private String id;
    private String nom;
    private String description;
    private double prix;
    private int calories;
    private String img;
    private int discount;
    private String type;

    // Constructor to convert JSON object into a Java class instance
    public Product(JSONObject object) {

        Log.w("Object json : ", object.toString());

        try {
            this.id = object.getString("id");
            this.nom = object.getString("name");
            this.description = object.getString("description");
            this.prix = Double.parseDouble(object.getString("price"));
            this.calories = Integer.parseInt(object.getString("calories"));
            this.img = object.getString("picture");
            this.discount = Integer.parseInt(object.getString("discount"));
            this.type = object.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<Product> fromJson(JSONArray jsonObjects) {
        ArrayList<Product> products = new ArrayList<>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                products.add(new Product(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return products;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
