package org.unice.mbds.tp1.tpandroid.utils;

import android.app.Activity;
import android.app.ProgressDialog;

import com.androidquery.AQuery;

import java.util.HashMap;
import java.util.Map;

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

    public void doGet(Activity caller, ProgressDialog progress, String url) {
        execute(caller, progress, url, AQuery.METHOD_GET, null);
    }

    public void doDelete(Activity caller, ProgressDialog progress, String url) {
        execute(caller, progress, url, AQuery.METHOD_DELETE, null);
    }

    public void doPost(Activity caller, ProgressDialog progress, String url, HashMap<String, Object> params) {
        execute(caller, progress, url, AQuery.METHOD_POST, params);
    }

    private void execute(Activity caller, ProgressDialog progress, String url, int method, Map<String, Object> params) {
        aq = new AQuery(caller);

        aq.progress(progress);

        if (method == AQuery.METHOD_POST) {
            aq.ajax(url, params, String.class, caller, "ajaxCallback");
        } else {
            aq.ajax(url, String.class, caller, "ajaxCallback");
        }
    }
}