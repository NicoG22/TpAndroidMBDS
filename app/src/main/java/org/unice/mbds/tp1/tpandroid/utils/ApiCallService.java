package org.unice.mbds.tp1.tpandroid.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.security.auth.callback.Callback;

/**
 * Created by Nicolas on 09/12/2015.
 */
public class ApiCallService {

    private static ApiCallService instance = null;
    private AQuery aq;
    private String result;
    private boolean ready = false;

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

    public AjaxCallback<String> doGet(Activity caller, ProgressDialog progress, String url) {
        return execute(caller, progress, url, AQuery.METHOD_GET, null);
    }

    public AjaxCallback<String> doDelete(Activity caller, ProgressDialog progress, String url) {
        return execute(caller, progress, url, AQuery.METHOD_DELETE, null);
    }

    public AjaxCallback<String> doPost(Activity caller, ProgressDialog progress, String url, HashMap<String, Object> params) {
        return execute(caller, progress, url, AQuery.METHOD_POST, params);
    }

    private AjaxCallback<String> execute(Activity caller, ProgressDialog progress, String url, int method, Map<String, Object> params) {
        aq = new AQuery(caller);

        AjaxCallback<String> cb = new AjaxCallback<>();
        cb.url(url).type(String.class).method(method);

        if (method == AQuery.METHOD_POST) {
            cb.params(params);
        }

        aq.progress(progress).sync(cb);

        return cb;
    }
}