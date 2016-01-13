package org.unice.mbds.tp1.tpandroid.object;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nicolas on 13/01/2016.
 */
public class UserManager {

    private static User userInstance;

    public static User getUser() {
        return userInstance;
    }

    public static void setUserInstance(JSONObject user) {
        Log.w("Object json : ", user.toString());

        // Should never be something different than null at this point
        if(userInstance == null) {
            try {
                String id = user.getString("id");
                String nom = user.getString("nom");
                String prenom = user.getString("prenom");

                userInstance = new User(nom, prenom, id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void logOut() {
        if(userInstance != null)
            userInstance = null;
    }
}
