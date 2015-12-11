package org.unice.mbds.tp1.tpandroid.utils;

import android.app.Activity;
import android.app.ProgressDialog;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;

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