package org.unice.mbds.tp1.tpandroid.utils;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Nicolas on 09/12/2015.
 */
public class ApiCallService {

    private static ApiCallService instance = null;

    public static ApiCallService getInstance() {
        if (instance == null) {
            synchronized (ApiCallService.class) {
                if (instance == null) {
                    instance = new ApiCallService();
                }
            }
        }

        return instance;
    }

    public String doGet(String url) {
        try {

            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(url);

            // add header
            get.setHeader("Content-Type", "application/json");

            HttpResponse response = client.execute(get);
            Log.w("Sending :", "\nSending 'GET' request to URL : " + url);
            Log.w("Params :", "GET parameters : " + response.getEntity());
            Log.w("Response :", "Response Code : " +
                    response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            return result.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String doDelete(String url) {

        try {
            HttpClient client = new DefaultHttpClient();
            HttpDelete delete = new HttpDelete(url);

            // add header
            delete.setHeader("Content-Type", "application/json");

            Log.w("Sending :", "Sending 'DELETE' request to URL : " + url);
            HttpResponse response = client.execute(delete);
            Log.w("Response :", "Response Code : " +
                    response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            return result.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String doPost(String url, HashMap<String, String> params) {
        try {
            // Simulate network access.
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);

            // add header
            post.setHeader("Content-Type", "application/json");
            JSONObject obj = new JSONObject();

            Log.w("URL : ", url);

            Iterator it = params.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> pair = (Map.Entry)it.next();

                Log.w("KEY / VALUE : ", pair.getKey() + " " + pair.getValue());

                obj.put(pair.getKey(), pair.getValue());
                it.remove();
            }

            StringEntity entity = new StringEntity(obj.toString());
            post.setEntity(entity);

            HttpResponse response = client.execute(post);
            Log.w("Sending :", "\nSending 'POST' request to URL : " + url);
            Log.w("Params :", "Post parameters : " + post.getEntity());
            Log.w("Response :", "Response Code : " +
                    response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            return result.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
